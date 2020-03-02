package com.teamium.domain.output;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.output.adapters.CalendarDateAdapter;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Xml representation for staff
 * @author JS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StaffXml extends ContactXml{

	/**
	 * The birth date
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	@XmlJavaTypeAdapter(CalendarDateAdapter.class)
	private Calendar birthDate;
	
	/**
	 * The birthplace
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String birthPlace;
	
	/**
	 * The birth date
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String birthState;
	
	/**
	 * Teh address
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private AddressXml address;
	
	/**
	 * Num secu
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String numSecu;
	
	/**
	 * Name of the security center
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String nameSecuCenter;
	
	/**
	 * Numero dd conges spectacles
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String numCotisation;
	
	/**
	 * Nationality
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String nationality;
	
	/**
	 * Marshal into xml
	 * @param obj
	 */
	public void marshal(StaffMember obj){
		super.marshal(obj);
		birthDate = obj.getIdentity().getDateOfBirth();
		birthState = obj.getIdentity().getBirthState();
		birthPlace = obj.getIdentity().getBirthPlace();
		address = new AddressXml();
		address.marshal(obj.getAddress());
		numSecu = obj.getIdentity().getHealthInsuranceNumber();
		nameSecuCenter = obj.getIdentity().getHealthInsuranceName();
		numCotisation = obj.getIdentity().getNumCotisationSpectacle();
		if(obj.getIdentity().getNationality() != null)
			nationality = obj.getIdentity().getNationality().getLabel();
	}
	
}
