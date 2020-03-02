package com.teamium.domain.prod.docsign;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.staff.StaffMember;

@Entity
@Table(name = "t_document_gerenation")
@NamedQueries({
		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_findAllDocuments, query = "SELECT document FROM DocumentGeneration document order by document.createdOn"),
		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_findDocumentById, query = "SELECT document FROM DocumentGeneration document WHERE document.id=?1"),
		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_findByPersonAuthorizedToSign, query = "SELECT document FROM DocumentGeneration document join document.signaturePersons srp join srp.signPerson sr WHERE sr=?1 order by document.createdOn"),
		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_findByDateCreated, query = "SELECT document FROM DocumentGeneration document WHERE cast(document.createdOn as date)=?1 order by document.createdOn"),

		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_findByPersonAuthorizedToSignAndFlagNumber, query = "SELECT document FROM DocumentGeneration document join document.signaturePersons srp join srp.signPerson sr WHERE sr=?1 AND srp.flagNumber=?2"),
		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_fullySignedDocument, query = "SELECT document FROM DocumentGeneration document WHERE document.completelySigned=?1 order by document.createdOn"),

		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_findDocumentCreatedByDateBetween, query = "SELECT document FROM DocumentGeneration document WHERE document.createdOn>=?1 and document.createdOn<=?2 order by document.createdOn"),
		@NamedQuery(name = DocumentGeneration.DOCUMENTGENERATION_findByPersonAuthorizedToSignAndUpdatedByFieldNotNull, query = "SELECT document FROM DocumentGeneration document join document.signaturePersons srp join srp.signPerson sr WHERE sr=?1 AND document.updatedBy IS NOT NULL order by document.createdOn") })
public class DocumentGeneration extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5454636684296129964L;

	/**
	 * Name of the query returning all documents generated.
	 */
	public static final String DOCUMENTGENERATION_findAllDocuments = "findAllDocuments";

	/**
	 * Name of the query returning the document.
	 */
	public static final String DOCUMENTGENERATION_findDocumentById = "findDocumentById";

	/**
	 * Name of the query returning all documents generated corresponding to
	 * particular user who have to sign it.
	 */
	public static final String DOCUMENTGENERATION_findByPersonAuthorizedToSign = "findByPersonAuthorizedToSignDocument";

	/**
	 * Name of the query returning all documents generated on date.
	 */
	public static final String DOCUMENTGENERATION_findByDateCreated = "findByDateGenerated";

	/**
	 * Name of the query returning all documents either signed by a particular
	 * person
	 */
	public static final String DOCUMENTGENERATION_findByPersonAuthorizedToSignAndFlagNumber = "DOCUMENTGENERATION_findByPersonAuthorizedToSignAndFlagNumber";

	/**
	 * Name of the query returning documents completely signed
	 * 
	 */
	public static final String DOCUMENTGENERATION_fullySignedDocument = "findByCompletelySignedDocuments";

	/**
	 * Name of the query returning documents assigned to person to sign and
	 * updatedBy field not null.
	 * 
	 * @param from
	 *            the start date
	 * @param to
	 *            the end
	 * 
	 */
	public static final String DOCUMENTGENERATION_findByPersonAuthorizedToSignAndUpdatedByFieldNotNull = "findDocumentByPersonAuthorizedToSignAndUpdatedByFieldNotNull";

	/**
	 * Name of the query returning documents created between dates.
	 * 
	 */
	public static final String DOCUMENTGENERATION_findDocumentCreatedByDateBetween = "findDocumentByDateBetween";

	@Id
	@Column(name = "c_iddocumentgerenation", insertable = false, updatable = false)
	@TableGenerator(name = "idDocumentGeneration_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "document_generation_idgerenation_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idDocumentGeneration_seq")
	private Long id;

	@Column(name = "c_document_path")
	private String documentPath;

	@Column(name = "c_created_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar createdOn;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_created_by")
	private StaffMember createdBy;

	@Column(name = "c_updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedOn;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "c_updated_by")
	private StaffMember updatedBy;

	@Column(name = "c_completely_signed")
	private boolean completelySigned;

	@Column(name = "c_total_required_signatures")
	private int totalSignaturesRequired;

	@Column(name = "c_edition_xsl")
	private String editionXsl;

	@Column(name = "c_edition_key")
	private String editionKey;

	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_idsignature_person", nullable = true)
	private List<SignaturePerson> signaturePersons = new ArrayList<>();

	public DocumentGeneration() {
	}

	public DocumentGeneration(String documentPath, Calendar createdOn, StaffMember createdBy, boolean completelySigned,
			String editionXsl, String editionKey) {
		this.documentPath = documentPath;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.completelySigned = completelySigned;
		this.editionXsl = editionXsl;
		this.editionKey = editionKey;
	}

	public DocumentGeneration(String documentPath, Calendar createdOn, StaffMember createdBy, boolean completelySigned,
			int totalSignaturesRequired, String editionXsl, String editionKey) {
		this.documentPath = documentPath;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.completelySigned = completelySigned;
		this.totalSignaturesRequired = totalSignaturesRequired;
		this.editionXsl = editionXsl;
		this.editionKey = editionKey;
	}

	public DocumentGeneration(String documentPath, Calendar createdOn, StaffMember createdBy, boolean completelySigned,
			int totalSignaturesRequired, String editionXsl, String editionKey, List<SignaturePerson> signaturePersons) {
		this.documentPath = documentPath;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.completelySigned = completelySigned;
		this.totalSignaturesRequired = totalSignaturesRequired;
		this.editionXsl = editionXsl;
		this.editionKey = editionKey;
		this.signaturePersons = signaturePersons;
	}

	public DocumentGeneration(String documentPath, Calendar createdOn, StaffMember createdBy, Calendar updatedOn,
			StaffMember updatedBy, boolean completelySigned, int totalSignaturesRequired, String editionXsl,
			String editionKey, List<SignaturePerson> signaturePersons) {
		this.documentPath = documentPath;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
		this.completelySigned = completelySigned;
		this.totalSignaturesRequired = totalSignaturesRequired;
		this.editionXsl = editionXsl;
		this.editionKey = editionKey;
		this.signaturePersons = signaturePersons;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the documentPath
	 */
	public String getDocumentPath() {
		return documentPath;
	}

	/**
	 * @param documentPath
	 *            the documentPath to set
	 */
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	/**
	 * @return the createdOn
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Calendar createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the createdBy
	 */
	public StaffMember getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(StaffMember createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updatedOn
	 */
	public Calendar getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * @param updatedOn
	 *            the updatedOn to set
	 */
	public void setUpdatedOn(Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * @return the updatedBy
	 */
	public StaffMember getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy
	 *            the updatedBy to set
	 */
	public void setUpdatedBy(StaffMember updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the completelySigned
	 */
	public boolean isCompletelySigned() {
		return completelySigned;
	}

	/**
	 * @param completelySigned
	 *            the completelySigned to set
	 */
	public void setCompletelySigned(boolean completelySigned) {
		this.completelySigned = completelySigned;
	}

	/**
	 * @return the totalRequiredSignatures
	 */
	public int getTotalSignaturesRequired() {
		return totalSignaturesRequired;
	}

	/**
	 * @param totalRequiredSignatures
	 *            the totalRequiredSignatures to set
	 */
	public void setTotalSignaturesRequired(int totalSignaturesRequired) {
		this.totalSignaturesRequired = totalSignaturesRequired;
	}

	/**
	 * @return the editionXsl
	 */
	public String getEditionXsl() {
		return editionXsl;
	}

	/**
	 * @param editionXsl
	 *            the editionXsl to set
	 */
	public void setEditionXsl(String editionXsl) {
		this.editionXsl = editionXsl;
	}

	/**
	 * @return the editionKey
	 */
	public String getEditionKey() {
		return editionKey;
	}

	/**
	 * @param editionKey
	 *            the editionKey to set
	 */
	public void setEditionKey(String editionKey) {
		this.editionKey = editionKey;
	}

	/**
	 * @return the signaturePersons
	 */
	public List<SignaturePerson> getSignaturePersons() {
		return signaturePersons;
	}

	/**
	 * @param signaturePersons
	 *            the signaturePersons to set
	 */
	public void setSignaturePersons(List<SignaturePerson> signaturePersons) {
		this.signaturePersons = signaturePersons;
	}

}
