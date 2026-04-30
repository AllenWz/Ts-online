package com.tsonline.app.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {	
	@NotBlank
	@Size(min = 5, message = "{common.size.min}")
	private String street;
	
	@NotBlank
	@Size(min = 5, message = "{common.size.min}")
	private String buildingName;
	
	@NotBlank
	@Size(min = 4, message = "{common.size.min}")
	private String city;
	
	@NotBlank
	@Size(min = 2, message = "{common.size.min}")
	private String state;
	
	@NotBlank
	@Size(min = 2, message = "{common.size.min}")
	private String country;
	
	@NotBlank
	@Size(min = 5, message = "{common.size.min}")
	private String pincode;
}
