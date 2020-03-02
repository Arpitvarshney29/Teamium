package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.projects.invoice.LineWrapper;

/**
 * Wrapper class for LineWrapper Entity
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LineWrapperDTO extends BookingDTO {

	private BookingDTO item;

	public LineWrapperDTO() {
		super();
	}

	/**
	 * @return the booking-item
	 */
	public BookingDTO getItem() {
		return item;
	}

	/**
	 * @param booking, the booking-item to set
	 */
	public void setItem(BookingDTO item) {
		this.item = item;
	}

	public LineWrapperDTO(LineWrapper lineWrapper) {
		super(lineWrapper);
		// if (lineWrapper.getItem() != null) {
		// this.item = new BookingDTO(lineWrapper.getItem());
		// }
	}

	/**
	 * Get LineWrapper entity from DTO
	 * 
	 * @param lineWrapper
	 * 
	 * @return lineWrapper object
	 */
	@JsonIgnore
	public LineWrapper getLineWrapper(LineWrapper lineWrapper) {
		super.getBooking(lineWrapper);
		return lineWrapper;
	}

}
