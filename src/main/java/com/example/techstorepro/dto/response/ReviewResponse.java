package com.example.techstorepro.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private UUID id;
    private UUID userId;
    private String userName;
    private UUID productId;
    private String productName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
