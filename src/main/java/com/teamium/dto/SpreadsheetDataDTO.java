package com.teamium.dto;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Wrapper class for Spreadsheet-Data
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SpreadsheetDataDTO {

	List<String> data;

	public SpreadsheetDataDTO() {

	}

	public SpreadsheetDataDTO(List<String> data) {
		super();
		this.data = data;
	}

	/**
	 * @return the data
	 */
	public List<String> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<String> data) {
		this.data = data;
	}

}
