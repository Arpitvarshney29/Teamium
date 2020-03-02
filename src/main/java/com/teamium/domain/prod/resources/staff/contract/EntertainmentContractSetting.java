/**
 * 
 */
package com.teamium.domain.prod.resources.staff.contract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.prod.resources.functions.units.RateUnit;

/**
 * Describe contract settings for french entertainment workers
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("entertainmentcontractsetting")
public class EntertainmentContractSetting extends ContractSetting{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 7647266514657101748L;
	
	/**
	 * Time format for toString
	 */
	private static DateFormat TF = new SimpleDateFormat("HH:mm");
	
	/**
	 * Start day time
	 */
	@Column(name="c_daystart")
	@Temporal(TemporalType.TIME)
	private Date dayStart;
	
	/**
	 * End day Time
	 */
	@Column(name="c_dayend")
	@Temporal(TemporalType.TIME)
	private Date dayEnd;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="key", column=@Column(name="c_rate")),
		@AttributeOverride(name="calendarConstant", column=@Column(name="c_rate_calendarconstant")),
	})
	private RateUnit defaultRate;
	
	/**
	 * The social convention
	 */
	@AttributeOverrides({
		@AttributeOverride(name="key", column=@Column(name="c_convention_key")),
		@AttributeOverride(name="threshold", column=@Column(name="c_convention_threshold")),
		@AttributeOverride(name="majoration", column=@Column(name="c_convention_majoration")),
		@AttributeOverride(name="interpige", column=@Column(name="c_convention_interpige")),
		@AttributeOverride(name="nightMajoration", column=@Column(name="c_convention_nightmajoration")),
		@AttributeOverride(name="stringNightRanges", column=@Column(name="c_convention_nightranges")),
		@AttributeOverride(name="chargePercent", column=@Column(name="c_convention_chargepercent")),
		@AttributeOverride(name="stringHolidaysRanges", column=@Column(name="c_convention_holidays")),
		@AttributeOverride(name="sundayMajoration", column=@Column(name="c_convention_sundaymajoration"))
	})
	private SocialConvention convention;
	
	/**
	 * @return the dayStart
	 */
	public Date getDayStart() {
		if(dayStart == null)
		{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 10);
			c.set(Calendar.MINUTE, 0);
			return c.getTime();
		}
		return dayStart;
	}

	/**
	 * @param dayStart the dayStart to set
	 */
	public void setDayStart(Date dayStart) {
		this.dayStart = dayStart;
	}

	/**
	 * @return the dayEnd
	 */
	public Date getDayEnd() {
		if(dayEnd == null)
		{
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 19);
			c.set(Calendar.MINUTE, 0);
			return c.getTime();
		}
		return dayEnd;
	}

	/**
	 * @param dayEnd the dayEnd to set
	 */
	public void setDayEnd(Date dayEnd) {
		this.dayEnd = dayEnd;
	}

	/**
	 * @return the defaultRate
	 */
	public RateUnit getDefaultRate() {
		if(defaultRate == null){
			defaultRate = new RateUnit();
			defaultRate.setKey("H");
		}
		return defaultRate;
	}

	/**
	 * @param defaultRate the defaultRate to set
	 */
	public void setDefaultRate(RateUnit defaultRate) {
		this.defaultRate = defaultRate;
	}

	/**
	 * @return the convention
	 */
	public SocialConvention getConvention() {
		return convention;
	}

	/**
	 * @param convention the convention to set
	 */
	public void setConvention(SocialConvention convention) {
		this.convention = convention;
	}

	/**
	 * Return a string description of the object
	 * @return String
	 */
	@Override
	public String toString()
	{
		return (super.toString() + "startTime = " + EntertainmentContractSetting.TF.format(getDayStart()) + " endTime = " + EntertainmentContractSetting.TF.format(getDayEnd()));
	}
	
	


}
