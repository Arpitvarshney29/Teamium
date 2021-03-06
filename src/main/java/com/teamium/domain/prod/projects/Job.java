/**
 * 
 */
package com.teamium.domain.prod.projects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.teamium.domain.prod.workflow.AbstractStep;

/**
 * Describes a job in a processing worflow
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue(Job.DISCRIMINATOR_VALUE)
public class Job extends AbstractStep<Booking> {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 3113007680087081940L;

	/**
	 * The discriminator value of the current class
	 */
	public static final String DISCRIMINATOR_VALUE = "job";
	
	/**
	 * The associated function
	 */
	@ManyToOne
	@JoinColumn(name="c_idbooking")
	private Booking booking;
	
	/**
	 * Returns the entity
	 * @return the function
	 */
	public Booking getFunction() {
		return booking;
	}
	
	/**
	 * @param booking the booking to set
	 */
	public void setFunction(Booking booking) {
		this.booking = booking;
	}
	
	/**
	 * Compare the current object with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(AbstractStep<Booking> o) {
		if(super.compareTo(o)==0){
			if(o==null) return 1;
			if(this.booking==null){
				if(o.getFunction()!=null){
					return -1;
				}
			}else{
				int compare = this.booking.compareTo(o.getFunction());
				if(compare !=0) return compare;
			}
		}
		return 0;
	}
}
