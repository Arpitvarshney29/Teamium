/**
 * 
 */
package com.teamium.domain.prod.projects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Describes a ball park
 * @author sraybaud - NovaRem
 *
 */
@Entity
@DiscriminatorValue(Layout.DISCRIMINATOR_VALUE)
@NamedQueries({
	@NamedQuery(name=Layout.QUERY_findAll, query="SELECT l FROM Layout l ORDER BY l.title ASC"),
	@NamedQuery(name=Layout.QUERY_find_By_Type_Line, query="SELECT l FROM Layout l WHERE EXISTS(SELECT li FROM Line li WHERE TYPE(li) = ?1 AND li.record = l) ORDER BY l.title ASC"),
})
public class Layout extends AbstractProject {

	/**
	 * Class UID
	 */
	private static final long serialVersionUID = 7955932619658016363L;
	
	/**
	 * The discriminator value of the current class
	 */
	public static final String DISCRIMINATOR_VALUE = "layout";
	
	/**
	 * Name of the query retrieving all project layouts from persistence unit
	 * @return the list of all projects
	 */
	public static final String QUERY_findAll = "findAllLayout";
	
	/**
	 * Name of the query retrieving all project layouts from persistence unit
	 * @param 1 type of line
	 */
	public static final String QUERY_find_By_Type_Line = "layout_find_by_type_line";



}
