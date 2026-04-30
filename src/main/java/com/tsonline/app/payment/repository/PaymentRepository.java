package com.tsonline.app.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tsonline.app.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
