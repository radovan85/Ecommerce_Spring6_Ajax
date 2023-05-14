package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer productId;

	private String productCategory;

	private String productDescription;

	private String productStatus;

	private String productBrand;

	private String productModel;

	private String productName;

	private Float productPrice;

	private Integer unitStock;

	private Float discount;

	private String imageUrl;

	private List<String> categoryList;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getProductBrand() {
		return productBrand;
	}

	public void setProductBrand(String productBrand) {
		this.productBrand = productBrand;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Float getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Float productPrice) {
		this.productPrice = productPrice;
	}

	public Integer getUnitStock() {
		return unitStock;
	}

	public void setUnitStock(Integer unitStock) {
		this.unitStock = unitStock;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}

	public ProductDto() {
		categoryList = new ArrayList<String>();
		categoryList.add("Laptop");
		categoryList.add("Mobile");
		categoryList.add("Camera");
		categoryList.add("TV");
		categoryList.add("Refrigerator");
		categoryList.add("Tablet");
		categoryList.add("Micro Oven");
		categoryList.add("DVD Player");
		categoryList.add("Fan");
		categoryList.add("Printer");
		categoryList.add("Desktop");
		categoryList.add("Washing Machine");
		categoryList.add("ipad");
		categoryList.add("Game console");
		categoryList.add("Router");

	}

}
