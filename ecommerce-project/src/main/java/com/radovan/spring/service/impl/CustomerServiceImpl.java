package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BillingAddressDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.ShippingAddressDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.entity.BillingAddressEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.ShippingAddressEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.ExistingEmailException;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.form.RegistrationForm;
import com.radovan.spring.repository.BillingAddressRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.ShippingAddressRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BillingAddressRepository billAddressRepository;

	@Autowired
	private ShippingAddressRepository shippAddressRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public List<CustomerDto> getAllCustomers() {
		// TODO Auto-generated method stub
		List<CustomerEntity> allCustomers = customerRepository.findAll();
		List<CustomerDto> returnValue = new ArrayList<CustomerDto>();

		allCustomers.forEach((customer) -> {
			CustomerDto customerDto = tempConverter.customerEntityToDto(customer);
			returnValue.add(customerDto);
		});

		return returnValue;
	}

	@Override
	public CustomerDto getCustomer(Integer id) {
		// TODO Auto-generated method stub
		Optional<CustomerEntity> customerOpt = customerRepository.findById(id);
		CustomerDto returnValue = new CustomerDto();
		if (customerOpt.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerOpt.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new InvalidUserException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto getCustomerByUserId(Integer userId) {
		// TODO Auto-generated method stub

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByUserId(userId));
		CustomerDto returnValue = new CustomerDto();
		if (customerOpt.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerOpt.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new InvalidUserException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto getCustomerByCartId(Integer cartId) {
		// TODO Auto-generated method stub

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByCartId(cartId));
		CustomerDto returnValue = new CustomerDto();
		if (customerOpt.isPresent()) {
			returnValue = tempConverter.customerEntityToDto(customerOpt.get());
		} else {
			Error error = new Error("Invalid Customer");
			throw new InvalidUserException(error);
		}
		return returnValue;
	}

	@Override
	public CustomerDto updateCustomer(Integer customerId, CustomerDto customer) {
		// TODO Auto-generated method stub
		Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
		CustomerDto returnValue = null;

		if (customerOpt.isPresent()) {
			customer.setCustomerId(customerId);
			CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customer);
			CustomerEntity updatedCustomer = customerRepository.saveAndFlush(customerEntity);
			returnValue = tempConverter.customerEntityToDto(updatedCustomer);
		}
		return returnValue;
	}

	@Override
	public CustomerDto storeCustomer(RegistrationForm form) {
		// TODO Auto-generated method stub
		UserDto userDto = form.getUser();
		Optional<UserEntity> testUser = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
		if (testUser.isPresent()) {
			Error error = new Error("Email exists");
			throw new ExistingEmailException(error);
		}

		RoleEntity role = roleRepository.findByRole("ROLE_USER");
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userDto.setEnabled((byte) 1);
		List<RoleEntity> roles = new ArrayList<RoleEntity>();
		roles.add(role);
		UserEntity userEntity = tempConverter.userDtoToEntity(userDto);
		userEntity.setRoles(roles);
		userEntity.setEnabled((byte) 1);
		UserEntity storedUser = userRepository.save(userEntity);
		List<UserEntity> users = new ArrayList<UserEntity>();
		users.add(storedUser);
		role.setUsers(users);
		roleRepository.saveAndFlush(role);

		BillingAddressDto billAddressDto = form.getBillingAddress();
		BillingAddressEntity billAddressEntity = tempConverter.billingAddressDtoToEntity(billAddressDto);
		BillingAddressEntity storedBillAddress = billAddressRepository.save(billAddressEntity);

		ShippingAddressDto shippAddressDto = form.getShippingAddress();
		ShippingAddressEntity shippAddressEntity = tempConverter.shippingAddressDtoToEntity(shippAddressDto);
		ShippingAddressEntity storedShippAddress = shippAddressRepository.save(shippAddressEntity);

		CartEntity cartEntity = new CartEntity();
		cartEntity.setCartPrice(0f);
		CartEntity storedCart = cartRepository.save(cartEntity);

		CustomerDto customerDto = form.getCustomer();
		customerDto.setUserId(storedUser.getId());
		customerDto.setCartId(storedCart.getCartId());
		customerDto.setBillingAddressId(storedBillAddress.getBillingAddressId());
		customerDto.setShippingAddressId(storedShippAddress.getShippingAddressId());
		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customerDto);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);

		storedCart.setCustomer(storedCustomer);
		cartRepository.saveAndFlush(storedCart);

		storedBillAddress.setCustomer(storedCustomer);
		billAddressRepository.saveAndFlush(storedBillAddress);

		storedShippAddress.setCustomer(storedCustomer);
		shippAddressRepository.saveAndFlush(storedShippAddress);

		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}

	@Override
	public CustomerDto addCustomer(CustomerDto customer) {
		// TODO Auto-generated method stub

		CustomerEntity customerEntity = tempConverter.customerDtoToEntity(customer);
		CustomerEntity storedCustomer = customerRepository.save(customerEntity);
		CustomerDto returnValue = tempConverter.customerEntityToDto(storedCustomer);
		return returnValue;
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		customerRepository.deleteById(customerId);
		customerRepository.flush();
	}

	@Override
	public void resetCustomer(Integer customerId) {
		// TODO Auto-generated method stub
		Optional<CustomerEntity> customerOptional = customerRepository.findById(customerId);
		if (customerOptional.isPresent()) {
			Optional<CartEntity> cartOptional = Optional.ofNullable(customerOptional.get().getCart());
			if (cartOptional.isPresent()) {
				CartEntity cartEntity = cartOptional.get();
				cartEntity.setCustomer(null);
				cartRepository.saveAndFlush(cartEntity);
				CustomerEntity customerEntity = customerOptional.get();
				customerEntity.setCart(null);
				customerEntity.setBillingAddress(null);
				customerEntity.setShippingAddress(null);
				customerEntity.setUser(null);
				customerRepository.saveAndFlush(customerEntity);
			}
		}
	}
}
