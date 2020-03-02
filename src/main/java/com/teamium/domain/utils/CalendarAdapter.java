package com.teamium.domain.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;



/**
 * Calendar adapter for XML
 * @author JS
 *
 */
public class CalendarAdapter extends XmlAdapter<String, Calendar>{

	/**
	 * Calendar to string
	 */
	@Override
	public String marshal(Calendar cal) throws Exception {
		Calendar calendar = (Calendar) cal;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		if(calendar != null){
			return  df.format(calendar.getTime());
		}
		else
			return "";
	}

	/**
	 * String to Calendar - 
	 */
	@Override
	public Calendar unmarshal(String dateString) throws Exception {
		Calendar cal = null;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		if(dateString != null && !dateString.isEmpty()){
			
			try {
				Date date = df.parse(dateString);
				cal = Calendar.getInstance();
				cal.setTime(date);
				return cal;
			} catch (ParseException e) {
			}
		}
		
		return null;
	}

}
