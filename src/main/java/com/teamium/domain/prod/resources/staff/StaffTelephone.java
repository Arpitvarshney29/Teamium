package com.teamium.domain.prod.resources.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * Entity class for staff-telephone table
 */
@Entity
@Table(name = "t_staff_telephone")
public class StaffTelephone extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7470264513846709944L;

	@Id
	@Column(name = "c_idstafftelephone", insertable = false, updatable = false)
	@TableGenerator(name = "idStaffTelephone_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "stafftelephone_idStaffTelephone_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idStaffTelephone_seq")
	private Long id;

	@Column(name = "c_telephone")
	private String telephone;

	@Column(name = "c_primary_telephone")
	private boolean primaryTelephone;

	public StaffTelephone() {
	}

	public StaffTelephone(String directorTelephone, boolean b) {
	this.telephone=directorTelephone;
	this.primaryTelephone=b;
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
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the primaryTelephone
	 */
	public boolean isPrimaryTelephone() {
		return primaryTelephone;
	}

	/**
	 * @param primaryTelephone
	 *            the primaryTelephone to set
	 */
	public void setPrimaryTelephone(boolean primaryTelephone) {
		this.primaryTelephone = primaryTelephone;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StaffTelephone other = (StaffTelephone) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		return true;
	}

}
