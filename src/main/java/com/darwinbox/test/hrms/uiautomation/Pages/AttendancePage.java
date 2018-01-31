/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.Pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;


/**
 * @author balaji
 * @Creation_Date:  29 Nov 2017 
 * @ClassName: RightMenuOptionsPage.java
 * @LastModified_Date:  29 Nov 2017 
 */
public class AttendancePage {

	WaitHelper objWait ;
	GenericHelper objGenHelper;
	WebDriver driver;
	private static final Logger log = Logger.getLogger(AttendancePage.class);
	
	public AttendancePage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
	}
	
	
	final String settingsMainMenuNavClass = "nav-setup-";
	
	@FindBy(xpath = "//a[contains(@class,'" + settingsMainMenuNavClass + "att')]")
	private WebElement settingsAttendance;

	@FindBy(xpath = "//li[@id ='shift']/a")
	private WebElement attendanceShifts;

	@FindBy(xpath = "//li[@id ='policy']/a")
	private WebElement attendancePolicy;

	@FindBy(xpath = "//li[@id ='option']/a")
	private WebElement attendanceIPRestrictions;

	@FindBy(xpath = "//li[@id ='checkin']/a")
	private WebElement attendanceCheckIn;

	/*
	 * Shifts Object Repository
	 */
	@FindBy(id = "create_shift_btn")
	private WebElement createShiftButton;

	@FindBy(xpath = "//*[@id='TenantShiftForm_shift_name']")
	private WebElement shiftNameTextBox;

	@FindBy(xpath = "//*[@id='TenantShiftForm_shift_description']")
	private WebElement shiftDescriptionTextBox;

	@FindBy(xpath = "//*[@id='dept_business_unit']")
	private WebElement groupCompanyDropBox;

	@FindBy(xpath = "//*[@id='TenantShiftForm_begin_time_hour']")
	private WebElement beginTimeHourDropBox;

	@FindBy(xpath = "//*[@id='TenantShiftForm_begin_time_min']")
	private WebElement beginTimeMinDropBox;

	@FindBy(xpath = "//*[@id='TenantShiftForm_end_time_hour']")
	private WebElement endTimeHourDropBox;

	@FindBy(xpath = "//*[@id='TenantShiftForm_end_time_min']")
	private WebElement endTimeMinDropBox;

	@FindBy(id = "shift_btn")
	private WebElement saveShiftButton;

	@FindBy(xpath = "//*[@id='input-error']")
	private WebElement shiftErrorMsg;

	@FindBy(xpath = "//div[@id='input-error']/text()[2]")
	private WebElement shiftErrorMsgList;

	@FindBy(xpath = "//*[@id='shift_table_filter']/label/input")
	private WebElement shiftSearchFilterTextBox;

	@FindBy(xpath = "//*[@id='TenantShiftForm_is_next_day']")
	private WebElement nextDayCheckBox;

	
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
	
	
	
	
	/**
	 * This method objGenHelper.elementClick on Shifts link
	 * 
	 * @author shikhar
	 */
	public boolean clickShifts() {
		return objGenHelper.elementClick(attendanceShifts, "Shifts");
	}

	/**
	 * This method objGenHelper.elementClick on Policies link
	 * 
	 * @author shikhar
	 */
	public boolean clickPolicies() {
		return objGenHelper.elementClick(attendancePolicy, "Policies");
	}

	/**
	 * This method objGenHelper.elementClick on IP Restrictions link
	 * 
	 * @author shikhar
	 */
	public boolean clickIPRestrictions() {
		return objGenHelper.elementClick(attendanceIPRestrictions, "IP Restrictions");
	}

	/**
	 * This method objGenHelper.elementClick on Check IN link
	 * 
	 * @author shikhar
	 */
	public boolean clickCheckIn() {
		return objGenHelper.elementClick(attendanceCheckIn, "Check IN");
	}

	/**
	 * This method objGenHelper.elementClick on Create Shift button
	 * 
	 * @author shikhar
	 */
	public boolean clickCreateShiftBtn() {
		return objGenHelper.elementClick(createShiftButton, "Create Shift button");
	}

	/**
	 * This method objGenHelper.elementClick on Shift button
	 * 
	 * @author shikhar
	 */
	public boolean clickSaveShiftBtn() {
		return objGenHelper.elementClick(saveShiftButton, "Save button");
	}

	/**
	 * This method clicks on Next Day check box
	 * 
	 * @author shikhar
	 */
	public boolean clickNextDayBtn() {
		return objGenHelper.elementClick(nextDayCheckBox, "Next Day check box");
	}

}
