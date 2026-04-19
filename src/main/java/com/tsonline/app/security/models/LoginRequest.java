package com.tsonline.app.security.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
	
	@NotBlank(message = "{common.required}")
	@Size(min=3, max=20, message="{common.size.range}")
	@Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "{common.pattern.alphanumeric}")
	private String username;
	
	@NotBlank(message = "{common.required}")
	private String password;
}
