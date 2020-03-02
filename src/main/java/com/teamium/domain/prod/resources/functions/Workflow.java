/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.teamium.domain.prod.workflow.AbstractWorkflow;

/**
 * Describes a preparameterized functional workflow 
 * @author sraybaud
 *
 */
@Entity
@DiscriminatorValue("workflow")
public class Workflow extends AbstractWorkflow<RatedFunction,Step>{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6195752632535146638L;
	
	/**
	 * Name of the query counting all workflows in persistence unit
	 * @return count of workflows
	 */
	public static final String QUERY_countAll = "countAllWorkflow";
	
	/**
	 * Name of the query retrieving all workflows from persistence unit
	 * @return the list of all workflows
	 */
	public static final String QUERY_findAll = "findAllWorkflow";

	/**
	 * The workflow nodes
	 */
	@OneToMany(cascade={CascadeType.DETACH,CascadeType.MERGE, CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="c_idworkflow")
	private List<Step> steps;

	/**
	 * @return the steps
	 */
	@Override
	public List<Step> getSteps() {
		if(this.steps==null) this.steps = new LinkedList<Step>();
		return steps;
	}

	/**
	 * Create a new instance of step
	 * @param step the created step
	 */
	@Override
	public Step createStep() {
		return new Step();
	}
}
