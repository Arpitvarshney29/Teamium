package com.teamium.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.prod.resources.staff.StaffMember;

@Entity
@Table(name = "t_labour_rule")
public class LabourRule {

	/**
	 * LabourRule ID
	 */
	@Id
	@Column(name = "c_labour_rule_id", insertable = false, updatable = false)
	@TableGenerator(name = "idLabourrule_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "labourrule_idlabourrule_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idLabourrule_seq")
	private Long labourRuleId;
	/**
	 * Name of the Labour rule.
	 */
	@Column(name = "c_labour_rule_name")
	private String labourRuleName;
	/**
	 * Morning Starting Time
	 */
	@Column(name = "c_morning_start_time")
	@Temporal(TemporalType.TIME)
	private Date morningStartTime;

	/**
	 * Morning End Time
	 */
	@Column(name = "c_morning_end_time")
	@Temporal(TemporalType.TIME)
	private Date morningEndTime;

	/**
	 * Night Starting Time
	 */
	@Column(name = "c_night_start_time")
	@Temporal(TemporalType.TIME)
	private Date nightStartTime;

	/**
	 * Night End Time
	 */
	@Column(name = "c_night_end_time")
	private Date nightEndTime;

	/**
	 * Working Duration in a Day.
	 * 
	 */
	@Column(name = "c_working_duration")
	private Long workingDuration;

	/**
	 * Maximum Working Duration in a Day.
	 * 
	 */
	@Column(name = "c_maximum_working_duration")
	private Long maximumWorkingDuration;

	/**
	 * Working Duration Per Month.
	 * 
	 */
	@Column(name = "c_working_duration_per_month")
	private Long workingDurationPerMonth;

	/**
	 * Working Day Per Year.
	 * 
	 */
	@Column(name = "c_working_day_per_year")
	private Long workingDayPerYear;

	/**
	 * Lunch break Duration.
	 * 
	 */
	@Column(name = "c_lunch_duration")
	private Long lunchDuration;
	/**
	 * Rest Time between two Booking.
	 */
	@Column(name = "c_rest_duration_between_booking")
	private Long restDurationBetweenBookings;

	/**
	 * Extra Hour Charges Percentage
	 */
	@Column(name = "c_extar_hour_charges_percentage")
	private Long extraHourChargesPercentage;

	/**
	 * Night Hour Charges Percentage
	 */
	@Column(name = "c_night_hour_charges_percentage")
	private Long nightHourChargesPercentage;

	/**
	 * Sunday Hour Charges Percentage
	 */
	@Column(name = "c_sunday_hour_charges_percentage")
	private Long sundayHourChargesPercentage;

	/**
	 * Holiday Hour Charges Percentage
	 */
	@Column(name = "c_holiday_hour_charges_percentage")
	private Long holidayHourChargesPercentage;

	/**
	 * Special Holiday Hour Charges Percentage
	 */
	@Column(name = "c_special_holiday_hour_charges_percentage")
	private Long specialHolidayHourChargesPercentage;

	/**
	 * OverTimePercentange Charges For 1 to 4 Hours
	 */
	@Column(name = "c_overtime_charges_for_1_to_4_Hour")
	private Long overTimeChargesFor1to4Hour;

	/**
	 * OverTimePercentange Charges For 5 to 8 Hours
	 */
	@Column(name = "c_overtime_charges_for_5_to_8_Hour")
	private Long overTimeChargesFor5to8Hour;

	/**
	 * OverTimePercentange Charges For 9 to above Hours
	 */
	@Column(name = "c_overtime_charges_for_9_to_above_Hour")
	private Long overTimeChargesFor9toAboveHour;

	/**
	 * Maximum Weekly Duration
	 */
	@Column(name = "c_maximum_weekly_duration")
	private Long maximumWeeklyDuration;

	/**
	 * Average Weekly Duration
	 */
	@Column(name = "c_average_weekly_duration")
	private Long averageWeeklyDuration;

	/**
	 * Maximum Freelancer Working Day
	 */
	@Column(name = "c_maximum_freelancer_working_day")
	private Long maximumFreelancerWorkingDay;

	/**
	 * Distance To Production
	 */
	@Column(name = "c_distance_to_production")
	private Long distanceToProduction;

	/**
	 * Travel Time Period
	 */
	@Column(name = "c_travel_time_period")
	private Long travelTimePeriod;

	/**
	 * List of Holiday For Labour
	 */
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_labour_holiday", nullable = true)
	List<Holiday> holidays = new ArrayList<>();

	@ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH })
	@JoinTable(name = "t_labour_staffmember", joinColumns = { @JoinColumn(name = "labour_id") }, inverseJoinColumns = {
			@JoinColumn(name = "staff_member_id") })
	private List<StaffMember> staffMembers = new ArrayList<>();

	public LabourRule() {

	}

	/**
	 * @return the morningStartTime
	 */
	public Date getMorningStartTime() {
		return morningStartTime;
	}

	/**
	 * @param morningStartTime
	 *            the morningStartTime to set
	 */
	public void setMorningStartTime(Date morningStartTime) {
		this.morningStartTime = morningStartTime;
	}

	/**
	 * @return the morningEndTime
	 */
	public Date getMorningEndTime() {
		return morningEndTime;
	}

	/**
	 * @param morningEndTime
	 *            the morningEndTime to set
	 */
	public void setMorningEndTime(Date morningEndTime) {
		this.morningEndTime = morningEndTime;
	}

	/**
	 * @return the nightStartTime
	 */
	public Date getNightStartTime() {
		return nightStartTime;
	}

	/**
	 * @param nightStartTime
	 *            the nightStartTime to set
	 */
	public void setNightStartTime(Date nightStartTime) {
		this.nightStartTime = nightStartTime;
	}

	/**
	 * @return the nightEndTime
	 */
	public Date getNightEndTime() {
		return nightEndTime;
	}

	/**
	 * @param nightEndTime
	 *            the nightEndTime to set
	 */
	public void setNightEndTime(Date nightEndTime) {
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
	 * @return the holidays
	 */
	public List<Holiday> getHolidays() {
		return holidays;
	}

	/**
	 * @param holidays
	 *            the holidays to set
	 */
	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}

	/**
	 * @return the staffMembers
	 */
	public List<StaffMember> getStaffMembers() {
		return staffMembers;
	}

	/**
	 * @param staffMembers
	 *            the staffMembers to set
	 */
	public void setStaffMembers(List<StaffMember> staffMembers) {
		this.staffMembers = staffMembers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LabourRule [labourRuleId=" + labourRuleId + ", labourRuleName=" + labourRuleName + ", morningStartTime="
				+ morningStartTime + ", morningEndTime=" + morningEndTime + ", nightStartTime=" + nightStartTime
				+ ", nightEndTime=" + nightEndTime + ", workingDuration=" + workingDuration
				+ ", maximumWorkingDuration=" + maximumWorkingDuration + ", workingDurationPerMonth="
				+ workingDurationPerMonth + ", workingDayPerYear=" + workingDayPerYear + ", lunchDuration="
				+ lunchDuration + ", restDurationBetweenBookings=" + restDurationBetweenBookings
				+ ", extraHourChargesPercentage=" + extraHourChargesPercentage + ", nightHourChargesPercentage="
				+ nightHourChargesPercentage + ", sundayHourChargesPercentage=" + sundayHourChargesPercentage
				+ ", holidayHourChargesPercentage=" + holidayHourChargesPercentage
				+ ", specialHolidayHourChargesPercentage=" + specialHolidayHourChargesPercentage + "]";
	}

}
