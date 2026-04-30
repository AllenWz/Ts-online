package com.tsonline.app.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.address.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
