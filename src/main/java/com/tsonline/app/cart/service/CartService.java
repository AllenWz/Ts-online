package com.tsonline.app.cart.service;

import java.util.List;

import com.tsonline.app.cart.dto.CartResponseDto;
import com.tsonline.app.cart.entity.Cart;
import com.tsonline.app.product.entity.Product;
import com.tsonline.app.user.entity.User;

public interface CartService {
	public CartResponseDto addProductToCart(User user, Long productId, Integer quantity);

	public List<CartResponseDto> getAllCarts();

	public CartResponseDto getCart(String email, Long cartId);

	public CartResponseDto updateProductQuantityInCart(Long productId, Integer quantity, User user);

	public CartResponseDto deleteProductFromCart(Long cartId, Long productId, User user);

	public void updateProductInCarts(List<Cart> carts, Product product);
}
