/**
 * 
 */
package com.teamium.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Describe a generic entity, persisted in a database
 * @author sraybaud - NovaRem
 *
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable{

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 5658121474554959082L;
	
	/**
	 * Entity version
	 */
	@Version
	@Column(name="c_version")
	private Integer version = 0;
	
	/**
	 * Return the persistant ID
	 * @return the id
	 */
	public abstract Long getId();
	
	/**
	 * Set the persistant ID
	 * @param the id to set
	 */
	public abstract void setId(Long id);
	
	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}
	
	/**
	 * @param version the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}



	/**
	 * Returns the object hashcode
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	/**
	 * Test if the given object and the current entity are equal
	 * @param obj the other object
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return true if both objects are equal, else returns false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
	
	/**
	 * Returns the text expression of the entity
	 * @return the text expression
	 */
	@Override
	public String toString(){
		return "{"+this.getClass().getSimpleName()+(this.getId()!=null? " #"+this.getId(): "")+"}";
	}
	
	/**
	 * Clone the current object erasing its id and initializing its version number
	 * @return the clone
	 * @throws CloneNotSupportedException 
	 */
	@Override
	protected AbstractEntity clone() throws CloneNotSupportedException{
		AbstractEntity clone=null;
		try{
			clone = (AbstractEntity) super.clone();
			clone.setId(null);
			clone.setVersion(0);
		}
		catch(CloneNotSupportedException e){
			throw e;
		}
		return clone;
	}
	

}
