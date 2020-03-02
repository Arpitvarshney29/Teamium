package com.teamium.dto;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teamium.domain.prod.resources.staff.Reel;

/**
 * Wrapper class for Reel Entity
 *
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReelDTO extends FileUploadDTO {

	private String id;

	private String type;

	private String title;

	private String bio;

	private byte[] file;

	private DateTime updatedOn;

	public ReelDTO() {

	}

	/**
	 * @param id
	 */
	public ReelDTO(Reel reel) {
		this.id = reel.getId().toString();
		this.title = reel.getTitle();
		this.bio = reel.getBio();
		this.type = reel.getType();
		this.updatedOn = reel.getUpdatedOn();
		
			this.setUrl(reel.getUrl());
		
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the bio
	 */
	public String getBio() {
		return bio;
	}

	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the updatedOn
	 */
	public DateTime getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * @param updatedOn the updatedOn to set
	 */
	public void setUpdatedOn(DateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * To get Reel entity from DTO
	 * 
	 * @param reel
	 * @return Reel
	 */
	@JsonIgnore
	public Reel getReel(Reel reel) {
		if (reel == null) {
			reel = new Reel();
		}
		reel.setBio(bio);
		if (StringUtils.isNoneBlank(id)) {
			reel.setId(Long.valueOf(id));
		}
		reel.setTitle(title);
		return reel;
	}

	/**
	 * @return the file
	 */
	public byte[] getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(byte[] file) {
		this.file = file;
	}

}
