package com.teamium.domain.prod.projects.planning;

import java.util.Calendar;
import java.util.Date;

import com.teamium.domain.prod.projects.planning.Event;
import com.teamium.domain.prod.resources.Resource;

public class RosterUnavailabilityEvent extends Event {

	private Resource<?> resource;
	private Date starDate;
	private Date endDate;
	private Long rosterUnavailabilityId;

	public RosterUnavailabilityEvent() {

	}

	public RosterUnavailabilityEvent(Resource<?> resource, Date starDate, Date endDate, Long rosterUnavailabilityId) {

		Calendar start = Calendar.getInstance();
		start.setTime(starDate);
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		super.setFrom(start);
		super.setTo(end);
		super.setEditable(false);
		super.setResource(resource);
		this.resource = resource;
		super.setTitle("Roster unavailable");
		this.starDate = starDate;
		this.endDate = endDate;
		this.rosterUnavailabilityId = rosterUnavailabilityId;
	}

	/**
	 * @return the resource
	 */
	public Resource<?> getResource() {
		return resource;
	}

	/**
	 * @param resource
	 *            the resource to set
	 */
	public void setResource(Resource<?> resource) {
		this.resource = resource;
	}

	/**
	 * @return the starDate
	 */
	public Date getStarDate() {
		return starDate;
	}

	/**
	 * @param starDate
	 *            the starDate to set
	 */
	public void setStarDate(Date starDate) {
		this.starDate = starDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the rosterUnavailabilityId
	 */
	public Long getRosterUnavailabilityId() {
		return rosterUnavailabilityId;
	}

	/**
	 * @param rosterUnavailabilityId
	 *            the rosterUnavailabilityId to set
	 */
	public void setRosterUnavailabilityId(Long rosterUnavailabilityId) {
		this.rosterUnavailabilityId = rosterUnavailabilityId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((rosterUnavailabilityId == null) ? 0 : rosterUnavailabilityId.hashCode());
		result = prime * result + ((starDate == null) ? 0 : starDate.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RosterUnavailabilityEvent other = (RosterUnavailabilityEvent) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (rosterUnavailabilityId == null) {
			if (other.rosterUnavailabilityId != null)
				return false;
		} else if (!rosterUnavailabilityId.equals(other.rosterUnavailabilityId))
			return false;
		if (starDate == null) {
			if (other.starDate != null)
				return false;
		} else if (!starDate.equals(other.starDate))
			return false;
		return true;
	}



}
