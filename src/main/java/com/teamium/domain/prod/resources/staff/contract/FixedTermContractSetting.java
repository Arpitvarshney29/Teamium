/**
 * 
 */
package com.teamium.domain.prod.resources.staff.contract;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Describe a fixed term contract settings
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue("fixedtermcontractsetting")
public class FixedTermContractSetting extends ContractSetting{

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 7647266514657101748L;


}
