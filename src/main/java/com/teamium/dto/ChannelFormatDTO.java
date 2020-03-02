package com.teamium.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.Channel;
import com.teamium.domain.ChannelFormat;
import com.teamium.utils.CommonUtil;

/**
 * Wrapper Class for Channel-Format Entity
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ChannelFormatDTO {

	private Long id;
	private ChannelDTO channel;
	private String format;

	public ChannelFormatDTO() {
	}

	public ChannelFormatDTO(ChannelFormat entity) {
		this.id = entity.getId();
		if (entity.getChannel() != null) {
			this.channel = new ChannelDTO(entity.getChannel(), null);
		}
		if(entity.getFormat() != null) {
			this.format = entity.getFormat().getName();
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
	 * @return the channel
	 */
	public ChannelDTO getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(ChannelDTO channel) {
		this.channel = channel;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * To get channel-Format
	 * 
	 * @param channelFormat
	 *            the channelFormat
	 * 
	 * @return the channelFormat object
	 */
	public ChannelFormat getChannelFormat(ChannelFormat channelFormat) {
		setChannelFormat(channelFormat);
		return channelFormat;
	}

	/**
	 * To set channel-format
	 * 
	 * @param channelFormat
	 *            the channelFormat
	 */
	public void setChannelFormat(ChannelFormat channelFormat) {
		channelFormat.setId(this.getId());
		if (this.channel != null) {
			Channel channel = this.channel.getChannel(new Channel());
			channelFormat.setChannel(channel);
		}
	}

}
