package com.teamium.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.projects.Layout;

/**
 * Layout/Template Wrapper class
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LayoutDTO extends AbstractProjectDTO {

	public LayoutDTO() {

	}

	public LayoutDTO(long id, String category, String title) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
	}

	public LayoutDTO(long id, String category, String title, String status) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
	}

	public LayoutDTO(long id, String category, String title, String status, String recordDiscriminator) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
		this.setRecordDiscriminator(recordDiscriminator);
	}

	public LayoutDTO(long id, String category, String title, String status, String recordDiscriminator,
			long pendingBooking) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
		this.setRecordDiscriminator(recordDiscriminator);
	}

	public LayoutDTO(Layout layout) {
		super(layout);
	}
	
	public LayoutDTO(Layout layout, boolean customized) {
		if(customized) {
			this.setId(layout.getId());
			this.setTitle(layout.getTitle());
		}
	}

	@JsonIgnore
	public Layout getLayout(Layout layout, LayoutDTO layoutDTO) {
		setLayoutDetail(layout, layoutDTO);
		return layout;
	}

	@JsonIgnore
	public void setLayoutDetail(Layout layout, LayoutDTO layoutDTO) {
		layoutDTO.setAbstractProjectDetail(layout, layoutDTO);
	}

}
