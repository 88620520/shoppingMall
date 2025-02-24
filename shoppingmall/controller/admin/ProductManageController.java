package com.example.shoppingmall.controller.admin;

import com.example.shoppingmall.dto.ProductDto;
import com.example.shoppingmall.service.FileService;
import com.example.shoppingmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class ProductManageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products/list";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDto productDto,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           RedirectAttributes redirectAttributes) {
        try {
            if (!imageFile.isEmpty()) {
                String imageUrl = fileService.uploadFile(imageFile);
                productDto.setImageUrl(imageUrl);
            }
            
            productService.createProduct(productDto);
            redirectAttributes.addFlashAttribute("success", "商品添加成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "添加失败：" + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/update")
    public String updateProduct(@PathVariable Long id,
                              @ModelAttribute ProductDto productDto,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            // 如果上传了新图片，先删除旧图片
            if (imageFile != null && !imageFile.isEmpty()) {
                ProductDto oldProduct = productService.getProductById(id);
                if (oldProduct.getImageUrl() != null) {
                    fileService.deleteFile(oldProduct.getImageUrl());
                }
                String imageUrl = fileService.uploadFile(imageFile);
                productDto.setImageUrl(imageUrl);
            }
            
            productService.updateProduct(id, productDto);
            redirectAttributes.addFlashAttribute("success", "商品更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败：" + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        try {
            // 删除商品前先删除图片
            ProductDto product = productService.getProductById(id);
            if (product.getImageUrl() != null) {
                fileService.deleteFile(product.getImageUrl());
            }
            
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "商品删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败：" + e.getMessage());
        }
        return "redirect:/admin/products";
    }
} 