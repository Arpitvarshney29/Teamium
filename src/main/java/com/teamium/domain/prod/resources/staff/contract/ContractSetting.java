/**
 * 
 */
package com.teamium.domain.prod.resources.staff.contract;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.prod.resources.SaleEntity;

/**
 * Describe a resource availibility period.
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name="t_staff_contractsetting")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="c_discriminator", discriminatorType=DiscriminatorType.STRING)
public abstract class ContractSetting extends AbstractEntity{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -4001256585545402071L;
	
	/**
	 * Query string changing type of the current contract setting
	 * @param 1 the discriminator value to set, that must be EXCLUSIVELY the simple class name of the entity, transformed to lower case
	 * @param id id of the targeted contract
	 * @return the result of the update
	 */
	public static final String QUERY_nativeUpdateType = "UPDATE t_staff_contractsetting SET c_version = c_version+1, c_discriminator = ?1 WHERE c_idsetting = ?2";

	/**
	 * Contract settings ID
	 */
	@Id
	@Column(name="c_idsetting", insertable=false, updatable=false)
	@TableGenerator( name = "idContractSetting_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "person_contractsetting_idsetting_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idContractSetting_seq")
	private Long id;
	
	
	/**
	 * Personal ID in company
	 */
	@Column(name="c_employeeid")
	private Long employeeID;
	
	/**
	 * Contract type
	 * @see com.teamium.domain.prod.resources.staff.contract.ContractType
	 */
	@Embedded
	@AttributeOverride(name="key",column=@Column(name="c_contracttype"))
	private ContractType type;
	
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
	 * @return the employeeID
	 */
	public Long getEmployeeID() {
		return employeeID;
	}

	/**
	 * @param employeeID the employeeID to set
	 */
	public void setEmployeeID(Long employeeID) {
		this.employeeID = employeeID;
	}

	/**
	 * @return the type
	 */
	public ContractType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ContractType type) {
		this.type = type;
	}
	
}
