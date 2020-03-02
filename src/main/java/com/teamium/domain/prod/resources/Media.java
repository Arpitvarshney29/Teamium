package com.teamium.domain.prod.resources;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.Document;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;

/**
 * Describe a media used in an asset
 * @author marjolaine
 * @author sraybaud @version TEAM-18
 *
 */
@Entity
@Table(name="t_media")
@NamedQueries({
	@NamedQuery(name=Media.QUERY_findAll, query ="SELECT m FROM Media m"),
	@NamedQuery(name=Media.QUERY_countAll, query ="SELECT count(m) FROM Media m"),
	@NamedQuery(name=Media.QUERY_findByIds, query ="SELECT m FROM Media m WHERE m.id IN (?1)"),
	@NamedQuery(name=Media.FIND_BY_KEYWORD, query="SELECT m FROM Media m WHERE LOWER(m.asset.title) LIKE ?1"),
})
public class Media extends AbstractEntity {

	/**
	 * Generated OID
	 */
	private static final long serialVersionUID = 5357881432201924196L;
	
	/**
	 * The alias used in named query
	 */
	public static final String ALIAS = "m";
	
	/**
	 * Name of the query retrieving all medias
	 * @return the total count
	 */
	public static final String QUERY_findAll = "find_all_media";
	
	/**
	 * Query string for retrieving all medias, that may be ordered by OrderByClause.getClause()
	 * @return the list of all medias
	 */
	public static final String QUERY_findAllOrdered = "SELECT m FROM Media m";
	
	/**
	 * Name of the query counting all medias
	 * @return the total count
	 */
	public static final String QUERY_countAll = "count_all_media";
	
	/**
	 * Name of the query finding medias by ids
	 * @return the total count
	 */
	public static final String QUERY_findByIds = "find_by_ids_media";
	
	/**
	 * The named query used to find equipment matching the keyword given in parameter
	 * @param 1 keyword
	 */
	public static final String FIND_BY_KEYWORD = "find_media_by_keyword";
	
	/**
	 * Query string to retrieve the medias matching the given list of ids, that may be ordered by OrderByClause.getClause()
	 * @param 1 the ids to search
	 * @return the list of matching medias
	 */
	public static final String QUERY_findOrderedByIds = "SELECT m.id FROM Media m WHERE m.id IN (?1)";


	/**
	 * The media Id
	 */
	@Id
	@Column(name="c_idmedia", insertable=false, updatable=false)
	@TableGenerator( name = "idMedia_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "media_idmedia_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idMedia_seq")
	private Long id;
	
	/**
	 * Information about the max lenght
	 */
	@Column(name="c_length")
	private String length; 
	
	/**
	 * the label
	 */
	@Column(name="c_libelle")
	private String label; 
	
	/**
	 * Information about the format
	 * @see com.teamium.domain.prod.resources.MediaFormat
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_format"))
	private XmlEntity format;
	
	/**
	 * CSA
	 * @see com.teamium.domain.prod.projects.CSA
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_csa"))
	private XmlEntity csa;
	
	/**
	 * The document file
	 */
	@OneToOne(targetEntity=Document.class)
	@JoinColumn(name="c_file")
	private Document file;
	
	/**
	 * The asset
	 */
	@ManyToOne
	@JoinColumn(name="c_asset")
	private Asset asset;
	
	/**
	 * Localization
	 * @see com.teamium.domain.prod.resources
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_localization"))
	private XmlEntity localization;
	
	/**
	 * The umid number
	 */
	@Column(name="c_umid")
	private String umid;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the format
	 */
	public XmlEntity getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(XmlEntity format) {
		this.format = format;
	}

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return file;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(Document file) {
		this.file = file;
	}

	/**
	 * @return the umid
	 */
	public String getUmid() {
		return umid;
	}

	/**
	 * @param umid the umid to set
	 */
	public void setUmid(String umid) {
		this.umid = umid;
	}

	/**
	 * @return the csa
	 */
	public XmlEntity getCsa() {
		return csa;
	}

	/**
	 * @param csa the csa to set
	 */
	public void setCsa(XmlEntity csa) {
		this.csa = csa;
	}

	/**
	 * @return the asset
	 */
	public Asset getAsset() {
		return asset;
	}

	/**
	 * @param asset the asset to set
	 */
	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	/**
	 * @return the localization
	 */
	public XmlEntity getLocalization() {
		return localization;
	}

	/**
	 * @param localization the localization to set
	 */
	public void setLocalization(XmlEntity localization) {
		this.localization = localization;
	}
	
}
