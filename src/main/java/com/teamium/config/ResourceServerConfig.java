package com.teamium.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * <p>
 * A resource class for configuring the url authorization requests
 * </p>
 * 
 * @author Avinash Gupta
 *
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private TokenEnhancer tokenEnhancer;
	private DefaultTokenServices defaultTokenServices;
	private static final String[] AUTH_WHITELIST = {
	            // -- swagger ui
	            "/swagger-resources/**",
	            "/swagger-ui.html",
	            "/v2/api-docs",
	            "/webjars/**"
	    };
	 
	@Autowired
	public ResourceServerConfig(TokenEnhancer tokenEnhancer, DefaultTokenServices defaultTokenServices) {
		this.tokenEnhancer = tokenEnhancer;
		this.defaultTokenServices = defaultTokenServices;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers(AUTH_WHITELIST).permitAll()
		.antMatchers("/**/oauth/token").permitAll()
		.antMatchers("/**/password/recovery-request", "/**/password/recovery-link/validate",
				"/**/password/recover").permitAll()
		.anyRequest().authenticated();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		defaultTokenServices.setTokenEnhancer(tokenEnhancer);
		defaultTokenServices.setSupportRefreshToken(false);
		resources.tokenServices(defaultTokenServices).resourceId("com.teamium");
	}
}
