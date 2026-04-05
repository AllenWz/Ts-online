package com.tsonline.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUserName(String username);
}
