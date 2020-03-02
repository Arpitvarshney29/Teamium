package com.teamium.domain.prod.resources.staff;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.joda.time.DateTime;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

/**
 * Entity class for reel table
 */

@Entity
@Table(name = "t_reel")
public class Reel extends AbstractEntity {

	/**
	 * Auto generated value
	 */
	private static final long serialVersionUID = -3443827989010316560L;
	@Id
	@Column(name = "c_idreel", insertable = false, updatable = false)
	@TableGenerator(name = "idReel_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "staff_idReel_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idReel_seq")
	private Long id;

	@Column(name = "c_type")
	private String type;

	@Column(name = "c_title")
	private String title;

	@Column(name = "c_bio")
	private String bio;

	@Column(name = "c_url")
	private String url;

	@Column(name = "c_upload")
	private boolean upload;

	@ManyToOne
	@JoinColumn(name = "c_idstaff")
	private StaffMember staff;

	@Column(name = "c_udated_on")
	private DateTime updatedOn;

	public Reel() {
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
	 * @return the bio
	 */
	public String getBio() {
		return bio;
	}

	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		this.bio = bio;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the upload
	 */
	public boolean isUpload() {
		return upload;
	}

	/**
	 * @param upload the upload to set
	 */
	public void setUpload(boolean upload) {
		this.upload = upload;
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
	 * @return the staff
	 */
	public StaffMember getStaff() {
		return staff;
	}

	/**
	 * @param staff the staff to set
	 */
	public void setStaff(StaffMember staff) {
		this.staff = staff;
	}

	/**
	 * @return the updatedOn
	 */
	public DateTime getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * @param updatedOn the updatedOn to set
	 */
	public void setUpdatedOn(DateTime updatedOn) {
		this.updatedOn = updatedOn;
	}

}
