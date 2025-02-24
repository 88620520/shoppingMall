// PurchaseRequestServiceImpl 实现了 PurchaseRequestService 接口
package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.dto.PurchaseRequestDto;
import com.example.shoppingmall.duty.Boss;
import com.example.shoppingmall.entity.PurchaseRequest;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.entity.Product;
import com.example.shoppingmall.repository.PurchaseRequestRepository;
import com.example.shoppingmall.repository.ProductRepository;
import com.example.shoppingmall.repository.UserRepository;
import com.example.shoppingmall.service.PurchaseRequestService;
import com.example.shoppingmall.duty.Handler;
import com.example.shoppingmall.duty.InventoryManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PurchaseRequestServiceImpl implements PurchaseRequestService {

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // 创建责任链
    private Handler createApprovalChain() {
        Handler inventoryManager = new InventoryManager();  // 库存经理
        Handler boss = new Boss();  // 老板

        inventoryManager.setNextHandler(boss);  // 链接责任链
        return inventoryManager;
    }

    @Override
    @Transactional
    public PurchaseRequestDto createRequest(PurchaseRequestDto requestDto, String username) {
        try {
            User requester = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            if (!"STAFF".equals(requester.getRole().getName())) {
                throw new RuntimeException("只有店员可以发起进货申请");
            }

            PurchaseRequest request = new PurchaseRequest();
            request.setProduct(productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("商品不存在")));
            request.setQuantity(requestDto.getQuantity());
            request.setRequester(requester);
            request.setStatus("PENDING");
            request.setRequestDate(LocalDateTime.now());

            request = purchaseRequestRepository.save(request);
            return convertToDto(request);
        } catch (Exception e) {
            throw new RuntimeException("创建进货申请失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PurchaseRequestDto approveRequest(Long id, String username, boolean approved, String comment) {
        try {
            PurchaseRequest request = purchaseRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("申请不存在"));

            User approver = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            // 创建责任链
            Handler approvalChain = createApprovalChain();

            // 使用责任链来处理申请
            approvalChain.processRequest(request, approver);

            request.setApprover(approver);
            request.setApprovalDate(LocalDateTime.now());
            request.setComment(comment);
            request.setStatus(approved ? "APPROVED" : "REJECTED");

            if (approved) {
                Product product = request.getProduct();
                product.setStock(product.getStock() + request.getQuantity());
                productRepository.save(product);
            }

            request = purchaseRequestRepository.save(request);
            return convertToDto(request);
        } catch (Exception e) {
            throw new RuntimeException("处理进货申请失败: " + e.getMessage());
        }
    }

    @Override
    public PurchaseRequestDto approveRequest(Long id, String username) {
        // 默认处理，假设默认批准
        return approveRequest(id, username, true, "默认批准");
    }

    @Override
    public List<PurchaseRequestDto> getPendingRequests(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            String role = user.getRole().getName();
            List<PurchaseRequest> requests;

            if ("BOSS".equals(role)) {
                requests = purchaseRequestRepository.findByStatus("PENDING");
            } else if ("MANAGER".equals(role)) {
                requests = purchaseRequestRepository.findByStatusAndQuantityLessThanEqual("PENDING", 500);
            } else {
                requests = new ArrayList<>();
            }

            return requests.stream()
                    .distinct()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取待审批申请失败: " + e.getMessage());
        }
    }

    @Override
    public List<PurchaseRequestDto> getMyRequests(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            List<PurchaseRequest> requests;
            String role = user.getRole().getName();

            if ("BOSS".equals(role)) {
                requests = purchaseRequestRepository.findAll();
            } else if ("MANAGER".equals(role)) {
                List<PurchaseRequest> approvedByMe = purchaseRequestRepository.findByApprover_Id(user.getId());
                List<PurchaseRequest> pendingSmall = purchaseRequestRepository.findByStatusAndQuantityLessThanEqual("PENDING", 500);
                requests = new ArrayList<>();
                requests.addAll(approvedByMe);
                requests.addAll(pendingSmall);
            } else if ("STAFF".equals(role)) {
                requests = purchaseRequestRepository.findByRequester_Id(user.getId());
            } else {
                requests = new ArrayList<>();
            }

            return requests.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("获取申请记录失败: " + e.getMessage());
        }
    }

    private PurchaseRequestDto convertToDto(PurchaseRequest request) {
        PurchaseRequestDto dto = new PurchaseRequestDto();
        dto.setId(request.getId());
        dto.setProductId(request.getProduct().getId());
        dto.setProductName(request.getProduct().getName());
        dto.setQuantity(request.getQuantity());
        dto.setStatus(request.getStatus());
        dto.setRequesterName(request.getRequester().getUsername());
        dto.setApproverName(request.getApprover() != null ? request.getApprover().getUsername() : null);
        dto.setRequestDate(request.getRequestDate());
        dto.setApprovalDate(request.getApprovalDate());
        dto.setComment(request.getComment());
        return dto;
    }
}
