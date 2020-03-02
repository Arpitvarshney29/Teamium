/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.text.DateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * @author slopes
 * Used to define if a booking is updated or deleted
 *
 */
@Entity
@Table(name="t_booking_status")
@NamedQueries({
	@NamedQuery(name=BookingStatus.FIND_BY_REFERENCE, query="SELECT bs FROM BookingStatus bs WHERE bs.reference = ?1"),
	@NamedQuery(name=BookingStatus.FIND_BY_DATE, query="SELECT bs FROM BookingStatus bs WHERE bs.updatedAt >= ?1"),
	
})
public class BookingStatus extends AbstractEntity {
	/**
	 * The generated UID
	 */
	private static final long serialVersionUID = -355548259561686937L;
	
	/**
	 * The named query to find booking status by the reference given in parameter
	 * @param 1 The reference (id of the booking)
	 */
	public static final String FIND_BY_REFERENCE = "find_booking_status_by_reference";
	
	/**
	 * The named query to find booking status after the date given in parameter
	 * @param 1 The from date
	 */
	public static final String FIND_BY_DATE = "find_booking_status_by_date";
	
	private static final DateFormat DF = DateFormat.getDateInstance(DateFormat.MEDIUM);

	/**
	 * The booking status id
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idBookingStatus_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "booking_status", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idBookingStatus_seq")
	private Long id;
	
	/**
	 * The referenced booking
	 */
	@Column(name="c_reference")
	private Long reference;
	
	/**
	 * The updated date
	 */
	@Column(name="c_updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updatedAt;
	
	/**
	 * The type
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="c_type")
	private BookingStatusType type;

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
	 * @return the reference
	 */
	public Long getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(Long reference) {
		this.reference = reference;
	}

	/**
	 * @return the updatedAt
	 */
	public Calendar getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Calendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the type
	 */
	public BookingStatusType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(BookingStatusType type) {
		this.type = type;
	}
	
	/**
	 * Textual representation of the instance
	 * @return The textual representation
	 */
	public String toString(){
		return super.toString() + " for the booking n°" + this.getReference() + " updated at " + this.formattedUpdatedAt() + " " + this.getType();
	}
	
	/**
	 * Format the date used by the to string method
	 * @return The formatted update at calendar value
	 */
	private String formattedUpdatedAt(){
		String updatedAt = null;
		if(this.getUpdatedAt() != null){
			updatedAt = BookingStatus.DF.format(this.getUpdatedAt().getTime());
		}
		return updatedAt;
	}
	
	
}
