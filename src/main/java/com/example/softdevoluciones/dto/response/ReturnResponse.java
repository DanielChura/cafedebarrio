package com.example.softdevoluciones.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.softdevoluciones.enums.ReturnStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResponse {

    private UUID id;
    private UUID orderId;
    private UUID userId;
    private ReturnStatus status;
    private String reason;
    private String comment;
    private String operatorNote;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private List<ReturnDetailResponse> items;
}
