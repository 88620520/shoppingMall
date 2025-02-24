package com.example.shoppingmall.controller.admin;

import com.example.shoppingmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/inventory")
@PreAuthorize("hasAnyRole('MANAGER', 'BOSS')")
public class InventoryController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listInventory(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/inventory/list";
    }

    @PostMapping("/update")
    public String updateStock(@RequestParam Long productId,
                            @RequestParam Integer stock,
                            RedirectAttributes redirectAttributes) {
        try {
            productService.updateStock(productId, stock);
            redirectAttributes.addFlashAttribute("success", "库存已更新");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败：" + e.getMessage());
        }
        return "redirect:/admin/inventory";
    }
} 