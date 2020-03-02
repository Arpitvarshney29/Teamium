/**
 * 
 */
package com.teamium.domain.prod.workflow;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * Describe the abstract layer of a step, which is part of a workflow
 * @author sraybaud
 *
 */
@Entity
@Table(name="t_step")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="c_discriminator", discriminatorType=DiscriminatorType.STRING)
public abstract class AbstractStep<T> extends AbstractEntity implements Comparable<AbstractStep<T>>{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 4974074493433844694L;

	/**
	 * Task ID
	 */
	@Id
	@Column(name="c_idstep", insertable=false, updatable=false)
	@TableGenerator( name = "idStep_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "step_idstep_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idStep_seq")
	private Long id;
	
	/**
	 * Boolean speciying if nodes are ordered in the workflow
	 */
	@Column(name="c_step_position")
	private Integer position;
	
	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}
	
	/**
	 * Return the entity composing the task
	 * @return the entity
	 */
	public abstract T getFunction();
	
	/**
	 * Set the entity composing the task
	 * @param entity the entity to set
	 */
	public abstract void setFunction(T entity);

	/**
	 * Compare the current object with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(AbstractStep<T> o) {
		if(o==null) return 1;
		if(this.position==null){
			if(o.position!=null){
				return -1;
			}
		}else{
			int compare = this.position.compareTo(o.position);
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
		return super.toString()+"["+(position!=null? position+"->" : "")+this.getFunction()+"]";
	}
}
