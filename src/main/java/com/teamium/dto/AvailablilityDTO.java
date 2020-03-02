package com.teamium.dto;

import java.util.Calendar;

import com.teamium.domain.prod.resources.functions.Function;

public class AvailablilityDTO {

	private long function;
	private Calendar from;
	private Calendar to;

	public AvailablilityDTO() {
	}

	/**
	 * @param function
	 * @param from
	 * @param to
	 */
	public AvailablilityDTO(long function, Calendar from, Calendar to) {
		this.function = function;
		this.from = from;
		this.to = to;
	}

	/**
	 * @return the function
	 */
	public long getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(long function) {
		this.function = function;
	}

	/**
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(Calendar from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Calendar getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}

}
