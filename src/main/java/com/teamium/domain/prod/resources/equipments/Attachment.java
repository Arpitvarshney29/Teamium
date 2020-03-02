package com.teamium.domain.prod.resources.equipments;

import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * @author Teamium
 *
 */
@Entity
@Table(name = "t_attachment")
public class Attachment extends AbstractEntity {

	/**
	 * Auto genereted value
	 */
	private static final long serialVersionUID = -8558587314503558981L;
	@Id
	@Column(name = "c_idattachment", insertable = false, updatable = false)
	@TableGenerator(name = "idAttachment_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "equipment_idattachment_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idAttachment_seq")
	private Long id;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "c_type")
	private String type;

	@Column(name = "c_url")
	private String url;

	@Column(name = "c_file_name")
	private String fileName;

	@Column(name = "c_path")
	private String path;

	@Column(name = "c_feed_by_url")
	private boolean feedByUrl;

	@Column(name = "c_extension")
	private String extension;

	public Attachment() {

	}

	public Attachment(Attachment attachment) {
		this.type = attachment.getType();
		this.url = attachment.getUrl().toString();
		this.fileName = attachment.getFileName();
		this.path = attachment.getPath();
		this.feedByUrl = attachment.isFeedByUrl();
		this.extension = attachment.getExtension();
	}

	public Attachment(Long id, String type, String url, String fileName) {
		if (id != null) {
			this.id = id;
		}
		this.type = type;
		this.url = url;
		this.fileName = fileName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the feedByUrl
	 */
	public boolean isFeedByUrl() {
		return feedByUrl;
	}

	/**
	 * @param feedByUrl
	 *            the feedByUrl to set
	 */
	public void setFeedByUrl(boolean feedByUrl) {
		this.feedByUrl = feedByUrl;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attachment other = (Attachment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
