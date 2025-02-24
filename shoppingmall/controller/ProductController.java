package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ProductDto;
import com.example.shoppingmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) Double minPrice,
                             @RequestParam(required = false) Double maxPrice,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
        int pageSize = 12;  // 每页显示12个商品
        List<ProductDto> products;
        
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProducts(keyword, page, pageSize);
            model.addAttribute("keyword", keyword);
        } else if (minPrice != null && maxPrice != null) {
            products = productService.searchByPrice(minPrice, maxPrice);
            model.addAttribute("minPrice", minPrice);
            model.addAttribute("maxPrice", maxPrice);
        } else {
            products = productService.getAllProducts(page, pageSize);
        }
        
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        return "products/list";
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "products/detail";
    }
} 