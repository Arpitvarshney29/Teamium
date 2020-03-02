package com.teamium.domain.prod.projects.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;

@Entity
@Table(name = "t_work_and_travel_order")
public class WorkAndTravelOrder extends AbstractEntity {

	/**
	 * Auto generated id.
	 */
	private static final long serialVersionUID = 6438637199141218796L;

	@Id
	@Column(name = "c_idworkandtravelorder", insertable = false, updatable = false)
	@TableGenerator(name = "idWorkAndTravelOrder_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "workAndTravelOrder_idWorkAndTravelOrder_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idWorkAndTravelOrder_seq")
	private Long id;

	@Column(name = "c_form_type")
	@NotNull
	private String formType;

	@OneToMany(orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "c_work_and_travel_order_id")
	private List<OrderKeyword> keywords = new ArrayList<>();

	@Column(name = "c_comment")
	private String comment;

	@Column(name = "c_mediaid", unique = true)
	private String mediaId;

	public static enum OrderType {
		WORK_ORDER, TRAVEL_ORDER, MEDIA_ORDER;

		public static List<String> getAllOrderTypes() {
			return Arrays.asList("WORK_ORDER", "TRAVEL_ORDER", "MEDIA_ORDER");
		}
	}

	@Column(name = "c_order_type")
	@Enumerated(EnumType.STRING)
	@NotNull
	private OrderType orderType;

	public static enum Status {
		CREATED, IN_PROGRESS, DONE
	}

	@Column(name = "c_status")
	private int status;

	@Column(name = "c_project_id")
	private long projectId;

	public WorkAndTravelOrder() {

	}

	public WorkAndTravelOrder(int status, OrderType orderType) {
		this.status = status;
		this.orderType = orderType;
	}

	public WorkAndTravelOrder(WorkAndTravelOrder workAndTravelOrder) {
		if (workAndTravelOrder.getId() != null) {
			this.id = workAndTravelOrder.getId();
			this.setVersion(workAndTravelOrder.getVersion());
			this.status = workAndTravelOrder.getStatus();
		} else {
			this.status = 0;
		}

		this.comment = workAndTravelOrder.getComment();
		this.mediaId = workAndTravelOrder.getMediaId();
		this.orderType = workAndTravelOrder.getOrderType();
		this.keywords = workAndTravelOrder.getKeywords();
		this.formType = workAndTravelOrder.getFormType();

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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the orderType
	 */
	public OrderType getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	 * @return the mediaId
	 */
	public String getMediaId() {
		return mediaId;
	}

	/**
	 * @param mediaId the mediaId to set
	 */
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	/**
	 * @return the formType
	 */
	public String getFormType() {
		return formType;
	}

	/**
	 * @param formType the formType to set
	 */
	public void setFormType(String formType) {
		this.formType = formType;
	}

	/**
	 * @return the keywords
	 */
	public List<OrderKeyword> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(List<OrderKeyword> keywords) {
		this.keywords = keywords;
	}

	@Override
	public String toString() {
		return "WorkAndTravelOrder [id=" + id + ", comment=" + comment + ", orderType=" + orderType + ", status="
				+ status + "]";
	}

}
