package za.co.capitec.customer.utilities.dates;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.capitec.customer.constants.CustomerConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CustomerConstants.DATE_PATTERN);
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(CustomerConstants.TIME_PATTERN);

	private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
	/**
	 *
	 * @return
	 */
	private static LocalDate getDate() { return LocalDate.now(ZoneId.of(CustomerConstants.TIME_ZONE_ZA)); }
	/**
	 *
	 * @return
	 */
	private static LocalDateTime getTime() { return LocalDateTime.now(ZoneId.of(CustomerConstants.TIME_ZONE_ZA)); }
	/**
	 *
	 * @return
	 */
	private static String getFormattedDate() { return formatter.format(getDate());}
	/**
	 *
	 * @return
	 */
	private static String getFormattedTime() { return getTime().format(timeFormatter);}
	/**
	 *
	 * @return
	 */
	public static LocalDate getCurrentDate() {
		return LocalDate.parse(getFormattedDate(), formatter);
	}
	/**
	 *
	 * @return
	 */
	public static LocalDate getMaxDate() { return LocalDate.parse(CustomerConstants.MAX_DATE, formatter); }
	/**
	 *
	 * @return
	 */
	public static LocalTime getCurrentTime() { return LocalTime.parse(getFormattedTime(), timeFormatter); }

}
