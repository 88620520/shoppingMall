package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.PurchaseRequestDto;
import java.util.List;

public interface PurchaseRequestService {
    PurchaseRequestDto createRequest(PurchaseRequestDto requestDto, String username);

    // 这个方法是你的接口中存在的，需要保持一致
    PurchaseRequestDto approveRequest(Long id, String username, boolean approved, String comment);

    // 如果你需要保留 approveRequest(Long, String)，你可以在实现类中进行重载
    PurchaseRequestDto approveRequest(Long id, String username);  // 添加无参数的重载版本

    List<PurchaseRequestDto> getPendingRequests(String username);
    List<PurchaseRequestDto> getMyRequests(String username);
}
