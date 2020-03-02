/**
 * 
 */
package com.teamium.domain;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * Describe a postal address
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name="t_address")
public class Address extends AbstractEntity implements Cloneable{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -692216947628618702L;

	/**
	 * Address ID
	 */
	@Id
	@Column(name="c_idaddress", insertable=false, updatable=false)
	@TableGenerator( name = "idAddress_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "address_idaddress_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idAddress_seq")
	private Long id;
	
	/**
	 * First line address
	 */
	@Column(name="c_line1")
	private String line1;
	
	/**
	 * Second line address
	 */
	@Column(name="c_line2")
	private String line2;
	
	/**
	 * ZIP Code
	 */
	@Column(name="c_zipcode")
	private String zipcode;
	
	/**
	 * City
	 */
	@Column(name="c_city")
	private String city;
	
	/**
	 * Region/State
	 */
	@Column(name="c_state")
	private String state;
	
	/**
	 * Code ISO du pays
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
	 * @return the line1
	 */
	public String getLine1() {
		if (line1 == null) line1 = new String();
		return line1;
	}

	/**
	 * @param line1 the line1 to set
	 */
	public void setLine1(String line1) {
		this.line1 = line1;
	}

	/**
	 * @return the line2
	 */
	public String getLine2() {
		if(line2 == null) line2 = new String();
		return line2;
	}

	/**
	 * @param line2 the line2 to set
	 */
	public void setLine2(String line2) {
		this.line2 = line2;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		if(zipcode == null) zipcode = new String();
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		if(city == null) city = new String();
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country=country;
	}
	
	/**
	 * Clone the current object
	 * @throws CloneNotSupportedException
	 * @return the clone (transient)
	 */
	@Override
	public Address clone() throws CloneNotSupportedException{
		Address clone = (Address) super.clone();
		clone.setId(null);
		return clone;
	}
}
