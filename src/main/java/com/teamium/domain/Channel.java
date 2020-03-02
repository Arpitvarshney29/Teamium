package com.teamium.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

/**
 * Describe a company
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("channel")
@NamedQuery(name = Channel.QUERY_findAll, query = "SELECT c From Channel c ORDER BY c.name ASC")
public class Channel extends Company {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6208640436475044828L;

	/**
	 * Name of the query returning all recorded channels
	 */
	public static final String QUERY_findAll = "findAllChannels";

	/**
	 * Format
	 * 
	 * @see com.teamium.domain.Format
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "t_channel_format_config", joinColumns = @JoinColumn(name = "c_idcompany"), inverseJoinColumns = @JoinColumn(name = "c_format_id"))
	private Set<Format> formats = new HashSet<>();

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
