package com.teamium.domain.prod.resources.staff.contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * Describe a social convention
 * @author JS
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Embeddable
public class SocialConvention extends AbstractXmlEntity implements Cloneable{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 5313259830659590891L;

	/**
	 * Date format
	 */
	private static SimpleDateFormat TF = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * List of threshold/majoration for additional hours
	 */
	@XmlElementWrapper(name="thresholdCouples", namespace=TeamiumConstants.XMLNS)
	@XmlElement(name="thresholdCouple", namespace=TeamiumConstants.XMLNS)
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="t_contract_thresholdcouple",joinColumns=@JoinColumn(name="c_idcontract"))
	@AttributeOverrides({
		@AttributeOverride(name="threshold", column=@Column(name="c_threshold")),
		@AttributeOverride(name="majoration", column=@Column(name="c_majoration"))
	})
	private Set<ThresholdCouple> thresholdCouples;
	
	/**
	 * The threshold for additional hours
	 */
	/*@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Integer threshold;*/
	
	/**
	 * the majoration for additional hours
	 */
	/*@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Float majoration;*/
	
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Float nightMajoration;
	
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Float sundayMajoration;
	
	/**
	 * The interpige
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Integer interpige;
	
	/**
	 * the string representation of the night ranges
	 * The regex pattern of this string is  
	 * 
	 * (([0-1][0-9]|2[0-3])(:[0-5][0-9]){2},([0-1][0-9]|2[0-3])(:[0-5][0-9]){2};)*([0-1][0-9]|2[0-3])(:[0-5][0-9]){2},([0-1][0-9]|2[0-3])(:[0-5][0-9]){2}
	 * 
	 * ex1 : 20:00:00,23:59:00;00:00:00,06:00:00
	 * ex2 : 00:00:00,00:59:00
	 * 
	 */
	@XmlElement(name="nightRanges",namespace=TeamiumConstants.XMLNS)
	private String stringNightRanges;
	
	@XmlElement(name="holidayRanges",namespace=TeamiumConstants.XMLNS)
	private String stringHolidaysRanges;
	
	
	
	/**
	 * The default charge percent for a staffRate
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Integer chargePercent;
	
	/**
	 * @return the thresholdCouples
	 */
	public Set<ThresholdCouple> getThresholdCouples() {
		if(thresholdCouples == null) thresholdCouples = new HashSet<ThresholdCouple>();
		return thresholdCouples;
	}

	/**
	 * @param thresholdCouples the thresholdCouples to set
	 */
	public void setThresholdCouples(Set<ThresholdCouple> thresholdCouples) {
		this.thresholdCouples = thresholdCouples;
	}

	/**
	 * @return the interpige
	 */
	public Integer getInterpige() {
		return interpige;
	}

	/**
	 * @param interpige the interpige to set
	 */
	public void setInterpige(Integer interpige) {
		this.interpige = interpige;
	}

	/**
	 * get the string representation of the night ranges
	 * The regex pattern of this string is  
	 * (([0-1][0-9]|2[0-3])(:[0-5][0-9]){2},([0-1][0-9]|2[0-3])(:[0-5][0-9]){2};)*([0-1][0-9]|2[0-3])(:[0-5][0-9]){2},([0-1][0-9]|2[0-3])(:[0-5][0-9]){2}
	 * 
	 * ex : 20:00:00,23:59:00;00:00:00,06:00:00
	 * @return the stringNightRanges
	 * @see getNightRanges()
	 * 
	 */
	public String getStringNightRanges() {
		return stringNightRanges;
	}

	/**
	 * set the string representation of the night ranges
	 * The regex pattern of this string is  
	 * (([0-1][0-9]|2[0-3])(:[0-5][0-9]){2},([0-1][0-9]|2[0-3])(:[0-5][0-9]){2};)*([0-1][0-9]|2[0-3])(:[0-5][0-9]){2},([0-1][0-9]|2[0-3])(:[0-5][0-9]){2}
	 * 
	 * ex : 20:00:00,23:59:00;00:00:00,06:00:00
	 * @param stringNightRanges the stringNightRanges to set
	 * @see setNightRanges()
	 */
	public void setStringNightRanges(String stringNightRanges) {
		this.stringNightRanges = stringNightRanges;
	}

	/**
	 * This list is transient, any modification done to it will not be persisted until the set is called
	 * @return the nightRanges
	 */
	public List<Range> getNightRanges() {
		
		List<Range> ranges = new ArrayList<Range>();
		try{
		String[] stringRange = stringNightRanges.split(";");
		for(String s : stringRange)
		{
			String[] minmax = s.split(",");
			Range range = new Range();
			range.setMin(TF.parse(minmax[0]));
			range.setMax(TF.parse(minmax[1]));
			ranges.add(range);
		}
		}
		catch(Exception e){}
		return ranges;
	}
	/**
	 * Set the list
	 * @param ranges
	 */
	public void setNightRanges(List<Range> ranges){
		
		StringBuilder sb = new StringBuilder();
		boolean firstRun = true;
		for(Range r : ranges)
		{
			if(!firstRun)
				sb.append(";");
			else
				firstRun = true;
			sb.append(r.format());
		}
		stringNightRanges = sb.toString();
		
	}

	/**
	 * @return the nightMajoration
	 */
	public Float getNightMajoration() {
		return nightMajoration;
	}

	/**
	 * @param nightMajoration the nightMajoration to set
	 */
	public void setNightMajoration(Float nightMajoration) {
		this.nightMajoration = nightMajoration;
	}

	/**
	 * @return the chargePercent
	 */
	public Integer getChargePercent() {
		if(chargePercent == null)
			return 0;
		return chargePercent;
	}

	/**
	 * @param chargePercent the chargePercent to set
	 */
	public void setChargePercent(Integer chargePercent) {
		this.chargePercent = chargePercent;
	}
	
	/**
	 * @return the stringHolidaysRanges
	 */
	public String getStringHolidaysRanges() {
		return stringHolidaysRanges;
	}

	/**
	 * @param stringHolidaysRanges the stringHolidaysRanges to set
	 */
	public void setStringHolidaysRanges(String stringHolidaysRanges) {
		this.stringHolidaysRanges = stringHolidaysRanges;
	}

	/**
	 * @return the sundayMajoration
	 */
	public Float getSundayMajoration() {
		return sundayMajoration;
	}

	/**
	 * @param sundayMajoration the sundayMajoration to set
	 */
	public void setSundayMajoration(Float sundayMajoration) {
		this.sundayMajoration = sundayMajoration;
	}

	/**
	 * Clone the current object
	 * @return the clone
	 */
	@Override
	public SocialConvention clone() throws CloneNotSupportedException{
		return (SocialConvention) super.clone();
	}
	
	public List<Holiday> getHolidays(){
		List<Holiday> holidays = new ArrayList<Holiday>();
		if(this.stringHolidaysRanges != null){
			String[] sHolidays = this.stringHolidaysRanges.split(";");
			for(String s : sHolidays){
				holidays.add(new Holiday(s));
			}
		}
		return holidays;
	}
}
