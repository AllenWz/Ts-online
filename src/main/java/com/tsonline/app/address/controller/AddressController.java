package com.tsonline.app.address.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsonline.app.address.dto.AddressRequestDto;
import com.tsonline.app.address.dto.AddressResponseDto;
import com.tsonline.app.address.service.AddressService;
import com.tsonline.app.cart.service.CartServiceImpl;
import com.tsonline.app.common.util.AuthUtil;
import com.tsonline.app.user.entity.User;

@RestController
@RequestMapping("/api")
public class AddressController {
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private AuthUtil authUtil;
	
	@PostMapping("/public/addresses")
	public ResponseEntity<AddressResponseDto> registerAddress(@RequestBody AddressRequestDto addressDto) {
		logger.info("#AddressController#registerAddress");
		User user = authUtil.getLoggedInUser();
		AddressResponseDto response = addressService.registerAddress(addressDto, user);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@GetMapping("/addresses")
	public ResponseEntity<List<AddressResponseDto>> getAllAddresses() {
		logger.info("#AddressController#getAllAddresses");
		List<AddressResponseDto> responseList = addressService.getAllAddresses();		
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@GetMapping("/public/addresses/{addressId}")
	public ResponseEntity<AddressResponseDto> getAddressesById(@PathVariable Long addressId) {
		logger.info("#AddressController#getAddressesById");
		AddressResponseDto response = addressService.getAddressesById(addressId);		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("/public/addresses")
	public ResponseEntity<List<AddressResponseDto>> getAddressesByUser() {
		logger.info("#AddressController#getAddressesByUser");
		User user = authUtil.getLoggedInUser();
		List<AddressResponseDto> responseList = addressService.getAddressesByUser(user);		
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
	
	@PutMapping("/public/addresses/{addressId}")
	public ResponseEntity<AddressResponseDto> updateAddress(@PathVariable Long addressId, @RequestBody AddressRequestDto addressDto) {
		logger.info("#AddressController#updateAddress");
		User user = authUtil.getLoggedInUser();
		AddressResponseDto response = addressService.updateAddress(addressId, addressDto, user);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/public/addresses/{addressId}")
	public ResponseEntity<List<AddressResponseDto>> deleteAddress(@PathVariable Long addressId) {
		logger.info("#AddressController#deleteAddress");
		User user = authUtil.getLoggedInUser();
		List<AddressResponseDto> responseList = addressService.deleteAddress(addressId, user);
		return new ResponseEntity<>(responseList, HttpStatus.OK);
	}
}
