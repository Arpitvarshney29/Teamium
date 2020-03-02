package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.teamium.domain.DigitalSignatureEnvelope;

public class DigitalSignatureEnvelopeDTO {
	private Long id;
	private String name;
	private String templateName;
	private List<SignatureRecipientDTO> signatureRecipientDTOs = new ArrayList<>();

	public DigitalSignatureEnvelopeDTO() {
		
	}
	public DigitalSignatureEnvelopeDTO(DigitalSignatureEnvelope digitalSignatureEnvelope) {
		this.id = digitalSignatureEnvelope.getId();
		this.name = digitalSignatureEnvelope.getName();
		this.templateName = digitalSignatureEnvelope.getTemplateType().getTemplateName();
		this.signatureRecipientDTOs = digitalSignatureEnvelope.getSignatureRecipients().stream()
				.map(ev -> new SignatureRecipientDTO(ev)).collect(Collectors.toList());

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
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the signatureRecipientDTOs
	 */
	public List<SignatureRecipientDTO> getSignatureRecipientDTOs() {
		return signatureRecipientDTOs;
	}

	/**
	 * @param signatureRecipientDTOs the signatureRecipientDTOs to set
	 */
	public void setSignatureRecipientDTOs(List<SignatureRecipientDTO> signatureRecipientDTOs) {
		this.signatureRecipientDTOs = signatureRecipientDTOs;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DigitalSignatureEnvelopeDTO [id=" + id + ", name=" + name + ", templateName=" + templateName
				+ ", signatureRecipientDTOs=" + signatureRecipientDTOs + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((templateName == null) ? 0 : templateName.hashCode());
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
		DigitalSignatureEnvelopeDTO other = (DigitalSignatureEnvelopeDTO) obj;
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
		if (templateName == null) {
			if (other.templateName != null)
				return false;
		} else if (!templateName.equals(other.templateName))
			return false;
		return true;
	}

}
