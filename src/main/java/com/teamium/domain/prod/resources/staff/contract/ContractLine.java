/**
 * 
 */
package com.teamium.domain.prod.resources.staff.contract;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.projects.Booking;

/**
 * Describe a contract line
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_contract_line")
public class ContractLine extends AbstractEntity {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6724271035728978263L;

	/**
	 * The ID
	 */
	@Id
	@Column(name = "c_idcontractline", insertable = false, updatable = false)
	@TableGenerator(name = "idContractLine_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "contract_line_idcontractline_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idContractLine_seq")
	private Long id;

	/**
	 * The assiociated contract
	 */
	@ManyToOne
	@JoinColumn(name = "c_idcontract")
	private Contract contract;

	/**
	 * The start of the period
	 */
	@Column(name = "c_startperiod")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startPeriod;

	/**
	 * the end of the period
	 */
	@Column(name = "c_endperiod")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endPeriod;

	/**
	 * The expected quantity
	 */
	@Column(name = "c_duequantity")
	private Integer dueQuantity;

	/**
	 * The effective quantity
	 */
	@Column(name = "c_effectivequantity")
	private Integer effectiveQuantity;

	/**
	 * The quantity effected while in extra hours
	 */
	@Column(name = "c_extraquantity")
	private Integer extraQuantity;

	/**
	 * The quantity effected in night hours
	 */
	@Column(name = "c_nightquantity")
	private Integer nightQuantity;

	/**
	 * The quantity effected in travel hours
	 */
	@Column(name = "c_travelquantity")
	private Integer travelQuantity;

	/**
	 * The quantity effected in travel hours
	 */
	@Column(name = "c_holidayquantity")
	private Integer holidayQuantity;

	/**
	 * The assiociated bookings
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinTable(name = "t_contract_booking", joinColumns = @JoinColumn(name = "c_idcontractline"), inverseJoinColumns = @JoinColumn(name = "c_idbooking"))
	private List<Booking> bookings;

	/**
	 * The quantity effected in travel hours
	 */
	@Column(name = "c_manual_update_on")
	private Boolean maunalUpdateOn = false;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;

	}

	/**
	 * @return the contract
	 */
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return the startPeriod
	 */
	public Calendar getStartPeriod() {
		return startPeriod;
	}

	/**
	 * @param startPeriod
	 *            the startPeriod to set
	 */
	public void setStartPeriod(Calendar startPeriod) {
		this.startPeriod = startPeriod;
	}

	/**
	 * @return the endPeriod
	 */
	public Calendar getEndPeriod() {
		return endPeriod;
	}

	/**
	 * @param endPeriod
	 *            the endPeriod to set
	 */
	public void setEndPeriod(Calendar endPeriod) {
		this.endPeriod = endPeriod;
	}

	/**
	 * @return the dueQuantity
	 */
	public Integer getDueQuantity() {
		if (dueQuantity == null) {
			dueQuantity = new Integer(0);
			for (Booking b : bookings) {
				if (b.getUnitUsed().getKey().equals("H")) {
					Long tpsEnMillis = b.getTo().getTimeInMillis() - b.getFrom().getTimeInMillis();
					tpsEnMillis = tpsEnMillis / 3600000;
					dueQuantity += tpsEnMillis.intValue();
					if (b.getFrom().get(Calendar.MINUTE) - b.getTo().get(Calendar.MINUTE) < 0)
						dueQuantity++;
					if (this.getContract().getBreakHourStart() != null
							&& this.getContract().getBreakHourEnd() != null) {
						Long tpsDePause = 0L;
						Calendar breakHourS = Calendar.getInstance();
						Calendar breakHourE = Calendar.getInstance();
						Calendar breakHourSTemp = Calendar.getInstance();
						Calendar breakHourETemp = Calendar.getInstance();
						breakHourS.setTime(this.getContract().getBreakHourStart());
						breakHourE.setTime(this.getContract().getBreakHourEnd());
						breakHourSTemp.setTime(this.getContract().getBreakHourStart());
						breakHourETemp.setTime(this.getContract().getBreakHourEnd());
						if (this.getContract().getBreakHourStart().before(this.getContract().getBreakHourEnd())) {
							breakHourS.setTime(b.getFrom().getTime());
							breakHourE.setTime(b.getFrom().getTime());
							breakHourS.set(Calendar.HOUR_OF_DAY, breakHourSTemp.get(Calendar.HOUR_OF_DAY));
							breakHourS.set(Calendar.MINUTE, breakHourSTemp.get(Calendar.MINUTE));
							breakHourE.set(Calendar.HOUR_OF_DAY, breakHourETemp.get(Calendar.HOUR_OF_DAY));
							breakHourE.set(Calendar.MINUTE, breakHourETemp.get(Calendar.MINUTE));
						} else {
							breakHourS.setTime(b.getFrom().getTime());
							breakHourE.setTime(b.getTo().getTime());
							breakHourS.set(Calendar.HOUR_OF_DAY, breakHourSTemp.get(Calendar.HOUR_OF_DAY));
							breakHourS.set(Calendar.MINUTE, breakHourSTemp.get(Calendar.MINUTE));
							breakHourE.set(Calendar.HOUR_OF_DAY, breakHourETemp.get(Calendar.HOUR_OF_DAY));
							breakHourE.set(Calendar.MINUTE, breakHourETemp.get(Calendar.MINUTE));
						}
						if (breakHourS.before(b.getFrom())) {
							if (breakHourE.after(b.getFrom()))
								tpsDePause = breakHourE.getTimeInMillis() - b.getFrom().getTimeInMillis();
						} else if (breakHourS.before(b.getTo())) {
							if (breakHourE.before(b.getTo()))
								tpsDePause = breakHourE.getTimeInMillis() - breakHourS.getTimeInMillis();
							else
								tpsDePause = b.getTo().getTimeInMillis() - breakHourS.getTimeInMillis();
						}
						if (tpsDePause > 0) {
							tpsDePause = tpsDePause / 3600000;
							dueQuantity -= tpsDePause.intValue();
						}

					}
				} else {
					dueQuantity = 1;

				}

			}

		}
		return dueQuantity;
	}

	/**
	 * @param dueQuantity
	 *            the dueQuantity to set
	 */
	public void setDueQuantity(Integer dueQuantity) {
		this.dueQuantity = dueQuantity;
	}

	/**
	 * @return the effectiveQuantity
	 */
	public Integer getEffectiveQuantity() {
		if (effectiveQuantity == null) {
			effectiveQuantity = 0;
			for (Booking b : bookings) {
				if (b.getFrom() != null && b.getTo() != null) {
					if (contract.getUnit().getKey().equals("H") || b.getUnitUsed().getKey().equals("H")) {
						Long tpsEnMillis = b.getTo().getTimeInMillis() - b.getFrom().getTimeInMillis();
						tpsEnMillis = tpsEnMillis / 3600000;
						effectiveQuantity += tpsEnMillis.intValue();
						if (b.getFrom().get(Calendar.MINUTE) - b.getTo().get(Calendar.MINUTE) < 0)
							effectiveQuantity++;
						if (this.getContract().getBreakHourStart() != null
								&& this.getContract().getBreakHourEnd() != null) {
							Long tpsDePause = 0L;
							Calendar breakHourS = Calendar.getInstance();
							Calendar breakHourE = Calendar.getInstance();
							Calendar breakHourS2 = Calendar.getInstance();
							Calendar breakHourE2 = Calendar.getInstance();
							Calendar breakHourSTemp = Calendar.getInstance();
							Calendar breakHourETemp = Calendar.getInstance();
							breakHourS.setTime(this.getContract().getBreakHourStart());
							breakHourE.setTime(this.getContract().getBreakHourEnd());
							breakHourSTemp.setTime(this.getContract().getBreakHourStart());
							breakHourETemp.setTime(this.getContract().getBreakHourEnd());
							if (this.getContract().getBreakHourStart().before(this.getContract().getBreakHourEnd())) {
								breakHourS.setTime(b.getFrom().getTime());
								breakHourE.setTime(b.getFrom().getTime());
								breakHourS.set(Calendar.HOUR_OF_DAY, breakHourSTemp.get(Calendar.HOUR_OF_DAY));
								breakHourS.set(Calendar.MINUTE, breakHourSTemp.get(Calendar.MINUTE));
								breakHourE.set(Calendar.HOUR_OF_DAY, breakHourETemp.get(Calendar.HOUR_OF_DAY));
								breakHourE.set(Calendar.MINUTE, breakHourETemp.get(Calendar.MINUTE));

								breakHourS2.setTime(b.getFrom().getTime());
								breakHourE2.setTime(b.getFrom().getTime());
								breakHourS2.set(Calendar.HOUR_OF_DAY, breakHourSTemp.get(Calendar.HOUR_OF_DAY));
								breakHourS2.set(Calendar.MINUTE, breakHourSTemp.get(Calendar.MINUTE));
								breakHourE2.set(Calendar.HOUR_OF_DAY, breakHourETemp.get(Calendar.HOUR_OF_DAY));
								breakHourE2.set(Calendar.MINUTE, breakHourETemp.get(Calendar.MINUTE));
								breakHourS2.roll(Calendar.DAY_OF_MONTH, true);
								breakHourE2.roll(Calendar.DAY_OF_MONTH, true);
							} else {
								breakHourS.setTime(b.getFrom().getTime());
								breakHourE.setTime(b.getTo().getTime());
								breakHourS.set(Calendar.HOUR_OF_DAY, breakHourSTemp.get(Calendar.HOUR_OF_DAY));
								breakHourS.set(Calendar.MINUTE, breakHourSTemp.get(Calendar.MINUTE));
								breakHourE.set(Calendar.HOUR_OF_DAY, breakHourETemp.get(Calendar.HOUR_OF_DAY));
								breakHourE.set(Calendar.MINUTE, breakHourETemp.get(Calendar.MINUTE));

								breakHourS2.setTime(b.getFrom().getTime());
								breakHourE2.setTime(b.getTo().getTime());
								breakHourS2.set(Calendar.HOUR_OF_DAY, breakHourSTemp.get(Calendar.HOUR_OF_DAY));
								breakHourS2.set(Calendar.MINUTE, breakHourSTemp.get(Calendar.MINUTE));
								breakHourE2.set(Calendar.HOUR_OF_DAY, breakHourETemp.get(Calendar.HOUR_OF_DAY));
								breakHourE2.set(Calendar.MINUTE, breakHourETemp.get(Calendar.MINUTE));
								breakHourS2.roll(Calendar.DATE, true);
								breakHourE2.roll(Calendar.DATE, true);
							}

							if (breakHourS.before(b.getFrom())) {
								if (breakHourE.after(b.getFrom()))
									tpsDePause += breakHourE.getTimeInMillis() - b.getFrom().getTimeInMillis();
							} else if (breakHourS.before(b.getTo())) {
								if (breakHourE.before(b.getTo()))
									tpsDePause += breakHourE.getTimeInMillis() - breakHourS.getTimeInMillis();
								else
									tpsDePause += b.getTo().getTimeInMillis() - breakHourS.getTimeInMillis();
							}
							if (breakHourS2.before(b.getFrom())) {
								if (breakHourE2.after(b.getFrom()))
									tpsDePause += breakHourE.getTimeInMillis() - b.getFrom().getTimeInMillis();
							} else if (breakHourS2.before(b.getTo())) {
								if (breakHourE2.before(b.getTo()))
									tpsDePause += breakHourE2.getTimeInMillis() - breakHourS2.getTimeInMillis();
								else
									tpsDePause += b.getTo().getTimeInMillis() - breakHourS2.getTimeInMillis();
							}
							if (tpsDePause > 0) {
								tpsDePause = tpsDePause / 3600000;
								effectiveQuantity -= tpsDePause.intValue();
							}

						}
					} else {

						effectiveQuantity = 1;

					}
				}
			}
		}
		return effectiveQuantity;
	}

	/**
	 * @param effectiveQuantity
	 *            the effectiveQuantity to set
	 */
	public void setEffectiveQuantity(Integer effectiveQuantity) {
		this.effectiveQuantity = effectiveQuantity;
	}

	/**
	 * @return the extraQuantity
	 */
	public Integer getExtraQuantity() {
//		if (extraQuantity == null) {
//			if (contract.getUnit().getKey().equals("H")) {
//				int lowestThreshold = 0;
//				for (ThresholdCouple tc : this.getContract().getConvention().getThresholdCouples()) {
//					if (lowestThreshold == 0 || lowestThreshold > tc.getThreshold())
//						lowestThreshold = tc.getThreshold();
//				}
//				extraQuantity = (this.getEffectiveQuantity() - lowestThreshold);
//				// extraQuantity = this.getEffectiveQuantity() -
//				// this.getContract().getConvention().getThreshold();
//			} else {
//				/*
//				 * Long tpsEnMillis = endPeriod.getTimeInMillis() -
//				 * startPeriod.getTimeInMillis(); tpsEnMillis = tpsEnMillis / 3600000; Integer
//				 * tempQuantity = tpsEnMillis.intValue();
//				 * if(bookings.get(0).getFrom().get(Calendar.MINUTE) -
//				 * bookings.get(0).getTo().get(Calendar.MINUTE) < 0) tempQuantity++;
//				 * if(this.getContract().getBreakHour() != null) { tempQuantity--; }
//				 * extraQuantity = tempQuantity -
//				 * this.getContract().getConvention().getThreshold();
//				 */
//				extraQuantity = 0;
//			}
//		}
		if (extraQuantity==null||extraQuantity < 0)
			extraQuantity = 0;
		return extraQuantity;
	}

	/**
	 * @param extraQuantity
	 *            the extraQuantity to set
	 */
	public void setExtraQuantity(Integer extraQuantity) {
		this.extraQuantity = extraQuantity;
	}

	/**
	 * @return the nightQuantity
	 */
	// TODO Revoir le calcul - Tous les cas ne sont pas gérés
	public Integer getNightQuantity() {
//		if (nightQuantity == null) {
//			if (contract.getUnit().getKey().equals("H")) {
//				nightQuantity = new Integer(0);
//				List<Range> ranges = this.getContract().getConvention().getNightRanges();
//				for (Booking b : bookings) {
//					Calendar from = b.getFrom() == null ? b.getFrom() : b.getFrom();
//					Calendar to = b.getTo() == null ? b.getTo() : b.getTo();
//
//					for (Range r : ranges) {
//						Integer temp = 0;
//						Calendar min = Calendar.getInstance();
//						Calendar max = Calendar.getInstance();
//						Calendar tMin = Calendar.getInstance();
//						Calendar tMax = Calendar.getInstance();
//
//						min.setTime(from.getTime());
//						max.setTime(to.getTime());
//						tMin.setTime(r.getMin());
//						tMax.setTime(r.getMax());
//
//						min.set(Calendar.HOUR_OF_DAY, tMin.get(Calendar.HOUR_OF_DAY));
//						min.set(Calendar.MINUTE, tMin.get(Calendar.MINUTE));
//						min.set(Calendar.SECOND, tMin.get(Calendar.SECOND));
//						max.set(Calendar.HOUR_OF_DAY, tMax.get(Calendar.HOUR_OF_DAY));
//						max.set(Calendar.MINUTE, tMax.get(Calendar.MINUTE));
//						max.set(Calendar.SECOND, tMax.get(Calendar.SECOND));
//
//						/*
//						 * String sMin =
//						 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(
//						 * min.getTime()); String sMax =
//						 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(
//						 * max.getTime());
//						 * 
//						 * String sFrom =
//						 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(
//						 * from.getTime()); String sTo =
//						 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(to
//						 * .getTime());
//						 */
//
//						if (from.get(Calendar.HOUR_OF_DAY) > to.get(Calendar.HOUR_OF_DAY)) // if the booking is on two
//																							// differents day
//						{
//							if (from.get(Calendar.HOUR_OF_DAY) < min.get(Calendar.HOUR_OF_DAY)) {
//
//								temp = 24 - min.get(Calendar.HOUR_OF_DAY);
//								if (min.get(Calendar.MINUTE) != 0 && max.get(Calendar.MINUTE) == 0)
//									temp += 1;
//								if (temp > 0)
//									nightQuantity += temp;
//							}
//							/*
//							 * else { temp = 24 - from.get(Calendar.HOUR_OF_DAY); if(temp > 0) nightQuantity
//							 * += temp; }
//							 */
//							if (to.get(Calendar.HOUR_OF_DAY) > max.get(Calendar.HOUR_OF_DAY)) {
//								temp = max.get(Calendar.HOUR_OF_DAY); // a revoir avec les minutes
//								if (min.get(Calendar.MINUTE) != 0 && max.get(Calendar.MINUTE) == 0)
//									temp += 1;
//								if (temp > 0)
//									nightQuantity += temp;
//							}
//
//							else {
//								temp = to.get(Calendar.HOUR_OF_DAY) - min.get(Calendar.HOUR_OF_DAY);
//								if (temp > 0)
//									nightQuantity += temp;
//							}
//						} else {
//							boolean done = false;
//
//							if (to.get(Calendar.HOUR_OF_DAY) <= max.get(Calendar.HOUR_OF_DAY)
//									&& from.get(Calendar.HOUR_OF_DAY) >= min.get(Calendar.HOUR_OF_DAY)) // if the
//																										// booking is in
//																										// the range
//							{
//
//								temp = to.get(Calendar.HOUR_OF_DAY) - from.get(Calendar.HOUR_OF_DAY);
//								if (temp > 0)
//									nightQuantity += temp;
//								done = true;
//							}
//							if (!done && to.get(Calendar.HOUR_OF_DAY) >= max.get(Calendar.HOUR_OF_DAY)
//									&& from.get(Calendar.HOUR_OF_DAY) >= min.get(Calendar.HOUR_OF_DAY)) // if the
//																										// booking is in
//																										// the range
//							{
//								temp = max.get(Calendar.HOUR_OF_DAY) - from.get(Calendar.HOUR_OF_DAY);
//								if (temp > 0)
//									nightQuantity += temp;
//								done = true;
//							}
//							if (!done && to.get(Calendar.HOUR_OF_DAY) <= max.get(Calendar.HOUR_OF_DAY)
//									&& from.get(Calendar.HOUR_OF_DAY) <= min.get(Calendar.HOUR_OF_DAY)) // if the
//																										// booking is in
//																										// the range
//							{
//								temp = to.get(Calendar.HOUR_OF_DAY) - min.get(Calendar.HOUR_OF_DAY);
//								if (temp > 0)
//									nightQuantity += temp;
//							}
//
//						}
//					}
//				}
//			} else {
//				nightQuantity = 0;
//			}
//		}
		if (nightQuantity ==null ||nightQuantity < 0)
			nightQuantity = 0;
		return nightQuantity;
	}

	/**
	 * @param nightQuantity
	 *            the nightQuantity to set
	 */
	public void setNightQuantity(Integer nightQuantity) {
		this.nightQuantity = nightQuantity;
	}

	/**
	 * @return the bookings
	 */
	public List<Booking> getBookings() {
		if (bookings == null)
			bookings = new ArrayList<Booking>();
		return bookings;
	}

	/**
	 * Recalculate all the calculated attributes
	 */
	public void calculate() {
		dueQuantity = null;
		effectiveQuantity = null;
		extraQuantity = null;
		nightQuantity = null;
		this.getDueQuantity();
		this.getEffectiveQuantity();
		this.getExtraQuantity();
		this.getNightQuantity();
	}

	/**
	 * Test if the booking can fit into this contract line
	 * 
	 * @param booking
	 * @return boolean
	 */
	public Boolean canFit(Booking booking) {
		if (booking.getUnitUsed() == null)
			return false; // If the unit is null, return false

		if (contract.getUnit().getKey().equals("H")) // If the contract is about hours
		{

			if (bookings == null || bookings.isEmpty()) // If there is no booking inside the list "bookings"
			{
				if ((booking.getTo().before(endPeriod) || booking.getTo().equals(endPeriod))
						&& (booking.getFrom().after(startPeriod) || booking.getFrom().equals(startPeriod)))
					return true; // if there is no booking inside the list and the booking fit into the range
				else
					return false; // if not, it return false
			}

			if (!booking.getUnitUsed().getKey().equals("H") || !bookings.get(0).getUnitUsed().getKey().equals("H")) // If
																													// the
																													// booking
																													// unit
																													// is
																													// different
																													// from
																													// H
																													// or
																													// the
																													// bookings
																													// already
																													// contains
																													// a
																													// booking
																													// different
																													// from
																													// H
				return false;

			Calendar next = null;
			Calendar before = null;

			for (Booking b : bookings) // This loop get the bookings before and after the place the booking should be
										// inserted
			{
				if (booking.getTo().before(b.getFrom()) || booking.getTo().equals(b.getFrom())) {
					next = b.getFrom();
				} else {
					before = b.getTo();
				}
			}

			if (next == null)
				next = endPeriod;
			if (before == null)
				before = startPeriod;

			// The place where the booking belongs is between before and after

			/*
			 * Debug code String nextS =
			 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(
			 * next.getTime()); String beforeS =
			 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(
			 * before.getTime());
			 */

			if ((next.after(booking.getTo()) || next.equals(booking.getTo()))
					&& (before.equals(booking.getFrom()) || before.before(booking.getFrom())))
				return true; // If the booking fits (i.e. It starts after the booking before it and ends
								// before the booking after it begins
			else
				return false; // If the booking does not fit

		} else // If the contract is about another Unit
		{
			if ((booking.getUnitUsed().getKey().equals(contract.getUnit()) && bookings.isEmpty())) // If the booking is
																									// about Hours or
																									// Units and the
																									// bookings list is
																									// empty
				return true;
			else
				return false;
		}
	}

	/**
	 * The cost for the line
	 * 
	 * @return
	 */
	// Fix contestable pour la majoration des heures
	public Float getLineCost() {
		Float cost = 0F;
		try {
			boolean found = false;
			if (contract.getUnit().getKey().equals("H")) {
				cost += this.getEffectiveQuantity() * this.getContract().getRate();
				for (Holiday h : this.getContract().getConvention().getHolidays()) {
					if (h.getDate().getTime().equals(this.getStartPeriod().getTime())) {
						cost += this.getEffectiveQuantity() * this.getContract().getRate() * h.getRate();
						found = true;
						break;
					}
				}
				if (!found && this.startPeriod.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cost += this.getEffectiveQuantity() * this.getContract().getRate()
							* this.getContract().getConvention().getSundayMajoration();
					found = true;
				}
				if (!found) {
					// Ligne d'origine
					// cost += this.getExtraQuantity() * this.getContract().getRate() *
					// this.getContract().getConvention().getMajoration();
					for (ThresholdCouple tc : this.getContract().getConvention().getThresholdCouples()) {
						int tcExtraQt = (this.getEffectiveQuantity() - tc.getThreshold());
						cost += tcExtraQt * this.getContract().getRate() * tc.getMajoration();
						cost += tcExtraQt > 4 ? (tcExtraQt - 4) * this.getContract().getRate() * tc.getMajoration() : 0;
					}
					/*
					 * cost += this.getExtraQuantity() * this.getContract().getRate() *
					 * this.getContract().getConvention().getMajoration(); cost +=
					 * this.getExtraQuantity() > 4 ? (this.getExtraQuantity() - 4)*
					 * this.getContract().getRate() *
					 * this.getContract().getConvention().getMajoration():0;
					 */
					cost += this.getNightQuantity() * this.getContract().getRate()
							* this.getContract().getConvention().getNightMajoration();
				}
			} else {
				cost += this.getEffectiveQuantity() * this.getContract().getRate();
				for (Holiday h : this.getContract().getConvention().getHolidays()) {
					/**
					 * String sefrt =
					 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(h.getDate().getTime());
					 * String jrtey =
					 * DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM).format(this.startPeriod.getTime());
					 **/
					if (h.getDate().get(Calendar.DAY_OF_YEAR) == this.getStartPeriod().get(Calendar.DAY_OF_YEAR)
							&& h.getDate().get(Calendar.YEAR) == this.getStartPeriod().get(Calendar.YEAR)) {
						cost += this.getEffectiveQuantity() * this.getContract().getRate() * h.getRate();
						found = true;
						break;
					}
				}
				if (!found && this.startPeriod.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cost += this.getEffectiveQuantity() * this.getContract().getRate()
							* this.getContract().getConvention().getSundayMajoration();
				}

			}
		} catch (Exception e) {

		}
		return cost;
	}

	/**
	 * the extra cost for the line
	 */
	public Float getExtraCost() {
		return this.getLineCost() - (this.getEffectiveQuantity() * this.getContract().getRate());
	}

	public Integer getSundayQuantity() {
		return null;
	}

	/**
	 * Pre remove method
	 */
	@PreRemove
	private void preRemove() {
		for (Booking b : bookings)
			b.setContractStatus(ContractStatus.NOT_MADE);
		this.getContract().removeLine(this);
		this.setContract(null);
	}

	public void addBooking(Booking booking) {
		this.getBookings().add(booking);

	}

	/**
	 * Method initializing the line
	 * 
	 * @param booking
	 *            - The initial booking
	 * @param cSetting
	 *            - The contractSetting
	 */
	public void initializeLine(Booking booking, EntertainmentContractSetting cSetting) {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.setTime(booking.getFrom().getTime());
		endDate.setTime(booking.getFrom().getTime());

		if (cSetting.getDayStart().after(cSetting.getDayEnd()))
			endDate.roll(Calendar.DATE, true);

		Calendar startTime = Calendar.getInstance();
		startTime.setTime(cSetting.getDayStart());
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(cSetting.getDayEnd());
		startDate.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
		startDate.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
		endDate.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
		endDate.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));

		this.setStartPeriod(startDate);
		this.setEndPeriod(endDate);
		this.addBooking(booking);
	}

	/**
	 * Remove a booking from the bookings collection
	 * 
	 * @param b
	 */
	public void removeBooking(Booking b) {
		this.bookings.remove(b);
		b.setContractStatus(ContractStatus.NOT_MADE);
	}

	/**
	 * @return the maunalUpdateOn
	 */
	public Boolean getMaunalUpdateOn() {
		return maunalUpdateOn;
	}

	/**
	 * @param maunalUpdateOn
	 *            the maunalUpdateOn to set
	 */
	public void setMaunalUpdateOn(Boolean maunalUpdateOn) {
		this.maunalUpdateOn = maunalUpdateOn;
	}

	/**
	 * @return the travelQuantity
	 */
	public Integer getTravelQuantity() {
		return travelQuantity;
	}

	/**
	 * @param travelQuantity
	 *            the travelQuantity to set
	 */
	public void setTravelQuantity(Integer travelQuantity) {
		this.travelQuantity = travelQuantity;
	}

	/**
	 * @return the holidayQuantity
	 */
	public Integer getHolidayQuantity() {
		return holidayQuantity;
	}

	/**
	 * @param holidayQuantity
	 *            the holidayQuantity to set
	 */
	public void setHolidayQuantity(Integer holidayQuantity) {
		this.holidayQuantity = holidayQuantity;
	}

}
