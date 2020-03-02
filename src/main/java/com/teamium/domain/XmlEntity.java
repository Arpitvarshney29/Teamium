/**
 * 
 */
package com.teamium.domain;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * This class is a concrete class derivated from the AbstractXmlEntity (@see com.teamium.domain.AbstractXmlEntity) that permits
 * to instanciate generic xml entities.
 * 
 * Instances of each type of xml entity are saved into the data/teamium/shared/entities directory with a given package that is specified
 * as javadoc for each class properties based on the XmlEntity type.
 * @author sraybaud - NovaRem
 *
 */
@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEntity extends AbstractXmlEntity{

	/**
	 *  Generated ID
	 */
	private static final long serialVersionUID = -8642874105699038049L;

}
