package com.teamium.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * Reference files in data directory
 * @author slopes - NovaRem
 *
 */
@Entity
@NamedQuery(name = TeamiumFile.QUERY_findTeamiumFileWithoutDocument, query = "SELECT tf FROM TeamiumFile tf WHERE tf.document IS NULL")
@Table(name="t_document_file")
public class TeamiumFile {
	
	public static final String QUERY_findTeamiumFileWithoutDocument = "QUERY_findTeamiumFileWithoutDocument";
	
	/**
	 * TeamiumFile ID
	 */
	@Id
	@Column(name="c_idfile", insertable=false, updatable=false)
	@TableGenerator( name = "idFile_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "document_file_idfile_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idFile_seq")
	private int id;
	/**
	 * Relative path of the file in server directory
	 */
	@Column(name="c_filepath")
	private String path;
	/**
	 * Reference of the document object
	 */
	@OneToOne (fetch = FetchType.EAGER)
	@JoinColumn(name="c_iddocument", nullable= true)
	private Document document;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}
	/**
	 * @param document the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
	}
	

}