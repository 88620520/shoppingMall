package com.example.shoppingmall.strategy.impl;

import com.example.shoppingmall.entity.PurchaseRequest;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.strategy.ApprovalStrategy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ManagerApprovalStrategy implements ApprovalStrategy {
    @Override
    public boolean canApprove(PurchaseRequest request, User approver) {
        return "MANAGER".equals(approver.getRole().getName()) 
            && request.getQuantity() <= 500;
    }

    @Override
    public void approve(PurchaseRequest request, User approver) {
        request.setStatus("APPROVED");
        request.setApprover(approver);
        request.setApprovalDate(LocalDateTime.now());
    }
} 