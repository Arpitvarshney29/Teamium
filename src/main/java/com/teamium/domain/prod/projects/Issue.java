package com.teamium.domain.prod.projects;

import java.util.Calendar;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Describe an issue
 * @author dolivier - NovaRem
 * @since v7
 */
@Entity
@Table(name="t_issue")
public class Issue extends AbstractEntity {
	
	/**
	 * Id
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idIssue_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "issue_id_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idIssue_seq")
	private Long id;
	
	/**
	 * User
	 */
	@ManyToOne
	@JoinColumn(name="c_reporter")
	private StaffMember reporter;
	
	/**
	 * Date
	 */
	@Column(name="c_date")
	private Calendar date;
	
	/**
	 * Severity
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_severity"))
	private XmlEntity severity;
	
	/**
	 * Comment
	 */
	@Column(name="c_comment")
	private String comment;
	
	/**
	 * Booking
	 */
	@ManyToOne
	@JoinColumn(name="c_booking")
	private Booking booking;

	/**
	 * @return the reporter
	 */
	public StaffMember getReporter() {
		return reporter;
	}

	/**
	 * @param reporter the reporter to set
	 */
	public void setReporter(StaffMember reporter) {
		this.reporter = reporter;
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

	/**
	 * @return the severity
	 */
	public XmlEntity getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(XmlEntity severity) {
		this.severity = severity;
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
	 * @return the booking
	 */
	public Booking getBooking() {
		return booking;
	}

	/**
	 * @param booking the booking to set
	 */
	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	
	/**
	 * Return id
	 */
	@Override
	public Long getId() {
		return id;
	}
	
	/**
	 * Update id
	 */
	@Override
	public void setId(Long id) {
		this.id = id;		
	}
	
}
