package com.teamium.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamium.domain.Channel;
import com.teamium.domain.Company;
import com.teamium.domain.Format;

/**
 * DTO Class for Channel Entity
 * 
 * @author Nishant Chauhan
 *
 */
public class ChannelDTO extends CompanyDTO {

	private Set<Format> formats = new HashSet<>();

	public ChannelDTO() {
	}

	public ChannelDTO(Channel channel) {
		super(channel);
	}

	public ChannelDTO(Company channel, String forChannel) {
		this.setId(channel.getId());
		this.setName(channel.getName());
		this.setFormats(((Channel)channel).getFormats());
	}

	/**
	 * Get Channel Entity from DTO
	 * 
	 * @param channel
	 * @return Channel
	 */
	@JsonIgnore
	public Channel getChannel(Channel channel) {
		super.getCompanyDetails(channel);
		return channel;
	}

	/**
	 * Get Channel Entity from DTO
	 * 
	 * @param channel
	 * @return Channel
	 */
	@JsonIgnore
	public Channel getChannel() {
		return this.getChannel(new Channel());
	}

	/**
	 * @return the formats
	 */
	public Set<Format> getFormats() {
		return formats;
	}

	/**
	 * @param formats the formats to set
	 */
	public void setFormats(Set<Format> formats) {
		this.formats = formats;
	}

}
