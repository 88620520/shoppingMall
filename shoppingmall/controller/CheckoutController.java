package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.CheckoutDto;
import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.service.CartService;
import com.example.shoppingmall.service.OrderService;
import com.example.shoppingmall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.shoppingmall.entity.Order;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;

    public Order convertDtoToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setUserId(orderDto.getUserId());
        order.setOrderDate(orderDto.getOrderDate());
        order.setStatus(orderDto.getStatus());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setTotal(orderDto.getTotal());
        // 根据需要填充其他字段
        return order;
    }


    @GetMapping
    public String checkout(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        if (cartService.getCartByUserId(username).isEmpty()) {
            model.addAttribute("error", "购物车为空，请先添加商品");
            return "redirect:/cart";
        }

        // 计算订单金额
        double subtotal = cartService.calculateTotal(username);
        double shipping = 10.00; // 固定运费
        double total = subtotal + shipping;

        // 添加到模型
        model.addAttribute("cartItems", cartService.getCartByUserId(username));
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shipping", shipping);
        model.addAttribute("total", total);
        model.addAttribute("checkoutDto", new CheckoutDto());
        model.addAttribute("addresses", addressService.getAddressesByUserId(username));
        
        return "checkout/index";
    }

    @PostMapping
    public String processCheckout(@ModelAttribute CheckoutDto checkoutDto,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  RedirectAttributes redirectAttributes) {
        String username = userDetails.getUsername();

        // 检查购物车是否为空
        if (cartService.getCartByUserId(username).isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "购物车为空，请先添加商品");
            return "redirect:/cart";
        }

        try {
            // 创建订单 DTO，包含基本订单信息
            OrderDto orderDto = orderService.createOrderFromCart(username, checkoutDto);

            // 将 OrderDto 转换为 Order 实体（假设有这样的转换逻辑）
            Order order = convertDtoToOrder(orderDto);

            // 清空购物车
            cartService.clearCart(username);

            // 添加成功消息
            redirectAttributes.addFlashAttribute("success", "订单创建成功！");

            // 在这里调用 placeOrder() 方法，通知所有观察者
            order.placeOrder();
            System.out.println("-------chuangjiang------");

            // 重定向到订单列表页面
            return "redirect:/orders";
        } catch (Exception e) {
            // 添加错误消息
            redirectAttributes.addFlashAttribute("error", "订单创建失败：" + e.getMessage());
            return "redirect:/checkout";
        }
    }


} 