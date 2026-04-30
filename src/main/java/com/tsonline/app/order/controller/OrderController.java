package com.tsonline.app.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsonline.app.common.util.AuthUtil;
import com.tsonline.app.order.dto.OrderRequestDto;
import com.tsonline.app.order.dto.OrderResponseDto;
import com.tsonline.app.order.service.OrderService;
import com.tsonline.app.user.entity.User;

@RestController
@RequestMapping("/api")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AuthUtil authUtil;

	@PostMapping("/order")
	public ResponseEntity<OrderResponseDto> orderProduct(@RequestBody OrderRequestDto orderRequest) {
		logger.info("#OrderController#orderProduct");
		User user = authUtil.getLoggedInUser();
		OrderResponseDto response = orderService.placeOrder(user, orderRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}
