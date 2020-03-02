package com.teamium.dto;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.staff.IDDocument;
import com.teamium.domain.prod.resources.staff.StaffMemberIdentity;
import com.teamium.enums.Gender;
import com.teamium.utils.CountryUtil;

/**
 * DTO for StaffMemberIdentity Entity
 * 
 * @author wittybrains
 *
 */
public class StaffMemberIdentityDTO {

	/**
	 * Gender
	 */
	private String gender;

	/**
	 * Date of birth
	 */
	private Calendar dateOfBirth;

	/**
	 * Birth city
	 */
	private String birthPlace;

	/**
	 * Birth state
	 */
	private String birthState;

	/**
	 * Birth country
	 */
	private String birthCountry;

	/**
	 * Social security number / Health insurance number
	 */
	private String socSecNumber;

	/**
	 * Health insurance company name
	 */
	private String healthInsuranceName;

	/**
	 * Talent Registartion / Numéro de conges spectacles
	 */
	private String talentReg;

	/**
	 * Routing and Accounting/IBAN
	 */
	private String routingAndAccounting;

	/**
	 * BIC
	 */
	private String numBic;

	/**
	 * Bank Address / Domicialiation
	 */
	private String bankAddress;

	/**
	 * Beneficiaire
	 */
	private String beneficiaire;

	/**
	 * Citizenship / Nationalité
	 */
	private String nationality;

	/**
	 * Married name
	 */
	private String marriedName;

	/**
	 * Birth province
	 */
	private String birthProvince;

	/**
	 * ID documents
	 */
	private List<IDDocument> documents;

	/**
	 * Number card press
	 */
	private String numCardPress;

	/**
	 * @return the gender
	 */

	public StaffMemberIdentityDTO() {
		super();
	}

	public StaffMemberIdentityDTO(StaffMemberIdentity staffMemberIdentity) {

		this.gender = staffMemberIdentity.getGender() != null ? staffMemberIdentity.getGender().getKey() : null;
		this.dateOfBirth = staffMemberIdentity.getDateOfBirth();
		this.birthPlace = staffMemberIdentity.getBirthPlace();
		this.birthState = staffMemberIdentity.getBirthState();
		this.birthCountry = staffMemberIdentity.getPersistentbirthCountry();
		this.socSecNumber = staffMemberIdentity.getHealthInsuranceNumber();
		this.healthInsuranceName = staffMemberIdentity.getHealthInsuranceName();
		this.talentReg = staffMemberIdentity.getNumCotisationSpectacle();
		this.routingAndAccounting = staffMemberIdentity.getNumIBAN();
		this.numBic = staffMemberIdentity.getNumBic();
		this.bankAddress = staffMemberIdentity.getDomiciliation();
		this.beneficiaire = staffMemberIdentity.getBeneficiaire();
		this.nationality = staffMemberIdentity.getNationality() != null ? staffMemberIdentity.getNationality().getKey()
				: null;
		this.marriedName = staffMemberIdentity.getMarriedName();
		this.birthProvince = staffMemberIdentity.getBirthProvince();
		// this.documents = staffMemberIdentity.getDocuments();
		this.numCardPress = staffMemberIdentity.getNumCardPress();

	}

	public StaffMemberIdentityDTO(String gender, Calendar dateOfBirth, String birthPlace, String birthState,
			String persistentbirthCountry, Locale birthCountry, String healthInsuranceNumber,
			String healthInsuranceName, String numCotisationSpectacle, String numIBAN, String numBic,
			String domiciliation, String beneficiaire, XmlEntity nationality, String marriedName, String birthProvince,
			List<IDDocument> documents, String numCardPress) {
		super();
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.birthPlace = birthPlace;
		this.birthState = birthState;
		this.birthCountry = birthCountry.getCountry();
		// this.birthCountry = birthCountry;
		this.socSecNumber = healthInsuranceNumber;
		this.healthInsuranceName = healthInsuranceName;
		this.talentReg = numCotisationSpectacle;
		this.routingAndAccounting = numIBAN;
		this.numBic = numBic;
		this.bankAddress = domiciliation;
		this.beneficiaire = beneficiaire;
		this.nationality = nationality.getKey();
		this.marriedName = marriedName;
		this.birthProvince = birthProvince;
		this.documents = documents;
		this.numCardPress = numCardPress;
	}

	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Calendar getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Calendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * @return the birthPlace
	 */
	public String getBirthPlace() {
		return birthPlace;
	}

	/**
	 * @param birthPlace
	 *            the birthPlace to set
	 */
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	/**
	 * @return the birthState
	 */
	public String getBirthState() {
		return birthState;
	}

	/**
	 * @param birthState
	 *            the birthState to set
	 */
	public void setBirthState(String birthState) {
		this.birthState = birthState;
	}

	/**
	 * @return the birthCountry
	 */
	public String getBirthCountry() {
		return birthCountry;
	}

	/**
	 * @param birthCountry
	 *            the birthCountry to set
	 */
	public void setBirthCountry(String birthCountry) {
		this.birthCountry = birthCountry;
	}

	/**
	 * @return the healthInsuranceName
	 */
	public String getHealthInsuranceName() {
		return healthInsuranceName;
	}

	/**
	 * @param healthInsuranceName
	 *            the healthInsuranceName to set
	 */
	public void setHealthInsuranceName(String healthInsuranceName) {
		this.healthInsuranceName = healthInsuranceName;
	}

	/**
	 * @return the numBic
	 */
	public String getNumBic() {
		return numBic;
	}

	/**
	 * @param numBic
	 *            the numBic to set
	 */
	public void setNumBic(String numBic) {
		this.numBic = numBic;
	}

	/**
	 * @return the beneficiaire
	 */
	public String getBeneficiaire() {
		return beneficiaire;
	}

	/**
	 * @param beneficiaire
	 *            the beneficiaire to set
	 */
	public void setBeneficiaire(String beneficiaire) {
		this.beneficiaire = beneficiaire;
	}

	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * @param nationality
	 *            the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the marriedName
	 */
	public String getMarriedName() {
		return marriedName;
	}

	/**
	 * @param marriedName
	 *            the marriedName to set
	 */
	public void setMarriedName(String marriedName) {
		this.marriedName = marriedName;
	}

	/**
	 * @return the birthProvince
	 */
	public String getBirthProvince() {
		return birthProvince;
	}

	/**
	 * @param birthProvince
	 *            the birthProvince to set
	 */
	public void setBirthProvince(String birthProvince) {
		this.birthProvince = birthProvince;
	}

	/**
	 * @return the documents
	 */
	public List<IDDocument> getDocuments() {
		return documents;
	}

	/**
	 * @param documents
	 *            the documents to set
	 */
	public void setDocuments(List<IDDocument> documents) {
		this.documents = documents;
	}

	/**
	 * @return the numCardPress
	 */
	public String getNumCardPress() {
		return numCardPress;
	}

	/**
	 * @param numCardPress
	 *            the numCardPress to set
	 */
	public void setNumCardPress(String numCardPress) {
		this.numCardPress = numCardPress;
	}

	/**
	 * @return the socSecNumber
	 */
	public String getSocSecNumber() {
		return socSecNumber;
	}

	/**
	 * @param socSecNumber
	 *            the socSecNumber to set
	 */
	public void setSocSecNumber(String socSecNumber) {
		this.socSecNumber = socSecNumber;
	}

	/**
	 * @return the talentReg
	 */
	public String getTalentReg() {
		return talentReg;
	}

	/**
	 * @param talentReg
	 *            the talentReg to set
	 */
	public void setTalentReg(String talentReg) {
		this.talentReg = talentReg;
	}

	/**
	 * @return the routingAndAccounting
	 */
	public String getRoutingAndAccounting() {
		return routingAndAccounting;
	}

	/**
	 * @param routingAndAccounting
	 *            the routingAndAccounting to set
	 */
	public void setRoutingAndAccounting(String routingAndAccounting) {
		this.routingAndAccounting = routingAndAccounting;
	}

	/**
	 * @return the bankAddress
	 */
	public String getBankAddress() {
		return bankAddress;
	}

	/**
	 * @param bankAddress
	 *            the bankAddress to set
	 */
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	/**
	 * To get StaffMemberIdentity from DTO
	 * 
	 * @return the StaffMemberIdentity object
	 */
	@JsonIgnore
	public StaffMemberIdentity getStaffMemberIdentity() {

		StaffMemberIdentity staffMemberIdentity = new StaffMemberIdentity();
		staffMemberIdentity.setBeneficiaire(this.beneficiaire);
		staffMemberIdentity.setPersistentbirthCountry(this.birthCountry);
		staffMemberIdentity.setBirthPlace(this.birthPlace);
		staffMemberIdentity.setBirthProvince(this.birthProvince);
		staffMemberIdentity.setBirthState(this.birthState);
		staffMemberIdentity.setDateOfBirth(this.dateOfBirth);
		staffMemberIdentity.setDocuments(this.documents);
		staffMemberIdentity.setDomiciliation(this.bankAddress);
		if (!StringUtils.isBlank(this.gender)) {
			XmlEntity xmlEntityGender = new XmlEntity();
			xmlEntityGender.setKey(Gender.getEnum(this.gender).getGender());
			staffMemberIdentity.setGender(xmlEntityGender);
		}
		
		staffMemberIdentity.setHealthInsuranceName(this.healthInsuranceName);
		staffMemberIdentity.setHealthInsuranceNumber(this.socSecNumber);
		staffMemberIdentity.setMarriedName(this.marriedName);
		if (nationality != null) {
			XmlEntity xmlEntityNationality = new XmlEntity();
			xmlEntityNationality.setKey(CountryUtil.getInstance().getCountry(this.nationality).getName());
			staffMemberIdentity.setNationality(xmlEntityNationality);
		}
		staffMemberIdentity.setNumBic(this.numBic);
		staffMemberIdentity.setNumCardPress(this.numCardPress);
		staffMemberIdentity.setNumCotisationSpectacle(this.talentReg);
		staffMemberIdentity.setNumIBAN(this.routingAndAccounting);

		return staffMemberIdentity;
	}
}
