package com.teamium.dto;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.DueDate;
import com.teamium.domain.prod.projects.AbstractProject;
import com.teamium.domain.prod.projects.Project;
import com.teamium.domain.prod.projects.Promo;
import com.teamium.domain.prod.projects.Quotation;
import com.teamium.utils.CommonUtil;

/**
 * Quotation Wrapper class
 * 
 * @author
 *
 */
@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class QuotationDTO extends AbstractProjectDTO {

	private Calendar startDate;
	private Calendar endDate;
	private Integer minuteDuration;
	private String idPad;
	private ChannelDTO channel;
	private ProductionUnitDTO productionUnit;
	private List<PromoDTO> promoList;
	private String delivery;
	private String csa;
	private boolean validated = false;
	private String comparison;

	private Float bookingTotalIVAT = 0f;
	private String comparisonTag;

	public QuotationDTO() {
	}

	public QuotationDTO(AbstractProject abstractProject) {
		super(abstractProject);
	}

	public QuotationDTO(Calendar startDate, Calendar endDate, Integer minuteDuration, String idPad) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.minuteDuration = minuteDuration;
		this.idPad = idPad;
	}

	public QuotationDTO(Long id, String category, String title) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
	}

	public QuotationDTO(Quotation project, String recordDiscriminator) {
		if (project != null) {
			this.setId(project.getId());
			this.setCategory(project.getCategory() != null ? project.getCategory().getName() : "");
			this.setTitle(project.getTitle());
			this.setStatus(project.getStatus() != null ? project.getStatus().getKey() : "");
			this.setRecordDiscriminator(recordDiscriminator);
			if (project.getCompany() != null) {
				this.setCompany(new CompanyDTO(project.getCompany()));
			}
			this.setCity(project.getCity());
			this.setCountry(project.getCountry() != null ? project.getCountry().getKey() : "");
			if (project.getDueDates() != null && !project.getDueDates().isEmpty()) {
				Optional<DueDate> optional = project.getDueDates().stream()
						.filter(d -> d.getType() != null && d.getType().getName().equalsIgnoreCase("delivery"))
						.findFirst();
				if (optional.isPresent()) {
					DueDate dueDate = optional.get();
					this.setDueDateForListView(CommonUtil.getStringFromCalendar(dueDate.getDate(), "dd/MM/yyyy"));
				}
			}
			if (project.getTheme() != null) {
				this.setTheme(project.getTheme().getKey());
			}
			if (project.getSource() != null) {
				this.setSource(new RecordDTO(project.getSource(), Boolean.TRUE));
			}
		}
	}

	public QuotationDTO(Boolean forExpenseDropdown, Quotation project) {
		this.setId(project.getId());
		this.setCategory(project.getCategory() != null ? project.getCategory().getName() : "");
		this.setTitle(project.getTitle());
		this.setStatus(project.getStatus() != null ? project.getStatus().getKey() : "");
		this.setFinancialStatus(project.getFinancialStatus());
		this.setStartDate(project.getStartDate());
		this.setEndDate(project.getEndDate());
		this.setDate(project.getDate());
		this.setTotalPriceIVAT(project.getTotalPriceIVAT());
		if (project.getSource() != null) {
			Float totalWithoutTaxAmount = project.getSource().getTotalPriceIVAT();
			this.setComparison(CommonUtil.projectTotalComparison(project.getTotalPriceIVAT(), totalWithoutTaxAmount));
			this.setSource(new RecordDTO(project.getSource(), Boolean.TRUE));
		}
	}

	public QuotationDTO(Quotation project, Boolean forExpenseDropdown) {
		this.setId(project.getId());
		this.setCategory(project.getCategory() != null ? project.getCategory().getName() : "");
		this.setTitle(project.getTitle());
		this.setStatus(project.getStatus() != null ? project.getStatus().getKey() : "");
		this.setFinancialStatus(project.getFinancialStatus());
		this.setStartDate(project.getStartDate());
		this.setEndDate(project.getEndDate());
		this.setDate(project.getDate());
		this.setTotalPriceIVAT(project.getTotalPriceIVAT());
	}

	public QuotationDTO(Quotation project, String recordDiscriminator, String templateTitle) {
		if (project != null) {
			this.setId(project.getId());
			this.setCategory(project.getCategory() != null ? project.getCategory().getName() : "");
			this.setTitle(project.getTitle());
			this.setStatus(project.getStatus() != null ? project.getStatus().getKey() : "");
			this.setRecordDiscriminator(recordDiscriminator);
			if (project.getCompany() != null) {
				this.setCompany(new CompanyDTO(project.getCompany()));
			}
			this.setCity(project.getCity());
			this.setCountry(project.getCountry() != null ? project.getCountry().getKey() : "");
			if (project.getDueDates() != null && !project.getDueDates().isEmpty()) {
				Optional<DueDate> optional = project.getDueDates().stream()
						.filter(d -> d.getType() != null && d.getType().getName().equalsIgnoreCase("delivery"))
						.findFirst();
				if (optional.isPresent()) {
					DueDate dueDate = optional.get();
					this.setDueDateForListView(CommonUtil.getStringFromCalendar(dueDate.getDate(), "dd/MM/yyyy"));
				}
			}
			this.setTemplateTitle(templateTitle);
			if (project.getTheme() != null) {
				this.setTheme(project.getTheme().getKey());
			}
		}
	}

	public QuotationDTO(long id, String category, String title, String status, String recordDiscriminator,
			String templateTitle) {
		this.setId(id);
		this.setCategory(category);
		this.setTitle(title);
		this.setStatus(status);
		this.setRecordDiscriminator(recordDiscriminator);
		this.setTemplateTitle(templateTitle);
	}

	public QuotationDTO(Quotation quotation) {
		super(quotation);
		if (quotation != null) {
			this.startDate = quotation.getStartDate();
			this.endDate = quotation.getEndDate();
			this.minuteDuration = quotation.getMinuteDuration();
			this.idPad = quotation.getIdPad();
			if (quotation.getChannel() != null) {
				ChannelDTO channelDTO = new ChannelDTO(quotation.getChannel());
				this.channel = channelDTO;
			}
			if (quotation.getProductionUnit() != null) {
				ProductionUnitDTO productionUnitDTO = new ProductionUnitDTO(quotation.getProductionUnit());
				this.productionUnit = productionUnitDTO;
			}
			List<Promo> promos = quotation.getPromoList();
			if (promos != null && !promos.isEmpty()) {
				this.promoList = promos.stream().map(promo -> new PromoDTO(promo)).collect(Collectors.toList());
			}

			XmlEntity delivery = quotation.getDelivery();
			this.delivery = delivery == null ? null : delivery.getKey();

			XmlEntity csa = quotation.getCsa();
			this.csa = csa == null ? null : csa.getKey();
			if (quotation.getTheme() != null) {
				this.setTheme(quotation.getTheme().getKey());
			}
		}
	}

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the minuteDuration
	 */
	public Integer getMinuteDuration() {
		return minuteDuration;
	}

	/**
	 * @param minuteDuration
	 *            the minuteDuration to set
	 */
	public void setMinuteDuration(Integer minuteDuration) {
		this.minuteDuration = minuteDuration;
	}

	/**
	 * @return the idPad
	 */
	public String getIdPad() {
		return idPad;
	}

	/**
	 * @param idPad
	 *            the idPad to set
	 */
	public void setIdPad(String idPad) {
		this.idPad = idPad;
	}

	/**
	 * @return the channel
	 */
	public ChannelDTO getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
	public void setChannel(ChannelDTO channel) {
		this.channel = channel;
	}

	/**
	 * @return the productionUnit
	 */
	public ProductionUnitDTO getProductionUnit() {
		return productionUnit;
	}

	/**
	 * @param productionUnit
	 *            the productionUnit to set
	 */
	public void setProductionUnit(ProductionUnitDTO productionUnit) {
		this.productionUnit = productionUnit;
	}

	/**
	 * @return the promoList
	 */
	public List<PromoDTO> getPromoList() {
		return promoList;
	}

	/**
	 * @param promoList
	 *            the promoList to set
	 */
	public void setPromoList(List<PromoDTO> promoList) {
		this.promoList = promoList;
	}

	/**
	 * @return the delivery
	 */
	public String getDelivery() {
		return delivery;
	}

	/**
	 * @param delivery
	 *            the delivery to set
	 */
	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	/**
	 * @return the csa
	 */
	public String getCsa() {
		return csa;
	}

	/**
	 * @param csa
	 *            the csa to set
	 */
	public void setCsa(String csa) {
		this.csa = csa;
	}

	public Quotation getQuotation(Quotation quotation, QuotationDTO quotationDTO) {
		setQuotationDetail(quotation, quotationDTO);
		return quotation;
	}

	@JsonIgnore
	public Quotation getQuotation() {
		return this.getQuotation(new Quotation(), this);
	}

	@JsonIgnore
	public void setQuotationDetail(Quotation quotation, QuotationDTO quotationDTO) {
		quotation.setStartDate(quotationDTO.getStartDate());
		quotation.setEndDate(quotationDTO.getEndDate());
		quotation.setMinuteDuration(quotationDTO.getMinuteDuration());
		quotation.setIdPad(quotationDTO.getIdPad());

		List<PromoDTO> promos = quotationDTO.getPromoList();
		if (promos != null && !promos.isEmpty()) {
			quotation.getPromoList().clear();
			quotation.setPromoList(promos.stream().map(dto -> {
				Promo entity = dto.getPromo(new Promo(), dto);
				return entity;
			}).collect(Collectors.toList()));
		}

		String delivery = quotationDTO.getDelivery();
		if (!StringUtils.isBlank(delivery)) {
			XmlEntity deliveryXml = new XmlEntity();
			deliveryXml.setKey(delivery);
			quotation.setDelivery(deliveryXml);
		}
		String csa = quotationDTO.getCsa();
		if (!StringUtils.isBlank(csa)) {
			XmlEntity csaXml = new XmlEntity();
			csaXml.setKey(csa);
			quotation.setCsa(csaXml);
		}

		quotationDTO.setAbstractProjectDetail(quotation, quotationDTO);
	}

	/**
	 * @return the validated
	 */
	public boolean isValidated() {
		return validated;
	}

	/**
	 * @param validated
	 *            the validated to set
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	/**
	 * @return the comparison
	 */
	public String getComparison() {
		return comparison;
	}

	/**
	 * @param comparison
	 *            the comparison to set
	 */
	public void setComparison(String comparison) {
		this.comparison = comparison;
	}

	/**
	 * 
	 * @return
	 */
	public Float getBookingTotalIVAT() {
		return bookingTotalIVAT;
	}

	/**
	 * 
	 * @param bookingTotalIVAT
	 */
	public void setBookingTotalIVAT(Float bookingTotalIVAT) {
		this.bookingTotalIVAT = bookingTotalIVAT;
	}

	/**
	 * 
	 * @return
	 */
	public String getComparisonTag() {
		return comparisonTag;
	}

	/**
	 * 
	 * @param comparisonTag
	 */
	public void setComparisonTag(String comparisonTag) {
		this.comparisonTag = comparisonTag;
	}				

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuotationDTO [startDate=" + startDate + ", endDate=" + endDate + ", minuteDuration=" + minuteDuration
				+ ", idPad=" + idPad + ", channel=" + channel + ", productionUnit=" + productionUnit + ", promoList="
				+ promoList + ", delivery=" + delivery + ", csa=" + csa + ", getProdAddress()=" + getProdAddress()
				+ ", getProdOrganisation()=" + getProdOrganisation() + ", getClient()=" + getClient() + ", getTitle()="
				+ getTitle() + ", getSubCategory()=" + getSubCategory() + ", getPonderation()=" + getPonderation()
				+ ", toString()=" + super.toString() + ", getId()=" + getId() + ", getDate()=" + getDate()
				+ ", getNumObjet()=" + getNumObjet() + ", getCurrency()=" + getCurrency()
				+ ", getPersistentExchangeRates()=" + getPersistentExchangeRates() + ", getTotalPrice()="
				+ getTotalPrice() + ", getDiscount()=" + getDiscount() + ", getDiscountRate()=" + getDiscountRate()
				+ ", getTotalNetPrice()=" + getTotalNetPrice() + ", getTotalPriceIVAT()=" + getTotalPriceIVAT()
				+ ", getTotalCost()=" + getTotalCost() + ", getPaymentTerms()=" + getPaymentTerms()
				+ ", getReferenceInternal()=" + getReferenceInternal() + ", getReferenceExternal()="
				+ getReferenceExternal() + ", getCity()=" + getCity() + ", getMargin()=" + getMargin()
				+ ", getCompany()=" + getCompany() + ", getFollower()=" + getFollower() + ", getStatus()=" + getStatus()
				+ ", getSaleEntity()=" + getSaleEntity() + ", getLanguage()=" + getLanguage() + ", getLocaleLanguage()="
				+ getLocaleLanguage() + ", getVatRates()=" + getVatRates() + ", getSource()=" + getSource()
				+ ", getCountry()=" + getCountry() + ", getContacts()=" + getContacts() + ", getDueDates()="
				+ getDueDates() + ", getLines()=" + getLines() + ", getFees()=" + getFees()
				+ ", getPurchaseOrderDate()=" + getPurchaseOrderDate() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}

}
