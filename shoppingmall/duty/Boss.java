package com.example.shoppingmall.duty;

import com.example.shoppingmall.entity.PurchaseRequest;
import com.example.shoppingmall.entity.User;
import com.example.shoppingmall.duty.Handler;

public class Boss implements Handler {

    @Override
    public void setNextHandler(Handler handler) {
        // Boss 是链条的最后一个节点，不需要设置下一个处理者
    }

    @Override
    public void processRequest(PurchaseRequest request, User approver) {
        System.out.println("老板审批通过：进货数量 " + request.getQuantity());
        request.setStatus("APPROVED");
    }
}

