package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamium.domain.SignatureRecipient;

public class SignatureRecipientDTO {
	private Long id;
	private StaffMemberDTO recipient;
	private int routingOrder;
	private boolean approvalRequired;

	public SignatureRecipientDTO() {

	}
	
	public SignatureRecipientDTO(SignatureRecipient signatureRecipient) {
		this.id = signatureRecipient.getId();
		this.recipient = new StaffMemberDTO(signatureRecipient.getRecipient(),"");
		this.routingOrder = signatureRecipient.getRoutingOrder();
		this.approvalRequired = signatureRecipient.isApprovalRequired();
	}

	@JsonIgnore
	public SignatureRecipient getSignatureRecipient(){
		SignatureRecipient signatureRecipient = new SignatureRecipient();
		signatureRecipient.setApprovalRequired(this.approvalRequired);
		signatureRecipient.setId(this.id);
		signatureRecipient.setRecipient(this.recipient.getStaffMember());
		signatureRecipient.setRoutingOrder(this.routingOrder);
		return signatureRecipient;
		
		
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

	/**
	 * @return the approvalRequired
	 */
	public boolean isApprovalRequired() {
		return approvalRequired;
	}

	/**
	 * @param approvalRequired the approvalRequired to set
	 */
	public void setApprovalRequired(boolean approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

}
