/**
 * 
 */
package com.teamium.domain;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;


/**
 * Describe an external document or file
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name="t_document")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="c_discriminator", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue(Document.TYPE)
public class Document extends AbstractEntity {
	
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6088523223799003161L;
	
	/**
	 * The standard document discriminator type
	 */
	public static final String TYPE = "document";
	
	/**
	 * This query string permits to change a staff member to supervisor in one way or an other
	 * @param staff|supervisor the final status to give
	 * @param the id of the staff member to set
	 */
	public static final String QUERY_nativeUpdateDocumentType = "UPDATE t_document SET c_version = c_version + 1, c_discriminator = ? WHERE c_iddocument = ?";

	
	/**
	 * document root url
	 */
	public static String DOCUMENT_ROOT_URL = "";
	
	/**
	 * Document ID
	 */
	@Id
	@Column(name="c_iddocument", insertable=false, updatable=false)
	@TableGenerator( name = "idDocument_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "document_iddocument_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idDocument_seq")
	private Long id;
	
	/**
	 * Document name
	 */
	@Column(name="c_name")
	private String name;
	
	/**
	 * Document comments
	 */
	@Column(name="c_comments")
	private String comments;
	
	/**
	 * Document keywords
	 */
	@ElementCollection(targetClass = java.lang.String.class, fetch=FetchType.LAZY)
    @Column(name="c_keyword")
    @CollectionTable(name="t_document_keyword", joinColumns=@JoinColumn(name="c_iddocument"))
	private List<String> keywords;
	
	/**
	 * Document type
	 * @see com.teamium.domain.prod.DocumentType
	 */
    @Column(name="c_documenttype")
	private String type;
	
	/**
	 * Document url (absolute or relative from documents server root)
	 */
    @Column(name="c_path")
	private String path;
    
	/**
	 * Effective document url
	 */
    @Column(name="c_url")
	private String url;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the absolute or relative path of the linked file
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the absolute or relative path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return the url
	 */
	public URL getUrl() {
		try {
			return  new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		if(url != null){
			this.url = url.toString();
		}else{
			this.url = null;
		}
	}

	/**
	 * Textual representation of document
	 * @return The textual representation
	 */
	@Override
	public String toString(){
		return super.toString() + " " + this.path;
	}
}
