package com.teamium.domain.prod.projects;

import java.util.Calendar;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.projects.planning.Event;

/**
 * Describe a booking combination [SELECT bc FROM com.teamium.domain.prod.projects.BookingCombination bc WHERE bc.update >= ?1]
 * @author marjolaine
 *
 * @param <T>
 */
@Entity
@Table(name="t_booking_combination")
@NamedQueries({
	@NamedQuery(name=BookingCombination.QUERY_findByBooking, query="SELECT bc FROM BookingCombination bc WHERE ?1 MEMBER OF bc.bookings"),
	@NamedQuery(name=BookingCombination.QUERY_findByUpdate, query="SELECT bc FROM BookingCombination bc WHERE bc.update_ts >= ?1"),
})
@DiscriminatorColumn(name="c_discriminator", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("combination")
public abstract class BookingCombination<T> extends AbstractEntity {
	/**
	 * The generated UID
	 */
	private static final long serialVersionUID = 7508860581168280499L;
	
	/**
	 * Find the booking combination matching booking given in parameter
	 * @param 1 The booking
	 */
	public static final String QUERY_findByBooking = "find_booking_combination_by_booking";
	
	/**
	 * Find the booking combination matching the update given in parameter
	 * @param 1 The timestamp ( calendar )
	 */
	public static final String QUERY_findByUpdate = "find_booking_combination_by_udpdate";
	
	/**
	 * The booking combination id
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idBookingCombination_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "booking_combination", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idBookingCombination_seq")
	private Long id;
	
	/**
	 * The bookings
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
		name="t_booking_bookingcombination",
		joinColumns=@JoinColumn(name="c_booking_combination"),
		inverseJoinColumns=@JoinColumn(name="c_id")
	)
	private List<Event> bookings;
	
	/**
	 * The combination type
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_bookingcombinationtype"))
	private CombinationType type;
	
	/**
	 * Update timestamp
	 */
	@Column(name="c_update")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar update_ts;
	
	/**
	 * The title
	 */
	@Column(name="c_title")
	private String title;
	
	/**
	 * The project
	 */
	@JoinColumn(name="c_project")
	@ManyToOne
	private Project project;

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
	 * 
	 * @return The reference
	 */
	public abstract T getReference();
	
	/**
	 * @param T The reference to set
	 */
	public abstract void setReference(T reference);

	/**
	 * @return the bookings
	 */
	public List<Event> getBookings() {
		return bookings;
	}

	/**
	 * @param bookings the bookings to set
	 */
	public void setBookings(List<Event> bookings) {
		this.bookings = bookings;
	}

	/**
	 * @return the type
	 */
	public CombinationType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(CombinationType type) {
		this.type = type;
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
	 * @return the update
	 */
	public Calendar getUpdate() {
		return update_ts;
	}

	/**
	 * @param update the update to set
	 */
	public void setUpdate(Calendar update) {
		this.update_ts = update;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}
	
	/**
	 * Textual representation of the instance
	 * @return The textual representation
	 */
	public String toString(){
		int size = 0;
		if(this.getBookings() != null){
			size = this.getBookings().size();
		}
		return super.toString() + " with the reference " + this.getReference() + " linked to " + size + " booking(s)";
	}
}
