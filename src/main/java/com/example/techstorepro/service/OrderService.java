package com.example.techstorepro.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.techstorepro.dto.request.OrderRequest;
import com.example.techstorepro.dto.request.OrderStatusRequest;
import com.example.techstorepro.dto.response.OrderResponse;
import com.example.techstorepro.entity.Cart;
import com.example.techstorepro.entity.CartItem;
import com.example.techstorepro.entity.Order;
import com.example.techstorepro.entity.OrderDetail;
import com.example.techstorepro.entity.Product;
import com.example.techstorepro.enums.OrderState;
import com.example.techstorepro.exception.BadRequestException;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.mapper.OrderMapper;
import com.example.techstorepro.repository.CartRepository;
import com.example.techstorepro.repository.OrderRepository;
import com.example.techstorepro.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    public Page<OrderResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::toResponse);
    }

    public OrderResponse findById(UUID id) {
        return OrderMapper.toResponse(getOrder(id));
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        Cart cart = cartRepository.findByUser_Id(request.getUserId())
                .orElseThrow(() -> new BadRequestException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setStatus(OrderState.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (!product.getActive()) {
                throw new BadRequestException("Product '" + product.getName() + "' is currently unavailable");
            }

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product '" + product.getName()
                        + "'. Available stock: " + product.getStock() + ", Requested: " + cartItem.getQuantity());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setProductName(product.getName());
            detail.setQuantity(cartItem.getQuantity());
            detail.setUnitPrice(unitPrice);
            detail.setSubtotal(subtotal);

            order.getItems().add(detail);
            total = total.add(subtotal);
        }

        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        cartService.clear(request.getUserId());
        return OrderMapper.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, OrderStatusRequest request) {
        OrderState newStatus = request.getStatus();

        if (newStatus == null) {
            throw new BadRequestException("Status is required");
        }

        Order order = getOrder(id);
        OrderState currentStatus = order.getStatus();

        validateStateTransition(currentStatus, newStatus);

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toResponse(updatedOrder);
    }

    private void validateStateTransition(OrderState current, OrderState next) {
        if (current == next) {
            return;
        }

        boolean isValid = switch (current) {
            case PENDING -> next == OrderState.PREPARING;
            case PREPARING -> next == OrderState.DELIVERED;
            case DELIVERED -> false;
        };

        if (!isValid) {
            throw new BadRequestException("Invalid status transition: cannot change from " + current + " to " + next
                    + ". Allowed flow: PENDING -> PREPARING -> DELIVERED");
        }
    }

    private Order getOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }
}
