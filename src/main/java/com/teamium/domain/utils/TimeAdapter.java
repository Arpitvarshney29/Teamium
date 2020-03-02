package com.teamium.domain.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;



/**
 * Time adapter for XML
 * @author sraybaud
 *
 */
public class TimeAdapter extends XmlAdapter<String, Date>{
	/**
	 * Date format
	 */
	private static final DateFormat TF = new SimpleDateFormat("HH:mm:ss");

	/**
	 * Calendar to string
	 */
	@Override
	public String marshal(Date date) throws Exception {
		if(date!=null){
			return TF.format(date);
		}else{
			return null;
		}
	}

	/**
	 * String to Calendar - 
	 */
	@Override
	public Date unmarshal(String dateString) throws Exception {
		if(dateString!=null){
			return TF.parse(dateString);
		}else{
			return null;
		}
	}

}
