/**
 * 
 */
package com.teamium.domain.output.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Convert a calendar as type xs:date
 * @author sraybaud - NovaRem
 *
 */
public class CalendarDateAdapter extends CalendarAdapter {
	/**
	 * @see com.teamium.domain.output.adapters.CalendarAdapter#getDateFormat()
	 */
	@Override
	protected DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}

}
