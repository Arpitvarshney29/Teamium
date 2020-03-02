package com.teamium.domain.prod.resources.staff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.teamium.domain.XmlEntity;

/**
 * Describe the user profile to login the application
 * 
 * @author sraybaud - NovaRem
 *
 */
@Embeddable
public class UserSetting implements Serializable {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 7967430089533736270L;

	/**
	 * The production role
	 */
	public static final String ROLE_PRODUCTION = "production";

	/**
	 * The seller role
	 */
	public static final String ROLE_SELLER = "seller";

	/**
	 * The administration role
	 */
	public static final String ROLE_ADMINISTRATOR = "administrator";

	/**
	 * The administration role
	 */
	public static final String ROLE_ACCOUNTANT = "accountant";

	/**
	 * The administration role
	 */
	public static final String ROLE_PROJECT_MANAGER = "projectManager";

	/**
	 * The administration role
	 */
	public static final String ROLE_SUPERVISOR = "supervisor";

	/**
	 * The user login
	 */
	@Column(name = "c_user_login")
	private String login;

	/**
	 * The user hashed password
	 */
	@Column(name = "c_user_password")
	private String password;

	/**
	 * The user timezone
	 */
	@Column(name = "c_user_timezone")
	private String timezone;

	/**
	 * the user role
	 */
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "t_person_role", joinColumns = @JoinColumn(name = "c_idperson"))
	@Column(name = "c_role")
	private Set<String> roles;

	/**
	 * UI language
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_user_language"))
	private XmlEntity language;
	@Transient
	private Locale localeLanguage;

	@Column(name = "c_user_email")
	private String email;

	/**
	 * staff-emails
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idperson")
	private Set<StaffEmail> emails = new HashSet<StaffEmail>();

	/**
	 * staff-telephones
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_idperson")
	private Set<StaffTelephone> telephones = new HashSet<StaffTelephone>();

	@Column(name = "c_signature_path")
	private String signaturePath;

	@Column(name = "c_editable_signature")
	private Boolean editable = false;

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
		// Fix against JSF empty string and unique in the db
		if (login != null && login.isEmpty()) {
			this.login = null;
		} else {
			this.login = login;
		}
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
	 * Return the user's roles
	 * 
	 * @return the roles
	 */
	public Set<String> getRoles() {
		if (this.roles == null)
			this.roles = new TreeSet<String>();
		return this.roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	/**
	 * Return true if the current user is in the given role, else returns false
	 * 
	 * @return boolean
	 */
	public boolean isInRole(String role) {
		return this.getRoles().contains(role);
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public boolean setRole(String role) {
		return this.getRoles().add(role);
	}

	/**
	 * @return the language
	 */
	public XmlEntity getLanguage() {
		return this.language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(XmlEntity language) {
		this.language = language;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone == null ? TimeZone.getDefault().getID() : timezone;
	}

	/**
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the language
	 */
	public Locale getLocaleLanguage() {
		if (this.localeLanguage == null && this.language != null)
			this.localeLanguage = new Locale(this.language.getKey());
		return this.localeLanguage;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLocaleLanguage(Locale language) {
		this.localeLanguage = language;
		if (this.localeLanguage == null)
			this.language = null;
		else {
			XmlEntity xml = new XmlEntity();
			xml.setKey(language.getLanguage());
			if (!xml.equals(this.language))
				this.language = xml;

		}
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
	 * @return the emails
	 */
	public Set<StaffEmail> getEmails() {
		return emails;
	}

	/**
	 * @param emails
	 *            the emails to set
	 */
	public void setEmails(Set<StaffEmail> emails) {
		this.emails = emails;
	}

	/**
	 * @return the telephones
	 */
	public Set<StaffTelephone> getTelephones() {
		return telephones;
	}

	/**
	 * @param telephones
	 *            the telephones to set
	 */
	public void setTelephones(Set<StaffTelephone> telephones) {
		this.telephones = telephones;
	}

	/**
	 * Digital signature path
	 * 
	 * @return
	 */
	public String getSignaturePath() {
		return signaturePath;
	}

	/**
	 * Digital signature path
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

}
