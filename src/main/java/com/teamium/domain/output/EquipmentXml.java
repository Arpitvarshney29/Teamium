package com.teamium.domain.output;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.teamium.domain.prod.resources.equipments.Equipment;

/**
 * Xml mapping for an equipment resource
 * @author Sébastien Raybaud
 * @version TEAMIUM-43
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EquipmentXml{

	
	/**
	 * Teh address
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private AddressXml address;
	
	/**
	 * Name
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String name;
	
	/**
	 * Description of the equipment
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String description;
	
	/**
	 * Brand
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String brand;
	
	/**
	 * Model
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String model;
	
	/**
	 * Reference
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String reference;
	
	/**
	 * Serial number
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String serialNumber;
	
	/**
	 * Location
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String location;
	
	/**
	 * Location details
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String locationDetails;
	

	
	/**
	 * Marshal into xml
	 * @param obj
	 */
	public void marshal(Equipment obj){
		if(obj !=null){
			name = obj.getName();
			description = obj.getDescription();
			brand = obj.getBrand();
			model = obj.getModel();
			reference = obj.getReference();
			serialNumber = obj.getSerialNumber();
			if(obj.getLocation()!=null) {
				location = obj.getLocation().toString();
			}
			locationDetails = obj.getLocationDetails();
		}

	}
	
}
