package com.teamium.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.teamium.exception.UnprocessableEntityException;

/**
 * The Milestone Entity
 *
 */
@Embeddable
public class Milestone {

	/**
	 * The Enum of available MilestoneName.
	 */
	public enum MilestoneName {
	Maintenance("Maintenance"), SLA("SLA"), License("License"), Warranty("Warranty");

		private String milestoneName;

		private MilestoneName(String milestoneName) {
			this.milestoneName = milestoneName;
		}

		/**
		 * Gets the MilestoneName value.
		 *
		 * @return the MilestoneName value
		 */
		public String getMilestoneNameString() {
			return this.milestoneName;
		}

		public static MilestoneName getEnum(String value) {
			for (MilestoneName v : values()) {
				if (v.getMilestoneNameString().equalsIgnoreCase(value)) {
					return v;
				}
			}
			throw new UnprocessableEntityException("Invalid Milestone Name.");
		}

		public static List<String> getAllMilestones() {
			return Arrays.asList(values()).stream().map(milestone -> milestone.getMilestoneNameString())
					.sorted((milestone1, milestone2) -> milestone1.toLowerCase().compareTo(milestone2.toLowerCase()))
					.collect(Collectors.toList());
		}
	}

	@Transient
	private String type;

	/**
	 * The MilestoneType
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_milestone_type_id", nullable = false)
	private MilestoneType milestoneType;

	@Column(name = "c_date")
	private DateTime date;

	@Transient
	private String expirationColor;

	/**
	 * @return the type
	 */
	public String getType() {
		if (this.milestoneType != null) {
			this.type = this.milestoneType.getName();
		}
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the milestoneType
	 */
	public MilestoneType getMilestoneType() {
		return milestoneType;
	}

	/**
	 * @param milestoneType the milestoneType to set
	 */
	public void setMilestoneType(MilestoneType milestoneType) {
		this.milestoneType = milestoneType;
	}

	/**
	 * @return the date
	 */
	public DateTime getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(DateTime date) {
		this.date = date;
	}

	/**
	 * @return the remaining days to expire
	 */
	@Transient
	public int getRemainingDays() {
		LocalDate currentDate = LocalDate.now();
		return Days.daysBetween(currentDate, date.toLocalDate()).getDays();
	}

	/**
	 * @return the expirationColor
	 */
	public String getExpirationColor() {
		return expirationColor;
	}

	/**
	 * @param expirationColor the expirationColor to set
	 */
	public void setExpirationColor(String expirationColor) {
		this.expirationColor = expirationColor;
	}

}
