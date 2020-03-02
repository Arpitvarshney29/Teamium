package com.teamium.domain.prod.resources.staff.contract;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.TeamiumConstants;

/**
 * 
 * @author mgenevier
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ThresholdCouple implements Serializable, Cloneable{


	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2980671306030204912L;

	/**
	 * The threshold for additional hours
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Integer threshold;
	
	/**
	 * the majoration for additional hours
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Float majoration;

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
	public Float getMajoration() {
		return majoration;
	}

	/**
	 * @param majoration the majoration to set
	 */
	public void setMajoration(Float majoration) {
		this.majoration = majoration;
	} 
	
	/**
	 * Clone the current object
	 * @return the clone
	 */
	@Override
	public ThresholdCouple clone() throws CloneNotSupportedException{
		return (ThresholdCouple) super.clone();
	}
}
