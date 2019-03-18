/**
 * 
 */
package com.darwinbox.framework.uiautomation.Utility;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.Calendar;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author balaji
 * @Creation_Date: 20 Nov 2017
 * @ClassName: DateTimeHelper.java
 * @LastModified_Date: 20 Feb 2018
 */
public class DateTimeHelper {

	UtilityHelper objUtil = new UtilityHelper();

	public static String getCurrentDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
		Calendar cal = Calendar.getInstance();
		String time = "" + dateFormat.format(cal.getTime());
		return time;
	}

	public static String getCurrentDate() {
		return getCurrentDateTime().substring(0, 11);
	}

	/**
	 * Set custom Date
	 * @return
	 */
	public static String getCurrentLocalDateAndTime() {
		try {
			String dateTime;
			if(UtilityHelper.getProperty("config", "Local.Date.Change.Required").equalsIgnoreCase("Yes")) {
				dateTime = UtilityHelper.getProperty("config", "Change.Local.Date") + LocalTime.now().toString();
			}else {
				dateTime = LocalDateTime.now().toString();
			}
			return dateTime;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while getting date and Time");
		}
	}

	/**
	 * Set custom Date
	 * @return
	 */
	public static String getCurrentLocalDate() {
		try {
			String dateTime;
			if(UtilityHelper.getProperty("config", "Local.Date.Change.Required").equalsIgnoreCase("Yes")) {
				dateTime = UtilityHelper.getProperty("config", "Change.Local.Date");
			}else {
				dateTime = LocalDate.now().toString();
			}
			return dateTime;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while getting date and Time");
		}
	}

	public boolean changeServerDate(WebDriver driver, String date) {
		try {
			String text = "emailtemplate/Setserverdate?set_date="+ date;
			Reporter.log("Server date is changed to '" + objUtil.getHTMLTextFromAPI(driver, text)+ "'",true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while changing server date and Local Date");
		}
	}

    /**
     * This method converts Local Date to Epoch day
     * @param date
     * @return long
     */
    public long convertLocalDateToEpochDay(String date){
	    try {
	        String dateTime = date + "T14:47:55";
           return ((LocalDateTime.parse(dateTime).atZone(ZoneId.of("Asia/Kolkata")).toInstant().toEpochMilli())/1000);
	    }catch (Exception e){
	        e.printStackTrace();
            throw new RuntimeException("Exception while changing Local date to epoch day");
        }
    }


	public static void changeLocalDateUsingCommandLine() {
		try {

			String localDate = UtilityHelper.getProperty("config", "Change.Server.Date");
			if(System.getProperty("os.name").contains("Window")) {
				String cmd1 = "cmd /c date " + UtilityHelper.getProperty("config", "Change.Local.Date");
				Runtime.getRuntime().exec(cmd1);

				String cmd2 = "cmd /c time 10:05";
				Runtime.getRuntime().exec(cmd2);
			}else if (System.getProperty("os.name").contains("Linux")) {
				String dateToChange = UtilityHelper.getProperty("config", "Change.Local.Date");
				String text = "echo Shikhar123 | sudo -S date --set='" +dateToChange+ " 10:05:59'";
				String[] cmd = {"/bin/bash","-c",text};
				Runtime.getRuntime().exec(cmd);
			}

			if(LocalDate.now().toString().equals(localDate)) {
				Reporter.log("System date changed to " + LocalDate.now().toString(),true);
			}else {
				throw new RuntimeException("Exception while changing server date its "+ LocalDate.now().toString()
						+ " and not changed to "+ localDate);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This month will calculate difference between months
	 *
     * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDifferenceFromCurrentDate(String DATEIN_YYYY_MM_DD_format) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);
			LocalDate currentDate = LocalDate.parse(getCurrentLocalDate());

			LocalDate startDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			LocalDate endDate = currentDate.with(firstDayOfMonth());
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate months difference between first day of month of two
	 * dates
	 * @param Date1
     * @param Date2
	 * @return
	 */
	public double getMonthDifferenceBetweenTwoDates(String Date1, String Date2) {
		try {

			LocalDate startDate = LocalDate.parse(Date1).with(firstDayOfMonth());
			LocalDate endDate = LocalDate.parse(Date2).with(firstDayOfMonth());
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate exact month difference between two dates
	 * @param Date1
     * @param Date2
	 * @return
	 */
	public double getExactMonthDifferenceBetweenTwoDates(String Date1, String Date2) {
		try {
			LocalDate startDate = LocalDate.parse(Date1);
			LocalDate endDate = LocalDate.parse(Date2);
			int febdiff = 0;
			int startday = 30;

			if (endDate.getMonth() == Month.FEBRUARY && startDate.isAfter(startDate.withDayOfMonth(28))
					&& (endDate.getDayOfMonth() > (startDate.getDayOfMonth()))) {
				febdiff = 1;
			}
			if ((startDate == startDate.withDayOfMonth(startDate.lengthOfMonth()))
					&& (endDate == endDate.withDayOfMonth(endDate.lengthOfMonth()))) {
				if (startDate.getMonth() == Month.FEBRUARY) {
					startday = 28;
				} else {
					startday = 30;
				}
				startDate = startDate.withDayOfMonth(startday);
			}
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff + febdiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate days difference between two dates
	 * @param Date1
	 * @param Date2
	 * @return
	 */
	public double getDaysDifferenceBetweenTwoDates(String Date1, String Date2) {
		try {
			LocalDate startDate = LocalDate.parse(Date1).with(firstDayOfMonth());
			LocalDate endDate = LocalDate.parse(Date2).with(firstDayOfMonth());
			double daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
			return daysDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate difference between two dates
	 *
     * @param DOJ
	 * @return
	 */
	public double getDaysDifferenceBetweenDOJAndCurrentDate(String DOJ) {
		try {
			LocalDate startDate = LocalDate.parse(DOJ);
			LocalDate endDate = LocalDate.parse(getCurrentLocalDate());
			double daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
			return daysDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate difference between two dates
	 * @param customCurrentDate
     * @param DOJ
	 * @return
	 */
	public double getDaysDifferenceBetweenDOJAndCurrentDate(String DOJ, String customCurrentDate) {
		try {
			LocalDate startDate = LocalDate.parse(DOJ);
			LocalDate endDate = LocalDate.parse(customCurrentDate);
			double daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
			return daysDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method verifies whether date is after of before mid of month
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return String
	 */
	public String verifyDOJMidJoining(String DATEIN_YYYY_MM_DD_format) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int day = Integer.parseInt(arr[2]);
		String midJoining = "";
		if (day > 15) {
			midJoining = "Yes";
		} else {
			midJoining = "No";
		}
		return midJoining;
	}

	/**
	 * This method returns Quarter difference from Current Date
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getQuarterDiffFromCurrentDate(String DATEIN_YYYY_MM_DD_format) {
		try {
			LocalDate DOJQuarterStartDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate currentQuarterStartDate = getFirstDayOfQuarter(getCurrentLocalDate());
			double quarterDiff = (ChronoUnit.MONTHS.between(DOJQuarterStartDate, currentQuarterStartDate) / 3);
			return quarterDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Quarter difference from Current Date
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getQuarterDiffBetweenTwoDates(String DATEIN_YYYY_MM_DD_format, String endDate) {
		try {
			LocalDate DOJQuarterStartDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate currentQuarterStartDate = getFirstDayOfQuarter(endDate);
			double monthsDiff = ChronoUnit.MONTHS.between(DOJQuarterStartDate, currentQuarterStartDate);
			double quarterDiff = (monthsDiff / 3);
			return quarterDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Month difference from first day of Quarter
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDiffFromFirstDayOfQuarter(String DATEIN_YYYY_MM_DD_format) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);

			LocalDate startDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate endDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			double monthsDiffFromQuarterMonth = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiffFromQuarterMonth;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Month difference from first day of Quarter
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDiffFromLastMonthOfQuarter(String DATEIN_YYYY_MM_DD_format) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);

			LocalDate endDate = getFirstDayOfLastMonthOfCurrentQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate startDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			double monthsDiffFromQuarterMonth = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiffFromQuarterMonth;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Month difference from first day of Quarter
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDiffFromFirstDayOfQuarterBetweenTwoDates(String DATEIN_YYYY_MM_DD_format,
																   String endDateString) {
		try {

			LocalDate startDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate endDate = LocalDate.parse(endDateString).with(firstDayOfMonth());
			double monthsDiffFromQuarterMonth = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiffFromQuarterMonth;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method will return First Day of Quarter in Local Date format
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return Local Date
	 */
	public LocalDate getFirstDayOfQuarter(String DATEIN_YYYY_MM_DD_format) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int DOJQuarter = LocalDate.parse(DATEIN_YYYY_MM_DD_format).get(IsoFields.QUARTER_OF_YEAR);
		int quarterMonth = 0;

		if (DOJQuarter == 1) {
			quarterMonth = 1;
		} else if (DOJQuarter == 2) {
			quarterMonth = 4;
		} else if (DOJQuarter == 3) {
			quarterMonth = 7;
		} else if (DOJQuarter == 4) {
			quarterMonth = 10;
		} else {
			throw new RuntimeException("DOJ is not correct");
		}

		LocalDate startDate = LocalDate.of(year, quarterMonth, 01).with(firstDayOfMonth());
		return startDate;
	}

	/**
	 * This method will return last Day of Quarter in Local Date format
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return Local Date
	 */
	public LocalDate getLastDayOfQuarter(String DATEIN_YYYY_MM_DD_format) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int DOJQuarter = LocalDate.parse(DATEIN_YYYY_MM_DD_format).get(IsoFields.QUARTER_OF_YEAR);
		int quarterMonth = 0;

		if (DOJQuarter == 1) {
			quarterMonth = 3;
		} else if (DOJQuarter == 2) {
			quarterMonth = 6;
		} else if (DOJQuarter == 3) {
			quarterMonth = 9;
		} else if (DOJQuarter == 4) {
			quarterMonth = 12;
		} else {
			throw new RuntimeException("DOJ is not correct");
		}

		LocalDate lastDate = LocalDate.of(year, quarterMonth, 01).with(lastDayOfMonth());
		return lastDate;
	}


	/**
	 * This method will return First Day of Quarter in Local Date format
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return Local Date
	 */
	public LocalDate getFirstDayOfLastMonthOfCurrentQuarter(String DATEIN_YYYY_MM_DD_format) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int DOJQuarter = LocalDate.parse(DATEIN_YYYY_MM_DD_format).get(IsoFields.QUARTER_OF_YEAR);
		int quarterMonth = 0;

		if (DOJQuarter == 1) {
			quarterMonth = 3;
		} else if (DOJQuarter == 2) {
			quarterMonth = 6;
		} else if (DOJQuarter == 3) {
			quarterMonth = 9;
		} else if (DOJQuarter == 4) {
			quarterMonth = 12;
		} else {
			throw new RuntimeException("DOJ is not correct");
		}

		LocalDate startDate = LocalDate.of(year, quarterMonth, 01).with(firstDayOfMonth());
		return startDate;
	}

	public LocalDate getLastDayOfBiannual(String DATEIN_YYYY_MM_DD_format, String leaveCycle) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int biannualEndMonth = 0;
		if (leaveCycle.equalsIgnoreCase("Calendar")) {
			biannualEndMonth = 6;
		}else if (leaveCycle.equalsIgnoreCase("Financial")) {
			biannualEndMonth = 9;
		}

		LocalDate biannualEndDate = LocalDate.of(year, biannualEndMonth, 01).with(lastDayOfMonth());
		return biannualEndDate;
	}

	public static LocalDate getCurrentDateLocalDateFormat(){
		return LocalDate.parse(LocalDate.now().toString());
	}

}
