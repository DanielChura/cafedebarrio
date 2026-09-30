package com.example.techstorepro.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.example.techstorepro.dto.request.ReturnCreateRequest;
import com.example.techstorepro.dto.request.ReturnDetailRequest;
import com.example.techstorepro.entity.Order;
import com.example.techstorepro.entity.OrderDetail;
import com.example.techstorepro.entity.ReturnDetail;
import com.example.techstorepro.entity.ReturnRequest;
import com.example.techstorepro.entity.User;
import com.example.techstorepro.enums.ReturnStatus;
import com.example.techstorepro.exception.BadRequestException;
import com.example.techstorepro.exception.ResourceNotFoundException;
import com.example.techstorepro.repository.OrderRepository;
import com.example.techstorepro.repository.ReturnDetailRepository;
import com.example.techstorepro.repository.ReturnRequestRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReturnRequestService {
    private final ReturnRequestRepository returnRequestRepository;
    private final OrderRepository orderRepository;
    private final ReturnDetailRepository returnDetailRepository;

    public ReturnRequest create(ReturnCreateRequest request, User user) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + request.getOrderId()));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("No tienes permiso para ver este pedido");
        }
        List<ReturnDetail> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setUser(user);
        returnRequest.setStatus(ReturnStatus.PENDING);
        returnRequest.setReason(request.getReason());
        returnRequest.setOrder(order);
        returnRequest.setOperatorNote(null);
        returnRequest.setComment(request.getComment());

        for (ReturnDetailRequest itemReq : request.getItems()) {
            OrderDetail orderDetail = order.getItems().stream()
                    .filter(item -> item.getId().equals(itemReq.getOrderDetailId())).findFirst()
                    .orElseThrow(() -> new BadRequestException(
                            "El producto con ID de detalle " + itemReq.getOrderDetailId()
                                    + " no pertenece a esta orden"));

            if (itemReq.getQuantity() > orderDetail.getQuantity()) {
                throw new BadRequestException(
                        "Cantidad no válida para el producto " + orderDetail.getProduct().getName() +
                                ". Disponibles para devolución: " + orderDetail.getQuantity());
            }

            BigDecimal unitPrice = orderDetail.getUnitPrice();
            Integer prevQuantity = returnDetailRepository.findTotalQuantityByOrderDetailId(orderDetail.getId());
            BigDecimal prevAmount = unitPrice.multiply(BigDecimal.valueOf(prevQuantity));

            BigDecimal amount = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity())).add(prevAmount);
            Integer quantity = prevQuantity + itemReq.getQuantity();

            totalAmount.add(amount);

            ReturnDetail returnDetail = new ReturnDetail();
            returnDetail.setAmount(amount);
            returnDetail.setQuantity(itemReq.getQuantity());
            returnDetail.setRequest(returnRequest);
            returnDetail.setOrderDetail(orderDetail);
            returnDetail.setQuantity(quantity);
            returnDetailRepository.save(returnDetail);
        }
        returnRequest.setItems(items);
        returnRequest.setAmount(totalAmount);
        return returnRequestRepository.save(returnRequest);
    }
}
