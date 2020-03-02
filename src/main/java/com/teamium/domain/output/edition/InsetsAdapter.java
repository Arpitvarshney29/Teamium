/**
 * 
 */
package com.teamium.domain.output.edition;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author sraybaud
 *
 */
public class InsetsAdapter extends XmlAdapter<Insets, java.awt.Insets> {

	/**
	 * @param xml the xml object to unmarshal
	 * @return the bounded object
	 * @throws Exception
	 */
	@Override
	public java.awt.Insets unmarshal(Insets xml) throws Exception {
		java.awt.Insets obj = null;
		if(xml!=null){
			obj = new java.awt.Insets(xml.getTop(), xml.getLeft(), xml.getBottom(),xml.getRight());			
		}
		return obj;
	}

	/**
	 * 
	 * @param obj the bounded object
	 * @return the xml object
	 * @throws Exception
	 */
	@Override
	public Insets marshal(java.awt.Insets obj) throws Exception {
		Insets xml = null;
		if(obj!=null){
			xml= new Insets();
			xml.setTop(obj.top);
			xml.setLeft(obj.left);
			xml.setBottom(obj.bottom);
			xml.setRight(obj.right);
		}
		return xml;
	}

}
