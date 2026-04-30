package com.tsonline.app.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
