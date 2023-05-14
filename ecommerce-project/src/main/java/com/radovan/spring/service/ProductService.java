package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.ProductDto;

public interface ProductService {

	List<ProductDto> listAll();

	ProductDto getProduct(Integer id);

	void deleteProduct(Integer id);

	ProductDto addProduct(ProductDto product);

	List<ProductDto> listAllByKeyword(String keyword);
}
