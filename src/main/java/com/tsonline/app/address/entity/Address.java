package com.tsonline.app.address.entity;

import com.tsonline.app.common.entity.BaseEntity;
import com.tsonline.app.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
	
	private String street;
	
	private String buildingName;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private String pincode;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
}
