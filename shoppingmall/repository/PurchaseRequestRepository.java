package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    List<PurchaseRequest> findByRequester_Id(Long requesterId);
    List<PurchaseRequest> findByStatus(String status);
    List<PurchaseRequest> findByApprover_Id(Long approverId);
    List<PurchaseRequest> findByStatusAndQuantityLessThanEqual(String status, int quantity);
} 