package com.example.shoppingmall.controller.admin;

import com.example.shoppingmall.dto.StaffDto;
import com.example.shoppingmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/staff")
@PreAuthorize("hasRole('BOSS')")
public class StaffController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute("staffList", userService.getAllStaff());
        return "admin/staff/list";
    }

    @PostMapping("/add")
    public String addStaff(@ModelAttribute StaffDto staffDto,
                          RedirectAttributes redirectAttributes) {
        try {
            userService.addStaff(staffDto);
            redirectAttributes.addFlashAttribute("success", "员工添加成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "添加失败：" + e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @PostMapping("/{id}/update")
    public String updateStaff(@PathVariable Long id,
                            @ModelAttribute StaffDto staffDto,
                            RedirectAttributes redirectAttributes) {
        try {
            userService.updateStaff(id, staffDto);
            redirectAttributes.addFlashAttribute("success", "员工信息更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败：" + e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @PostMapping("/{id}/delete")
    public String deleteStaff(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        try {
            userService.deleteStaff(id);
            redirectAttributes.addFlashAttribute("success", "员工删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/admin/staff";
    }
} 