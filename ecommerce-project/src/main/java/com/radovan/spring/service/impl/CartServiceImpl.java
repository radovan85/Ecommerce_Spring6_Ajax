package com.radovan.spring.service.impl;

import java.text.DecimalFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.exceptions.InvalidCartException;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartItemRepository cartItemRepository;
	
	private DecimalFormat decfor = new DecimalFormat("0.00");
	

	@Override
	public CartDto getCartByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
		CartDto returnValue = new CartDto();
		if (cartEntity.isPresent()) {
			returnValue = tempConverter.cartEntityToDto(cartEntity.get());
		} else {
			Error error = new Error("Invalid cart");
			throw new InvalidCartException(error);
		}
		return returnValue;
	}

	@Override
	public void refreshCartState(Integer cartId) {
		// TODO Auto-generated method stub
		CartEntity cartEntity = cartRepository.findById(cartId).get();
		Optional<Float> priceOpt = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId));
		if (priceOpt.isPresent()) {
			Float price = priceOpt.get();
			price = Float.valueOf(decfor.format(price));
			cartEntity.setCartPrice(price);
		} else {
			cartEntity.setCartPrice(0f);
		}
		cartRepository.saveAndFlush(cartEntity);
	}

	@Override
	public Float calculateGrandTotal(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<Float> grandTotalOpt = Optional.ofNullable(cartItemRepository.calculateGrandTotal(cartId));
		Float returnValue = 0f;

		if (grandTotalOpt.isPresent()) {
			Float grandTotal = grandTotalOpt.get();
			returnValue = Float.valueOf(decfor.format(grandTotal));
		}

		return returnValue;
	}

	@Override
	public Float calculateFullPrice(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<Float> fullPriceOpt = Optional.ofNullable(cartItemRepository.calculateFullPrice(cartId));
		Float returnValue = 0f;

		if (fullPriceOpt.isPresent()) {
			Float fullPrice = fullPriceOpt.get();
			returnValue = Float.valueOf(decfor.format(fullPrice));
		}

		return returnValue;
	}

	@Override
	public CartDto validateCart(Integer cartId) {
		// TODO Auto-generated method stub
		Optional<CartEntity> cartEntity = cartRepository.findById(cartId);
		CartDto returnValue = new CartDto();
		Error error = new Error("Invalid Cart");

		if (cartEntity.isPresent()) {
			if (cartEntity.get().getCartItems().size() == 0) {
				throw new InvalidCartException(error);
			}

			returnValue = tempConverter.cartEntityToDto(cartEntity.get());

		} else {
			throw new InvalidCartException(error);
		}

		return returnValue;
	}

	@Override
	public void deleteCart(Integer cartId) {
		// TODO Auto-generated method stub
		cartRepository.deleteById(cartId);
		cartRepository.flush();
	}
}
