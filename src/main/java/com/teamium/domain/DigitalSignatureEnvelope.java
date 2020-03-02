package com.teamium.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "t_digital_signature_envelope")
public class DigitalSignatureEnvelope {
	/**
	 * DigitalSignatureEnvelope ID
	 */
	@Id
	@Column(name = "c_digital_signature_envelope_id", insertable = false, updatable = false)
	@TableGenerator(name = "idDigitalSignatureEnvelope_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "signature_idDigitalSignatureEnvelope_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idDigitalSignatureEnvelope_seq")
	private Long id;
	/**
	 * Name of the DigitalSignatureEnvelope.
	 */
	@Column(name = "c_name",unique=true)
	@NotNull
	private String name;
	/**
	 * templateType of the DigitalSignatureEnvelope.
	 */
	@NotNull
	@OneToOne
	@JoinColumn(name = "c_id_template_type")
	private EditionTemplateType templateType;

	/**
	 * SignatureRecipient
	 * 
	 * @see com.teamium.domain.SignatureRecipient
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "t_envelope_signature_recipient", joinColumns = @JoinColumn(name = "c_digital_signature_envelope_id"), inverseJoinColumns = @JoinColumn(name = "c_signature_recipient_id"))
	private List<SignatureRecipient> signatureRecipients = new ArrayList<>();

	public DigitalSignatureEnvelope() {

	}

	public DigitalSignatureEnvelope(Long id, String name, EditionTemplateType templateType,
			List<SignatureRecipient> signatureRecipients) {
		if (id != null) {
			this.id = id;
		}
		this.name = name;
		this.templateType = templateType;
		this.signatureRecipients = signatureRecipients;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the templateType
	 */
	public EditionTemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType the templateType to set
	 */
	public void setTemplateType(EditionTemplateType templateType) {
		this.templateType = templateType;
	}

	/**
	 * @return the signatureRecipients
	 */
	public List<SignatureRecipient> getSignatureRecipients() {
		return signatureRecipients;
	}

	/**
	 * @param signatureRecipients the signatureRecipients to set
	 */
	public void setSignatureRecipients(List<SignatureRecipient> signatureRecipients) {
		this.signatureRecipients = signatureRecipients;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DigitalSignatureEnvelope [id=" + id + ", name=" + name + ", templateType=" + templateType
				+ ", signatureRecipients=" + signatureRecipients + "]";
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((signatureRecipients == null) ? 0 : signatureRecipients.hashCode());
		result = prime * result + ((templateType == null) ? 0 : templateType.hashCode());
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
		DigitalSignatureEnvelope other = (DigitalSignatureEnvelope) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (signatureRecipients == null) {
			if (other.signatureRecipients != null)
				return false;
		} else if (!signatureRecipients.equals(other.signatureRecipients))
			return false;
		if (templateType == null) {
			if (other.templateType != null)
				return false;
		} else if (!templateType.equals(other.templateType))
			return false;
		return true;
	}

}
