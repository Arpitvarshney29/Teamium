package com.teamium.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Properties Configuration bean maps all the properties presents in
 * application.properties.
 * </p>
 * 
 * @author Avinash Gupta
 * @version 1.0
 */
@Component
@PropertySources({
		@PropertySource(value = "file:${user.home}/Teamium/config/application.properties", ignoreResourceNotFound = false) })
public class PropConfig implements InitializingBean {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The database Driver.
	 */
	@Value("${database.driver}")
	private String databaseDriver;

	/**
	 * The database URL.
	 */
	@Value("${database.url}")
	private String databaseUrl;

	/**
	 * The database UserName.
	 */
	@Value("${database.username}")
	private String databaseUsername;

	/**
	 * The database password.
	 */
	@Value("${database.password}")
	private String databasePassword;

	/**
	 * The boolean property to whether show SQL or not.
	 */
	@Value("${database.showSql}")
	private boolean showSql;

	/**
	 * The boolean property to whether generate DDL or not.
	 */
	@Value("${database.generateDdl}")
	private boolean generateDdl;

	/**
	 * The database vendor.
	 */
	@Value("${database.vendor}")
	private Database vendor;

	/**
	 * The server port address.
	 */
	@Value("${server.port}")
	private Long serverPort;

	/**
	 * The web client id
	 */
	@Value("${teamium.web.client.id}")
	private String webAppClientId;

	/**
	 * The web app secret
	 */
	@Value("${teamium.web.api.secret}")
	private String webAppApiSecret;

	/**
	 * The mobile client id
	 */
	@Value("${teamium.mobile.client.id}")
	private String mobileAppClientId;

	/**
	 * The mobile app secret
	 */
	@Value("${teamium.mobile.api.secret}")
	private String mobileAppApiSecret;

	/**
	 * The swagger app client id
	 */
	@Value("${teamium.swagger.client.id}")
	private String swaggerClientId;

	/**
	 * The swagger api secret
	 */
	@Value("${teamium.swagger.api.secret}")
	private String swaggerApiSecret;

	/**
	 * The password recovery expiration hours
	 */
	@Value("${password.recovery.token.expiration.hours}")
	private long passwordRecoveryExpirationHours;

	/**
	 * The support email
	 */
	@Value("${support.email}")
	private String supportEmail;

	/**
	 * The app base url
	 */
	@Value("${app.base.url}")
	private String appBaseURL;

	@Value("${spring.mail.port}")
	private String smtpPort;

	@Value("${spring.mail.host}")
	private String smtpHost;

	@Value("${spring.mail.username}")
	private String smtpUserName;

	@Value("${spring.mail.password}")
	private String smtpPassword;

	@Value("${teamium.resources.path}")
	private String teamiumResourcesPath;

	@Value("${equipment.milestone.expiration}")
	private int equipmentMilestoneExpiration;

	@Value("${teamium.staff.document.expiration}")
	private int staffDocumentExpiration;

	@Value("${teamium.web.app.url}")
	private String webAppBaseUrl;

//	/**
//	 * The static resource url
//	 */
//	@Value("${teamium.static.resources.url}")
//	private String teamiumStaticResourseURL;

	/**
	 * The collaborator client id
	 */
	@Value("${teamium.collaborator.client.id}")
	private String collaboratorClientId;

	/**
	 * The mobile app secret
	 */
	@Value("${teamium.collaborator.api.secret}")
	private String collaboratorApiSecret;

	public PropConfig() {
		logger.info("PropConfog initialized");
	}

	/**
	 * @return the databaseDriver
	 */
	public String getDatabaseDriver() {
		return databaseDriver;
	}

	/**
	 * @param databaseDriver the databaseDriver to set
	 */
	public void setDatabaseDriver(String databaseDriver) {
		this.databaseDriver = databaseDriver;
	}

	/**
	 * @return the databaseUrl
	 */
	public String getDatabaseUrl() {
		return databaseUrl;
	}

	/**
	 * @param databaseUrl the databaseUrl to set
	 */
	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	/**
	 * @return the databaseUsername
	 */
	public String getDatabaseUsername() {
		return databaseUsername;
	}

	/**
	 * @param databaseUsername the databaseUsername to set
	 */
	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	/**
	 * @return the databasePassword
	 */
	public String getDatabasePassword() {
		return databasePassword;
	}

	/**
	 * @param databasePassword the databasePassword to set
	 */
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	/**
	 * @return the showSql
	 */
	public boolean isShowSql() {
		return showSql;
	}

	/**
	 * @param showSql the showSql to set
	 */
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	/**
	 * @return the generateDdl
	 */
	public boolean isGenerateDdl() {
		return generateDdl;
	}

	/**
	 * @param generateDdl the generateDdl to set
	 */
	public void setGenerateDdl(boolean generateDdl) {
		this.generateDdl = generateDdl;
	}

	/**
	 * @return the vendor
	 */
	public Database getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(Database vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the serverPort
	 */
	public Long getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(Long serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the webAppClientId
	 */
	public String getWebAppClientId() {
		return webAppClientId;
	}

	/**
	 * @param webAppClientId the webAppClientId to set
	 */
	public void setWebAppClientId(String webAppClientId) {
		this.webAppClientId = webAppClientId;
	}

	/**
	 * @return the passwordRecoveryExpirationTime
	 */
	public long getPasswordRecoveryExpirationHours() {
		return passwordRecoveryExpirationHours;
	}

	/**
	 * @param passwordRecoveryExpirationTime the passwordRecoveryExpirationTime to
	 *                                       set
	 */
	public void setPasswordRecoveryExpirationHours(long passwordRecoveryExpirationHours) {
		this.passwordRecoveryExpirationHours = passwordRecoveryExpirationHours;
	}

	/**
	 * @return the supportEmail
	 */
	public String getSupportEmail() {
		return supportEmail;
	}

	/**
	 * @param supportEmail the supportEmail to set
	 */
	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}

	/**
	 * @return the appBaseURL
	 */
	public String getAppBaseURL() {
		return appBaseURL;
	}

	/**
	 * @param appBaseURL the appBaseURL to set
	 */
	public void setAppBaseURL(String appBaseURL) {
		this.appBaseURL = appBaseURL;
	}

	/**
	 * @return the mobileAppClientId
	 */
	public String getMobileAppClientId() {
		return mobileAppClientId;
	}

	/**
	 * @param mobileAppClientId the mobileAppClientId to set
	 */
	public void setMobileAppClientId(String mobileAppClientId) {
		this.mobileAppClientId = mobileAppClientId;
	}

	/**
	 * @return the webAppApiSecret
	 */
	public String getWebAppApiSecret() {
		return webAppApiSecret;
	}

	/**
	 * @param webAppApiSecret the webAppApiSecret to set
	 */
	public void setWebAppApiSecret(String webAppApiSecret) {
		this.webAppApiSecret = webAppApiSecret;
	}

	/**
	 * @return the mobileAppApiSecret
	 */
	public String getMobileAppApiSecret() {
		return mobileAppApiSecret;
	}

	/**
	 * @param mobileAppApiSecret the mobileAppApiSecret to set
	 */
	public void setMobileAppApiSecret(String mobileAppApiSecret) {
		this.mobileAppApiSecret = mobileAppApiSecret;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	/**
	 * Allows a set of command to be run after all properties have been set.
	 * 
	 * @throws Exception the exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// logger.debug("Application Properties={}", this);
	}

	/**
	 * @return the smtpPort
	 */
	public String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param smtpPort the smtpPort to set
	 */
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	/**
	 * @return the smtpHost
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * @param smtpHost the smtpHost to set
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * @return the smtpUserName
	 */
	public String getSmtpUserName() {
		return smtpUserName;
	}

	/**
	 * @param smtpUserName the smtpUserName to set
	 */
	public void setSmtpUserName(String smtpUserName) {
		this.smtpUserName = smtpUserName;
	}

	/**
	 * @return the smtpPassword
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}

	/**
	 * @param smtpPassword the smtpPassword to set
	 */
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	/**
	 * @return the swaggerClientId
	 */
	public String getSwaggerClientId() {
		return swaggerClientId;
	}

	/**
	 * @param swaggerClientId the swaggerClientId to set
	 */
	public void setSwaggerClientId(String swaggerClientId) {
		this.swaggerClientId = swaggerClientId;
	}

	/**
	 * @return the swaggerApiSecret
	 */
	public String getSwaggerApiSecret() {
		return swaggerApiSecret;
	}

	/**
	 * @param swaggerApiSecret the swaggerApiSecret to set
	 */
	public void setSwaggerApiSecret(String swaggerApiSecret) {
		this.swaggerApiSecret = swaggerApiSecret;
	}

	/**
	 * @return the teamiumResourcesPath
	 */
	public String getTeamiumResourcesPath() {
		return teamiumResourcesPath;
	}

//	/**
//	 * @return the teamiumStaticResourseURL
//	 */
//	public String getTeamiumStaticResourseURL() {
//		return teamiumStaticResourseURL;
//	}
//
//	/**
//	 * @param teamiumStaticResourseURL
//	 *            the teamiumStaticResourseURL to set
//	 */
//	public void setTeamiumStaticResourseURL(String teamiumStaticResourseURL) {
//		this.teamiumStaticResourseURL = teamiumStaticResourseURL;
//	}

	/**
	 * @param teamiumResourcesPath the teamiumResourcesPath to set
	 */
	public void setTeamiumResourcesPath(String teamiumResourcesPath) {
		this.teamiumResourcesPath = teamiumResourcesPath;
	}

	/**
	 * @return the equipmentMilestoneExpiration
	 */
	public int getEquipmentMilestoneExpiration() {
		return equipmentMilestoneExpiration;
	}

	/**
	 * @param equipmentMilestoneExpiration the equipmentMilestoneExpiration to set
	 */
	public void setEquipmentMilestoneExpiration(int equipmentMilestoneExpiration) {
		this.equipmentMilestoneExpiration = equipmentMilestoneExpiration;
	}

	/**
	 * @return the staffDocumentExpiration
	 */
	public int getStaffDocumentExpiration() {
		return staffDocumentExpiration;
	}

	/**
	 * @return the webAppBaseUrl
	 */
	public String getWebAppBaseUrl() {
		return webAppBaseUrl;
	}

	/**
	 * @param webAppBaseUrl the webAppBaseUrl to set
	 */
	public void setWebAppBaseUrl(String webAppBaseUrl) {
		this.webAppBaseUrl = webAppBaseUrl;
	}

	/**
	 * @param staffDocumentExpiration the staffDocumentExpiration to set
	 */
	public void setStaffDocumentExpiration(int staffDocumentExpiration) {
		this.staffDocumentExpiration = staffDocumentExpiration;
	}

	/**
	 * @return the collaboratorClientId
	 */
	public String getCollaboratorClientId() {
		return collaboratorClientId;
	}

	/**
	 * @param collaboratorClientId the collaboratorClientId to set
	 */
	public void setCollaboratorClientId(String collaboratorClientId) {
		this.collaboratorClientId = collaboratorClientId;
	}

	/**
	 * @return the collaboratorApiSecret
	 */
	public String getCollaboratorApiSecret() {
		return collaboratorApiSecret;
	}

	/**
	 * @param collaboratorApiSecret the collaboratorApiSecret to set
	 */
	public void setCollaboratorApiSecret(String collaboratorApiSecret) {
		this.collaboratorApiSecret = collaboratorApiSecret;
	}

}
