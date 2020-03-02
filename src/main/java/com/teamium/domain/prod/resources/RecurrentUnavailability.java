/**
 * 
 */
package com.teamium.domain.prod.resources;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.prod.projects.planning.Event;

/**
 * Describes a resource unavailability based on recurrent pattern (days of week and time range in the day)
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("recurrent")
@NamedQueries({
	@NamedQuery(name=RecurrentUnavailability.QUERY_findByResources, query="SELECT ru FROM RecurrentUnavailability ru WHERE (ru.resource IS NULL OR ru.resource IN (?1))"),
	@NamedQuery(name=RecurrentUnavailability.QUERY_findByResourceFromDate, query="SELECT ru FROM RecurrentUnavailability ru WHERE ru.resource = ?1 AND (ru.to IS NULL OR ru.to >= ?2) ORDER BY ru.from, ru.to"),
	@NamedQuery(name=RecurrentUnavailability.QUERY_findByResourceByPeriod, query="SELECT ru FROM RecurrentUnavailability ru WHERE ru.resource = ?1 AND (ru.from IS NULL or ru.from <= ?3) AND (ru.to IS NULL OR ru.to >= ?2)"),
	@NamedQuery(name=RecurrentUnavailability.QUERY_findByResourceUntilDate, query="SELECT ru FROM RecurrentUnavailability ru WHERE (ru.resource IS NULL OR ru.resource = ?1) AND (ru.from IS NULL OR ru.from <= ?3)"),
	@NamedQuery(name=RecurrentUnavailability.QUERY_findByResource, query="SELECT ru FROM RecurrentUnavailability ru WHERE (ru.resource IS NULL OR ru.resource = ?1) ORDER BY ru.from, ru.to"),
})
public class RecurrentUnavailability extends Unavailability{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -7278620256903188729L;
	
	/**
	 * Name of the query retrieving unavailable periods for the given resources on the given period
	 * @param 1 the given resources
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResources = "findRecurrentUnavailabilityByResources";
	
	/**
	 * Name of the query retrieving unavailable periods for the given resource (including shared ones) from the given date
	 * @param 1 the given resource
	 * @param 2 beginning date
	 */
	public static final String QUERY_findByResourceFromDate = "recurrent_findPeriodicUnavailabilityByResourceFromDate";
	
	/**
	 * Name of the query retrieving unavailable periods (including shared ones) for the given resources on the given period
	 * @param 1 the given resources
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResourceByPeriod = "recurrent_findPeriodicUnavailabilityByResourceByPeriod";

	/**
	 * Name of the query retrieving unavailable periods for the given resource from the given date
	 * @param 1 the given resource
	 * @param 2 ending date
	 */
	public static final String QUERY_findByResourceUntilDate = "recurrent_findPeriodicUnavailabilityByResourceUntilDate";

	/**
	 * Name of the query retrieving unavailable periods for the given resource
	 * @param 1 the given resource
	 */
	public static final String QUERY_findByResource = "recurrent_findPeriodicUnavailabilityByResource";
	
	/**
	 * The token to use for separating persistent values
	 */
	private static final String TOKEN = "|";
	
	/**
	 * Time format for toString
	 */
	private static DateFormat TF = new SimpleDateFormat("HH:mm");

	/**
	 * Recurrent unavailable days
	 */
	@Column(name="c_recurrent_days")
	private String persistentDays;
	@Transient
	private Set<Integer> days;
	
	/**
	 * Min time of the unavailable period
	 */
	@Column(name="c_recurrent_time_min")
	@Temporal(TemporalType.TIME)
	private Date timeMin;
	
	/**
	 * Max time of the unavailable period
	 */
	@Column(name="c_recurrent_time_max")
	@Temporal(TemporalType.TIME)
	private Date timeMax;

	/**
	 * Returns the unavailable days in the week (1 for sunday, 7 for Saturday)
	 * @return the days
	 */
	public Set<Integer> getDays() {
		if(this.days==null){
			this.days = new TreeSet<Integer>();
			if(this.persistentDays!=null){
				StringTokenizer st = new StringTokenizer(this.persistentDays, TOKEN);
				while(st.hasMoreTokens()){
					Integer day = Integer.parseInt(st.nextToken());
					this.days.add(day);
				}
			}
		}
		return days;
	}

	/**
	 * Set the unavailable days in the week  (1 for sunday, 7 for Saturday)
	 * @param days the days to set
	 */
	public void setDays(Set<Integer> days) {
		this.days = days;
		if(this.days==null) this.persistentDays=null;
		else{
			days = new HashSet<Integer>(new ArrayList<Integer>(days));
			this.persistentDays="";
			Iterator<Integer> i = this.days.iterator();
			while(i.hasNext()){
				//??? There is a class cast Exception when i used directly the iterator ???
				//Integer day =  i.next();
				Object dayObject = i.next();
				Integer day = Integer.parseInt(dayObject.toString());
				
				if(day<1 || day>7) this.days.remove(day);
				else{
					//TODO slopes Pourquoi on utilise le next ???
					//this.persistentDays+=i.next().toString();
					this.persistentDays+=day.toString();
					if(i.hasNext()){
						this.persistentDays+=TOKEN;
					}
				}
			}
		}
	}

	/**
	 * @return the timeMin
	 */
	public Date getTimeMin() {
		return timeMin;
	}

	/**
	 * @param timeMin the timeMin to set
	 */
	public void setTimeMin(Date timeMin) {
		this.timeMin = timeMin;
	}

	/**
	 * @return the timeMax
	 */
	public Date getTimeMax() {
		return timeMax;
	}

	/**
	 * @param timeMax the timeMax to set
	 */
	public void setTimeMax(Date timeMax) {
		this.timeMax = timeMax;
	}
	
	/**
	 * Create events matching the current recurrent unavailability for the given perio
	 * @param start beginning of the period (date only)
	 * @param end end of the period (date only)
	 * @return the created events for the given period
	 */
	public List<UnavailabilityEvent> createEvents(Calendar startP, Calendar endP){
		List<UnavailabilityEvent> events=new ArrayList<UnavailabilityEvent>();
		Calendar start = (Calendar) startP.clone();
		Calendar end = (Calendar) endP.clone();
		
		// Watch if the start and end are possible.
		if(end.before(end)){
			Calendar tmp = (Calendar) end.clone();
			end = (Calendar) start.clone();
			start = (Calendar) tmp.clone();
		}
		// Watch if the period in param is out from the period recurrent.
		if(this.getTo() != null && startP.after(this.getTo())){
			return events;
		}
		if(this.getFrom() != null && endP.before(this.getFrom())){
			return events;
		}
		
		try{
			if(this.getFrom() != null && start.before(this.getFrom())) start = (Calendar)this.getFrom().clone();
			if(this.getTo() != null && end.after(this.getTo())) end = (Calendar)this.getTo().clone();
			if(start==null) start = end;
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);
			if(end==null || end.before(start)) end = start;
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);
			end.set(Calendar.MILLISECOND, 999);
			UnavailabilityEvent event=null;
			Calendar timeMin = Calendar.getInstance();
			if(this.timeMin!=null){
				timeMin.setTime(this.timeMin);
			}else{
				timeMin.set(Calendar.HOUR_OF_DAY, 0);
				timeMin.set(Calendar.MINUTE, 0);
				timeMin.set(Calendar.SECOND, 0);
				timeMin.set(Calendar.MILLISECOND, 0);
			}
			Calendar timeMax = Calendar.getInstance();
			if(this.timeMax!=null){
				timeMax.setTime(this.timeMax);
			}else{
				timeMax.set(Calendar.HOUR_OF_DAY, 23);
				timeMax.set(Calendar.MINUTE, 59);
				timeMax.set(Calendar.SECOND, 59);
				timeMax.set(Calendar.MILLISECOND, 999);
			}
			Calendar current = (Calendar) start.clone();
			current.set(Calendar.HOUR_OF_DAY, timeMin.get(Calendar.HOUR_OF_DAY) );
			current.set(Calendar.MINUTE, timeMin.get(Calendar.MINUTE));
			current.set(Calendar.SECOND, timeMin.get(Calendar.SECOND));
			current.set(Calendar.MILLISECOND, 0);
			while(current.before(end)){
				if((this.getDays().isEmpty() && (this.timeMin != null || this.timeMax != null)) || this.getDays().contains(current.get(Calendar.DAY_OF_WEEK))){
					if(event==null){
						//If event is null, a new event is set
						event = new UnavailabilityEvent();
						event.setFrom((Calendar) current.clone());
						Calendar currentEnd = (Calendar) current.clone();
						currentEnd.set(Calendar.HOUR_OF_DAY, timeMax.get(Calendar.HOUR_OF_DAY) );
						currentEnd.set(Calendar.MINUTE, timeMax.get(Calendar.MINUTE));
						currentEnd.set(Calendar.SECOND, timeMax.get(Calendar.SECOND));
						currentEnd.set(Calendar.MILLISECOND, 999);
						event.setTo(currentEnd);
						events.add(event);
						//If time range is set, only set one event by day
						if(this.timeMin!=null || this.timeMax!=null) event=null;
					}else{
						//If event is not null, that means that we have to merge two consecutive days : only end of event is set
						Calendar currentEnd = (Calendar) current.clone();
						currentEnd.set(Calendar.HOUR_OF_DAY, timeMax.get(Calendar.HOUR_OF_DAY) );
						currentEnd.set(Calendar.MINUTE, timeMax.get(Calendar.MINUTE));
						currentEnd.set(Calendar.SECOND, timeMax.get(Calendar.SECOND));
						currentEnd.set(Calendar.MILLISECOND, 999);
						event.setTo(currentEnd);						
					}
				}else{
					//If no consecutive day, event variable is reset to set a new event
					event=null;
				}
				current.add(Calendar.DAY_OF_MONTH, 1);
			}
		}catch(NullPointerException e){
			events = new ArrayList<UnavailabilityEvent>(0);
		}
		return events;
	}
	
	/**
	 * Clone the current object
	 * @return the clone
	 * @throws CloneNotSupportedException 
	 */
	@Override
	public RecurrentUnavailability clone() throws CloneNotSupportedException{
		RecurrentUnavailability clone = null;
		Set<Integer> days = this.getDays();
		this.days=null;
		try{
			clone = (RecurrentUnavailability) super.clone();
			clone.days = new TreeSet<Integer>();
			for(Integer day : days){
				clone.days.add(day);
			}
			clone.setDays(clone.days);
		}catch(CloneNotSupportedException e){
			throw e;
		}finally{
			this.days=days;
		}
		return clone;
	}
	
	/**
	 * Returns a text description of the current object
	 * @return the string
	 */
	@Override
	public String toString(){
		return super.toString()+"[days="+this.getDays()+(this.timeMin!=null?", timeStart="+TF.format(this.timeMin) : "")+(this.timeMax!=null?", timeMax="+TF.format(this.timeMax) : "")+"]";
	}
	
	/**
	 * Copy the informations in the unavailability given in parameter in the current instance
	 * @param unavailability The unavailability
	 */
	public void copy(UnavailabilityEvent unavailability){
		/* TODO REWORK
		super.copy(unavailability);
		if(unavailability instanceof RecurrentUnavailability){
			RecurrentUnavailability ru = (RecurrentUnavailability) unavailability;
			this.persistentDays = new String(ru.persistentDays);
			this.setTimeMin((Date)ru.getTimeMin().clone());
			this.setTimeMax((Date)ru.getTimeMax().clone());
		}*/
	}


}
