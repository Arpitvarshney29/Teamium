package com.teamium.domain.prod.export;

import java.util.Calendar;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represent an export for a period
 * @author dolivier - NovaRem
 * @since v6
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PeriodicExport extends ExportFile {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 1833402982787203596L;
	
	/**
	 * Start date for export
	 */
	private Calendar start;
	
	/**
	 * End date for exports
	 */
	private Calendar end;

	/**
	 * @return the start
	 */
	public Calendar getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Calendar start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Calendar getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Calendar end) {
		this.end = end;
	}
	
}
