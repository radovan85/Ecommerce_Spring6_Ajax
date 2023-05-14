package com.radovan.spring.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.AdminMessageDto;
import com.radovan.spring.dto.BillingAddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.AdminMessageService;
import com.radovan.spring.service.BillingAddressService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.OrderAddressService;
import com.radovan.spring.service.OrderItemService;
import com.radovan.spring.service.OrderService;
import com.radovan.spring.service.ProductService;
import com.radovan.spring.service.ShippingAddressService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserService userService;

	@Autowired
	private BillingAddressService billingAddressService;

	@Autowired
	private ShippingAddressService shippingAddressService;

	@Autowired
	private AdminMessageService messageService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private OrderAddressService orderAddressService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private CartService cartService;

	@GetMapping(value = "/")
	public String adminHome() {
		return "fragments/admin :: ajaxLoadedContent";
	}

	@GetMapping(value = "/addNewProduct")
	public String renderProductForm(ModelMap map) {

		ProductDto product = new ProductDto();
		map.put("product", product);
		return "fragments/addProduct :: ajaxLoadedContent";
	}

	@PostMapping(value = "/createProduct")
	public String createProduct(@ModelAttribute("product") ProductDto product, ModelMap map) {

		productService.addProduct(product);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/deleteProduct/{productId}")
	public String deleteProduct(@PathVariable("productId") Integer productId) {

		cartItemService.eraseAllByProductId(productId);
		productService.deleteProduct(productId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/updateProduct/{productId}")
	public String renderUpdateForm(@PathVariable("productId") Integer productId, ModelMap map) {

		ProductDto product = new ProductDto();
		ProductDto currentProduct = productService.getProduct(productId);
		map.put("product", product);
		map.put("currentProduct", currentProduct);
		return "fragments/updateProduct :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allCustomers")
	public String customerList(ModelMap map) {

		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 8);
		return "fragments/customerList :: ajaxLoadedContent";

	}

	@GetMapping(value = "/getCustomer/{customerId}")
	public String getCustomer(@PathVariable("customerId") Integer customerId, ModelMap map) {

		CustomerDto customer = customerService.getCustomer(customerId);
		UserDto tempUser = userService.getUserById(customer.getUserId());
		ShippingAddressDto shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId());
		BillingAddressDto billingAddress = billingAddressService.getBillingAddress(customer.getBillingAddressId());
		map.put("tempCustomer", customer);
		map.put("tempUser", tempUser);
		map.put("billingAddress", billingAddress);
		map.put("shippingAddress", shippingAddress);
		return "fragments/customerDetails :: ajaxLoadedContent";
	}

	@GetMapping(value = "/deleteCustomer/{customerId}")
	public String removeCustomer(@PathVariable("customerId") Integer customerId) {
		CustomerDto customer = customerService.getCustomer(customerId);
		CartDto cart = cartService.getCartByCartId(customer.getCartId());
		BillingAddressDto billingAddress = billingAddressService.getBillingAddress(customer.getBillingAddressId());
		ShippingAddressDto shippingAddress = shippingAddressService.getShippingAddress(customer.getShippingAddressId());
		UserDto user = userService.getUserById(customer.getUserId());

		List<OrderDto> allOrders = orderService.listAllByCustomerId(customerId);
		allOrders.forEach((order) -> {
			orderItemService.eraseAllByOrderId(order.getOrderId());
			orderService.deleteOrder(order.getOrderId());
		});

		cartItemService.eraseAllCartItems(cart.getCartId());
		messageService.deleteAllByCustomerId(customerId);
		customerService.resetCustomer(customerId);
		billingAddressService.deleteBillingAddress(billingAddress.getBillingAddressId());
		shippingAddressService.deleteShippingAddress(shippingAddress.getShippingAddressId());
		cartService.deleteCart(cart.getCartId());
		customerService.deleteCustomer(customerId);
		userService.deleteUser(user.getId());
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allMessages")
	public String listAllMessages(ModelMap map) {

		List<AdminMessageDto> allMessages = messageService.listAll();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("allMessages", allMessages);
		map.put("recordsPerPage", 5);
		return "fragments/messageList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/messageDetails/{messageId}")
	public String getMessage(@PathVariable("messageId") Integer messageId, ModelMap map) {
		AdminMessageDto message = messageService.getMessage(messageId);
		CustomerDto customer = customerService.getCustomer(message.getCustomerId());
		UserDto user = userService.getUserById(customer.getUserId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = message.getCreatedAt().toLocalDateTime();
		String createdAtStr = dateTime.format(formatter);
		map.put("message", message);
		map.put("user", user);
		map.put("createdAtStr", createdAtStr);
		return "fragments/messageDetails :: ajaxLoadedContent";
	}

	@GetMapping(value = "/deleteMessage/{messageId}")
	public String deleteReview(@PathVariable("messageId") Integer messageId) {

		messageService.deleteMessage(messageId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@PostMapping(value = "/suspendUser/{userId}")
	public String suspendUser(@PathVariable("userId") Integer userId) {
		userService.suspendUser(userId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allOrders")
	public String listAllOrders(ModelMap map) {

		List<OrderDto> allOrders = orderService.listAll();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		List<UserDto> allUsers = userService.listAllUsers();
		map.put("allOrders", allOrders);
		map.put("allCustomers", allCustomers);
		map.put("allUsers", allUsers);
		map.put("recordsPerPage", 10);
		return "fragments/orderList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/deleteOrder/{orderId}")
	public String deleteOrder(@PathVariable("orderId") Integer orderId) {

		orderItemService.eraseAllByOrderId(orderId);
		orderService.deleteOrder(orderId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/getOrder/{orderId}")
	public String orderDetails(@PathVariable("orderId") Integer orderId, ModelMap map) {

		OrderDto order = orderService.getOrder(orderId);
		OrderAddressDto address = orderAddressService.getAddressById(order.getAddressId());
		List<ProductDto> allProducts = productService.listAll();
		Float orderPrice = orderService.calculateOrderTotal(orderId);
		List<OrderItemDto> orderedItems = orderItemService.listAllByOrderId(orderId);
		map.put("order", order);
		map.put("address", address);
		map.put("allProducts", allProducts);
		map.put("orderPrice", orderPrice);
		map.put("orderedItems", orderedItems);
		return "fragments/orderDetails :: ajaxLoadedContent";
	}

}
