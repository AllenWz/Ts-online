package com.tsonline.app.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
