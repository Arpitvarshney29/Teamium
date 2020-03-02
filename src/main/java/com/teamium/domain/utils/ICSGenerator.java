package com.teamium.domain.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringJoiner;

/**
 * A Utility class that generate ICS files
 * @author JS
 *
 */
public class ICSGenerator {

	private static final DateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
	private static final DateFormat formatTime= new SimpleDateFormat("HHmmss");
	
	
	/**
	 * A method that create an ics file in the gived writedFile.
	 * @param start The event start
	 * @param end the event end
	 * @param summary the event summary
	 * @param location the event location
	 * @param description the event description
	 * @param writedFile the file to create, it will be deleted if it already exists
	 */
	public static void generateICS(Calendar start,Calendar end,String summary,String location, String description,File writedFile) throws Exception{
		if(writedFile.exists()){
			writedFile.delete();
		}
		writedFile.createNewFile();
		StringJoiner joiner = new StringJoiner(System.lineSeparator());
		joiner.add("BEGIN:VCALENDAR");
		joiner.add("VERSION:2.0");
		joiner.add("PRODID:-//Teamium/cal//NONSGML v1.0//FR");
		joiner.add("BEGIN:VEVENT");
		joiner.add("DTSTART:"+formatDate.format(start.getTime())+"T"+formatTime.format(start.getTime()));
		joiner.add("DTEND:"+formatDate.format(end.getTime())+"T"+formatTime.format(end.getTime()));
		joiner.add("SUMMARY:"+summary);
		joiner.add("LOCATION:"+location);
		joiner.add("DESCRIPTION:"+description.replaceAll(System.lineSeparator(), ""));
		joiner.add("END:VEVENT");
		joiner.add("END:VCALENDAR");
		String str = joiner.toString();
		FileOutputStream fos = new FileOutputStream(writedFile);
		fos.write(str.getBytes());
		fos.flush();
		fos.close();
	}
	
}