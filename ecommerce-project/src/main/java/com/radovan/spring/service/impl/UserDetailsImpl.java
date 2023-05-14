package com.radovan.spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.service.UserService;

@Service
public class UserDetailsImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserEntity loadUserByUsername(String name) {

		UserEntity returnValue = null;
		Optional<UserEntity> userOpt = Optional.ofNullable(userService.getUserByEmail(name));
		if (!userOpt.isPresent()) {
			Error error = new Error("Invalid user");
			throw new InvalidUserException(error);
		} else {
			returnValue = userOpt.get();
		}

		return returnValue;

	}
}
