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

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.SaleEntity;

@Entity
@Table(name="t_staff_employee_id")
public class StaffEmployeeID extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1320905157506246686L;

	/**
	 * ID de l'entité
	 */
	@Id
	@Column(name="c_staffemployeeid", insertable=false, updatable=false)
	@TableGenerator( name = "idemployee_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "employee_idemployee_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idemployee_seq")
	private Long id;
	
	/**
	 * The saleEntity
	 */
	@ManyToOne
	@JoinColumn(name="c_saleentity")
	private SaleEntity entity;
	

	@Column(name="c_employeeId")
	private Long employeeId;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	/**
	 * @return the entity
	 */
	public SaleEntity getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(SaleEntity entity) {
		this.entity = entity;
	}

	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	
}
