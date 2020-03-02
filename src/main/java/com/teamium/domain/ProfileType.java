/**
 * 
 */
package com.teamium.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;



/**
 * Describe a company type of profile
 * @author sraybaud- NovaRem
 */
@Embeddable
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
public class ProfileType extends AbstractXmlEntity{

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = 3464111206887385670L;

	/**
	 * List of choice
	 */
	@XmlElementWrapper(namespace=TeamiumConstants.XMLNS)
	@XmlElement(namespace=TeamiumConstants.XMLNS,name="choice")
	@Transient
	private List<String> choices;

	/**
	 * @return the choices
	 */
	public List<String> getChoices() {
		if(this.choices==null) this.choices = new ArrayList<String>();
		return choices;
	}

	/**
	 * @param choices the choices to set
	 */
	public void setChoices(List<String> choices) {
		this.choices = choices;
	}
	
	/**
	 * Teste l'égalité de l'objet courant avec celui passé en argument
	 * Un type de profile dérivé peut être comparé au super type sur la base de la clé
	 * @see java.lang.Object#equals(java.lang.Object)
	 * return true si les deux objets sont égaux, false sinon
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ProfileType))
			return false;
		ProfileType other = (ProfileType) obj;
		if (this.getKey() == null) {
			if (other.getKey() != null)
				return false;
		} else if (!this.getKey().equals(other.getKey()))
			return false;
		return true;
	}
}
