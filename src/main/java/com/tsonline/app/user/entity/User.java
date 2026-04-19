package com.tsonline.app.user.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tsonline.app.cart.entity.Cart;
import com.tsonline.app.common.entity.BaseEntity;
import com.tsonline.app.product.entity.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", 
		uniqueConstraints = {@UniqueConstraint(columnNames = "userName"),
							@UniqueConstraint(columnNames = "email")})
public class User extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	@NotBlank
	@Size(max = 20)
	private String userName;
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	@NotBlank
	@Size(max = 120)
	private String password;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
				fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	@OneToMany(mappedBy = "user", 
				cascade = {CascadeType.PERSIST, CascadeType.MERGE},
				orphanRemoval = true)
	private Set<Product> products;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
			fetch = FetchType.EAGER)
	@JoinTable(name = "user_address",
				joinColumns = @JoinColumn(name = "user_id"),
				inverseJoinColumns = @JoinColumn(name = "address_id"))
	private List<Address> addresses = new ArrayList<>();
	
	@ToString.Exclude
	@OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private Cart cart;

	// Constructor
	public User(@NotBlank @Size(max = 20) String userName, @NotBlank @Size(max = 50) @Email String email,
			@NotBlank @Size(max = 120) String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
}
