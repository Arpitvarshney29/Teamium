package com.teamium.domain.prod.resources.staff.contract;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.TeamiumConstants;

@XmlAccessorType(XmlAccessType.FIELD)
public class Range implements Serializable {

	/**
	 *  Class UID
	 */
	private static final long serialVersionUID = -4328548708138954807L;

	/**
	 * Date format
	 */
	private static SimpleDateFormat TF = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * Range start
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Date min;
	
	/**
	 * Range end
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Date max;

	/**
	 * @return the min
	 */
	public Date getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Date min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public Date getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Date max) {
		this.max = max;
	}
	
	/**
	 * Return a formatted string representation of the object
	 * @return String
	 */
	public String format()
	{
		return TF.format(min) + ',' + TF.format(max);
	}
	
	
}
