package com.teamium.domain.prod.resources;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Document;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.Comment;

/**
 * Describe an asset
 * @author marjolaine
 *
 */
@Entity
@Table(name="t_asset")
@NamedQueries({
	@NamedQuery(name=Asset.QUERY_findAll, query ="SELECT a FROM Asset a"),
	@NamedQuery(name=Asset.QUERY_countAll, query ="SELECT count(a) FROM Asset a"),
	@NamedQuery(name=Asset.QUERY_findByIds, query ="SELECT a FROM Asset a WHERE a.id IN (?1)")
})
public class Asset extends AbstractEntity{

	/**
	 * Default OID
	 */
	private static final long serialVersionUID = 3900871493824684093L;
	
	/**
	 * The alias used in named query
	 */
	public static final String ALIAS = "a";
	
	/**
	 * Name of the query retrieving all assets
	 * @return the total count
	 */
	public static final String QUERY_findAll = "find_all_asset";
	
	/**
	 * Query string for retrieving all assets, that may be ordered by OrderByClause.getClause()
	 * @return the list of all assets
	 */
	public static final String QUERY_findAllOrdered = "SELECT a FROM Asset a";
	
	/**
	 * Name of the query counting all assets
	 * @return the total count
	 */
	public static final String QUERY_countAll = "count_all_asset";
	
	/**
	 * Name of the query finding assets by ids
	 * @return the total count
	 */
	public static final String QUERY_findByIds = "find_by_ids_asset";
	
	/**
	 * Query string to retrieve the assets matching the given list of ids, that may be ordered by OrderByClause.getClause()
	 * @param 1 the ids to search
	 * @return the list of matching assets
	 */
	public static final String QUERY_findOrderedByIds = "SELECT a.id FROM Asset a WHERE a.id IN (?1)";

	/**
	 * The asset ID
	 */
	@Id
	@Column(name="c_idasset", insertable=false, updatable=false)
	@TableGenerator( name = "idAsset_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "asset_idasset_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idAsset_seq")
	private Long id;
	
	/**
	 * The title
	 */
	@Column(name="c_title")
	private String title;

	/**
	 * The thumbnail
	 */
	@OneToOne(targetEntity=Document.class)
	@JoinColumn(name="c_thumbnail")
	private Document thumbnail;
	
	/**
	 * The guid
	 */
	@Column(name="c_guid")
	private String guid;
	
	/**
	 * The description
	 */
	@Column(name="c_description")
	private String description;
	
	/**
	 * The comments
	 */
	@ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(
			name="t_asset_comment",
			joinColumns=@JoinColumn(name="c_asset"),
			inverseJoinColumns=@JoinColumn(name="c_comment")
	)
	private List<Comment> comments;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
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
	 * @return the thumbnail
	 */
	public Document getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(Document thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		if(comments == null) 
			comments = new ArrayList<Comment>();
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	/**
	 * Add comment to the list of comments
	 * @param comment The comment
	 * @return True if the comment is correctly added
	 */
	public boolean addComment(Comment comment){
		if(comments == null){
			comments = new ArrayList<Comment>();
		}
		return this.comments.add(comment);
	}
	
	/**
	 * Remove comment to the list of comments
	 * @param comment The comment
	 * @return True if the comment is correctly removed
	 */
	public boolean removeComment(Comment comment){
		if(comments == null){
			comments = new ArrayList<Comment>();
		}
		return  this.comments.remove(comment);
	}
	

}
