package com.example.shoppingmall.duty;

import com.example.shoppingmall.entity.PurchaseRequest;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.duty.Handler;  // 引用正确的接口包

public class InventoryManager implements Handler {

    private Handler nextHandler;

    @Override
    public void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }

    @Override
    public void processRequest(PurchaseRequest request, User approver) {
        if (request.getQuantity() <= 500) {
            System.out.println("库存经理审批通过：进货数量 " + request.getQuantity());
            request.setStatus("APPROVED");
        } else if (nextHandler != null) {
            nextHandler.processRequest(request, approver);
        } else {
            System.out.println("无法处理请求：库存经理无法处理大于500的进货请求");
        }
    }
}