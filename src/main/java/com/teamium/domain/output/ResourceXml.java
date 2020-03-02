package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.prod.projects.Booking;
import com.teamium.domain.prod.resources.Resource;
import com.teamium.domain.prod.resources.equipments.Equipment;
import com.teamium.domain.prod.resources.equipments.EquipmentResource;
import com.teamium.domain.prod.resources.staff.StaffMember;
import com.teamium.domain.prod.resources.staff.StaffResource;

/**
 * An XML resource output
 * @author sraybaud
 * @version TEAM-43
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceXml {
	/**
	 * The id of the referenced resource
	 */
	@XmlAttribute
	private Long id;
	
	/**
	 * Full name of the function
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String fullName;
	
	/**
	 * The name of the resource's function
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String name;
	
	/**
	 * Assignment type
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String assignation;
	
	/**
	 * The staff resource
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private StaffXml staff;
	
	
	/**
	 * The equipment resource
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private EquipmentXml equipment;
	

	/**
	 * @param b the object to marshal
	 */
	public void marshal(Booking b){		
		if(b!=null) {
			Resource<?> obj = b.getResource();
			if(obj!=null) {
				this.id = obj.getId();
				if(b.getFunction()!=null) {
					this.name = b.getFunction().getValue().toUpperCase();	
					this.fullName = b.getFunction().getQualifiedName();
				}
				if(obj.getClass().equals(StaffResource.class))
				{
					StaffXml xml = new StaffXml();
					xml.marshal((StaffMember) obj.getEntity());
					this.staff = xml;
				}
				if(obj.getClass().equals(EquipmentResource.class))
				{
					EquipmentXml xml = new EquipmentXml();
					xml.marshal((Equipment) obj.getEntity());
					this.equipment = xml;
				}
			}
		}
	}	
}
