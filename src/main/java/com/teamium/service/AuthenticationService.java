package com.teamium.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.teamium.config.PropConfig;
import com.teamium.domain.Role;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.exception.UnauthorizedException;
import com.teamium.repository.UserRepository;
import com.teamium.security.TeamiumUserDetails;

/**
 * A service class implementation for Authentication Controller
 *
 */
@Service
public class AuthenticationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/// private static GoogleAuthenticator googleAuthenticator = new
	/// GoogleAuthenticator();

	private UserRepository userRepository;
	private TokenStore tokenStore;
	private DefaultTokenServices tokenServices;
	private PropConfig propConfig;

	@Autowired
	public AuthenticationService(UserRepository userRepository, TokenStore tokenStore,
			DefaultTokenServices tokenServices, PropConfig propConfig) {
		this.userRepository = userRepository;
		this.tokenStore = tokenStore;
		this.tokenServices = tokenServices;
		this.propConfig = propConfig;
	}

	/**
	 * Method to get authenticated user
	 * 
	 * @return the authenticated User
	 */
	public StaffMember getAuthenticatedUser() {
		logger.info("Inside AuthenticationService::getAuthenticatedUser(), To get the authenticated user.");
		TeamiumUserDetails teamiumUserDetails;
		try {
			teamiumUserDetails = (TeamiumUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
		} catch (Exception e) {
			logger.error("Unauthorized Access.", e.getMessage());
			throw new UnauthorizedException("Unauthorized Access.");
		}
		StaffMember user = userRepository.findOne(teamiumUserDetails.getId());

		// if (!user.isActive()) {
		// logger.error("This user no longer exists in our system.");
		// throw new UserDeletedException("This user no longer exists in our
		// system.");
		// }
		logger.info("Returning the authenticated user.");
		return user;
	}

	/**
	 * Method to revoke the access-token
	 * 
	 */
	public void revokeToken() {
		logger.info("Inside AuthenticationService::revokeToken(), To revoke token.");
		OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext()
				.getAuthentication();
		OAuth2AccessToken token = tokenStore.getAccessToken(oAuth2Authentication);
		tokenStore.removeAccessToken(token);
		logger.info("Returning after revoking Token.");
	}

	/**
	 * Method to revoke access-token
	 * 
	 * @param token
	 *            the token
	 */
	public void revokeToken(String token) {
		logger.info("Inside AuthenticationService::revokeToken(), To revoke token : " + token);
		tokenServices.revokeToken(token);
		logger.info("Returning after revoking Token : " + token);
	}

	/**
	 * Method to revoke the access-token
	 * 
	 * @param request
	 *            the request
	 */
	public void revokeToken(HttpServletRequest request) {
		logger.info("Inside AuthenticationService::revokeToken(), To revoke token.");
		try {
			String authHeader = request.getHeader("Authorization");
			if (!StringUtils.isBlank(authHeader)) {
				String tokenValue = authHeader.replace("Bearer", "").trim();
				revokeToken(tokenValue);
			}
		} catch (Exception e) {
			logger.info("Token not found");
		}
		logger.info("Returning after revoking Token.");
	}

	/**
	 * To revoke all the tokens of logged-in user.
	 * 
	 * @param request
	 *            the HttpServletRequest object.
	 */
	/*
	 * public void revokeAllTokens(HttpServletRequest request, String userName) {
	 * logger.info("To revoke all the tokens of logged-in user.");
	 * List<OAuth2AccessToken> tokens = new
	 * ArrayList<OAuth2AccessToken>(tokenStore.findTokensByClientIdAndUserName(
	 * propConfig.getWebAppClientId(), userName));
	 * tokens.addAll(tokenStore.findTokensByClientIdAndUserName(propConfig.
	 * getMobileAppClientId(), userName)); tokens.stream().forEach( token -> {
	 * request.setAttribute(Constants.UUID, ((Map<String, Object>)
	 * token.getAdditionalInformation().get(Constants.ADDITIONAL_INFO))
	 * .get(Constants.UUID)); tokenStore.removeAccessToken(token);
	 * request.removeAttribute(Constants.UUID); });
	 * logger.info("Returning after revoking all the tokens of logged-in user." ); }
	 */

	/**
	 * To validate whether a logged in user has a role EquipmentManager or not
	 * 
	 * @return true if yes or false
	 */
	public boolean isEquipmentManager() {
		logger.info(
				"Inside AuthenticationService::isEquipmentManager(), To check if logged-in-user is EquipmentManager.");
		return getAuthenticatedUser().getRole().stream()
				.filter(role -> role.getRoleName().equals(Role.RoleName.EQUIPMENT_MANAGER.getRoleNameString()))
				.collect(Collectors.toList()).size() > 0;
	}

	/**
	 * To validate whether a logged in user has a role ADMINISTRATOR or not
	 * 
	 * @return true if yes or false
	 */
	public boolean isAdmin() {
		logger.info("Inside AuthenticationService :: isAdmin(), To check if logged-in-user is Admin.");
		return true;
		// return getAuthenticatedUser().getRole().stream()
		// .filter(role -> role.getRoleName().toLowerCase()
		// .equalsIgnoreCase(Role.RoleName.Administrator.getRoleNameString().toLowerCase()))
		// .collect(Collectors.toList()).size() > 0;
	}

	/**
	 * To validate whether a logged in user has a role Human-Resource or not
	 * 
	 * @return true if yes or false
	 */
	public boolean isHumanResource() {
		logger.info("Inside AuthenticationService::isHumanResource(), To check if logged-in-user is isHumanResource.");
		return getAuthenticatedUser().getRole().stream()
				.filter(role -> role.getRoleName().toLowerCase()
						.equalsIgnoreCase(Role.RoleName.Human_Resources.getRoleNameString().toLowerCase()))
				.collect(Collectors.toList()).size() > 0;
	}

	/**
	 * To revoke tokens from all the logged-in devices
	 * 
	 * @param request
	 * @param userName
	 */
	public void revokeAllTokens(HttpServletRequest request, String userName) {
		logger.info(
				"Inside AuthenticationService::revokeAllTokens(), To revoke all the tokens of logged-in user : username = "
						+ userName);
		List<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>(
				tokenStore.findTokensByClientIdAndUserName(propConfig.getWebAppClientId(), userName));
		tokens.addAll(tokenStore.findTokensByClientIdAndUserName(propConfig.getMobileAppClientId(), userName));
		tokens.addAll(tokenStore.findTokensByClientIdAndUserName(propConfig.getSwaggerClientId(), userName));
		tokens.stream().forEach(token -> {
			tokenStore.removeAccessToken(token);
		});
		logger.info("Returning after revoking all the tokens of logged-in user.");
	}

	/**
	 * To get user's timezone
	 * 
	 */
	public DateTimeZone getUserTimeZone() {
		return DateTimeZone.forID("Asia/Calcutta");
	}

	/**
	 * To get joda time from string
	 * 
	 */
	public DateTime getJodaTime(String dateTime) {
		return DateTime.parse(dateTime).withZone(this.getUserTimeZone());
	}

	/**
	 * To get joda time from string
	 * 
	 */
	public Calendar getCalanderTime(String dateTime) {
		Calendar calendar = Calendar.getInstance(this.getUserTimeZone().toTimeZone());
		calendar.setTimeInMillis(this.getJodaTime(dateTime).getMillis());
		return calendar;
	}

	/**
	 * To validate whether a logged in user has a role Manager or not
	 * 
	 * 
	 * @return true if yes or false
	 */
	public boolean isManager() {
		logger.info("Inside AuthenticationService::isManager(), To check if logged-in-user is Manager.");

		return getAuthenticatedUser().getRole().stream().filter(role -> {
			String roleName = role.getRoleName();
			if (roleName.equals(Role.RoleName.Administrator.getRoleNameString())) {
				return true;
			} else if (roleName.equals(Role.RoleName.Human_Resources.getRoleNameString())) {
				return true;
			} else if (roleName.equals(Role.RoleName.Production_Manager.getRoleNameString())) {
				return true;
			} else if (roleName.equals(Role.RoleName.Sales_Manager.getRoleNameString())) {
				return true;
			} else if (roleName.equals(Role.RoleName.EQUIPMENT_MANAGER.getRoleNameString())) {
				return true;
			}
			return false;
		}).collect(Collectors.toList()).size() > 0;
	}

}