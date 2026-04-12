package com.tsonline.app.user.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsonline.app.security.jwt.JwtUtils;
import com.tsonline.app.security.models.LoginRequest;
import com.tsonline.app.security.models.MessageResponse;
import com.tsonline.app.security.models.SignupRequest;
import com.tsonline.app.security.models.UserInfoResponse;
import com.tsonline.app.security.services.UserDetailsImpl;
import com.tsonline.app.user.entity.AppRole;
import com.tsonline.app.user.entity.Role;
import com.tsonline.app.user.entity.User;
import com.tsonline.app.user.repository.RoleRepository;
import com.tsonline.app.user.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private RoleRepository roleRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication;

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch(AuthenticationException ex) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
		List<String> roles = userDetails.getAuthorities()
										.stream()
										.map(item -> item.getAuthority())
										.collect(Collectors.toList());
		
		UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles, jwtToken);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
		if(userRepository.existsByUserName(signupRequest.getUsername())) {
			return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Username is already taken!"));
		}
		
		if(userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
						.badRequest()
						.body(new MessageResponse("Error: Email is already taken!"));
		}
		
		User user = new User(
				signupRequest.getUsername(),
				signupRequest.getEmail(),
				encoder.encode(signupRequest.getPassword())
		);
		
		Set<String> strRoles = signupRequest.getRole();
		Set<Role> roles = new HashSet<>();
		
		if(strRoles == null) {
			Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role not found"));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				if(role == "admin") {
					Role adminRole = roleRepository
										.findByRoleName(AppRole.ROLE_ADMIN)
										.orElseThrow(() -> new RuntimeException("Error: Role not found"));
					roles.add(adminRole);
				} else if(role == "seller") {
					Role sellerRole = roleRepository
							.findByRoleName(AppRole.ROLE_SELLER)
							.orElseThrow(() -> new RuntimeException("Error: Role not found"));
					roles.add(sellerRole);
				} else {
					Role userRole = roleRepository
							.findByRoleName(AppRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role not found"));
					roles.add(userRole);
				}
			});
		}
		
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User Registered Successfully!"));
	}
}














