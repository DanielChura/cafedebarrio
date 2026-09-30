package com.example.techstorepro.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnDetailResponse {

    private UUID id;
    private UUID orderDetailId;
    private Integer quantity;
    private BigDecimal amount;
}
