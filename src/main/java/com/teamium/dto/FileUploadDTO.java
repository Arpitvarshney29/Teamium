package com.teamium.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.resources.equipments.Attachment;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FileUploadDTO {

	private MultipartFile fileContent;
	private String attachmentType;
	private String fileName;
	private String url;
	private String isFeedByUrl;
	private String discriminator;
	private String extension;

	/**
	 * Default FileUploadDTO constructor
	 */
	public FileUploadDTO() {
	}

	/**
	 * FileUploadDTO constructor
	 */
	public FileUploadDTO(Attachment attachment) {
		this.attachmentType = attachment.getType();
		this.fileName = attachment.getFileName();
		this.url = attachment.getUrl().toString();
		this.extension = attachment.getExtension();
	}

	/**
	 * Parameterized FileUploadDTO constructor
	 * 
	 * @param fileContent
	 * @param channelId
	 * @param scheduleDate
	 * @param fileName
	 */
	public FileUploadDTO(MultipartFile fileContent, String attachmentType, String scheduleDate, String fileName) {
		this.fileContent = fileContent;
		this.attachmentType = attachmentType;

		this.fileName = fileName;
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
	 * @return the fileContent
	 */
	public MultipartFile getFileContent() {
		return fileContent;
	}

	/**
	 * @param fileContent
	 *            the fileContent to set
	 */
	public void setFileContent(MultipartFile fileContent) {
		this.fileContent = fileContent;
	}

	/**
	 * @return the attachmentType
	 */
	public String getAttachmentType() {
		return attachmentType;
	}

	/**
	 * @param attachmentType
	 *            the attachmentType to set
	 */
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
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
	 * @return the isFeedByUrl
	 */
	public String getIsFeedByUrl() {
		return isFeedByUrl;
	}

	/**
	 * @param isFeedByUrl
	 *            the isFeedByUrl to set
	 */
	public void setIsFeedByUrl(String isFeedByUrl) {
		this.isFeedByUrl = isFeedByUrl;
	}

	/**
	 * @return the discriminator
	 */
	public String getDiscriminator() {
		return discriminator;
	}

	/**
	 * @param discriminator
	 *            the discriminator to set
	 */
	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
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

}
