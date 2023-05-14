package com.radovan.spring.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.exceptions.InvalidUserException;
import com.radovan.spring.exceptions.SuspendedUserException;
import com.radovan.spring.form.RegistrationForm;
import com.radovan.spring.service.CustomerService;

@Controller
public class MainController {

	@Autowired
	private CustomerService customerService;

	@GetMapping(value = "/")
	public String sayIndex() {

		return "index";
	}

	@GetMapping(value = "/home")
	public String home() {

		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, ModelMap map) {
		if (error != null) {
			map.put("error", "Invalid username and Password");
		}

		if (logout != null) {
			map.put("logout", "You have logged out successfully");
		}
		return "fragments/login :: ajaxLoadedContent";
	}

	@GetMapping(value = "/userRegistration")
	public String register(ModelMap map) {

		RegistrationForm tempForm = new RegistrationForm();
		map.put("tempForm", tempForm);
		return "fragments/registration :: ajaxLoadedContent";
	}

	@PostMapping(value = "/userRegistration")
	public String storeUser(@ModelAttribute("tempForm") RegistrationForm form) {
		customerService.storeCustomer(form);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/registerComplete")
	public String registrationCompl() {
		return "fragments/registration_completed :: ajaxLoadedContent";
	}

	@GetMapping(value = "/registerFail")
	public String registrationFail() {
		return "fragments/registration_failed :: ajaxLoadedContent";
	}

	@PostMapping(value = "/loggedout")
	public String logout() {
		SecurityContext context = SecurityContextHolder.getContext();
	    context.setAuthentication(null);
	    SecurityContextHolder.clearContext();
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/loginErrorPage")
	public String logError(ModelMap map) {
		map.put("alert", "Invalid username or password!");
		return "fragments/login :: ajaxLoadedContent";
	}

	@GetMapping(value = "/suspensionPage")
	public String suspensionAlert(ModelMap map) {
		map.put("alert", "Account suspended!");
		return "fragments/login :: ajaxLoadedContent";
	}

	@PostMapping(value = "/loginPassConfirm")
	public String confirmLoginPass(Principal principal) {
		Optional<Principal> authPrincipal = Optional.ofNullable(principal);
		if (!authPrincipal.isPresent()) {
			Error error = new Error("Invalid user");
			throw new InvalidUserException(error);
		}

		return "fragments/homePage :: ajaxLoadedContent";
	}

	@PostMapping(value = "/suspensionChecker")
	public String checkForSuspension() {
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (authUser.getEnabled() == (byte) 0) {
			Error error = new Error("Account suspended!");
			throw new SuspendedUserException(error);
		}

		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/aboutUs")
	public String aboutPage() {
		return "fragments/about :: ajaxLoadedContent";
	}
}
