package com.teamium.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * SignatureHistory will contain the information about all the signatures done
 * or needed to be done.
 * 
 * @author Avinash
 *
 */
@Entity
@Table(name = "t_signature_history")
public class SignatureHistory extends AbstractEntity {

	/**
	 * SignatureHistory Id.
	 */
	@Id
	@Column(name = "c_idsignature_history", insertable = false, updatable = false)
	@TableGenerator(name = "idsignature_history_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "signature_history_idsignature_history_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idsignature_history_seq")
	private Long id;

	/**
	 * Signature is done on this Record.
	 */
	@NotNull
	@JoinColumn(name = "c_record_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Record record;

	/**
	 * EditionTemplateType used for the Record.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_edition_template_type_id", nullable = false, updatable = false)
	private EditionTemplateType editionTemplateType;

	/**
	 * Will hold the signature date in case the record is already signed.
	 */
	@Column(name = "c_signature_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar signatureDate = null;

	/**
	 * Will hold if the record is approved or rejected in case the record is already
	 * signed.
	 */
	@Column(name = "c_approved")
	private Boolean approved = null;

	/**
	 * Will hold if the record is signed.
	 */
	@NotNull
	@Column(name = "c_signed")
	private boolean signed = false;

	/**
	 * The person who signed or is responsible to sign as per the
	 * DigitalSignatureEnvelope.
	 */
	@NotNull
	@JoinColumn(name = "c_idrecipient")
	@OneToOne(fetch = FetchType.EAGER)
	private StaffMember recipient;

	/**
	 * This the comment to be filled by the rejecter of the record.
	 */
	@Column(name = "c_comment")
	private String comment;

	/**
	 * Routing order of the recipient.
	 */
	@NotNull
	@Column(name = "c_routing_order")
	private int routingOrder;

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -759225759954274058L;

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
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the editionTemplateType
	 */
	public EditionTemplateType getEditionTemplateType() {
		return editionTemplateType;
	}

	/**
	 * @param editionTemplateType the editionTemplateType to set
	 */
	public void setEditionTemplateType(EditionTemplateType editionTemplateType) {
		this.editionTemplateType = editionTemplateType;
	}

	/**
	 * @return the signatureDate
	 */
	public Calendar getSignatureDate() {
		return signatureDate;
	}

	/**
	 * @param signatureDate the signatureDate to set
	 */
	public void setSignatureDate(Calendar signatureDate) {
		this.signatureDate = signatureDate;
	}

	/**
	 * @return the approved
	 */
	public Boolean getApproved() {
		return approved;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * @return the signed
	 */
	public boolean isSigned() {
		return signed;
	}

	/**
	 * @param signed the signed to set
	 */
	public void setSigned(boolean signed) {
		this.signed = signed;
	}

	/**
	 * @return the recipient
	 */
	public StaffMember getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(StaffMember recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the routingOrder
	 */
	public int getRoutingOrder() {
		return routingOrder;
	}

	/**
	 * @param routingOrder the routingOrder to set
	 */
	public void setRoutingOrder(int routingOrder) {
		this.routingOrder = routingOrder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SignatureHistory [id=" + id + ", record=" + record + ", editionTemplateType=" + editionTemplateType
				+ ", signatureDate=" + signatureDate + ", approved=" + approved + ", signed=" + signed + ", recipient="
				+ recipient + ", comment=" + comment + ", routingOrder=" + routingOrder + "]";
	}
}