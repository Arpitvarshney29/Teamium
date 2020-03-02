package com.teamium.domain.prod;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Describe a comment
 * @author marjolaine
 *
 */
@Entity
@Table(name="t_comment")
public class Comment extends AbstractEntity{

	/**
	 * The generated UID
	 */
	private static final long serialVersionUID = 7130183572685384953L;
	
	/**
	 * The comment id
	 */
	@Id
	@Column(name="c_idcomment", insertable=false, updatable=false)
	@TableGenerator( name = "idcomment_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "comment_idcomment_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idcomment_seq")
	private Long id;
	
	/**
	 * The detail
	 */
	@Column(name="c_detail")
	private String detail;
	
	/**
	 * The author
	 */
	@ManyToOne
	@JoinColumn(name="c_author")
	private StaffMember author;
	
	/**
	 * The date
	 */
	@Column(name="c_date")
	private Calendar date;

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
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the author
	 */
	public StaffMember getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(StaffMember author) {
		this.author = author;
	}

	/**
	 * @return the date
	 */
	public Calendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
}
