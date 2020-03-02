package com.teamium.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.LabourRule;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LabourRuleDTO {
	/**
	 * Labour Rule Id.
	 */
	private Long labourRuleId;

	/**
	 * Name of the Labour rule.
	 */
	private String labourRuleName;

	/**
	 * Morning Starting Time
	 */
	private String morningStartTime;

	/**
	 * Morning End Time
	 */
	private String morningEndTime;

	/**
	 * Night Starting Time
	 */
	private String nightStartTime;

	/**
	 * Night End Time
	 */
	private String nightEndTime;

	/**
	 * Working Duration in a Day.
	 * 
	 */
	private Long workingDuration;

	/**
	 * Maximum Working Duration in a Day.
	 * 
	 */
	private Long maximumWorkingDuration;

	/**
	 * Working Duration Per Month.
	 * 
	 */
	private Long workingDurationPerMonth;

	/**
	 * Working Day Per Year.
	 * 
	 */
	private Long workingDayPerYear;

	/**
	 * Lunch break Duration.
	 * 
	 */
	private Long lunchDuration;

	/**
	 * Rest Time between two Booking.
	 */
	private Long restDurationBetweenBookings;

	/**
	 * Extra Hour Charges Percentage
	 */
	private Long extraHourChargesPercentage;

	/**
	 * Night Hour Charges Percentage
	 */
	private Long nightHourChargesPercentage;

	/**
	 * Sunday Hour Charges Percentage
	 */
	private Long sundayHourChargesPercentage;

	/**
	 * Holiday Hour Charges Percentage
	 */
	private Long holidayHourChargesPercentage;

	/**
	 * Special Holiday Hour Charges Percentage
	 */
	private Long specialHolidayHourChargesPercentage;
	/**
	 * OverTimePercentange Charges For 1 to 4 Hours
	 */
	private Long overTimeChargesFor1to4Hour;

	/**
	 * OverTimePercentange Charges For 5 to 8 Hours
	 */
	private Long overTimeChargesFor5to8Hour;

	/**
	 * OverTimePercentange Charges For 9 to above Hours
	 */
	private Long overTimeChargesFor9toAboveHour;

	/**
	 * Maximum Weekly Duration
	 */
	private Long maximumWeeklyDuration;

	/**
	 * Average Weekly Duration
	 */
	private Long averageWeeklyDuration;

	/**
	 * Maximum Freelancer Working Day
	 */
	private Long maximumFreelancerWorkingDay;

	/**
	 * Distance To Production
	 */
	private Long distanceToProduction;

	/**
	 * Travel Time Period
	 */
	private Long travelTimePeriod;

	/**
	 * HolidayDTO List.
	 */
	private List<HolidayDTO> holidayDTOs = new ArrayList<>();

	/**
	 * StaffMemberDTO List.
	 */
	private List<StaffMemberDTO> staffMemberDTOs = new ArrayList<>();

	public LabourRuleDTO() {

	}

	public LabourRuleDTO(LabourRule labourRule) {
		if (labourRule.getMorningStartTime() != null) {
			this.morningStartTime = new DateTime(labourRule.getMorningStartTime()).withZone(DateTimeZone.UTC)
					.toString();
		}
		if (labourRule.getMorningEndTime() != null) {
			this.morningEndTime = new DateTime(labourRule.getMorningEndTime()).withZone(DateTimeZone.UTC).toString();
		}
		if (labourRule.getNightStartTime() != null) {
			this.nightStartTime = new DateTime(labourRule.getNightStartTime()).withZone(DateTimeZone.UTC).toString();
		}
		if (labourRule.getNightEndTime() != null) {
			this.nightEndTime = new DateTime(labourRule.getNightEndTime()).withZone(DateTimeZone.UTC).toString();
		}
		this.labourRuleId = labourRule.getLabourRuleId();
		this.labourRuleName = labourRule.getLabourRuleName();
		this.workingDuration = labourRule.getWorkingDuration();
		this.maximumWorkingDuration = labourRule.getMaximumWorkingDuration();
		this.workingDurationPerMonth = labourRule.getWorkingDurationPerMonth();
		this.workingDayPerYear = labourRule.getWorkingDayPerYear();
		this.lunchDuration = labourRule.getLunchDuration();
		this.restDurationBetweenBookings = labourRule.getRestDurationBetweenBookings();
		this.extraHourChargesPercentage = labourRule.getExtraHourChargesPercentage();
		this.nightHourChargesPercentage = labourRule.getNightHourChargesPercentage();
		this.sundayHourChargesPercentage = labourRule.getSundayHourChargesPercentage();
		this.holidayHourChargesPercentage = labourRule.getHolidayHourChargesPercentage();
		this.specialHolidayHourChargesPercentage = labourRule.getSpecialHolidayHourChargesPercentage();
		this.overTimeChargesFor1to4Hour = labourRule.getOverTimeChargesFor1to4Hour();
		this.overTimeChargesFor5to8Hour = labourRule.getOverTimeChargesFor5to8Hour();
		this.overTimeChargesFor9toAboveHour = labourRule.getOverTimeChargesFor9toAboveHour();
		this.maximumWeeklyDuration = labourRule.getMaximumWeeklyDuration();
		this.averageWeeklyDuration = labourRule.getAverageWeeklyDuration();
		this.maximumFreelancerWorkingDay = labourRule.getMaximumFreelancerWorkingDay();
		this.distanceToProduction = labourRule.getDistanceToProduction();
		this.travelTimePeriod = labourRule.getTravelTimePeriod();
		this.holidayDTOs = labourRule.getHolidays().stream().map(HolidayDTO::new).collect(Collectors.toList());
		this.staffMemberDTOs = labourRule.getStaffMembers().stream().map(StaffMemberDTO::new)
				.collect(Collectors.toList());
	}

	public LabourRule getLabourEntity(LabourRule labourRule) {
		if (this.morningStartTime != null) {
			labourRule.setMorningStartTime(
					DateTime.parse(this.morningStartTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}

		if (this.morningEndTime != null) {
			labourRule.setMorningEndTime(
					DateTime.parse(this.morningEndTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}

		if (this.nightStartTime != null) {
			labourRule.setNightStartTime(
					DateTime.parse(this.nightStartTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}

		if (this.nightEndTime != null) {
			labourRule.setNightEndTime(
					DateTime.parse(this.nightEndTime).withZone(DateTimeZone.forID("Asia/Calcutta")).toDate());
		}

		labourRule.setLabourRuleName(labourRuleName);
		labourRule.setWorkingDuration(workingDuration);
		labourRule.setMaximumWorkingDuration(maximumWorkingDuration);
		labourRule.setWorkingDayPerYear(workingDayPerYear);
		labourRule.setWorkingDurationPerMonth(workingDurationPerMonth);
		labourRule.setLunchDuration(lunchDuration);
		labourRule.setRestDurationBetweenBookings(restDurationBetweenBookings);
		labourRule.setExtraHourChargesPercentage(extraHourChargesPercentage);
		labourRule.setNightHourChargesPercentage(nightHourChargesPercentage);
		labourRule.setSundayHourChargesPercentage(sundayHourChargesPercentage);
		labourRule.setHolidayHourChargesPercentage(holidayHourChargesPercentage);
		labourRule.setSpecialHolidayHourChargesPercentage(specialHolidayHourChargesPercentage);
		labourRule.setOverTimeChargesFor1to4Hour(this.overTimeChargesFor1to4Hour);
		labourRule.setOverTimeChargesFor5to8Hour(this.overTimeChargesFor5to8Hour);
		labourRule.setOverTimeChargesFor9toAboveHour(this.overTimeChargesFor9toAboveHour);
		labourRule.setMaximumWeeklyDuration(this.maximumWeeklyDuration);
		labourRule.setAverageWeeklyDuration(this.averageWeeklyDuration);
		labourRule.setMaximumFreelancerWorkingDay(this.maximumFreelancerWorkingDay);
		labourRule.setDistanceToProduction(this.distanceToProduction);
		labourRule.setTravelTimePeriod(this.travelTimePeriod);
		return labourRule;
	}

	/**
	 * @return the morningStartTime
	 */
	public String getMorningStartTime() {
		return morningStartTime;
	}

	/**
	 * @param morningStartTime
	 *            the morningStartTime to set
	 */
	public void setMorningStartTime(String morningStartTime) {
		this.morningStartTime = morningStartTime;
	}

	/**
	 * @return the morningEndTime
	 */
	public String getMorningEndTime() {
		return morningEndTime;
	}

	/**
	 * @param morningEndTime
	 *            the morningEndTime to set
	 */
	public void setMorningEndTime(String morningEndTime) {
		this.morningEndTime = morningEndTime;
	}

	/**
	 * @return the nightStartTime
	 */
	public String getNightStartTime() {
		return nightStartTime;
	}

	/**
	 * @param nightStartTime
	 *            the nightStartTime to set
	 */
	public void setNightStartTime(String nightStartTime) {
		this.nightStartTime = nightStartTime;
	}

	/**
	 * @return the nightEndTime
	 */
	public String getNightEndTime() {
		return nightEndTime;
	}

	/**
	 * @param nightEndTime
	 *            the nightEndTime to set
	 */
	public void setNightEndTime(String nightEndTime) {
		this.nightEndTime = nightEndTime;
	}

	/**
	 * @return the workingDuration
	 */
	public Long getWorkingDuration() {
		return workingDuration;
	}

	/**
	 * @param workingDuration
	 *            the workingDuration to set
	 */
	public void setWorkingDuration(Long workingDuration) {
		this.workingDuration = workingDuration;
	}

	/**
	 * @return the maximumWorkingDuration
	 */
	public Long getMaximumWorkingDuration() {
		return maximumWorkingDuration;
	}

	/**
	 * @param maximumWorkingDuration
	 *            the maximumWorkingDuration to set
	 */
	public void setMaximumWorkingDuration(Long maximumWorkingDuration) {
		this.maximumWorkingDuration = maximumWorkingDuration;
	}

	/**
	 * @return the workingDurationPerMonth
	 */
	public Long getWorkingDurationPerMonth() {
		return workingDurationPerMonth;
	}

	/**
	 * @param workingDurationPerMonth
	 *            the workingDurationPerMonth to set
	 */
	public void setWorkingDurationPerMonth(Long workingDurationPerMonth) {
		this.workingDurationPerMonth = workingDurationPerMonth;
	}

	/**
	 * @return the workingDayPerYear
	 */
	public Long getWorkingDayPerYear() {
		return workingDayPerYear;
	}

	/**
	 * @param workingDayPerYear
	 *            the workingDayPerYear to set
	 */
	public void setWorkingDayPerYear(Long workingDayPerYear) {
		this.workingDayPerYear = workingDayPerYear;
	}

	/**
	 * @return the lunchDuration
	 */
	public Long getLunchDuration() {
		return lunchDuration;
	}

	/**
	 * @param lunchDuration
	 *            the lunchDuration to set
	 */
	public void setLunchDuration(Long lunchDuration) {
		this.lunchDuration = lunchDuration;
	}

	/**
	 * @return the restDurationBetweenBookings
	 */
	public Long getRestDurationBetweenBookings() {
		return restDurationBetweenBookings;
	}

	/**
	 * @param restDurationBetweenBookings
	 *            the restDurationBetweenBookings to set
	 */
	public void setRestDurationBetweenBookings(Long restDurationBetweenBookings) {
		this.restDurationBetweenBookings = restDurationBetweenBookings;
	}

	/**
	 * @return the extraHourChargesPercentage
	 */
	public Long getExtraHourChargesPercentage() {
		return extraHourChargesPercentage;
	}

	/**
	 * @param extraHourChargesPercentage
	 *            the extraHourChargesPercentage to set
	 */
	public void setExtraHourChargesPercentage(Long extraHourChargesPercentage) {
		this.extraHourChargesPercentage = extraHourChargesPercentage;
	}

	/**
	 * @return the nightHourChargesPercentage
	 */
	public Long getNightHourChargesPercentage() {
		return nightHourChargesPercentage;
	}

	/**
	 * @param nightHourChargesPercentage
	 *            the nightHourChargesPercentage to set
	 */
	public void setNightHourChargesPercentage(Long nightHourChargesPercentage) {
		this.nightHourChargesPercentage = nightHourChargesPercentage;
	}

	/**
	 * @return the sundayHourChargesPercentage
	 */
	public Long getSundayHourChargesPercentage() {
		return sundayHourChargesPercentage;
	}

	/**
	 * @param sundayHourChargesPercentage
	 *            the sundayHourChargesPercentage to set
	 */
	public void setSundayHourChargesPercentage(Long sundayHourChargesPercentage) {
		this.sundayHourChargesPercentage = sundayHourChargesPercentage;
	}

	/**
	 * @return the holidayHourChargesPercentage
	 */
	public Long getHolidayHourChargesPercentage() {
		return holidayHourChargesPercentage;
	}

	/**
	 * @param holidayHourChargesPercentage
	 *            the holidayHourChargesPercentage to set
	 */
	public void setHolidayHourChargesPercentage(Long holidayHourChargesPercentage) {
		this.holidayHourChargesPercentage = holidayHourChargesPercentage;
	}

	/**
	 * @return the specialHolidayHourChargesPercentage
	 */
	public Long getSpecialHolidayHourChargesPercentage() {
		return specialHolidayHourChargesPercentage;
	}

	/**
	 * @param specialHolidayHourChargesPercentage
	 *            the specialHolidayHourChargesPercentage to set
	 */
	public void setSpecialHolidayHourChargesPercentage(Long specialHolidayHourChargesPercentage) {
		this.specialHolidayHourChargesPercentage = specialHolidayHourChargesPercentage;
	}

	/**
	 * @return the labourRuleId
	 */
	public Long getLabourRuleId() {
		return labourRuleId;
	}

	/**
	 * @param labourRuleId
	 *            the labourRuleId to set
	 */
	public void setLabourRuleId(Long labourRuleId) {
		this.labourRuleId = labourRuleId;
	}

	/**
	 * @return the labourRuleName
	 */
	public String getLabourRuleName() {
		return labourRuleName;
	}

	/**
	 * @param labourRuleName
	 *            the labourRuleName to set
	 */
	public void setLabourRuleName(String labourRuleName) {
		this.labourRuleName = labourRuleName;
	}

	/**
	 * @return the overTimeChargesFor1to4Hour
	 */
	public Long getOverTimeChargesFor1to4Hour() {
		return overTimeChargesFor1to4Hour;
	}

	/**
	 * @param overTimeChargesFor1to4Hour
	 *            the overTimeChargesFor1to4Hour to set
	 */
	public void setOverTimeChargesFor1to4Hour(Long overTimeChargesFor1to4Hour) {
		this.overTimeChargesFor1to4Hour = overTimeChargesFor1to4Hour;
	}

	/**
	 * @return the overTimeChargesFor5to8Hour
	 */
	public Long getOverTimeChargesFor5to8Hour() {
		return overTimeChargesFor5to8Hour;
	}

	/**
	 * @param overTimeChargesFor5to8Hour
	 *            the overTimeChargesFor5to8Hour to set
	 */
	public void setOverTimeChargesFor5to8Hour(Long overTimeChargesFor5to8Hour) {
		this.overTimeChargesFor5to8Hour = overTimeChargesFor5to8Hour;
	}

	/**
	 * @return the overTimeChargesFor9toAboveHour
	 */
	public Long getOverTimeChargesFor9toAboveHour() {
		return overTimeChargesFor9toAboveHour;
	}

	/**
	 * @param overTimeChargesFor9toAboveHour
	 *            the overTimeChargesFor9toAboveHour to set
	 */
	public void setOverTimeChargesFor9toAboveHour(Long overTimeChargesFor9toAboveHour) {
		this.overTimeChargesFor9toAboveHour = overTimeChargesFor9toAboveHour;
	}

	/**
	 * @return the maximumWeeklyDuration
	 */
	public Long getMaximumWeeklyDuration() {
		return maximumWeeklyDuration;
	}

	/**
	 * @param maximumWeeklyDuration
	 *            the maximumWeeklyDuration to set
	 */
	public void setMaximumWeeklyDuration(Long maximumWeeklyDuration) {
		this.maximumWeeklyDuration = maximumWeeklyDuration;
	}

	/**
	 * @return the averageWeeklyDuration
	 */
	public Long getAverageWeeklyDuration() {
		return averageWeeklyDuration;
	}

	/**
	 * @param averageWeeklyDuration
	 *            the averageWeeklyDuration to set
	 */
	public void setAverageWeeklyDuration(Long averageWeeklyDuration) {
		this.averageWeeklyDuration = averageWeeklyDuration;
	}

	/**
	 * @return the maximumFreelancerWorkingDay
	 */
	public Long getMaximumFreelancerWorkingDay() {
		return maximumFreelancerWorkingDay;
	}

	/**
	 * @param maximumFreelancerWorkingDay
	 *            the maximumFreelancerWorkingDay to set
	 */
	public void setMaximumFreelancerWorkingDay(Long maximumFreelancerWorkingDay) {
		this.maximumFreelancerWorkingDay = maximumFreelancerWorkingDay;
	}

	/**
	 * @return the distanceToProduction
	 */
	public Long getDistanceToProduction() {
		return distanceToProduction;
	}

	/**
	 * @param distanceToProduction
	 *            the distanceToProduction to set
	 */
	public void setDistanceToProduction(Long distanceToProduction) {
		this.distanceToProduction = distanceToProduction;
	}

	/**
	 * @return the travelTimePeriod
	 */
	public Long getTravelTimePeriod() {
		return travelTimePeriod;
	}

	/**
	 * @param travelTimePeriod
	 *            the travelTimePeriod to set
	 */
	public void setTravelTimePeriod(Long travelTimePeriod) {
		this.travelTimePeriod = travelTimePeriod;
	}

	/**
	 * @return the holidayDTOs
	 */
	public List<HolidayDTO> getHolidayDTOs() {
		return holidayDTOs;
	}

	/**
	 * @param holidayDTOs
	 *            the holidayDTOs to set
	 */
	public void setHolidayDTOs(List<HolidayDTO> holidayDTOs) {
		this.holidayDTOs = holidayDTOs;
	}

	/**
	 * @return the staffMemberDTOs
	 */
	public List<StaffMemberDTO> getStaffMemberDTOs() {
		return staffMemberDTOs;
	}

	/**
	 * @param staffMemberDTOs
	 *            the staffMemberDTOs to set
	 */
	public void setStaffMemberDTOs(List<StaffMemberDTO> staffMemberDTOs) {
		this.staffMemberDTOs = staffMemberDTOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LabourRuleDTO [labourRuleId=" + labourRuleId + ", labourRuleName=" + labourRuleName
				+ ", morningStartTime=" + morningStartTime + ", morningEndTime=" + morningEndTime + ", nightStartTime="
				+ nightStartTime + ", nightEndTime=" + nightEndTime + ", workingDuration=" + workingDuration
				+ ", maximumWorkingDuration=" + maximumWorkingDuration + ", workingDurationPerMonth="
				+ workingDurationPerMonth + ", workingDayPerYear=" + workingDayPerYear + ", lunchDuration="
				+ lunchDuration + ", restDurationBetweenBookings=" + restDurationBetweenBookings
				+ ", extraHourChargesPercentage=" + extraHourChargesPercentage + ", nightHourChargesPercentage="
				+ nightHourChargesPercentage + ", sundayHourChargesPercentage=" + sundayHourChargesPercentage
				+ ", holidayHourChargesPercentage=" + holidayHourChargesPercentage
				+ ", specialHolidayHourChargesPercentage=" + specialHolidayHourChargesPercentage + "]";
	}

}
