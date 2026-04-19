package com.tsonline.app.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.tsonline.app.cart.service.CartServiceImpl;
import com.tsonline.app.user.entity.User;
import com.tsonline.app.user.repository.UserRepository;

@Component
public class AuthUtil {
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;

	public String getLoggedInEmail() {
		logger.info("#AuthUtil#getLoggedInEmail");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userRepository.findByUserName(auth.getName())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+auth.getName()));
		return loggedInUser.getEmail();
	}
	
	public User getLoggedInUser() {
		logger.info("#AuthUtil#getLoggedInUser");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userRepository.findByUserName(auth.getName())
								.orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+auth.getName()));
		return loggedInUser;
	}

	public Long getLoggedInUserId() {
		logger.info("#AuthUtil#getLoggedInUserId");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userRepository.findByUserName(auth.getName())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: "+auth.getName()));
		return loggedInUser.getUserId();
	}

}
