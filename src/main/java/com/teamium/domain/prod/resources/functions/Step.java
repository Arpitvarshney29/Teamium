/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.teamium.domain.prod.workflow.AbstractStep;

/**
 * Describes a functional step in a workflow
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("step")
public class Step extends AbstractStep<RatedFunction> {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 8831964657195321516L;
	
	/**
	 * The associated function
	 */
	@ManyToOne
	@JoinColumn(name="c_idfunction")
	private RatedFunction function;
	
	/**
	 * Returns the entity
	 * @return the function
	 */
	public RatedFunction getFunction() {
		return function;
	}
	
	/**
	 * @param function the function to set
	 */
	public void setFunction(RatedFunction function) {
		this.function = function;
	}
	
	/**
	 * Compare the current object with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(AbstractStep<RatedFunction> o) {
		if(super.compareTo(o)==0){
			if(o==null) return 1;
			if(this.function==null){
				if(o.getFunction()!=null){
					return -1;
				}
			}else{
				int compare = this.function.compareTo(o.getFunction());
				if(compare !=0) return compare;
			}
		}
		return 0;
	}
}
