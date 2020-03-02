package com.teamium.domain;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class is an abstract superclass that describes an XML entity, which instances are persisted in a xml file
 * saved in the data/teamium/shared/entities directory or any client equivalent data/teamium/[client]/entities
 * @see data/teamium/shared/entities
 * 
 * Most of options in value lists used as the framework of the application are instanciated as :
 * - either a generic xml entity (@see com.teamium.domain.XmlEntity) with the given properties declared in this superclass
 * - either a specific xml entity if the entity embed more information than the generic properties
 * @author sraybaud
 *
 */
@MappedSuperclass
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractXmlEntity implements Comparable<AbstractXmlEntity>,Serializable{
	/**
	 * Namespace
	 */
	public static final String XMLNS = TeamiumConstants.XMLNS;

	/**
	 * Class uid
	 */
	private static final long serialVersionUID = -3957130584708164738L;
	
	/**
	 * Entity unique code
	 */
	@XmlAttribute
	private String key;
	
	/**
	 * Entity ordering number
	 */
	@XmlAttribute(name="number")
	@Transient
	private Integer position;
	
	/**
	 * Localized label
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private XmlLocalizedLabel label;

	/**
	 * @return the entity key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}


	/**
	 * @return the label
	 */
	public String getLabel() {
		try{
			return label.getText().toString();
		}
		catch(NullPointerException e){
			return key;
		}
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		if(this.label==null) this.label = new XmlLocalizedLabel();
		this.label.setText(label);
	}
	
	/**
	 * @return the locale of the label
	 */
	public Locale getLocale() {
		try{
			return label.getLocale();
		}
		catch(NullPointerException e){
			return Locale.getDefault();
		}
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		if(this.label==null) this.label = new XmlLocalizedLabel();
		this.label.setLocale(locale);
	}
	
	/**
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}

	/**
	 * Retourne le hashcode de l'objet
	 * @see java.lang.Object#hashCode()
	 * @return le hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((this.getClass().getName() == null) ? 0 : this.getClass().getName().hashCode());
		return result;
	}

	/**
	 * Teste l'égalité de l'objet courant avec celui passé en argument
	 * @see java.lang.Object#equals(java.lang.Object)
	 * return true si les deux objets sont égaux, false sinon
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!getClass().isInstance(obj))
			return false;
		AbstractXmlEntity other = (AbstractXmlEntity) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	
	/**
	 * Compare the current objet with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(AbstractXmlEntity o) {
		if(o==null) return 1;
		if(this.position==null){
			if(o.position!=null){
				return -1;
			}
		}else{
			if(o.position!=null){
				int compare = this.position.compareTo(o.position);
				if(compare !=0) return compare;
			}
		}
		if(this.getLabel()==null){
			if(o.getLabel()!=null){
				return -1;
			}
		}else{
			int compare = 0;
			compare = Collator.getInstance(getLocale()).compare(this.getLabel(), o.getLabel());
			if(compare !=0) return compare;
		}
		if(this.key==null){
			if(o.key!=null){
				return -1;
			}
		}else{
			int compare = this.key.compareTo(o.key);
			if(compare !=0) return compare;
		}
		return 0;
	}	

	/**
	 * Retourne l'expression textuelle de l'objet
	 * @return le texte correspondant
	 */
	public String toString(){
		return this.getClass().getSimpleName()+" "+this.key;
	
	}
}
