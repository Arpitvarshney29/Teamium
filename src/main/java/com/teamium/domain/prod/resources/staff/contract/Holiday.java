package com.teamium.domain.prod.resources.staff.contract;

import java.io.Serializable;
import java.util.Calendar;

public class Holiday implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4262946301273279859L;

	private Calendar date;
	
	private Float rate;

	public Holiday(String exp){
		Integer year = null;
		Integer month = null;
		Integer day = null;
		
		String[] tab = exp.split(",");
		String[] dateInfo = tab[0].split("/");
		year = Integer.valueOf(dateInfo[2]);
		month = Integer.valueOf(dateInfo[1]);
		day = Integer.valueOf(dateInfo[0]);
		rate = Float.valueOf(tab[1]);
		
		date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_MONTH, day);
		date.set(Calendar.MONTH, month-1);
		date.set(Calendar.YEAR, year);
	}
	
	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the rate
	 */
	public Float getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate(Float rate) {
		this.rate = rate;
	}
	
}
