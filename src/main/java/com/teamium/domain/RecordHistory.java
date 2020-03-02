package com.teamium.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.prod.Record;

/**
 * Entity class for maintaining the history of records visited by a particular
 * person
 */
@Entity
@Table(name = "t_record_history")
public class RecordHistory extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 417510913593740285L;

	@Id
	@Column(name = "c_idrecordhistory", insertable = false, updatable = false)
	@TableGenerator(name = "idRecordHistory_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "recordhistory_idrecordhistory_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idRecordHistory_seq")
	private Long id;

	@OneToOne
	@JoinColumn(name = "c_idrecord")
	private Record record;

	@Column(name = "c_record_discriminator")
	private String recordDiscriminator;

	@Column(name = "c_date_viewed")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateViewed;

	public RecordHistory() {

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the record
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * @param record
	 *            the record to set
	 */
	public void setRecord(Record record) {
		this.record = record;
	}

	/**
	 * @return the recordDiscriminator
	 */
	public String getRecordDiscriminator() {
		return recordDiscriminator;
	}

	/**
	 * @param recordDiscriminator
	 *            the recordDiscriminator to set
	 */
	public void setRecordDiscriminator(String recordDiscriminator) {
		this.recordDiscriminator = recordDiscriminator;
	}

	/**
	 * @return the dateViewed
	 */
	public Calendar getDateViewed() {
		return dateViewed;
	}

	/**
	 * @param dateViewed
	 *            the dateViewed to set
	 */
	public void setDateViewed(Calendar dateViewed) {
		this.dateViewed = dateViewed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dateViewed == null) ? 0 : dateViewed.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((record == null) ? 0 : record.hashCode());
		result = prime * result + ((recordDiscriminator == null) ? 0 : recordDiscriminator.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordHistory other = (RecordHistory) obj;
		if (dateViewed == null) {
			if (other.dateViewed != null)
				return false;
		} else if (!dateViewed.equals(other.dateViewed))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (record == null) {
			if (other.record != null)
				return false;
		} else if (!record.equals(other.record))
			return false;
		if (recordDiscriminator == null) {
			if (other.recordDiscriminator != null)
				return false;
		} else if (!recordDiscriminator.equals(other.recordDiscriminator))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecordHistory [id=" + id + ", record=" + record + ", recordDiscriminator=" + recordDiscriminator
				+ ", dateViewed=" + dateViewed + "]";
	}

}
