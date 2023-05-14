package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.AdminMessageDto;
import com.radovan.spring.dto.BillingAddressDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.AdminMessageEntity;
import com.radovan.spring.entity.BillingAddressEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.ShippingAddressEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.BillingAddressRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.ShippingAddressRepository;
import com.radovan.spring.repository.UserRepository;

@Component
public class TempConverter {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BillingAddressRepository billingAddressRepository;

	@Autowired
	private ShippingAddressRepository shippingAddressRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	private static DecimalFormat decfor = new DecimalFormat("0.00");

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public AdminMessageDto messageEntityToDto(AdminMessageEntity message) {
		AdminMessageDto returnValue = mapper.map(message, AdminMessageDto.class);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(message.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(message.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime createdAtZoned = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = createdAtZoned.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public AdminMessageEntity messageDtoToEntity(AdminMessageDto message) {
		AdminMessageEntity returnValue = mapper.map(message, AdminMessageEntity.class);

		Optional<Integer> customerIdOpt = Optional.ofNullable(message.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);
		Optional<Float> cartPriceOpt = Optional.ofNullable(cartEntity.getCartPrice());
		if (!cartPriceOpt.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(cartEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		List<Integer> itemsIds = new ArrayList<>();
		List<CartItemEntity> cartItems = cartEntity.getCartItems();
		cartItems.forEach((itemEntity) -> {
			Integer itemId = itemEntity.getCartItemId();
			itemsIds.add(itemId);
		});

		returnValue.setCartItemsIds(itemsIds);
		return returnValue;

	}

	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);
		Optional<Float> cartPriceOpt = Optional.ofNullable(cartDto.getCartPrice());
		if (!cartPriceOpt.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<Integer> customerIdOpt = Optional.ofNullable(cartDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		List<CartItemEntity> cartItems = new ArrayList<>();
		List<Integer> itemIds = cartDto.getCartItemsIds();

		itemIds.forEach((itemId) -> {
			CartItemEntity itemEntity = cartItemRepository.findById(itemId).get();
			cartItems.add(itemEntity);
		});

		returnValue.setCartItems(cartItems);
		return returnValue;
	}

	public CartItemDto cartItemEntityToDto(CartItemEntity cartItemEntity) {
		CartItemDto returnValue = mapper.map(cartItemEntity, CartItemDto.class);
		Optional<ProductEntity> productOpt = Optional.ofNullable(cartItemEntity.getProduct());
		if (productOpt.isPresent()) {
			returnValue.setProductId(productOpt.get().getProductId());
			Float price = productOpt.get().getProductPrice();
			Float discount = productOpt.get().getDiscount();
			Integer quantity = returnValue.getQuantity();
			price = (price - ((price / 100) * discount)) * quantity;
			price = Float.valueOf(decfor.format(price));
			returnValue.setPrice(price);
		}

		Optional<CartEntity> cartOpt = Optional.ofNullable(cartItemEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		return returnValue;
	}

	public CartItemEntity cartItemDtoToEntity(CartItemDto cartItemDto) {
		CartItemEntity returnValue = mapper.map(cartItemDto, CartItemEntity.class);
		Optional<Integer> cartIdOpt = Optional.ofNullable(cartItemDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> productIdOpt = Optional.ofNullable(cartItemDto.getProductId());
		if (productIdOpt.isPresent()) {
			Integer productId = productIdOpt.get();
			ProductEntity productEntity = productRepository.findById(productId).get();
			returnValue.setProduct(productEntity);
			Float price = productEntity.getProductPrice();
			Float discount = productEntity.getDiscount();
			Integer quantity = returnValue.getQuantity();
			price = (price - ((price / 100) * discount)) * quantity;
			price = Float.valueOf(decfor.format(price));
			returnValue.setPrice(price);
		}

		return returnValue;
	}

	public ProductDto productEntityToDto(ProductEntity productEntity) {
		ProductDto returnValue = mapper.map(productEntity, ProductDto.class);
		Float price = Float.valueOf(decfor.format(returnValue.getProductPrice()));
		Float discount = Float.valueOf(decfor.format(returnValue.getDiscount()));
		returnValue.setProductPrice(price);
		returnValue.setDiscount(discount);
		return returnValue;
	}

	public ProductEntity productDtoToEntity(ProductDto productDto) {
		ProductEntity returnValue = mapper.map(productDto, ProductEntity.class);
		Float price = Float.valueOf(decfor.format(returnValue.getProductPrice()));
		Float discount = Float.valueOf(decfor.format(returnValue.getDiscount()));
		returnValue.setProductPrice(price);
		returnValue.setDiscount(discount);
		return returnValue;
	}

	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);

		Optional<BillingAddressEntity> billingAddressEntity = Optional.ofNullable(customerEntity.getBillingAddress());
		if (billingAddressEntity.isPresent()) {
			returnValue.setBillingAddressId(billingAddressEntity.get().getBillingAddressId());
		}

		Optional<ShippingAddressEntity> shippingAddressEntity = Optional
				.ofNullable(customerEntity.getShippingAddress());
		if (shippingAddressEntity.isPresent()) {
			returnValue.setShippingAddressId(shippingAddressEntity.get().getShippingAddressId());
		}

		Optional<CartEntity> cartEntity = Optional.ofNullable(customerEntity.getCart());
		if (cartEntity.isPresent()) {
			returnValue.setCartId(cartEntity.get().getCartId());
		}

		Optional<UserEntity> userEntity = Optional.ofNullable(customerEntity.getUser());
		if (userEntity.isPresent()) {
			returnValue.setUserId(userEntity.get().getId());
		}

		return returnValue;
	}

	public CustomerEntity customerDtoToEntity(CustomerDto customerDto) {
		CustomerEntity returnValue = mapper.map(customerDto, CustomerEntity.class);

		Optional<Integer> billingAddressIdOpt = Optional.ofNullable(customerDto.getBillingAddressId());
		if (billingAddressIdOpt.isPresent()) {
			Integer billingAddressId = billingAddressIdOpt.get();
			BillingAddressEntity billingAddressEntity = billingAddressRepository.findById(billingAddressId).get();
			returnValue.setBillingAddress(billingAddressEntity);
		}

		Optional<Integer> shippingAddressIdOpt = Optional.ofNullable(customerDto.getShippingAddressId());
		if (shippingAddressIdOpt.isPresent()) {
			Integer shippingAddressId = shippingAddressIdOpt.get();
			ShippingAddressEntity shippingAddressEntity = shippingAddressRepository.findById(shippingAddressId).get();
			returnValue.setShippingAddress(shippingAddressEntity);
		}

		Optional<Integer> cartIdOpt = Optional.ofNullable(customerDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> userIdOpt = Optional.ofNullable(customerDto.getUserId());
		if (userIdOpt.isPresent()) {
			Integer userId = userIdOpt.get();
			UserEntity userEntity = userRepository.findById(userId).get();
			returnValue.setUser(userEntity);
		}

		return returnValue;
	}

	public BillingAddressDto billingAddressEntityToDto(BillingAddressEntity addressEntity) {
		BillingAddressDto returnValue = mapper.map(addressEntity, BillingAddressDto.class);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(addressEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}
		return returnValue;
	}

	public BillingAddressEntity billingAddressDtoToEntity(BillingAddressDto addressDto) {
		BillingAddressEntity returnValue = mapper.map(addressDto, BillingAddressEntity.class);

		Optional<Integer> customerIdOpt = Optional.ofNullable(addressDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}
		return returnValue;
	}

	public ShippingAddressDto shippingAddressEntityToDto(ShippingAddressEntity addressEntity) {
		ShippingAddressDto returnValue = mapper.map(addressEntity, ShippingAddressDto.class);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(addressEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		return returnValue;
	}

	public ShippingAddressEntity shippingAddressDtoToEntity(ShippingAddressDto addressDto) {
		ShippingAddressEntity returnValue = mapper.map(addressDto, ShippingAddressEntity.class);

		Optional<Integer> customerIdOpt = Optional.ofNullable(addressDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);

		Optional<OrderAddressEntity> addressEntity = Optional.ofNullable(orderEntity.getAddress());
		if (addressEntity.isPresent()) {
			returnValue.setAddressId(addressEntity.get().getOrderAddressId());
		}

		Optional<CustomerEntity> customerEntity = Optional.ofNullable(orderEntity.getCustomer());
		if (customerEntity.isPresent()) {
			returnValue.setCustomerId(customerEntity.get().getCustomerId());
		}

		Optional<CartEntity> cartEntity = Optional.ofNullable(orderEntity.getCart());
		if (cartEntity.isPresent()) {
			returnValue.setCartId(cartEntity.get().getCartId());
		}

		List<Integer> orderedItemsIds = new ArrayList<>();
		List<OrderItemEntity> orderedItems = orderEntity.getOrderedItems();
		orderedItems.forEach((itemEntity) -> {
			Integer itemId = itemEntity.getOrderItemId();
			orderedItemsIds.add(itemId);
		});

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime createdAtZoned = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = createdAtZoned.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		returnValue.setOrderedItemsIds(orderedItemsIds);

		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto orderDto) {
		OrderEntity returnValue = mapper.map(orderDto, OrderEntity.class);

		Optional<Integer> addressIdOpt = Optional.ofNullable(orderDto.getAddressId());
		if (addressIdOpt.isPresent()) {
			Integer addressId = addressIdOpt.get();
			OrderAddressEntity address = orderAddressRepository.findById(addressId).get();
			returnValue.setAddress(address);
		}

		Optional<Integer> customerIdOpt = Optional.ofNullable(orderDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		Optional<Integer> cartIdOpt = Optional.ofNullable(orderDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		List<OrderItemEntity> orderedItems = new ArrayList<>();
		List<Integer> orderedItemsIds = orderDto.getOrderedItemsIds();
		orderedItemsIds.forEach((itemId) -> {
			OrderItemEntity itemEntity = orderItemRepository.findById(itemId).get();
			orderedItems.add(itemEntity);
		});

		returnValue.setOrderedItems(orderedItems);
		return returnValue;
	}

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled(userEntity.getEnabled());
		Optional<List<RoleEntity>> rolesOpt = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOpt.isEmpty()) {
			rolesOpt.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOpt = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOpt.isEmpty()) {
			rolesIdsOpt.get().forEach((roleId) -> {
				RoleEntity role = roleRepository.findById(roleId).get();
				roles.add(role);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		List<UserEntity> users = roleEntity.getUsers();
		List<Integer> userIds = new ArrayList<>();

		users.forEach((user) -> {
			userIds.add(user.getId());
		});

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		List<Integer> usersIds = roleDto.getUsersIds();
		List<UserEntity> users = new ArrayList<>();

		usersIds.forEach((userId) -> {
			UserEntity userEntity = userRepository.findById(userId).get();
			users.add(userEntity);
		});

		returnValue.setUsers(users);
		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);

		Optional<OrderEntity> orderOpt = Optional.ofNullable(itemEntity.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);

		Optional<Integer> orderIdOpt = Optional.ofNullable(itemDto.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderOpt = Optional.ofNullable(address.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderIdOpt = Optional.ofNullable(address.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public OrderAddressEntity shippingAddressToOrderAddress(ShippingAddressEntity address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		return returnValue;
	}

	public OrderItemEntity cartItemToOrderItemEntity(CartItemEntity cartItemEntity) {
		OrderItemEntity returnValue = mapper.map(cartItemEntity, OrderItemEntity.class);

		Optional<ProductEntity> productOpt = Optional.ofNullable(cartItemEntity.getProduct());
		if (productOpt.isPresent()) {
			returnValue.setProductName(productOpt.get().getProductName());
			returnValue.setProductPrice(productOpt.get().getProductPrice());
			returnValue.setDiscount(productOpt.get().getDiscount());
		}

		return returnValue;
	}
}
