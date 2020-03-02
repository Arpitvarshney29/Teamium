package com.teamium.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.teamium.domain.Company;
import com.teamium.domain.Document;
import com.teamium.domain.prod.Record;
import com.teamium.domain.prod.resources.suppliers.Supplier;
import com.teamium.dto.prod.resources.functions.RateDTO;

/**
 * DTO Class for Supplier
 *
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SupplierDTO extends CompanyDTO {

	private OrderDTO orderDTO;

	public SupplierDTO() {
	}

	public SupplierDTO(String name, Document logo, AddressDTO address, String companyNumber, String vatNumber,
			String accountNumber, String comments, Map<String, String> numbers) {
		super(name, logo, address, companyNumber, vatNumber, accountNumber, comments, numbers, address, comments);
	}

	public SupplierDTO(Company company) {
		super(company);
	}

	@JsonIgnore
	public Supplier getSupplier(Supplier supplier) {
		
		getCompanyDetails(supplier);
		return supplier;
	}

	@JsonIgnore
	public Supplier getSupplier() {
		return getSupplier(new Supplier());
	}

	/**
	 * @return the orderDTO
	 */
	public OrderDTO getOrderDTO() {
		return orderDTO;
	}

	/**
	 * @param orderDTO the orderDTO to set
	 */
	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}


}
