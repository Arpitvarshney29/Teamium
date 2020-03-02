package com.teamium.domain.output;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.teamium.domain.prod.resources.functions.Function;
import com.teamium.domain.prod.resources.functions.RatedFunction;

/**
 * An XML function hierarchy output
 * @author sraybaud - NovaRem
 * @version 1.1
 * 	-added description field
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FunctionXml implements Comparable<FunctionXml>{

	/**
	 * Position in the hierarchy
	 */	
	@XmlAttribute
	private Long id ;
	
	/**
	 * Position in the hierarchy
	 */	
	@XmlAttribute
	private Integer position ;
	
	/**
	 * Function name
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String name;
	
	/**
	 * Function type
	 */
	@XmlAttribute
	private String type;
	
	/**
	 * The source function (used in the compare method)
	 */
	@XmlTransient
	private Function function;
	
	/**
	 * Function description
	 */
	@XmlElement(namespace=XmlOutput.XMLNS)
	private String description;
	
	@XmlAttribute
	private Integer displayNumber;
	
	/**
	 * @param function the object to marshal
	 * @param position in the hierarchy
	 */
	public void marshal(Function function, Integer position){
		this.id=function.getId();
		this.type = function.getClass().getSimpleName();
		this.name=function.getValue();
		this.position=position;
		this.function = function;
		if(function instanceof RatedFunction){
			this.description = ((RatedFunction)function).getDescription();
		}
		this.displayNumber = function.getDisplayNumber();
	}
	
	/**
	 * @param function the object to marshal
	 */
	public void marshal(Function function){
		LinkedList<Function> hierarchy = new LinkedList<Function>();
		this.function = function;
		Function current = function;
		while(current!=null){
			hierarchy.addFirst(current);
			current = current.getParent();
		}
		this.marshal(function, hierarchy.size());
	}

	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionXml other = (FunctionXml) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	/**
	 * Compare the current function to the given one
	 * @param o the other function to compare
	 * @return -1 if the current is before, 0 if they are equals, +1 if the current one is next
	 */
	@Override
	public int compareTo(FunctionXml o) {
		if(o==null) return 1;
		if(function==null && o.function!=null){
			return -1;
		}
		else{
			if(o.function==null) return 1;
			else{
				int compare = function.compareTo(o.function);
				if(compare != 0) return compare; 				
			}
		}
		return 0;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	

}
