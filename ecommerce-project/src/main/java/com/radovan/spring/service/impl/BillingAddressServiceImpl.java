package com.radovan.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BillingAddressDto;
import com.radovan.spring.entity.BillingAddressEntity;
import com.radovan.spring.repository.BillingAddressRepository;
import com.radovan.spring.service.BillingAddressService;

@Service
@Transactional
public class BillingAddressServiceImpl implements BillingAddressService {

	@Autowired
	private BillingAddressRepository billingAddressRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public BillingAddressDto addBillingAddress(BillingAddressDto billingAddress) {
		// TODO Auto-generated method stub
		BillingAddressEntity billingAddressEntity = tempConverter.billingAddressDtoToEntity(billingAddress);
		BillingAddressEntity storedBillingAddress = billingAddressRepository.save(billingAddressEntity);
		BillingAddressDto returnValue = tempConverter.billingAddressEntityToDto(storedBillingAddress);
		return returnValue;
	}

	@Override
	public BillingAddressDto updateBillingAddress(Integer id, BillingAddressDto billingAddress) {
		// TODO Auto-generated method stub
		BillingAddressEntity tempBillingAddress = billingAddressRepository.findById(id).get();
		BillingAddressEntity billingAddressEntity = tempConverter.billingAddressDtoToEntity(billingAddress);
		billingAddressEntity.setCustomer(tempBillingAddress.getCustomer());
		billingAddressEntity.setBillingAddressId(id);
		BillingAddressEntity updatedBillingAddress = billingAddressRepository.saveAndFlush(billingAddressEntity);
		BillingAddressDto returnValue = tempConverter.billingAddressEntityToDto(updatedBillingAddress);
		return returnValue;
	}

	@Override
	public BillingAddressDto getBillingAddress(Integer addressId) {
		// TODO Auto-generated method stub
		Optional<BillingAddressEntity> addressEntity = billingAddressRepository.findById(addressId);
		BillingAddressDto returnValue = null;

		if (addressEntity.isPresent()) {
			returnValue = tempConverter.billingAddressEntityToDto(addressEntity.get());
		}
		return returnValue;
	}

	@Override
	public void deleteBillingAddress(Integer billingAddressId) {
		// TODO Auto-generated method stub
		billingAddressRepository.deleteById(billingAddressId);
		billingAddressRepository.flush();
	}
}
