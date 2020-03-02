package com.teamium.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.teamium.security.TeamiumUserDetails;

public class SpringSecurityAuditorAware implements AuditorAware<Long> {

	public Long getCurrentAuditor() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		return ((TeamiumUserDetails) authentication.getPrincipal()).getId();
	}
}