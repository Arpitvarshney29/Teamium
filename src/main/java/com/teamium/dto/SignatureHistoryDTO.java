package com.teamium.dto;

import java.util.Calendar;

import com.teamium.domain.EditionTemplateType;
import com.teamium.domain.SignatureHistory;

/**
 * DTO for SignatureHistory.
 * 
 * @author Avinash
 *
 */
public class SignatureHistoryDTO {

	private Long id;
	
	private Integer version;

	private Long recordId;

	private EditionTemplateType editionTemplateType;

	private Calendar signatureDate = null;

	private Boolean approved = null;

	private boolean signed = false;

	private StaffMemberDTO recipient;

	private String comment;

	private boolean nextInRoutingOrder = false;

	private int routingOrder;

	/**
	 * 
	 */
	public SignatureHistoryDTO() {
	}

	/**
	 * @param SignatureHistory
	 */
	public SignatureHistoryDTO(SignatureHistory signatureHistory) {
		this.id = signatureHistory.getId();
		this.version = signatureHistory.getVersion();
		this.recordId = signatureHistory.getRecord().getId();
		this.editionTemplateType = signatureHistory.getEditionTemplateType();
		this.signatureDate = signatureHistory.getSignatureDate();
		this.approved = signatureHistory.getApproved();
		this.signed = signatureHistory.isSigned();
		this.recipient = new StaffMemberDTO().getStaffWithoutContractSettingDTO(signatureHistory.getRecipient());
		this.comment = signatureHistory.getComment();
		this.routingOrder = signatureHistory.getRoutingOrder();
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
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the recordId
	 */
	public Long getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
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
	public StaffMemberDTO getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(StaffMemberDTO recipient) {
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
	 * @return the nextInRoutingOrder
	 */
	public boolean isNextInRoutingOrder() {
		return nextInRoutingOrder;
	}

	/**
	 * @param nextInRoutingOrder the nextInRoutingOrder to set
	 */
	public void setNextInRoutingOrder(boolean nextInRoutingOrder) {
		this.nextInRoutingOrder = nextInRoutingOrder;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SignatureHistoryDTO [id=" + id + ", version=" + version + ", recordId=" + recordId
				+ ", editionTemplateType=" + editionTemplateType + ", signatureDate=" + signatureDate + ", approved="
				+ approved + ", signed=" + signed + ", recipient=" + recipient + ", comment=" + comment
				+ ", nextInRoutingOrder=" + nextInRoutingOrder + ", routingOrder=" + routingOrder + "]";
	}
}
