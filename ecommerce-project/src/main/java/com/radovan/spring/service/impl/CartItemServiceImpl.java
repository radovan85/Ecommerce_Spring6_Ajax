package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartRepository cartRepository;

	@Override
	public CartItemDto addCartItem(CartItemDto cartItem) {
		// TODO Auto-generated method stub
		CartItemEntity cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem);
		CartItemEntity storedItem = cartItemRepository.save(cartItemEntity);
		CartItemDto returnValue = tempConverter.cartItemEntityToDto(storedItem);
		return returnValue;
	}

	@Override
	public void removeCartItem(Integer cartId, Integer itemId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeCartItem(itemId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);

	}

	@Override
	public void eraseAllCartItems(Integer cartId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByCartId(cartId);
		cartItemRepository.flush();
		cartService.refreshCartState(cartId);
	}

	@Override
	public List<CartItemDto> listAllByCartId(Integer cartId) {
		// TODO Auto-generated method stub
		List<CartItemEntity> cartItems = cartItemRepository.findAllByCartId(cartId);
		List<CartItemDto> returnValue = new ArrayList<>();

		cartItems.forEach((item) -> {
			CartItemDto itemDto = tempConverter.cartItemEntityToDto(item);
			returnValue.add(itemDto);
		});

		return returnValue;
	}

	@Override
	public CartItemDto getCartItem(Integer id) {
		// TODO Auto-generated method stub
		Optional<CartItemEntity> cartItemEntity = cartItemRepository.findById(id);
		CartItemDto returnValue = null;
		if (cartItemEntity.isPresent()) {
			returnValue = tempConverter.cartItemEntityToDto(cartItemEntity.get());
		}

		return returnValue;
	}

	@Override
	public void eraseAllByProductId(Integer productId) {
		// TODO Auto-generated method stub
		cartItemRepository.removeAllByProductId(productId);
		cartItemRepository.flush();

		List<CartEntity> allCarts = cartRepository.findAll();

		allCarts.forEach((cartEntity) -> {
			cartService.refreshCartState(cartEntity.getCartId());
		});

	}
}
