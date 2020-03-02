package com.teamium.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.resources.staff.StaffEmail;
import com.teamium.domain.prod.resources.staff.StaffTelephone;
import com.teamium.domain.prod.resources.staff.UserSetting;

/**
 * Describe the user profile to login the application
 * 
 * @author Nishant
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class UserSettingDTO {

	/**
	 * The user login
	 */
	private String login;

	/**
	 * The user hashed password
	 */
	private String password;

	/**
	 * The user timezone
	 */
	private String timezone;

	/**
	 * The user email
	 */
	private String email;

	/**
	 * confirm password
	 */
	private String confirmPassword;

	/**
	 * existing password
	 */
	private String existingPassword;

	/**
	 * password-recovery token
	 */
	private String passwordRecoveryToken;

	/**
	 * staff-emails
	 */
	private Set<StaffEmailDTO> emails = new HashSet<StaffEmailDTO>();

	private Set<StaffTelephoneDTO> telephones = new HashSet<StaffTelephoneDTO>();

	private byte[] digitalSignatureByteCode;

	private String signaturePath;
	private Boolean editable;

	public UserSettingDTO() {

	}

	public UserSettingDTO(UserSetting userSetting) {
		this.login = userSetting.getLogin();
		this.timezone = userSetting.getTimezone();
		this.email = userSetting.getEmail();
		Set<StaffEmail> staffEmails = userSetting.getEmails();
		if (staffEmails != null && !staffEmails.isEmpty()) {
			this.emails = staffEmails.stream().map(entity -> new StaffEmailDTO(entity)).collect(Collectors.toSet());
		}
		Set<StaffTelephone> staffTelephones = userSetting.getTelephones();
		if (staffTelephones != null && !staffTelephones.isEmpty()) {
			this.telephones = staffTelephones.stream().map(entity -> new StaffTelephoneDTO(entity))
					.collect(Collectors.toSet());
		}
		if (userSetting.getSignaturePath() != null) {
			this.signaturePath = userSetting.getSignaturePath();
		}
	}

	public UserSettingDTO(UserSetting userSetting, boolean forEmail) {
		if (userSetting != null) {

			Set<StaffEmail> staffEmails = userSetting.getEmails();
			if (staffEmails != null && !staffEmails.isEmpty()) {
				this.emails = staffEmails.stream().map(entity -> new StaffEmailDTO(entity)).collect(Collectors.toSet());
			}
			Set<StaffTelephone> staffTelephones = userSetting.getTelephones();
			if (staffTelephones != null && !staffTelephones.isEmpty()) {
				this.telephones = staffTelephones.stream().map(entity -> new StaffTelephoneDTO(entity))
						.collect(Collectors.toSet());
			}
		}
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassword
	 *            the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * @return the existingPassword
	 */
	public String getExistingPassword() {
		return existingPassword;
	}

	/**
	 * @param existingPassword
	 *            the existingPassword to set
	 */
	public void setExistingPassword(String existingPassword) {
		this.existingPassword = existingPassword;
	}

	/**
	 * @return the passwordRecoveryToken
	 */
	public String getPasswordRecoveryToken() {
		return passwordRecoveryToken;
	}

	/**
	 * @param passwordRecoveryToken
	 *            the passwordRecoveryToken to set
	 */
	public void setPasswordRecoveryToken(String passwordRecoveryToken) {
		this.passwordRecoveryToken = passwordRecoveryToken;
	}

	/**
	 * @return the emails
	 */
	public Set<StaffEmailDTO> getEmails() {
		return emails;
	}

	/**
	 * @param emails
	 *            the emails to set
	 */
	public void setEmails(Set<StaffEmailDTO> emails) {
		this.emails = emails;
	}

	/**
	 * @return the telephones
	 */
	public Set<StaffTelephoneDTO> getTelephones() {
		return telephones;
	}

	/**
	 * @param telephones
	 *            the telephones to set
	 */
	public void setTelephones(Set<StaffTelephoneDTO> telephones) {
		this.telephones = telephones;
	}

	/**
	 * 
	 * @return
	 */
	public byte[] getDigitalSignatureByteCode() {
		return digitalSignatureByteCode;
	}

	/**
	 * 
	 * @param digitalSignatureByteCode
	 */
	public void setDigitalSignatureByteCode(byte[] digitalSignatureByteCode) {
		this.digitalSignatureByteCode = digitalSignatureByteCode;
	}

	/**
	 * 
	 * digital signature path
	 * 
	 * @return signaturePath
	 */
	public String getSignaturePath() {
		return signaturePath;
	}

	/**
	 * 
	 * @param signaturePath
	 */
	public void setSignaturePath(String signaturePath) {
		this.signaturePath = signaturePath;
	}

	/**
	 * 
	 * @return
	 */

	public Boolean getEditable() {
		return editable;
	}

	/**
	 * 
	 * @param editable
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}

	/**
	 * Get Contract Object for Current DTO
	 * 
	 * @param userSetting
	 *            the userSetting
	 * 
	 * @return userSetting object
	 */
	public UserSetting getUserSetting(UserSetting userSetting) {
		userSetting.setLogin(this.getLogin());
		userSetting.setTimezone(this.getTimezone());
		Set<StaffEmail> staffEmails = userSetting.getEmails();
		if (staffEmails != null && !staffEmails.isEmpty()) {
			userSetting.setEmails(staffEmails);
		}
		Set<StaffTelephone> staffTelephones = userSetting.getTelephones();
		if (staffTelephones != null && !staffTelephones.isEmpty()) {
			userSetting.setTelephones(staffTelephones);
		}
		return userSetting;
	}

}
