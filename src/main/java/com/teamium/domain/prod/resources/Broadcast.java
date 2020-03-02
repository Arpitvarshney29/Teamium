package com.teamium.domain.prod.resources;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Channel;
import com.teamium.domain.TeamiumConstants;

@Entity
public class Broadcast extends AbstractEntity{

	/**
	 * Generated OID
	 */
	private static final long serialVersionUID = 1309789479124889734L;

	/**
	 * ID
	 */
	@Id
	@Column(name="c_idbraodcast", insertable=false, updatable=false)
	@TableGenerator( name = "idBroadcast_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "broadcast_idbroadcast_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idBroadcast_seq")
	private Long id;
	
	/**
	 * The channel which braodcast
	 */
	@ManyToOne
	@JoinColumn(name="c_idchannel")
	private Channel channel;
	
	/**
	 * the broadcasted media
	 */
	@ManyToOne
	@JoinColumn(name="c_idmedia")
	private Media media;
	
	/**
	 * The air date of the braodcast
	 */
	@Column(name="c_date")
	@Temporal(value = TemporalType.TIMESTAMP)
	private Calendar airDate;
	
	/**
	 * The title of the braodcast
	 */
	@Column(name="c_title")
	private String title;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
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

	/**
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media the media to set
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * @return the airDate
	 */
	public Calendar getAirDate() {
		return airDate;
	}

	/**
	 * @param airDate the airDate to set
	 */
	public void setAirDate(Calendar airDate) {
		this.airDate = airDate;
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

	
	
}
