package com.teamium.dto;

public class ProgramEventDTO {
	long id;
	long resource;
	String startTime;
	String endTime;
	String text;
	String color;

	public ProgramEventDTO() {
	}

	public ProgramEventDTO(long id, long resource, String startTime, String endTime, String text, String color) {
		this.id = id;
		this.resource = resource;
		this.startTime = startTime;
		this.endTime = endTime;
		this.text = text;
		this.color = color;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the resource
	 */
	public long getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(long resource) {
		this.resource = resource;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProgramEventDTO [id=" + id + ", resource=" + resource + ", startTime=" + startTime + ", endTime="
				+ endTime + ", text=" + text + ", color=" + color + "]";
	}

}
