package com.teamium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.service.AuthenticationService;
import com.teamium.service.UserService;

/**
 * 
 * Handles operations related to logged in user like
 * 
 * @author TeamiumNishant
 *
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

	private UserService userService;
	private AuthenticationService authenticationService;

	@Autowired
	public UserController(UserService userService, AuthenticationService authenticationService) {
		this.userService = userService;
		this.authenticationService = authenticationService;
	}

	/**
	 * To logout a user. Service URL: /user/logout method: GET.
	 * 
	 * Service URL: /user/logout method: POST.
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	void logout() {
		authenticationService.revokeToken();
	}

	/**
	 * To change login first time login status of user
	 * 
	 * Service URL: /user/login-status method: PUT.
	 */
	@RequestMapping(value = "/login-status", method = RequestMethod.PUT)
	public void changeFirstTimeLoginStatus() {
		userService.changeFirstTimeLoginStatus();
	}

	/**
	 * To get logged in user's first time login status
	 * 
	 * Service URL: /user/login-status method: GET.
	 * 
	 * @return current first time login status.
	 */
	@RequestMapping(value = "/login-status", method = RequestMethod.GET)
	public boolean getFirstTimeLoginStatus() {
		return userService.getFirstTimeLoginStatus();
	}
}
