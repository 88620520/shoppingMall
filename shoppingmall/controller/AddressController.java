package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.AddressDto;
import com.example.shoppingmall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public String listAddresses(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        model.addAttribute("addresses", addressService.getAddressesByUserId(username));
        return "addresses/list";
    }

    @PostMapping("/add")
    public String addAddress(@AuthenticationPrincipal UserDetails userDetails,
                             @ModelAttribute AddressDto addressDto) {
        String username = userDetails.getUsername();
        addressService.addAddress(username, addressDto);
        return "redirect:/addresses";
    }

    @PostMapping("/{id}/update")
    public String updateAddress(@PathVariable Long id, @ModelAttribute AddressDto addressDto) {
        addressService.updateAddress(id, addressDto);
        return "redirect:/addresses";
    }

    @PostMapping("/{id}/delete")
    public String deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return "redirect:/addresses";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public AddressDto getAddress(@PathVariable Long id) {
        return addressService.getAddressById(id);
    }
}