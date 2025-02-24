package com.example.shoppingmall.strategy;

import com.example.shoppingmall.entity.PurchaseRequest;
import com.example.shoppingmall.entity.User;

public interface ApprovalStrategy {
    boolean canApprove(PurchaseRequest request, User approver);
    void approve(PurchaseRequest request, User approver);
} 