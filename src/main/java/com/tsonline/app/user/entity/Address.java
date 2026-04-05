package com.tsonline.app.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.tsonline.app.common.entity.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
public class Address extends BaseEntity{	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;
	
	@NotBlank
	@Size(min = 5, message = "Street name must be at least 5 character")
	private String street;
	
	@NotBlank
	@Size(min = 5, message = "Building name must be at least 5 character")
	private String buildingName;
	
	@NotBlank
	@Size(min = 4, message = "City name must be at least 4 character")
	private String city;
	
	@NotBlank
	@Size(min = 2, message = "State name must be at least 2 character")
	private String state;
	
	@NotBlank
	@Size(min = 2, message = "Country name must be at least 2 character")
	private String country;
	
	@NotBlank
	@Size(min = 6, message = "Pincode name must be at least 6 character")
	private String pincode;
	
	@ManyToMany(mappedBy = "addresses")
	private List<User> users = new ArrayList<>();
	
}
