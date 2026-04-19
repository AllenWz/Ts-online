package com.tsonline.app.security.services;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String>{

	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth == null 
			|| !auth.isAuthenticated()) {
			return Optional.of("SYSTEM");
		}
		return Optional.ofNullable(auth.getName());
	}

}
