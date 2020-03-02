package com.teamium.dto;

import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.teamium.domain.prod.projects.Promo;


@SuppressWarnings("deprecation")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PromoDTO extends AbstractDTO {

	private Long id;
	private String title;
	private String idClean;
	private String rdv;
	private Integer week;
	private Date hour;

	public PromoDTO() {
	}

	public PromoDTO(Promo promo) {
		super(promo);
		this.id = promo.getId();
		this.title = promo.getTitle();
		this.idClean = promo.getIdClean();
		this.rdv = promo.getRdv();
		this.week = promo.getWeek();
		this.hour = promo.getHour();
	}

	/**
	 * To get promo
	 * 
	 * @param promo
	 *            the promo
	 * 
	 * @param promoDTO
	 *            the promoDTO
	 * 
	 * @return Promo object
	 */
	public Promo getPromo(Promo promo, PromoDTO promoDTO) {
		setPromoDetail(promo, promoDTO);
		return promo;
	}

	/**
	 * To set promo details
	 * 
	 * @param promo
	 *            the promo
	 * 
	 * @param promoDTO
	 *            the promoDTO
	 */
	public void setPromoDetail(Promo promo, PromoDTO promoDTO) {
		promo.setId(promoDTO.getId());
		promo.setVersion(promoDTO.getVersion());
		promo.setTitle(promoDTO.getTitle());
		promo.setIdClean(promoDTO.getIdClean());
		promo.setRdv(promoDTO.getRdv());
		promo.setWeek(promoDTO.getWeek());
		promo.setHour(promoDTO.getHour());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the idClean
	 */
	public String getIdClean() {
		return idClean;
	}

	/**
	 * @param idClean
	 *            the idClean to set
	 */
	public void setIdClean(String idClean) {
		this.idClean = idClean;
	}

	/**
	 * @return the rdv
	 */
	public String getRdv() {
		return rdv;
	}

	/**
	 * @param rdv
	 *            the rdv to set
	 */
	public void setRdv(String rdv) {
		this.rdv = rdv;
	}

	/**
	 * @return the week
	 */
	public Integer getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            the week to set
	 */
	public void setWeek(Integer week) {
		this.week = week;
	}

	/**
	 * @return the hour
	 */
	public Date getHour() {
		return hour;
	}

	/**
	 * @param hour
	 *            the hour to set
	 */
	public void setHour(Date hour) {
		this.hour = hour;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PromoDTO [id=" + id + ", title=" + title + ", idClean=" + idClean + ", rdv=" + rdv + ", week=" + week
				+ ", hour=" + hour + "]";
	}

}
