package com.tsonline.app.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tsonline.app.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	@Query("SELECT c from Cart c WHERE c.user.email = :email")
	Cart findCartByEmail(@Param("email") String email);
	
	@Query("SELECT c from Cart c WHERE c.user.email = :email AND c.id = :cartId")
	Cart findCartByEmailAndCartId(@Param("email") String email,@Param("cartId") Long cartId);

	 @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = :productId")
	List<Cart> findCartsByProductId(@Param("productId") Long productId);
}
