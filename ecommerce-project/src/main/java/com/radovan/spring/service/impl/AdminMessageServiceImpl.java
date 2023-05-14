package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.AdminMessageDto;
import com.radovan.spring.entity.AdminMessageEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.AdminMessageRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.service.AdminMessageService;

@Service
@Transactional
public class AdminMessageServiceImpl implements AdminMessageService{

	@Autowired
	private AdminMessageRepository messageRepository;
	
	@Autowired
	private TempConverter tempConverter;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public AdminMessageDto addMessage(AdminMessageDto message) {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		CustomerEntity customerEntity = customerRepository.findByUserId(authUser.getId());
		AdminMessageEntity messageEntity = tempConverter.messageDtoToEntity(message);
		messageEntity.setCustomer(customerEntity);
		ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Belgrade"));
		Timestamp createdAt = new Timestamp(currentTime.toInstant().getEpochSecond() * 1000L);
		messageEntity.setCreatedAt(createdAt);
		AdminMessageEntity storedMessage = messageRepository.save(messageEntity);
		AdminMessageDto returnValue = tempConverter.messageEntityToDto(storedMessage);
		return returnValue;
	}

	

	@Override
	public void deleteMessage(Integer messageId) {
		// TODO Auto-generated method stub
		messageRepository.deleteById(messageId);
		messageRepository.flush();
	}



	@Override
	public AdminMessageDto getMessage(Integer messageId) {
		// TODO Auto-generated method stub
		Optional<AdminMessageEntity> messageOpt = messageRepository.findById(messageId);
		AdminMessageDto returnValue = null;
		
		if(messageOpt.isPresent()) {
			returnValue = tempConverter.messageEntityToDto(messageOpt.get());
		}
		return returnValue;
	}



	@Override
	public List<AdminMessageDto> listAll() {
		// TODO Auto-generated method stub
		List<AdminMessageEntity> allMessages = messageRepository.findAll();
		List<AdminMessageDto> returnValue = new ArrayList<>();
		
		allMessages.forEach((messageEntity) -> {
			AdminMessageDto messageDto = tempConverter.messageEntityToDto(messageEntity);
			returnValue.add(messageDto);
		});
		
		return returnValue;
	}



	@Override
	public void deleteAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		messageRepository.deleteAllByCustomerId(customerId);
		messageRepository.flush();
	}
}
