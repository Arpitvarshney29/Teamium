package com.teamium.domain.prod.resources;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;

/**
 * Describe an InOut used in a project
 * @author marjolaine
 *
 */
@Entity
@Table(name="t_inout")
@NamedQueries({
	@NamedQuery(name=InOut.QUERY_findAll, query ="SELECT i FROM InOut i"),
	@NamedQuery(name=InOut.QUERY_countAll, query ="SELECT count(i) FROM InOut i"),
	@NamedQuery(name=InOut.QUERY_findByIds, query ="SELECT i FROM InOut i WHERE i.id IN (?1)")
})
public class InOut extends AbstractEntity implements ResourceEntity{
	
	/**
	 * The generated UID
	 */
	private static final long serialVersionUID = -6288056865843547844L;
	
	/**
	 * The alias used in named query
	 */
	public static final String ALIAS = "i";
	
	/**
	 * Name of the query retrieving all inOuts
	 * @return the total count
	 */
	public static final String QUERY_findAll = "find_all_inout";
	
	/**
	 * Query string for retrieving all inOuts, that may be ordered by OrderByClause.getClause()
	 * @return the list of all inOuts
	 */
	public static final String QUERY_findAllOrdered = "SELECT i FROM InOut i";
	
	/**
	 * Name of the query counting all inOuts
	 * @return the total count
	 */
	public static final String QUERY_countAll = "count_all_inout";
	
	/**
	 * Name of the query finding inOuts by ids
	 * @return the total count
	 */
	public static final String QUERY_findByIds = "find_by_ids_inout";
	
	/**
	 * Query string to retrieve the inOuts matching the given list of ids, that may be ordered by OrderByClause.getClause()
	 * @param 1 the ids to search
	 * @return the list of matching inOuts
	 */
	public static final String QUERY_findOrderedByIds = "SELECT i.id FROM InOut i WHERE i.id IN (?1)";
	
	/**
	 * The InOut id
	 */
	@Id
	@Column(name="c_idinout", insertable=false, updatable=false)
	@TableGenerator( name = "idinout_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "inout_idinout_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idinout_seq")
	private Long id;
	
	/**
	 * The media
	 */
	@ManyToOne
	@JoinColumn(name="c_media")
	private Media media;
	
	/**
	 * The type
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_inout_type"))
	private XmlEntity type;
	
	/**
	 * The comment
	 */
	@Column(name="c_comment")
	private String comment;
	
	/**
	 * The status
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_inout_status"))
	private XmlEntity status;
	
	/**
	 * The resource
	 */
	@OneToOne(mappedBy="entity",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private InOutResource resource;

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
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type the type to set
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
	 * @param comment the comment to set
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
	 * @param status the status to set
	 */
	public void setStatus(XmlEntity status) {
		this.status = status;
	}

	/**
	 * @return the resource
	 */
	public InOutResource getResource() {
		if(this.resource==null){
			this.resource = new InOutResource();
			this.resource.setEntity(this);
		}
		return this.resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(InOutResource resource) {
		this.resource = resource;
	}

	/**
	 * Update resource name before update
	 */
	@PrePersist
	@PreUpdate
	private void beforeUpdate(){
		if(this.getResource()!=null && this.getMedia().getAsset().getTitle() != null){
			this.getResource().setName( this.getMedia().getAsset().getTitle().toUpperCase());
		}
	}

}
