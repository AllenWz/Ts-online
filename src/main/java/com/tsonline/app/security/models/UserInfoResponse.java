package com.tsonline.app.security.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
	private Long id;

	private String username;

	private List<String> roles;

	private String jwtToken;

	public UserInfoResponse(Long id, String username, List<String> roles) {
		this.id = id;
		this.username = username;
		this.roles = roles;
	}
}
