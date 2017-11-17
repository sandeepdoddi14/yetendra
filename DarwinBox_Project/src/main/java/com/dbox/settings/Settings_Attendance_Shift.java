package com.dbox.settings;

import java.util.List;
import java.util.ListIterator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Settings_Attendance_Shift extends Settings {

	WebDriver driver;
	public Settings_Attendance_Shift(WebDriver driver) {
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	static String ShiftName;

	/*
	 * Attendance Object Repository
	 */
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
	 * This method click on Shifts link
	 * 
	 * @author shikhar
	 */
	public boolean clickShifts() {
		return click(attendanceShifts, "Shifts");
	}

	/**
	 * This method click on Policies link
	 * 
	 * @author shikhar
	 */
	public boolean clickPolicies() {
		return click(attendancePolicy, "Policies");
	}

	/**
	 * This method click on IP Restrictions link
	 * 
	 * @author shikhar
	 */
	public boolean clickIPRestrictions() {
		return click(attendanceIPRestrictions, "IP Restrictions");
	}

	/**
	 * This method click on Check IN link
	 * 
	 * @author shikhar
	 */
	public boolean clickCheckIn() {
		return click(attendanceCheckIn, "Check IN");
	}

	/**
	 * This method click on Create Shift button
	 * 
	 * @author shikhar
	 */
	public boolean clickCreateShiftBtn() {
		return click(createShiftButton, "Create Shift button");
	}

	/**
	 * This method click on Shift button
	 * 
	 * @author shikhar
	 */
	public boolean clickSaveShiftBtn() {
		return click(saveShiftButton, "Save button");
	}

	/**
	 * This method clicks on Next Day check box
	 * 
	 * @author shikhar
	 */
	public boolean clickNextDayBtn() {
		return click(nextDayCheckBox, "Next Day check box");
	}

	/**
	 * This method insert details in Shift form
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean insertAddShiftsDetails() throws Exception {
		try {
			ShiftName = excelInput("Shift Name");
			insertText(shiftNameTextBox, "Shift Name", ShiftName);
			insertText(shiftDescriptionTextBox, "Shift Description", excelInput("Shift Description"));
			selectTextFromDropDownByIndex(groupCompanyDropBox, 1, "Group Company");
			selectVisibleTextFromDropDown(beginTimeHourDropBox, excelInput("Start Time hr"), "Start Time hr");
			selectVisibleTextFromDropDown(beginTimeMinDropBox, excelInput("Start Time min"), "Start Time min");
			selectVisibleTextFromDropDown(endTimeHourDropBox, excelInput("End Time hr"), "End Time hr");
			selectVisibleTextFromDropDown(endTimeMinDropBox, excelInput("End Time min"), "End Time min");
			return true;
		} catch (RuntimeException e) {
			Reporter("Exception while inserting shift details: " + e.getLocalizedMessage(), "Fail");
			return false;
		}
	}

	/**
	 * This method creates a valid Shift with unique Shift Name by taking Test Data
	 * from Settings excel
	 * 
	 * @param takes
	 *            parameters from excel
	 * @return boolean
	 * @author shikhar
	 */
	public boolean createShift() {
		try {
			insertAddShiftsDetails();
			click(saveShiftButton, "Save button");
			checkIfDuplicateShiftName();
			return true;
		} catch (Exception e) {
			Reporter("Exception while creating valid shift: " + e.getMessage(), "Fail");
			return false;
		} finally {
			waitForPageToLoad();
		}
	}
/**
 * This method creates a random shift if Shift Name is duplicate
 * @return
 */
	public boolean checkIfDuplicateShiftName() {
		try {
			if (checkVisbilityOfElement(shiftErrorMsg, "Error msg ") == true) {
				if (shiftErrorMsg.getText().contains(ShiftName)) {
					Reporter(shiftErrorMsg.getText(), "Pass");
					ShiftName = excelInput("Shift Name") + getRandomNumber();
					insertText(shiftNameTextBox, "Shift Name", ShiftName);
					click(saveShiftButton, "Save button");
					return true;
				}
			} else {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method verifies whether Error message specified is displayed
	 * 
	 * @author shikhar
	 * @param takes
	 *            parameter from excel
	 * @return boolean
	 */
	public boolean verifyShiftBeginTimeGreaterThanEndTimeErrorMsg() {
		try {
			if (checkVisbilityOfElement(shiftErrorMsg, "Error msg") == true) {
				WebDriverWait wait = new WebDriverWait(driver, 20);				
				wait.until(ExpectedConditions.textToBePresentInElement(shiftErrorMsg,
						excelInput("ErrorMsgBeginTimeGreaterThanEndTime").trim()));
				String errorMsg = shiftErrorMsg.getText().trim();
				if (errorMsg.contains(excelInput("ErrorMsgBeginTimeGreaterThanEndTime").trim()) == true) {
					Reporter(shiftErrorMsg.getText() + " is displayed correctly", "Pass");
					return true;
				} else {
					System.out.println(excelInput("ErrorMsgBeginTimeGreaterThanEndTime"));
					Reporter("Unexpected error " + shiftErrorMsg.getText() + " is displayed", "Fail");
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			Reporter("Exception while verifying Shift error message " + e.getMessage(), "Fail");
			return false;
		}
	}

	public boolean searchShiftName() {
		// waitElementToBeClickable(driver, searchFilterTextBox);
		waitForPageToLoad();
		// searchFilterTextBox.sendKeys(ShiftName);
		return insertText(shiftSearchFilterTextBox, "Search: ", ShiftName);
	}

	/**
	 * This method verifies shift details
	 * 
	 * @return
	 */
	public boolean checkShiftDetails() {
		try {
			if (getDetailsFromShiftTable(1, "Shift Name").contentEquals(ShiftName)) {
				System.out.println(getDetailsFromShiftTable(1, "Shift Name"));
				Reporter("Validated shift details on page sucessfully", "Pass");
				return true;
			}
			return false;
		} catch (Exception e) {
			Reporter("Shift Name not matching", "Fail");

			return false;
		}
	}

	/**
	 * This method deletes the Shift created
	 * 
	 * @return
	 * @throws Exception
	 */

	public boolean deleteShift() throws Exception {
		try {
			String text = "//*[@id='shift_table']/tbody//td[text() = '" + ShiftName + "']/following::a[2]";
			WebElement ele = driver.findElement(By.xpath(text));
			click(ele, "Delete shift button");
			acceptAlert();
			Reporter("Record deleted successfully", "Pass");
			return true;
		} catch (Exception e) {
			Reporter("Exception while deleting Shift", "Pass");
			return false;
		}

	}

	/**
	 * This method edit the Shift details already created
	 * 
	 * @return
	 * @throws Exception
	 */

	public boolean editShiftDetails() throws Exception {
		try {
			String text = "//*[@id='shift_table']/tbody//td[text() = '" + ShiftName + "']/following::a[1]";
			WebElement ele = driver.findElement(By.xpath(text));
			waitElementToBeClickable(ele);			
			click(ele, "Edit shift button");
			waitElementToBeClickable(shiftNameTextBox);
			insertText(shiftNameTextBox, "Shift Name", ShiftName + getRandomNumber());
			insertText(shiftDescriptionTextBox, "Shift Description",
					excelInput("Shift Description") + getRandomNumber());
			selectTextFromDropDownByIndex(groupCompanyDropBox, 2, "Group Company");
			selectVisibleTextFromDropDown(beginTimeHourDropBox, excelInput("Start Time hr"), "Start Time hr");
			selectVisibleTextFromDropDown(beginTimeMinDropBox, excelInput("Start Time min"), "Start Time min");
			selectVisibleTextFromDropDown(endTimeHourDropBox, excelInput("End Time hr"), "End Time hr");
			selectVisibleTextFromDropDown(endTimeMinDropBox, excelInput("End Time min"), "End Time min");
			checkIfDuplicateShiftName();
			Reporter("Record edited successfully", "Pass");
			return true;
		} catch (Exception e) {
			Reporter("Exception while editing Shift", "Fail");
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
			if (colName.equalsIgnoreCase("Shift name")) {
				colNo = 1;
			}
			switch (colName.toLowerCase().trim()) {
			case ("Shift name"):
				colNo = 1;
			case ("Shift Timings"):
				colNo = 2;
			case ("Parent Company"):
				colNo = 3;
			case ("Actions"):
				colNo = 4;
			}

			String text = "//*[@id='shift_table']/tbody/tr[" + rowNo + "]/td[" + colNo + "]";
			return driver.findElement(By.xpath(text)).getText();
		} catch (Exception e) {
			Reporter("Exception while get Details From Shift Table" + e.getMessage(), "Fail");
			throw new RuntimeException("Exception while get Details From Shift Table" + e.getMessage());
		}
	}

	public boolean shiftErrorMsgList() {
		try {
	//		waitForPageToLoad();
			List text = driver.findElements(By.xpath("//div[@id='input-error']"));
			System.out.println(text.size());
			
			ListIterator<WebElement> itr=text.listIterator(); 
			
			while(itr.hasNext()) {
				System.out.println(itr.next().getText());
			}
			//System.out.println(shiftErrorMsgList.getAttribute("innerText")+ "  55555555");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
