/**
 * 
 */
package com.teamium.domain.prod;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.MilestoneType;
import com.teamium.domain.TeamiumConstants;

/**
 * Describes a due date for a project
 * 
 * @author sraybaud
 * @version TEAM-18
 *
 */
@Entity
@Table(name = "t_record_duedate")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "c_discriminator")
@DiscriminatorValue("duedate")
public class DueDate implements Cloneable {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 1044459431675283008L;

	/**
	 * Due date ID
	 */
	@Id
	@Column(name = "c_idduedate", insertable = false, updatable = false)
	@TableGenerator(name = "idDueDate_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "record_duedate_idduedate_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idDueDate_seq")
	private Long id;

	/**
	 * Milestone type
	 * 
	 * @see com.teamium.domain.prod.projects
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "c_milestone_type_id", nullable = false)
	private MilestoneType type;

	/**
	 * Date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "c_duedate")
	private Calendar date;

	/**
	 * The project Added by DO
	 */
	@ManyToOne
	@JoinColumn(name = "c_idrecord")
	private Record record;

	public DueDate() {

	}

	public DueDate(DueDate dueDate) {
		this.type = dueDate.getType();
		this.date = dueDate.getDate();
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
	 * @return the type
	 */
	public MilestoneType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(MilestoneType type) {
		this.type = type;
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
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param case the case to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * Compare the current object with the given one
	 * 
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
//	@Override
//	public int compareTo(DueDate o) {
//		if (o == null)
//			return 1;
//		if (this.type == null) {
//			if (o.type != null) {
//				return -1;
//			}
//		} else {
//			int compare = this.type.compareTo(o.type);
//			if (compare != 0)
//				return compare;
//		}
//		return 0;
//	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof DueDate))
			return false;
		DueDate other = (DueDate) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public DueDate clone() throws CloneNotSupportedException {
		DueDate clone = null;
		Long id = this.id;
		this.id = null;
		MilestoneType type = this.type;
		this.type = null;
		try {
			clone = (DueDate) super.clone();
			clone.type = type;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.id = id;
			this.type = type;
		}
	}

}
