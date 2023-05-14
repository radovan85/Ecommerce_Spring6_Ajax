package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.AdminMessageDto;

public interface AdminMessageService {

	AdminMessageDto addMessage(AdminMessageDto review);

	AdminMessageDto getMessage(Integer messageId);

	List<AdminMessageDto> listAll();

	void deleteMessage(Integer messageId);

	void deleteAllByCustomerId(Integer customerId);
}
