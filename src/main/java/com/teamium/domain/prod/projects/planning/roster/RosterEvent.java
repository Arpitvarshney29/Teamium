package com.teamium.domain.prod.projects.planning.roster;

import java.util.Calendar;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.functions.Function;

/**
 * @author Nishant
 *
 */
@Entity
@Table(name = "t_roster_event")
@NamedQueries({
		@NamedQuery(name = RosterEvent.ROSTER_findAll, query = "SELECT rosterEvent FROM RosterEvent rosterEvent"),
		@NamedQuery(name = RosterEvent.ROSTER_findByFunctions, query = "SELECT rosterEvent FROM RosterEvent rosterEvent WHERE rosterEvent.function IN(?1)"),
		@NamedQuery(name = RosterEvent.ROSTER_findByDateBetween, query = "SELECT rosterEvent FROM RosterEvent rosterEvent WHERE rosterEvent.from>=?1 and rosterEvent.to<=?2"),
		@NamedQuery(name = RosterEvent.ROSTER_findByFuctionsAndDateBetween, query = "SELECT rosterEvent FROM RosterEvent rosterEvent WHERE (rosterEvent.from>=?1 and rosterEvent.to<=?2) and rosterEvent.function IN(?3)"),
		@NamedQuery(name = RosterEvent.ROSTER_findByDateLinked, query = "SELECT rosterEvent FROM RosterEvent rosterEvent WHERE rosterEvent.from<?2 and rosterEvent.to>=?1") })


public class RosterEvent extends AbstractEntity {

	private static final long serialVersionUID = -4750463996207881198L;
	/**
	 * Name of the query returning all roster events.
	 */
	public static final String ROSTER_findAll = "findAll";

	/**
	 * Name of the query returning roster events based on given function.
	 * 
	 * @param functions
	 *            the list of functions
	 */
	public static final String ROSTER_findByFunctions = "findByFunctions";

	/**
	 * Name of the query returning roster events based on given dates.
	 * 
	 * @param from
	 *            the start date
	 * @param to
	 *            the end
	 * 
	 */
	public static final String ROSTER_findByDateBetween = "findByDateBetween";

	/**
	 * Name of the query returning roster events based on given dates.
	 * 
	 * To get roster events that lies between the given date and also contains
	 * events that are crossing the end date.
	 * 
	 * @param from
	 *            the start date
	 * @param to
	 *            the end
	 * 
	 */
	public static final String ROSTER_findByDateLinked = "findByDateLinked";

	/**
	 * Name of the query returning roster events based on given dates and functions.
	 * 
	 * 
	 * @param from
	 *            the start date
	 * @param to
	 *            the end
	 * @param functions
	 *            the list of functions
	 * 
	 */

	public static final String ROSTER_findByFuctionsAndDateBetween = "findByFunctionsAndDateBetween";

	@Id
	@Column(name = "c_idrosterevent", insertable = false, updatable = false)
	@TableGenerator(name = "idRosterEvent_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "roster_event_idevent_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idRosterEvent_seq")
	private Long id;

	@Column(name = "c_title")
	String title;

	/**
	 * Estimated starting date
	 */
	@Column(name = "c_from")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar from;

	/**
	 * Estimated ending date
	 */
	@Column(name = "c_to")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar to;

	@Column(name = "c_function_type")
	private String functionType;

	@Column(name = "c_quantity")
	private Float quantity;

	@ManyToOne
	@JoinColumn(name = "c_idfunction")
	private Function function;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "t_roster_resource", joinColumns = @JoinColumn(name = "c_id"), inverseJoinColumns = @JoinColumn(name = "c_idresource"))
	private List<Resource<?>> resources;

	public RosterEvent() {

	}

	public RosterEvent(String title, Calendar from, Calendar to, String functionType, Float quantity,
			List<Resource<?>> resources, Function function) {
		this.title = title;
		this.from = from;
		this.to = to;
		this.functionType = functionType;
		this.quantity = quantity;
		this.resources = resources;
		this.function = function;
	}

	public RosterEvent(String title, Calendar from, Calendar to, String functionType, Float quantity,
			Function function) {
		this.title = title;
		this.from = from;
		this.to = to;
		this.functionType = functionType;
		this.quantity = quantity;
		this.function = function;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
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
	 * @param from
	 *            the from to set
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
	 * @param to
	 *            the to to set
	 */
	public void setTo(Calendar to) {
		this.to = to;
	}

	/**
	 * @return the functionType
	 */
	public String getFunctionType() {
		return functionType;
	}

	/**
	 * @param functionType
	 *            the functionType to set
	 */
	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}

	/**
	 * @return the quantity
	 */
	public Float getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the resources
	 */
	public List<Resource<?>> getResources() {
		return resources;
	}

	/**
	 * @param resources
	 *            the resources to set
	 */
	public void setResources(List<Resource<?>> resources) {
		this.resources = resources;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the function
	 */
	public Function getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(Function function) {
		this.function = function;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RosterEvent [title=" + title + ", from=" + from + ", to=" + to + ", resources=" + resources + "]";
	}
	
	/**
	 * To perform clone
	 * 
	 * @param event
	 * 
	 * @return clone event
	 */
	public RosterEvent clone(RosterEvent event) {
		RosterEvent clone = new RosterEvent();
		clone.setFunction(event.getFunction());
		clone.setFunctionType(event.getFunctionType());
		clone.setQuantity(event.getQuantity());
		clone.setResources(event.getResources());
		clone.setTitle(event.getTitle());
		return clone;
	}

}
