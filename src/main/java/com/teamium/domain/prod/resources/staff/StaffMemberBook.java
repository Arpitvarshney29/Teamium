/**
 * 
 */
package com.teamium.domain.prod.resources.staff;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.teamium.domain.Document;
import com.teamium.domain.XmlEntity;

/**
 * Describe a part of a staff member book
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue(StaffMemberBook.TYPE)
public class StaffMemberBook extends Document{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -1978763406955514756L;
	
	/**
	 * The book document discriminator type
	 */
	public static final String TYPE = "book";
	
	/**
	 * Book category
	 * @see com.teamium.domain.prod.resources.staff.BookCategory
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_book_category"))
	private XmlEntity category;
	
	/**
	 * Book rating
	 */
	@Column(name="c_book_rating")
	private Integer rating;

	/**
	 * @return the category
	 */
	public XmlEntity getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(XmlEntity category) {
		this.category = category;
	}

	/**
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}
}
