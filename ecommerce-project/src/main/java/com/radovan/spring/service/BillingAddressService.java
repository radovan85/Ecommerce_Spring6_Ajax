package com.radovan.spring.service;

import com.radovan.spring.dto.BillingAddressDto;

public interface BillingAddressService {

	BillingAddressDto addBillingAddress(BillingAddressDto billingAddress);

	BillingAddressDto updateBillingAddress(Integer id, BillingAddressDto billingAddress);

	BillingAddressDto getBillingAddress(Integer addressId);

	void deleteBillingAddress(Integer billingAddressId);
}
