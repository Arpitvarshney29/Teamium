package com.teamium.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.teamium.config.PropConfig;
import com.teamium.domain.UserPasswordRecovery;
import com.teamium.domain.prod.resources.staff.StaffEmail;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.dto.StaffMemberDTO;
import com.teamium.dto.UserSettingDTO;
import com.teamium.exception.NotFoundException;
import com.teamium.exception.UnauthorizedException;
import com.teamium.exception.UnprocessableEntityException;
import com.teamium.repository.StaffMemberRepository;
import com.teamium.repository.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;
	private AuthenticationService authenticationService;
	private PasswordEncoder passwordEncoder;
	private EmailService emailService;
	private PropConfig propConfig;
	private StaffMemberRepository staffMemberRepository;
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public UserService(UserRepository userRepository, AuthenticationService authenticationService,
			PasswordEncoder passwordEncoder, EmailService emailService, PropConfig propConfig,
			StaffMemberRepository staffMemberRepository) {
		this.userRepository = userRepository;
		this.authenticationService = authenticationService;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.propConfig = propConfig;
		this.staffMemberRepository = staffMemberRepository;
	}

	public StaffMember findByLogin(String userName) {
		return userRepository.findByLogin(userName);
	}

	/**
	 * To change status of first time login of a user.
	 * 
	 */
	public void changeFirstTimeLoginStatus() {
		StaffMember staffMember = authenticationService.getAuthenticatedUser();

		if (staffMember != null) {
			staffMember.setFirstTimeLogin(false);
			userRepository.save(staffMember);
		} else {
			logger.info("Invalid user id.");
			throw new NotFoundException("Invalid user id.");
		}
	}

	/**
	 * To get status of first time login of a user.
	 * 
	 * @return status
	 */
	public boolean getFirstTimeLoginStatus() {
		StaffMember staffMember = authenticationService.getAuthenticatedUser();
		return staffMember.isFirstTimeLogin();
	}

	/**
	 * To update password of a logged-in-user
	 * 
	 * @param staffMemberDTO
	 *            the staffMemberDTO
	 * @param request
	 *            the request
	 */
	public void updatePassword(StaffMemberDTO staffMemberDTO, HttpServletRequest request) {
		logger.info(
				"Inside UserService:: updatePassword(), To change the password of logged-in user : staffMemberDTO = "
						+ staffMemberDTO);
		authenticationService.getAuthenticatedUser();
		if (staffMemberDTO.getUserSettingDTO() == null) {
			logger.info("Please provide user-setting.");
			throw new UnprocessableEntityException("Please provide user-setting.");
		}
		validatePassword(staffMemberDTO);
		StaffMember loggedInUser = authenticationService.getAuthenticatedUser();

		Set<StaffEmail> loggedInUserEmails = loggedInUser.getUserSetting().getEmails();
		Optional<StaffEmail> entityUser = loggedInUserEmails.stream().filter(email -> email.isPrimaryEmail() == true)
				.findFirst();
		StaffEmail staffEmail = null;
		try {
			staffEmail = entityUser.get();
		} catch (Exception e) {
			logger.info("Primary email not found.");
			throw new UnprocessableEntityException("Primary email not found.");
		}
		if (staffEmail == null) {
			logger.info("Primary email not found.");
			throw new UnprocessableEntityException("Primary email not found.");
		}
		StaffMember user = staffMemberRepository.findByEmail(staffEmail.getEmail());
		if (user == null) {
			logger.error("Staff-Member doesn't exist with this primary email.");
			throw new UnprocessableEntityException("Staff-Member doesn't exist with this primary email.");
		}
		if (user.getId() != loggedInUser.getId()
				&& !user.getUserSetting().getLogin().equals(loggedInUser.getUserSetting().getLogin())) {
			logger.error("Not allowed to update other staff password");
			throw new UnprocessableEntityException("Not allowed to update other staff password");
		}

		validateExistingPasswordAndEmail(staffMemberDTO, loggedInUser);
		user = loggedInUser;
		logger.info("updating password for staff-member : id = " + user.getId());
		updatePassword(user, staffMemberDTO.getUserSettingDTO().getPassword(), request);
		logger.info("sending mail to the staff-member : email = " + staffEmail.getEmail());
		emailService.sendPasswordResetNotificationMail(user, staffEmail.getEmail(),
				staffMemberDTO.getUserSettingDTO().getConfirmPassword());

		logger.info("Returning after changing the password of logged-in user.");
	}

	/**
	 * To validate password
	 * 
	 * @param staffMemberDTO
	 *            the staffMemberDTO
	 */
	public void validatePassword(StaffMemberDTO staffMemberDTO) {
		logger.info(
				"Indside UserService::validatePassword(), To validate staffMemberDTO for password fields. staffMemberDTO : "
						+ staffMemberDTO);
		if (staffMemberDTO.getUserSettingDTO() != null) {

			UserSettingDTO userSettingDTO = staffMemberDTO.getUserSettingDTO();

			if (!StringUtils.isBlank(userSettingDTO.getLogin())) {

				if (StringUtils.isBlank(userSettingDTO.getPassword()) || userSettingDTO.getPassword().length() < 6
						|| userSettingDTO.getPassword().length() > 20) {
					logger.error("Please enter a valid password.");
					throw new UnprocessableEntityException("Please enter a valid password.");
				}

				if (StringUtils.isBlank(userSettingDTO.getConfirmPassword())
						|| userSettingDTO.getConfirmPassword().length() > 20
						|| userSettingDTO.getPassword().length() < 6) {
					logger.error("Please enter a valid confirm password.");
					throw new UnprocessableEntityException("Please enter a valid confirm password.");
				}

				if (!userSettingDTO.getConfirmPassword().equals(userSettingDTO.getPassword())) {
					logger.error("Please enter a same value in password and confirm password field.");
					throw new UnprocessableEntityException(
							"Please enter a same value in password and confirm password field.");
				}
			}
			logger.info("Returning after validating StaffMemberDTO for password fields.");
		}
	}

	/**
	 * To update user password
	 * 
	 * @param staffMember
	 *            the staffMember
	 * @param password
	 *            the password
	 * @param request
	 *            the request
	 */
	public void updatePassword(StaffMember staffMember, String password, HttpServletRequest request) {
		logger.info("Inside UserService:: updatePassword(), To update the password of a staff-member : id = "
				+ staffMember.getId());
		staffMember.getUserSetting().setPassword(passwordEncoder.encode(password.trim()));
		userRepository.save(staffMember);
		authenticationService.revokeToken(request);
		// authenticationService.revokeAllTokens(request,
		// staffMember.getUserSetting().getEmail());
		logger.info("Returning after updating the password of a staff-member.");
	}

	/**
	 * Method to validate existing password
	 * 
	 * @param staffMemberDTO
	 *            the staffMemberDTO
	 * @param staffMember
	 *            the staffMember
	 */
	public void validateExistingPasswordAndEmail(StaffMemberDTO staffMemberDTO, StaffMember staffMember) {
		logger.info(
				"Inside UserService::validateExistingPasswordAndEmail(), To validate the existing password is same as in db. staffMemberDTO : "
						+ staffMemberDTO);
		if (StringUtils.isBlank(staffMemberDTO.getUserSettingDTO().getExistingPassword())) {
			logger.info("Please enter existing password.");
			throw new UnprocessableEntityException("Please enter existing password.");
		}

		if (!passwordEncoder.matches(staffMemberDTO.getUserSettingDTO().getExistingPassword(),
				staffMember.getUserSetting().getPassword())) {
			logger.info("Incorrect Password");
			throw new UnauthorizedException("Incorrect Password");
		}

		// if
		// (!staffMemberDTO.getUserSettingDTO().getEmail().equals(staffMember.getUserSetting().getEmail()))
		// {
		// logger.info("Please provide the same email.");
		// throw new UnauthorizedException("Please provide the same email");
		// }
		logger.info("Returning after validating the existing password is same as in db.");
	}

	/**
	 * To send password recovery Mail.
	 * 
	 * @param userDTO
	 *            the userDTO object.
	 */
	public void passwordRecoveryRequest(StaffMemberDTO staffMemberDTO, HttpServletRequest request) {
		logger.info("Inside UserService::passwordRecoveryRequest(),"
				+ " To send password recovery Mail. staffMemberDTO : " + staffMemberDTO.toString());
		UserSettingDTO userSettingDTO = staffMemberDTO.getUserSettingDTO();
		if (userSettingDTO == null) {
			logger.info("Please provide user-setting.");
			throw new UnprocessableEntityException("Please provide user-setting.");
		}
		StaffMember staffMember = staffMemberRepository.findByEmail(userSettingDTO.getEmail());
		if (staffMember == null) {
			logger.error("Staff-Member doesn't exist with this primary email.");
			throw new NotFoundException("Staff-Member doesn't exist with this primary email.");
		}

		UserPasswordRecovery userPasswordRecovery = staffMember.getUserPasswordRecovery();
		if (userPasswordRecovery == null) {
			userPasswordRecovery = new UserPasswordRecovery();
		}

		String token = createRandomToken();
		userPasswordRecovery.setCreatedOn(Calendar.getInstance());
		userPasswordRecovery.setToken(token);
		userPasswordRecovery.setUser(staffMember);
		staffMember.setUserPasswordRecovery(userPasswordRecovery);

		userRepository.save(staffMember);
		try {
			logger.info("Sending mail after password recovery-request.");
			emailService.sendPasswordRecoveryRequest(token, request, staffMember,
					staffMemberDTO.getUserSettingDTO().getEmail());
		} catch (MailException e) {
			logger.info("Exception while sending message after password recovery-request.");
			logger.error(e.getMessage());
			throw new UnprocessableEntityException("Exception while sending message after password recovery-request.");
		}
		logger.info("Returning after sending password recovery Mail.");
	}

	/**
	 * Method to Generate a random token.
	 *
	 * @return the random token
	 */
	private String createRandomToken() {
		logger.info("Inside UserService::createRandomToken(), To generate a random token.");
		final String LETTERS = "ABCDEFGHIJLMNOPQRSTUVXZ123456789";

		final int LENGTH = 40;
		int i = 0;

		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		while (i < LENGTH) {
			char randomChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
			sb.append(randomChar);
			i++;
		}
		logger.info("Returning after generating a random token.");
		return sb.toString();
	}

	/**
	 * To check the expiration of password recovery link.
	 * 
	 * @param userDTO
	 *            the userDTO object.
	 * 
	 * @return boolean.
	 */
	public boolean validateRecoveryLink(StaffMemberDTO staffMemberDTO) {
		logger.info("Inside UserService::validateRecoveryLink(), To check the expiration of password recovery link. :"
				+ " staffMemberDTO = " + staffMemberDTO.toString());
		try {
			validatePasswordRecoveryWrapper(staffMemberDTO);
			StaffMember user = staffMemberRepository.findByEmail(staffMemberDTO.getUserSettingDTO().getEmail());
			user.getUserPasswordRecovery().setLinkValidated(true);
			userRepository.save(user);
			logger.info("Returning after checking the expiration of password recovery link.");
			return Boolean.TRUE;
		} catch (UnauthorizedException e) {
			logger.error("Link has been expired.");
			return Boolean.FALSE;
		}
	}

	/**
	 * To validate StaffMemberDTO for password recovery.
	 * 
	 * @param staffMemberDTO
	 *            the staffMemberDTO object.
	 * 
	 * @return the staffMember.
	 */
	private StaffMember validatePasswordRecoveryWrapper(StaffMemberDTO staffMemberDTO) {
		logger.info(
				"Inside UserService::validatePasswordRecoveryWrapper(), To validate StaffMemberDTO for password recovery : "
						+ staffMemberDTO.toString());
		UserSettingDTO userSettingDTO = staffMemberDTO.getUserSettingDTO();
		if (userSettingDTO == null) {
			logger.info("Please provide user-setting.");
			throw new UnprocessableEntityException("Please provide user-setting.");
		}
		StaffMember user = staffMemberRepository.findByEmail(userSettingDTO.getEmail());

		if (user == null) {
			logger.error("Invalid primary email.");
			throw new UnprocessableEntityException("Invalid primary email.");
		}

		if (StringUtils.isBlank(userSettingDTO.getPasswordRecoveryToken())) {
			logger.error("Please enter a password recovery token.");
			throw new UnprocessableEntityException("Please enter a password recovery token.");
		}

		UserPasswordRecovery userPasswordRecovery = user.getUserPasswordRecovery();

		if (userPasswordRecovery == null) {
			logger.error("Password cannot be recovered.");
			throw new UnprocessableEntityException("Password cannot be recovered.");
		}

		TimeZone tz = userPasswordRecovery.getCreatedOn().getTimeZone();

		DateTimeZone jodaTz = DateTimeZone.forID(tz.getID());
		DateTime dateTime = new DateTime(userPasswordRecovery.getCreatedOn().getTimeInMillis(), jodaTz);
		LocalDate localDate = dateTime.toLocalDate();

		Duration duration = new Duration(dateTime, DateTime.now());

		if (duration.getStandardHours() > propConfig.getPasswordRecoveryExpirationHours()) {
			logger.error("Link has been expired.");
			throw new UnauthorizedException("Link has been expired.");
		}

		if (!userPasswordRecovery.getToken().equals(userSettingDTO.getPasswordRecoveryToken())) {
			logger.error("Unauthorized access.");
			throw new UnauthorizedException("Unauthorized access.");
		}
		logger.info("Returning after validating UserDTO for password recovery.");
		return user;
	}

	/**
	 * To recover the password.
	 * 
	 * @param userDTO
	 *            the userDTO object.
	 */
	public void recoverPassword(StaffMemberDTO staffMemberDTO, HttpServletRequest request) {
		logger.info("Inside UserService::recoverPassword(), To recover the password. : staffMemberDTO = "
				+ staffMemberDTO.toString());
		StaffMember user = validatePasswordRecoveryWrapper(staffMemberDTO);
		if (user.getUserPasswordRecovery().isLinkValidated() == false) {
			logger.info("Recovery Link is not validated.");
			throw new UnprocessableEntityException("Recovery Link is not validated.");
		}
		validatePassword(staffMemberDTO);
		user.getUserPasswordRecovery().setToken(createRandomToken());
		user.getUserPasswordRecovery().setLinkValidated(false);
		updatePassword(user, staffMemberDTO.getUserSettingDTO().getPassword(), request);
		logger.info("Returning after recovering the password.");
	}

	/**
	 * To validate the assigned login
	 * 
	 * @param loginName
	 *            the assigned login.
	 */
	public void validateAssignedLogin(StaffMemberDTO staffMemberDTO) {
		logger.info("Inside UserService::validateLoginAssigned(), To validate StaffMemberDTO for assigned login name : "
				+ staffMemberDTO.toString());

		UserSettingDTO userSettingDTO = staffMemberDTO.getUserSettingDTO();

		if (userSettingDTO == null) {
			logger.info("Please provide user setting");
			throw new UnprocessableEntityException("Please provide user setting");
		}
		String loginName = userSettingDTO.getLogin();

		// if (StringUtils.isBlank(loginName)) {
		// logger.info("Please provide valid assigned-login name.");
		// throw new UnprocessableEntityException("Please provide valid assigned-login
		// name.");
		// }

		if (!StringUtils.isBlank(loginName) && !(loginName.length() >= 6 && loginName.length() < 65)) {
			System.out.println("loginName.length()" + loginName.length());
			logger.info("Assigned login name must be in between 6 and 65 characters long.");
			throw new UnprocessableEntityException("Assigned login name must be in between 6 and 65 characters long.");
		}

		if (!StringUtils.isBlank(loginName) && userRepository.findByLogin(loginName) != null) {
			logger.info("Assigned login name already exist.");
			throw new UnprocessableEntityException("Assigned login name already exist.");
		}

		logger.info("Returning after validating assigned login.");
	}

}
