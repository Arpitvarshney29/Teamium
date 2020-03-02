/**
 * 
 */
package com.teamium.domain.prod.projects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.Channel;
import com.teamium.domain.ProductionUnit;
import com.teamium.domain.XmlEntity;

/**
 * Describes a ball park
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue(Quotation.DISCRIMINATOR_VALUE)
@NamedQueries({
		@NamedQuery(name = Quotation.QUERY_findByStatusByPeriod, query = "SELECT q FROM Quotation q WHERE q.status IN(?1) AND TYPE(q) = ?2 AND q.startDate <= ?4 AND q.endDate >= ?3"),
		@NamedQuery(name = Quotation.QUERY_findByStatusByPeriodByProductionUnit, query = "SELECT q FROM Quotation q WHERE q.status IN(?1) AND TYPE(q) = ?2 AND q.startDate <= ?4 AND q.endDate >= ?3 AND q.productionUnit IN (?5)"), })
public class Quotation extends AbstractProject {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -8027390109445523318L;

	/**
	 * The discriminator value of the current class
	 */
	public static final String DISCRIMINATOR_VALUE = "quotation";

	/**
	 * Name of the query retrieving the quotation for the given status
	 * 
	 * @param 1 the given status
	 * @param 2 the type of record
	 * @param 3 start period
	 * @param 4 end period
	 * @return the quotation
	 */
	public static final String QUERY_findByStatusByPeriod = "findByStatusByPeriod";

	/**
	 * Name of the query retrieving the quotation for the given status
	 * 
	 * @param 1 the given status
	 * @param 2 the type of record
	 * @param 3 start period
	 * @param 4 end period
	 * @param 5 production unit
	 * @return the quotation
	 */
	public static final String QUERY_findByStatusByPeriodByProductionUnit = "findByStatusByPeriodByProductionUnit";

	/**
	 * Start date of the project
	 */
	@Column(name = "c_project_start")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar startDate;

	/**
	 * End date of the project
	 */
	@Column(name = "c_project_end")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar endDate;

	/**
	 * The duration of the TV show in minutes
	 */
	@Column(name = "c_minute_duration")
	private Integer minuteDuration;

	/**
	 * The channel
	 */
	@ManyToOne
	@JoinColumn(name = "c_project_idchannel")
	private Channel channel;

	/**
	 * The production unit
	 */
	@ManyToOne
	@JoinColumn(name = "c_project_idprodunit")
	private ProductionUnit productionUnit;

	/**
	 * Entity for delivery
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_delivery"))
	private XmlEntity delivery;

	/**
	 * CSA
	 */
	@Embedded
	@AttributeOverride(name = "key", column = @Column(name = "c_csa"))
	private XmlEntity csa;

	/**
	 * Id for pad
	 */
	@Column(name = "c_idpad")
	private String idPad;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "c_quotation")
	private List<Promo> promoList;

	/**
	 * @return the startDate
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
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
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * @return the productionUnit
	 */
	public ProductionUnit getProductionUnit() {
		if (this.productionUnit == null && this.getProgram() != null) {
			// this.productionUnit = this.getProgram().getProdUnit();
		}
		return productionUnit;
	}

	/**
	 * @param productionUnit the productionUnit to set
	 */
	public void setProductionUnit(ProductionUnit productionUnit) {
		this.productionUnit = productionUnit;
	}

	/**
	 * Clone the current object
	 * 
	 * @return the clone
	 * @throws CloneNotSupportedException
	 */
	@Override
	public Quotation clone() throws CloneNotSupportedException {
		Quotation clone = null;
		Channel channel = this.channel;
		this.channel = null;
		ProductionUnit prodUnit = this.productionUnit;
		Program program = this.getProgram();
		this.setProgram(null);
		try {
			this.productionUnit = null;
			clone = (Quotation) super.clone();
			clone.channel = channel;
			clone.productionUnit = prodUnit;
			clone.setProgram(program);
			clone.setPromoList(null);
			clone.setMinuteDuration(null);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw e;
		} finally {
			this.channel = channel;
			this.productionUnit = prodUnit;
			this.setProgram(program);
		}

	}

	/**
	 * Return the id pad
	 */
	public String getIdPad() {
		return idPad;
	}

	/**
	 * Update the id pad
	 */
	public void setIdPad(String idPad) {
		this.idPad = idPad;
	}

	/**
	 * Return csa
	 */
	public XmlEntity getCsa() {
		return csa;
	}

	/**
	 * Update csa
	 */
	public void setCsa(XmlEntity csa) {
		this.csa = csa;
	}

	/**
	 * Return delivery
	 */
	public XmlEntity getDelivery() {
		return delivery;
	}

	/**
	 * Update delivery
	 */
	public void setDelivery(XmlEntity delivery) {
		this.delivery = delivery;
	}

	public List<Promo> getPromoList() {
		if (promoList == null)
			promoList = new ArrayList<Promo>();
		return promoList;
	}

	public void setPromoList(List<Promo> promoList) {
		this.promoList = promoList;
	}

	public Integer getMinuteDuration() {
		if (minuteDuration == null && this.getProgram() != null) {
			try {
				minuteDuration = this.getProgram().getMinuteDuration();
			} catch (Exception e) {
			}
		}
		return minuteDuration;
	}

	public void setMinuteDuration(Integer minuteDuration) {
		this.minuteDuration = minuteDuration;
	}

}
