package com.teamium.domain.output.edition;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Describes insets for an edition document
 * @author sraybaud - NovaRem
 * @version 1.0
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Insets implements Serializable{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -5319662928521044384L;

	/**
	 * Top
	 */
	@XmlElement(namespace=Edition.XMLNS)
	private Integer top;
	
	/**
	 * Left
	 */
	@XmlElement(namespace=Edition.XMLNS)
	private Integer left;
	
	/**
	 * Bottom
	 */
	@XmlElement(namespace=Edition.XMLNS)
	private Integer bottom;
	
	/**
	 * Right
	 */
	@XmlElement(namespace=Edition.XMLNS)
	private Integer right;

	/**
	 * @return the top
	 */
	public Integer getTop() {
		return top;
	}

	/**
	 * @param top the top to set
	 */
	public void setTop(Integer top) {
		this.top = top;
	}

	/**
	 * @return the left
	 */
	public Integer getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(Integer left) {
		this.left = left;
	}

	/**
	 * @return the bottom
	 */
	public Integer getBottom() {
		return bottom;
	}

	/**
	 * @param bottom the bottom to set
	 */
	public void setBottom(Integer bottom) {
		this.bottom = bottom;
	}

	/**
	 * @return the right
	 */
	public Integer getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(Integer right) {
		this.right = right;
	}

	
}
