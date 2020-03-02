package com.teamium.domain.prod.projects.order;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

@Entity
@Table(name = "t_booking_order_form")
@NamedQueries({ @NamedQuery(name = BookingOrderForm.QUERY_FINDAll, query = "SELECT bof FROM BookingOrderForm bof") })
public class BookingOrderForm extends AbstractEntity {

	/**
	 * Auto Generated Value
	 * 
	 */
	private static final long serialVersionUID = 561545602100423061L;

	public static final String QUERY_FINDAll = "findAllBookingOrderForm";
	@Id
	@Column(name = "c_idbookingorderform", insertable = false, updatable = false)
	@TableGenerator(name = "idBookingOrderForm_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "booking_order_form_idForm_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idBookingOrderForm_seq")
	private Long id;

	@Column(name = "c_form_type")
	@NotNull
	private String formType;

	@NotNull
	@Column(name = "c_order_type")
	private String orderType;

	@OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_booking_order_form_id")
	private List<Keyword> keyword=new ArrayList<Keyword>();
	
	public BookingOrderForm() {
		
	}

	public BookingOrderForm(Long id,Integer version, String formType, String orderType, List<Keyword> keyword) {
		if(id!=null) {
			this.id = id;
			
		}
		if(version!=null) {
			this.setVersion(version);
		}
		this.formType = formType;
		this.orderType = orderType;
		this.keyword = keyword;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the formType
	 */
	public String getFormType() {
		return formType;
	}

	/**
	 * @param formType
	 *            the formType to set
	 */
	public void setFormType(String formType) {
		this.formType = formType;
	}

	/**
	 * @return the keyword
	 */
	public List<Keyword> getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(List<Keyword> keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType
	 *            the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "BookingOrderForm [id=" + id + ", formType=" + formType + ", orderType=" + orderType + ", keyword="
				+ keyword + "]";
	}
}