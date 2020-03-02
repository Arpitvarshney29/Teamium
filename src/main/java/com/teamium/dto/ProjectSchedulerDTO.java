package com.teamium.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.teamium.constants.Constants;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.enums.ProjectStatus.ProjectStatusName;

public class ProjectSchedulerDTO {
	private String title;
	private String startTime;
	private String endTime;
	private String color;
	private String discriminator;
	private Long recordId;

	public ProjectSchedulerDTO() {

	}

	public ProjectSchedulerDTO(Record record) {
		List<Booking> bookings = record.getLines().stream().filter(line -> line instanceof Booking)
				.map(line -> (Booking) line).filter(booking -> booking.getFrom() != null && booking.getTo() != null)
				.collect(Collectors.toList());

		Date bookingStartDate = bookings.stream()
				.sorted((b1, b2) -> b1.getFrom().getTime().before(b2.getFrom().getTime()) ? -1 : 1)
				.map(b -> b.getFrom().getTime()).findFirst()
				.orElse(record.getDate() == null ? new Date() : record.getDate().getTime());

		Date bookingEndDate = bookings.stream()
				.sorted((b1, b2) -> b1.getTo().getTime().after(b2.getTo().getTime()) ? -1 : 1)
				.map(b -> b.getTo().getTime()).findFirst()
				.orElse(new Date(bookingStartDate.getTime() + (8L * 60 * 60 * 1000)));

		String key = null;

		if (record instanceof Project) {
			Project project = (Project) record;
			this.setTitle(project.getTitle());
			this.setDiscriminator(Constants.BOOKING_STRING);
		} else if (record instanceof Quotation) {
			Quotation quotation = (Quotation) record;
			this.setTitle(quotation.getTitle());
			this.setDiscriminator(Constants.BUDGET_STRING);
		}
		if (record.getStatus() != null) {
			key = record.getStatus().getKey();
		}
		if (key != null) {
			AbstractProject project = (AbstractProject) record;
			this.setColor(project.getTheme() != null ? project.getTheme().getKey()
					: key.equalsIgnoreCase(ProjectStatusName.TO_DO.getProjectStatusNameString()) ? "#2f6f98"
							: key.equalsIgnoreCase(ProjectStatusName.IN_PROGRESS.getProjectStatusNameString())
									? "rgba(255, 152, 0, 0.7)"
									: "#55a757");
		}

		this.setStartTime(new DateTime(bookingStartDate).withZone(DateTimeZone.UTC).toString());

		this.setEndTime(bookingEndDate == null ? this.getStartTime()
				: new DateTime(bookingEndDate).withZone(DateTimeZone.UTC).toString());

		this.setRecordId(record.getId());
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
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
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the discriminator
	 */
	public String getDiscriminator() {
		return discriminator;
	}

	/**
	 * @param discriminator the discriminator to set
	 */
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	/**
	 * @return the recordId
	 */
	public Long getRecordId() {
		return recordId;
	}

	/**
	 * @param recordId the recordId to set
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProjectSchedulerDTO [title=" + title + ", startTime=" + startTime + ", endTime=" + endTime + ", color="
				+ color + ", discriminator=" + discriminator + ", recordId=" + recordId + "]";
	}

}
