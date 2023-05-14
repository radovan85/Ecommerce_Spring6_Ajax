package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.BillingAddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.BillingAddressService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.ProductService;
import com.radovan.spring.service.ShippingAddressService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/order")
public class OrderController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OrderService orderService;

	@GetMapping(value = "/cancel")
	public String cancelOrder() {
		return "fragments/checkout/checkOutCancelled :: ajaxLoadedContent";
	}

	@GetMapping(value = "/cartError")
	public String cartError() {
		return "fragments/checkout/invalidCartWarning :: ajaxLoadedContent";
	}

	@GetMapping(value = "/billingConfirmation")
	public String checkout(ModelMap map) {

		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		cartService.validateCart(customer.getCartId());
		BillingAddressDto billingAddress = new BillingAddressDto();
		BillingAddressDto currentBillingAddress = billingAddressService
				.getBillingAddress(customer.getBillingAddressId());
		map.put("billingAddress", billingAddress);
		map.put("currentBillingAddress", currentBillingAddress);
		return "fragments/checkout/confirm_billing_details :: ajaxLoadedContent";
	}

	@PostMapping(value = "/storeBillingAddress")
	public String storeBilling(@ModelAttribute("billingAddress") BillingAddressDto billingAddress) {
		billingAddressService.addBillingAddress(billingAddress);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/shippingConfirmation")
	public String confirmShipping(ModelMap map) {
		ShippingAddressDto shippingAddress = new ShippingAddressDto();
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		ShippingAddressDto currentShippingAddress = shippingAddressService
				.getShippingAddress(customer.getShippingAddressId());
		map.put("shippingAddress", shippingAddress);
		map.put("currentShippingAddress", currentShippingAddress);
		return "fragments/checkout/confirm_shipping_details :: ajaxLoadedContent";
	}

	@PostMapping(value = "/storeShippingAddress")
	public String storeShipping(@ModelAttribute("shippingAddress") ShippingAddressDto shippingAddress) {

		shippingAddressService.addShippingAddress(shippingAddress);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/phoneConfirmation")
	public String confirmPhone(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = new CustomerDto();
		CustomerDto currentCustomer = customerService.getCustomerByUserId(authUser.getId());
		map.put("customer", customer);
		map.put("currentCustomer", currentCustomer);
		return "fragments/checkout/confirm_customer_phone :: ajaxLoadedContent";
	}

	@PostMapping(value = "/storeCustomer")
	public String storeCustomer(@ModelAttribute("customer") CustomerDto customer) {
		customerService.addCustomer(customer);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/orderConfirmation")
	public String confirmOrder(ModelMap map) {

		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		BillingAddressDto billingAddress = billingAddressService.getBillingAddress(customer.getBillingAddressId());
		ShippingAddressDto shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId());
		OrderDto order = new OrderDto();
		List<CartItemDto> allCartItems = cartItemService.listAllByCartId(cart.getCartId());
		List<ProductDto> allProducts = productService.listAll();
		map.put("cart", cart);
		map.put("billingAddress", billingAddress);
		map.put("shippingAddress", shippingAddress);
		map.put("order", order);
		map.put("allCartItems", allCartItems);
		map.put("allProducts", allProducts);

		return "fragments/checkout/orderConfirmation :: ajaxLoadedContent";
	}

	@PostMapping(value = "/createOrder")
	public String createOrder() {

		orderService.addCustomerOrder();
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/stockProblem")
	public String stockProblem() {
		return "fragments/checkout/stock_exception :: ajaxLoadedContent";
	}

	@GetMapping(value = "/orderExecuted")
	public String orderCompleted() {
		return "fragments/checkout/thankCustomer :: ajaxLoadedContent";
	}
}
