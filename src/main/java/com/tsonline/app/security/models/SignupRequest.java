package com.tsonline.app.security.models;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
	@NotBlank(message = "{common.required}")
	@Size(min=3, max=20, message = "{common.size.range}")
	@Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "{common.pattern.alphanumeric}")
	private String username;
	
	@NotBlank(message = "{common.required}")
	@Size(max=50, message = "{common.size.max}")
	@Email(message = "{auth.email.invalid}")
	private String email;
	
	private Set<String> role;
	
	@NotBlank(message = "{common.required}")
	@Size(min=6, max=40, message = "{common.size.range}")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$", message = "{auth.password.complexity}")
	private String password;
}
