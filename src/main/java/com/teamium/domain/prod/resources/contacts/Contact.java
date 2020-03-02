/**
 * 
 */
package com.teamium.domain.prod.resources.contacts;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.teamium.domain.Person;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Describe a commercial contact
 * @author sraybaud- NovaRem
 *
 */
@Entity
@DiscriminatorValue("contact")
@NamedQueries({
	@NamedQuery(name=Contact.QUERY_findLikeKeyword, query="SELECT c FROM Contact c WHERE LOWER(c.name) LIKE ?1 ORDER BY c.name ASC, c.firstName ASC"),
	@NamedQuery(name=Contact.QUERY_findAll, query="SELECT c From Contact c ORDER BY c.name ASC, c.firstName ASC")
})

public class Contact extends Person{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -3825569030190655247L;
	
	/**
	 * Follower
	 */
	@ManyToOne
	@JoinColumn(name="c_contact_idfollower")
	private StaffMember follower;

	/**
	 * Name of the query returning all recorded directors
	 * @return the directors' list
	 */
	public static final String QUERY_findLikeKeyword = "findByKeyword";
	
	/**
	 * Name of the query returning all recorded contacts
	 * @return the contacts' list
	 */
	public static final String QUERY_findAll = "findAllContacts";

	/**
	 * @return the follower
	 */
	public StaffMember getFollower() {
		return follower;
	}

	/**
	 * @param follower the follower to set
	 */
	public void setFollower(StaffMember follower) {
		this.follower = follower;
	}
}
