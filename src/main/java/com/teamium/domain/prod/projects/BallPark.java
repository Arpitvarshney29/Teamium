/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.teamium.domain.prod.Line;

/**
 * Describes a ball park
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue(BallPark.DISCRIMINATOR_VALUE)
public class BallPark extends AbstractProject {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 3873130311519253324L;
	
	/**
	 * The discriminator value of the current class
	 */
	public static final String DISCRIMINATOR_VALUE = "ballpark";
	
	/**
	 * Clone the current object
	 * @return the clone
	 * @throws CloneNotSupportedException 
	 */
	@Override
	public BallPark clone() throws CloneNotSupportedException{
		BallPark clone = null;
		try{
			clone = (BallPark) super.clone();
			return clone;
		}catch(CloneNotSupportedException e){
			throw e;
		}finally{
		}
		
	}

}
