package com.teamium.domain.prod.resources.functions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceEntity;
import com.teamium.domain.prod.resources.ResourceFunction;

/**
 * Describe a default resource assigned to planning
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("default")
public class DefaultResource extends Resource{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6669566017782820800L;
	
	/**
	 * 
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="c_idfunction", nullable=false)
	private RatedFunction function;
	
	/**
	 * @return the managed entity as a resource
	 */
	@Override
	public RatedFunction getEntity() {
		return null;
	}

	/**
	 * @param entity the entity to manage as a resource
	 */
	public void setFunction(RatedFunction entity) {
		this.function=entity;
		if(entity!=null){
			super.assignFunction(entity);
		}else{
			this.getFunctions().clear();
		}
	}
	
	/**
	 * @param entity the entity to manage as a resource
	 */
	@Override
	public void setEntity(ResourceEntity entity) {
		if(entity instanceof RatedFunction){
			this.setFunction((RatedFunction) entity);
		}
	}

	/**
	 * Assignes the given function to the current resource
	 * @param function the function to assign
	 * @retun the resource assignment if success, else returns false
	 */
	@Override
	public ResourceFunction assignFunction(Function name) {
		ResourceFunction function=null;
		try{
			if(name.equals(this.function)){
				function = this.getFunction(name);
				if(function==null){
					this.getFunctions().clear();
					function = super.assignFunction(name);
				}
			}
		}
		catch(Exception e){	}
		return function;
	}
	
	/**
	 * Removes the given assignement from the current resource
	 * @return function the function assignment to remove
	 * @retun true if success, else returns false
	 */
	@Override
	public boolean unassignFunction(ResourceFunction function){
		return false;
	}
	
	/**
	 * Compare order
	 */
	@Override
	public Integer getComparatorPosition(){
		return 0;
	}

}
