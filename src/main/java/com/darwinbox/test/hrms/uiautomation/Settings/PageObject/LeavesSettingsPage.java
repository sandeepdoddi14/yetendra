package com.darwinbox.test.hrms.uiautomation.Settings.PageObject;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class LeavesSettingsPage extends TestBase {

	WaitHelper objWait;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	WebDriver driver;

	public static final Logger log = Logger.getLogger(LeavesSettingsPage.class);
	

	public LeavesSettingsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/*
	 * Leaves Main Menu Object Repository
	 */

	@FindBy(xpath = "//*[@id='edit']/a")
	private WebElement leavesManageLeavePolicies;

	@FindBy(xpath = "//*[@id='create']/a")
	private WebElement leavesMenuCreateLeavePolicies;

	@FindBy(xpath = "//*[@id='unpaid']/a")
	private WebElement leavesMenuUnpaidLeaves;

	@FindBy(xpath = "//*[@id='holidays']/a")
	private WebElement leavesMenuHolidays;

	@FindBy(xpath = "//*[@id='settings']/a")
	private WebElement leavesMenuSettings;

	@FindBy(xpath = "//*[@id='compoff']/a")
	private WebElement leavesMenuCompoff;

	/*
	 * Create Leave Policy object repository
	 */

	@FindBy(xpath = "//*[@id='leave_policy_create_btn']")
	private WebElement createLeavePolicySaveButton;

	@FindBy(xpath = "//*[@id='dept_grp_company']")
	private WebElement groupCompanyDropdown;

	@FindBy(xpath = "//*[@id='Leaves_description']")
	private WebElement descriptionTextBox;

	@FindBy(xpath = "//*[@id='Leaves_yearly_endowment']")
	private WebElement maximumLeavesAllowedPerYearTextBox;

	@FindBy(xpath = "//*[@id='Leaves_p3_max_consecutive_days_limit']")
	private WebElement consecutiveLeavesAllowedPerMonthTextBox;

	String chooseRestrictionFromListBoxLocator = "//*[@id='parent_dept_load_chosen']/ul/li/input";

	@FindBy(xpath = "//*[@id='Leaves_p4_carry_over_time']")
	private WebElement leaveCycleDropdown;

	@FindBy(xpath = "//*[@id='Leaves_push_leaves_to_admin']")
	private WebElement pushLeaveRequestToAdminCheckbox;

	String restrictLeaveToWeekDaysListBoxLocator = "//*[@id='week_days_chosen']/ul/li/input";

	@FindBy(xpath = "//*[@id='Leaves_auto_approve_days']")
	private WebElement autoApproveLeaveRequestTextBox;

	@FindBy(xpath = "//*[@id='Leaves_name']")
	private WebElement leaveTypeTextBox;

	@FindBy(xpath = "//*[@id='Leaves_restrictGender']")
	private WebElement genderApplicabilityDropdown;

	@FindBy(xpath = "//*[@id='Leaves_pre_approved_no_of_days']")
	private WebElement minimumNoticePeriodTextBox;

	@FindBy(xpath = "//*[@id='Leaves_p1_waiting_after_doj']")
	private WebElement probationPeriodBeforeLeaveValidityTextBox;

	@FindBy(xpath = "//*[@id='Leaves_p2_max_per_month']")
	private WebElement maximumLeavesAllowedPerMonthTextBox;

	@FindBy(xpath = "//*[@id='Leaves_allow_in_notice_period']")
	private WebElement dontAllowLeavesInNoticePeriodCheckBox;

	@FindBy(xpath = "//*[@id='Leaves_max_number_of_leaves_accrued']")
	private WebElement maximumNumberofLeavesAccruedTextBox;

	@FindBy(xpath = "//*[@id='Leaves_min_consecutive_days_limit']")
	private WebElement minimumConsecutiveLeavesTextBox;

	@FindBy(xpath = "//*[@id='Leaves_is_special_leave']")
	private WebElement specialLeaveCheckBox;

	/**
	 * This method clicks on Manage Leave Policies on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickManageLeavePolicies() {
		return objGenHelper.elementClick(leavesManageLeavePolicies, "Manage Leave Policies link");
	}

	/**
	 * This method clicks on Create Leave Policies on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickCreateLeavePolicies() {
		return objGenHelper.elementClick(leavesMenuCreateLeavePolicies, "Create Leave Policies link");
	}

	/**
	 * This method clicks on Unpaid Leaves on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickUnpaidLeaves() {
		return objGenHelper.elementClick(leavesMenuUnpaidLeaves, "Unpaid Leaves link");
	}

	/**
	 * This method clicks on Holidays on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickHolidays() {
		return objGenHelper.elementClick(leavesMenuHolidays, "Holidays link");
	}

	/**
	 * This method clicks on Settings on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickSettings() {
		return objGenHelper.elementClick(leavesMenuSettings, "Settings link");
	}

	/**
	 * This method clicks on Compensatory Off on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickCompensatoryOff() {
		return objGenHelper.elementClick(leavesMenuCompoff, "Compensatory Off link");
	}

	/**
	 * This method select Restriction Condition Leaves Or And
	 * 
	 * @return
	 */
	public boolean selectRestrictionConditionLeavesOrAnd(String text) {
		String locator = "//*[@id='Leaves_or_and']/../label[contains(text(),'" + text + "')]/../input";
		WebElement element = driver.findElement(By.xpath(locator));
		return objGenHelper.elementClick(element, "Restriction Condition Leaves radio button");
	}

//	/**
//	 * This method select Restriction (Department, Employee Type or Location) from
//	 * list box
//	 * 
//	 * @param textToSelect
//	 * @return
//	 */
//	public boolean selectLeaveRestrictionFromListBox(String textToSelect) {
//		return chooseTextFromListBox(chooseRestrictionFromListBoxLocator, textToSelect, "Restriction");
//	}
//
//	/**
//	 * This method select week days from list box
//	 * 
//	 * @param textToSelect
//	 * @return
//	 */
//	public boolean selectWeekDaysLeaveRestrictionFromListBox(String textToSelect) {
//		try {
//			return chooseTextFromListBox(restrictLeaveToWeekDaysListBoxLocator, textToSelect, "Restrict to Week Days");
//		} catch (Exception e) {
//			return false;
//		}
//	}

	/**
	 * This method clicks on Create Leave Policy Save button
	 * 
	 * @return
	 */
	public boolean clickCreateLeavePolicySaveButton() {
		return objGenHelper.elementClick(createLeavePolicySaveButton, "Create Leave Policy Save button");
	}

	/**
	 * This method selects text from Group Company Drop down
	 * 
	 * @return
	 */
	public boolean selectGroupCompanyDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue(groupCompanyDropdown, text, "Compensatory Off lik");
	}

	/**
	 * This method insert text in Description text box
	 * 
	 * @param text
	 * @return
	 */
	public boolean insertDescription(String text) {
		return objGenHelper.setElementText(descriptionTextBox, text, "Description");
	}

	/**
	 * This method insert text in Maximum Leaves Allowed Per Year text box
	 * @param text
	 * @return
	 */
	public boolean insertMaximumLeavesAllowedPerYear(String text) {
		return objGenHelper.setElementText(maximumLeavesAllowedPerYearTextBox, text, "Maximum leaves allowed per year");
	}

	/**
	 * This method insert text in Maximum Leaves Allowed Per Year text box
	 * @param text
	 * @return
	 */

	public boolean insertConsecutiveLeavesAllowedPerMonthTextBox(String text) {
		
		driver.switchTo().defaultContent();
		objGenHelper.elementClick(consecutiveLeavesAllowedPerMonthTextBox, "Maximum leaves allowed per year");
		return objGenHelper.setElementText(consecutiveLeavesAllowedPerMonthTextBox, text, "Maximum leaves allowed per year");
	}

	/**
	 * This method select text from Leave Cycle Drop down
	 * @param text
	 * @return
	 */
	public boolean selectLeaveCycleDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue(leaveCycleDropdown, text, "Leave Cycle");
	}

	/**
	 * This method enables or disable Push Leave Request To Admin Checkbox
	 * @param text
	 * @return
	 */
	public boolean pushLeaveRequestToAdminCheckbox(String text) {
		return objGenHelper.toggleElementStatus(pushLeaveRequestToAdminCheckbox, text, "Push all these leave requests to admin");
	}

	/**
	 * This method insert text in Maximum Leaves Allowed Per Year text box
	 * @param text
	 * @return
	 */
	public boolean insertAutoApproveLeaveRequestDays(String text) {
		return objGenHelper.setElementText(autoApproveLeaveRequestTextBox, text, "Auto Approve Leave Request Days");
	}

	/**
	 * This method insert text in Leave Type text box
	 * @param text
	 * @return
	 */
	public boolean insertLeaveType(String text) {
		return objGenHelper.setElementText(leaveTypeTextBox, text, "Leave Type");
	}
	
	/**
	 * This method select text from Gender applicability Drop down
	 * @param text
	 * @return
	 */
	public boolean selectGenderApplicabilityDropdown(String text) {
		return objDropDownHelper.selectUsingVisibleValue(genderApplicabilityDropdown, text, "Gender applicability");
	}
	
	/**
	 * This method insert text in Minimum Notice Period (days)text box
	 * @param text
	 * @return
	 */
	public boolean insertMinimumNoticePeriodDays(String text) {
		return objGenHelper.setElementText(minimumNoticePeriodTextBox, text, "Minimum Notice Period (days)");
	}
	
	/**
	 * This method insert text in Probation period before leave validity (months) text box
	 * @param text
	 * @return
	 */
	public boolean insertProbationPeriodBeforeLeaveValidityMonths(String text) {
		return objGenHelper.setElementText(probationPeriodBeforeLeaveValidityTextBox, text, "Probation period before leave validity (months)");
	}
	
	/**
	 * This method insert text in Maximum leaves allowed per month text box
	 * @param text
	 * @return
	 */
	public boolean insertMaximumLeavesAllowedPerMonth(String text) {
		return objGenHelper.setElementText(maximumLeavesAllowedPerMonthTextBox, text, "Maximum leaves allowed per month");
	}
	
	/**
	 * This method enables or disable Don't Allow these leaves in notice period Checkbox
	 * @param text
	 * @return
	 */
	public boolean dontAllowLeavesInNoticePeriodCheckbox(String text) {
		return objGenHelper.toggleElementStatus(dontAllowLeavesInNoticePeriodCheckBox, text, "Don't Allow these leaves in notice period");
	}
	
	/**
	 * This method insert text in Maximum Number of Leaves which can be accrued text box
	 * @param text
	 * @return
	 */
	public boolean insertMaximumNumberOfLeavesWhichCanBeAccrued(String text) {
		return objGenHelper.setElementText(maximumNumberofLeavesAccruedTextBox, text, "Maximum Number of Leaves which can be accrued");
	}
	
	/**
	 * This method insert text in Minimum Consecutive leaves text box
	 * @param text
	 * @return
	 */
	public boolean insertMinimumConsecutiveLeaves(String text) {
		return objGenHelper.setElementText(minimumConsecutiveLeavesTextBox, text, "Minimum Consecutive leaves");
	}
	
	/**
	 * This method enables or disable Push Is this a Special Leave Checkbox
	 * @param text
	 * @return
	 */
	public boolean isthisSpecialLeaveCheckBox(String text) {
		return objGenHelper.toggleElementStatus(specialLeaveCheckBox, text, "Is this a Special Leave");
	}

}
