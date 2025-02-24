package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("orders", orderService.getOrdersByUserId(username));
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, 
                         @AuthenticationPrincipal UserDetails userDetails,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        try {
            OrderDto order = orderService.getOrderById(id);
            // 检查订单是否属于当前用户
            if (!order.getUserId().equals(userDetails.getUsername())) {
                redirectAttributes.addFlashAttribute("error", "无权访问此订单");
                return "redirect:/orders";
            }
            model.addAttribute("order", order);
            return "orders/detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "订单不存在");
            return "redirect:/orders";
        }
    }

    // 修改这里的权限注解，添加 BOSS 角色
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'BOSS')")
    public String getOrderForAdmin(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            OrderDto order = orderService.getOrderById(id);
            model.addAttribute("order", order);
            return "orders/detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "订单不存在");
            return "redirect:/orders";
        }
    }
} 