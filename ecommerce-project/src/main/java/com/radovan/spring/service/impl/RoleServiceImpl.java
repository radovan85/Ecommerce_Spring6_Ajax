package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public List<RoleDto> listAllAuthorities() {
		// TODO Auto-generated method stub
		List<RoleEntity> allRoles = roleRepository.findAll();
		List<RoleDto> returnValue = new ArrayList<>();

		allRoles.forEach((role) -> {
			RoleDto roleDto = tempConverter.roleEntityToDto(role);
			returnValue.add(roleDto);
		});

		return returnValue;
	}

	@Override
	public RoleDto getRoleById(Integer id) {
		// TODO Auto-generated method stub
		Optional<RoleEntity> roleEntity = roleRepository.findById(id);
		RoleDto returnValue = null;

		if (roleEntity.isPresent()) {
			returnValue = tempConverter.roleEntityToDto(roleEntity.get());
		}
		return returnValue;
	}
}
