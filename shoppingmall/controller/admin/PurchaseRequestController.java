package com.example.shoppingmall.controller.admin;

import com.example.shoppingmall.dto.PurchaseRequestDto;
import com.example.shoppingmall.service.PurchaseRequestService;
import com.example.shoppingmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/purchase")
public class PurchaseRequestController {

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String purchaseRequests(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("pendingRequests", purchaseRequestService.getPendingRequests(userDetails.getUsername()));
        model.addAttribute("myRequests", purchaseRequestService.getMyRequests(userDetails.getUsername()));
        model.addAttribute("products", productService.getAllProducts());
        return "admin/purchase/list";
    }

    @PostMapping("/create")
    public String createRequest(@ModelAttribute PurchaseRequestDto requestDto,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            purchaseRequestService.createRequest(requestDto, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("success", "进货申请已提交");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "提交失败：" + e.getMessage());
        }
        return "redirect:/admin/purchase";
    }

    @PostMapping("/{id}/approve")
    public String approveRequest(@PathVariable Long id,
                               @RequestParam boolean approved,
                               @RequestParam String comment,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            purchaseRequestService.approveRequest(id, userDetails.getUsername(), approved, comment);
            redirectAttributes.addFlashAttribute("success", 
                approved ? "进货申请已通过" : "进货申请已拒绝");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "处理失败：" + e.getMessage());
        }
        return "redirect:/admin/purchase";
    }
} 