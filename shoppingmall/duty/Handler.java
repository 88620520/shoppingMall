// Handler接口定义在 duty 包
package com.example.shoppingmall.duty;

import com.example.shoppingmall.entity.PurchaseRequest;
import com.example.shoppingmall.entity.User;

public interface Handler {
    void setNextHandler(Handler handler);
    void processRequest(PurchaseRequest request, User approver);
}
