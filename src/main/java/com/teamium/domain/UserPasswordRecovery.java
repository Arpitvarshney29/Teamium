package com.teamium.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "t_user_password_recovery")
public class UserPasswordRecovery extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8295532164778013658L;

	@Id
	@Column(name = "c_iduserpasswordrecovery", insertable = false, updatable = false)
	@TableGenerator(name = "idUserPasswordRecovery_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "userpasswordrecovery_iduserpasswordrecovery_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idUserPasswordRecovery_seq")
	private Long id;

	@Column(name = "c_token")
	private String token;

	@Column(name = "c_created_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdOn;

	@OneToOne
	@JoinColumn(name = "c_idperson", nullable = false)
	private Person user;

	@Column(name = "c_link_validated")
	private boolean linkValidated;

	/**
	 * Default UserPasswordRecovery constructor
	 */
	public UserPasswordRecovery() {
	}

	/**
	 * Parameterized UserPasswordRecovery constructor
	 * 
	 * @param userPasswordRecoveryId
	 * @param token
	 * @param createdOn
	 * @param user
	 */
	public UserPasswordRecovery(Long id, String token, Calendar createdOn, Person user) {
		this.id = id;
		this.token = token;
		this.createdOn = createdOn;
		this.user = user;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the createdOn
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the user
	 */
	public Person getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(Person user) {
		this.user = user;
	}

	/**
	 * @return the linkValidated
	 */
	public boolean isLinkValidated() {
		return linkValidated;
	}

	/**
	 * @param linkValidated
	 *            the linkValidated to set
	 */
	public void setLinkValidated(boolean linkValidated) {
		this.linkValidated = linkValidated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPasswordRecovery other = (UserPasswordRecovery) obj;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

}
