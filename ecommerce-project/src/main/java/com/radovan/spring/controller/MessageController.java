package com.radovan.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.AdminMessageDto;
import com.radovan.spring.service.AdminMessageService;

@Controller
@RequestMapping(value = "/message")
public class MessageController {

	@Autowired
	private AdminMessageService messageService;

	@GetMapping(value = "/sendMessage")
	public String renderMessageForm(ModelMap map) {
		AdminMessageDto message = new AdminMessageDto();
		map.put("message", message);
		return "fragments/messageForm :: ajaxLoadedContent";
	}

	@PostMapping(value = "/sendMessage")
	public String sendMessage(@ModelAttribute("message") AdminMessageDto message) {
		messageService.addMessage(message);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/messageSent")
	public String messageResult() {
		return "fragments/messageSent :: ajaxLoadedContent";
	}
}
