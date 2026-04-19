package com.tsonline.app.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.tsonline.app.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

	@Query("SELECT ci FROM CartItem ci WHERE ci.product.id = ?1 AND ci.cart.id = ?2")
	CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);
	
	@Modifying
	@Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 and ci.product.id = ?2")
	void deleteCartItemByCartIdAndProductId(Long cartId, Long productId);
}
