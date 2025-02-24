package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.AddressDto;
import java.util.List;

public interface AddressService {
    List<AddressDto> getAddressesByUserId(String userId);
    AddressDto addAddress(String userId, AddressDto addressDto);
    AddressDto updateAddress(Long id, AddressDto addressDto);
    void deleteAddress(Long id);
    AddressDto getAddressById(Long id);
}