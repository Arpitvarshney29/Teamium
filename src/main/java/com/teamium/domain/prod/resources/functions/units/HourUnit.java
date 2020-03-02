/**
 * 
 */
package com.teamium.domain.prod.resources.functions.units;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.utils.TimeAdapter;



/**
 * Describe a company type of profile
 * @author sraybaud- NovaRem
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class HourUnit extends RateUnit{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -1169904265516692292L;

	/**
	 * Break time
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@XmlJavaTypeAdapter(TimeAdapter.class)
	private Date breakTime;
	
	/**
	 * Start time
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@XmlJavaTypeAdapter(TimeAdapter.class)
	private Date duration;
	/**
	 * @return the breakTime
	 */
	protected Date getBreakTime() {
		return breakTime;
	}

	/**
	 * Returns the end of slot starting with the given date
	 * @param date the start of the slot
	 * @return the end of the slot
	 */
	protected Calendar getEndOfSlot(Calendar date, Integer quantity){
		Calendar end = (Calendar) date.clone();
		
		int durationQuantity = 0;
		durationQuantity = quantity;
		end.add(Calendar.HOUR_OF_DAY, durationQuantity);
		
		return end;
	}
	
	/**
	 * Create bookings for the given line
	 * @param line the line to book
	 * @param unavailabilities events marking unavailability
	 * @return available bookings
	 */
	@Override
	public List<Booking> createBookings(Booking line, List<Event> unavailabilities) throws TeamiumException{
		return this.createBookingsAlgorithm(line, unavailabilities);
	}
	
	/**
	 * Calculate the booking quantity taking care of resa dates
	 * @param booking the booking to quantify
	 */
	@Override
	public void quantify(Booking booking){
		/*if(booking.getFrom()!=null && booking.getTo()!=null){
			Calendar start = booking.getTo().before(booking.getFrom()) ? booking.getTo() : booking.getFrom();
			Calendar end = booking.getTo().before(booking.getFrom()) ? booking.getFrom() : booking.getTo();
			Float quantity=0f;
			Calendar startOfSlot = (Calendar) start.clone();
			Calendar endOfSlot = this.getEndOfSlot(startOfSlot);
			Float breakHours = Float.parseFloat(HF.format(this.getBreakTime()));
			Float breakMinutes = Float.parseFloat(MF.format(this.getBreakTime()));
			long time = end.getTimeInMillis() - start.getTimeInMillis();
			quantity = Float.valueOf(time)/(1000*60*60);
			//if(!end.before(endOfSlot)){
			//	quantity=quantity-(breakHours+(breakMinutes/60));				
			//}
			Integer quantityInHours = quantity.intValue();
			if(quantityInHours.floatValue() < quantity) quantityInHours++;			
			booking.setQuantity(quantityInHours);			
		}*/
	}
	
	/**
	 * @return the duration
	 */
	public Date getDuration() {
		return duration;
	}
	
	/**
	 * Calculate the dates of the given booking quantity taking care of the quantity set
	 * @param booking the booking to plan
	 */
	@Override
	public void plan(Booking booking){
		if(booking.getQtyTotalUsed()!=null && booking.getQtyTotalUsed()>0){
			float plannedQuantity=0;
			float quantity = booking.getQtyTotalUsed().floatValue()*60f;
			Calendar start = booking.getFrom()!=null ? booking.getFrom() : Calendar.getInstance();
			start = this.getStartOfSlot(start);
			Calendar current = (Calendar) start.clone();
			Calendar end=null;
			while(plannedQuantity < quantity){
				end = this.getEndOfSlot(current, booking.getQtyTotalUsed().intValue());
				plannedQuantity+=(this.getDuration().getTime()/1000/60);
				current = this.getStartOfSlot(end);			
			}
			booking.setFrom(start);
			booking.setTo(end);
		}
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
			int quantity = line.getOccurrenceCount().intValue();
			int assignedQuantity=0;
			while(quantity > assignedQuantity){
				next = this.getNextAvailableSlot(next, ua);
				if(next==null){
					if(assignedQuantity==0){
						throw new TeamiumException("line_booking_unable");
					}else{
						next = this.getStartOfSlot(line.getFrom());
					}
				}else{
					Booking booking = new Booking();
					booking.setFrom(next);
					booking.setTo((Calendar) booking.getFrom().clone());
					booking.getTo().add(Calendar.HOUR_OF_DAY, line.getQtyUsedPerOc().intValue());
					/*System.out.println("start " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getFrom().getTime()));
					System.out.println("end " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getTo().getTime()));*/
					
					assignedQuantity+=1;
					bookings.add(booking);
					next = (Calendar) booking.getTo().clone();
				
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
