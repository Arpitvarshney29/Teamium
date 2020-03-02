package com.teamium.domain.prod.resources.staff.contract;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.TeamiumConstants;

/**
 * Xml entity which describe extra time modalities for contract
 * @author JS
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class ExtraTime implements Serializable{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -7388961347880950954L;

	/**
	 * Threshold until considered as extra hours
	 */
	@XmlAttribute(namespace=TeamiumConstants.XMLNS)
	private Integer threshold;
	
	/**
	 * Taux de majoration
	 */
	@XmlAttribute(namespace=TeamiumConstants.XMLNS)
	private Long majoration;

	/**
	 * @return the threshold
	 */
	public Integer getThreshold() {
		return threshold;
	}

	/**
	 * @param threshold the threshold to set
	 */
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	/**
	 * @return the majoration
	 */
	public Long getMajoration() {
		return majoration;
	}

	/**
	 * @param majoration the majoration to set
	 */
	public void setMajoration(Long majoration) {
		this.majoration = majoration;
	}
	
	
	
}
