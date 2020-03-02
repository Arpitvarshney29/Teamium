package com.teamium.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.teamium.constants.Constants;
import com.teamium.dto.DateRangeDTO;
import com.teamium.exception.UnprocessableEntityException;

/**
 * <p>
 * A common class providing utility methods like : checking if email pattern is
 * valid or not, to check whether a password pattern is valid or not, to convert
 * DateTime into given format, generates a random Token, format the number of
 * MilliSeconds into String, format MilliSeconds to String, validate time with
 * limit, convert a string into Joda DateTime object, etc.
 * </p>
 */
@Component
public class CommonUtil {

	private final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * To check whether a email is valid or not.
	 * 
	 * Description for Email regular expression.
	 * 
	 * @param email
	 *            the email-id.
	 * 
	 * @return the boolean
	 */
	public static boolean isEmailValid(String email) {
		Pattern emailPattern = Pattern.compile(
				"^[a-zA-Z0-9]+((\\.|\\-|\\_)[a-zA-Z0-9]{1,})*@[a-zA-Z0-9]+((\\.|\\-|\\_)[a-zA-Z0-9]+)*(\\.[a-zA-Z0-9]{2,})$");
		Matcher m = emailPattern.matcher(email);
		return m.matches();
	}

	/**
	 * Method to check if telephone code is valid or not
	 * 
	 * @param telephone
	 * 
	 * @return the boolean 'true' if telephone code is not valid
	 */
	public static boolean isTelephoneCodeNotValid(String telephoneCode) {
		Pattern telephonePattern = Pattern.compile("^([+])?([0]{1,2})?([1-9]{1})$");
		Matcher m = telephonePattern.matcher(telephoneCode);
		if (!m.matches()) {
			return m.matches();
		} else {
			telephonePattern = Pattern.compile("^([+])?[0]{1,7}$");
			m = telephonePattern.matcher(telephoneCode);
			return m.matches();
		}
	}

	/**
	 * Method to check if telephone number is valid or not
	 * 
	 * @param telephone
	 * 
	 * @return the boolean 'true' if telephone is valid
	 */
	public static boolean isTelephoneNumberValid(String telephoneNumber) {
		Pattern telephonePattern = Pattern.compile("^([+])?([0-9]{1,7}[-])?[1-9]{1,1}[0-9]{5,9}$");
		Matcher m = telephonePattern.matcher(telephoneNumber);
		return m.matches();
	}

	/**
	 * Check if mobile valid or not
	 * 
	 * @param mobileNumber
	 * 
	 * @return true if valid
	 */
	public static boolean isMobileNumberValid(String mobileNumber) {
		Pattern telephonePattern = Pattern.compile("^([+])?(([0]{1,2})?([1-9]{1})([0-9]{5,11}))$");
		Matcher m = telephonePattern.matcher(mobileNumber);
		return m.matches();
	}

	/**
	 * To check whether a password is valid or not
	 * 
	 * Description for Password regular expression.
	 * 
	 * The password must contain : one capital letter ,one small letter,one special
	 * character,one numeric and must contains at least 8 characters.
	 * 
	 * ^ #start of the line
	 * 
	 * ("\\s*,\\s*) means: \s* any number of whitespace characters, a comma and \s*
	 * any number of whitespace characters,
	 * 
	 * 
	 * $ #end of the line
	 * 
	 * @param password
	 *            the password
	 * 
	 * @return the boolean
	 */
	/*
	 * public static boolean isPasswordValid(String password) { Pattern
	 * passwordPattern = Pattern .compile(
	 * "^(?!.*\\s)(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[$@$!%*#?&])(?=.*?[0-9]).{8,}$") ;
	 * Matcher m = passwordPattern.matcher(password); return m.matches(); }
	 * 
	 */
	/**
	 * To Generates a random Token.
	 *
	 * @return the random UUID
	 */

	/*
	 * public static String createRandomToken() { return
	 * RandomStringUtils.random(16, true, true); }
	 * 
	 */
	/**
	 * To convert DateTime into given format.
	 * 
	 * @param dateTime
	 *            the DateTime dateTime.
	 * 
	 * @param format
	 *            the String format.
	 * 
	 * @return String into given format.
	 */
	/*
	 * public static String dateTimeFormat(DateTime dateTime, String format) {
	 * return dateTime == null ? "" :
	 * dateTime.toString(DateTimeFormat.forPattern(format)); }
	 * 
	 */

	/**
	 * To convert DateTime into given format.
	 * 
	 * @param format
	 *            the String format.
	 * 
	 * @return String into given format.
	 */
	/*
	 * public static String dateTimeFormat(String format) { return
	 * dateTimeFormat(DateTime.now(), format); }
	 * 
	 */
	/**
	 * To format the number of MilliSeconds into String.
	 * 
	 * @param timeInMilliSeconds
	 *            the timeInMilliSeconds.
	 * 
	 * @return formatted String.
	 */
	/*
	 * public static String formatMilliSecondsToString(Long timeInMilliSeconds) { if
	 * (timeInMilliSeconds == null) { return null; } if (timeInMilliSeconds == 0) {
	 * return "00:00:00:00"; } String formattedTimeInString = ""; // For hours
	 * formattedTimeInString += getTime((timeInMilliSeconds / (60 * 60 * 1000)),
	 * ""); // For Minute formattedTimeInString += getTime((timeInMilliSeconds / (60
	 * * 1000)) % 60, ":"); // For Seconds formattedTimeInString +=
	 * getTime((timeInMilliSeconds / 1000) % 60, ":"); // For MilliSeconds
	 * formattedTimeInString += getTime(((timeInMilliSeconds % 1000) *
	 * (Constants.FRAME_PER_SECOND)) / 1000, ":");
	 * 
	 * return formattedTimeInString; }
	 * 
	 */
	/**
	 * Method to format MilliSeconds to String [HH:mm:ss:ff] format.
	 * 
	 * @param timeInMilliSeconds
	 *            the timeInMilliSeconds
	 * 
	 * @param frame
	 *            the frame
	 * 
	 * @return milliseconds in String
	 */
	/*
	 * public static String formatMilliSecondToString(Long timeInMilliSeconds,
	 * String frame) { if (timeInMilliSeconds == null) { return null; } int frames;
	 * if (frame != null) { frames = (int) Math.ceil(Double.parseDouble(frame)); }
	 * else { frames = Constants.FRAME_PER_SECOND; }
	 * 
	 * if (timeInMilliSeconds == 0) { return "00:00:00:00"; } String
	 * formattedTimeInString = ""; // For hours formattedTimeInString +=
	 * getTime((timeInMilliSeconds / (60 * 60 * 1000)), ""); // For Minute
	 * formattedTimeInString += getTime((timeInMilliSeconds / (60 * 1000)) % 60,
	 * ":"); // For Seconds formattedTimeInString += getTime((timeInMilliSeconds /
	 * 1000) % 60, ":"); // For MilliSeconds formattedTimeInString +=
	 * getTime(((timeInMilliSeconds % 1000) * (frames)) / 1000, ":");
	 * 
	 * return formattedTimeInString; }
	 * 
	 *//**
		 * To format the number of MilliSeconds into String [HH:mm:ss] format.
		 * 
		 * @param timeInMilliSeconds
		 *            the timeInMilliSeconds.
		 * 
		 * @return formatted String.
		 */
	/*
	 * public static String formatMilliSecondsToStringValue(Long timeInMilliSeconds)
	 * { if (timeInMilliSeconds == null) { return null; } if (timeInMilliSeconds ==
	 * 0) { return "00:00:00"; } long seconds = (((timeInMilliSeconds % 1000) *
	 * Constants.FRAME_PER_SECOND) / 1000 > 0 ? 1 : 0) + (timeInMilliSeconds /
	 * 1000); long s = seconds % 60; long m = (seconds / 60) % 60; long h = (seconds
	 * / (60 * 60)) % 24; return String.format("%02d:%02d:%02d", h, m, s); }
	 * 
	 *//**
		 * To format the String into number of MilliSeconds.
		 * 
		 * @param time
		 *            the time.
		 * 
		 * @return formatted milliSeconds.
		 */
	/*
	 * public static long formatStringToMilliSeconds(String time) { if
	 * (StringUtils.isBlank(time)) { return 0; } String timePart[] =
	 * time.split(":"); if (timePart.length != 4) {
	 * logger.error("Please enter valid Time."); throw new
	 * UnprocessableEntityException("Please enter valid Time."); } long
	 * formatedTimeInMilliSeconds = 0; // For hours formatedTimeInMilliSeconds +=
	 * validateTime(Integer.parseInt(timePart[0]), 0, 60 * 60 * 1000); // For Minute
	 * formatedTimeInMilliSeconds += validateTime(Integer.parseInt(timePart[1]), 60,
	 * 60 * 1000); // For Seconds formatedTimeInMilliSeconds +=
	 * validateTime(Integer.parseInt(timePart[2]), 60, 1000); // For MilliSeconds
	 * formatedTimeInMilliSeconds += validateTime(Integer.parseInt(timePart[3]),
	 * (Constants.FRAME_PER_SECOND - 1), ((double) 1000 /
	 * (Constants.FRAME_PER_SECOND)));
	 * 
	 * return formatedTimeInMilliSeconds; }
	 * 
	 *//***
		 * 
		 * Method to format String to MilliSeconds
		 * 
		 * @param time
		 *            the time
		 * 
		 * @param frame
		 *            the frame
		 * 
		 * @return MilliSeconds in Long
		 */
	/*
	 * public static long formatStringToMilliSecond(String time, String frame) { int
	 * frames; if (frame != null) { frames = (int)
	 * Math.ceil(Double.parseDouble(frame)); } else { frames =
	 * Constants.FRAME_PER_SECOND; } if (StringUtils.isBlank(time)) { return 0; }
	 * String timePart[] = time.split(":"); if (timePart.length != 4) {
	 * logger.error("Please enter valid Time."); throw new
	 * UnprocessableEntityException("Please enter valid Time."); } long
	 * formatedTimeInMilliSeconds = 0; // For hours formatedTimeInMilliSeconds +=
	 * validateTime(Integer.parseInt(timePart[0]), 0, 60 * 60 * 1000); // For Minute
	 * formatedTimeInMilliSeconds += validateTime(Integer.parseInt(timePart[1]), 60,
	 * 60 * 1000); // For Seconds formatedTimeInMilliSeconds +=
	 * validateTime(Integer.parseInt(timePart[2]), 60, 1000); // For MilliSeconds
	 * formatedTimeInMilliSeconds += validateTime(Integer.parseInt(timePart[3]),
	 * (frames - 1), ((double) 1000 / (frames)));
	 * 
	 * return formatedTimeInMilliSeconds; }
	 * 
	 *//**
		 * To Validate time with limit.
		 * 
		 * @param time
		 * 
		 * @param limit
		 * 
		 * @param value
		 * 
		 * @return long.
		 */
	/*
	 * private static long validateTime(int time, int limit, double value) { if
	 * (limit != 0 && time > limit) { logger.error("Please enter valid Time.");
	 * throw new UnprocessableEntityException("Please enter valid Time."); } return
	 * (long) Math.ceil(time * value); }
	 * 
	 * private static double validateTimeDouble(int time, int limit, double value) {
	 * if (limit != 0 && time > limit) { logger.error("Please enter valid Time.");
	 * throw new UnprocessableEntityException("Please enter valid Time."); } return
	 * time * value; }
	 * 
	 *//**
		 * To get String with prefix.
		 * 
		 * @param value
		 * 
		 * @param prefix
		 * 
		 * @return String.
		 */
	/*
	 * private static String getTime(long value, String prefix) { if (value < 10)
	 * prefix += "0"; return prefix += value; }
	 * 
	 *//**
		 * To get membership filter against which to filter.
		 * 
		 * @param fromFlashMedia
		 *            the fromFlashMedia
		 * 
		 * @return membership string.
		 */
	/*
	 * public static String getFilterMembership(boolean fromFlashMedia) { return
	 * (fromFlashMedia ? MembershipName.Flash_Media :
	 * MembershipName.Flash_Quality).getMembershipNameString(); }
	 * 
	 * public static boolean isNullOrNotInCorrectFormat(String value) { return value
	 * == null || !!value.matches("[0-9:]*"); }
	 * 
	 *//**
		 * To get time in String
		 * 
		 * @param list
		 *            the list
		 * @param startIndex
		 *            the startIndex
		 * @param seconds
		 *            the seconds
		 * 
		 * @return time in String
		 */
	/*
	 * public static String getTime(List<String> list, int startIndex, long seconds)
	 * { if (list.size() == 0) { return "00:00:00"; } if (list.size() == startIndex)
	 * { long s = seconds % 60; long m = (seconds / 60) % 60; long h = (seconds /
	 * (60 * 60)) % 24; return String.format("%02d:%02d:%02d", h, m, s); } String
	 * timestampStr = list.get(startIndex); String[] tokens =
	 * timestampStr.split(":"); long hours = Integer.parseInt(tokens[0]); long
	 * minutes = Integer.parseInt(tokens[1]); long second =
	 * Integer.parseInt(tokens[2]); long duration = 3600 * hours + 60 * minutes +
	 * second; return getTime(list, ++startIndex, (seconds + duration)); }
	 * 
	 *//**
		 * To cast milliseconds to seconds.
		 * 
		 * @param milliseconds
		 *            the milliseconds
		 * 
		 * @return milliseconds in Long
		 */
	/*
	 * public static long formatMilliseconds(double milliseconds) { milliseconds =
	 * Math.ceil(milliseconds / 1000); return (long) milliseconds * 1000; }
	 * 
	 *//***
		 * To get the double value from string.
		 * 
		 * @param time
		 *            the time
		 * @param frame
		 *            the frame
		 * 
		 * @return MilliSeconds in double
		 */
	/*
	 * public static double formatStringToMilliSecondDouble(String time, String
	 * frame) { int frames; if (frame != null) { frames = (int)
	 * Math.ceil(Double.parseDouble(frame)); } else { frames =
	 * Constants.FRAME_PER_SECOND; } if (StringUtils.isBlank(time)) { return 0; }
	 * String timePart[] = time.split(":"); if (timePart.length != 4) {
	 * logger.error("Please enter valid Time."); throw new
	 * UnprocessableEntityException("Please enter valid Time."); } double
	 * formatedTimeInMilliSeconds = 0; // For hours formatedTimeInMilliSeconds +=
	 * validateTimeDouble(Integer.parseInt(timePart[0]), 0, 60 * 60 * 1000); // For
	 * Minute formatedTimeInMilliSeconds +=
	 * validateTimeDouble(Integer.parseInt(timePart[1]), 60, 60 * 1000); // For
	 * Seconds formatedTimeInMilliSeconds +=
	 * validateTimeDouble(Integer.parseInt(timePart[2]), 60, 1000); // For
	 * MilliSeconds formatedTimeInMilliSeconds +=
	 * validateTimeDouble(Integer.parseInt(timePart[3]), (frames - 1), ((double)
	 * 1000 / (frames)));
	 * 
	 * return formatedTimeInMilliSeconds; }
	 * 
	 *//**
		 * To get the channel property value of a given channel by channel property
		 * name.
		 * 
		 * @param channel
		 * @param channelProperty
		 * @param channelPropertyName
		 * 
		 * @return value of given channel property.
		 */
	/*
	 * public static String getChannelPropertyValue(Channel channel, ChannelProperty
	 * channelProperty, String channelPropertyName) {
	 * channelProperty.setName(channelPropertyName); String propertValue =
	 * channel.getChannelPropertyValue().get(channelProperty); return propertValue
	 * != null ? propertValue : ""; }
	 * 
	 *//**
		 * To convert a string into Joda DateTime object.
		 * 
		 * @param date
		 *            the date
		 * @param dateFormat
		 *            the dateFormat
		 * 
		 * @return DateTime
		 */
	/*
	 * public static DateTime stringToDateTime(String date, String dateFormat) {
	 * DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateFormat);
	 * return DateTime.parse(date, dateTimeFormatter); }
	 * 
	 *//**
		 * To convert string to Joda Date String in [MM/dd/yyyy] format.
		 * 
		 * @param name
		 *            the name
		 * 
		 * @param format
		 *            the format
		 * 
		 * @return string to Date String
		 */
	/*
	 * public static String stringToDateConverter(String name, String format) {
	 * Pattern p = Pattern.compile("-?\\d+"); Matcher m = p.matcher(name); while
	 * (m.find()) { try { return convertStringtoDate(m.group(), format); } catch
	 * (Exception e) { System.err.println("Error in String To Numeric date ");
	 * e.printStackTrace(); } } return null; }
	 * 
	 *//**
		 * To convert String to Joda Date String in [MM/dd/yyyy] format.
		 * 
		 * @param strDate
		 *            the strDate
		 * 
		 * @param format
		 *            the format
		 * 
		 * @return String to Date String
		 * 
		 * @throws Exception
		 */
	/*
	 * public static String convertStringtoDate(String strDate, String format)
	 * throws Exception { String ddate = null; try { DateTime dateTime =
	 * stringToDateTime(strDate, format); ddate = dateTimeFormat(dateTime,
	 * "MM/dd/yyyy"); } catch (Exception e) { logger.error("Invalid Date!!!!");
	 * throw new Exception("Invalid Date!!!!", e); } return ddate; }
	 * 
	 *//**
		 * To get list of exportTypes of a channel.
		 * 
		 * @param channel
		 * @return list of exportTypes of a channel.
		 */
	/*
	 * public static List<String> getChannelExportTypes(Channel channel) { return
	 * channel.getScheduleExportTypes().stream().map(ob ->
	 * ob.getName()).collect(Collectors.toList()); }
	 * 
	 *//**
		 * To get DateTime after applying specific timezone of channel.
		 * 
		 * @param dateTime
		 * @param timeZone
		 * @return DateTime object.
		 */

	/*
	 * public static DateTime getDateTimeWithTimeZone(DateTime dateTime, TimeZone
	 * timeZone) { return
	 * dateTime.withZone(DateTimeZone.UTC).withZone(DateTimeZone.forID(timeZone.
	 * getOffset())); }
	 * 
	 *//**
		 * To match data in list
		 * 
		 * @param channelPropertyValue
		 * @param searchFor
		 * @return
		 *//*
			 * public static boolean equalsWith(String channelPropertyValue, String
			 * searchFor) { List<String> checkList = new
			 * ArrayList<>(Arrays.asList(channelPropertyValue.split(","))); return
			 * checkList.contains(searchFor); }
			 */
	public static URL validateURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			logger.info("Please provide a valid url : " + url);
			throw new UnprocessableEntityException("Please provide a valid url.");
		}
	}

	/**
	 * Method to get project formats
	 * 
	 * @return list of project formats
	 */
	public static List<String> getProjectFormats() {
		return Arrays.asList("2K", "4K", "HD", "Normal", "Print", "SD");
	}

	/**
	 * Method to get project unit basis
	 * 
	 * @return list of project unit basis
	 */
	public static List<String> getProjectUnitBasis() {
		return Arrays.asList("Day", "Hour", "Minute", "Unity", "Week", "Special");
	}

	/**
	 * Method to get project status
	 * 
	 * @return list of project status
	 */
	public static List<String> getProjectStatus() {
		return Arrays.asList("To Do", "In Progress", "Done");
	}

	/**
	 * Method to get project financial status
	 * 
	 * @return list of project financial status
	 */
	public static List<String> getProjectFinancialStatus() {
		return Arrays.asList("Approved", "Rejected", "Revised");
	}

	public static String getStringFromCalendar(Calendar c, String format) {
		Date date = c.getTime();
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * To get all currencies code
	 * 
	 * @return list of currency-code
	 */
	public static List<String> getAllCurrencyCode() {
		return Currency.getAvailableCurrencies().stream().map(cur -> cur.getCurrencyCode())
				.sorted((currency1, currency2) -> currency1.toLowerCase().compareTo(currency2.toLowerCase()))
				.collect(Collectors.toList());
	}

	/**
	 * Method to get date-pattern
	 * 
	 * @param pattern
	 * @param instance
	 *            the calendar instance
	 * 
	 * @return the date in string
	 */
	public static String datePattern(String pattern, Calendar instance) {
		if (instance == null || StringUtils.isBlank(pattern)) {
			return "";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(instance.getTime());
	}

	/**
	 * Method to format value into format string
	 * 
	 * @param value
	 * 
	 * @return the value string after formatting
	 */
	public static String formatValue(float value) {
		if (value == 0f) {
			return "0";
		}
		String number = new Float(value).toString();
		double amount = Double.parseDouble(number);
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		return formatter.format(amount);
	}

	/**
	 * Method to compare project totalWithoutTax with the budget totalWithoutTax
	 * 
	 * @param projectTotalPriceIVAT
	 * @param sourceTotalPriceIVAT
	 * 
	 * @return the actual comparison string
	 */
	public static String projectTotalComparison(float projectTotalPriceIVAT, float sourceTotalPriceIVAT) {
		return projectTotalPriceIVAT > sourceTotalPriceIVAT ? Constants.PROJECT_ACTUAL_EXCEED
				: projectTotalPriceIVAT < sourceTotalPriceIVAT ? Constants.PROJECT_ACTUAL_LOWER
						: Constants.PROJECT_ACTUAL_ON_BUDGET;
	}

	/**
	 * Method to validate Date-Range
	 * 
	 * @param startDate
	 * @param endDate
	 * 
	 * @return the DateRange wrapper object
	 */
	public static DateRangeDTO validateDateRange(String startDate, String endDate) {
		logger.info("Inside CommonUtil :: validateDateRange(startDate : " + startDate + ", endDate : " + endDate + ")");
		DateRangeDTO dateRangeDTO = new DateRangeDTO();
		Calendar start = null;
		Calendar end = null;
		if (!StringUtils.isBlank(startDate) || !StringUtils.isBlank(endDate)) {
			if (StringUtils.isBlank(startDate)) {
				logger.info("Start date cannot be empty");
				throw new UnprocessableEntityException("Start date cannot be empty.");
			}
			if (StringUtils.isBlank(endDate)) {
				logger.info("End date cannot be empty");
				throw new UnprocessableEntityException("End date cannot be empty");
			}

			DateTime startDateTime = null;
			DateTime endDateTime = null;
			try {
				startDateTime = DateTime.parse(startDate).withZone(DateTimeZone.forID("Asia/Calcutta"));
				endDateTime = DateTime.parse(endDate).withZone(DateTimeZone.forID("Asia/Calcutta"));
			} catch (Exception e) {
				logger.info("Invalid date");
				throw new UnprocessableEntityException("Invalid date");
			}
			if (startDateTime == null || endDateTime == null) {
				logger.info("Invalid date");
				throw new UnprocessableEntityException("Invalid date");
			}
			start = Calendar.getInstance();
			start.setTimeInMillis(startDateTime.getMillis());

			end = Calendar.getInstance();
			end.setTimeInMillis(endDateTime.getMillis());

			long daysDiff = Days.daysBetween(startDateTime.toLocalDate(), endDateTime.toLocalDate()).getDays();
			long timeDiff = Hours.hoursBetween(startDateTime.toLocalDateTime(), endDateTime.toLocalDateTime())
					.getHours();
			if (daysDiff <= 0) {
				if (daysDiff == 0 && timeDiff < 0) {
					logger.info("End time should be greater than start time");
					throw new UnprocessableEntityException("End time should be greater than start time");
				} else if (daysDiff == 0 && timeDiff == 0) {
					logger.info("Time difference should not be less than 1 hour");
					throw new UnprocessableEntityException("Time difference should not be less than 1 hour");
				} else if (daysDiff < 0) {
					logger.info("End Date cannot be before Start Date.");
					throw new UnprocessableEntityException("End Date cannot be before Start Date.");
				}
			}

			dateRangeDTO.setDuration(daysDiff + 1);
			dateRangeDTO.setPreviousMonthData(Boolean.FALSE);
		} else {
			start = new DateTime().toGregorianCalendar();
			end = new DateTime().toGregorianCalendar();

			start.set(Calendar.DAY_OF_MONTH, 1);
			start.set(Calendar.HOUR_OF_DAY, 0);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);

			end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH));
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);

			dateRangeDTO.setPreviousMonthData(Boolean.TRUE);
			dateRangeDTO.setDuration(end.getActualMaximum(Calendar.DAY_OF_MONTH));
		}

		dateRangeDTO.setStartDate(start);
		dateRangeDTO.setEndDate(end);
		return dateRangeDTO;
	}

	/**
	 * Method to find days difference
	 * 
	 * @param start
	 * @param end
	 * 
	 * @return the days difference count
	 */
	public static long dayDifference(Calendar start, Calendar end) {
		DateTime startDateTime = new DateTime(start.getTimeInMillis());
		DateTime endDateTime = new DateTime(end.getTimeInMillis());

		long daysDiff = Days.daysBetween(startDateTime.toLocalDate(), endDateTime.toLocalDate()).getDays();
		return daysDiff;
	}
}