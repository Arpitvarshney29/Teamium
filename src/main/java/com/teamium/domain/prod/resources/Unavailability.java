package com.teamium.domain.prod.resources;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
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

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;

/**
 * Describes an unavailability
 * @author dolivier - NovaRem
 * @since v7
 * @author sraybaud @version TEAM-18
 */
@Entity
@Table(name="t_unavailability")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="c_discriminator")
@DiscriminatorValue(Unavailability.DISCRIMINATOR)
@NamedQueries({
	@NamedQuery(name=Unavailability.QUERY_findByResourceFromDate, query="SELECT pu FROM Unavailability pu WHERE pu.resource = ?1 AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = Unavailability ORDER BY pu.from, pu.to"),
	@NamedQuery(name=Unavailability.QUERY_findByResourceByPeriod, query="SELECT pu FROM Unavailability pu WHERE pu.resource = ?1 AND (pu.from IS NULL or pu.from <= ?3) AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = Unavailability"),
	@NamedQuery(name=Unavailability.QUERY_findByResourcesByPeriod, query="SELECT pu FROM Unavailability pu WHERE pu.resource IN (?1) AND (pu.from IS NULL or pu.from <= ?3) AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = Unavailability"),
	@NamedQuery(name=Unavailability.QUERY_findByResourceCoveringPeriod, query="SELECT pu FROM Unavailability pu WHERE pu.resource = ?1 AND (pu.from IS NULL or pu.from <= ?3) AND (pu.to IS NULL OR pu.to >= ?2) AND TYPE(pu) = Unavailability"),
	@NamedQuery(name=Unavailability.QUERY_findByResourceOnlyFromDate, query="SELECT pu FROM Unavailability pu WHERE pu.resource = ?1 AND (pu.to IS NULL OR pu.to >= ?3) AND TYPE(pu) = Unavailability"),
	@NamedQuery(name=Unavailability.QUERY_findByResourceOnlyUntilDate, query="SELECT pu FROM Unavailability pu WHERE pu.resource = ?1 AND (pu.from IS NULL OR pu.from <= ?3) AND TYPE(pu) = Unavailability"),
	@NamedQuery(name=Unavailability.QUERY_findByResourceOnlyForEver, query="SELECT pu FROM Unavailability pu WHERE pu.resource = ?1 AND pu.from IS NULL AND pu.to IS NULL AND TYPE(pu) = Unavailability"),
	@NamedQuery(name=Unavailability.QUERY_findByResource, query="SELECT pu FROM Unavailability pu WHERE (pu.resource IS NULL OR pu.resource = ?1) AND TYPE(pu) = Unavailability ORDER BY pu.from, pu.to"),
	@NamedQuery(name=Unavailability.QUERY_findByResourceUntilDate, query="SELECT pu FROM Unavailability pu WHERE (pu.resource IS NULL OR pu.resource = ?1) AND (pu.from IS NULL OR pu.from <= ?3) AND TYPE(pu) = Unavailability"),
	@NamedQuery(name=Unavailability.QUERY_findLinkedByUnavailability, query="SELECT pu FROM Unavailability pu WHERE pu.reference = ?1 AND pu.resource IN(?2) AND TYPE(pu) = Unavailability"),
})
public class Unavailability extends AbstractEntity implements Cloneable, Comparable<Unavailability> {
	
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 6155317074390990260L;
	
	/**
	 * DateFormat for toString
	 */
	private static final DateFormat DF = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT);
	
	/**
	 * Discriminator
	 */
	public static final String DISCRIMINATOR = "unavailability";
	
	/**
	 * Name of the query retrieving unavailable periods (including shared ones) for the given resources on the given period
	 * @param 1 the given resources
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResourcesByPeriod = "findUnavailabilityByResourcesByPeriod";

	/**
	 * Name of the query retrieving unavailable periods for the given resource (including shared ones) from the given date
	 * @param 1 the given resource
	 * @param 2 beginning date
	 */
	public static final String QUERY_findByResourceFromDate = "findUnavailabilityByResourceFromDate";

	/**
	 * Name of the query retrieving unavailable periods (excluding shared ones) for the given resource on the given period
	 * @param 1 group list
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByGroupByPeriod = "findUnavailabilityByGroupByPeriod";

	/**
	 * Name of the query retrieving unavailabilities of a given resource fully covering a given period
	 * @param 1 the given resource
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByGroupCoveringPeriod = "findUnavailabilityByGroupCoveringPeriod";

	/**
	 * Name of the query retrieving unavailabilities of a given resource fully covering a given period
	 * @param 1 the given resource
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResourceCoveringPeriod = "findUnavailabilityByResourcesCoveringPeriod";

	/**
	 * Name of the query retrieving unavailable periods for the given resource only from the given date
	 * @param 1 the given resource
	 * @param 2 from date
	 */
	public static final String QUERY_findByResourceOnlyFromDate = "findUnavailabilityByResourceOnlyFromDate";


	/**
	 * Name of the query retrieving unavailable periods for the given resource only until the given date
	 * @param 1 the given resource
	 * @param 2 until date
	 */
	public static final String QUERY_findByResourceOnlyUntilDate = "findUnavailabilityByResourceOnlyUntilDate";

	/**
	 * Name of the query retrieving unavailable periods for the given resource for ever
	 * @param 1 the given resource
	 */
	public static final String QUERY_findGroupByResourceOnlyForEver = "findUnavailabilityByResourceOnlyForEver";

	/**
	 * Name of the query retrieving unavailable periods for the given resource from the given date
	 * @param 1 the given resource
	 * @param 2 ending date
	 */
	public static final String QUERY_findByResourceUntilDate = "findUnavailabilityByResourceUntilDate";

	/**
	 * Name of the query retrieving unavailable periods for the given resource
	 * @param 1 the given resource
	 */
	public static final String QUERY_findByResource = "findUnavailabilityByResource";

	/**
	 * Name of the query retrieving unavailable periods (excluding shared ones) for the given resource on the given period
	 * @param 1 the given resource
	 * @param 2 beginning of period
	 * @param 3 end of period
	 */
	public static final String QUERY_findByResourceByPeriod = "findUnavailabilityByResourceByPeriod";

	/**
	 * Name of the query retrieving unavailable periods for the given resource for ever
	 * @param 1 the given resource
	 */
	public static final String QUERY_findByResourceOnlyForEver = "findUnavailabilityByResourceOnlyForEver";
	
	/**
	 * Name of the query retrieving the unavailabilities matching the unavailability given in parameter as reference and resource in the resources given parameter
	 * @param 1 The unavailability
	 * @param 2 The resources
	 */
	public static final String QUERY_findLinkedByUnavailability = "findUnavailabilityReferencedByUnavailability";

	@Id
	@Column(name="c_idunavailability")
	@TableGenerator( name = "idUnavailability_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "resource_unavailability_idunavailability_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idUnavailability_seq")
	private Long id;

	/**
	 * Estimated starting date
	 */
	@Column(name="c_periodic_from")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar from;
	
	/**
	 * Estimated ending date
	 */
	@Column(name="c_periodic_to")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar to;
	
	/**
	 * The resource concerned by the unavailability
	 */
	@ManyToOne
	@JoinColumn(name="c_idresource")
	private Resource<?> resource;
	
	/**
	 * Unavailability title
	 */
	@Column(name="c_detail")
	private String detail;
	
	/**
	 * Type of unavailability
	 * @see com.teamium.domain.prod.resources.staff.UnavailabilityTypeStaff
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_type"))
	private XmlEntity type;
	
	/**
	 * Reference the base unavailability
	 */
	@ManyToOne
	@JoinColumn(name="c_reference")
	private Unavailability reference;
	
	/**
	 * Return the id
	 */
	@Override
	public Long getId() {
		return this.id;
	}
	
	/**
	 * Update the id
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the from
	 */
	public Calendar getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Calendar from) {
		this.from = from;
	}

	/**
	 * @return the to
	 */
	public Calendar getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @return the resource
	 */
	public Resource<?> getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource<?> resource) {
		this.resource = resource;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
	}

	/**
	 * @return the reference
	 */
	public Unavailability getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(Unavailability reference) {
		this.reference = reference;
	}
	
	/**
	 * Create event(s) for the given unavailability
	 */
	public static List<UnavailabilityEvent> createEvent(Unavailability un){
		List<UnavailabilityEvent> events = new ArrayList<UnavailabilityEvent>();
		
		if(un != null){
			if(un instanceof RecurrentUnavailability){
				if(un.getTo() != null && un.getFrom() != null){
					for(UnavailabilityEvent ev : ((RecurrentUnavailability) un).createEvents(un.from, un.to)){
						ev.setDetail(un.getDetail());
						ev.setType(un.getType());
						ev.setUnavailability(un);
						ev.setResource(un.getResource());
						events.add(ev);
					}
				}
			}else{
				UnavailabilityEvent event = new UnavailabilityEvent();
				event.setResource(un.getResource());
				event.setFrom(un.getFrom());
				event.setTo(un.getTo());
				event.setDetail(un.getDetail());
				event.setType(un.getType());
				event.setUnavailability(un);
				events.add(event);
			}
		}
		
		return events;
	}

	/**
	 * Copy the informations in the unavailability given in parameter in the current instance
	 * @param unavailability The unavailability
	 */
	public void copy(Unavailability unavailability){
		//Detail
		if(unavailability.getDetail() != null){
			this.setDetail(new String(unavailability.getDetail()));
		}else{
			this.setDetail(null);
		}
		//Type
		if(unavailability.getType() != null){
			this.setType(unavailability.getType());
		}else{
			this.setType(null);
		}
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
	}
	
	/**
	 * Clone the current object
	 * @return the clone
	 * @throws CloneNotSupportedException 
	 */
	@Override
	public Unavailability clone() throws CloneNotSupportedException{
		Unavailability clone = null;
		Long id = this.getId();
		this.setId(null);
		Resource<?> resource = this.getResource();
		try{
			this.setResource(null);
			clone = (Unavailability) super.clone();
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
	 * Returns a text description of the current object
	 * @return the string
	 */
	@Override
	public String toString(){
		return super.toString()+"[from="+(this.getFrom()==null? "null" : DF.format(this.getFrom().getTime()))+", to"+(this.getTo()==null? "null" : DF.format(this.getTo().getTime()))+"]"+(this.getDetail()!=null? this.getDetail() : "");
	}

	@Override
	public int compareTo(Unavailability o) {
		int compare = 0;
		
		/**
		 * Resource
		 */
		if(compare == 0){
			if(this.resource != null){
				if(o.resource == null){
					return 1;
				}else{
					compare = this.resource.compareTo(o.resource);
				}
			}else{
				if(o.resource != null){
					return -1;
				}
			}
		}
		
		/**
		 * From
		 */
		if(compare == 0){
			if(this.from != null){
				if(o.from == null){
					return 1;
				}else{
					compare = this.from.compareTo(o.from);
				}
			}else{
				if(o.from != null){
					return -1;
				}
			}
		}
		
		/**
		 * To
		 */
		if(compare == 0){
			if(this.to != null){
				if(o.to == null){
					return 1;
				}else{
					compare = this.to.compareTo(o.to);
				}
			}else{
				if(o.to != null){
					return -1;
				}
			}
		}
		
		return compare;
	}
	
}
