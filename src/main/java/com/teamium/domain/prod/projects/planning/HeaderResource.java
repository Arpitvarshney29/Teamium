/**
 * 
 */
package com.teamium.domain.prod.projects.planning;



import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceEntity;

/**
 * Transient resource used as header on plannings
 * @author sraybaud
 *
 */
public class HeaderResource extends Resource {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6477525351799110775L;
	
	/**
	 * Comparator order
	 */
	private Integer comparatorPosition = 0;

	@Override
	public ResourceEntity getEntity() {
		return null;
	}

	@Override
	public void setEntity(ResourceEntity entity) {}
	
	/**
	 * Comparator order
	 */
	public Integer getComparatorPosition(){
		return comparatorPosition;
	}
	
	/**
	 * Update comparator order
	 */
	public void setComparatorPosition(Integer comparatorPosition){
		this.comparatorPosition = comparatorPosition;
	}
	
	/**
	 * @return the position
	 */
	@Override
	public String getPosition() {
		return this.getComparatorPosition()+"_  "+this.getName()+"_"+this.getId();
	}

}
