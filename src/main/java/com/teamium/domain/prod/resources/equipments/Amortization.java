/**
 * 
 */
package com.teamium.domain.prod.resources.equipments;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.ResourceInformation;

/**
 * @author slopes
 * Entity used to define the amortization of an equipment
 *
 */
@Entity
@Table(name="t_amortization")
public class Amortization extends AbstractEntity implements Comparable<Amortization> {
	/**
	 * The generated serial IUD
	 */
	private static final long serialVersionUID = -325211672921845709L;

	/**
	 * The amortization ID
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idAmortization_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "amortization", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idAmortization_seq")
	private Long id;
	
	/**
	 * The date
	 */
	@Column(name="c_date")
	@Temporal(TemporalType.DATE)
	private Calendar date;
	
	/**
	 * The percent
	 */
	@Column(name="c_percent")
	private Float percent;
	
	/**
	 * The value
	 */
	@Column(name="c_value")
	private Float value;
	
	/**
	 * The equipment concern by the amortization
	 */
	@ManyToOne
	@JoinColumn(name="c_equipment")
	private Equipment equipment;

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
	 *
	 * @return the percent
	 */
	public Float getPercent() {
		return percent;
	}

	/**
	 * The percent is synchronize with the value
	 * @param percent the percent to set
	 */
	public void setPercent(Float percent) {
		if(percent != null && percent >= 0 && this.percent != null && !this.percent.equals(percent)){
			this.percent = percent;
			this.value = equipment.getValue() * percent;
		}
	}

	/**
	 * @return the value
	 */
	public Float getValue() {
		return value;
	}

	/**
	 * The value is synchronize with percent
	 * @param value the value to set
	 */
	public void setValue(Float value) {
		if(value != null){
			//No refresh needed if the value doesn't change
			if(!(this.value != null && this.value.equals(value))){
				this.value = value;
				if(this.equipment != null && this.equipment.getValue() != null && !this.equipment.getValue().equals(0)){
					this.percent = this.getValue() / this.equipment.getValue();
				}
			}
		}
	}

	/**
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}
	
	/**
	 * Refresh value & percent
	 */
	public void refreshValue(){
		//Value
		if(this.percent != null && this.equipment.getValue() != null){
			this.value = equipment.getValue() * percent;
		}
	}
	
	/**
	 * Compare the date
	 * @param section The date
	 * @return The comparison value
	 */
	@Override
	public int compareTo(Amortization o){
		int compare = 0;
		
		if(o == null){
			return -1;
		}
		
		// Date
		if(compare == 0){
			if(this.date != null){
				if(o.date == null){
					compare = -1;
				}else{
					compare = this.date.compareTo(o.date);
				}
			}else{
				if(o.date == null){
					compare = 1;
				}
			}
		}
		
		// Id
		if(compare == 0){
			if(this.id != null){
				if(o.id == null){
					compare = -1;
				}else{
					compare = this.id.compareTo(o.id);
				}
			}else{
				if(o.id == null){
					compare = 1;
				}
			}
		}
		
		return compare;
	}
}
