/**
 * 
 */
package com.teamium.domain.prod.workflow;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * Describes the abstract layer of a workflow
 * @author sraybaud
 *
 */
@Entity
@Table(name="t_workflow")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="c_discriminator", discriminatorType=DiscriminatorType.STRING)
@NamedQueries({
	@NamedQuery(name=AbstractWorkflow.QUERY_findByIds, query="SELECT w FROM AbstractWorkflow w WHERE w.id IN (?1) AND TYPE(w) = ?2 ORDER BY w.name"),
	@NamedQuery(name=AbstractWorkflow.QUERY_countAllByType, query="SELECT COUNT(w) FROM Workflow w WHERE TYPE(w) = ?1"),
	@NamedQuery(name=AbstractWorkflow.QUERY_findAllByType, query="SELECT w FROM Workflow w WHERE TYPE(w) = ?1 ORDER BY w.name"),
})
public abstract class AbstractWorkflow<T, U extends AbstractStep<T>> extends AbstractEntity implements Comparable<AbstractWorkflow<T,U>>{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6195752632535146638L;
	
	/**
	 * The entity alias used in queries
	 */
	public static final String QUERY_ENTITY_ALIAS = "w";

	/**
	 * Name of the query retrieving the given type of workflows matching the given list of ids
	 * @param 1 the ids to match
	 * @return the matching entities
	 */
	public static final String QUERY_findByIds = "findWorkflowByIds";	
	
	/**
	 * String of the query retrieving the given type of workflows matching the given list of ids, that may be completed by OrderByClause.getClause()
	 * @param 1 the ids to match
	 * @return the matching entities
	 */
	public static final String QUERY_findOrderedByIds = "SELECT w.id FROM AbstractWorkFlow w WHERE w.id IN (?1)";	
	
	
	/**
	 * Name of the query counting all workflows with the given type
	 * @param 1 the type to workflows to count
	 * @return count of workflows matching this given type
	 */
	public static final String QUERY_countAllByType = "countAllWorkflowByType";
	
	/**
	 * Name of the query retrieving all workflows with the given type
	 * @param 1 the type of workflows to retrieve
	 * @return the list of all workflows matching this given type
	 */
	public static final String QUERY_findAllByType = "findAllWorkflowByType";
	
	/**
	 * String of the query retrieving all instances of the given type of workflows, that may be completed by OrderByClause.getClause()
	 * @param 1 the type of workflows to retrieve
	 * @return the matching entities
	 */
	public static final String QUERY_findAllOrderedByType = "SELECT w FROM AbstractWorkFlow w WHERE TYPE(w) = ?1";	
	
	/**
	 * Workflow ID
	 */
	@Id
	@Column(name="c_idworkflow", insertable=false, updatable=false)
	@TableGenerator( name = "idWorkflow_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "workflow_idworkflow_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idWorkflow_seq")
	private Long id;
	
	/**
	 * The worflow's name
	 */
	@Column(name="c_name")
	private String name;
	
	/**
	 * Specify if workflow is ordered
	 */
	@Column(name="c_ordered")
	private Boolean ordered;


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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ordered
	 */
	public Boolean getOrdered() {
		return ordered;
	}

	/**
	 * @param ordered the ordered to set
	 */
	public void setOrdered(Boolean ordered) {
		this.ordered = ordered;
		this.updatePositions();
	}

	/**
	 * @return the steps
	 */
	public abstract List<U> getSteps();
	
	/**
	 * Create a step instance
	 * @return the created step
	 */
	protected abstract U createStep();
	
	/**
	 * Set the given step at the given index on the current workflow
	 * @param step the step to set
	 * @param index the final index of the step
	 * @return true if the entity has been correctly placed, else returns false
	 */
	public boolean moveStep(U step, Integer index){
		boolean success = false;
		if(step!=null){
			this.ordered=true;
			this.getSteps().remove(step);
			this.addStep(step, index);
		}
		return success;
	}
	
	/**
	 * Add the given entity as step of the current workflow at the given index
	 * @param entity the entity to add
	 * @param index the index of the step
	 * @return true if the entity has been added, else returns false if already exists or if an error occurs
	 */
	public boolean addStep(T entity, Integer index){
		boolean success = false;
		if(entity!=null){
			U newStep = this.createStep();
			newStep.setFunction(entity);
			success = this.addStep(newStep, index);
		}
		return success;
	}
	
	/**
	 * Add the given step on the current workflow at the given index.
	 * @param entity the entity to add
	 * @param index the index of the step
	 * @return true if the entity has been added, else returns false if already exists or if an error occurs
	 */
	private boolean addStep(U step, Integer index){
		boolean success = false;
		if(step!=null){
			for(U s : this.getSteps()){
				if(step.getFunction().equals(s.getFunction())){
					return false;
				}
			}
			if(Boolean.TRUE.equals(ordered)){
				index = this.getSteps().isEmpty()? null : 
							index==null? this.getSteps().size()-1:
								index < 0 ? 0:
								index >= this.getSteps().size()? null : index;
				if(index==null){
					step.setPosition(this.getSteps().size());
					success = this.getSteps().add(step);
				}else{
					try{
						this.getSteps().add(index, step);
						success=true;
					}
					catch(Exception e){
						success=false;
					}
				}
			}else{
				index=null;
				success = this.getSteps().add(step);
			}
			if(success) this.updatePositions();
		}
		return success;
	}
	
	/**
	 * Remove the given step from the current workflow
	 * @param step the step to remove
	 * @return true if the step has been successfully removed, else returns false
	 */
	public boolean removeStep(U step){
		return this.getSteps().remove(step);
	}

	/**
	 * Before update operations
	 */
	@PreUpdate
	@PrePersist
	private void updatePositions(){
		for(int i=0; i < this.getSteps().size() ; i++){
			U step = this.getSteps().get(i);
			step.setPosition(Boolean.TRUE.equals(ordered) ? i : null);
		}
	}
	
	/**
	 * Compare the current object with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(AbstractWorkflow<T,U> o) {
		if(o==null) return 1;
		if(this.name==null){
			if(o.name!=null){
				return -1;
			}
		}else{
			int compare = this.name.compareTo(o.name);
			if(compare !=0) return compare;
		}
		return 0;
	}
	
	/**
	 * Returns the string expression of a project
	 * @return the text
	 */
	@Override
	public String toString(){
		return super.toString()+this.name;
	}

	
	

}
