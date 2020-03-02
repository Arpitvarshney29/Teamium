/**
 * 
 */
package com.teamium.domain.prod.projects;

import javax.persistence.Embeddable;

import com.teamium.domain.AbstractXmlEntity;

/**
 * Describe a completion status
 * @author sraybaud - NovaRem
 *
 */
@Embeddable
public class CompletionStatus extends AbstractXmlEntity{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 557740252031156567L;
	
	/**
	 * Default constructor
	 */
	public CompletionStatus(){
		super();
	}

	/**
	 * Construct a completion status with the given key
	 */
	public CompletionStatus(String key) {
		this.setKey(key);
	}

}
