/**
 * 
 */
package com.teamium.domain.prod.resources.staff;

import java.util.Calendar;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;

/**
 * Describe a document ID
 * @author sraybaud- NovaRem
 *
 */
@Entity
@Table(name="t_staff_iddocument")
public class IDDocument extends AbstractEntity{

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = -7704855103809180035L;

	/**
	 * ID
	 */
	@Id
	@Column(name="c_iddocument", insertable=false, updatable=false)
	@TableGenerator( name = "idIDDocument_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "staff_iddocument_iddocument_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idIDDocument_seq")
	private Long id;

	/**
	 * Type of ID document
	 * @see com.teamium.domain.prod.resources.staff.IDDocumentType
	 */
	@AttributeOverride(name="key", column=@Column(name="c_typedocument"))
	private XmlEntity type;
	
	/**
	 * Document number
	 */
	@Column(name="c_number")
	private String number;
	
	/**
	 * Delivered by
	 */
	@Column(name="c_deliveredby")
	private String deliveredBy;
	
	/**
	 * Delivered in
	 */
	@Column(name="c_deliveredin")
	private String deliveredIn;
	
	/**
	 * Until date
	 */
	@Column(name="c_untildate")
	private Calendar untilDate;

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
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the deliveredBy
	 */
	public String getDeliveredBy() {
		return deliveredBy;
	}

	/**
	 * @param deliveredBy the deliveredBy to set
	 */
	public void setDeliveredBy(String deliveredBy) {
		this.deliveredBy = deliveredBy;
	}

	/**
	 * @return the deliveredIn
	 */
	public String getDeliveredIn() {
		return deliveredIn;
	}

	/**
	 * @param deliveredIn the deliveredIn to set
	 */
	public void setDeliveredIn(String deliveredIn) {
		this.deliveredIn = deliveredIn;
	}

	/**
	 * @return the untilDate
	 */
	public Calendar getUntilDate() {
		return untilDate;
	}

	/**
	 * @param untilDate the untilDate to set
	 */
	public void setUntilDate(Calendar untilDate) {
		this.untilDate = untilDate;
	}
	
	

}
