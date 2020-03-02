package com.teamium.domain.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.teamium.domain.ContactRecord;
import com.teamium.domain.Person;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * An XML contact output
 * @author sraybaud - NovaRem
 *
 */
public class ContactXml {
	/**
	 * Courtesy
	 * @see com.teamium.domain.prod.Courtesy
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private ItemXml courtesy;
	
	/**
	 * Contact first name
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String firstName;
	
	
	/**
	 * Contact last name
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String lastName;
	
	/**
	 * Contact function in his company
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String function;
	
	/**
	 * employeeCode
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String employeeCode;
	
	/**
	 * Contact role in project
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String role;
	
	/**
	 * The staff language
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String language;
	
	/**
	 * Contact numbers
	 */
	@XmlElementWrapper(name="numbers", namespace=XmlOutput.XMLNS)
	@XmlElement(name="number", namespace=XmlOutput.XMLNS)
	private List<NumberXml> numbers;

	/**
	 * @param obj the object to marshal
	 */
	@SuppressWarnings("unused")
	public void marshal(Person obj){
		if(obj.getCourtesy()!=null){
			this.courtesy = new ItemXml();
			this.courtesy.marshal(obj.getCourtesy());
		}else{
			this.courtesy=null;
		}
		this.firstName=obj.getFirstName();
		this.lastName=obj.getName();
		this.function=obj.getFunction();
		if (obj instanceof StaffMember) {
			StaffMember staff = (StaffMember) obj;
			if (staff != null) {
				this.employeeCode = staff.getEmployeeCode();
			} else {
				this.employeeCode = "0";
			}
			
			if (staff != null && (staff.getLanguages()!= null && !staff.getLanguages().isEmpty()) ) {
				this.language = staff.getLanguages().stream().findFirst().get().getLabel();
			} else {
				this.language = "";
			}
		}
		
		this.numbers = new ArrayList<NumberXml>();
		for(String key : obj.getNumbers().keySet()){
			NumberXml number = new NumberXml();
			number.marshal(key, obj.getNumbers().get(key));
			this.numbers.add(number);
		}
	}
	
	/**
	 * @param obj the object to marshal
	 */
	public void marshal(ContactRecord obj){
		if(obj.getPerson()!=null) {
			if(obj.getPerson().getCourtesy()!=null){
				this.courtesy = new ItemXml();
				this.courtesy.marshal(obj.getPerson().getCourtesy());
			}else{
				this.courtesy=null;
			}
			this.firstName=obj.getPerson().getFirstName();
			this.lastName=obj.getPerson().getName();
			this.function=obj.getPerson().getFunction();
			this.numbers = new ArrayList<NumberXml>();
			for(String key : obj.getPerson().getNumbers().keySet()){
				NumberXml number = new NumberXml();
				number.marshal(key, obj.getPerson().getNumbers().get(key));
				this.numbers.add(number);
			}
			this.role = obj.getRole() == null ? obj.getPerson().getClass().getSimpleName(): obj.getRole().getKey();
		}
	}
	
}

