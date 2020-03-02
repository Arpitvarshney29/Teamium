/**
 * 
 */
package com.teamium.domain;

import java.io.Serializable;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.utils.LocaleAdapter;

/**
 * Describe a generic xml entity, persisted in an xml file
 * @author sraybaud - NovaRem
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlLocalizedLabel implements Serializable{

	public XmlLocalizedLabel(){};
	
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2691972598677598682L;

	/**
	 * Locale
	 */
	@XmlAttribute(namespace=TeamiumConstants.XMLNS)
	@XmlJavaTypeAdapter(LocaleAdapter.class)
	private Locale locale;
	
	/**
	 * Label
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	private String text;

	/**
	 * @return the locale
	 */

	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	

}
