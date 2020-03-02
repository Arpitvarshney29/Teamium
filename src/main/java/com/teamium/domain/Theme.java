/**
 * 
 */
package com.teamium.domain;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Describe a company type of profile
 * @author sraybaud- NovaRem
 * @author jsdimpre - NovaRem
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Theme extends AbstractXmlEntity{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 2413828260417264030L;
	
	/**
	 * Background color start
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String bgColorDebut;
	
	/**
	 * Background color start
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String bgColorFin;
	
	/**
	 * Completion Color
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String completionColor;
	
	/**
	 * Background image
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String bgImage;
	
	/**
	 * Font color
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String fontColor;
	
	/**
	 * Style class
	 */
	@XmlElement(namespace=TeamiumConstants.XMLNS)
	@Transient
	private String styleClass;


	/**
	 * @return the bgColorDebut
	 */
	public String getBgColorDebut() {
		return bgColorDebut;
	}

	/**
	 * @param bgColorDebut the bgColorDebut to set
	 */
	public void setBgColorDebut(String bgColorDebut) {
		this.bgColorDebut = bgColorDebut;
	}

	/**
	 * @return the bgColorFin
	 */
	public String getBgColorFin() {
		return bgColorFin;
	}

	/**
	 * @param bgColorFin the bgColorFin to set
	 */
	public void setBgColorFin(String bgColorFin) {
		this.bgColorFin = bgColorFin;
	}

	/**
	 * @return the completionColor
	 */
	public String getCompletionColor() {
		return completionColor;
	}

	/**
	 * @param completionColor the completionColor to set
	 */
	public void setCompletionColor(String completionColor) {
		this.completionColor = completionColor;
	}

	/**
	 * @return the bgImage
	 */
	public String getBgImage() {
		return bgImage;
	}

	/**
	 * @param bgImage the bgImage to set
	 */
	public void setBgImage(String bgImage) {
		this.bgImage = bgImage;
	}

	/**
	 * @return the fontColor
	 */
	public String getFontColor() {
		return fontColor;
	}

	/**
	 * @param fontColor the fontColor to set
	 */
	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	/**
	 * @return the styleClass
	 */
	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
}
