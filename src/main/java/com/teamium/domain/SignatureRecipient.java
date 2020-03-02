package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.prod.resources.staff.StaffMember;

@Entity
@Table(name = "t_signature_recipient")
public class SignatureRecipient {

	/**
	 * SignatureRecipient ID
	 */
	@Id
	@Column(name = "c_signature_recipient_id", insertable = false, updatable = false)
	@TableGenerator(name = "idSignatureRecipient_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "signature_idSignatureRecipient_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idSignatureRecipient_seq")
	private Long id;

	@OneToOne
	@JoinColumn(name = "c_idrecipient")
	private StaffMember recipient;

	@Column(name = "c_routing_order")
	private int routingOrder;

	@Column(name = "c_approval_required")
	private boolean approvalRequired;
	
	public SignatureRecipient(){
		
	}

	
	public SignatureRecipient(Long id, StaffMember recipient, int routingOrder, boolean approvalRequired) {
		if(id!=null) {
			this.id = id;
		}
		this.recipient = recipient;
		this.routingOrder = routingOrder;
		this.approvalRequired = approvalRequired;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SignatureRecipient [id=" + id + ", recipient=" + recipient + ", routingOrder=" + routingOrder
				+ ", approvalRequired=" + approvalRequired + "]";
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (approvalRequired ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result + routingOrder;
		return result;
	}


	/* (non-Javadoc)
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
		SignatureRecipient other = (SignatureRecipient) obj;
		if (approvalRequired != other.approvalRequired)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (recipient == null) {
			if (other.recipient != null)
				return false;
		} else if (!recipient.equals(other.recipient))
			return false;
		if (routingOrder != other.routingOrder)
			return false;
		return true;
	}
	
	
}
