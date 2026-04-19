package com.tsonline.app.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tsonline.app.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	@Query("SELECT c from Cart c WHERE c.user.email = ?1")
	Cart findCartByEmail(String email);
	
	@Query("SELECT c from Cart c WHERE c.user.email = ?1 AND c.id = ?2")
	Cart findCartByEmailAndCartId(String email, Long cartId);

	@Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p.id WHERE p.id = ?1")
	List<Cart> findCartsByProductId(Long productId);
}
