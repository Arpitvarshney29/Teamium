/**
 * 
 */
package com.teamium.domain.prod.resources.accountancy;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
 * @author slopes
 * The accountancy number
 *
 */
@Entity
@Table(name="t_accountancy_number")
public class AccountancyNumber extends AbstractEntity implements Comparable<AccountancyNumber> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3032406020737190162L;
	/**
	 * The ID
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idAccountancyNumber_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "accountancy", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idAccountancyNumber_seq")
	private Long id;
	
	/**
	 * The type
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_type"))
	private XmlEntity type;
	
	/**
	 * The account number
	 */
	@Column(name="c_value")
	private String value;	

	/**
	 * @return The id
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * The id to set
	 */
	@Override
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Compare the value
	 * @param o The accountancy number to compare
	 */
	@Override
	public int compareTo(AccountancyNumber o) {
		int compare = 0;
		if(o == null){
			return -1;
		}
		
		if(this.type != null){
			if(o.type == null){
				return 1;
			}else{
				compare = this.type.compareTo(o.type);
			}
		}else{
			if(o.type != null){
				return -1;
			}
		}
		if(compare == 0){
			if(this.value != null){
				if(o.value == null){
					return 1;
				}else{
					compare = this.value.compareTo(o.value);
				}
			}else{
				if(o.value != null){
					return -1;
				}
			}
		}
		if(compare == 0){
			if(this.id != null){
				if(o.id == null){
					return 1;
				}else{
					compare = this.id.compareTo(o.id);
				}
			}else{
				if(o.id != null){
					return -1;
				}
			}
		}
		
		return compare;
	}
	
	/**
	 * True if the value is equals to the value of the object given in parameter
	 * @return True if the value is equals to the value of the object given in parameter
	 */
	public boolean equals(Object obj){
		boolean equals = super.equals(obj);
		if(!equals){
			return false;
		}else{
			AccountancyNumber other = (AccountancyNumber) obj;
			if(this.getType() != null){
				if(other.getType() == null){
					return false;
				}else{
					if(! (this.getType().equals(other.getType()))){
						return false;
					}
				}
			}
			if(this.getValue() != null){
				if(other.getValue() == null){
					return false;
				}else{
					if(! (this.getValue().equals(other.getValue()))){
						return false;
					}
				}
			}
			return true;
		}
	}

}
