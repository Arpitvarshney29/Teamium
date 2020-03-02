package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.prod.Record;

/**
 * Describe an entity class for channel and its format for a record
 * 
 * @author Teamium
 */
@Entity
@Table(name = "t_channel_format")
public class ChannelFormat {

	@Id
	@Column(name = "c_idchannelformat", insertable = false, updatable = false)
	@TableGenerator(name = "idChannelFormat_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "channelformat_idchannelformat_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idChannelFormat_seq")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "c_idchannel")
	private Channel channel;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_format_id", nullable = false)
	private Format format;

	public ChannelFormat() {
	}

	public ChannelFormat(Channel channel, Format format) {
		this.channel = channel;
		this.format = format;
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
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	// /**
	// * @return the record
	// */
	// public Record getRecord() {
	// return record;
	// }
	//
	// /**
	// * @param record
	// * the record to set
	// */
	// public void setRecord(Record record) {
	// this.record = record;
	// }

	/**
	 * @return the format
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ChannelFormat [id=" + id + ", channel=" + channel + ", format=" + format + "]";
	}

}
