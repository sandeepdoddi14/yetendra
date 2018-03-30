package com.darwinbox.test.hrms.uiautomation.Leaves.Action;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CreateAndManageLeavePoliciesPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.LeavesSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelWriter;
import com.darwinbox.test.hrms.uiautomation.Utility.UtilityHelper;
import com.darwinbox.test.hrms.uiautomation.configreader.ObjectRepo;
import com.darwinbox.test.hrms.uiautomation.helper.Action.ActionHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class LeavesActionClass extends TestBase {

	WaitHelper objWait;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	WebDriver driver;
	DateTimeHelper objDateTimeHelper;
	UtilityHelper objUtil;
	CreateAndManageLeavePoliciesPage createManageLeaves;
	LeavesSettingsPage leaveSettings;
	ActionHelper objActionHelper;
	BrowserHelper objBrowserHelper;
	ExcelWriter objExcelWriter;

	public static final Logger log = Logger.getLogger(LeavesActionClass.class);

	public LeavesActionClass(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
		objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
		objDateTimeHelper = PageFactory.initElements(driver, DateTimeHelper.class);
		objUtil = PageFactory.initElements(driver, UtilityHelper.class);
		createManageLeaves = PageFactory.initElements(driver, CreateAndManageLeavePoliciesPage.class);
		objJavaScrHelper = PageFactory.initElements(driver, JavaScriptHelper.class);
		leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
		objActionHelper = PageFactory.initElements(driver, ActionHelper.class);
		objBrowserHelper = PageFactory.initElements(driver, BrowserHelper.class);
		objExcelWriter = PageFactory.initElements(driver, ExcelWriter.class);

	}

	public static String EMPID;
	double ExpectedLeaveBalance;
	double ActualLeaveBalance;
	Date leaveCycleFirstDay = null;
	String DateOfJoining = "";
	public static String Leave_Type = "";
	public static String Max_Leaves_Allowed_Per_Year;
	public static double Leaves_Allowed_Per_Year;
	public static String Leave_Cycle;
	public static String Pro_rata;
	public static String Calculate_from_joining_date;
	public static String Calculate_after_probation_period;
	public static String Half_Month_Leaves_if_employee_joins_after_15th;
	public static String Full_Month_Leaves_if_employee_joins_after_15th;
	public static String Accrual;
	public static String Monthly;
	public static String Quarterly;
	public static String Biannually;
	public static String Begin_of_monthORQuarter;
	public static String End_of_monthORQuarter;
	public static String Leave_Probation_period_Custom_Months;
	public static String Probation_period_before_leave_validity_months;
	public static String Leave_Probation_period_employee_probation_period;
	public static String Employee_probation_period;
	public static String Leave_Probation_End_Date;
	public static String LeaveCalBeginningDate;
	public static StringBuffer LeaveScenario;
	public static String CreditOnTenureBasis;
	public static String CreditFromYear;
	public static String CreditToYear;
	public static String CreditNoOfLeaves;
	// public static String WriteResultToExcel = "No";

	/**
	 * This method set Leave Scenario parameters from property file
	 */
	public boolean setLeaveScenarioFromPropertyFile() {
		try {

			Leave_Probation_period_Custom_Months = UtilityHelper.getProperty("leavesCalculation",
					"Probation.period.Custom.Months");
			Probation_period_before_leave_validity_months = UtilityHelper.getProperty("leavesCalculation",
					"Probation.period.before.leave.validity.months");
			Leave_Probation_period_employee_probation_period = UtilityHelper.getProperty("leavesCalculation",
					"Probation.period.employee.probation.period");
			Employee_probation_period = UtilityHelper.getProperty("leavesCalculation", "Employees.Probation.Days");

			Leave_Type = UtilityHelper.getProperty("leavesCalculation", "Leave.Type");
			Max_Leaves_Allowed_Per_Year = UtilityHelper.getProperty("leavesCalculation", "Max.Leaves.Allowed.Per.Year");
			Leaves_Allowed_Per_Year = Double.parseDouble(Max_Leaves_Allowed_Per_Year);
			Leave_Cycle = UtilityHelper.getProperty("leavesCalculation", "Leave.Cycle");
			Pro_rata = UtilityHelper.getProperty("leavesCalculation", "Pro.rata");
			Calculate_from_joining_date = UtilityHelper.getProperty("leavesCalculation", "From.Joining.date");
			Calculate_after_probation_period = UtilityHelper.getProperty("leavesCalculation", "After.Probation.period");
			Half_Month_Leaves_if_employee_joins_after_15th = UtilityHelper.getProperty("leavesCalculation",
					"Half.Month.Leaves.if.employee.joins.after.15th");
			Full_Month_Leaves_if_employee_joins_after_15th = UtilityHelper.getProperty("leavesCalculation",
					"Full.Month.Leaves.if.employee.joins.after.15th");
			Accrual = UtilityHelper.getProperty("leavesCalculation", "Accrual");
			Monthly = UtilityHelper.getProperty("leavesCalculation", "Monthly");
			Quarterly = UtilityHelper.getProperty("leavesCalculation", "Quarterly");
			Biannually = UtilityHelper.getProperty("leavesCalculation", "Biannually");
			Begin_of_monthORQuarter = UtilityHelper.getProperty("leavesCalculation", "Begin.of.monthORQuarter");
			End_of_monthORQuarter = UtilityHelper.getProperty("leavesCalculation", "End.of.monthORQuarter");
			CreditOnTenureBasis = UtilityHelper.getProperty("leavesCalculation", "Credit.on.Tenure.basis");
			CreditFromYear = UtilityHelper.getProperty("leavesCalculation", "Credit.From.Year");
			CreditToYear = UtilityHelper.getProperty("leavesCalculation", "Credit.To.Year");
			CreditNoOfLeaves = UtilityHelper.getProperty("leavesCalculation", "NO.OF.LEAVES");

			// WriteResultToExcel = objUtil.getProperty("leavesCalculation",
			// "Write.Result.to.excel");

			displayLeaveScenarioToReport();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while setting Leave Calculation Scenario", "Error");
			return false;
		}
	}

	/**
	 * This method set Leave Scenario parameters from excel file
	 */
	public boolean setLeaveScenarioFromExcelFile() {
		try {

			Leave_Probation_period_Custom_Months = getData("Leave Probation Period according to Custom Months");
			Probation_period_before_leave_validity_months = getData("Probation period before leave validity months");
			Leave_Probation_period_employee_probation_period = getData(
					"Leave Probation Period according to Employee Probation Period");
			Employee_probation_period = getData("Employee Probation Period");
//			Leave_Type = getData("Leave_Type");			
			Max_Leaves_Allowed_Per_Year = getData("Max_Leaves_Allowed_Per_Year");
			Leaves_Allowed_Per_Year = Double.parseDouble(Max_Leaves_Allowed_Per_Year);
			Leave_Cycle = getData("Leave Cycle");
			Pro_rata = getData("Pro rata");
			Calculate_from_joining_date = getData("From Joining date");
			Calculate_after_probation_period = getData("After Probation period");
			Half_Month_Leaves_if_employee_joins_after_15th = getData("Half Month Leaves if employee joins after 15th");
			Full_Month_Leaves_if_employee_joins_after_15th = getData("Full Month Leaves if employee joins after 15th");
			Accrual = getData("Accrual");
			Monthly = getData("Monthly");
			Quarterly = getData("Quarterly");
			Biannually = getData("Biannually");
			Begin_of_monthORQuarter = getData("Begin of month/Quarter");
			End_of_monthORQuarter = getData("End of month/Quarter");
			CreditOnTenureBasis = getData("Credit On Tenure Basis");
			CreditFromYear = getData("Credit From Year");
			CreditToYear = getData("Credit To Year");
			CreditNoOfLeaves = getData("Credit No of Leaves");

			displayLeaveScenarioToReport();
			return true;
		} catch (Exception e) {
			Reporter("Exception while setting Leave Calculation Scenario", "Error");
			return false;
		}
	}

	/**
	 * This method displays leave scenario to reports
	 */
	public void displayLeaveScenarioToReport() {
		try {
			LeaveScenario = new StringBuffer("Leave Scenario is--\nLeave Cycle: " + Leave_Cycle);

			if (Leave_Probation_period_Custom_Months.equalsIgnoreCase("Yes")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("No")) {
				LeaveScenario.append(
						",\nLeave Probation Period: Custom Months->" + Probation_period_before_leave_validity_months);
			} else if (Leave_Probation_period_Custom_Months.equalsIgnoreCase("No")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("Yes")) {
				LeaveScenario.append(",\nLeave Probation Period: According to Employee Probation period which is "
						+ Employee_probation_period + " days");
			} else {
				LeaveScenario.append(
						",\nLeave Probation Period: Custom Months->" + Probation_period_before_leave_validity_months);
			}

			LeaveScenario.append(",\nPro Rata: " + Pro_rata);
			if (Pro_rata.equalsIgnoreCase("Yes")) {
				LeaveScenario.append(",\nProbation Status: ");
				if (Calculate_from_joining_date.equalsIgnoreCase("Yes")
						&& Calculate_after_probation_period.equalsIgnoreCase("No")) {
					LeaveScenario.append("Start calculating leaves from joining date");
				} else if (Calculate_from_joining_date.equalsIgnoreCase("No")
						&& Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
					LeaveScenario.append("Start calculating leaves after probation period");
				}

				if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					LeaveScenario.append(", Mid Joining Leaves: Half");
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					LeaveScenario.append(", Mid Joining Leaves: Full");
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
					LeaveScenario.append(", Mid Joining Leaves: Half and Full both selected");
				} else {
					LeaveScenario.append(", Mid Joining Leaves: Not allowed");
				}
			}

			LeaveScenario.append(",\nAccrual: " + Accrual);
			if (Accrual.equalsIgnoreCase("Yes")) {
				LeaveScenario.append(",\nAccrual time frame: ");
				if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
						&& Biannually.equalsIgnoreCase("No")) {
					LeaveScenario.append("Month");
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						LeaveScenario.append(",\n Accrual point: Begin of month");
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						LeaveScenario.append(", Accrual point: End of month");
					} else {
						LeaveScenario.append(", Accrual point: Begin of month");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
						&& Biannually.equalsIgnoreCase("No")) {
					LeaveScenario.append("Quarter");
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						LeaveScenario.append(", Accrual point: Begin of Quarter");
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						LeaveScenario.append(", Accrual point: End of Quarter");
					} else {
						LeaveScenario.append(", Accrual point: Begin of Quarter");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
						&& Biannually.equalsIgnoreCase("Yes")) {
					LeaveScenario.append("Biannual");
				} else {
					Reporter("Accuarl Time frame selected is not proper.", "Fail");
					throw new RuntimeException();
				}
			}
			if (CreditOnTenureBasis != null) {
				if (CreditOnTenureBasis.equalsIgnoreCase("Yes") && !CreditOnTenureBasis.isEmpty()) {
					LeaveScenario.append(",\nCredit On TenureBasis: " + CreditOnTenureBasis);
					LeaveScenario.append(", Credit From Year: " + CreditFromYear);
					LeaveScenario.append(", Credit To Year: " + CreditToYear);
					LeaveScenario.append(", Credit No. Of Leaves: " + CreditNoOfLeaves);
				}
			}
			ReporterForCodeBlock(LeaveScenario.toString(), "Info");
			// writeInLeaveScenarioToExcel("Leave_Sce_Des", LeaveScenario.toString(),
			// objDateTimeHelper.getCurrentLocalDateTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeInLeaveScenarioToExcel(String sheetName, String LeaveScenario, String CurrentDateTime) {

		String[] dataToWrite = new String[3];
		dataToWrite[0] = LeaveScenario;
		dataToWrite[1] = CurrentDateTime;

		try {
			ExcelWriter.writeToExcel("ExportExcel.xlsx", sheetName, dataToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

	/**
	 * This method deletes mentioned Leave Type if it is already present
	 * 
	 * @return boolean
	 */
	public boolean deleteLeaveTypeIfAlreadyPresent() {
		try {
			leaveSettings.clickManageLeavePolicies();
			String leaveTypeNameXpath = "//*[contains(@id,'leaveContainerModal')][text()='" + Leave_Type + "']";
			List<WebElement> leaveTypeNameList = driver.findElements(By.xpath(leaveTypeNameXpath));
			String leaveTypeNameDeleteButtonXpath = "//*[contains(@id,'leaveContainerModal')][contains(text(),'"
					+ Leave_Type + "')]/following::a[contains(@id, 'delete-leave')]";
			List<WebElement> leaveTypeNameDeleteButtonList = driver
					.findElements(By.xpath(leaveTypeNameDeleteButtonXpath));

			int i = leaveTypeNameDeleteButtonList.size();

			if (i > 0) {
				leaveTypeNameDeleteButtonList.get(0).click();
				objAlertHelper.acceptAlert();
				objBrowserHelper.refresh();
				Leave_Type = Leave_Type + objGenHelper.getRandomNumber();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while deleting already present same leave type", "Error");
			return false;
		}
	}

	/**
	 * This method deletes mentioned Leave Type if it is already present
	 * 
	 * @return boolean
	 */
	public boolean getLeaveTypeIdAndWriteToExcel(String sheetName) {
		try {
			String leaveTypeID = "";
			leaveSettings.clickManageLeavePolicies();
			String leaveTypeNameXpath = "//*[contains(@id,'leaveContainerModal')][text()='" + Leave_Type + "']";
			List<WebElement> leaveTypeNameList = driver.findElements(By.xpath(leaveTypeNameXpath));

			int i = leaveTypeNameList.size();

			if (i == 1) {
				leaveTypeID = leaveTypeNameList.get(0).getAttribute("id");

				String leaveTypeShortId = leaveTypeID.substring(leaveTypeID.indexOf("-") + 1);
				writeInLeaveTypeRepository(sheetName, LeaveScenario.toString(), Leave_Type, leaveTypeShortId,
						objDateTimeHelper.getCurrentLocalDateTime());
				objBrowserHelper.refresh();
			} else {
				Reporter("Duplicate Leave Type Name or Leave not present: Leave_Type-->" + Leave_Type, "Fail");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while deleting already present same leave type", "Error");
			return false;
		}
	}

	public void writeInLeaveTypeRepository(String sheetName, String LeaveDescription, String LeaveTypeName,
			String LeaveTypeID, String CurrentDateTime) {

		String[] dataToWrite = new String[6];
		dataToWrite[0] = LeaveDescription;
		dataToWrite[1] = LeaveTypeName;
		dataToWrite[2] = LeaveTypeID;
		dataToWrite[3] = CurrentDateTime;

		try {
			ExcelWriter.writeToExcel("Leave_Type_Repository.xlsx", sheetName, dataToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

	public static int sequenceNo = 0;

	public boolean leaveTypeSequenceGenerator() {
		try {
			leaveSettings.clickManageLeavePolicies();
			String leaveTypeNameXpath = "//*[contains(@id,'leaveContainerModal')][text()='" + Leave_Type + "']";
			List<WebElement> leaveTypeNameList = driver.findElements(By.xpath(leaveTypeNameXpath));
			int i = leaveTypeNameList.size();
			if (i > 0) {
				Leave_Type = Leave_Type + sequenceNo++;
				objBrowserHelper.refresh();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while genearting sequencial no. for Leave Type", "Error");
			return false;
		}
	}

	public boolean insertMaxLeaveAllowedPerYear() {
		return createManageLeaves.insertMaximumLeavesAllowedPerYear(Max_Leaves_Allowed_Per_Year);
	}

	public boolean insertLeaveType() {
		return createManageLeaves.insertLeaveType(Leave_Type);
	}

	public boolean setLeaveProbationPeriod() {
		try {
			if (Leave_Probation_period_Custom_Months.equalsIgnoreCase("Yes")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("No")) {
				createManageLeaves.clickCustomMonthsRadioButton();
				createManageLeaves
						.insertProbationPeriodBeforeLeaveValidityMonths(Probation_period_before_leave_validity_months);
			} else if (Leave_Probation_period_Custom_Months.equalsIgnoreCase("No")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickAccordingToEmployeeProbationPeriodRadioButton();
			} else {
				Reporter(
						"By default 'Probation period before leave validity' is selected as 'Custom Months' with Probation period as '0'",
						"Pass");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while setting Leave Probation Period", "Error");
			return false;
		}
	}

	public boolean setLeaveCycle() {
		return createManageLeaves.selectLeaveCycleDropdown(Leave_Cycle);
	}

	public boolean setCreditOnProRataBasis() {
		try {

			objJavaScrHelper.scrollToElement(driver,
					createManageLeaves.getWebElementDontShowApplyInProbationPeriodCheckbox(),
					"Dont show & apply in Probation Period Checkbox");

			if (Pro_rata.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickCreditOnProRataBasisAccordion();
				createManageLeaves.clickCreditOnProRataBasisYesRadioButton();
				if (Calculate_from_joining_date.equalsIgnoreCase("Yes")
						&& Calculate_after_probation_period.equalsIgnoreCase("No")) {
					createManageLeaves.clickCalculateFromJoiningDateRadioButton();
				} else if (Calculate_from_joining_date.equalsIgnoreCase("No")
						&& Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
					createManageLeaves.clickCalculateAfterProbationPeriodRadioButton();
				}

				if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					// createManageLeaves.clickHalfMidJoiningLeavesRadioButton();
					createManageLeaves.clickHalfMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					// createManageLeaves.clickFullMidJoiningLeavesRadioButton();
					createManageLeaves.clickFullMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
					// throw new RuntimeException("Set Mid Joining correctly");
					createManageLeaves.clickHalfMidJoiningLeavesCheckBox();
					createManageLeaves.clickFullMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					// throw new RuntimeException("Set Mid Joining correctly");
				}
				// createManageLeaves.clickCreditOnProRataBasisAccordion();
				// driver.switchTo().defaultContent();
			} else {
				Reporter("Pro Rata condition is set to No", "Paas");
			}
			return true;
		} catch (Exception e) {
			Reporter("Exception while setting Credit on Pro Rata Basis scenario", "Error");
			return false;
		}
	}

	/**
	 * This method set Credit on ProRata Basis Scenario
	 * 
	 * @return
	 */
	public boolean setCreditOnAccrualBasis() {
		try {
			if (Accrual.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickCreditOnAccrualBasisAccordion();
				createManageLeaves.clickCreditOnAccrualBasisYesRadioButton();
				if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
						&& Biannually.equalsIgnoreCase("No")) {
					createManageLeaves.clickAccrualTimeFrameMonthRadioButton();
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						createManageLeaves.clickAccrualPointBeginOfMonthRadioButton();
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						createManageLeaves.clickAccrualPointEndOfMonthRadioButton();
					} else {
						Reporter("By default Begin of Month Accrual Point is selected", "Pass");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
						&& Biannually.equalsIgnoreCase("No")) {

					createManageLeaves.clickAccrualTimeFrameQuarterRadioButton();
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						createManageLeaves.clickAccrualPointBeginOfQuarterRadioButton();
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						createManageLeaves.clickAccrualPointEndOfQuarterRadioButton();
					} else {
						Reporter("By default Begin of Quarter Accrual Point is selected", "Pass");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
						&& Biannually.equalsIgnoreCase("Yes")) {
					createManageLeaves.clickAccrualTimeFrameBiannualRadioButton();
				} else {
					Reporter("Accuarl Time frame selected is not proper.", "Fail");
					throw new RuntimeException();
				}
			} else {
				Reporter("Accrual is selected as No", "Pass");
			}
			// createManageLeaves.clickCreditOnAccrualBasisAccordion();
			// driver.switchTo().defaultContent();
			return true;

		} catch (Exception e) {
			Reporter("Exception while setting Credit on Accrual Basis scenario", "Error");
			return false;
		}
	}

	/**
	 * This method creates Leave with with scenarios mentioned
	 * 
	 * @return
	 */
	public boolean createLeaveTypeWithMentionedScenarios() {
		try {

			createManageLeaves.insertMaximumLeavesAllowedPerYear(Max_Leaves_Allowed_Per_Year);
			createManageLeaves.insertLeaveType(Leave_Type);

			if (Leave_Probation_period_Custom_Months.equalsIgnoreCase("Yes")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("No")) {
				createManageLeaves.clickCustomMonthsRadioButton();
				createManageLeaves
						.insertProbationPeriodBeforeLeaveValidityMonths(Probation_period_before_leave_validity_months);
			} else if (Leave_Probation_period_Custom_Months.equalsIgnoreCase("No")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickAccordingToEmployeeProbationPeriodRadioButton();
			} else {
				Reporter(
						"By default 'Probation period before leave validity' is selected as 'Custom Months' with Probation period as '0'",
						"Pass");
			}

			createManageLeaves.selectLeaveCycleDropdown(Leave_Cycle);

			objJavaScrHelper.scrollToElement(driver,
					createManageLeaves.getWebElementDontShowApplyInProbationPeriodCheckbox(),
					"Dont show & apply in Probation Period Checkbox");

			if (Pro_rata.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickCreditOnProRataBasisAccordion();
				createManageLeaves.clickCreditOnProRataBasisYesRadioButton();
				if (Calculate_from_joining_date.equalsIgnoreCase("Yes")
						&& Calculate_after_probation_period.equalsIgnoreCase("No")) {
					createManageLeaves.clickCalculateFromJoiningDateRadioButton();
				} else if (Calculate_from_joining_date.equalsIgnoreCase("No")
						&& Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
					createManageLeaves.clickCalculateAfterProbationPeriodRadioButton();
				}

				if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					// createManageLeaves.clickHalfMidJoiningLeavesRadioButton();
					createManageLeaves.clickHalfMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					// createManageLeaves.clickFullMidJoiningLeavesRadioButton();
					createManageLeaves.clickFullMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
					// throw new RuntimeException("Set Mid Joining correctly");
					createManageLeaves.clickHalfMidJoiningLeavesCheckBox();
					createManageLeaves.clickFullMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					// throw new RuntimeException("Set Mid Joining correctly");
				}
				// createManageLeaves.clickCreditOnProRataBasisAccordion();
				// driver.switchTo().defaultContent();
			} else {
				Reporter("Pro Rata condition is set to No", "Paas");
			}

			if (Accrual.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickCreditOnAccrualBasisAccordion();
				createManageLeaves.clickCreditOnAccrualBasisYesRadioButton();
				if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
						&& Biannually.equalsIgnoreCase("No")) {
					createManageLeaves.clickAccrualTimeFrameMonthRadioButton();
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						createManageLeaves.clickAccrualPointBeginOfMonthRadioButton();
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						createManageLeaves.clickAccrualPointEndOfMonthRadioButton();
					} else {
						Reporter("By default Begin of Month Accrual Point is selected", "Pass");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
						&& Biannually.equalsIgnoreCase("No")) {

					createManageLeaves.clickAccrualTimeFrameQuarterRadioButton();
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						createManageLeaves.clickAccrualPointBeginOfQuarterRadioButton();
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						createManageLeaves.clickAccrualPointEndOfQuarterRadioButton();
					} else {
						Reporter("By default Begin of Quarter Accrual Point is selected", "Pass");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
						&& Biannually.equalsIgnoreCase("Yes")) {
					createManageLeaves.clickAccrualTimeFrameBiannualRadioButton();
				} else {
					Reporter("Accuarl Time frame selected is not proper.", "Fail");
					throw new RuntimeException();
				}
			} else {
				Reporter("Accrual is selected as No", "Pass");
			}
			// createManageLeaves.clickCreditOnAccrualBasisAccordion();
			// driver.switchTo().defaultContent();
			return true;
		} catch (Exception e) {
			Reporter("Exception while creating leave type with desiered Scenarios", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method set Credit on Tenure Based
	 */
	public boolean setCreditOnTenureBasisLeaveScenario() {
		try {
			List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
			List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
			List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

			Iterator<String> creditFromYearListitr = creditFromYearList.iterator();
			Iterator<String> creditToYearListitr = creditToYearList.iterator();
			Iterator<String> creditNoOfLeavesListitr = creditNoOfLeavesList.iterator();
			if (CreditOnTenureBasis.equalsIgnoreCase("Yes") && (!CreditOnTenureBasis.isEmpty())) {

				createManageLeaves.clickCreditOnTenureBasisAccordion();
				createManageLeaves.clickCreditOnTenureBasisYesRadioButton();

				int flag = 1;
				while (flag == 1) {
					String CreditFromYearVar = creditFromYearListitr.next().toString();
					String CreditToYearVar = creditToYearListitr.next().toString();
					String CreditNoOfLeavesVar = creditNoOfLeavesListitr.next().toString();

					createManageLeaves.selectCreditOnTenureBasisFromYearFromDropdown(CreditFromYearVar);
					createManageLeaves.selectCreditOnTenureBasisToYearFromDropdown(CreditToYearVar);
					createManageLeaves.insertCreditOnTenureBasisNumberOfLeavesTextBox(CreditNoOfLeavesVar);

					if (creditFromYearListitr.hasNext() && creditToYearListitr.hasNext()
							&& creditNoOfLeavesListitr.hasNext()) {
						createManageLeaves.clickCreditOnTenureBasisAddNewIcon();
					} else {
						flag = 0;
					}
				}
			}
			// createManageLeaves.clickCreditOnTenureBasisAccordion();
			// driver.switchTo().defaultContent();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while setting tenure leave scebario", "Error");
			return false;
		}
	}

	/**
	 * This method gets employees leave balance shown in front end
	 * 
	 * @param employee_Id
	 * @author shikhar
	 * @return Leave Balance
	 */
	public double getEmployeesFrontEndLeaveBalance(String leaveType) {
		try {
			String applicationURL = ObjectRepo.reader.getApplication();
			String URL = applicationURL + "emailtemplate/Employeeleave?id=" + EMPID + "&leave=" + leaveType;
			driver.navigate().to(URL);
			String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
			if (frontEndLeaveBalance.isEmpty()) {
				throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
			}
			double actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
			return actualLeaveBalance;
		} catch (Exception e) {
			Reporter("Exception while getting front end leave balance for the employee", "Fatal");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method returns first day of Leave Cycle provided
	 * 
	 * @param leaveCycle
	 * @return String leaveCycleStartDate
	 */
	public String getFirstDayofLeaveCycle(String leaveCycle) {
		try {
			String leaveCycleStartDate = "";
			int year = LocalDate.now().getYear();

			String calendarYearEndDate = year + "-" + "12" + "-" + "31";
			String financialYearEndDate = year + "-" + "03" + "-" + "31";
			LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
			LocalDate.parse(calendarYearEndDate);
			LocalDate today = LocalDate.now();
			int previousYear = year - 1;

			if (leaveCycle.equalsIgnoreCase("Financial Year")) {
				if (financialYearEndDateInDateFormat.isAfter(LocalDate.now())) {
					leaveCycleStartDate = previousYear + "-" + "04" + "-" + "01";
				} else {
					leaveCycleStartDate = today.getYear() + "-" + "04" + "-" + "01";
				}
			} else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
				leaveCycleStartDate = LocalDate.now().getYear() + "-" + "01" + "-" + "01";
			}
			return leaveCycleStartDate;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * This method returns last day of Leave Cycle
	 * 
	 * @param leaveCycle
	 * @return String leaveCycleEndDate
	 */
	public String getLastDayofLeaveCycle(String leaveCycle) {
		try {
			String leaveCycleEndDate = "";
			int year = LocalDate.now().getYear();

			String calendarYearEndDate = year + "-" + "12" + "-" + "31";
			String financialYearEndDate = year + "-" + "03" + "-" + "31";
			LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
			LocalDate.parse(calendarYearEndDate);
			LocalDate today = LocalDate.now();
			int nextYear = year + 1;

			if (leaveCycle.equalsIgnoreCase("Financial Year")) {
				if (financialYearEndDateInDateFormat.isAfter(LocalDate.now())) {
					leaveCycleEndDate = today.getYear() + "-" + "03" + "-" + "31";
				} else {
					leaveCycleEndDate = nextYear + "-" + "03" + "-" + "31";
				}
			} else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
				leaveCycleEndDate = LocalDate.now().getYear() + "-" + "12" + "-" + "31";
			}
			return leaveCycleEndDate;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * This method verifies leaves balance for whole leave cycle of the employee
	 * 
	 * @return boolean
	 */
	public boolean verifyEmployeeLeaveBalanceForWholeLeaveCycle() {
		try {
			String result = "";
			int flag = 0;
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
					+ " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
					+ "' till current date", "Info");
			LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
			int i = 1;
			LocalDate iterationDate = LocalDate.now();
			while (iterationDate.isAfter(leaveCycleStartDateInDateFormat)) {
				iterationDate = LocalDate.now().minusDays(i);
				DateOfJoining = changeEmployeeDOJ(iterationDate);

				double expectedBalance = calculateLeaveBalance(DateOfJoining);
				double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
				if (expectedBalance != actualBalance) {
					Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
							+ "||Actual Leave Balance=" + actualBalance, "Fail");
					result = "Fail";
					flag++;
				} else {
					Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
							+ "||Actual Leave Balance=" + actualBalance, "Pass");
					result = "Pass";
				}
				i++;
				if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
					writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
							LocalDateTime.now().toString());
				}
			}

			if (flag > 0) {
				return false;
			} else {
				return true;
			}

		} catch (

		Exception e) {
			Reporter("Exception while comparing leave balance", "Error");
			return false;
		}
	}

	public static String LastDayOfCalculation = "LocalDate";

	public void setLastDateOfCalculation() {

	}

	/**
	 * This method verifies leaves balance for whole leave cycle of the employee
	 * 
	 * @return boolean
	 */
	public boolean verifyEmployeeLeaveBalanceForWholeLeaveCycleForFourEdgeDays() {
		try {
			String result = "";
			int flag = 0;
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
					+ " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
					+ "' till current date", "Info");
			LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
			int i = 0;
			LocalDate iterationDate = LocalDate.now();
			while (iterationDate.isAfter(leaveCycleStartDateInDateFormat)) {
				iterationDate = LocalDate.now().minusDays(i);
				if (iterationDateFourTimesPerMonth(iterationDate) == true) {
					DateOfJoining = changeEmployeeDOJ(iterationDate);
					double expectedBalance = calculateLeaveBalance(DateOfJoining);
					double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
					if (expectedBalance != actualBalance) {
						Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
								+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
						result = "Fail";
						flag++;
					} else {
						Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
								+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
						result = "Pass";
					}

					if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
						writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
								LocalDateTime.now().toString());
					}
				}
				i++;
			}
			if (flag > 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			Reporter("Exception while comparing leave balance", "Error");
			return false;
		}
	}

	public boolean iterationDateFourTimesPerMonth(LocalDate iterationDate) {
		LocalDate todaysDate = LocalDate.now();
		LocalDate lastDayOfIterationMonth = iterationDate.withDayOfMonth(iterationDate.lengthOfMonth());
		LocalDate firstDayOfIterationMonth = iterationDate.with(firstDayOfMonth());
		LocalDate fifteenThOfIterationMonth = iterationDate.withDayOfMonth(15);
		LocalDate sixteenThOfIterationMonth = iterationDate.withDayOfMonth(16);
		if (iterationDate.equals(todaysDate) || iterationDate.equals(lastDayOfIterationMonth)
				|| iterationDate.equals(firstDayOfIterationMonth) || iterationDate.equals(fifteenThOfIterationMonth)
				|| iterationDate.equals(sixteenThOfIterationMonth)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method writes Leave calculation results to Excel
	 * 
	 * @param DateOfJoining
	 * @param expectedBalance
	 * @param actualBalance
	 * @param result
	 * @param dateTime
	 */
	public void writeLeavesResultToExcel(String DateOfJoining, double expectedBalance, double actualBalance,
			String result, String dateTime) {

		String[] dataToWrite = new String[6];
		dataToWrite[0] = LeaveScenario.toString();
		dataToWrite[1] = DateOfJoining;
		dataToWrite[2] = String.valueOf(expectedBalance);
		dataToWrite[3] = String.valueOf(actualBalance);
		dataToWrite[4] = result;
		dataToWrite[5] = dateTime;

		try {
			ExcelWriter.writeToExcel(TestBase.resultsDIR, "ExportExcel.xlsx", "Leave_Balance", dataToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

	/**
	 * This method writes Leave calculation results to Excel
	 * 
	 * @param DateOfJoining
	 * @param expectedBalance
	 * @param actualBalance
	 * @param result
	 * @param dateTime
	 */
	public void writeEffectiveDateResultToExcel(String DateOfJoining, double expectedBalance, double actualBalance,
			String result, String dateTime) {

		String[] dataToWrite = new String[7];
		dataToWrite[0] = LeaveScenario.toString();
		dataToWrite[1] = Leave_Type;
		dataToWrite[2] = DateOfJoining;
		dataToWrite[3] = String.valueOf(expectedBalance);
		dataToWrite[4] = String.valueOf(actualBalance);
		dataToWrite[5] = result;
		dataToWrite[6] = dateTime;

		try {
			ExcelWriter.writeToExcel(TestBase.resultsDIR, "ExportExcel.xlsx", "Effective Date", dataToWrite);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			Reporter("Exception while sending data to Excel", "Error");
		}
	}

	/**
	 * This method verifies leave balance of an employee for a particular DOJ
	 * 
	 * @param DOJ
	 * @author shikhar
	 * @return boolean
	 */
	public boolean verifyEmployeeLeaveBalanceForParticularDOJ(String DOJ) {
		try {
			int flag = 0;
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
					+ " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
					+ "' till current date", "Info");
			LocalDate iterationDate = LocalDate.parse(DOJ);
			DateOfJoining = changeEmployeeDOJ(iterationDate);
			double expectedBalance = calculateLeaveBalance(DateOfJoining);
			double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
			if (expectedBalance != actualBalance) {
				flag++;
				Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
						+ "||Actual Leave Balance=" + actualBalance, "Fail");
			} else {
				Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
						+ "||Actual Leave Balance=" + actualBalance, "Pass");
			}
			if (flag > 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			Reporter("Exception while verifying leave balance for particular DOJ", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method is used to set employee id to calculate leave balance
	 * 
	 * @param employeeID
	 * @return
	 */
	public boolean setEmployeeID(String employeeID) {
		try {
			EMPID = employeeID;
			Reporter("Employee is set to '" + EMPID + "' to calculate leave balance", "Pass");
			return true;
		} catch (Exception e) {
			Reporter("Exception while setting Employee id to calculate leave balance", "Error");
			return false;
		}
	}

	/**
	 * This method changes Employee Date of Joining
	 * 
	 * @param iterationDate
	 * @return DOJ as String
	 */
	public String changeEmployeeDOJ(LocalDate iterationDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String DOJ = iterationDate.format(formatter);
			String applicationURL = ObjectRepo.reader.getApplication();
			String URL = applicationURL + "emailtemplate/employeedoj?id=" + EMPID + "&date=" + DOJ;
			driver.navigate().to(URL);
			return DOJ;
		} catch (Exception e) {
			Reporter("Exception while changing employees DOJ", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method calculate leave balance
	 * 
	 * @param DOJ
	 * @param leaveType
	 * @return double
	 */
	public double calculateLeaveBalance(String DOJ) {
		try {
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
			double midJoinigYesLeaves = 0;
			double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
			double perMonthOrQuarterLeaves = 0;
			double MonthOrQuarterDifference = 0;
			double leavesDiffFromFirstDayOfQuarter = 0;
			String leavesCalculationStartDate = "";
			String midYearEndDate;
			double biannualLeave = 0;

			if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
				ExpectedLeaveBalance = 0;
			} else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {

				if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
						&& Calculate_after_probation_period.equalsIgnoreCase("No")) {
					LeaveCalBeginningDate = DOJ;
				} else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
						&& Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
					LeaveCalBeginningDate = Leave_Probation_End_Date;
				} else if (Pro_rata.equalsIgnoreCase("No")) {
					LeaveCalBeginningDate = DOJ;
				}

				if (Pro_rata.equalsIgnoreCase("Yes")) {
					if (Accrual.equalsIgnoreCase("Yes")) {
						leavesCalculationStartDate = LeaveCalBeginningDate;
					}
					if (Accrual.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = perMonthLeaves;
						MonthOrQuarterDifference = objDateTimeHelper
								.getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, leaveCycleEndDate);
						if (MonthOrQuarterDifference < 0) {
							MonthOrQuarterDifference = -MonthOrQuarterDifference;
						}
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					}
				} else if (Pro_rata.equalsIgnoreCase("No")) {
					leavesCalculationStartDate = leaveCycleStartDate;
					if (Accrual.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = perMonthLeaves;
						MonthOrQuarterDifference = 12;
					}
				}
				if (Pro_rata.equalsIgnoreCase("Yes")) {
					if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						midJoinigYesLeaves = 0;
					} else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
							// midJoinigYesLeaves = 0;
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					} else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					}
				}
				if (Accrual.equalsIgnoreCase("Yes")) {
					if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 12);
						MonthOrQuarterDifference = objDateTimeHelper
								.getMonthDifferenceFromCurrentDate(leavesCalculationStartDate);
						leavesDiffFromFirstDayOfQuarter = 0;
					} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
							&& Biannually.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 4);
						MonthOrQuarterDifference = objDateTimeHelper
								.getQuarterDiffFromCurrentDate(leavesCalculationStartDate);
						leavesDiffFromFirstDayOfQuarter = ((perMonthLeaves)
								* objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(leavesCalculationStartDate));
					} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("Yes")) {

						perMonthOrQuarterLeaves = perMonthLeaves;
						String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate);
						String currentDateBiannualHalf = checkBiannualHalfOfDate(LocalDate.now().toString());

						midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString();
						String biannualEndDate = "";

						if (DOJBiannualHalf.equalsIgnoreCase("First")) {
							biannualEndDate = midYearEndDate;
						} else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
							biannualEndDate = leaveCycleEndDate;
						}
						MonthOrQuarterDifference = objDateTimeHelper
								.getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

						if (DOJBiannualHalf.equalsIgnoreCase("First")
								&& currentDateBiannualHalf.equalsIgnoreCase("First")) {
							biannualLeave = 0;
						} else if (DOJBiannualHalf.equalsIgnoreCase("First")
								&& currentDateBiannualHalf.equalsIgnoreCase("Second")) {
							biannualLeave = Leaves_Allowed_Per_Year / 2;
						} else if (DOJBiannualHalf.equalsIgnoreCase("Second")
								&& currentDateBiannualHalf.equalsIgnoreCase("Second")) {
							biannualLeave = 0;
						} else {
							Reporter("Exception while calculating Biannual Leaves", "Error");
							throw new RuntimeException();
						}

					}

					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("No")) {
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					}
				}

				ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
						- (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) + biannualLeave);
			}
			double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance * 100.0) / 100.0;

			if (ExpectedLeaveBalanceRoundOff < 0) {
				ExpectedLeaveBalanceRoundOff = 0;
			}

			return ExpectedLeaveBalanceRoundOff;
		} catch (Exception e) {
			Reporter("Exception while calculating employess expected leave balance", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method checks if employee DOJ is under Leave Probation period
	 * 
	 * @param DOJ
	 * @return
	 */
	public boolean checkDOJisUnderLeaveProbationPeriod(String DOJ) {
		try {
			String todaysDate = objDateTimeHelper.getCurrentLocalDate();
			int flag = 0;
			if (Leave_Probation_period_Custom_Months.equals("Yes")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("No")) {
				double probation_Months = Double.valueOf(Probation_period_before_leave_validity_months);
				double monthsDiff = objDateTimeHelper.getExactMonthDifferenceBetweenTwoDates(DOJ, todaysDate);
				if (monthsDiff < probation_Months) {
					flag++;
				}
				long longPBMonth = (long) (-probation_Months);
				Leave_Probation_End_Date = LocalDate.parse(DOJ).minusMonths(longPBMonth).toString();
			} else if (Leave_Probation_period_Custom_Months.equals("No")
					&& Leave_Probation_period_employee_probation_period.equalsIgnoreCase("Yes")) {
				double daysDiff = objDateTimeHelper.getDaysDifferenceBetweenDOJAndCurrentDate(DOJ);
				double Employee_probation_period_Int = Double.valueOf(Employee_probation_period);
				if (daysDiff < Employee_probation_period_Int) {
					flag++;
				}
				long daysToSubtract = (long) (-Employee_probation_period_Int);
				Leave_Probation_End_Date = LocalDate.parse(DOJ).minusDays(daysToSubtract).toString();
			} else {

				flag = 0;
			}

			if (flag > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method calculate and returns whether Date falls in First or Second
	 * Biannual Half
	 * 
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public String checkBiannualHalfOfDate(String DATEIN_YYYY_MM_DD_format) {
		try {
			String biannualHalf = "";
			String employeeConfirmationdate = LocalDate.parse(DATEIN_YYYY_MM_DD_format).toString();
			String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
			double monthsDiff = objDateTimeHelper.getMonthDifferenceBetweenTwoDates(employeeConfirmationdate,
					leaveCycleEndDate);

			if (monthsDiff > 6) {
				biannualHalf = "First";
			} else if (monthsDiff <= 6) {
				biannualHalf = "Second";
			}
			return biannualHalf;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while calculating Biannual Leave Balance", "Error");
			throw new RuntimeException();
		}
	}

	/**
	 * This method verifies leaves balance for whole leave cycle of the employee
	 * 
	 * @return boolean
	 */
	public boolean verifyEmployeeTenureBasedLeaveBalanceForFourEdgeDays() {
		try {

			String result = "";
			int flag = 0;
			double expectedBalance = 0;
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
			double expectedLeaveBalanceAfterAnniversary;
			List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
			List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
			List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

			int anniversaryIterator = 0;
			while ((anniversaryIterator < creditFromYearList.size()) && (anniversaryIterator < creditToYearList.size())
					&& (anniversaryIterator < creditNoOfLeavesList.size())) {
				String CreditFromYearVar = creditFromYearList.get(anniversaryIterator).toString().trim();
				String CreditToYearVar = creditToYearList.get(anniversaryIterator).toString().trim();
				String CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).toString().trim();

				long CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
				long CreditToYearVarLong = Long.valueOf(CreditToYearVar);
				long CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

				if (anniversaryIterator > 0) {
					Leaves_Allowed_Per_Year = Double
							.valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).toString().trim());
				}

				if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
					LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
					LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

					LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
							.minusYears(CreditToYearVarLong - 1);
					LocalDate lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);

					int anniversaryYear = anniversaryIterator + 1;

					Reporter("Leaves will be calculated for ~" + anniversaryYear + "~ anniversary", "Info");
					Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
							+ " hence leave balance will be verifed from '" + lastDateForCalculation + "' till '"
							+ startDateForCalculation, "Info");

					int i = 0;
					LocalDate iterationDate = lastDateForCalculation;

					while (iterationDate.isAfter(startDateForCalculation.minusDays(-1))
							&& iterationDate.isBefore(lastDateForCalculation.minusDays(-1))) {
						iterationDate = lastDateForCalculation.minusDays(i);
						if (iterationDateFourTimesPerMonth(iterationDate) == true) {
							DateOfJoining = changeEmployeeDOJ(iterationDate);

							LocalDate anniversaryDateInDateFormat = iterationDate.minusYears(-CreditFromYearVarLong);

							if (anniversaryDateInDateFormat.isAfter(LocalDate.now())) {
								expectedBalance = calculateLeaveBalance(leaveCycleStartDate);
							} else {
								String anniversaryDate = anniversaryDateInDateFormat.toString();
								double expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
										leaveCycleStartDate, anniversaryDate, "Before");
								double temLeaveVar = Leaves_Allowed_Per_Year;
								Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
								if (anniversaryDateInDateFormat.isBefore(LocalDate.now())) {
									expectedLeaveBalanceAfterAnniversary = calculateTenureBasedLeaveBalance(
											anniversaryDate, objDateTimeHelper.getCurrentLocalDate(), "After");
								} else {
									expectedLeaveBalanceAfterAnniversary = 0;
								}
								Leaves_Allowed_Per_Year = temLeaveVar;
								expectedBalance = expectedLeaveBalanceBeforeAnniversary
										+ expectedLeaveBalanceAfterAnniversary;
							}
							double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
							if (expectedBalance != actualBalance) {
								Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
										+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
								result = "Fail";
								flag++;
							} else {
								Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
										+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
								result = "Pass";
							}

							if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
								writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
										LocalDateTime.now().toString());
							}
						}
						i++;
					}
				} else {
					Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
				}
				anniversaryIterator++;
			}
			if (flag > 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			Reporter("Exception while comparing leave balance", "Error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method verifies leaves balance for whole leave cycle of the employee
	 * 
	 * @return boolean
	 */
	public boolean verifyEmployeeTenureBasedLeaveBalanceForWholeYear() {
		try {

			String result = "";
			int flag = 0;
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
			double expectedLeaveBalanceAfterAnniversary;
			List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
			List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
			List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

			int anniversaryIterator = 0;
			while ((anniversaryIterator < creditFromYearList.size()) && (anniversaryIterator < creditToYearList.size())
					&& (anniversaryIterator < creditNoOfLeavesList.size())) {
				String CreditFromYearVar = creditFromYearList.get(anniversaryIterator).toString().trim();
				String CreditToYearVar = creditToYearList.get(anniversaryIterator).toString().trim();
				String CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).toString().trim();

				long CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
				long CreditToYearVarLong = Long.valueOf(CreditToYearVar);
				long CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

				if (anniversaryIterator > 0) {
					Leaves_Allowed_Per_Year = Double
							.valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).toString().trim());
				}

				if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
					LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
					LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

					LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
							.minusYears(CreditToYearVarLong - 1);
					LocalDate lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);

					Reporter("Leave will get calculated for: " + anniversaryIterator + " Annivaersary", "Info");
					Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
							+ " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
							+ "' till " + lastDateForCalculation, "Info");

					int i = 0;
					LocalDate iterationDate = lastDateForCalculation;

					while (iterationDate.isAfter(startDateForCalculation.minusDays(-1))
							&& iterationDate.isBefore(lastDateForCalculation.minusDays(-1))) {
						iterationDate = lastDateForCalculation.minusDays(i);
						DateOfJoining = changeEmployeeDOJ(iterationDate);

						LocalDate anniversaryDateInDateFormat = iterationDate.minusYears(-CreditFromYearVarLong);

						String anniversaryDate = anniversaryDateInDateFormat.toString();

						double expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
								leaveCycleStartDate, anniversaryDate, "Before");

						double temLeaveVar = Leaves_Allowed_Per_Year;
						Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
						if (anniversaryDateInDateFormat.isBefore(LocalDate.now())) {
							expectedLeaveBalanceAfterAnniversary = calculateTenureBasedLeaveBalance(anniversaryDate,
									objDateTimeHelper.getCurrentLocalDate(), "After");
						} else {
							expectedLeaveBalanceAfterAnniversary = 0;
						}
						Leaves_Allowed_Per_Year = temLeaveVar;
						double expectedBalance = expectedLeaveBalanceBeforeAnniversary
								+ expectedLeaveBalanceAfterAnniversary;
						double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
						if (expectedBalance != actualBalance) {
							Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
									+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
							result = "Fail";
							flag++;
						} else {
							Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
									+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
							result = "Pass";
						}
						if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
							writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
									LocalDateTime.now().toString());
						}
					}
					i++;
				} else {
					Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
				}
				anniversaryIterator++;
			}

			if (flag > 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			Reporter("Exception while comparing leave balance", "Error");
			return false;
		}
	}

	/**
	 * This method verifies leaves balance for whole leave cycle of the employee
	 * 
	 * @return boolean
	 */
	public boolean verifyEmployeeTenureBasedLeaveBalanceForAParticularDOJ(String DOJ, String endDate) {
		try {

			LocalDate endDateinDateFormat = LocalDate.parse(endDate);
			String result = "";
			int flag = 0;
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
			double expectedLeaveBalanceAfterAnniversary;
			List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
			List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
			List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

			int anniversaryIterator = 0;
			while ((anniversaryIterator < creditFromYearList.size()) && (anniversaryIterator < creditToYearList.size())
					&& (anniversaryIterator < creditNoOfLeavesList.size())) {
				String CreditFromYearVar = creditFromYearList.get(anniversaryIterator).toString().trim();
				String CreditToYearVar = creditToYearList.get(anniversaryIterator).toString().trim();
				String CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).toString().trim();

				long CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
				long CreditToYearVarLong = Long.valueOf(CreditToYearVar);
				long CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

				if (anniversaryIterator > 0) {
					Leaves_Allowed_Per_Year = Double
							.valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).toString().trim());
				}

				if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
					LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
					LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);
					LocalDate dateToCalculateInDateFormat = LocalDate.parse(DOJ);

					LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
							.minusYears(CreditToYearVarLong - 1);
					LocalDate lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
					LocalDate dateToCalculate = dateToCalculateInDateFormat.minusYears(CreditFromYearVarLong);

					Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
							+ " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
							+ "' till current date", "Info");

					int i = 0;
					LocalDate iterationDate = dateToCalculate;

					while (iterationDate.isAfter(startDateForCalculation.minusDays(-1))
							&& iterationDate.isBefore(lastDateForCalculation.minusDays(-1))) {
						iterationDate = dateToCalculate.minusYears(i);
						DateOfJoining = changeEmployeeDOJ(iterationDate);

						LocalDate anniversaryDateInDateFormat = iterationDate.minusYears(-CreditFromYearVarLong);

						String anniversaryDate = anniversaryDateInDateFormat.toString();

						double expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
								leaveCycleStartDate, anniversaryDate, "Before");

						double temLeaveVar = Leaves_Allowed_Per_Year;
						Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
						if (anniversaryDateInDateFormat.isBefore(endDateinDateFormat)) {
							expectedLeaveBalanceAfterAnniversary = calculateTenureBasedLeaveBalance(anniversaryDate,
									endDate, "After");
						} else {
							expectedLeaveBalanceAfterAnniversary = 0;
						}
						Leaves_Allowed_Per_Year = temLeaveVar;
						double expectedBalance = expectedLeaveBalanceBeforeAnniversary
								+ expectedLeaveBalanceAfterAnniversary;
						double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
						if (expectedBalance != actualBalance) {
							Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
									+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
							result = "Fail";
							flag++;
						} else {
							Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
									+ expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
							result = "Pass";
						}
						i++;
						if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
							writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
									LocalDateTime.now().toString());
						}

					}

				} else {
					Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
				}
				anniversaryIterator++;
			}

			if (flag > 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			Reporter("Exception while comparing leave balance", "Error");
			return false;
		}
	}


	/**
	 * This method calculate leave balance
	 * 
	 * @param DOJ
	 * @param leaveType
	 * @return double
	 */
	public double calculateTenureBasedLeaveBalance(String DOJ, String LastDayOfCalculation, String beforeAfter) {
		try {
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
			double midJoinigYesLeaves = 0;
			double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
			double perMonthOrQuarterLeaves = 0;
			double MonthOrQuarterDifference = 0;
			double leavesDiffFromFirstDayOfQuarter = 0;
			String leavesCalculationStartDate = "";
			String midYearEndDate;
			double biannualLeave = 0;
			double truncateLeaves = 0;
			LocalDate LastDayOfCalculationInDateFormat = LocalDate.parse(LastDayOfCalculation);
			Boolean tempFlag = false;

			if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
				ExpectedLeaveBalance = 0;
			} else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {

				if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
						&& Calculate_after_probation_period.equalsIgnoreCase("No")) {
					LeaveCalBeginningDate = DOJ;
				} else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
						&& Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
					LeaveCalBeginningDate = Leave_Probation_End_Date;
				} else if (Pro_rata.equalsIgnoreCase("No")) {
					LeaveCalBeginningDate = DOJ;
				}

				if (Pro_rata.equalsIgnoreCase("Yes")) {

					if (Accrual.equalsIgnoreCase("Yes")) {
						leavesCalculationStartDate = LeaveCalBeginningDate;
					}
					if (Accrual.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = perMonthLeaves;
						if (beforeAfter.equalsIgnoreCase("Before")) {
							MonthOrQuarterDifference = objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
						} else if (beforeAfter.equalsIgnoreCase("After")) {
							MonthOrQuarterDifference = objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, leaveCycleEndDate);
						}

						if (MonthOrQuarterDifference < 0) {
							MonthOrQuarterDifference = -MonthOrQuarterDifference;
						}
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					}

				} else if (Pro_rata.equalsIgnoreCase("No")) {

					if (beforeAfter.equalsIgnoreCase("Before")) {
						leavesCalculationStartDate = leaveCycleStartDate;
					} else if (beforeAfter.equalsIgnoreCase("After")) {
						leavesCalculationStartDate = LeaveCalBeginningDate;
					}

					if (Accrual.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = perMonthLeaves;
						// MonthOrQuarterDifference = 12;
						if (beforeAfter.equalsIgnoreCase("Before")) {
							MonthOrQuarterDifference = objDateTimeHelper.getMonthDifferenceBetweenTwoDates(
									leavesCalculationStartDate, LastDayOfCalculation);
						} else if (beforeAfter.equalsIgnoreCase("After")) {
							MonthOrQuarterDifference = objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, leaveCycleEndDate);
						}

						if (MonthOrQuarterDifference < 0) {
							MonthOrQuarterDifference = -MonthOrQuarterDifference;
						}
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					}
				}

				if (Pro_rata.equalsIgnoreCase("Yes")) {
					if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						midJoinigYesLeaves = 0;
					} else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
							// midJoinigYesLeaves = 0;
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					} else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					}
				}
				if (Accrual.equalsIgnoreCase("Yes")) {
					if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 12);
						MonthOrQuarterDifference = objDateTimeHelper
								.getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, LastDayOfCalculation);
						leavesDiffFromFirstDayOfQuarter = 0;
					} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
							&& Biannually.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 4);
						MonthOrQuarterDifference = objDateTimeHelper
								.getQuarterDiffBetweenTwoDates(leavesCalculationStartDate, LastDayOfCalculation);
						leavesDiffFromFirstDayOfQuarter = ((perMonthLeaves)
								* objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(leavesCalculationStartDate));
						if (beforeAfter.equalsIgnoreCase("Before")) {
							truncateLeaves = (perMonthLeaves
									* objDateTimeHelper.getMonthDiffFromLastMonthOfQuarter(LastDayOfCalculation));
						}
					} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("Yes")) {

						perMonthOrQuarterLeaves = perMonthLeaves;
						String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate);
						String currentDateBiannualHalf = checkBiannualHalfOfDate(LastDayOfCalculation);

						midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString();
						String biannualEndDate = "";

						if (DOJBiannualHalf.equalsIgnoreCase("First")) {
							biannualEndDate = midYearEndDate;
						} else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
							biannualEndDate = leaveCycleEndDate;
						}
						MonthOrQuarterDifference = objDateTimeHelper
								.getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

						if (DOJBiannualHalf.equalsIgnoreCase("First")
								&& currentDateBiannualHalf.equalsIgnoreCase("First")) {
							biannualLeave = 0;
						} else if (DOJBiannualHalf.equalsIgnoreCase("First")
								&& currentDateBiannualHalf.equalsIgnoreCase("Second")) {
							biannualLeave = Leaves_Allowed_Per_Year / 2;
						} else if (DOJBiannualHalf.equalsIgnoreCase("Second")
								&& currentDateBiannualHalf.equalsIgnoreCase("Second")) {
							biannualLeave = 0;
						} else {
							Reporter("Exception while calculating Biannual Leaves", "Fail");
							throw new RuntimeException();
						}

						if (beforeAfter.equalsIgnoreCase("Before")) {
							truncateLeaves = perMonthLeaves * objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LastDayOfCalculation, biannualEndDate);
						}

					}

					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("No")) {
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes") && Biannually.equalsIgnoreCase("No")
							&& beforeAfter.equalsIgnoreCase("Before")) {
						if (Monthly.equalsIgnoreCase("Yes")) {
							tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat);
						} else if (Quarterly.equalsIgnoreCase("Yes")) {
							tempFlag = checkIfDayIsLastDayOfQuarter(LastDayOfCalculationInDateFormat);
						} else {
							tempFlag = false;
						}
						if (tempFlag == true) {
							MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
						}
					}
				}

				ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
						- (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave);
			}
			double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance * 100.0) / 100.0;

			if (ExpectedLeaveBalanceRoundOff < 0) {
				ExpectedLeaveBalanceRoundOff = 0;
			}

			return ExpectedLeaveBalanceRoundOff;
		} catch (Exception e) {
			Reporter("Exception while calculating employess expected leave balance", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method calculate leave balance
	 * 
	 * @param DOJ
	 * @param leaveType
	 * @return double
	 */
	public double calculateEffectiveDateLeaveBalance(String DOJ, String LastDayOfCalculation, String beforeAfter,
			String EffectiveDate) {
		try {
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
			double midJoinigYesLeaves = 0;
			double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
			double perMonthOrQuarterLeaves = 0;
			double MonthOrQuarterDifference = 0;
			double leavesDiffFromFirstDayOfQuarter = 0;
			String leavesCalculationStartDate = "";
			String midYearEndDate;
			double biannualLeave = 0;
			double truncateLeaves = 0;
			LocalDate LastDayOfCalculationInDateFormat = LocalDate.parse(LastDayOfCalculation);
			Boolean tempFlag = false;

			if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
				ExpectedLeaveBalance = 0;
			} else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {
				DOJ = EffectiveDate;
				if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
						&& Calculate_after_probation_period.equalsIgnoreCase("No")) {
					LeaveCalBeginningDate = DOJ;
				} else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
						&& Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
					LeaveCalBeginningDate = DOJ;// Leave_Probation_End_Date;
				} else if (Pro_rata.equalsIgnoreCase("No")) {
					LeaveCalBeginningDate = DOJ;
				}

				if (Pro_rata.equalsIgnoreCase("Yes")) {

					if (Accrual.equalsIgnoreCase("Yes")) {
						leavesCalculationStartDate = LeaveCalBeginningDate;
					}
					if (Accrual.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = perMonthLeaves;
						if (beforeAfter.equalsIgnoreCase("Before")) {
							MonthOrQuarterDifference = objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
						} else if (beforeAfter.equalsIgnoreCase("After")) {
							MonthOrQuarterDifference = objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, leaveCycleEndDate);
						}

						if (MonthOrQuarterDifference < 0) {
							MonthOrQuarterDifference = -MonthOrQuarterDifference;
						}
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					}

				} else if (Pro_rata.equalsIgnoreCase("No")) {

					if (beforeAfter.equalsIgnoreCase("Before")) {
						leavesCalculationStartDate = leaveCycleStartDate;
					} else if (beforeAfter.equalsIgnoreCase("After")) {
						leavesCalculationStartDate = LeaveCalBeginningDate;
					}

					if (Accrual.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = perMonthLeaves;
						// MonthOrQuarterDifference = 12;
						if (beforeAfter.equalsIgnoreCase("Before")) {
							MonthOrQuarterDifference = objDateTimeHelper.getMonthDifferenceBetweenTwoDates(
									leavesCalculationStartDate, LastDayOfCalculation);
						} else if (beforeAfter.equalsIgnoreCase("After")) {
							MonthOrQuarterDifference = objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, leaveCycleEndDate);
						}

						if (MonthOrQuarterDifference < 0) {
							MonthOrQuarterDifference = -MonthOrQuarterDifference;
						}
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					}
				}

				if (Pro_rata.equalsIgnoreCase("Yes")) {
					if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						midJoinigYesLeaves = 0;
					} else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
							// midJoinigYesLeaves = 0;
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					} else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
							&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
						if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
							midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
						} else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
								.equalsIgnoreCase("No")) {
							midJoinigYesLeaves = 0;
						}
					}
				}
				if (Accrual.equalsIgnoreCase("Yes")) {
					if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 12);
						MonthOrQuarterDifference = objDateTimeHelper
								.getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, LastDayOfCalculation);
						leavesDiffFromFirstDayOfQuarter = 0;
					} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
							&& Biannually.equalsIgnoreCase("No")) {
						perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 4);
						MonthOrQuarterDifference = objDateTimeHelper
								.getQuarterDiffBetweenTwoDates(leavesCalculationStartDate, LastDayOfCalculation);
						leavesDiffFromFirstDayOfQuarter = ((perMonthLeaves)
								* objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(leavesCalculationStartDate));
						if (beforeAfter.equalsIgnoreCase("Before")) {
							truncateLeaves = (perMonthLeaves
									* objDateTimeHelper.getMonthDiffFromLastMonthOfQuarter(LastDayOfCalculation));
						}
					} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("Yes")) {

						perMonthOrQuarterLeaves = perMonthLeaves;
						String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate);
						String currentDateBiannualHalf = checkBiannualHalfOfDate(LastDayOfCalculation);

						midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString();
						String biannualEndDate = "";

						if (DOJBiannualHalf.equalsIgnoreCase("First")) {
							biannualEndDate = midYearEndDate;
						} else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
							biannualEndDate = leaveCycleEndDate;
						}
						MonthOrQuarterDifference = objDateTimeHelper
								.getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

						if (DOJBiannualHalf.equalsIgnoreCase("First")
								&& currentDateBiannualHalf.equalsIgnoreCase("First")) {
							biannualLeave = 0;
						} else if (DOJBiannualHalf.equalsIgnoreCase("First")
								&& currentDateBiannualHalf.equalsIgnoreCase("Second")) {
							biannualLeave = Leaves_Allowed_Per_Year / 2;
						} else if (DOJBiannualHalf.equalsIgnoreCase("Second")
								&& currentDateBiannualHalf.equalsIgnoreCase("Second")) {
							biannualLeave = 0;
						} else {
							Reporter("Exception while calculating Biannual Leaves", "Fail");
							throw new RuntimeException();
						}

						if (beforeAfter.equalsIgnoreCase("Before")) {
							truncateLeaves = perMonthLeaves * objDateTimeHelper
									.getMonthDifferenceBetweenTwoDates(LastDayOfCalculation, biannualEndDate);
						}

					}

					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")
							&& Biannually.equalsIgnoreCase("No")) {
						MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes") && Biannually.equalsIgnoreCase("No")
							&& beforeAfter.equalsIgnoreCase("Before")) {
						if (Monthly.equalsIgnoreCase("Yes")) {
							tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat);
						} else if (Quarterly.equalsIgnoreCase("Yes")) {
							tempFlag = checkIfDayIsLastDayOfQuarter(LastDayOfCalculationInDateFormat);
						} else {
							tempFlag = false;
						}
						if (tempFlag == true) {
							MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
						}
					}
				}

				ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
						- (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave);
			}
			double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance * 100.0) / 100.0;

			if (ExpectedLeaveBalanceRoundOff < 0) {
				ExpectedLeaveBalanceRoundOff = 0;
			}

			return ExpectedLeaveBalanceRoundOff;
		} catch (Exception e) {
			Reporter("Exception while calculating employess expected leave balance", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public boolean checkIfDayIsLastDayOfMonth(LocalDate LastDateOfCalculationInDateFormat) {
		try {
			LocalDate todaysDate = LocalDate.now();
			LocalDate lastDayOfCurrentMonth = todaysDate.withDayOfMonth(todaysDate.lengthOfMonth());
			LocalDate firstDayOfCurrentMonth = todaysDate.with(firstDayOfMonth());

			if (LastDateOfCalculationInDateFormat.isBefore(firstDayOfCurrentMonth)) {
				return true;
			} else if (LastDateOfCalculationInDateFormat.isAfter(firstDayOfCurrentMonth)
					|| LastDateOfCalculationInDateFormat.isEqual(firstDayOfCurrentMonth)) {
				if (LastDateOfCalculationInDateFormat.isEqual(lastDayOfCurrentMonth)) {
					return true;
				} else {
					return false;
				}
			} else {
				throw new RuntimeException("Date is not before or after 1st day of current month. Please check");
			}
		} catch (Exception e) {
			Reporter("Exception while calculating Last Day of Month or Quarter", "Error");
			throw new RuntimeException();
		}
	}

	public boolean checkIfDayIsLastDayOfQuarter(LocalDate LastDateOfCalculationInDateFormat) {
		try {
			LocalDate todaysDate = LocalDate.now();
			LocalDate currentQuarterLastMonth = objDateTimeHelper.getFirstDayOfQuarterLastMonth(todaysDate.toString());
			LocalDate lastDayOfCurrentQuarterLastMonth = currentQuarterLastMonth
					.withDayOfMonth(currentQuarterLastMonth.lengthOfMonth());
			LocalDate firstDayOfCurrentQuarter = objDateTimeHelper.getFirstDayOfQuarter(todaysDate.toString());

			if (LastDateOfCalculationInDateFormat.isBefore(firstDayOfCurrentQuarter)) {
				return true;
			} else if (LastDateOfCalculationInDateFormat.isAfter(firstDayOfCurrentQuarter)
					|| LastDateOfCalculationInDateFormat.isEqual(firstDayOfCurrentQuarter)) {
				if (LastDateOfCalculationInDateFormat.isEqual(lastDayOfCurrentQuarterLastMonth)) {
					return true;
				} else {
					return false;
				}
			} else {
				throw new RuntimeException("Date is not before or after 1st day of current month. Please check");
			}
		} catch (Exception e) {
			Reporter("Exception while calculating Last Day of Month or Quarter", "Error");
			throw new RuntimeException();
		}
	}

	public static List<String> EmployeeTypeName = new ArrayList<String>();
	public static List<String> EmployeeTypeId = new ArrayList<String>();;

	public void getAllEmployeeTypes() {
		try {
			String text = "emailtemplate/GetAllEmployeeTypes";
			String frontEndEmployeeType = objUtil.getHTMLTextFromAPI(driver, text);
			System.out.println(frontEndEmployeeType);
			String[] frontEndEmployeeTypeSplit = frontEndEmployeeType.split("\\r?\\n");
			int i = 0;
			for (String line : frontEndEmployeeTypeSplit) {
				// System.out.println("line -->" + ": " + line);
				EmployeeTypeName.add(line.substring(0, line.indexOf("<")));
				EmployeeTypeId.add(line.substring(line.indexOf(">") + 1));
				// System.out.println("EmployeeTypeName.get(i)-->" + EmployeeTypeName.get(i));
				// System.out.println("EmployeeTypeId.get(i)-->" + EmployeeTypeId.get(i));
			}

		} catch (Exception e) {
			Reporter("Exception while getting all employee types in instance", "Error");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static HashMap<String, String> EmployeeTypesHmap = new HashMap<String, String>();

	public void getAllEmployeeTypesInInstance() {
		try {
			String text = "emailtemplate/GetAllEmployeeTypes";
			String frontEndEmployeeType = objUtil.getHTMLTextFromAPI(driver, text);
			System.out.println(frontEndEmployeeType);
			String[] frontEndEmployeeTypeSplit = frontEndEmployeeType.split("\\r?\\n");
			String employeeType;
			String employeeTypeID;

			for (String line : frontEndEmployeeTypeSplit) {
				employeeType = line.substring(0, line.indexOf("<"));
				employeeTypeID = line.substring(line.indexOf(">") + 1);
				EmployeeTypesHmap.put(employeeType, employeeTypeID);
			}

		} catch (Exception e) {
			Reporter("Exception while getting all employee types in instance", "Error");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String EmployeeDataEmployeeNo;
	public static String EmployeeDataEmployeeType;
	public static String EmployeeDataEmployeeTypeID;
	public static String EmployeeDataDesignation;
	public static String EmployeeDataDesignationID;

	public static HashMap<String, String> EmployeeDataHmap = new HashMap<String, String>();

	public void getEmployeeData() {
		try {
			String text = "emailtemplate/GetEmployeeData/id/" + EMPID;
			String frontEndEmployeeDataDetails = objUtil.getHTMLTextFromAPI(driver, text);
			System.out.println(frontEndEmployeeDataDetails);
			String[] frontEndEmployeeDataDetailsSplit = frontEndEmployeeDataDetails.split("\\r?\\n");

			for (String line : frontEndEmployeeDataDetailsSplit) {
				String key = line.substring(0, line.indexOf("<"));
				String value = line.substring(line.indexOf(">") + 1);
				System.out.println("key----->" + key);
				System.out.println("value--->" + value);
				EmployeeDataHmap.put(key, value);
			}

			EmployeeDataEmployeeNo = "employee_no";
			EmployeeDataEmployeeType = "type";
			EmployeeDataEmployeeTypeID = "emp_type_id";
			EmployeeDataDesignation = "designation";
			EmployeeDataDesignationID = "designation_id";
		} catch (Exception e) {
			Reporter("Exception while getting front end leave balance for the employee", "Fatal");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public boolean changeEmployeeType() {
		try {
			String anotherEmployeeTypeID = "";
			int flag = 0;
			int i = 0;
			while (flag == 0) {
				if (EmployeeTypeId.get(i).trim().equals(EmployeeDataHmap.get(EmployeeDataEmployeeTypeID))) {
					flag = 0;
				} else {
					flag++;
					anotherEmployeeTypeID = EmployeeTypeId.get(i).trim();
				}
				i++;
			}

			String text = "emailtemplate/SetEmployeeData/id/" + EMPID + "/type/" + anotherEmployeeTypeID;
			objUtil.callTheAPI(driver, text);

			Reporter("Changed emaployees Employee Type from '" + EmployeeDataHmap.get(EmployeeDataEmployeeType)
					+ "' to '" + EmployeeTypeName.get(i), "Pass");
			String currentURL = driver.getCurrentUrl();
			String getEmployeeURL = ObjectRepo.reader.getApplication() + "emailtemplate/GetEmployeeData/id/" + EMPID;

			if (currentURL.trim().equals(getEmployeeURL)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			Reporter("Exception while changing Employee Type", "Error");
			e.printStackTrace();
			return false;
		}
	}

	public boolean changeEmployeeTypeBackToOriginal() {
		try {
			String text = "emailtemplate/SetEmployeeData/id/" + EMPID + "/type/"
					+ EmployeeDataHmap.get(EmployeeDataEmployeeType).trim();
			objUtil.callTheAPI(driver, text);

			Reporter("Changed emaployees Employee Type back to original value which is: '"
					+ EmployeeDataHmap.get(EmployeeDataEmployeeType), "Pass");
			String currentURL = driver.getCurrentUrl();
			String getEmployeeURL = ObjectRepo.reader.getApplication() + "emailtemplate/GetEmployeeData/id/" + EMPID;

			if (currentURL.trim().equals(getEmployeeURL)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			Reporter("Exception while changing Employee type back to Original value", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public boolean changeEmployeeType(String empType) {
		try {
			String anotherEmployeeTypeID = EmployeeTypesHmap.get(empType.trim());

			String text = "emailtemplate/SetEmployeeData/id/" + EMPID + "/type/" + anotherEmployeeTypeID;
			objUtil.callTheAPI(driver, text);

			Reporter("Changed emaployees Employee Type from '" + EmployeeDataHmap.get(EmployeeDataEmployeeType)
					+ "' to '" + empType, "Pass");
			String currentURL = driver.getCurrentUrl();
			String getEmployeeURL = ObjectRepo.reader.getApplication() + "emailtemplate/GetEmployeeData/id/" + EMPID;

			if (currentURL.trim().equals(getEmployeeURL)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			Reporter("Exception while changing Employee Type", "Error");
			e.printStackTrace();
			return false;
		}
	}

	public boolean setLeaveType() {
		Leave_Type = getData("Leave_Type");
		Reporter("Leave Type is : '" + Leave_Type + "'", "Info");
		return true;
	}
	
	public boolean setLeaveType1() {
		Leave_Type = getData("Leave Type1");
		Reporter("Leave Type is : '" + Leave_Type + "'", "Info");
		return true;
	}

	public boolean setLeaveType2() {
		Leave_Type = getData("Leave Type2");
		Reporter("Leave Type is : '" + Leave_Type + "'", "Info");
		return true;
	}

	/**
	 * This method verifies leave balance of an employee for a particular DOJ
	 * 
	 * @param DOJ
	 * @author shikhar
	 * @return boolean
	 */
	public boolean verifyEffectiveDateLeaveBalanceForParticularDOJ(String effectiveDate) {
		try {
			int flag = 0;
			String result = "";
			// String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			Reporter("Leave will be calculated for Effective Date which is: " + effectiveDate, "Info");
			// LocalDate leaveCycleStartDateInDateFormat =
			// LocalDate.parse(leaveCycleStartDate);
			// LocalDate iterationDate = LocalDate.parse(DOJ);
			 DateOfJoining = changeEmployeeDOJ(LocalDate.parse("2017-01-01"));
			double expectedBalance = calculateEffectiveDateLeaveBalance(DateOfJoining,
					objDateTimeHelper.getCurrentLocalDate(), "After", effectiveDate);
			double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
			if (expectedBalance != actualBalance) {
				flag++;
				Reporter("Failed||" + "Effective Date '" + effectiveDate + "'||" + "Expected Leave Balance=" + expectedBalance
						+ "||Actual Leave Balance=" + actualBalance, "Fail");
				result = "Fail";
			} else {
				Reporter("Passed||" + "Effective Date '" + effectiveDate + "'||" + "Expected Leave Balance=" + expectedBalance
						+ "||Actual Leave Balance=" + actualBalance, "Pass");
				result = "Pass";
			}

			if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
				writeEffectiveDateResultToExcel(effectiveDate, expectedBalance, actualBalance, result,
						LocalDateTime.now().toString());
			}

			if (flag > 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			Reporter("Exception while verifying leave balance for particular DOJ", "Error");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method verifies leaves balance for whole leave cycle of the employee
	 * 
	 * @return boolean
	 */
	public boolean verifyEffectiveDateLeaveBalanceForWholeLeaveCycle(String effectiveDate) {
		try {
			String result = "";
			int flag = 0;
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
					+ " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
					+ "' till current date", "Info");
			Reporter("Leave will be calculated for Effective Date which is: " + effectiveDate, "Info");
			
			LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
			int i = 1;
			LocalDate iterationDate = LocalDate.now();
			while (iterationDate.isAfter(leaveCycleStartDateInDateFormat)) {
				iterationDate = LocalDate.now().minusDays(i);
				DateOfJoining = changeEmployeeDOJ(iterationDate);
				double expectedBalance = calculateEffectiveDateLeaveBalance(DateOfJoining,
						objDateTimeHelper.getCurrentLocalDate(), "After", effectiveDate);
				double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
				if (expectedBalance != actualBalance) {
					Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
							+ "||Actual Leave Balance=" + actualBalance, "Fail");
					result = "Fail";
					flag++;
				} else {
					Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
							+ "||Actual Leave Balance=" + actualBalance, "Pass");
					result = "Pass";
				}
				i++;
				if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
					writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
							LocalDateTime.now().toString());
				}
			}

			if (flag > 0) {
				return false;
			} else {
				return true;
			}

		} catch (

		Exception e) {
			Reporter("Exception while comparing leave balance", "Error");
			return false;
		}
	}

}
