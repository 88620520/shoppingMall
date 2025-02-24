package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.dto.AddressDto;
import com.example.shoppingmall.entity.Address;
import com.example.shoppingmall.repository.AddressRepository;
import com.example.shoppingmall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddressesByUserId(String userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDto addAddress(String userId, AddressDto addressDto) {
        Address address = new Address();
        address.setUserId(userId);
        address.setRecipientName(addressDto.getRecipientName());
        address.setPhoneNumber(addressDto.getPhoneNumber());
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry("中国");
        address = addressRepository.save(address);
        return convertToDto(address);
    }

    @Override
    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("地址不存在"));
        address.setRecipientName(addressDto.getRecipientName());
        address.setPhoneNumber(addressDto.getPhoneNumber());
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address = addressRepository.save(address);
        return convertToDto(address);
    }

    @Override
    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public AddressDto getAddressById(Long id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("地址不存在"));
        return convertToDto(address);
    }

    private AddressDto convertToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setRecipientName(address.getRecipientName());
        dto.setPhoneNumber(address.getPhoneNumber());
        dto.setAddressLine(address.getAddressLine());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        return dto;
    }
}