package com.teamium.config;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * <p>
 * BootStrap of all initially required configuration beans.
 * </p>
 * 
 * @author Avinash Gupta
 * @since 1.0
 */
@Configuration
public class InitialBeanConfig {

	/**
	 * Instantiate the Bean of TokenEnhancer to enhance a token.
	 * 
	 * @return TokenEnhancer instance
	 */
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new TeamiumTokenEnhancer();
	}

	@Bean
	public TokenStore tokenStore() {
		return new TokenStore();
	}

	/**
	 * Instantiate the Bean of DefaultTokenServices.
	 * 
	 * @return DefaultTokenServices instance
	 */
	@Primary
	@Bean
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setReuseRefreshToken(false);
		return defaultTokenServices;
	}

	/**
	 * Instantiate the Bean of MultipartConfigElement
	 * 
	 * @return the MultipartConfigElement object
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("10MB");
		factory.setMaxRequestSize("10MB");
		return factory.createMultipartConfig();
	}

	/**
	 * Get the template loader resolver. Folder is set to: static/html/mail/
	 * 
	 * @return ClassLoaderTemplateResolver responsible to locate and resolve e-mail
	 *         templates
	 */
	@Bean
	public ClassLoaderTemplateResolver emailTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("static/html/mail/");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setOrder(1);
		return templateResolver;
	}

	/**
	 * Get the e-mail template engine.
	 *
	 * @return TemplateEngine the e-mail template processor
	 */
	@Bean
	@Primary
	public TemplateEngine emailTemplateEngine() {
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(emailTemplateResolver());
		return engine;
	}

	/**
	 * Get the editions template engine.
	 *
	 * @return TemplateEngine the editions template processor
	 */
	@Bean(name = "editionTemplate")
	public TemplateEngine editionTemplateEngine() {
		TemplateEngine engine = new TemplateEngine();
		engine.setTemplateResolver(edtionTemplateResolver());
		return engine;
	}

	/**
	 * Get the template loader resolver. Folder is set to: static/html/edition/
	 * 
	 * @return ClassLoaderTemplateResolver responsible to locate and resolve edition
	 *         templates
	 */
	@Bean
	public ClassLoaderTemplateResolver edtionTemplateResolver() {
		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
		emailTemplateResolver.setPrefix("static/html/edition/");
		emailTemplateResolver.setTemplateMode("HTML5");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode("XHTML");
		emailTemplateResolver.setCharacterEncoding("UTF-8");
		emailTemplateResolver.setOrder(1);
		emailTemplateResolver.setCacheable(false);
		return emailTemplateResolver;
	}

}
