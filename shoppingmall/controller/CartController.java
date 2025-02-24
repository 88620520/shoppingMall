package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.CartItemDto;
import com.example.shoppingmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("cartItems", cartService.getCartByUserId(userDetails.getUsername()));
        model.addAttribute("total", cartService.calculateTotal(userDetails.getUsername()));
        return "cart/index";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                          @RequestParam(defaultValue = "1") Integer quantity,
                          @AuthenticationPrincipal UserDetails userDetails,
                          RedirectAttributes redirectAttributes) {
        try {
            CartItemDto cartItem = new CartItemDto();
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(userDetails.getUsername());
            cartService.addToCart(cartItem);
            redirectAttributes.addFlashAttribute("success", "商品已成功加入购物车！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "添加商品失败：" + e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(@RequestParam Long itemId,
                           @RequestParam Integer quantity,
                           RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(itemId, quantity);
            redirectAttributes.addFlashAttribute("success", "购物车已更新！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败：" + e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long itemId,
                               RedirectAttributes redirectAttributes) {
        try {
            cartService.removeFromCart(itemId);
            redirectAttributes.addFlashAttribute("success", "商品已从购物车中移除！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "移除失败：" + e.getMessage());
        }
        return "redirect:/cart";
    }
} 