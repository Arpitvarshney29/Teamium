package com.teamium.domain.prod.upload.log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

@Entity
@Table(name = "t_spreadsheet_upload_log")
@NamedQueries({
		@NamedQuery(name = SpreadsheetUploadLog.QUERY_findAllByImportFor, query = "SELECT s FROM SpreadsheetUploadLog s WHERE s.uploadedBy = ?1 AND s.importFor = ?2 ORDER BY s.id"), })
public class SpreadsheetUploadLog extends AbstractEntity {

	/**
	 * Query retrieving all SpreadsheetUploadLog with the given type and uploaded by
	 * 
	 * @param 1 the name of person to retrieve
	 * @param 2 the type of Spreadsheet to retrieve
	 * @return the list of all SpreadsheetUploadLog with the given type
	 */
	public static final String QUERY_findAllByImportFor = "findSpreadsheetUploadLogByuploadedByAndImportFor";

	/**
	 * Generated serial version id.
	 */
	private static final long serialVersionUID = 5582197897754596236L;

	@Id
	@Column(name = "c_idspreadsheetuploadlog", insertable = false, updatable = false)
	@TableGenerator(name = "idSpreadsheetUploadLog_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "spreadsheet_upload_log_iduploadlog_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idSpreadsheetUploadLog_seq")
	private Long id;

	@Column(name = "c_log_details")
	private String logDetails;

	@Column(name = "c_upload_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar uploadTime;

	@Column(name = "c_uploaded_by")
	private String uploadedBy;

	@Enumerated(EnumType.STRING)
	private ImportFor importFor;

	@Column(name = "c_document_type")
	private String documentType;

	@Column(name = "c_error")
	private boolean hasError;

	@Column(name = "c_staff_id")
	private Long staffId;

	@Column(name = "c_url")
	private String url;

	@Column(name = "c_file_name")
	private String fileName;

	@Column(name = "c_path")
	private String path;

	public SpreadsheetUploadLog() {

	}

	public SpreadsheetUploadLog(String logDetails, Calendar uploadTime, String uploadedBy, ImportFor importFor) {
		this.logDetails = logDetails;
		this.uploadTime = uploadTime;
		this.uploadedBy = uploadedBy;
		this.importFor = importFor;
	}

	public SpreadsheetUploadLog(String logDetails, Calendar uploadTime, String uploadedBy, ImportFor importFor,
			String documentType, boolean hasError) {
		this.logDetails = logDetails;
		this.uploadTime = uploadTime;
		this.uploadedBy = uploadedBy;
		this.importFor = importFor;
		this.documentType = documentType;
		this.hasError = hasError;
	}

	public SpreadsheetUploadLog(String logDetails, Calendar uploadTime, String uploadedBy, ImportFor importFor,
			String documentType, boolean hasError, Long staffId, String fileName, String path) {
		this.logDetails = logDetails;
		this.uploadTime = uploadTime;
		this.uploadedBy = uploadedBy;
		this.importFor = importFor;
		this.documentType = documentType;
		this.hasError = hasError;
		this.staffId = staffId;
		this.fileName = fileName;
		this.path = path;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the logDetails
	 */
	public String getLogDetails() {
		return logDetails;
	}

	/**
	 * @param logDetails the logDetails to set
	 */
	public void setLogDetails(String logDetails) {
		this.logDetails = logDetails;
	}

	/**
	 * @return the uploadTime
	 */
	public Calendar getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(Calendar uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}

	/**
	 * @param uploadedBy the uploadedBy to set
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	/**
	 * @return the importFor
	 */
	public ImportFor getImportFor() {
		return importFor;
	}

	/**
	 * @param importFor the importFor to set
	 */
	public void setImportFor(ImportFor importFor) {
		this.importFor = importFor;
	}

	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType the documentType to set
	 */
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return the hasError
	 */
	public boolean isHasError() {
		return hasError;
	}

	/**
	 * @param hasError the hasError to set
	 */
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}

	/**
	 * @return the staffId
	 */
	public Long getStaffId() {
		return staffId;
	}

	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(URL url) {
		if (url != null) {
			this.url = url.toString();
		} else {
			this.url = null;
		}
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

}
