package com.example.softdevoluciones.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.softdevoluciones.dto.request.OrderRequest;
import com.example.softdevoluciones.dto.request.OrderStatusRequest;
import com.example.softdevoluciones.dto.response.OrderResponse;
import com.example.softdevoluciones.entity.Cart;
import com.example.softdevoluciones.entity.CartItem;
import com.example.softdevoluciones.entity.Order;
import com.example.softdevoluciones.entity.OrderDetail;
import com.example.softdevoluciones.entity.Product;
import com.example.softdevoluciones.entity.User;
import com.example.softdevoluciones.enums.OrderState;
import com.example.softdevoluciones.enums.UserRole;
import com.example.softdevoluciones.exception.BadRequestException;
import com.example.softdevoluciones.exception.ResourceNotFoundException;
import com.example.softdevoluciones.mapper.OrderMapper;
import com.example.softdevoluciones.repository.CartRepository;
import com.example.softdevoluciones.repository.OrderRepository;
import com.example.softdevoluciones.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    public Page<OrderResponse> findAllFor(User user, Pageable pageable) {
        if (user.getRole() == UserRole.ADMIN) {
            return orderRepository.findAll(pageable)
                    .map(OrderMapper::toResponse);
        }
        return orderRepository.findByUser_Id(user.getId(), pageable)
                .map(OrderMapper::toResponse);
    }

    public OrderResponse findByIdFor(User user, UUID id) {
        Order order = getOrder(id);
        if (user.getRole() != UserRole.ADMIN && !order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException(
                    "No tienes permiso para ver este pedido porque pertenece a otro usuario. Si crees que se trata de un error, contacta al soporte.");
        }
        return OrderMapper.toResponse(order);
    }

    @Transactional
    public OrderResponse create(UUID userId, OrderRequest request) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new BadRequestException(
                        "Tu carrito está vacío. Por favor, agrega al menos un producto al carrito antes de crear el pedido."));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException(
                    "Tu carrito está vacío. Por favor, agrega al menos un producto al carrito antes de crear el pedido.");
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
                throw new BadRequestException("El producto '" + product.getName()
                        + "' no está disponible actualmente. Por favor, retíralo del carrito e inténtalo de nuevo con otros productos.");
            }

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("No hay stock suficiente para el producto '" + product.getName()
                        + "'. Stock disponible: " + product.getStock() + ", cantidad solicitada: "
                        + cartItem.getQuantity()
                        + ". Por favor, ajusta la cantidad en tu carrito e inténtalo de nuevo.");
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
        cartService.clear(userId);
        return OrderMapper.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateStatus(UUID id, OrderStatusRequest request) {
        OrderState newStatus = request.getStatus();

        if (newStatus == null) {
            throw new BadRequestException(
                    "El estado del pedido es obligatorio. Por favor, indica el nuevo estado que deseas asignar al pedido.");
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
            throw new BadRequestException("No es posible cambiar el estado del pedido de " + current + " a " + next
                    + ". Por favor, respeta el flujo permitido: PENDIENTE → EN_PREPARACIÓN → ENTREGADO.");
        }
    }

    private Order getOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El pedido solicitado no existe o fue eliminado. Por favor, verifica la información e inténtalo de nuevo."));
    }
}
