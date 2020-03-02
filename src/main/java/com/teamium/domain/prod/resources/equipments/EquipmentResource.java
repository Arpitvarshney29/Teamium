package com.teamium.domain.prod.resources.equipments;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.ResourceEntity;

/**
 * Describes an equipment faceted resource
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("equipment")
@NamedQueries({
	@NamedQuery(name=EquipmentResource.FIND_ALL, query="SELECT er FROM EquipmentResource er ORDER BY er.name"),
	@NamedQuery(name=EquipmentResource.FIND_GROUPS, query="SELECT DISTINCT er FROM EquipmentResource er WHERE EXISTS(SELECT er2 FROM EquipmentResource er2 WHERE er2.parent = er) OR er.entity is NULL"),
	@NamedQuery(name=EquipmentResource.FIND_GROUPS_BY_KEYWORD, query="SELECT DISTINCT er FROM EquipmentResource er WHERE (EXISTS(SELECT er2 FROM EquipmentResource er2 WHERE er2.parent = er) OR er.entity is NULL) AND LOWER(er.name) LIKE ?1"),
	@NamedQuery(name=EquipmentResource.FIND_CHILDREN, query="SELECT er FROM EquipmentResource er WHERE er.parent = ?1"),
	@NamedQuery(name=EquipmentResource.FIND_AVAILABLE_CHILDREN_BY_KEYWORD, query="SELECT er FROM EquipmentResource er WHERE er.parent IS NULL AND er.entity IS NOT NULL AND LOWER(er.name) LIKE ?1"),
	@NamedQuery(name=EquipmentResource.FIND_BY_KEYWORD, query="SELECT er FROM EquipmentResource er WHERE LOWER(er.name) LIKE ?1"),
})
public class EquipmentResource extends Resource<EquipmentResource>{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 3692829466937926564L;
	
	/**
	 * The named query to find equipment resources
	 */
	public static final String FIND_ALL = "find_equipment_resource";
	
	/**
	 * The named query used to find equipment groups
	 */
	public static final String FIND_GROUPS = "find_equipment_group";
	
	/**
	 * The named query used to find equipment groups matching the keyword given in parameter
	 * @param 1 keyword
	 */
	public static final String FIND_GROUPS_BY_KEYWORD = "find_equipment_group_by_keyword";
	
	/**
	 * The named query used to find children of the equipment given in parameter
	 * @param 1 The equipment
	 */
	public static final String FIND_CHILDREN = "find_equipment_resource_childrens";
	
	/**
	 * The named query used to find children available to be add as a children
	 */
	public static final String FIND_AVAILABLE_CHILDREN = "find_equipment_resource_available_to_use_as_child";
	
	/**
	 * The named query used to find children available to be add as a children filter by the keyword given in parameter
	 */
	public static final String FIND_AVAILABLE_CHILDREN_BY_KEYWORD = "find_equipment_resource_available_to_use_as_child";
	
	/**
	 * The named query used to find equipment matching the keyword given in parameter
	 * @param 1 keyword
	 */
	public static final String FIND_BY_KEYWORD = "find_equipment_resource_by_keyword";
	
	
	/**
	 * The staff member corresponding to the resource
	 */
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="c_idequipment")
	private Equipment entity;

	/**
	 * @return the managed entity as a resource
	 */
	@Override
	public Equipment getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to manage as a resource
	 */
	public void setEquipment(Equipment entity) { 
		this.entity = entity;
	}
	
	/**
	 * @param entity the entity to manage as a resource
	 */
	@Override
	public void setEntity(ResourceEntity entity) {
		if(entity instanceof Equipment){
			this.setEquipment((Equipment) entity);
		}
	}
	
	/**
	 * Returns the resource name
	 * @return the name
	 */
	public String fetchName(){
		if(this.entity!=null) return entity.getName();
		else return null;
	}
	
	/**
	 * Comparator order
	 */
	@Override
	protected Integer getComparatorPosition(){
		return 200;
	}
	
	

}
