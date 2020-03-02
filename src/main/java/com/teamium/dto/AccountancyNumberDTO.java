package com.teamium.dto;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.accountancy.AccountancyNumber;
import com.teamium.enums.AccountancyType;
import com.teamium.enums.ProjectStatus;

/**
 * DTO Class for AccountancyNumber Entity
 * 
 * @author Teamium
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class AccountancyNumberDTO extends AbstractDTO {

	private String type;
	private String value;

	public AccountancyNumberDTO() {
		super();
	}

	public AccountancyNumberDTO(AccountancyNumber entity) {
		super(entity);
		if (entity.getType() != null) {
			this.type = entity.getType().getKey();
		}
		this.value = entity.getValue();
	}

	@JsonIgnore
	public AccountancyNumber getAccountancyNumber(AccountancyNumber accountancyNumber) {
		accountancyNumber.setId(this.getId());
		accountancyNumber.setVersion(this.getVersion());
		if (!StringUtils.isBlank(type)) {
			XmlEntity accountancyType = new XmlEntity();
			accountancyType.setKey(AccountancyType.getEnum(type).getAccountancyType());
			accountancyNumber.setType(accountancyType);
		}
		accountancyNumber.setValue(this.value);
		return accountancyNumber;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
