package com.teamium.dto;

import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import com.teamium.domain.PersonalDocument;
import com.teamium.exception.UnprocessableEntityException;

/**
 * The dto class for PersonalDocument Entity
 *
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonalDocumentDTO {

	private Long id;

	private String type;

	private String number;

	private DateTime expirationDate;

	@Transient
	private String expirationColor;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public PersonalDocumentDTO() {
	}

	public PersonalDocumentDTO(PersonalDocument document) {
		id = document.getId();
		number = document.getNumber();
		expirationDate = new DateTime(document.getExpirationDate());
		if (document.getType() != null) {
			this.type = document.getType().getType();
		}
	}

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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the expirationDate
	 */
	public DateTime getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(DateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Get PersonalDcoument entity from DTO
	 * 
	 * @param document
	 * @return PersonalDocument
	 */
	@JsonIgnore
	public PersonalDocument getPersonalDocument(PersonalDocument document) {
		if (expirationDate == null) {
			logger.error("expirationDate date is null");
			throw new UnprocessableEntityException("expirationDate date can not be null");

		}
		if (StringUtils.isBlank(number)) {
			logger.error("invalid document number ");
			throw new UnprocessableEntityException("invalid document number");
		}
		// currently skipping validation due to old database
		// if (expirationDate.isBefore(new DateTime())) {
		// logger.error("expirationDate date can not be past date ");
		// throw new UnprocessableEntityException("expirationDate date can not be past
		// date ");
		// }

		document.setExpirationDate(expirationDate.toGregorianCalendar());

		document.setNumber(number);
		return document;
	}

	/**
	 * Get PersonalDcoument entity from DTO
	 * 
	 * @return PersonalDocument
	 */
	@JsonIgnore
	public PersonalDocument getPersonalDocument() {
		return this.getPersonalDocument(new PersonalDocument());
	}

	/**
	 * @return the remaining days to expire
	 */
	public int getRemainingDays() {
		LocalDate currentDate = LocalDate.now();
		return Days.daysBetween(currentDate, expirationDate.toLocalDate()).getDays();
	}

	/**
	 * @return the expirationColor
	 */
	public String getExpirationColor() {
		return expirationColor;
	}

	/**
	 * @param expirationColor the expirationColor to set
	 */
	public void setExpirationColor(String expirationColor) {
		this.expirationColor = expirationColor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PersonalDocumentDTO [id=" + id + ", type=" + type + ", number=" + number + ", expirationDate="
				+ expirationDate + ", logger=" + logger + "]";
	}

}
