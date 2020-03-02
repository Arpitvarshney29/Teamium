/**
 * 
 */
package com.teamium.domain.prod.resources.functions.units;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.projects.BookingEvent;
import com.teamium.domain.prod.projects.planning.Event;


/**
 * Describe a generic rate unit
 * @author sraybaud- NovaRem
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class RateUnit extends AbstractXmlEntity{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 2413828260417264030L;
	
	@XmlAttribute
	private Integer calendarConstant;
	
	@Transient
	@XmlTransient
	private Booking line;
	
	/**
	 * Create bookings for the given line
	 * @param line the line to book
	 * @param unavailabilities events marking unavailability
	 * @return available bookings
	 *  throws TeamiumException line_disabled, line_start_missing, line_quantity_missing, line_booking_unable, line_booking_failed
	 */
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
		this.line = line;
		
		List<Booking> bookings = new ArrayList<Booking>();
		if(unavailabilities==null) unavailabilities = new ArrayList<Event>();
		TreeMap<Calendar,Boolean> ua = this.getAvailabilitySequence(line, unavailabilities);
		try{
			if(line.getDisabled()) throw new TeamiumException("line_disabled");
			if(line.getFrom()==null) throw new TeamiumException("line_start_missing");
			if(line.getQtyTotalUsed()==null || !(line.getQtyTotalUsed() > 0)) throw new TeamiumException("line_quantity_missing");
			Calendar next = (Calendar) this.getStartOfSlot((Calendar) line.getFrom().clone());
			float quantity = line.getOccurrenceCount();
			int assignedOccurence=0;
			while(quantity > assignedOccurence){
				next = this.getNextAvailableSlot(next, ua);
				if(next==null){
					if(assignedOccurence==0){
						throw new TeamiumException("line_booking_unable");
					}else{
						next = (Calendar) this.getStartOfSlot((Calendar) line.getFrom().clone());
					}
				}else{
					Booking booking = new Booking();
					booking.setOrigin(line.getId());
					booking.setFrom(next);
					Calendar endOfSlot = (Calendar) this.getEndOfSlot((Calendar) next.clone());
					for( int i = 1; i < line.getQtyUsedPerOc() ; i++ ){
						endOfSlot.add(Calendar.MILLISECOND, 1);
						endOfSlot = this.getEndOfSlot(endOfSlot);
					}
					booking.setTo(endOfSlot);
					/*System.out.println("start " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getFrom().getTime()));
					System.out.println("end " + DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(booking.getTo().getTime()));*/
					assignedOccurence+=1;
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
	
	/**
	 * Returns the next available start time from the given date taking account given unavailabilities
	 * @param date the date to test
	 * @param unavailability the availability sequence
	 * @return in fact, the given date in this class
	 */
	protected Calendar getNextAvailableSlot(Calendar date, TreeMap<Calendar,Boolean> availability){
		Calendar next = (Calendar) date.clone();
		boolean available=false;
		if(!availability.isEmpty()){
			Entry<Calendar,Boolean> floor = availability.floorEntry(next);
			if(floor == null){
				floor = availability.firstEntry();
			}
			Calendar nextNext = (Calendar) floor.getKey().clone();
			nextNext.add(Calendar.MILLISECOND, 1);
			Entry<Calendar,Boolean> ceiling = availability.ceilingEntry(nextNext);
			if((floor==null || floor.getValue()) && (ceiling==null || !ceiling.getValue())){
				Calendar endOfSlot = this.getEndOfSlot((Calendar) next.clone());
				available = (ceiling==null || !endOfSlot.after(ceiling.getKey()));

				/*DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				System.out.println("Next : " + df.format(next.getTime()));
				System.out.println("End : " + df.format(endOfSlot.getTime()));
				System.out.println("Floor : " + df.format(floor.getKey().getTime()));
				System.out.println("Ceiling : " + df.format(ceiling.getKey().getTime()));*/
			}else{
				available=false;
			}
			if(!available){
				if(ceiling!=null){
					next = this.getNextAvailableSlot(ceiling.getKey(), availability);
				}else{
					next=null;
				}
			}
		}
		return next;
	}
	
	/**
	 * Returns the next start of slot following the given date
	 * @param date
	 * @return the next available start slot
	 */
	protected Calendar getStartOfSlot(Calendar date){
		return date;
	}

	/**
	 * Returns the end of slot starting with the given date
	 * @param date the start of the slot
	 * @return in fact
	 */
	protected Calendar getEndOfSlot(Calendar date){
		if(line != null && line.getFrom() != null && line.getTo() != null){
			Long interval = line.getTo().getTimeInMillis() - line.getFrom().getTimeInMillis();
			Long dateL = date.getTimeInMillis() + interval;
			date.setTimeInMillis(dateL);
		}
		return date;
	}
	
	/**
	 * Returns the unavailability sequence
	 * @param unavailabilities the unavailabilities to sort
	 * @return the unavailability sequence
	 */
	protected TreeMap<Calendar,Boolean> getAvailabilitySequence(Booking line, List<Event> unavailabilities){
		TreeMap<Calendar,Boolean> map = new TreeMap<Calendar,Boolean>();
		Calendar start = (Calendar) line.getFrom().clone();
		if(start==null){
			start = Calendar.getInstance();
			start.set(Calendar.DATE, 1);
			start.set(Calendar.MONTH, 1);
			start.set(Calendar.YEAR, 1);
		}
		map.put(start, true);
		Calendar end = (Calendar) line.getTo().clone();
		if(end==null || end.before(start)){
			if(end == null){
				end = Calendar.getInstance();
			}
			end.set(Calendar.DATE, 31);
			end.set(Calendar.MONTH, 12);
			end.set(Calendar.YEAR, 3000);
		}

		map.put(end, false);
		if(unavailabilities !=null){
			for(Event event : unavailabilities){
				if(event.getFrom()==null){
					map.put(start, false);
				}
				else{
					map.put(event.getFrom(), false);
				}
				if(event.getTo()==null){
					map.put(end, true);
				}else{
					map.put(event.getTo(), true);
				}
			}
		}
		
		TreeMap<Calendar,Boolean> sequence = new TreeMap<Calendar,Boolean>();
		if(!map.isEmpty()){
			boolean state= !map.firstEntry().getValue();
			for(Calendar d : map.keySet()){
				if(map.get(d) != state){
					state = !state;
					sequence.put(d, state);
				}
			}
		}
		return sequence;
	}
	

	/**
	 * Calculate the booking quantity taking care of resa dates
	 * @param booking the booking to quantify
	 */
	public void quantify(Booking booking){}
	
	
	/**
	 * Calculate the dates of the given booking quantity taking care of the quantity set
	 * @param booking the booking to plan
	 */
	public void plan(Booking booking){
		if(booking.getQtyTotalUsed()!=null && booking.getQtyTotalUsed()>0){
			int plannedQuantity=0;
			Calendar start = booking.getFrom()!=null ? booking.getFrom() : Calendar.getInstance();
			start = this.getStartOfSlot(start);
			Calendar current = (Calendar) start.clone();
			Calendar end=null;
			while(plannedQuantity < booking.getQtyTotalUsed()){
				end = this.getEndOfSlot(current);
				plannedQuantity++;
				current = this.getStartOfSlot(end);			
			}
			booking.setFrom(start);
			booking.setTo(end);
		}
	}
	
	/**
	 * Split method for a specific unit
	 * @param occurenceSize for a booking
	 * @return list of generate bookings
	 * @throws TeamiumException booking_split_failed
	 */
	public List<BookingEvent> split(BookingEvent booking, Integer occurenceSize) throws TeamiumException {
		List<BookingEvent> bookings = new ArrayList<BookingEvent>();
		
		if(booking != null && occurenceSize != null){
			Float totalSize = 0f;
			Float maxSize = booking.getOrigin().getQtyUsedPerOc();
			Calendar date = booking.getFrom();
			while(totalSize < maxSize){
				Calendar end = (Calendar) date.clone();
				Float size = maxSize - totalSize > occurenceSize ? occurenceSize : maxSize - totalSize ;
				for(int i = 0 ; i < size; i++ ){
					if(booking.getOrigin().getUnitUsed() != null && booking.getOrigin().getUnitUsed().getCalendarConstant() != null){
						end.add(booking.getOrigin().getUnitUsed().getCalendarConstant(), 1);
					}else{
						if(this.line == null){
							this.line = booking.getOrigin();
						}
						end = ((RateUnit) this).getEndOfSlot(end);
					}
				}
				BookingEvent splitBook = null;
				try {
					if(totalSize + occurenceSize < maxSize){
						splitBook = (BookingEvent) booking.clone();
					}else{
						splitBook = booking;
					}
					splitBook.setFrom(date);
					splitBook.setTo(end);
					splitBook.getOrigin().setQtyUsedPerOc(size);
					splitBook.setResource(booking.getResource());
					bookings.add(splitBook);
					totalSize += size;
					date = (Calendar) end.clone();
					date.add(Calendar.MILLISECOND, 1);
				}catch(Exception e){
					throw new TeamiumException("booking_split_failed", e);
				}
			}
		}
		
		return bookings;
	}

	/**
	 * @return the calendarConstant
	 */
	public Integer getCalendarConstant() {
		return calendarConstant;
	}

	/**
	 * @param calendarConstant the calendarConstant to set
	 */
	public void setCalendarConstant(Integer calendarConstant) {
		this.calendarConstant = calendarConstant;
	}
	
}
