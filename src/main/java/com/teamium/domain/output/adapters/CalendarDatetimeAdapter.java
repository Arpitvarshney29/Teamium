/**
 * 
 */
package com.teamium.domain.output.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Converts a calendar as as xs:datetime
 * @author sraybaud - NovaRem
 *
 */
public class CalendarDatetimeAdapter extends CalendarAdapter {
	/**
	 * @see com.teamium.domain.output.adapters.CalendarAdapter#getDateFormat()
	 */
	@Override
	protected DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}
}
