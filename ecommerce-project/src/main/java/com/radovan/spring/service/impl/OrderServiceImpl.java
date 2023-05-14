package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.entity.ShippingAddressEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InsufficientStockException;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderAddressRepository orderAddressRepository;
	
	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	public OrderDto addCustomerOrder() {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CustomerEntity customerEntity = customerRepository.findByUserId(authUser.getId());
		ShippingAddressEntity shippingAddress = customerEntity.getShippingAddress();
		CartEntity cartEntity = customerEntity.getCart();
		OrderEntity orderEntity = new OrderEntity();
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();

		List<CartItemEntity> cartItems = cartEntity.getCartItems();
		if (!cartItems.isEmpty()) {
			cartItems.forEach((item) -> {
				Optional<ProductEntity> productEntity = Optional.ofNullable(item.getProduct());
				if (productEntity.isPresent()) {
					if (productEntity.get().getUnitStock() < item.getQuantity()) {
						Error error = new Error("Not enough stock");
						throw new InsufficientStockException(error);
					} else {
						ProductEntity tempProduct = productEntity.get();
						Integer newStock = tempProduct.getUnitStock() - item.getQuantity();
						tempProduct.setUnitStock(newStock);
						productRepository.saveAndFlush(tempProduct);
					}
				}
			});
		}

		if (!cartItems.isEmpty()) {
			cartItems.forEach((item) -> {
				OrderItemEntity orderedItem = tempConverter.cartItemToOrderItemEntity(item);
				OrderItemEntity storedOrderedItem = orderItemRepository.save(orderedItem);
				orderedItems.add(storedOrderedItem);
			});
		}

		OrderAddressEntity orderAddress = tempConverter.shippingAddressToOrderAddress(shippingAddress);
		OrderAddressEntity storedOrderAddress = orderAddressRepository.save(orderAddress);

		orderEntity.setCart(cartEntity);
		orderEntity.setCustomer(customerEntity);
		orderEntity.setOrderedItems(orderedItems);
		orderEntity.setAddress(storedOrderAddress);
		ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Belgrade"));
		Timestamp createdAt = new Timestamp(currentTime.toInstant().getEpochSecond() * 1000L);
		orderEntity.setCreatedAt(createdAt);

		OrderEntity storedOrder = orderRepository.save(orderEntity);

		storedOrderAddress.setOrder(storedOrder);
		orderAddressRepository.saveAndFlush(storedOrderAddress);

		OrderDto returnValue = tempConverter.orderEntityToDto(storedOrder);

		storedOrder.getOrderedItems().forEach((item) -> {
			item.setOrder(storedOrder);
			orderItemRepository.saveAndFlush(item);
		});

		cartItemRepository.removeAllByCartId(cartEntity.getCartId());
		cartService.refreshCartState(cartEntity.getCartId());
		return returnValue;
	}

	@Override
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderEntity> allOrders = orderRepository.findAll();
		List<OrderDto> returnValue = new ArrayList<>();

		allOrders.forEach((order) -> {
			OrderDto orderDto = tempConverter.orderEntityToDto(order);
			returnValue.add(orderDto);
		});

		return returnValue;
	}

	@Override
	public Float calculateOrderTotal(Integer orderId) {
		// TODO Auto-generated method stub
		Optional<Float> orderTotalOpt = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId));
		Float returnValue = 0f;

		if (orderTotalOpt.isPresent()) {
			Float orderTotal = orderTotalOpt.get();
			orderTotal = Float.valueOf(decfor.format(orderTotal));
			
			returnValue = orderTotal;
		}

		return returnValue;
	}

	@Override
	public OrderDto getOrder(Integer orderId) {
		// TODO Auto-generated method stub
		OrderEntity orderEntity = orderRepository.findById(orderId).get();
		OrderDto returnValue = tempConverter.orderEntityToDto(orderEntity);
		return returnValue;
	}

	@Override
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		orderRepository.deleteById(orderId);
		orderRepository.flush();

	}

	@Override
	public List<OrderDto> listAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		List<OrderEntity> allOrders = orderRepository.findAllByCustomerId(customerId);
		allOrders.forEach((order) -> {
			OrderDto orderDto = tempConverter.orderEntityToDto(order);
			returnValue.add(orderDto);
		});

		return returnValue;
	}
}
