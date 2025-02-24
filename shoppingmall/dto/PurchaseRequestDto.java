package com.example.shoppingmall.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PurchaseRequestDto {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private String status;
    private String requesterName;
    private String approverName;
    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;
    private String comment;
} 