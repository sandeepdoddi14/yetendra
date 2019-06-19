package com.darwinbox.attendance.pages.settings;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.*;

public class AttendanceSettingsPage extends TestBase {

	public AttendanceSettingsPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		waitHelper = new WaitHelper(driver);
	}

	WaitHelper waitHelper;
	GenericHelper genHelper;

	public void waitForLoader() {
		waitHelper.waitForElementDisapear(loader, 6000);
	}

	@FindBy(xpath = "//a[contains(@class,'nav-setup-att')]")
	private WebElement settingsAttendance;

	@FindBy(xpath = "//li[@id ='shift']/a")
	private WebElement attendanceShifts;

	@FindBy(xpath = "//li[@id ='policy']/a")
	private WebElement attendancePolicy;

	@FindBy(xpath = "//li[@id ='option']/a")
	private WebElement attendanceIPRestrictions;

	@FindBy(xpath = "//li[@id ='checkin']/a")
	private WebElement attendanceCheckIn;


	@FindBy(id = "AttendancePolicyForm_work_duration_deduction_on_holiday")
	private WebElement policyWorkDurationDeductLeaveOnHolidayCheckBox;

	@FindBy(id = "AttendancePolicyForm_work_duration_deduction_on_weeklyoff")
	private WebElement policyWorkDurationDeductLeaveOnWeeklyOffCheckBox;

	@FindBy(id = "AttendancePolicyForm_earlymark_policy")
	private WebElement policyEarlyMarkDropDown;

	@FindBy(id = "AttendancePolicyForm_earlymark_leave_deduction")
	private WebElement policyEarlyMarkLeaveDeductionDropDown;

	@FindBy(id = "AttendancePolicyForm_earlymark_leave_type")
	private WebElement policyEarlyMarkLeaveTypeDropDown;

	@FindBy(id = "AttendancePolicyForm_number_of_earlymarks")
	private WebElement policyNumberOfEarlyMarkTextBox;

	@FindBy(id = "AttendancePolicyForm_earlymark_deduct_after_approval")
	private WebElement policyEarlyMarkNumberOfEarlyMarkTextBox;

	@FindBy(id = "AttendancePolicyForm_earlymark_deduct_after_approval")
	private WebElement policyEarlyMarkDeductLeaveAfterApprovalCheckBox;

	@FindBy(id = "AttendancePolicyForm_earlymark_deduction_on_holiday")
	private WebElement policyEarlyMarkDeductLeaveOnHolidayCheckBox;

	@FindBy(id = "AttendancePolicyForm_earlymark_deduction_on_weeklyoff")
	private WebElement policyEarlyMarkDeductLeaveOnWeeklyOffCheckBox;

	@FindBy(id = "policy_btn")
	private WebElement policySaveButton;

	@FindBy(xpath = "//*[@id='policy_table_filter']/label/input")
	private WebElement policySearchFilterTextBox;

	//Attendance Request Form Elements

	@FindBy(id = "attendance_request")
	public WebElement applyAttendancebtn;

	@FindBy(id = "edit_attendance_form")
	public WebElement attendanceForm;

	@FindBy(css = "#request_attendance_modal .close")
	public WebElement attendanceFormClose;

	@FindBy(id = "AttendanceRequestForm_request_type")
	public WebElement attendanceReqType;

	@FindBy(id = "punchin-timestamp")
	public WebElement shiftFromDate;

	@FindBy(id = "punchin-timestamp-to")
	public WebElement shiftToDate;

	@FindBy(id = "AttendanceRequestForm_location")
	public WebElement locationType;

	@FindBy(id = "AttendanceRequestForm_is_next_day")
	public WebElement overNightClockOut;

	@FindBy(id = "AttendanceRequestForm_clock_in_hrs")
	public WebElement clockInHrs;

	@FindBy(id = "AttendanceRequestForm_clock_in_min")
	public WebElement clockInMins;

	@FindBy(id = "AttendanceRequestForm_clock_out_hrs")
	public WebElement clockOutHrs;

	@FindBy(id = "AttendanceRequestForm_clock_out_min")
	public WebElement clockOutMins;

	@FindBy(id = "AttendanceRequestForm_break_hrs")
	public WebElement breaktHrs;

	@FindBy(id = "AttendanceRequestForm_break_min")
	public WebElement breakMins;

	@FindBy(id = "AttendanceRequestForm_message")
	public WebElement requestMessage;

	@FindBy(id = "AddAttachmentWorkFlow_name")
	public WebElement chooseFileBtn;

	@FindBy(id = "add_request_btn")
	public WebElement submitRequestBtn;

	//Calender Elements

	@FindBy(css = "#attendance_log tbody tr")
	public List<WebElement> attendanceRows;

	@FindBy(css = "#attendance_log thead th")
	public List<WebElement> attendanceHeaders;

	@FindAll({
			@FindBy(className = "sorting_desc"),
			@FindBy(className = "sorting_asc")
	})
	public WebElement sortDates;

	@FindBy(id = "year")
	public WebElement selectYear;

	@FindBy(id = "month")
	public WebElement selectMonth;

	@FindBy(id = "waiter")
	public WebElement loader;

	/**
	 * To get the Attendance details of a particular timestamp in Attendance View
	 *
	 * @param timestamp for the timestamp for which attendance status should be returned
	 *             DATE Formate should be as: '31 Mar 2018'
	 * @return returns all the details for a given timestamp as a HashMap
	 */
}
//	public HashMap getAttendanceForDate(String timestamp) {
//		List<WebElement> headers = attendanceHeaders;
//		HashMap<String, String> data = new HashMap<>();
//		for(WebElement row : attendanceRows) {
//			if (row.getText().contains(timestamp)) {
//				for (int i=1; i<=headers.size(); i++) {
//					data.put(headers.get(i-1).toString(), row.findElement(By.xpath("/td["+i+"]")).getText());
//				}
//			} else {
//				throw new RuntimeException("Unable to find the given timestamp in Attendance view");
//			}
//		}
//		return data;
//	}

	/**
	 * To sort the attendance log based on timestamp
	 *
	 * @param isAsc true to set the dates in Ascending order, else false
	 */
//	public boolean sortDates(boolean isAsc) {
//		List<WebElement> headers = attendanceHeaders;
//		boolean value = headers.get(0).getAttribute("class").equals("sorting_desc");
//		if (value == isAsc) {
//			sortDates.click();
//			waitForAjax(5);
//			value = headers.get(0).getAttribute("class").equals("sorting_asc");
//		}
//		return value;
//	}
//
//	/**
//	 * To get the Attendance log of a particular row/timestamp in Attendance View
//	 *
//	 * @param rowIndex row to fetch in Attendance view
//	 * @return returns all the details (Clock in, clock out, assigned policy.... ) for a given row/timestamp in HashMap
//	 */
//	public HashMap<String, String> getAttendanceLogInfo(int rowIndex, boolean isAsc) {
//		List<WebElement> headers = attendanceHeaders;
//		sortDates(isAsc);
//		HashMap<String, String> map = new HashMap<>();
//		for (int i = 1; i <= headers.size(); i++) {
//			if (headers.get(i - 1).getText().equalsIgnoreCase("Assignment")) {
//				String assignment = "";
//				try {
//					assignment = attendanceRows.get(rowIndex - 1).findElement(By.cssSelector("td:nth-child(" + i + ") a")).getAttribute("data-original-title");
//				} catch (NoSuchElementException e) {
//					log.warn("No assignment details found");
//				}
//				map.put(headers.get(i - 1).getText(), assignment);
//			} else {
//				map.put(headers.get(i - 1).getText(), attendanceRows.get(rowIndex - 1).findElement(By.cssSelector("td:nth-child(" + i + ")")).getText());
//			}
//		}
//		return map;
//	}
//
//	/**
//	 * To get the Attendance log of particular column for all dates in a month in Attendance View
//	 *
//	 * @param columnName name of the column (STATUS/CLOCK IN/CLOCK OUT...)
//	 * @return returns the data of a required column for all dates in List formate
//	 */
//	public List<String> getAttendanceLogInfo(String columnName, boolean isAsc) {
//		List<WebElement> headers = attendanceHeaders;
//		sortDates(isAsc);
//		int reqdColumn = 0;
//		for (int i = 0; i < headers.size(); i++) {
//			if (headers.get(i).getText().contains(columnName)) {
//				reqdColumn = i + 1;
//			}
//		}
//		boolean isSpl = columnName.equalsIgnoreCase("Assignment");
//		List<String> reqdValues = new ArrayList<>();
//		for (WebElement ele : attendanceRows) {
//			if (isSpl) {
//				reqdValues.add(ele.findElement(By.cssSelector("td:nth-child(" + reqdColumn + ") a")).getAttribute("data-original-title"));
//			} else {
//				reqdValues.add(ele.findElement(By.cssSelector("td:nth-child(" + reqdColumn + ")")).getText());
//			}
//		}
//		return reqdValues;
//	}
//
//	/**
//	 * To get the Attendance log of particular month in Attendance View
//	 *
//	 * @param isAsc true to set the dates in Ascending order, else false
//	 * @return returns the attendance log information for all dates in List formate
//	 */
//	public List<HashMap<String, String>> getAttendanceLogInfo(boolean isAsc) {
//		List<WebElement> headers = attendanceHeaders;
//		sortDates(isAsc);
//		List<HashMap<String, String>> attendance = new ArrayList<>();
//		for (WebElement ele : attendanceRows) {
//			HashMap<String, String> map = new HashMap<>();
//			for (int i = 1; i <= headers.size(); i++) {
//				if (headers.get(i - 1).getText().equalsIgnoreCase("Assignment")) {
//					String assignment = ele.findElement(By.cssSelector("td:nth-child(" + i + ") a")).getAttribute("data-original-title");
//					map.put(headers.get(i - 1).getText(), assignment);
//				} else {
//					map.put(headers.get(i - 1).getText(), ele.findElement(By.cssSelector("td:nth-child(" + i + ")")).getText());
//				}
//			}
//			attendance.add(map);
//		}
//		return attendance;
//	}
//
//	//2018-04
//	public void selectDate(String timestamp) {
//		String[] dt = timestamp.split("-");
//		selectByVisibleText(selectYear, dt[0], "year");
//		selectByValue(selectMonth, timestamp, "month");
//	}
//
//	public String getAttendanceStatusForDate(Date timestamp) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(timestamp);
//		selectByVisibleText(selectYear, String.valueOf(calendar.get(Calendar.YEAR)), "Year");
//		waitForElementDisapear(loader, 5000);
//		selectByIndex(selectMonth, calendar.get(Calendar.MONTH), "Month");
//		waitForElementDisapear(loader, 5000);
//		return attendanceRows.get(calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)).getText();
//	}
//
//	public void navigateToListViewByUrl() {
//		navigateTo( "/attendance/index/index/view/list");
//	}
//
//	public void navigateToCalendarViewByUrl() {
//		navigateTo("/attendance/index/index/view/calendar");
//	}
//
//	/**
//	 * This method objGenHelper.elementClick on Shifts link
//	 *
//	 * @author shikhar
//	 */
//	public void clickShifts() {
//		click(attendanceShifts, "Shifts");
//	}
//
//	/**
//	 * This method objGenHelper.elementClick on Policies link
//	 *
//	 * @author shikhar
//	 */
//	public void clickPolicies() {
//		click(attendancePolicy, "Policies");
//	}
//
//	/**
//	 * This method objGenHelper.elementClick on IP Restrictions link
//	 *
//	 * @author shikhar
//	 */
//	public void clickIPRestrictions() {
//		click(attendanceIPRestrictions, "IP Restrictions");
//	}
//
//	/**
//	 * This method objGenHelper.elementClick on Check IN link
//	 *
//	 * @author shikhar
//	 */
//	public void clickCheckIn() {
//		click(attendanceCheckIn, "Check IN");
//	}
//
//	/**
//	 * This method objGenHelper.elementClick on Create Shift button
//	 *
//	 * @author shikhar
//	 */
//	public void clickCreateShiftBtn() {
//		click(createShiftButton, "Create Shift button");
//	}
//
//	/**
//	 * This method objGenHelper.elementClick on Shift button
//	 *
//	 * @author shikhar
//	 */
//	public void clickSaveShiftBtn() {
//		click(saveShiftButton, "Save button");
//	}
//
//	/**
//	 * This method clicks on Next Day check box
//	 *
//	 * @author shikhar
//	 */
//	public void clickNextDayBtn() {
//		click(nextDayCheckBox, "Next Day check box");
//	}

