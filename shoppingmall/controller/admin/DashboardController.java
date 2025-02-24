package com.example.shoppingmall.controller.admin;

import com.example.shoppingmall.service.OrderService;
import com.example.shoppingmall.service.ProductService;
import com.example.shoppingmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasAnyRole('STAFF', 'MANAGER', 'BOSS')")
public class DashboardController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String dashboard(Model model) {
        // 添加仪表盘数据
        model.addAttribute("todayOrders", orderService.getTodayOrderCount());
        model.addAttribute("lowStockCount", productService.getLowStockCount());
        model.addAttribute("staffCount", userService.getAllStaff().size());
        return "admin/dashboard";
    }
} 