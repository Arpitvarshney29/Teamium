package com.teamium.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DashboardDataDTO {
	private int size;
	private List<BookingConflictDTO> bookingConflicts;
	private List<WidgetDataDTO> widgetDataDTOs;

	public DashboardDataDTO() {
	}

	/**
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 
	 * @return
	 */
	public List<BookingConflictDTO> getBookingConflicts() {
		return bookingConflicts;
	}

	/**
	 * 
	 * @param bookingConflicts
	 */
	public void setBookingConflicts(List<BookingConflictDTO> bookingConflicts) {
		this.bookingConflicts = bookingConflicts;
	}

	/**
	 * 
	 * @return
	 */
	public List<WidgetDataDTO> getWidgetDataDTOs() {
		return widgetDataDTOs;
	}

	/**
	 * 
	 * @param widgetDataDTOs
	 */
	public void setWidgetDataDTOs(List<WidgetDataDTO> widgetDataDTOs) {
		this.widgetDataDTOs = widgetDataDTOs;
	}

}
