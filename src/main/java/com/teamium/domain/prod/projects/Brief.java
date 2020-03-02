/**
 * 
 */
package com.teamium.domain.prod.projects;

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
 *
 */
@Entity
@Table(name="t_brief")
public class Brief extends AbstractEntity implements Cloneable, Comparable<Brief> {
	
	/**
	 * The generate serial ID
	 */
	private static final long serialVersionUID = 3378055159747195275L;
	
	/**
	 * The id of the brief
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idBrief_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "brief_idbrief_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idBrief_seq")
	private Long id;
	
	/**
	 * The brief type
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_type"))
	private XmlEntity briefType;
	

	/**
	 * The description
	 */
	@Column(name="c_description")
	private String description;
	

	/**
	 * @return The id of the brief
	 */
	@Override
	public Long getId() {
		return this.id;
	}

	/**
	 * Setting the id of the brief
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return the briefType
	 */
	public XmlEntity getBriefType() {
		return briefType;
	}

	/**
	 * @param briefType the briefType to set
	 */
	public void setBriefType(XmlEntity briefType) {
		this.briefType = briefType;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		if (description == null)
			description = "";
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	/**
	 * Return a clone of the current instance
	 * @return The cloned instance
	 */
	@Override
	public Brief clone() throws CloneNotSupportedException{
		XmlEntity briefType = this.briefType;
		try{
			Brief clone = (Brief) super.clone();
			this.briefType = null;
			clone.briefType = briefType;
			return clone;
		}catch (CloneNotSupportedException e){
			throw e;
		}finally{
			this.briefType = briefType;
		}
		
	}

	@Override
	public int compareTo(Brief o) {
		if(o==null) return 1;
		if(this.briefType==null){
			return -1;
		}else{
			if(o.briefType!=null){
				int compare = this.briefType.compareTo(o.briefType);
				if(compare !=0) return compare;
			}
		}
		if (id == null) {
			if (o.id != null)
				return -1;
		} else {
			int compare = this.id.compareTo(o.id);
			if(compare !=0) return compare;
		}
		return 0;
	}
	

}
