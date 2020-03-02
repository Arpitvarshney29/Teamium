/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.teamium.domain.prod.Line;
import com.teamium.domain.prod.resources.InOut;
import com.teamium.domain.prod.resources.SaleEntity;
import com.teamium.domain.prod.resources.staff.contract.Contract;

/**
 * Describes a project
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue(Project.DISCRIMINATOR_VALUE)
@NamedQueries({
		@NamedQuery(name = Project.QUERY_findRunning, query = "SELECT p FROM Project p WHERE NOT EXISTS (SELECT i FROM Invoice i WHERE i.source = p) ORDER BY p.title ASC"),
		@NamedQuery(name = Project.QUERY_findLikeKeyword, query = "SELECT p FROM Project p WHERE LOWER(p.title) LIKE ?1 ORDER BY p.title"),
		@NamedQuery(name = Project.QUERY_findByDates, query = "SELECT p FROM Project p WHERE p.startDate BETWEEN ?1 AND ?2 AND TYPE(p) = Project"),
		@NamedQuery(name = Project.QUERY_findByMedia, query = "SELECT p FROM Project p WHERE EXISTS ( SELECT ino.id FROM InOut ino WHERE ino MEMBER OF p.inOuts and ino.media = ?1 )"), })
public class Project extends Quotation {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -8027390109445523318L;

	/**
	 * Name of the query retrieving all running projects (not invoiced yet)
	 * 
	 * @return the list of matching projects
	 */
	public static final String QUERY_findRunning = "findRunningProject";

	/**
	 * Name of the query retrieving all projects from a keyword
	 * 
	 * @param keyword,
	 *            String used on project title
	 * @return the list of matching projects
	 */
	public static final String QUERY_findLikeKeyword = "findLikeKeyword";

	/**
	 * Query which return all project which starts between the two dates
	 * 
	 * @param ?1
	 *            The start date
	 * @param ?2
	 *            the end date
	 */
	public static final String QUERY_findByDates = "findProjectWithDates";

	/**
	 * Query which return all project which are linked to the given media
	 * 
	 * @param ?1
	 *            The media
	 */
	public static final String QUERY_findByMedia = "findProjectByMedia";

	/**
	 * The discriminator value of the current class
	 */
	public static final String DISCRIMINATOR_VALUE = "project";

	/**
	 * The list of InOut
	 */
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "c_project")
	private List<InOut> inOuts = new ArrayList<InOut>();

	/**
	 * L'employeur
	 */
	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "c_contract")
	private Contract contract;

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Project clone() throws CloneNotSupportedException {
		return (Project) super.clone();

	}

	/**
	 * Add the given line to the project
	 * 
	 * @param line
	 *            the line to add
	 * @return true if success, else returns false
	 */
	@Override
	public boolean addLine(Line line) {
		if (line == null)
			return false;
		for (Line current : this.getLines()) {
			if (line.equals(current)) {
				return false;
			}
		}
		return super.addBooking(line);
	}

	/**
	 * @return the inOuts
	 */
	public List<InOut> getInOuts() {
		return inOuts;
	}

	/**
	 * @param inOuts
	 *            the inOuts to set
	 */
	public void setInOuts(List<InOut> inOuts) {
		this.inOuts = inOuts;
	}

	/**
	 * @return the contract
	 */
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            the contract to set
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

}
