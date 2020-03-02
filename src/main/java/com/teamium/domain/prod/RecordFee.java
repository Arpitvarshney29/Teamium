/**
 * 
 */
package com.teamium.domain.prod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.TeamiumConstants;

/**
 * Describes a fee added to a project
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name="t_record_fee")
public class RecordFee extends Fee{
	
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -7094157468983355633L;

	/**
	 * Fee ID
	 */
	@Id
	@Column(name="c_idfee", insertable=false, updatable=false)
	@TableGenerator( name = "idFee_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "record_fee_idfee_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idFee_seq")
	private Long id;
	
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


	
}
