package com.darwinbox.test.hrms.uiautomation.Settings.PageObject;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class AttendanceSettingsPolicyPage extends TestBase{

	WaitHelper objWait ;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	BrowserHelper objBrowserHelper;
	HomePage homePage;
	
	WebDriver driver;
	public static final Logger log = Logger.getLogger(AttendanceSettingsPolicyPage.class);


	public AttendanceSettingsPolicyPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
		objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
		objJavaScrHelper = PageFactory.initElements(driver, JavaScriptHelper.class);
		objBrowserHelper = PageFactory.initElements(driver, BrowserHelper.class);
		homePage =PageFactory.initElements(driver, HomePage.class);
		
	}

	/*
	 * Object Repository for Policy
	 */

	@FindBy(xpath = "//*[@id='policy']/a")
	private WebElement policiesLink;
	
	@FindBy(xpath="//*[@class='main-navigation']/div[4]/div[1]/ul/li[2]/a]")
	protected WebElement userIcon;

	@FindBy(id = "create_policy_btn")
	private WebElement createPolicyButton;

	@FindBy(xpath = "//*[@id='AttendancePolicyForm_policy_name']")
	private WebElement policyNameTextBox;

	@FindBy(xpath = "//*[@id='input-error']/div//li")
	private List<WebElement> policyErrorMsg;

	@FindBy(id = "AttendancePolicyForm_policy_description")
	private WebElement policyDescriptionTextBox;

	@FindBy(id = "dept_grp_company")
	private WebElement policyGrpCompanyDropDown;

	@FindBy(id = "AttendancePolicyForm_grace_time")
	private WebElement policyGraceTimeDropDown;

	@FindBy(id = "AttendancePolicyForm_grace_time_early")
	private WebElement policyGraceTimeEarlyClockOutDropDown;

	@FindBy(id = "AttendancePolicyForm_over_time")
	private WebElement policyOverTimeHrDropDown;

	@FindBy(id = "AttendancePolicyForm_over_time_mins")
	private WebElement policyOverTimeMinDropDown;

	@FindBy(id = "AttendancePolicyForm_max_optional_holiday_policy")
	private WebElement policyOptionalHolidayTextBox;

	@FindBy(id = "AttendancePolicyForm_markin_policy")
	private WebElement policyMarkinPolicyDropDown;

	@FindBy(id = "AttendancePolicyForm_work_from_home")
	private WebElement policyWorkFromHomeCheckBox;

	@FindBy(id = "AttendancePolicyForm_out_duty	")
	private WebElement policyOutDutyCheckBox;

	@FindBy(id = "AttendancePolicyForm_edit_back_days")
	private WebElement policyEditBackDatedAttendanceTextBox;

	@FindBy(id = "AttendancePolicyForm_apply_back_days_employee")
	private WebElement policyRestrictBackDatedAttendanceTextBox;

	@FindBy(id = "AttendancePolicyForm_auto_approve_days")
	private WebElement policyAutoApproveAttendanceDaysTextBox;

	@FindBy(id = "AttendancePolicyForm_absconding_days")
	private WebElement policyAbscondingDaysTextBox;

	@FindBy(id = "AttendancePolicyForm_disable_work_duration")
	private WebElement policyDisableWorkDurationCheckBox;

	@FindBy(id = "AttendancePolicyForm_disable_late_mark")
	private WebElement policyDisableLateMarkCheckBox;

	@FindBy(id = "AttendancePolicyForm_disable_overtime")
	private WebElement policyDisableOverTimeCheckBox;

	@FindBy(id = "AttendancePolicyForm_allow_attrequest_weekoff")
	private WebElement policyAllowAttendanceOnWeeklyOffCheckBox;

	@FindBy(id = "AttendancePolicyForm_allow_attrequest_holiday")
	private WebElement policyAllowAttendanceOnHolidayCheckBox;

	@FindBy(id = "AttendancePolicyForm_leave_deduction_policy")
	private WebElement policyleaveDeductionDropDown;

	@FindBy(id = "AttendancePolicyForm_leave_deduction")
	private WebElement policyDeductLeavesFromDropDown;

	@FindBy(id = "AttendancePolicyForm_leave_deduction_deduct_after_approval")
	private WebElement policyDeductLeavesAfterApprovalCheckBox;

	@FindBy(id = "AttendancePolicyForm_leave_deduction_on_holiday")
	private WebElement policyLeaveDeductionOnHolidayCheckBox;

	@FindBy(id = "AttendancePolicyForm_leave_deduction_on_weeklyoff")
	private WebElement policyLeaveDeductionOnWeeklyOffCheckBox;

	@FindBy(id = "AttendancePolicyForm_latemark_policy")
	private WebElement policyLateMarkDropDown;

	@FindBy(id = "AttendancePolicyForm_latemark_leave_deduction")
	private WebElement policyLateMarkLeaveDeductionDropDown;

	@FindBy(id = "AttendancePolicyForm_latemark_leave_type")
	private WebElement policyLateMarkLeaveTypeDropDown;

	@FindBy(id = "AttendancePolicyForm_number_of_latemarks")
	private WebElement policyNumberOfLateMarkTextBox;

	@FindBy(id = "AttendancePolicyForm_latemark_deduct_after_approval")
	private WebElement policyLateMarkDeductionAfterApprovalCheckBox;

	@FindBy(id = "AttendancePolicyForm_latemark_deduction_on_holiday")
	private WebElement policyLateMarkDeductionOnHolidayCheckBox;

	@FindBy(id = "AttendancePolicyForm_latemark_deduction_on_weeklyoff")
	private WebElement policyLateMarkDeductionOnWeeklyOffCheckBox;

	@FindBy(id = "AttendancePolicyForm_work_duration_policy")
	private WebElement policyWorkDurationDropDown;

	@FindBy(id = "AttendancePolicyForm_clock_in_hrs_half_day")
	private WebElement policyMinimumDurationToCountHalfDayHrDropdown;

	@FindBy(id = "AttendancePolicyForm_clock_in_min_half_day")
	private WebElement policyMinimumDurationToCountHalfDayMinDropdown;

	@FindBy(id = "AttendancePolicyForm_work_duration_leave_type")
	private WebElement policyWorkDurationLeaveTypeDropDown;

	@FindBy(id = "AttendancePolicyForm_clock_in_hrs_full_day")
	private WebElement policyMinimumWorkDurationToCountFullDayHrDropDown;

	@FindBy(id = "AttendancePolicyForm_clock_in_min_full_day")
	private WebElement policyMinimumWorkDurationToCountFullDayMinDropDown;

	@FindBy(id = "AttendancePolicyForm_work_duration_deduct_after_approval")
	private WebElement policyWorkDurationDeductLeaveAfterApprovalCheckBox;

	@FindBy(id = "AttendancePolicyForm_work_duration_deduction_on_holiday")
	private WebElement policyWorkDurationDeductLeaveOnHolidaylCheckBox;

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
	private WebElement policyEarlyMarkDeductionAfterApprovalCheckBox;

	@FindBy(id = "AttendancePolicyForm_earlymark_deduction_on_holiday")
	private WebElement policyEarlyMarkDeductionOnHolidayCheckBox;

	@FindBy(id = "AttendancePolicyForm_earlymark_deduction_on_weeklyoff")
	private WebElement policyEarlyMarkDeductionOnWeeklyOffCheckBox;

	@FindBy(xpath = "//*[@id='policy_btn']")
	private WebElement policySaveButton;

	@FindBy(xpath = "//*[@id='policy_table_filter']/label/input")
	private WebElement policySearchFilterTextBox;
	
	@FindBy(xpath = "//*[@id='policy_table']")
	protected WebElement policyTable;
	
	@FindBy(xpath="//*[@id='info_message']/div")
	protected WebElement errorDialog;

	/**
	 * This method clicks on Policies link
	 * 
	 * @return
	 */
	public boolean clickPoliciesLink() {
		return objGenHelper.elementClick(policiesLink, "Policies link");
	}

	/**
	 * This method clicks on Save button
	 * 
	 * @return
	 */
	public boolean clickSaveButton() {
		try {
			objJavaScrHelper.scrollUpVertically("Save button");
			objGenHelper.elementClick(policySaveButton, "Save button");
			checkIfDuplicatePolicyName();
			return true;
		} catch (Exception e) {
			Reporter("Exception while clicking on Save button: " + e.getMessage(), "Fail");
			return false;
		}
	}

	/**
	 * This method clicks on Create Policy button
	 * 
	 * @return
	 */
	public boolean clickCreatePolicyButton() {
		return objGenHelper.elementClick(createPolicyButton, "Create Policy Button");
	}

	public boolean checkMandatoryFields() {
		try {
			if (getData("Policy Name").isEmpty()) {

			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String policyName = "";

	/**
	 * This method insert text in Policy Name text box
	 * 
	 * @param text
	 * @return
	 */
	public boolean insertPolicyName(String text) {
		try {
			policyName = text;
			return objGenHelper.setElementText(policyNameTextBox, "Policy Name", policyName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method creates a random shift if Shift Name is duplicate
	 * 
	 * @return
	 */
	public boolean checkIfDuplicatePolicyName() {
		try {

			String duplicatePolicyErrorMsg = policyName + " already exist.";
			if (policyErrorMsg.size() > 0) {
				for (int i = 0; i < policyErrorMsg.size(); i++) {
					if (policyErrorMsg.get(i).getText().trim().contains(duplicatePolicyErrorMsg)) {
						Reporter(policyErrorMsg.get(i).getText(), "Pass");
						policyName = policyName + objGenHelper.getRandomNumber();
						objGenHelper.setElementText(policyNameTextBox, "Policy Name", policyName);
						objGenHelper.elementClick(policySaveButton, "Save button");
						return true;
					}
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This method chooses Leave Deduction Policy from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromLeaveDeductionPolicyDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyleaveDeductionDropDown, text,
				"Leave Deduction Policy");
	}

	/**
	 * This method chooses Leave Deduction Policy from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromLeaveDeductionPolicy_DeductLeavesDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyDeductLeavesFromDropDown, text,
				"Under Leave Deduction Policy: Deduct Leaves from");
	}

	/**
	 * This method Leave Deduction Policy Deduct: Leave After Approval
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableLeaveDeductionPolicy_DeductLeaveAfterApprovalCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyDeductLeavesAfterApprovalCheckBox,
					getData("WorkDuration_Deduct Leave after Approval?_CheckBox"),
					"Leave Deduction Policy Deduct: Leave After Approval");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method Leave Deduction Policy Deduct: Leave Deduction On
	 * Holiday_CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableLeaveDeductionPolicy_LeaveDeductionOnHolidayCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyLeaveDeductionOnHolidayCheckBox,
					getData("Leave Deduction On Holiday_CheckBox"),
					"Leave Deduction Policy Deduct: Leave Deduction On Holiday");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method Leave Deduction Policy Deduct: Leave Deduction On Weekly Off
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableLeaveDeductionPolicy_LeaveDeductionOnWeeklyOffCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyLeaveDeductionOnWeeklyOffCheckBox,
					getData("Leave Deduction On Weeklyoff_CheckBox"),
					"Leave Deduction Policy Deduct: Leave Deduction On Weeklyoff");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method Leave Deduction Policy Deduct: Leave Deduction On Weekly Off
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableLeaveDeductionPolicyLeaveDeductionOnWeeklyOffCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyLeaveDeductionOnWeeklyOffCheckBox,
					getData("Leave Deduction On Weeklyoff_CheckBox"),
					"Leave Deduction Policy Deduct: Leave Deduction On Weeklyoff");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method chooses Late Mark Policy Deduct Leaves from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean enableOrDisableFromLateMarkPolicyDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyLateMarkDropDown, text, "Late Mark policy");
	}

	/**
	 * This method chooses Late Mark Policy Deduct Leaves from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromLateMarkLeaveDeductionDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyLateMarkLeaveDeductionDropDown, text,
				"Under Late Mark policy: Late Mark Leave Deduction");
	}

	/**
	 * This method chooses Late Mark Leave Type from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromLateMarkLeaveTypeDropDown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyLateMarkLeaveTypeDropDown, text,
				"Under Late Mark policy: Late Mark Leave Type");
	}

	/**
	 * This method insert text in Number Of Late Mark Leave Deducted text box
	 * 
	 * @param text
	 * @return
	 */
	public boolean insertNumberOfLateMarkLeaveDeductedTextBox(String text) {
		try {
			return objGenHelper.setElementText(policyNumberOfLateMarkTextBox, "Number of Late mark on which leave should be deducted",
					getData("Number of Late mark on which leave should be deducted_TextBox"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Late Mark Policy Deduct Leave After Approval
	 * CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableLateMarkPolicyDeductLeaveAfterApprovalCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyLateMarkDeductionAfterApprovalCheckBox, enableOrDisable,
					"Deduct Leave after Approval");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Late Mark Deduction On Holiday CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableLateMarkDeductionOnHolidayCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyLateMarkDeductionOnHolidayCheckBox, enableOrDisable,
					"Late Mark Deduction On Holiday");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Late Mark Deduction On Weekly off CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableLateMarkDeductionOnWeekOffCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyLateMarkDeductionOnWeeklyOffCheckBox, enableOrDisable,
					"Latemark Deduction On Weeklyoff");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enables or disables Leave Deduction in Policy
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectLeaveDeductionPolicy(String text) {
		try {
			if (text.equalsIgnoreCase("Enable")) {
				selectFromLeaveDeductionPolicyDropdown("Enable");
				selectFromLeaveDeductionPolicy_DeductLeavesDropdown(
						getData("Deduct Leaves From_Dropdown"));
				enableOrDisableLeaveDeductionPolicy_DeductLeaveAfterApprovalCheckBox(
						getData("LeaveDeductionPolicy_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableLeaveDeductionPolicy_LeaveDeductionOnHolidayCheckBox(
						getData("Leave Deduction On Holiday_CheckBox"));
				enableOrDisableLeaveDeductionPolicy_LeaveDeductionOnWeeklyOffCheckBox(
						getData("Leave Deduction On Weeklyoff_CheckBox"));
				return true;
			} else if (text.equalsIgnoreCase("Disable")) {
				selectFromLeaveDeductionPolicyDropdown("Disable");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method insert text in Policy Name text box
	 * 
	 * @param text
	 * @return
	 */
	public boolean enableOrDisableLateMarkPolicy(String text) {
		try {
			if (text.equalsIgnoreCase("Enable")) {
				enableOrDisableFromLateMarkPolicyDropdown("Enable");
				selectFromLateMarkLeaveDeductionDropdown(getData("Late Mark Leave Deduction_Dropdown"));
				selectFromLateMarkLeaveTypeDropDown(getData("Late Mark Leave Type_Dropdown"));
				insertNumberOfLateMarkLeaveDeductedTextBox(
						getData("Number of Late mark on which leave should be deducted_TextBox"));
				enableOrDisableLateMarkPolicyDeductLeaveAfterApprovalCheckBox(
						getData("LateMark_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableLateMarkDeductionOnHolidayCheckBox(
						getData("Latemark Deduction On Holiday_CheckBox"));
				enableOrDisableLateMarkDeductionOnWeekOffCheckBox(
						getData("Latemark Deduction On Weeklyoff_CheckBox"));

				return true;
			} else if (text.equalsIgnoreCase("Disable")) {
				enableOrDisableFromLateMarkPolicyDropdown("Disable");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/* Work Duration Policy */

	/**
	 * This method chooses Work Duration Policy Deduct Leaves from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean enableOrDisableFromWorkDurationPolicyDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyWorkDurationDropDown, text, "Work Duration policy");
	}

	/**
	 * This method chooses Work Duration Policy Minimum Duration To Count Half Day
	 * from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromMinimumDurationToCountHalfDayDropdown(String hr, String min) {
		objDropDownHelper.selectUsingVisibleValue( policyMinimumDurationToCountHalfDayHrDropdown, hr,
				"Under Work Duration Policy: Minimum Duration To Count Half Day Hr");
		objDropDownHelper.selectUsingVisibleValue( policyMinimumDurationToCountHalfDayMinDropdown, min,
				"Under Work Duration Policy: Minimum Duration To Count Half Day Minutes");
		return true;
	}

	/**
	 * This method chooses Work Duration Leave Type from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromWorkDurationLeaveTypeDropDown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyWorkDurationLeaveTypeDropDown, text,
				"Work Duration Leave Type");
	}

	/**
	 * This method chooses Work Duration Policy Minimum Duration To Count Full Day
	 * from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromMinimumDurationToCountFullDayDropdown(String hr, String min) {
		objDropDownHelper.selectUsingVisibleValue( policyMinimumWorkDurationToCountFullDayHrDropDown, hr,
				"Under Work Duration Policy: Minimum Duration To Count Full Day Hr");
		objDropDownHelper.selectUsingVisibleValue( policyMinimumWorkDurationToCountFullDayMinDropDown, min,
				"Under Work Duration Policy: Minimum Duration To Count Full Day Minutes");
		return true;
	}

	/**
	 * This method enable Or Disable Late Mark Policy Deduct Leave After Approval
	 * CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableWorkDurationPolicyDeductLeaveAfterApprovalCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyWorkDurationDeductLeaveAfterApprovalCheckBox, enableOrDisable,
					"Deduct Leave after Approval");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Work Duration Deduction On Holiday CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableWorkDurationDeductionOnHolidayCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyWorkDurationDeductLeaveOnHolidaylCheckBox, enableOrDisable,
					"Work Duration Deduction On Holiday");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Late Mark Deduction On Weekly off CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableWorkDurationDeductionOnWeekOffCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyWorkDurationDeductLeaveOnWeeklyOffCheckBox, enableOrDisable,
					"Work Duration Deduction On Weeklyoff");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enables or disables Work duration Policy
	 * 
	 * @param text
	 * @return
	 */
	public boolean enableOrDisableWorkDurationPolicy(String text) {
		try {
			if (text.equalsIgnoreCase("Enable")) {
				enableOrDisableFromWorkDurationPolicyDropdown("Enable");
				selectFromMinimumDurationToCountHalfDayDropdown(
						getData("Minimum duration to count half day_Hr_Dropdown"),
						getData("Minimum duration to count half day_Min_Dropdown"));
				selectFromWorkDurationLeaveTypeDropDown(getData("Work Duration Leave Type_Dropdown"));
				selectFromMinimumDurationToCountFullDayDropdown(
						getData("Minimum duration to count Full day_Hr_Dropdown"),
						getData("Minimum duration to count Full day_Min_Dropdown"));
				enableOrDisableWorkDurationPolicyDeductLeaveAfterApprovalCheckBox(
						getData("WorkDuration_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableWorkDurationDeductionOnHolidayCheckBox(
						getData("Work Duration Deduction On Holiday_CheckBox"));
				enableOrDisableWorkDurationDeductionOnWeekOffCheckBox(
						getData("Work Duration Deduction On Weeklyoff_CheckBox"));
				return true;
			} else if (text.equalsIgnoreCase("Disable")) {
				enableOrDisableFromWorkDurationPolicyDropdown("Disable");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method chooses Early Mark Policy Deduct Leaves from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean enableOrDisableFromEarlyMarkPolicyDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyEarlyMarkDropDown, text, "Early Mark policy");
	}

	/**
	 * This method chooses Early Mark Policy Deduct Leaves from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromEarlyMarkLeaveDeductionDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyEarlyMarkLeaveDeductionDropDown, text,
				"Under Late Mark policy: Early Mark Leave Deduction");
	}

	/**
	 * This method chooses Early Mark Leave Type from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromEarlyMarkLeaveTypeDropDown(String text) {
		return objDropDownHelper.selectUsingVisibleValue( policyEarlyMarkLeaveTypeDropDown, text,
				"Under Late Mark policy: Early Mark Leave Type");
	}

	/**
	 * This method insert text in Number Of Early Mark Leave Deducted text box
	 * 
	 * @param text
	 * @return
	 */
	public boolean insertNumberOfEarlyMarkLeaveDeductedTextBox(String text) {
		try {
			return objGenHelper.setElementText(policyNumberOfEarlyMarkTextBox, "Number of Early mark on which leave should be deducted",
					getData("Number of Early mark on which leave should be deducted_TextBox"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Early Mark Policy Deduct Leave After Approval
	 * CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableEarlyMarkPolicyDeductLeaveAfterApprovalCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyEarlyMarkDeductionAfterApprovalCheckBox, enableOrDisable,
					"Deduct Leave after Approval");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Early Mark Deduction On Holiday CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableEarlyMarkDeductionOnHolidayCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyEarlyMarkDeductionOnHolidayCheckBox, enableOrDisable,
					"Early Mark Deduction On Holiday");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enable Or Disable Early Mark Deduction On Weekly off CheckBox
	 * 
	 * @param enableOrDisable
	 * @return
	 */
	public boolean enableOrDisableEarlyMarkDeductionOnWeekOffCheckBox(String enableOrDisable) {
		try {
			return objGenHelper.toggleElementStatus(policyEarlyMarkDeductionOnWeeklyOffCheckBox, enableOrDisable,
					"Early mark Deduction On Weeklyoff");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method enables or disables Work duration Policy
	 * 
	 * @param text
	 * @return
	 */
	public boolean enableOrDisableEarlyMarkPolicy(String text) {
		try {
			if (text.equalsIgnoreCase("Enable")) {
				enableOrDisableFromEarlyMarkPolicyDropdown("Enable");
				selectFromEarlyMarkLeaveDeductionDropdown(getData("Early Mark Leave Deduction_Dropdown"));
				selectFromEarlyMarkLeaveTypeDropDown(getData("Early Mark Leave Type_Dropdown"));
				insertNumberOfEarlyMarkLeaveDeductedTextBox(
						getData("Number of Early mark on which leave should be deducted_TextBox"));
				enableOrDisableEarlyMarkPolicyDeductLeaveAfterApprovalCheckBox(
						getData("EarlyMark_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableEarlyMarkDeductionOnHolidayCheckBox(
						getData("Earlymark Deduction On Holiday_CheckBox"));
				enableOrDisableEarlyMarkDeductionOnWeekOffCheckBox(
						getData("Earlymark Deduction On Weeklyoff_CheckBox"));
				return true;
			} else if (text.equalsIgnoreCase("Disable")) {
				enableOrDisableFromEarlyMarkPolicyDropdown("Disable");
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean searchPolicyName() {
		//objWait.waitForPageToLoad();
		driver.switchTo().defaultContent();
		// objGenHelper.elementClick(policySearchFilterTextBox, "Search: ");
		//objWait.waitAndClickElement(policySearchFilterTextBox, "Search: ");
		//objWait.waitForElement(policyTable, 30);
		return objGenHelper.setElementText(policySearchFilterTextBox, "Search: ", policyName);
	}

	/**
	 * This method verifies shift details
	 * 
	 * @return
	 */
	public boolean checkPolicyDetails() {
		try {
			//objWait.waitForElement(policyTable, 30);
			if (getDetailsFromShiftTable(1, "Policy name").equalsIgnoreCase(policyName)) {
				Reporter("Validated policy details on page sucessfully", "Pass",log);
				return true;
			}else{
				Reporter("Policy details validation on Page failed", "Fail",log);
			}
			return false;
		} catch (Exception e) {
			Reporter("Policy Name not matching", "Fail",log);

			return false;
		}
	}

	/**
	 * This method deletes the Shift created
	 * 
	 * @return
	 * @throws Exception
	 */

	public boolean deletePolicy() throws Exception {
		try {
			String text = "//*[@id='policy_table']/tbody//td[text() = '" + policyName + "']/following::a[2]";
			WebElement ele = driver.findElement(By.xpath(text));
			objGenHelper.elementClick(ele, "Delete policy button");
			objAlertHelper.acceptAlert();
			
			objWait.waitForElementDisapear(errorDialog, 10);
			Reporter("Record deleted successfully", "Pass",log);
			Thread.sleep(10000);
			objBrowserHelper.switchToDefaultWindow();
			homePage.clickUserProfileIcon();			
			return true;
		} catch (Exception e) {
			Reporter("Exception while deleting Policy", "Fail",log);
			return false;
		} finally {
			objWait.waitForPageToLoad();
		}
	}

	/**
	 * This method get details from Shift table
	 * 
	 * @param rowNo
	 * @param colName
	 * @return
	 */
	public String getDetailsFromShiftTable(int rowNo, String colName) {
		WebElement element;
		try {
			int colNo = 1;
			switch (colName.toLowerCase().trim()) {
			case ("Policy name"):
				colNo = 1;
			case ("Group Company"):
				colNo = 2;
			case ("Actions"):
				colNo = 3;
			}

			String text = "//*[@id='policy_table']/tbody/tr[" + rowNo + "]/td[" + colNo + "]";
			element = driver.findElement(By.xpath(text)); 
			objWait.waitForElement(element, 30);
			return element.getText();
		} catch (Exception e) {
			Reporter("Exception while get Details From Shift Table" + e.getMessage(), "Fail");
			throw new RuntimeException("Exception while get Details From Shift Table" + e.getMessage());
		}
	}

	/**
	 * This method edit the Policy details already created
	 * 
	 * @return
	 * @throws Exception
	 */

	public boolean clickEditPolicyButton() throws Exception {
		try {
			String text = "//*[@id='policy_table']/tbody//td[text() = '" + policyName + "']/following::a[1]";
			WebElement ele = driver.findElement(By.xpath(text));
			objWait.waitElementToBeClickable(ele);
			objGenHelper.elementClick(ele, "Edit policy button");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
