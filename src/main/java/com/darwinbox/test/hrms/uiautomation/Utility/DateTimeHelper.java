/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.Calendar;
import static java.time.temporal.TemporalAdjusters.*;

/**
 * @author balaji
 * @Creation_Date: 20 Nov 2017
 * @ClassName: DateTimeHelper.java
 * @LastModified_Date: 20 Nov 2017
 */
public class DateTimeHelper {

	public static String getCurrentDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
		Calendar cal = Calendar.getInstance();
		String time = "" + dateFormat.format(cal.getTime());
		return time;
	}

	public static String getCurrentDate() {
		return getCurrentDateTime().substring(0, 11);
	}

	public Year getCurrentYear() {
		return Year.now();
	}

	/**
	 * This month will calculate difference between months
	 * 
	 * @param Year
	 * @param Month
	 * @param Date
	 * @return
	 */
	public double getMonthDifferenceFromCurrentDate(String DATEIN_YYYY_MM_DD_format) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);
 
			LocalDate startDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			LocalDate endDate = LocalDate.now().with(firstDayOfMonth());
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff;
			
		} catch (Exception e) {
			// Reporter("Exception while getting month difference from current date",
			// "Fail");
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

	public double getQuarterDiffFromCurrentDate(String DATEIN_YYYY_MM_DD_format) {
		try {
			// double monthDiff =
			// getMonthDifferenceFromCurrentDate(DATEIN_YYYY_MM_DD_format);
			// double quarterDiff = (monthDiff / 3);
			LocalDate DOJQuarterStartDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate currentQuarterStartDate = getFirstDayOfQuarter(LocalDate.now().toString());
			double quarterDiff = (ChronoUnit.MONTHS.between(DOJQuarterStartDate, currentQuarterStartDate) / 3);
			return quarterDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


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

	public void getMonthDiff(String DATEIN_YYYY_MM_DD_format) {
		
		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);
		int month = Integer.parseInt(arr[1]);
		int day = Integer.parseInt(arr[2]);
	
	
	}
	
}
