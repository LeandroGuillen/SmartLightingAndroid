package um.cmovil.util;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	public static final String GMT_TIMEZONE = "GMT";

	public static final boolean isNetworkOk(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}

	public static String md5(String source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(source.getBytes("UTF-8"));
			return getString(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			String hex = Integer.toHexString((int) 0x00FF & b);
			if (hex.length() == 1) {
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * Time management based on
	 * https://svn.apache.org/repos/asf/wink/contrib/ibm
	 * -jaxrs/src/com/ibm/wsspi/jaxrs/http/DateHandler.java
	 */
	private static final ThreadLocal<DateFormat> rfc1123DateFormat = new ThreadLocal<DateFormat>() {
		@Override
		public DateFormat initialValue() {
			/*
			 * this is the format that RFC 1123 uses
			 */
			DateFormat format = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
			format.setLenient(false);
			return format;
		}

		@Override
		public DateFormat get() {
			DateFormat format = super.get();
			format.setTimeZone(TimeZone.getTimeZone(GMT_TIMEZONE));
			return format;
		}
	};

	private static final ThreadLocal<DateFormat> rfc850DateFormat = new ThreadLocal<DateFormat>() {
		@Override
		public DateFormat initialValue() {
			/*
			 * this is the format that RFC 850 uses
			 */
			DateFormat format = new SimpleDateFormat(
					"EEEE, dd-MMM-yy HH:mm:ss zzz", Locale.ENGLISH);
			format.setLenient(false);
			return format;
		}

		@Override
		public DateFormat get() {
			DateFormat format = super.get();
			format.setTimeZone(TimeZone.getTimeZone(GMT_TIMEZONE));
			return format;
		}
	};

	private static final ThreadLocal<DateFormat> asctimeDateFormat = new ThreadLocal<DateFormat>() {
		@Override
		public DateFormat initialValue() {
			/*
			 * this is the format that asctime uses
			 */
			DateFormat format = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
			format.setLenient(false);
			return format;
		}

		@Override
		public DateFormat get() {
			/*
			 * override here because ASCTIME format does not specify GMT time so
			 * let's assume it was sent in GMT time when parsing
			 */
			DateFormat format = super.get();
			format.setTimeZone(TimeZone.getTimeZone(GMT_TIMEZONE));
			return format;
		}
	};

	private static final ThreadLocal<DateFormat> asctimeDateFormatWithOneDayDigit = new ThreadLocal<DateFormat>() {
		@Override
		public DateFormat initialValue() {
			/*
			 * this is the format that asctime uses with a single day digit
			 */
			DateFormat format = new SimpleDateFormat(
					"EEE MMM  d HH:mm:ss yyyy", Locale.ENGLISH);
			format.setLenient(false);
			return format;
		}

		@Override
		public DateFormat get() {
			/*
			 * override here because ASCTIME format does not specify GMT time so
			 * let's assume it was sent in GMT time when parsing
			 */
			DateFormat format = super.get();
			format.setTimeZone(TimeZone.getTimeZone(GMT_TIMEZONE));
			return format;
		}
	};

	/**
	 * Creates an instance of java.util.Date from the specified String. The
	 * String represents the value of an HTTP date header.
	 */
	public static Date formatStringToDateInRFC1123Only(String httpDateHeader)
			throws ParseException {
		return rfc1123DateFormat.get().parse(httpDateHeader);
	}

	/**
	 * Extracts the date from the string in as many HTTP supported ways as
	 * possible.
	 * 
	 * @param dateInString
	 *            the date in string format
	 * @return a Date object representing the date, null if it could not be
	 *         parsed
	 */
	public static Date extractDateFromString(String dateInString) {
		/*
		 * tries various defined http-date formats to get the Date
		 */
		Date dateSince = null;
		try {
			dateSince = rfc1123DateFormat.get().parse(dateInString);
		} catch (ParseException ex) {
			try {
				dateSince = rfc850DateFormat.get().parse(dateInString);
			} catch (ParseException ex2) {
				try {
					dateSince = asctimeDateFormat.get().parse(dateInString);
				} catch (ParseException ex3) {
					try {
						dateSince = asctimeDateFormatWithOneDayDigit.get()
								.parse(dateInString);
					} catch (ParseException ex4) {

						// could not parse, so return null
						return null;
					}
				}
			}
		}
		return dateSince;
	}

	/**
	 * Returns the current time as a String in HTTP date header format.
	 */
	public static String getCurrentDateAsStringInRFC1123Format(String timeZone) {
		DateFormat formatter = rfc1123DateFormat.get();
		formatter.setTimeZone(TimeZone.getTimeZone(GMT_TIMEZONE));
		return formatter.format(Calendar.getInstance(
				TimeZone.getTimeZone(timeZone)).getTime());
	}

}
