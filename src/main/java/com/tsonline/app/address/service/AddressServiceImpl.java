package com.tsonline.app.address.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsonline.app.address.dto.AddressRequestDto;
import com.tsonline.app.address.dto.AddressResponseDto;
import com.tsonline.app.address.entity.Address;
import com.tsonline.app.address.repository.AddressRepository;
import com.tsonline.app.cart.service.CartServiceImpl;
import com.tsonline.app.common.exception.BusinessRuleException;
import com.tsonline.app.common.exception.ResourceNotFoundException;
import com.tsonline.app.common.util.Mapper;
import com.tsonline.app.user.entity.User;
import com.tsonline.app.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AddressServiceImpl implements AddressService{
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private Mapper mapper;

	@Override
	@Transactional
	public AddressResponseDto registerAddress(AddressRequestDto addressDto, User user) {
		logger.info("#AddressServiceImpl#registerAddress");
		Address address = mapper.addressDtoToEntity(addressDto);
		
		List<Address> addressList = user.getAddresses();
		addressList.add(address);
		user.setAddresses(addressList);
		
		address.setUser(user);
		Address savedAddress = addressRepository.save(address);
		
		return mapper.addressEntityToDto(savedAddress);
	}

	@Override
	public List<AddressResponseDto> getAllAddresses() {
		logger.info("#AddressServiceImpl#getAllAddresses");
		List<Address> addressList = addressRepository.findAll();
		List<AddressResponseDto> addressDto = addressList.stream()
														.map(address -> mapper.addressEntityToDto(address))
														.toList();
		return addressDto;
	}

	@Override
	public AddressResponseDto getAddressesById(Long addressId) {
		logger.info("#AddressServiceImpl#getAddressesById");
		Address address = addressRepository.findById(addressId)
								.orElseThrow(() -> new ResourceNotFoundException("Address", "Address ID", addressId));
		return mapper.addressEntityToDto(address);
	}

	@Override
	public List<AddressResponseDto> getAddressesByUser(User user) {
		logger.info("#AddressServiceImpl#getAddressesByUser");
		List<Address> addressList = user.getAddresses();
		List<AddressResponseDto> addressDto = addressList.stream()
				.map(address -> mapper.addressEntityToDto(address))
				.toList();
		return addressDto;
	}

	@Override
	@Transactional
	public AddressResponseDto updateAddress(Long addressId, AddressRequestDto addressDto, User user) {
		logger.info("#AddressServiceImpl#updateAddress");
		Address addressFromDb = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "Address ID", addressId));
		
		boolean ownsAddress = user.getAddresses().stream()
										.anyMatch(a -> a.getAddressId().equals(addressId));
		
		if (!ownsAddress) {
	        throw new BusinessRuleException("Access Denied: Address does not belong to user", HttpStatus.FORBIDDEN);
	    }
		
		addressFromDb.setStreet(addressDto.getStreet());
		addressFromDb.setBuildingName(addressDto.getBuildingName());
		addressFromDb.setCity(addressDto.getCity());
		addressFromDb.setState(addressDto.getState());
		addressFromDb.setCountry(addressDto.getCountry());
		addressFromDb.setPincode(addressDto.getPincode());
		
		Address updatedAddress = addressRepository.save(addressFromDb);
		
		// remove the old address form the user address list and add the newly updated address
		// ** Actually this logics doesn't need to write manually since dirty checking of hibernate will automatically handle that
//		User updatedUser = updatedAddress.getUser();
//		updatedUser.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
//		updatedUser.getAddresses().add(updatedAddress);
//		
//		userRepository.save(updatedUser);
		
		return mapper.addressEntityToDto(updatedAddress);
	}

	@Override
	@Transactional
	public List<AddressResponseDto> deleteAddress(Long addressId, User user) {
		logger.info("#AddressServiceImpl#deleteAddress");
		Address addressFromDb = addressRepository.findById(addressId)
								.orElseThrow(() -> new ResourceNotFoundException("Address", "Address ID", addressId));
		
		boolean ownsAddress = user.getAddresses().stream()
				.anyMatch(a -> a.getAddressId().equals(addressId));

		if (!ownsAddress) {
			throw new BusinessRuleException("Access Denied: Address does not belong to user", HttpStatus.FORBIDDEN);
		}
		
		user.getAddresses().remove(addressFromDb);
		userRepository.save(user);
		
		// we don't need to delete this manually since we use orphanRemoval in entity
		// Removing it from the list + saving user = Auto-delete from DB
		//addressRepository.delete(addressFromDb);
		
		return user.getAddresses().stream()
									.map(mapper::addressEntityToDto)
									.toList();
	}
}
