package com.teamium.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.teamium.dto.StaffMemberDTO;
import com.teamium.service.UserService;

/**
 * <p>
 * Handles operations related to password like: updating password, creating
 * password recovery request, recover password, validate password recovery link
 * </p>
 */
@RestController
@RequestMapping("/password")
public class PasswordController {

	private UserService userService;

	@Autowired
	public PasswordController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * To update the password of logged-in user.
	 * 
	 * Service URL: /password/update , method: POST.
	 * 
	 * @param userDTO
	 *            the UserDTO object.
	 * 
	 * @param request
	 *            the HttpServletRequest object.
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	void updatePassword(@RequestBody StaffMemberDTO staffMemberDTO, HttpServletRequest request) {
		userService.updatePassword(staffMemberDTO, request);
	}

	/**
	 * To send password recovery mail.
	 * 
	 * Service URL: /password/recovery-request , method: POST.
	 * 
	 * @param userDTO
	 *            the UserDTO object.
	 */
	@RequestMapping(value = "/recovery-request", method = RequestMethod.POST)
	void recoveryRequest(@RequestBody StaffMemberDTO staffMemberDTO, HttpServletRequest request) {
		userService.passwordRecoveryRequest(staffMemberDTO, request);
	}

	/**
	 * To recover password.
	 * 
	 * Service URL: /password/recover method: POST.
	 * 
	 * @param userDTO
	 *            the UserDTO object.
	 * 
	 * @param request
	 *            the HttpServletRequest object.
	 */
	@RequestMapping(value = "/recover", method = RequestMethod.POST)
	void recoverPassword(@RequestBody StaffMemberDTO userDTO, HttpServletRequest request) {
		userService.recoverPassword(userDTO, request);
	}

	/**
	 * To check the expiration of password recovery link.
	 * 
	 * Service URL: /password/recovery-link/validate , method: POST.
	 * 
	 * @param userDTO
	 *            the UserDTO object.
	 */
	@RequestMapping(value = "/recovery-link/validate", method = RequestMethod.POST)
	boolean validateRecoveryLink(@RequestBody StaffMemberDTO staffMemberDTO) {
		return userService.validateRecoveryLink(staffMemberDTO);
	}

}
