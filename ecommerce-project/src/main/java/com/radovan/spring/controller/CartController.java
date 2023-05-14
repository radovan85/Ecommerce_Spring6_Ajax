package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.ProductService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/cart")
public class CartController {

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ProductService productService;

	@GetMapping(value = "/viewCart")
	public String getCart(ModelMap map) {

		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cart.getCartId());
		List<ProductDto> allProducts = productService.listAll();
		Float fullPrice = cartService.calculateFullPrice(cart.getCartId());
		map.put("allCartItems", allCartItems);
		map.put("allProducts", allProducts);
		map.put("fullPrice", fullPrice);
		map.put("cart", cart);
		return "fragments/cart :: ajaxLoadedContent";
	}

	@GetMapping(value = "/add/{productId}")
	public String addCartItem(@PathVariable(value = "productId") Integer productId) {
		UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CustomerDto customer = customerService.getCustomerByUserId(user.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		List<Integer> cartItemIds = cart.getCartItemsIds();
		ProductDto product = productService.getProduct(productId);
		for (int i = 0; i < cartItemIds.size(); i++) {
			Integer itemId = cartItemIds.get(i);
			CartItemDto cartItem = cartItemService.getCartItem(itemId);
			if (product.getProductId().intValue() == cartItem.getProductId().intValue()) {
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				ProductDto tempProduct = productService.getProduct(cartItem.getProductId());
				cartItem.setPrice(cartItem.getQuantity() * tempProduct.getProductPrice());
				cartItemService.addCartItem(cartItem);
				cartService.refreshCartState(cart.getCartId());
				return "fragments/homePage :: ajaxContentLoaded";
			}
		}
		CartItemDto cartItem = new CartItemDto();
		cartItem.setQuantity(1);
		cartItem.setProductId(productId);
		cartItem.setPrice(product.getProductPrice() * 1);
		cartItem.setCartId(cart.getCartId());
		cartItemService.addCartItem(cartItem);
		cartService.refreshCartState(cart.getCartId());
		return "fragments/homePage :: ajaxContentLoaded";
	}

	@GetMapping(value = "/removeCartItem/{cartId}/{itemId}")
	public String removeCartItem(@PathVariable(value = "cartId") Integer cartId,
			@PathVariable(value = "itemId") Integer itemId) {
		cartItemService.removeCartItem(cartId, itemId);
		return "fragments/homePage :: ajaxContentLoaded";
	}

	@GetMapping(value = "/removeAllItems/{cartId}")
	public String removeAllCartItems(@PathVariable(value = "cartId") Integer cartId) {
		cartItemService.eraseAllCartItems(cartId);
		return "fragments/homePage :: ajaxContentLoaded";
	}
}
