/**
 * 
 */
package com.teamium.domain.output.adapters;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Convert a calendar to date xml type
 * @author sraybaud
 *
 */
public abstract class CalendarAdapter extends XmlAdapter<String, Calendar> {
	/**
	 * Returns the date pattern
	 */
	protected abstract DateFormat getDateFormat();

	/**
	 * 
	 */
	@Override
	public String marshal(Calendar obj) throws Exception {
		if(obj!=null){
			return this.getDateFormat().format(obj.getTime());
		}else{
			return null;
		}
	}

	/**
	 * 
	 */
	@Override
	public Calendar unmarshal(String string) throws Exception {
		if(string!=null){
			Date date = this.getDateFormat().parse(string);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal;
		}else{
			return null;
		}
	}

}
