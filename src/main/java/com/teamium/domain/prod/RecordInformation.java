/**
 * 
 */
package com.teamium.domain.prod;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyClass;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import com.teamium.domain.Person;
import com.teamium.domain.TeamiumException;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.projects.Brief;
import com.teamium.domain.prod.resources.Guest;
import com.teamium.domain.prod.resources.staff.StaffMember;

/**
 * Describes a kind of information about the project
 * @author sraybaud
 * @version TEAM-18
 *
 */
@Embeddable
public class RecordInformation implements Serializable, Cloneable{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 6096813889790032505L;

	/**
	 * Information about the max lenght
	 */
	@Column(name="c_info_maxlength")
	private String length; 
	
	/**
	 * Information about the format
	 * @see com.teamium.domain.prod.projects.ProjectFormat
	 */
	@Embedded
	@AttributeOverride(name="key", column=@Column(name="c_info_format"))
	private XmlEntity format;
	
	/**
	 * Information about the version
	 */
	@Column(name="c_info_version")
	private String version;
	
	/**
	 * Comment
	 */
	@Column(name="c_info_comment")
	private String comment;


	/**
	 * Checked status
	 */
	@ElementCollection(targetClass = java.lang.Boolean.class)
	@MapKeyClass(java.lang.String.class)
    @MapKeyColumn(name="c_name")
	@Column(name="c_status")
    @CollectionTable(name="t_record_informations_status", joinColumns=@JoinColumn(name="c_idrecord"))
	private Map<String,Boolean> status;
	
	
	/**
	 * The different briefs of the project
	 */
	@OneToMany(cascade={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinColumn(name="c_idrecord")
	private List<Brief> briefs;
	
	/**
	 * The members of the production
	 */
	@JoinTable(name="t_program_members", joinColumns = @JoinColumn(name="c_idrecord"), 
			inverseJoinColumns=@JoinColumn(name="c_idperson"))
	@ManyToMany(cascade={CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private List<Person> programMembers;
	
	/**
	 * Clone the current object
	 * @return the clone
	 * @throws CloneNotSupportedException 
	 */
	@Override
	public RecordInformation clone() throws CloneNotSupportedException{
		XmlEntity format = this.format;
		this.format=null;
		List<Brief> briefs = this.briefs;
		this.briefs = null;
		Map<String, Boolean> status = this.status;
		List<Person> programMembers = this.programMembers;
		this.programMembers = null;
		try{
			RecordInformation clone = (RecordInformation) super.clone();
			clone.format=format;
			
			clone.setBriefs(null);
			clone.setStatus(null);
			clone.setProgramMembers(null);
			
			clone.setBriefs(new ArrayList<Brief>(briefs.size()));
			for(Brief brief : briefs){
				clone.getBriefs().add((Brief)brief.clone());
			}
			clone.setStatus(new HashMap<String, Boolean>(status.size()));
			for(Entry<String, Boolean> entry : status.entrySet()){
				clone.getStatus().put(entry.getKey(), entry.getValue());
			}
			clone.setProgramMembers(new ArrayList<Person>(programMembers.size()));
			for(Person member : programMembers){
				if (member instanceof Guest){
					clone.getProgramMembers().add(member.clone());
				}
				else{
					//TODO report
					//clone.getProgramMembers().add(member);
				}
			}
			
			return clone;
		}
		catch(CloneNotSupportedException e){
			throw e;
		}
		finally{
			this.format=format;
			this.briefs = briefs;
			this.status = status;
			this.programMembers = programMembers;
		}
	}
	
	/**
	 * @return information about the max lenght
	 */
	public String getLength() {
		return length;
	}
	
	/**
	 * Update information about the max lenght
	 * @param length
	 */
	public void setLength(String length) {
		this.length = length;
	}
	
	/**
	 * @return information about the format
	 */
	public XmlEntity getFormat() {
		return format;
	}
	
	/**
	 * Update information about the format
	 * @param format
	 */
	public void setFormat(XmlEntity format) {
		this.format = format;
	}
	
	/**
	 * @return information about the version
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Update information about the version
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * @return the briefs
	 */
	public List<Brief> getBriefs() {
		if(briefs == null){
			briefs = new ArrayList<Brief>();
		}
		return briefs;
	}

	/**
	 * @param briefs the briefs to set
	 */
	public void setBriefs(List<Brief> briefs) {
		this.briefs = briefs;
	}

	/**
	 * Get the status
	 * @return The status
	 */
	public Map<String, Boolean> getStatus(){
		if(this.status == null){
			this.status = new HashMap<String, Boolean>() ;
		}
		return this.status;
	}
	/**
	 * status Set status
	 * @param The status to set
	 */
	public void setStatus(Map<String, Boolean>status){
		this.status = status;
	}
	
	/**
	 * @return the program members
	 */
	public List<Person> getProgramMembers() {
		if(programMembers == null){
			programMembers = new ArrayList<Person>();
		}
		return programMembers;
	}

	/**
	 * @param program Members the program Members to set
	 */
	public void setProgramMembers(List<Person> programMembers) {
		this.programMembers = programMembers;
	}
	
	 
	/**
	 * Get the list of the guest of the TV show
	 * @return the list of staff member for the production
	 */
	@SuppressWarnings("unchecked")
	public List<Guest> getGuests(){
		return (List<Guest>)getFilterProductionMember(Guest.class);
	}
	
	/**
	 * Get the list of staff member of the TV show
	 * @return the list of staff member for the production
	 */
	@SuppressWarnings("unchecked")
	public List<StaffMember> getStaffProduction(){
		return  (List<StaffMember>)getFilterProductionMember(StaffMember.class);
	}
	
	/**
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Update comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Get the list of person filter by the class given in parameter
	 * @param type The class type to filter
	 * @return the list of person filter by the class given in parameter
	 */
	private List<? extends Person> getFilterProductionMember(Class<? extends Person> type){
		List<Person> filteredProductionMembers = new ArrayList<Person>();
		for(Person productionMember : this.programMembers){
			if (productionMember.getClass().equals(type))
				filteredProductionMembers.add(productionMember);
		}
		return filteredProductionMembers;
	}
	
	
	/**
	 * Add an attendee to the production members
	 * @return True if the attendee is correctly added
	 */
	public boolean  addToProgramMembers(Person attendee){
		return this.programMembers.add(attendee);
	}
	
	
	/**
	 * Add a guest to the production members
	 * @param
	 */
	public Person addToProgramGuests() throws TeamiumException{
		Person attendee = null;
		try {
			attendee = Guest.class.newInstance();
			if(!this.addToProgramMembers(attendee)){
				attendee = null;
			}
		} catch (Exception e) {
			throw new TeamiumException("Unable to initialize a guest for tv show", e);
		}
		
		return attendee;
	}
}
