package com.example.techstorepro.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.techstorepro.dto.request.OrderItemRequest;
import com.example.techstorepro.dto.request.OrderRequest;
import com.example.techstorepro.dto.request.OrderStatusRequest;
import com.example.techstorepro.dto.response.OrderResponse;
import com.example.techstorepro.entity.Order;
import com.example.techstorepro.entity.OrderDetail;
import com.example.techstorepro.entity.Product;
import com.example.techstorepro.entity.User;
import com.example.techstorepro.enums.OrderState;
import com.example.techstorepro.exception.BadRequestException;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.mapper.OrderMapper;
import com.example.techstorepro.repository.OrderRepository;
import com.example.techstorepro.repository.ProductRepository;
import com.example.techstorepro.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Page<OrderResponse> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::toResponse);
    }

    public OrderResponse findById(UUID id) {
        return OrderMapper.toResponse(findOrderByIdOrThrow(id));
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        boolean noItems = request.getItems() == null || request.getItems().isEmpty();

        if (noItems) {
            throw new BadRequestException("Order must contain at least one product");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setPhone(request.getPhone());
        order.setAddress(request.getAddress());
        order.setStatus(OrderState.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with ID: " + itemRequest.getProductId()));

            if (!product.getActive()) {
                throw new BadRequestException("Product '" + product.getName() + "' is currently unavailable");
            }

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new BadRequestException(String.format(
                        "Insufficient stock for product '" + product.getName() + "'. Available stock: "
                                + product.getStock() + ", Requested: " + itemRequest.getQuantity()));
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setProductName(product.getName());
            detail.setQuantity(itemRequest.getQuantity());
            detail.setUnitPrice(unitPrice);
            detail.setSubtotal(subtotal);

            order.getItems().add(detail);
            total = total.add(subtotal);
        }

        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, OrderStatusRequest request) {
        OrderState newStatus = request.getStatus();

        if (newStatus == null) {
            throw new BadRequestException("Status is required");
        }

        Order order = findOrderByIdOrThrow(id);
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

    private Order findOrderByIdOrThrow(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
    }
}
