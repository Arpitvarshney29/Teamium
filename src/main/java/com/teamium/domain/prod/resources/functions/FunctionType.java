package com.teamium.domain.prod.resources.functions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.teamium.domain.AbstractXmlEntity;
import com.teamium.domain.TeamiumConstants;
import com.teamium.domain.utils.FunctionClassAdapter;

/**
 * Class who describe functions' classes
 * @author JS
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class FunctionType extends AbstractXmlEntity {

	/**
	 * Generated ID
	 */
	private static final long serialVersionUID = -9153281946161919127L;
	
	@XmlElement(name="class",namespace=TeamiumConstants.XMLNS)
	@XmlJavaTypeAdapter(FunctionClassAdapter.class)
	private Class<? extends RatedFunction> functionClass;

	/**
	 * @return the functionClass
	 */
	public Class<? extends RatedFunction> getFunctionClass() {
		return functionClass;
	}

	/**
	 * @param functionClass the functionClass to set
	 */
	public void setFunctionClass(Class<? extends RatedFunction> functionClass) {
		this.functionClass = functionClass;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((functionClass == null) ? 0 : functionClass.hashCode());
		return result;
	}

	/**
	 * 
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
		FunctionType other = (FunctionType) obj;
		if (functionClass == null) {
			if (other.functionClass != null)
				return false;
		} else if (!functionClass.equals(other.functionClass))
			return false;
		return true;
	}

}
