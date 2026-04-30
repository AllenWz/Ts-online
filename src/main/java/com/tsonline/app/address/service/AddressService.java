package com.tsonline.app.address.service;

import java.util.List;

import com.tsonline.app.address.dto.AddressRequestDto;
import com.tsonline.app.address.dto.AddressResponseDto;
import com.tsonline.app.user.entity.User;

public interface AddressService {
	AddressResponseDto registerAddress(AddressRequestDto addressDto, User user);

	List<AddressResponseDto> getAllAddresses();

	AddressResponseDto getAddressesById(Long addressId);

	List<AddressResponseDto> getAddressesByUser(User user);

	AddressResponseDto updateAddress(Long addressId, AddressRequestDto addressDto, User user);

	List<AddressResponseDto> deleteAddress(Long addressId, User user);
}
