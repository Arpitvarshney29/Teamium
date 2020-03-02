package com.teamium.domain.prod.resources;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.contacts.Customer;

/**
 * Location entity
 * @author dolivier - NovaRem
 * @version 1.0
 */
@Entity
@Table(name="t_location")
public class Location extends AbstractEntity {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -8097514052478406389L;
	
	/**
	 * The resource ID  
	 */
	@Id
	@Column(name="c_idlocation", insertable=false, updatable=false)
	@TableGenerator( name = "idLocation_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "location_idlocation_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idLocation_seq")
	private Long id;
	
	/**
	 * Associate customer
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="c_idcustomer")
	private Customer customer;
	
	/**
	 * Building
	 */
	@Column(name="c_building")
	private String building;
	
	/**
	 * Floor
	 */
	@Column(name="c_floor")
	private String floor;
	
	/**
	 * Room
	 */
	@Column(name="c_room")	
	private String room;
	
	/**
	 * @since v6
	 * @author slopes
	 * The country
	 */
	@Column(name="c_country")
	private String country;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return associate customer
	 */
	public Customer getCustomer() {
		return customer;
	}
	
	/**
	 * @param customer associate customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	/**
	 * @return building
	 */
	public String getBuilding() {
		return building;
	}
	
	/**
	 * @param building
	 */
	public void setBuilding(String building) {
		this.building = building;
	}
	
	/**
	 * @return floor
	 */
	public String getFloor() {
		return floor;
	}
	
	/**
	 * @param floor
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}
	
	/**
	 * @return room
	 */
	public String getRoom() {
		return room;
	}
	
	/**
	 * @param room
	 */
	public void setRoom(String room) {
		this.room = room;
	}
	
	/**
	 * @return the country
	 */
	public Locale getCountry() {
		if(country==null) return null;
		else return new Locale(Locale.getDefault().getLanguage(), country);
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(Locale country) {
		if(country==null) this.country=null;
		else this.country = country.getCountry();
	}
}
