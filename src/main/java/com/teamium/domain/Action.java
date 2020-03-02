/**
 * 
 */
package com.teamium.domain;

import java.util.Calendar;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Describe an action
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name="t_action")
@NamedQueries({
	@NamedQuery(name=Action.QUERY_findByPerson, query="SELECT a FROM Action a WHERE a.targetPerson = ?1 ORDER BY a.date DESC"),
	@NamedQuery(name=Action.QUERY_findByCompany , query="SELECT a FROM Action a WHERE a.targetPerson IN (?1) ORDER BY a.date DESC"),
})
public class Action extends AbstractEntity implements Comparable<Action>{
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -6522230516348743229L;
	
	/**
	 * Name of the query retrieving actions targeting the given contact
	 * @param 1 the targeted contact
	 * @return the list of actions
	 */
	public static final String QUERY_findByPerson = "findActionByPerson";
	
	/**
	 * Name of the query retrieving actions targeting all contacts from the given list of targeted persons
	 * @param 1 the list of targeted persons
	 * @return the list of actions
	 */
	public static final String QUERY_findByCompany = "findActionByPersons";
	
	/**
	 * Name of the query retrieving actions followed by the given staff member and the given date
	 * @param 1 the given follower
	 * @param 2 the given date
	 * @param 3 the given type of company
	 * @return the list of actions
	 */
	public static final String QUERY_findByFollowerByDateByCompanyType = "findActionByFollowerByCompanyTypeByDate";
	
	/**
	 * Name of the query retrieving actions followed by the given staff member and the given date with status among the given list of status
	 * @param 1 the given follower
	 * @param 2 the given date
	 * @param 3 the given type of company
	 * @param 4 the given list of status
	 * @return the list of actions
	 */
	public static final String QUERY_findByFollowerByDateByCompanyTypeByStatus = "findActionByFollowerByDateByCompanyTypeByStatus";
	
	/**
	 * Action ID
	 */
	@Id
	@Column(name="c_idaction", insertable=false, updatable=false)
	@TableGenerator( name = "idAction_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "action_idaction_seq", 
						valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1 )
	@GeneratedValue(strategy=GenerationType.TABLE, generator="idAction_seq")
	private Long id;
	
	/**
	 * Date
	 */
	@Column(name="c_actiondate")
	@Temporal(TemporalType.DATE)
	private Calendar date;
	
	/**
	 * Subject
	 */
	@Column(name="c_subject")
	private String subject;
	
	/**
	 * Summary
	 */
	@Column(name="c_summary")
	private String summary;
	
	/**
	 * Targeted contact
	 */
	@ManyToOne(targetEntity=Person.class, fetch=FetchType.EAGER)
	@JoinColumn(name="c_idtargetperson")
	private Person targetPerson;
	
	/**
	 * Follower
	 */
	@ManyToOne(targetEntity=Person.class, fetch=FetchType.EAGER)
	@JoinColumn(name="c_idfollower")
	private Person follower;
	
	/**
	 * Type
	 * @see com.teamium.domain.prod.resources.ActionType
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_actiontype"))
	private XmlEntity type;
	
	/**
	 * Priorité
	 */
	@Column(name="c_priority")
	private Integer priority;
	
	/**
	 * Status
	 * @see com.teamium.domain.prod.resources.ActionStatus
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_status"))
	private XmlEntity status;
	
	/**
	 * Visibility
	 * @see com.teamium.domain.prod.resources.ActionVisibility
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_visibility"))
	private XmlEntity visibility;
	
	/*
	 * Informations manquantes à valider auprès de YD :
	 * - date de saisie du statut,
	 * - editeur du statut,
	 * - référencement document,
	 * - document,
	 * - type de document
	 * - version,
	 * - mots clés du document
	 */

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
	 * @return the date
	 */
	public Calendar getDate() {
		if (date == null)
			date = Calendar.getInstance();
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * @return the target person
	 */
	public Person getTargetPerson() {
		return targetPerson;
	}

	/**
	 * @param targetContact the targetedContact to set
	 */
	public void setTargetPerson(Person targetPerson) {
		this.targetPerson = targetPerson;
	}

	/**
	 * @return the follower
	 */
	public Person getFollower() {
		return follower;
	}

	/**
	 * @param follower the follower to set
	 */
	public void setFollower(Person follower) {
		this.follower = follower;
	}

	/**
	 * @return the type
	 */
	public XmlEntity getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(XmlEntity type) {
		this.type = type;
	}

	/**
	 * @return the priority
	 */
	public Integer getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * @return the status
	 */
	public XmlEntity getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(XmlEntity status) {
		this.status = status;
	}

	/**
	 * @return the visibility
	 */
	public XmlEntity getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(XmlEntity visibility) {
		this.visibility = visibility;
	}

	/**
	 * Compare the current objet with the given one
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given is previous
	 */
	@Override
	public int compareTo(Action o) {
		if(o==null) return 1;
		if(this.date==null){
			if(o.date!=null){
				return -1;
			}
		}else{
			if(o.date==null) return 1;
			int compare = this.date.compareTo(o.date);
			if(compare !=0) return compare;
		}
		if(this.id==null){
			if(o.id!=null){
				return -1;
			}
		}else{
			int compare = this.id.compareTo(o.id);
			if(compare !=0) return compare;
		}
		return 0;
	}
			
}
