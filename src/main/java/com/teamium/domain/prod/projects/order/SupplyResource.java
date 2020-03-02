package com.teamium.domain.prod.projects.order;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceEntity;

/**
 * Describe supply faceted resource
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("supply")
public class SupplyResource extends Resource{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 4670086237496146600L;
	
	/**
	 * The staff member corresponding to the resource
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="c_idorder")
	private Order entity;

	/**
	 * @return the managed entity as a resource
	 */
	public Order getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to manage as a resource
	 */
	public void setOrder(Order entity) {
		this.entity = entity;
		if(entity!=null){
			this.setName((entity.getCompany()!=null? entity.getCompany().getName() +" ": "")+"#"+entity.getId());			
		}else{
			this.setName(null);
		}
	}
	
	/**
	 * @param entity the entity to manage as a resource
	 */
	@Override
	public void setEntity(ResourceEntity entity) {
		if(entity instanceof Order){
			this.setOrder((Order) entity);
		}
	}
	
	/**
	 * Comparator order
	 */
	protected Integer getComparatorPosition(){
		return 300;
	}
}
