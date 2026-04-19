package com.tsonline.app.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tsonline.app.security.models.UserDetailsImpl;
import com.tsonline.app.user.entity.User;
import com.tsonline.app.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username)
						.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: "+ username));
		
		return UserDetailsImpl.builder(user);
	}

}
