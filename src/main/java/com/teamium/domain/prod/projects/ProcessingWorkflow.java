/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.teamium.domain.prod.workflow.AbstractWorkflow;

/**
 * Describes a processing workflow
 * @author sraybaud
 *
 */
@Entity
@DiscriminatorValue("process")
@NamedQueries({
	@NamedQuery(name=ProcessingWorkflow.QUERY_findByBooking, query="SELECT DISTINCT w FROM ProcessingWorkflow w, IN(w.steps) s WHERE s.booking = ?1")
})
public class ProcessingWorkflow extends AbstractWorkflow<Booking,Job>{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -8674438482879311361L;
	
	/**
	 * Name of the query retrieving worklows including the given booking
	 * @param 1 the given booking
	 * @return the workflows list
	 */
	public static final String QUERY_findByBooking = "findProcessingWorkflowByBooking";
	
	/**
	 * The associated project
	 */
	@ManyToOne
	@JoinColumn(name="c_idproject")
	private Project project;
	
	/**
	 * The workflow nodes
	 */
	@OneToMany(cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="c_idworkflow")
	private List<Job> steps;

	/**
	 * @return the steps
	 */
	@Override
	public List<Job> getSteps() {
		if(this.steps==null) this.steps = new LinkedList<Job>();
		return steps;
	}

	/**
	 * Create a new instance of step
	 * @param step the created step
	 */
	@Override
	protected Job createStep() {
		return new Job();
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}
	
	/**
	 * Returns the string expression of a project
	 * @return the text
	 */
	@Override
	public String toString(){
		return super.toString()+" on "+this.project;
	}

}
