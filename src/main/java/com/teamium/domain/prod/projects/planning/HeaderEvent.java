/**
 * 
 */
package com.teamium.domain.prod.projects.planning;

import java.util.Calendar;

import com.teamium.domain.Theme;
import com.teamium.domain.XmlEntity;
import com.teamium.domain.prod.resources.Resource;

/**
 * @author sraybaud
 *
 */
public class HeaderEvent extends Event {
	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -2135317755041587023L;

	/**
	 * Resource
	 */
	private Resource resource;
	
	/**
	 * From
	 */
	private Calendar from;
	
	/**
	 * To
	 */
	private Calendar to;

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getFrom()
	 */
	@Override
	public Calendar getFrom() {
		return this.from;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#setFrom(java.util.Calendar)
	 */
	@Override
	public void setFrom(Calendar date) {
		this.from=date;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getTo()
	 */
	@Override
	public Calendar getTo() {
		return this.to;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#setTo(java.util.Calendar)
	 */
	@Override
	public void setTo(Calendar date) {
		this.to=date;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getResource()
	 */
	@Override
	public Resource getResource() {
		return this.resource;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#setResource(com.teamium.domain.prod.resources.Resource)
	 */
	@Override
	public void setResource(Resource resource) {
		this.resource=resource;

	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getEditable()
	 */
	@Override
	public Boolean getEditable() {
		return false;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#setEditable(java.lang.Boolean)
	 */
	@Override
	public void setEditable(Boolean bool) {}
	
	/**
	 * Hide on large scale
	 * @return true if the event has to be hidden on large scale
	 */
	public boolean getHideOnLargeScale(){
		return false;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getTheme()
	 */
	@Override
	public Theme getTheme() {
		return null;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getTitle()
	 */
	@Override
	public String getTitle() {
		return null;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getDescription()
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getStatus()
	 */
	@Override
	public XmlEntity getStatus() {
		return null;
	}

	/**
	 * @see com.teamium.domain.prod.projects.planning.Event#getCompletion()
	 */
	@Override
	public Float getCompletion() {
		return null;
	}
	
	/**
	 * Return false
	 * @return the superposition
	 */
	public Boolean getSuperposition() {
		return false;
	}

	/**
	 * DO NOTHING
	 * @param superposition the superposition to set
	 */
	public void setSuperposition(Boolean superposition) {
		
	}

}
