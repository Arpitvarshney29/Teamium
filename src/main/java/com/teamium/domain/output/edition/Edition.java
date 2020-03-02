package com.teamium.domain.output.edition;

import java.awt.Insets;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.AbstractXmlEntity;

/**
 * Describes an edition layout
 * @author sraybaud - NovaRem
 * @version 1.0
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Edition extends AbstractXmlEntity{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2467461047027873611L;

	
	/**
	 * Page format (A4, A3 etc.)
	 */
	@Transient
	@XmlElement(namespace=Edition.XMLNS)
	private String format;
	
	/**
	 * Page orientation
	 */
	@Transient
	@XmlElement(namespace=Edition.XMLNS)
	private Boolean landscape;

	
	/**
	 * Insets of the final document
	 */
	@XmlElement(namespace=XMLNS)
	@XmlJavaTypeAdapter(InsetsAdapter.class)
	private Insets insets;
	
	/**
	 * Width of the final XHTML document
	 */
	@XmlElement(namespace=XMLNS)
	private Integer htmlWidth;
	
	/**
	 * Absolute or relative path of the xsl-t file used for transformation from XML to XHTML
	 */
	@XmlElement(namespace=XMLNS)
	private String xsl;

	/**
	 * @return the returns page format
	 */
	public String getFormat() {
		return format;
	}
	
	/**
	 * @return true if page orientation is landscape, else returns false
	 */
	public Boolean isLandscape() {
		try{
			return landscape.booleanValue();
		}
		catch(NullPointerException e){
			return false;
		}
	}

	/**
	 * @return the insets
	 */
	public Insets getInsets() {
		return insets;
	}

	/**
	 * @param insets the insets to set
	 */
	public void setInsets(Insets insets) {
		this.insets = insets;
	}

	/**
	 * @return the htmlWidth
	 */
	public Integer getHtmlWidth() {
		return htmlWidth;
	}

	/**
	 * @param htmlWidth the htmlWidth to set
	 */
	public void setHtmlWidth(Integer htmlWidth) {
		this.htmlWidth = htmlWidth;
	}

	/**
	 * @return the xslPath
	 */
	public String getXsl() {
		return xsl;
	}

	/**
	 * @param xslPath the xslPath to set
	 */
	public void setXsl(String xslPath) {
		this.xsl = xslPath;
	}
}
