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
 * Describe a day unit
 * @author sraybaud- NovaRem
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class DayUnit extends RateUnit{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2171099627151780624L;
	
	
	/**
	 * Hour format
	 */
	protected static final DateFormat HF = new SimpleDateFormat("HH");
	
	/**
	 * Minute format
	 */
	protected static final DateFormat MF = new SimpleDateFormat("mm");
	
	
	/**
	 * Start time
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@XmlJavaTypeAdapter(TimeAdapter.class)
	private Date startTime;

	/**
	 * Start time
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@XmlJavaTypeAdapter(TimeAdapter.class)
	private Date duration;
	
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@XmlJavaTypeAdapter(TimeAdapter.class)
	private Date breakTime;

	/**
	 * @return the startTime
	 */
	protected Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the duration
	 */
	public Date getDuration() {
		return duration;
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
						next = this.getStartOfSlot((Calendar) line.getFrom().clone());
					}
				}else{
					Booking booking = new Booking();
					booking.setFrom(next);
					Calendar endOfSlot = this.getEndOfSlot(next);
					for( int i = 1; i < line.getQtyUsedPerOc() ; i++ ){
						endOfSlot.add(Calendar.DAY_OF_MONTH, 1);
					}
					booking.setTo(endOfSlot);
					/*System.out.println("start " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getFrom().getTime()));
					System.out.println("end " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getTo().getTime()));*/
					assignedOccurens+=1;
					bookings.add(booking);
					next = (Calendar) endOfSlot.clone();
					next = this.getStartOfSlot(next);
				
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
	
	/**
	 * Returns the next start of slot following the given date
	 * @param date
	 * @return the next available start slot
	 */
	@Override
	public Calendar getStartOfSlot(Calendar date){
		Calendar start = (Calendar) date.clone();
		Calendar time = Calendar.getInstance();
		time.setTime(startTime);
		start.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		start.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
		start.set(Calendar.SECOND, time.get(Calendar.SECOND));
		start.set(Calendar.MILLISECOND, 0);
		if(date.after(start)){
			start.add(Calendar.DATE, 1);
		}
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
		Calendar time = Calendar.getInstance();
		time.setTime(duration);
		end.add(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		end.add(Calendar.MINUTE, time.get(Calendar.MINUTE));
		end.add(Calendar.SECOND, time.get(Calendar.SECOND));
		end.set(Calendar.MILLISECOND, 0);
		return end;
	}
	
	/**
	 * Calculate the booking quantity taking care of resa dates
	 * @param booking the booking to quantify
	 */
	@Override
	public void quantify(Booking booking){
		if(booking.getFrom()!=null && booking.getTo()!=null){
			Calendar start = booking.getTo().before(booking.getFrom()) ? booking.getTo() : booking.getFrom();
			Calendar end = booking.getTo().before(booking.getFrom()) ? booking.getFrom() : booking.getTo();
			float quantity=0f;
			Calendar startOfSlot = (Calendar) start.clone();
			Calendar endOfSlot = this.getEndOfSlot(startOfSlot);
			while(!endOfSlot.after(end)){
				quantity++;
				if(end.equals(endOfSlot)){
					break;
				}
				endOfSlot.add(Calendar.DATE, 1);
			}
			if(!end.equals(endOfSlot)){
				quantity++;
				booking.setTo(endOfSlot);
			}
			booking.setQtyTotalUsed(quantity);
		}
	}

	public String getDurationWithoutBreakTime(){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(duration);
		c2.setTime(breakTime);
		return Integer.toString((c1.get(Calendar.HOUR) - c2.get(Calendar.HOUR)));
	}


}
