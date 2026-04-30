package com.tsonline.app.order.service;

import com.tsonline.app.order.dto.OrderRequestDto;
import com.tsonline.app.order.dto.OrderResponseDto;
import com.tsonline.app.user.entity.User;

public interface OrderService {
	OrderResponseDto placeOrder(User user, OrderRequestDto orderRequest);
}
