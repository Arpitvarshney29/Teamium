package com.teamium.domain.prod.projects;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.teamium.domain.AbstractEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.XmlEntity;

@Entity
@Table(name="t_promo")
public class Promo extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8653785827043755604L;
	/**
	 * Header ID
	 */
	@Id
	@Column(name="c_id", insertable=false, updatable=false)
	@TableGenerator( name = "idPromo_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "promo_idpromo_seq", 
	valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idPromo_seq")
	private Long id;
	
	@Column(name="c_title")
	private String title;
	
	@Column(name="c_idclean")
	private String idClean;
	
	@Column(name="c_rdv")
	private String rdv;
	
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_csa"))
	private XmlEntity csa;
	
	@Column(name="c_week")
	private Integer week;
	
	@Column(name="c_hour")
	@Temporal(TemporalType.TIME)
	private Date hour;
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIdClean() {
		return idClean;
	}

	public void setIdClean(String idClean) {
		this.idClean = idClean;
	}

	public String getRdv() {
		return rdv;
	}

	public void setRdv(String rdv) {
		this.rdv = rdv;
	}

	public XmlEntity getCsa() {
		return csa;
	}

	public void setCsa(XmlEntity csa) {
		this.csa = csa;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Date getHour() {
		return hour;
	}

	public void setHour(Date hour) {
		this.hour = hour;
	}
	

}
