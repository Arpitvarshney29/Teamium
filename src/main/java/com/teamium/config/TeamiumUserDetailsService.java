package com.teamium.config;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.security.TeamiumUserDetails;
import com.teamium.service.UserService;

/**
 * <p>
 * A core class which loads user-specific data.
 * </p>
 * 
 * @author Avinash Gupta
 *
 */
@Component
public class TeamiumUserDetailsService implements UserDetailsService {

	private UserService userService;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpServletRequest request;

	@Autowired
	public TeamiumUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.debug("Inside TeamiumUserDetailsService:loadUserByUsername,Getting details for user: ", username);
		StaffMember user = userService.findByLogin(username);
		logger.info("username : " + request.getParameter("username"));
		logger.info("grant_type : " + request.getParameter("grant_type"));
		// logger.info(Constants.UUID + request.getHeader(Constants.UUID));
		logger.info("Authorization : " + request.getHeader("Authorization"));
		if (user == null) {
			logger.info("User {} tried to login but it was not found on database", username);
			throw new UsernameNotFoundException("The email or password you entered is incorrect.");
		} /*
			 * else if (!user.isActive() || user.isDeleted()) {
			 * logger.info("User {} tried to login but it is NO longer active",
			 * username); String message = getMessage("user.not-active"); throw
			 * new UsernameNotFoundException(message); }
			 */
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//		user.getRole().forEach(r -> {
//			authorities.add(new SimpleGrantedAuthority(r.getRoleName()));
//		});

		UserDetails userDetails = new TeamiumUserDetails(user.getId(), user.getUserSetting().getLogin(),
				user.getUserSetting().getPassword(), authorities);
		logger.info("Successfully returning details of user.");
		return userDetails;
	}
}