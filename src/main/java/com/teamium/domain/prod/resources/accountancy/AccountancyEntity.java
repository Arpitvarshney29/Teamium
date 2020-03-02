/**
 * 
 */
package com.teamium.domain.prod.resources.accountancy;

import java.util.List;

/**
 * @author slopes
 * Entity allowed to be used in the accountancy context
 *
 */
public interface AccountancyEntity {
	/**
	 * @return the accountancyNumbers
	 */
	List<AccountancyNumber> getAccountancyNumbers();
}
