/**
 * 
 */
package com.teamium.domain.prod.resources.staff;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.teamium.domain.Skill;
import com.teamium.domain.TeamiumConstants;

/**
 * Describe a staff member skill
 * 
 * @author sraybaud - NovaRem
 *
 */
@Entity
@Table(name = "t_staff_skill")
public class StaffMemberSkill implements Serializable, Comparable<StaffMemberSkill> {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -4158934806409769105L;

	/**
	 * Skill ID
	 */
	@Id
	@Column(name = "c_idstaffskill", insertable = false, updatable = false)
	@TableGenerator(name = "idStaffSkill_seq", table = TeamiumConstants.TeamiumSequenceTable, pkColumnName = TeamiumConstants.TeamiumSequencePkColumnName, pkColumnValue = "staff_skill_idskill_seq", valueColumnName = TeamiumConstants.TeamiumSequenceColumnValue, initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idStaffSkill_seq")
	private Long id;

	@OneToOne
	@JoinColumn(name = "c_idskill")
	private Skill skill;

	/**
	 * Skill rating
	 */
	@Column(name = "c_rating")
	private Integer rating;

	/**
	 * @return the skill
	 */
	public Skill getSkill() {
		return skill;
	}

	/**
	 * @param skill the skill to set
	 */
	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	/**
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

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
	 * Compare the current object with the given one
	 * 
	 * @param other the other to compare
	 * @return -1 if the current is previous, 0 if both are equal, +1 if the given
	 *         is previous
	 */
	@Override
	public int compareTo(StaffMemberSkill o) {
		if (o == null)
			return 1;
		if (this.skill == null) {
			if (o.skill != null) {
				return -1;
			}
		} else {
			int compare = this.skill.getName().compareTo(o.skill.getName());
			if (compare != 0)
				return compare;
		}
		return 0;
	}
}
