/**
 * 
 */
package com.teamium.domain.prod.resources;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

import com.teamium.domain.Person;

/**
 * Describe a director
 * @author sraybaud- NovaRem
 *
 */
@Entity
@DiscriminatorValue("director")
@NamedQuery(name=Director.QUERY_findAll, query="SELECT d From Director d ORDER BY d.name ASC, d.firstName ASC")
public class Director extends Person{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 2027848792157054438L;
	
	/**
	 * Name of the query returning all recorded directors
	 * @return the directors' list
	 */
	public static final String QUERY_findAll = "findAllDirectors";
	
	

}
