/**
 * 
 */
package com.teamium.domain.prod.resources.contacts;

import javax.persistence.Embeddable;

import com.teamium.domain.ClassType;
import com.teamium.domain.TeamiumPersistenceException;

/**
 * Describe an equipment type
 * @author sraybaud - NovaRem
 *
 */
@Embeddable
public class RatePolicyType extends ClassType{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -4963313258798025984L;
	


	/**
	 * @see com.teamium.domain.ClassType#newInstance()
	 * @return the created instance
	 * @throws TeamiumPersistenceException classType_create_failed
	 */
	@Override
	public RatePolicy newInstance() throws TeamiumPersistenceException {
		RatePolicy setting=null;
		try{
			setting = (RatePolicy) super.newInstance();
			setting.setType(this);
			return setting;
		}
		catch(TeamiumPersistenceException e){
			throw e;
		}
		catch(Exception e){
			throw new TeamiumPersistenceException("classType_create_failed", e);
		}
	}
	
	

}
