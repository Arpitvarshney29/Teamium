/**
 * 
 */
package com.teamium.domain.prod.resources.functions.units;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.planning.Event;



/**
 * Describe a week unit
 * @author sraybaud- NovaRem
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class WeekUnit extends RateUnit{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 8285204200074005864L;

	/**
	 * First day of the week
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Integer firstDay = 1;
	
	/**
	 * Count of workable days for week
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private Integer dayCount =6;
	
	/**
	 * Create bookings for the given line
	 * @param line the line to book
	 * @param unavailabilities events marking unavailability
	 * @return available bookings
	 */
	@Override
	public List<Booking> createBookings(Booking line, List<Event> unavailabilities) throws TeamiumException{
		return super.createBookingsAlgorithm(line, unavailabilities);
	}
	
	/**
	 * Returns the next start of slot following the given date
	 * @param date
	 * @return the next available start slot
	 */
	@Override
	protected Calendar getStartOfSlot(Calendar date){
		Calendar start = (Calendar) date.clone();
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		return start;
	}

	/**
	 * Returns the end of slot starting with the given date
	 * @param date the start of the slot
	 * @return the end of the slot
	 */
	@Override
	protected Calendar getEndOfSlot(Calendar date){
		Calendar end = (Calendar) date.clone();
		end.set(Calendar.HOUR_OF_DAY, 23);
		end.set(Calendar.MINUTE, 59);
		end.set(Calendar.SECOND, 59);
		end.set(Calendar.MILLISECOND, 999);
		end.add(Calendar.DATE, dayCount);
		return end;
	}
	
	/**
	 * Calculate the booking quantity taking care of resa dates
	 * @param booking the booking to quantify
	 */
	@Override
	public void quantify(Booking booking){
		/*if(booking.getFrom()!=null && booking.getTo()!=null){
			Calendar start = booking.getTo().before(booking.getFrom()) ? booking.getTo() : booking.getFrom();
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);
			Calendar end = booking.getTo().before(booking.getFrom()) ? booking.getFrom() : booking.getTo();
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);
			end.set(Calendar.MILLISECOND, 999);
			int quantity=0;
			Calendar startOfSlot = (Calendar) start.clone();
			Calendar endOfSlot = this.getEndOfSlot(startOfSlot);
			while(endOfSlot.before(end) || endOfSlot.equals(end)){
				quantity++;
				startOfSlot.add(Calendar.DATE, 7);
				endOfSlot = this.getEndOfSlot(startOfSlot);
			}
			if(!end.equals(endOfSlot)){
				quantity++;
				booking.setTo(endOfSlot);
			}
			booking.setQuantity(quantity);
		}*/
		
		booking.setQtyTotalUsed(1f);
	}
	
	/**
	 * Create bookings for the given line
	 * @param line the line to book
	 * @param unavailabilities events marking unavailability
	 * @return available bookings
	 *  throws TeamiumException line_disabled, line_start_missing, line_quantity_missing, line_booking_unable, line_booking_failed
	 */
	protected List<Booking> createBookingsAlgorithm(Booking line, List<Event> unavailabilities) throws TeamiumException{
		List<Booking> bookings = new ArrayList<Booking>();
		if(unavailabilities==null) unavailabilities = new ArrayList<Event>();
		TreeMap<Calendar,Boolean> ua = this.getAvailabilitySequence(line, unavailabilities);
		try{
			if(line.getDisabled()) throw new TeamiumException("line_disabled");
			if(line.getFrom()==null) throw new TeamiumException("line_start_missing");
			if(line.getQtyTotalUsed()==null || !(line.getQtyTotalUsed() > 0)) throw new TeamiumException("line_quantity_missing");
			Calendar next = this.getStartOfSlot(line.getFrom());
			float quantity = line.getOccurrenceCount();
			int assignedOccurens=0;
			while(quantity > assignedOccurens){
				next = this.getNextAvailableSlot(next, ua);
				if(next==null){
					if(assignedOccurens==0){
						throw new TeamiumException("line_booking_unable");
					}else{
						next = this.getStartOfSlot(line.getFrom());
					}
				}else{
					Booking booking = new Booking();
					booking.setFrom(next);
					Calendar endOfSlot = this.getEndOfSlot(next);
					for( int i = 1; i < line.getQtyUsedPerOc() ; i++ ){
						endOfSlot.add(Calendar.MILLISECOND, 1);
						endOfSlot = this.getEndOfSlot(endOfSlot);
					}
					booking.setTo(endOfSlot);
					/*System.out.println("start " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getFrom().getTime()));
					System.out.println("end " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getTo().getTime()));*/
					//this.quantify(booking);
					assignedOccurens+=1;
					bookings.add(booking);
					next = (Calendar) endOfSlot.clone();
					next.add(Calendar.MILLISECOND, 1);
					
				}
			}
			return bookings;
		}
		catch(TeamiumException e){
			throw e;
		}
		catch(Exception e){
			throw new TeamiumException("line_booking_failed", e);
		}
	}

}
