package za.co.capitec.accounts.utilities.dates;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.capitec.accounts.utilities.constants.Constant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATE_PATTERN);
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constant.TIME_PATTERN);

	private static final Logger log = LoggerFactory.getLogger(DateUtils.class);
	/**
	 *
	 * @return
	 */
	private static LocalDate getDate() { return LocalDate.now(ZoneId.of(Constant.TIME_ZONE_ZA)); }
	/**
	 *
	 * @return
	 */
	private static LocalDateTime getTime() { return LocalDateTime.now(ZoneId.of(Constant.TIME_ZONE_ZA)); }
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
	public static LocalDate getMaxDate() { return LocalDate.parse(Constant.MAX_DATE, formatter); }
	/**
	 *
	 * @return
	 */
	public static LocalTime getCurrentTime() { return LocalTime.parse(getFormattedTime(), timeFormatter); }

}
