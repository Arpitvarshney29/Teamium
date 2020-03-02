package com.teamium.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Address;
import com.teamium.utils.CountryUtil;

/**
 * 
 * Wrapper class for Address
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AddressDTO extends AbstractDTO {

	private Long id;
	private String line1;
	private String line2;
	private String zipcode;
	private String city;
	private String state;
	private String country;

	public AddressDTO() {
		super();
	}

	public AddressDTO(Long id, String line1, String line2, String zipcode, String city, String state, String country) {
		super();
		this.id = id;
		this.line1 = line1;
		this.line2 = line2;
		this.zipcode = zipcode;
		this.city = city;
		this.state = state;
		if (country != null) {
			this.country = country;
		}

	}

	public AddressDTO(Address address) {
		this.id = address.getId();
		this.line1 = address.getLine1();
		this.line2 = address.getLine2();
		this.zipcode = address.getZipcode();
		this.state = address.getState();
		this.country = address.getCountry();
		
		this.city = address.getCity();
		this.country = CountryUtil.getInstance().getNameByCode(address.getCountry());
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

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
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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
		this.country = country;
	}

	/**
	 * Get address entity from DTO
	 * 
	 * @param address
	 */
	@JsonIgnore
	public Address getAddress(Address address) {
		if (address == null) {
			address = new Address();
		}
		address.setLine1(line1);
		address.setLine2(line2);
		address.setZipcode(zipcode);
		address.setCity(city);
		address.setState(state);
		if(this.getCountry()!=null) {
			address.setCountry(CountryUtil.getInstance().getCountry(country).getCode());
		}
		return address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((line1 == null) ? 0 : line1.hashCode());
		result = prime * result + ((line2 == null) ? 0 : line2.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((zipcode == null) ? 0 : zipcode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddressDTO other = (AddressDTO) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (line1 == null) {
			if (other.line1 != null)
				return false;
		} else if (!line1.equals(other.line1))
			return false;
		if (line2 == null) {
			if (other.line2 != null)
				return false;
		} else if (!line2.equals(other.line2))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (zipcode == null) {
			if (other.zipcode != null)
				return false;
		} else if (!zipcode.equals(other.zipcode))
			return false;
		return true;
	}

}
