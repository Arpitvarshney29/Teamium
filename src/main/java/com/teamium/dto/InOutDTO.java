package com.teamium.dto;

import java.net.MalformedURLException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.AbstractEntity;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.InOut;
import com.teamium.domain.prod.resources.InOutResource;
import com.teamium.domain.prod.resources.Media;

@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class InOutDTO extends AbstractDTO {

	private Long id;
	private MediaDTO media;
	private XmlEntity type;
	private String comment;
	private XmlEntity status;
	private InOutResource resource;

	public InOutDTO() {

	}

	public InOutDTO(AbstractEntity abstractEntity) {
		super(abstractEntity);
	}

	public InOutDTO(InOut inOut) {
		super(inOut);
		this.id = inOut.getId();
		this.type = inOut.getType();
		this.comment = inOut.getComment();
		this.status = inOut.getStatus();
		this.resource = inOut.getResource();
	}

	public InOutDTO(MediaDTO media, XmlEntity type, String comment, XmlEntity status, InOutResource resource) {
		this.media = media;
		this.type = type;
		this.comment = comment;
		this.status = status;
		this.resource = resource;
	}

	@JsonIgnore
	public InOut getInOut(InOut inOut, InOutDTO inOutDTO) throws MalformedURLException {
		setInOutDetail(inOut, inOutDTO);
		return inOut;
	}

	@JsonIgnore
	public void setInOutDetail(InOut inOut, InOutDTO inOutDTO) throws MalformedURLException {
		inOut.setVersion(inOutDTO.getVersion());
		inOut.setType(inOutDTO.getType());
		inOut.setComment(inOutDTO.getComment());
		inOut.setStatus(inOutDTO.getStatus());
		inOut.setResource(inOutDTO.getResource());
		MediaDTO mediaDTO = inOutDTO.getMedia();
		if (mediaDTO != null) {
			Media media = mediaDTO.getMedia(new Media(), mediaDTO);
			inOut.setMedia(media);
		}
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
	 * @return the media
	 */
	public MediaDTO getMedia() {
		return media;
	}

	/**
	 * @param media
	 *            the media to set
	 */
	public void setMediaDTO(MediaDTO media) {
		this.media = media;
	}

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the status
	 */
	public XmlEntity getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(XmlEntity status) {
		this.status = status;
	}

	/**
	 * @return the resource
	 */
	public InOutResource getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(InOutResource resource) {
		this.resource = resource;
	}

}
