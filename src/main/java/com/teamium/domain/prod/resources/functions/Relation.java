/**
 * 
 */
package com.teamium.domain.prod.resources.functions;

/**
 * @author slopes
 * @since v5.2.0
 * The list of existing relation
 *
 */
public enum Relation {
	EXTERNALIZED("externalized");
	
	/**
	 * The value of the relation instance
	 */
	private String relation;
	
	/**
	 * Instance the enumeration with the value given in parameter
	 * @param relation The relation
	 */
	private Relation(String relation){
		this.relation = relation;
	}
	
	/**
	 * The string representation of the relation
	 * @return The string representation of the relation 
	 */
	public String getStringValue(){
		return this.relation;
	}

}
