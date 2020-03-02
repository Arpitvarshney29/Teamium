package com.teamium.domain.output;


/**
 * Describes an editable entity root
 * @author sraybaud - NovaRem
 *
 */
public interface OutputEntityRoot<T extends XmlOutputEntity> {
	/**
	 * Marshalls the given editable entity as an editable entity
	 */
	public abstract void marshal(T e);
}
