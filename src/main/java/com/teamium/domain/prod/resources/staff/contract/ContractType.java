/**
 * 
 */
package com.teamium.domain.prod.resources.staff.contract;

import javax.persistence.Embeddable;

import com.teamium.domain.ClassType;
import com.teamium.domain.TeamiumPersistenceException;

/**
 * Describe a contract type
 * @author sraybaud - NovaRem
 *
 */
@Embeddable
public class ContractType extends ClassType{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = -333256114162673300L;

	/**
	 * @see com.teamium.domain.ClassType#newInstance()
	 * @return the created instance
	 * @throws TeamiumPersistenceException classType_create_failed
	 */
	@Override
	public ContractSetting newInstance() throws TeamiumPersistenceException {
		ContractSetting setting=null;
		try{
			setting = (ContractSetting) super.newInstance();
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
