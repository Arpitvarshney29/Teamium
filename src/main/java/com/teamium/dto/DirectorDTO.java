package com.teamium.dto;

import java.net.MalformedURLException;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Person;
import com.teamium.domain.prod.resources.Director;

/**
 * Wrapper class for Abstract-Project
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DirectorDTO extends PersonDTO {

	public DirectorDTO() {

	}

	public DirectorDTO(Director director) {
		super(director);
	}

	public DirectorDTO(Person person) {
		super(person,"forDirector");
	}
	
	/**
	 * To get director object
	 * 
	 * @param director    the director
	 * 
	 * @param directorDTO the directorDTO
	 * 
	 * @return director object
	 */
	public Director getDirector(Director director, DirectorDTO directorDTO) {
		setDirectorDetail(director, directorDTO);
		return director;
	}

	/**
	 * To get director object
	 * 
	 * @return director object
	 */
	public Director getDirector() {
		return this.getDirector(new Director(), this);
	}

	/**
	 * To set director details
	 * 
	 * @param director    the director
	 * 
	 * @param directorDTO the directorDTO
	 */
	public void setDirectorDetail(Director director, DirectorDTO directorDTO) {
		try {
			directorDTO.setPersonDetail(director, directorDTO);
		} catch (MalformedURLException e) {
		}
	}

}
