package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.ProductDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.ProductEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.ProductRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartService cartService;

	@Override
	public List<ProductDto> listAll() {
		// TODO Auto-generated method stub
		List<ProductEntity> allProducts = productRepository.findAll();
		List<ProductDto> returnValue = new ArrayList<ProductDto>();

		allProducts.forEach((productEntity) -> {
			ProductDto productDto = tempConverter.productEntityToDto(productEntity);
			returnValue.add(productDto);
		});

		return returnValue;
	}

	@Override
	public ProductDto getProduct(Integer id) {
		// TODO Auto-generated method stub
		Optional<ProductEntity> productOpt = productRepository.findById(id);
		ProductDto returnValue = null;

		if (productOpt.isPresent()) {
			returnValue = tempConverter.productEntityToDto(productOpt.get());
		}
		return returnValue;
	}

	@Override
	public void deleteProduct(Integer id) {
		// TODO Auto-generated method stub
		productRepository.deleteById(id);
		productRepository.flush();
	}

	@Override
	public ProductDto addProduct(ProductDto product) {
		// TODO Auto-generated method stub
		Optional<Integer> productId = Optional.ofNullable(product.getProductId());
		ProductEntity productEntity = tempConverter.productDtoToEntity(product);
		ProductEntity storedProduct = productRepository.save(productEntity);
		ProductDto returnValue = tempConverter.productEntityToDto(storedProduct);

		if (productId.isPresent()) {
			List<CartItemEntity> allCartItems = cartItemRepository.findAllByProductId(productId.get());
			allCartItems.forEach((itemEntity) -> {
				Float price = returnValue.getProductPrice();
				price = (price - ((price / 100) * returnValue.getDiscount())) * itemEntity.getQuantity();
				itemEntity.setPrice(price);
				cartItemRepository.saveAndFlush(itemEntity);
			});

			List<CartEntity> allCarts = cartRepository.findAll();

			allCarts.forEach((cartEntity) -> {
				cartService.refreshCartState(cartEntity.getCartId());
			});

		}
		return returnValue;
	}

	@Override
	public List<ProductDto> listAllByKeyword(String keyword) {
		// TODO Auto-generated method stub
		List<ProductEntity> listResult = productRepository.findAllByKeyword(keyword);
		List<ProductDto> returnValue = new ArrayList<>();

		listResult.forEach((productEntity) -> {
			ProductDto productDto = tempConverter.productEntityToDto(productEntity);
			returnValue.add(productDto);
		});

		return returnValue;
	}

}
