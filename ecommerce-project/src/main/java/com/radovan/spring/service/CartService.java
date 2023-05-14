package com.radovan.spring.service;

import com.radovan.spring.dto.CartDto;

public interface CartService {

	CartDto getCartByCartId(Integer cartId);

	void refreshCartState(Integer cartId);

	Float calculateGrandTotal(Integer cartId);

	CartDto validateCart(Integer cartId);

	Float calculateFullPrice(Integer cartId);

	void deleteCart(Integer cartId);
}
