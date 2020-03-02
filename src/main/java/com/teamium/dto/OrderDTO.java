package com.teamium.dto;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.order.Order;
import com.teamium.utils.CommonUtil;

/**
 * DTO class for ORDER Entity
 * 
 * @author Nishant Kumar
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrderDTO extends RecordDTO {

	private SupplyResourceDTO resource;
	private long supplierId;
	private long projectId;
	private String projectTitle;

	public OrderDTO() {
		super();
	}

	public OrderDTO(Order order) {
		super(order);
		if (order.getResource() != null) {
			this.resource = new SupplyResourceDTO(order.getResource());
		}
		this.setPaymentTerms(order.getPaymentTerms());
		this.setProcurementTax(order.getProcurementTax());
		XmlEntity statusEntity = order.getStatus();
		if (statusEntity != null) {
			this.setStatus(statusEntity.getKey());
		}
	}

	public OrderDTO(Order project, String recordDiscriminator) {
		this.setId(project.getId());
		if (project.getCompany() != null) {
			this.setCompany(new CompanyDTO(project.getCompany()));
		}
		this.setCity(project.getCity());
		this.setCountry(project.getCountry() != null ? project.getCountry().getKey() : "");
		if (project.getDueDates() != null && !project.getDueDates().isEmpty()) {
			Optional<DueDate> optional = project.getDueDates().stream()
					.filter(d -> d.getType() != null && d.getType().getName().equalsIgnoreCase("delivery")).findFirst();
			if (optional.isPresent()) {
				DueDate dueDate = optional.get();
				this.setDueDateForListView(CommonUtil.getStringFromCalendar(dueDate.getDate(), "dd/MM/yyyy"));
			}
		}
		if (project.getSource() != null) {
			Project bookingProject = (Project) project.getSource();
			this.projectTitle = bookingProject != null ? bookingProject.getTitle() : "";
		}
		this.setRecordDiscriminator(recordDiscriminator);
		this.setPaymentTerms(project.getPaymentTerms());
		this.setProcurementTax(project.getProcurementTax());
		XmlEntity statusEntity = project.getStatus();
		if (statusEntity != null) {
			this.setStatus(statusEntity.getKey());
		}
	}

	/**
	 * Get Order entity from DTO
	 * 
	 * @param order
	 * @return Order
	 */
	@JsonIgnore
	public Order getOrder(Order order) {
		// order.setResource();
		super.getRecord(order, this);
		return order;
	}

	/**
	 * Get Order entity from DTO
	 * 
	 * @return Order
	 */
	@JsonIgnore
	public Order getOrder() {
		return this.getOrder(new Order());
	}

	/**
	 * @return the resource
	 */
	public SupplyResourceDTO getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(SupplyResourceDTO resource) {
		this.resource = resource;
	}

	/**
	 * @return the supplierId
	 */
	public long getSupplierId() {
		return supplierId;
	}

	/**
	 * @param supplierId the supplierId to set
	 */
	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	/**
	 * @return the projectId
	 */
	public long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return the projectTitle
	 */
	public String getProjectTitle() {
		return projectTitle;
	}

	/**
	 * @param projectTitle the projectTitle to set
	 */
	public void setProjectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}

}
