package com.dbox.settings;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Settings_Attendance_Policy extends Settings {

	WebDriver driver;

	public Settings_Attendance_Policy(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/*
	 * Object Repository for Policy
	 */

	@FindBy(xpath = "//*[@id='policy']/a")
	private WebElement policiesLink;

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

	/**
	 * This method clicks on Policies link
	 * 
	 * @return
	 */
	public boolean clickPoliciesLink() {
		return click(policiesLink, "Policies link");
	}

	/**
	 * This method clicks on Save button
	 * 
	 * @return
	 */
	public boolean clickSaveButton() {
		try {
			
			scrollUp("Save button");
			click(policySaveButton, "Save button");
			checkIfDuplicatePolicyName();		
			return true;
		} catch (Exception e) {
			Reporter("Exception while clicking on Save button: ", "Fail");
			return false;
		}
	}

	/**
	 * This method clicks on Create Policy button
	 * 
	 * @return
	 */
	public boolean clickCreatePolicyButton() {
		return click(createPolicyButton, "Create Policy Button");
	}

	public boolean checkMandatoryFields() {
		try {
			if (excelInput("Policy Name").isEmpty()) {

			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static String policyName;

	/**
	 * This method insert text in Policy Name text box
	 * 
	 * @param text
	 * @return
	 */
	public boolean insertPolicyName(String text) {
		try {
			policyName = excelInput("Policy Name_TextBox");
			return insertText(policyNameTextBox, "Policy Name", policyName);
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
						policyName = policyName + getRandomNumber();
						insertText(policyNameTextBox, "Policy Name", policyName);
						click(policySaveButton, "Save button");
						return true;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method chooses Leave Deduction Policy from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean enableOrDisableFromLeaveDeductionPolicyDropdown(String text) {
		return selectVisibleTextFromDropDown(policyleaveDeductionDropDown, text, "Leave Deduction Policy");
	}

	/**
	 * This method chooses Leave Deduction Policy from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromLeaveDeductionPolicy_DeductLeavesDropdown(String text) {
		return selectVisibleTextFromDropDown(policyDeductLeavesFromDropDown, text,
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
			return enableOrDisableCheckBox(policyDeductLeavesAfterApprovalCheckBox,
					excelInput("WorkDuration_Deduct Leave after Approval?_CheckBox"),
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
			return enableOrDisableCheckBox(policyLeaveDeductionOnHolidayCheckBox,
					excelInput("Leave Deduction On Holiday_CheckBox"),
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
			return enableOrDisableCheckBox(policyLeaveDeductionOnWeeklyOffCheckBox,
					excelInput("Leave Deduction On Weeklyoff_CheckBox"),
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
			return enableOrDisableCheckBox(policyLeaveDeductionOnWeeklyOffCheckBox,
					excelInput("Leave Deduction On Weeklyoff_CheckBox"),
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
		return selectVisibleTextFromDropDown(policyLateMarkDropDown, text, "Late Mark policy");
	}

	/**
	 * This method chooses Late Mark Policy Deduct Leaves from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromLateMarkLeaveDeductionDropdown(String text) {
		return selectVisibleTextFromDropDown(policyLateMarkLeaveDeductionDropDown, text,
				"Under Late Mark policy: Late Mark Leave Deduction");
	}

	/**
	 * This method chooses Late Mark Leave Type from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromLateMarkLeaveTypeDropDown(String text) {
		return selectVisibleTextFromDropDown(policyLateMarkLeaveTypeDropDown, text,
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
			return insertText(policyNumberOfLateMarkTextBox, "Number of Late mark on which leave should be deducted",
					excelInput("Number of Late mark on which leave should be deducted_TextBox"));
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
			return enableOrDisableCheckBox(policyLateMarkDeductionAfterApprovalCheckBox, enableOrDisable,
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
			return enableOrDisableCheckBox(policyLateMarkDeductionOnHolidayCheckBox, enableOrDisable,
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
			return enableOrDisableCheckBox(policyLateMarkDeductionOnWeeklyOffCheckBox, enableOrDisable,
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
	public boolean enableOrDisableLeaveDeductionPolicy(String text) {
		try {
			if (text.equalsIgnoreCase("Enable")) {
				enableOrDisableFromLeaveDeductionPolicyDropdown("Enable");
				selectFromLeaveDeductionPolicy_DeductLeavesDropdown(excelInput("Deduct Leaves From_Dropdown"));
				enableOrDisableLeaveDeductionPolicy_DeductLeaveAfterApprovalCheckBox(
						excelInput("LeaveDeductionPolicy_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableLeaveDeductionPolicy_LeaveDeductionOnHolidayCheckBox(
						excelInput("Leave Deduction On Holiday_CheckBox"));
				enableOrDisableLeaveDeductionPolicy_LeaveDeductionOnWeeklyOffCheckBox(
						excelInput("Leave Deduction On Weeklyoff_CheckBox"));
				return true;
			} else if (text.equalsIgnoreCase("Disable")) {
				enableOrDisableFromLeaveDeductionPolicyDropdown("Disable");
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
				selectFromLateMarkLeaveDeductionDropdown(excelInput("Late Mark Leave Deduction_Dropdown"));
				selectFromLateMarkLeaveTypeDropDown(excelInput("Late Mark Leave Type_Dropdown"));
				insertNumberOfLateMarkLeaveDeductedTextBox(
						excelInput("Number of Late mark on which leave should be deducted_TextBox"));
				enableOrDisableLateMarkPolicyDeductLeaveAfterApprovalCheckBox(
						excelInput("LateMark_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableLateMarkDeductionOnHolidayCheckBox(excelInput("Latemark Deduction On Holiday_CheckBox"));
				enableOrDisableLateMarkDeductionOnWeekOffCheckBox(
						excelInput("Latemark Deduction On Weeklyoff_CheckBox"));

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
		return selectVisibleTextFromDropDown(policyWorkDurationDropDown, text, "Work Duration policy");
	}

	/**
	 * This method chooses Work Duration Policy Minimum Duration To Count Half Day
	 * from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromMinimumDurationToCountHalfDayDropdown(String hr, String min) {
		selectVisibleTextFromDropDown(policyMinimumDurationToCountHalfDayHrDropdown, hr,
				"Under Work Duration Policy: Minimum Duration To Count Half Day Hr");
		selectVisibleTextFromDropDown(policyMinimumDurationToCountHalfDayMinDropdown, min,
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
		return selectVisibleTextFromDropDown(policyWorkDurationLeaveTypeDropDown, text, "Work Duration Leave Type");
	}

	/**
	 * This method chooses Work Duration Policy Minimum Duration To Count Full Day
	 * from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromMinimumDurationToCountFullDayDropdown(String hr, String min) {
		selectVisibleTextFromDropDown(policyMinimumWorkDurationToCountFullDayHrDropDown, hr,
				"Under Work Duration Policy: Minimum Duration To Count Full Day Hr");
		selectVisibleTextFromDropDown(policyMinimumWorkDurationToCountFullDayMinDropDown, min,
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
			return enableOrDisableCheckBox(policyWorkDurationDeductLeaveAfterApprovalCheckBox, enableOrDisable,
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
			return enableOrDisableCheckBox(policyWorkDurationDeductLeaveOnHolidaylCheckBox, enableOrDisable,
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
			return enableOrDisableCheckBox(policyWorkDurationDeductLeaveOnWeeklyOffCheckBox, enableOrDisable,
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
						excelInput("Minimum duration to count half day_Hr_Dropdown"),
						excelInput("Minimum duration to count half day_Min_Dropdown"));
				selectFromWorkDurationLeaveTypeDropDown(excelInput("Work Duration Leave Type_Dropdown"));
				selectFromMinimumDurationToCountFullDayDropdown(
						excelInput("Minimum duration to count Full day_Hr_Dropdown"),
						excelInput("Minimum duration to count Full day_Min_Dropdown"));
				enableOrDisableWorkDurationPolicyDeductLeaveAfterApprovalCheckBox(
						excelInput("WorkDuration_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableWorkDurationDeductionOnHolidayCheckBox(
						excelInput("Work Duration Deduction On Holiday_CheckBox"));
				enableOrDisableWorkDurationDeductionOnWeekOffCheckBox(
						excelInput("Work Duration Deduction On Weeklyoff_CheckBox"));
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
		return selectVisibleTextFromDropDown(policyEarlyMarkDropDown, text, "Early Mark policy");
	}

	/**
	 * This method chooses Early Mark Policy Deduct Leaves from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromEarlyMarkLeaveDeductionDropdown(String text) {
		return selectVisibleTextFromDropDown(policyEarlyMarkLeaveDeductionDropDown, text,
				"Under Late Mark policy: Early Mark Leave Deduction");
	}

	/**
	 * This method chooses Early Mark Leave Type from Drop down
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectFromEarlyMarkLeaveTypeDropDown(String text) {
		return selectVisibleTextFromDropDown(policyEarlyMarkLeaveTypeDropDown, text,
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
			return insertText(policyNumberOfEarlyMarkTextBox, "Number of Early mark on which leave should be deducted",
					excelInput("Number of Early mark on which leave should be deducted_TextBox"));
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
			return enableOrDisableCheckBox(policyEarlyMarkDeductionAfterApprovalCheckBox, enableOrDisable,
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
			return enableOrDisableCheckBox(policyEarlyMarkDeductionOnHolidayCheckBox, enableOrDisable,
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
			return enableOrDisableCheckBox(policyEarlyMarkDeductionOnWeeklyOffCheckBox, enableOrDisable,
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
				selectFromEarlyMarkLeaveDeductionDropdown(excelInput("Early Mark Leave Deduction_Dropdown"));
				selectFromEarlyMarkLeaveTypeDropDown(excelInput("Early Mark Leave Type_Dropdown"));
				insertNumberOfEarlyMarkLeaveDeductedTextBox(
						excelInput("Number of Early mark on which leave should be deducted_TextBox"));
				enableOrDisableEarlyMarkPolicyDeductLeaveAfterApprovalCheckBox(
						excelInput("EarlyMark_Deduct Leave after Approval?_CheckBox"));
				enableOrDisableEarlyMarkDeductionOnHolidayCheckBox(
						excelInput("Earlymark Deduction On Holiday_CheckBox"));
				enableOrDisableEarlyMarkDeductionOnWeekOffCheckBox(
						excelInput("Earlymark Deduction On Weeklyoff_CheckBox"));

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
		waitForPageToLoad();
		driver.switchTo().defaultContent();
		//click(policySearchFilterTextBox, "Search: ");
		waitAndClickElement(policySearchFilterTextBox, "Search: ");
		return insertText(policySearchFilterTextBox, "Search: ", policyName);
	}

	/**
	 * This method verifies shift details
	 * 
	 * @return
	 */
	public boolean checkPolicyDetails() {
		try {
			if (getDetailsFromShiftTable(1, "Policy name").contentEquals(policyName)) {
				System.out.println(getDetailsFromShiftTable(1, "Shift Name"));
				Reporter("Validated policy details on page sucessfully", "Pass");
				return true;
			}
			return false;
		} catch (Exception e) {
			Reporter("Policy Name not matching", "Fail");

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
			click(ele, "Delete policy button");
			acceptAlert();
			Reporter("Record deleted successfully", "Pass");
			return true;
		} catch (Exception e) {
			Reporter("Exception while deleting Policy", "Pass");
			return false;
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
			return driver.findElement(By.xpath(text)).getText();
		} catch (Exception e) {
			Reporter("Exception while get Details From Shift Table" + e.getMessage(), "Fail");
			throw new RuntimeException("Exception while get Details From Shift Table" + e.getMessage());
		}
	}

	
}
