package com.teamium.domain.prod.resources.staff;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.teamium.domain.XmlEntity;

/**
 * Describe identity information of a staff member
 * 
 * @author sraybaud - NovaRem
 * @version 5.1.0 : Add property numCardPress
 */
@Embeddable
public class StaffMemberIdentity implements Serializable {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 3143269194006758029L;

	/**
	 * Genre
	 * 
	 * @see com.teamium.domain.prod.resources.staff.Gender
	 */
	@AttributeOverride(name = "key", column = @Column(name = "c_gender"))
	private XmlEntity gender;

	/**
	 * Date of birth
	 */
	@Column(name = "c_birth_date")
	@Temporal(TemporalType.DATE)
	private Calendar dateOfBirth;

	/**
	 * Birth city
	 */
	@Column(name = "c_birth_city")
	private String birthPlace;

	/**
	 * Birth state
	 */
	@Column(name = "c_birth_state")
	private String birthState;

	/**
	 * Birth country
	 */
	@Column(name = "c_birth_country")
	private String persistentbirthCountry;
	@Transient
	private Locale birthCountry;

	/**
	 * Health insurance number
	 */
	@Column(name = "c_healthinsurance_number")
	private String healthInsuranceNumber;

	/**
	 * Health insurance company name / Socaial security number
	 */
	@Column(name = "c_healthinsurance_company")
	private String healthInsuranceName;

	/**
	 * Numéro de conges spectacles/ Talent Registration
	 */
	@Column(name = "c_cotisation_spectacle")
	private String numCotisationSpectacle;

	/**
	 * IBAN /Routing and Account
	 */
	@Column(name = "c_iban")
	private String numIBAN;

	/**
	 * BIC
	 */
	@Column(name = "c_bic")
	private String numBic;

	/**
	 * Domicialiation/bank address
	 */
	@Column(name = "c_domiciliation")
	private String domiciliation;

	/**
	 * Beneficiaire
	 */
	@Column(name = "c_beneficiaire")
	private String beneficiaire;

	/**
	 * Nationalité/ Citizenship
	 */
	@AttributeOverride(name = "key", column = @Column(name = "c_nationality"))
	private XmlEntity nationality;

	/**
	 * Married name
	 */
	@Column(name = "c_marriedname")
	private String marriedName;

	/**
	 * Birth province
	 */
	@Column(name = "c_birth_province")
	private String birthProvince;

	/**
	 * ID documents
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JoinColumn(name = "c_idcontact")
	private List<IDDocument> documents;

	/**
	 * Number card press
	 */
	@Column(name = "c_numcardpress")
	private String numCardPress;

	/**
	 * @return the gender
	 */
	public XmlEntity getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(XmlEntity gender) {
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
	public Locale getBirthCountry() {
		return this.birthCountry;
	}

	/**
	 * @param birthCountry
	 *            the birthCountry to set
	 */
	public void setBirthCountry(Locale birthCountry) {
		this.birthCountry = birthCountry;
		this.persistentbirthCountry = this.birthCountry != null ? this.birthCountry.getCountry() : null;
	}

	/**
	 * @return the healthInsuranceNumber
	 */
	public String getHealthInsuranceNumber() {
		return healthInsuranceNumber;
	}

	/**
	 * @param healthInsuranceNumber
	 *            the healthInsuranceNumber to set
	 */
	public void setHealthInsuranceNumber(String healthInsuranceNumber) {
		this.healthInsuranceNumber = healthInsuranceNumber;
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
	 * @return the numIBAN
	 */
	public String getNumIBAN() {
		return numIBAN;
	}

	/**
	 * @param numIBAN
	 *            the numIBAN to set
	 */
	public void setNumIBAN(String numIBAN) {
		this.numIBAN = numIBAN;
	}

	/**
	 * @return the numBIC
	 */
	public String getNumBic() {
		return numBic;
	}

	/**
	 * @param numBIC
	 *            the numBIC to set
	 */
	public void setNumBic(String numBIC) {
		this.numBic = numBIC;
	}

	/**
	 * @return the domiciliation
	 */
	public String getDomiciliation() {
		return domiciliation;
	}

	/**
	 * @param domiciliation
	 *            the domiciliation to set
	 */
	public void setDomiciliation(String domiciliation) {
		this.domiciliation = domiciliation;
	}

	/**
	 * @return the benefiaire
	 */
	public String getBeneficiaire() {
		return beneficiaire;
	}

	/**
	 * @param benefiaire
	 *            the benefiaire to set
	 */
	public void setBeneficiaire(String benefiaire) {
		this.beneficiaire = benefiaire;
	}

	/**
	 * @return the documents
	 */
	public List<IDDocument> getDocuments() {
		if (this.documents == null)
			this.documents = new ArrayList<IDDocument>();
		return documents;
	}

	/**
	 * @param documents
	 *            the documents to set
	 */
	public void setDocuments(List<IDDocument> documents) {
		this.documents = documents;
	}

	public String getNumCotisationSpectacle() {
		return numCotisationSpectacle;
	}

	public void setNumCotisationSpectacle(String numCotisationSpectacle) {
		this.numCotisationSpectacle = numCotisationSpectacle;
	}

	public XmlEntity getNationality() {
		return nationality;
	}

	public void setNationality(XmlEntity nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the number card press
	 */
	public String getNumCardPress() {
		return numCardPress;
	}

	/**
	 * @param numCardPress
	 *            the number card press to set
	 */
	public void setNumCardPress(String numCardPress) {
		this.numCardPress = numCardPress;
	}

	/**
	 * @return the persistentbirthCountry
	 */
	public String getPersistentbirthCountry() {
		return persistentbirthCountry;
	}

	/**
	 * @param persistentbirthCountry
	 *            the persistentbirthCountry to set
	 */
	public void setPersistentbirthCountry(String persistentbirthCountry) {
		this.persistentbirthCountry = persistentbirthCountry;
	}

	/**
	 * 
	 */
	@PostLoad
	private void onLoading() {
		if (this.persistentbirthCountry != null) {
			this.birthCountry = new Locale(null, this.persistentbirthCountry);
		}
	}

	/**
	 * Return the married name
	 */
	public String getMarriedName() {
		return marriedName;
	}

	/**
	 * Update the married name
	 */
	public void setMarriedName(String marriedName) {
		this.marriedName = marriedName;
	}

	/**
	 * Return the birth province
	 */
	public String getBirthProvince() {
		return birthProvince;
	}

	/**
	 * Update the birth province
	 */
	public void setBirthProvince(String birthProvince) {
		this.birthProvince = birthProvince;
	}
}
