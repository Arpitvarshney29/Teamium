package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.resources.functions.ExtraLine;

/**
 * 
 * Wrapper class for Extra-Line
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ExtraLineDTO extends ExtraDTO {

	private LineDTO line;

	public ExtraLineDTO() {
		super();
	}

	public ExtraLineDTO(ExtraLine extraLine) {
		super(extraLine);
		// if (extraLine.getLine() != null) {
		// this.line = new LineDTO(extraLine.getLine());
		// }
	}

	/**
	 * Method to get extra
	 * 
	 * @param extra
	 *            the extra
	 * 
	 * @return extra object
	 */
	public ExtraLine getExtraLine(ExtraLine extraLine) {
		super.getExtra(extraLine);
//		 if (this.getLine() != null) {
//			 extraLine.setLine(this.getLine().getLine(new Line(), this.getLine()));
//		 }
		return extraLine;
	}

	/**
	 * @return the line
	 */
	public LineDTO getLine() {
		return line;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(LineDTO line) {
		this.line = line;
	}

}
