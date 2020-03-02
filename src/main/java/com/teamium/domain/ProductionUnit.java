/**
 * 
 */
package com.teamium.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;



/**
 * Describe an action
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name="t_produnit")
@NamedQuery(name=ProductionUnit.QUERY_findAll, query="SELECT pu FROM ProductionUnit pu WHERE pu.archived != TRUE")
public class ProductionUnit extends AbstractEntity implements Comparable<ProductionUnit>{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -6522230516348743229L;
	
	/**
	 * Find all query for production unit
	 */
	public static final String QUERY_findAll = "findAllProdUnit";
	
	/**
	 * Action ID
	 */
	@Id
	@Column(name="c_idprodunit", insertable=false, updatable=false)
	@TableGenerator( name = "idProductionUnit_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "produnit_idprodunit_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idProductionUnit_seq")
	private Long id;
	
	/**
	 * Unit name
	 */
	@Column(name="c_name")
	private String name;
	
	/**
	 * UI Theme
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_theme"))
	private Theme theme;
	
	@Column(name="c_archived")
	private Boolean archived = Boolean.FALSE;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the theme
	 */
	public Theme getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	/**
	 * @return the archived
	 */
	public Boolean getArchived() {
		return archived;
	}

	/**
	 * @param archived the archived to set
	 */
	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	/**
	 * Compare the current objet with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(ProductionUnit o) {
		if(o==null) return 1;
		if(o.getName()==null){
			if(this.name==null)
				return 0;
			else{
				return 1;
			}
		}else{
			return o.getName().compareTo(this.name);
		}
	}	
	
	/**
	 * Return the string expression of the current object
	 * @return the string
	 */
	@Override
	public String toString(){
		return super.toString()+" "+this.name;
	}
}
