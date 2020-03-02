/**
 * 
 */
package com.teamium.domain.prod.projects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.teamium.domain.prod.resources.functions.ProcessFunction;


/**
 * @author slopes
 * The booking combination to use in process booking process
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name=ProcessBookingCombination.QUERY_findByBooking, query="SELECT pbc FROM ProcessBookingCombination pbc WHERE ?1 MEMBER OF pbc.bookings"),
	@NamedQuery(name=ProcessBookingCombination.QUERY_findByReference, query="SELECT pbc FROM ProcessBookingCombination pbc WHERE pbc.reference = ?1"),
})
@DiscriminatorValue("process")
public class ProcessBookingCombination extends BookingCombination<BookingEvent> {
	/**
	 * Find the booking combination matching the reference given in parameter
	 * @param 1 The reference
	 */
	public static final String QUERY_findByReference = "find_process_booking_combination_by_reference";
	
	/**
	 * Find the process booking combination matching booking given in parameter
	 * @param 1 The booking
	 */
	public static final String QUERY_findByBooking = "find_process_booking_combination_by_booking";
	/**
	 * The generated serial UID
	 */
	private static final long serialVersionUID = 2943358495498260459L;
	/**
	 * The reference
	 */
	@ManyToOne
	@JoinColumn(name="c_booking_reference")
	private BookingEvent reference;

	/**
	 * Return the booking reference
	 */
	@Override
	public BookingEvent getReference() {
		return this.reference;
	}

	/**
	 * Setting the reference <strong>if the given reference has as function a process function</strong>
	 * @param reference the reference to set
	 */
	@Override
	public void setReference(BookingEvent reference) {
		if(reference != null && reference.getOrigin().getFunction() != null && reference.getOrigin().getFunction() instanceof ProcessFunction){
			this.reference = reference;	
		}
	}

}
