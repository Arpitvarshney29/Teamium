package com.teamium.dto;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Document;
import com.teamium.utils.FileReader;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DocumentDTO extends AbstractDTO {

	private Long id;
	private String name;
	private String comments;
	private List<String> keywords;
	private String type;
	private String path;
	private String url;
	private byte[] picture;

	public DocumentDTO() {
	}

	public DocumentDTO(AbstractEntity entity) {
		super(entity);
	}

	public DocumentDTO(Document document) {
		super(document);
		this.id = document.getId();
		this.name = document.getName();
		this.comments = document.getComments();
		this.keywords = document.getKeywords();
		this.type = document.getType();
		this.path = document.getPath();
		if (document.getUrl() != null) {
			this.url = document.getUrl().toString();
		}
		try {
		} catch (Exception e) {
			this.picture = null;
		}

	}

	public DocumentDTO(String name, String comments, List<String> keywords, String type, String path, String url) {
		this.name = name;
		this.comments = comments;
		this.keywords = keywords;
		this.type = type;
		this.path = path;
		this.url = url + "/" + path;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the picture
	 */
	public byte[] getPicture() {
		return picture;
	}

	/**
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	/**
	 * To get Document
	 * 
	 * @return Document object
	 */
	@JsonIgnore
	public Document getDocument() {
		Document document = new Document();
		setDocumentDetail(document);
		return document;
	}

	/**
	 * to set document detail
	 * 
	 * @param document
	 *            the document
	 */
	public void setDocumentDetail(Document document) {
		document.setId(this.id);
		document.setVersion(this.getVersion());
		document.setName(this.getName());
		document.setComments(this.getComments());
		document.setKeywords(this.keywords);
		document.setType(this.getType());
		document.setPath(this.path);
		String url = this.getUrl();
		if (url != null && !url.isEmpty()) {
			try {
				document.setUrl(new URL(url));
			} catch (MalformedURLException e) {
				document.setUrl(null);
			}
		}
	}

}
