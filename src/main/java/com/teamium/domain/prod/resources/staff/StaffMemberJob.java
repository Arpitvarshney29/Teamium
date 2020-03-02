/**
 * 
 */
package com.teamium.domain.prod.resources.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.functions.RatedFunction;

/**
 * Describe a staff member function rating
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name="t_staff_function")
public class StaffMemberJob extends AbstractEntity implements Comparable<StaffMemberJob>{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -7211402274438036064L;

	/**
	 * Job ID
	 */
	@Id
	@Column(name="c_idstafffunction", insertable=false, updatable=false)
	@TableGenerator( name = "idStaffFunction_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "person_function_idstafffunction_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idStaffFunction_seq")
	private Long id;
	
	/**
	 * Skilled function
	 */
	@ManyToOne
	@JoinColumn(name="c_idfunction")
	private RatedFunction function;
	
	/**
	 * Job rating
	 */
	@Column(name="c_rating")
	private Integer rating;

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
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	
	/**
	 * Compare the current object with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(StaffMemberJob o) {
		if(o==null) return 1;
		if(this.function==null){
			if(o.function!=null){
				return -1;
			}
		}else{
			int compare = this.function.compareTo(o.function);
			if(compare !=0) return compare;
		}
		return 0;
	}
	
	
}
