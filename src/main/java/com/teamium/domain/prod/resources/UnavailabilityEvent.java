/**
 * 
 */
package com.teamium.domain.prod.resources;

import java.text.DateFormat;
import java.util.Calendar;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.Theme;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.projects.planning.Event;

/**
 * Describe a resource periodic unavalaibility
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("unavailibility")
/**
 * Unavailability event
 * @author slopes
 * @since v5.7.0
 *
 */

@NamedQueries({
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourceFromDate, query="SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource = ?1 AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = UnavailabilityEvent ORDER BY pu.from, pu.to"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourceByPeriod, query="SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource = ?1 AND (pu.from IS NULL or pu.from <= ?3) AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = UnavailabilityEvent"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourcesByPeriod, query="SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource IN (?1) AND (pu.from IS NULL or pu.from <= ?3) AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = UnavailabilityEvent"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourceCoveringPeriod, query="SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource = ?1 AND (pu.from IS NULL or pu.from <= ?3) AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = UnavailabilityEvent"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourceOnlyFromDate, query="SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource = ?1 AND (pu.to IS NULL OR pu.to >= ?3) AND TYPE(pu) = UnavailabilityEvent"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourceOnlyUntilDate, query="SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource = ?1 AND (pu.from IS NULL OR pu.from <= ?3) AND TYPE(pu) = UnavailabilityEvent"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourceOnlyForEver, query="SELECT pu FROM UnavailabilityEvent pu WHERE pu.resource = ?1 AND pu.from IS NULL AND pu.to IS NULL AND TYPE(pu) = UnavailabilityEvent"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResource, query="SELECT pu FROM UnavailabilityEvent pu WHERE (pu.resource IS NULL OR pu.resource = ?1) AND TYPE(pu) = UnavailabilityEvent ORDER BY pu.from, pu.to"),
	@NamedQuery(name=UnavailabilityEvent.QUERY_findByResourceUntilDate, query="SELECT pu FROM UnavailabilityEvent pu WHERE (pu.resource IS NULL OR pu.resource = ?1) AND (pu.from IS NULL OR pu.from <= ?3) AND TYPE(pu) = UnavailabilityEvent"),
})
public class UnavailabilityEvent extends Event implements Cloneable{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -7278620256903188729L;

	/**
	 * DateFormat for toString
	 */
	private static final DateFormat DF = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
	/**
	 * Name of the query retrieving unavailable periods (including shared ones) for the given resources on the given period
	 * @param 1 the given resources
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResourcesByPeriod = "findUnavailabilityEventByResourcesByPeriod";

	/**
	 * Name of the query retrieving unavailable periods for the given resource (including shared ones) from the given date
	 * @param 1 the given resource
	 * @param 2 beginning date
	 */
	public static final String QUERY_findByResourceFromDate = "findUnavailabilityEventByResourceFromDate";

	/**
	 * Name of the query retrieving unavailable periods (excluding shared ones) for the given resource on the given period
	 * @param 1 group list
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByGroupByPeriod = "findUnavailabilityEventByGroupByPeriod";

	/**
	 * Name of the query retrieving unavailabilities of a given resource fully covering a given period
	 * @param 1 the given resource
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByGroupCoveringPeriod = "findUnavailabilityEventByGroupCoveringPeriod";

	/**
	 * Name of the query retrieving unavailabilities of a given resource fully covering a given period
	 * @param 1 the given resource
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResourceCoveringPeriod = "findUnavailabilityEventByResourcesCoveringPeriod";

	/**
	 * Name of the query retrieving unavailable periods for the given resource only from the given date
	 * @param 1 the given resource
	 * @param 2 from date
	 */
	public static final String QUERY_findByResourceOnlyFromDate = "findUnavailabilityEventByResourceOnlyFromDate";


	/**
	 * Name of the query retrieving unavailable periods for the given resource only until the given date
	 * @param 1 the given resource
	 * @param 2 until date
	 */
	public static final String QUERY_findByResourceOnlyUntilDate = "findUnavailabilityEventByResourceOnlyUntilDate";

	/**
	 * Name of the query retrieving unavailable periods for the given resource for ever
	 * @param 1 the given resource
	 */
	public static final String QUERY_findGroupByResourceOnlyForEver = "findUnavailabilityEventByResourceOnlyForEver";

	/**
	 * Name of the query retrieving unavailable periods for the given resource from the given date
	 * @param 1 the given resource
	 * @param 2 ending date
	 */
	public static final String QUERY_findByResourceUntilDate = "findUnavailabilityEventByResourceUntilDate";

	/**
	 * Name of the query retrieving unavailable periods for the given resource
	 * @param 1 the given resource
	 */
	public static final String QUERY_findByResource = "findUnavailabilityEventByResource";

	/**
	 * Name of the query retrieving unavailable periods (excluding shared ones) for the given resource on the given period
	 * @param 1 the given resource
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResourceByPeriod = "findUnavailabilityEventByResourceByPeriod";

	/**
	 * Name of the query retrieving unavailable periods for the given resource for ever
	 * @param 1 the given resource
	 */
	public static final String QUERY_findByResourceOnlyForEver = "findUnavailabilityEventByResourceOnlyForEver";
	
	/**
	 * Linked unavailability
	 */
	@ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="c_unavailability")
	private Unavailability unavailability;
	
	/**
	 * Returns the event's detail
	 * @return the detail
	 */
	public String getDetail(){
		if(this.getUnavailability() != null){
			return this.getUnavailability().getDetail();
		}else{
			return "";
		}
	}

	/**
	 * Sets the event detail
	 * @param title the detail to set
	 */
	public void setDetail(String detail){
		if(this.getUnavailability() != null){
			this.getUnavailability().setDetail(detail);
		}
	}

	/**
	 * Returns the event's title
	 * @return the title
	 */
	public String getTitle(){
		if(this.getUnavailability() != null){
			return this.getUnavailability().getType() + " " + this.getUnavailability().getDetail();
		}else{
			return "";
		}
	}

	/**
	 * Sets the event title
	 * @param title the title to set
	 */
	public void setTitle(String detail){
		if(this.getUnavailability() != null){
			this.getUnavailability().setDetail(detail);
		}
	}


	/**
	 * Returns a text description of the current object
	 * @return the string
	 */
	@Override
	public String toString(){
		return super.toString()+"[from="+(this.getFrom()==null? "null" : DF.format(this.getFrom().getTime()))+", to"+(this.getTo()==null? "null" : DF.format(this.getTo().getTime()))+"]"+(this.getTitle()!=null? this.getTitle() : "");
	}

	/**
	 * Is the event editable ?
	 */
	@Transient
	private Boolean editable;

	/**
	 * Returns the detail of the event
	 * @return the detailed information
	 */
	public String getDescription(){
		return this.getTitle();
	}


	/**
	 * Returns the edit status of the event
	 */
	public Boolean getEditable(){
		if(this.editable==null) this.editable=false;
		return this.editable;
	}

	/**
	 * Set the event editable
	 * 
	 */
	public void setEditable(Boolean bool){
		this.editable=bool;
	}


	/**
	 * Hide on large scale
	 * @return true if the event has to be hidden on large scale
	 */
	public boolean getHideOnLargeScale(){
		return false;
	}

	/**
	 * Returns the event's theme
	 * @return the theme
	 */
	public Theme getTheme(){
		return null;
	}

	/**
	 * Returns the event completion
	 * @return the completion percentage
	 */
	public Float getCompletion(){
		return null;
	}

	/**
	 * Returns the event status
	 * @return the status
	 */
	public XmlEntity getStatus(){
		return null;
	}

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		if(this.getUnavailability() != null){
			return this.getUnavailability().getType();
		}else{
			return null;
		}
	}

	/**
	 * @param type the type to set
	 */
	public void setType(XmlEntity type) {
		if(this.getUnavailability() != null){
			this.getUnavailability().setType(type);
		}
	}

	/**
	 * @return the unavailability
	 */
	public Unavailability getUnavailability() {
		return unavailability;
	}

	/**
	 * @param unavailability the unavailability to set
	 */
	public void setUnavailability(Unavailability unavailability) {
		this.unavailability = unavailability;
	}

	/**
	 * Clone the current object
	 * @return the clone
	 * @throws CloneNotSupportedException 
	 */
	@Override
	public UnavailabilityEvent clone() throws CloneNotSupportedException{
		UnavailabilityEvent clone = null;
		Long id = this.getId();
		this.setId(null);
		Resource<?> resource = this.getResource();
		clone = (UnavailabilityEvent) super.clone();
		try{
			this.setResource(null);
			clone = (UnavailabilityEvent) super.clone();
			clone.setResource(resource);
		}catch(CloneNotSupportedException e){
			throw e;
		}
		finally{
			this.setId(id);
			this.setResource(resource);
		}
		return clone;
	}
	
	/**
	 * Return false
	 * @return the superposition
	 */
	public Boolean getSuperposition() {
		return false;
	}

	/**
	 * DO NOTHING
	 * @param superposition the superposition to set
	 */
	public void setSuperposition(Boolean superposition) {
		
	}
	
	/**
	 * Copy the informations in the unavailability given in parameter in the current instance
	 * @param unavailability The unavailability
	 */
	public void copy(UnavailabilityEvent unavailability){
		//From
		if(unavailability.getFrom() != null){
			this.setFrom((Calendar)unavailability.getFrom().clone());
		}else{
			this.setFrom(null);
		}
		//To
		if(unavailability.getTo() != null){
			this.setTo((Calendar)unavailability.getTo().clone());
		}else{
			this.setTo(null);
		}
		//Unavailability
		if(unavailability.getUnavailability() != null){
			this.setUnavailability(unavailability.getUnavailability());
		}else{
			this.setUnavailability(null);
		}
	}
}
