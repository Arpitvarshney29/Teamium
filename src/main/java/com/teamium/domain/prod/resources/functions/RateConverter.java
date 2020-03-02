/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Describe a convert ratio in another rate unit
 * @author sraybaud- NovaRem
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RateConverter implements Serializable{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -1409063601059699828L;

	/**
	 * Other rate unit
	 */
	@XmlAttribute
	private String unit;
	
	/**
	 * Convert ratio
	 */
	@XmlAttribute
	private Float ratio;

	/**
	 * @return the unit
	 */
	protected String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	protected void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the ratio
	 */
	protected Float getRatio() {
		return ratio;
	}

	/**
	 * @param ratio the ratio to set
	 */
	protected void setRatio(Float ratio) {
		this.ratio = ratio;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		return result;
	}

	/**
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
		RateConverter other = (RateConverter) obj;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		return true;
	}
	
	
}
