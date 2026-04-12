package com.tsonline.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.user.entity.AppRole;
import com.tsonline.app.user.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	public Optional<Role> findByRoleName(AppRole appRole);
}
