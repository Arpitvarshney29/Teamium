package com.teamium.domain.prod.docsign;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.staff.StaffMember;

@Entity
@Table(name = "t_signature_person")
public class SignaturePerson extends AbstractEntity {

	private static final long serialVersionUID = 9145821313731165477L;

	@Id
	@Column(name = "c_idsignatureperson", insertable = false, updatable = false)
	@TableGenerator(name = "idSignaturePerson_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "signature_person_idperson_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idSignaturePerson_seq")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_signed_by")
	private StaffMember signPerson;

	@Column(name = "c_document_signed_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar documentSignedOn;

	@Column(name = "c_flag_number")
	private int flagNumber=0;

	public SignaturePerson() {
	}

	public SignaturePerson(StaffMember signPerson) {
		this.signPerson = signPerson;
	}
	
	public SignaturePerson(StaffMember signPerson, int flagNumber) {
		this.signPerson = signPerson;
		this.flagNumber = flagNumber;
	}

	public SignaturePerson(StaffMember signPerson, Calendar documentSignedOn, int flagNumber) {
		this.signPerson = signPerson;
		this.documentSignedOn = documentSignedOn;
		this.flagNumber = flagNumber;
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
	 * @return the signPerson
	 */
	public StaffMember getSignPerson() {
		return signPerson;
	}

	/**
	 * @param signPerson
	 *            the signPerson to set
	 */
	public void setSignPerson(StaffMember signPerson) {
		this.signPerson = signPerson;
	}

	/**
	 * @return the documentSignedOn
	 */
	public Calendar getDocumentSignedOn() {
		return documentSignedOn;
	}

	/**
	 * @param documentSignedOn
	 *            the documentSignedOn to set
	 */
	public void setDocumentSignedOn(Calendar documentSignedOn) {
		this.documentSignedOn = documentSignedOn;
	}

	/**
	 * @return the flagNumber
	 */
	public int getFlagNumber() {
		return flagNumber;
	}

	/**
	 * @param flagNumber
	 *            the flagNumber to set
	 */
	public void setFlagNumber(int flagNumber) {
		this.flagNumber = flagNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((signPerson == null) ? 0 : signPerson.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		SignaturePerson other = (SignaturePerson) obj;
		if (signPerson == null) {
			if (other.signPerson != null)
				return false;
		} else if (!signPerson.equals(other.signPerson))
			return false;
		return true;
	}


}
