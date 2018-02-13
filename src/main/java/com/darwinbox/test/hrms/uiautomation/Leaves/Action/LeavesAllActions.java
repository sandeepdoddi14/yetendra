package com.darwinbox.test.hrms.uiautomation.Leaves.Action;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CreateAndManageLeavePoliciesPage;
import com.darwinbox.test.hrms.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.UtilityHelper;
import com.darwinbox.test.hrms.uiautomation.configreader.ObjectRepo;
import com.darwinbox.test.hrms.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class LeavesAllActions extends TestBase {

	WaitHelper objWait;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	WebDriver driver;
	DateTimeHelper objDateTimeHelper;
	UtilityHelper objUtil;
	CreateAndManageLeavePoliciesPage createManageLeaves;

	public static final Logger log = Logger.getLogger(LeavesAllActions.class);

	public LeavesAllActions(WebDriver driver) {
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
	}

	public static String EMPID;
	double ExpectedLeaveBalance;
	double ActualLeaveBalance;
	Date leaveCycleFirstDay = null;
	String DateOfJoining = "";
	public static String Leave_Type;
	public static String Max_Leaves_Allowed_Per_Year;
	public static double Leaves_Allowed_Per_Year;
	public static String Leave_Cycle;
	public static String Pro_rata;
	public static String From_Joining_date;
	public static String After_Probation_period;
	public static String Half_Month_Leaves_if_employee_joins_after_15th;
	public static String Full_Month_Leaves_if_employee_joins_after_15th;
	public static String Accural;
	public static String Monthly;
	public static String Quarterly;
	public static String Biannually;
	public static String Begin_of_monthORQuarter;
	public static String End_of_monthORQuarter;

	/**
	 * This method set Leave Scenario parameters from property file
	 */
	public void setLeaveScenarioFromPropertyFile() {
		try {

			Leave_Type = objUtil.getProperty("leavesCalculation", "Leave.Type");
			Max_Leaves_Allowed_Per_Year = objUtil.getProperty("leavesCalculation", "Max.Leaves.Allowed.Per.Year");
			Leaves_Allowed_Per_Year = Double.parseDouble(Max_Leaves_Allowed_Per_Year);
			Leave_Cycle = objUtil.getProperty("leavesCalculation", "Leave.Cycle");
			Pro_rata = objUtil.getProperty("leavesCalculation", "Pro.rata");
			From_Joining_date = objUtil.getProperty("leavesCalculation", "From.Joining.date");
			After_Probation_period = objUtil.getProperty("leavesCalculation", "After.Probation.period");
			Half_Month_Leaves_if_employee_joins_after_15th = objUtil.getProperty("leavesCalculation",
					"Half.Month.Leaves.if.employee.joins.after.15th");
			Full_Month_Leaves_if_employee_joins_after_15th = objUtil.getProperty("leavesCalculation",
					"Full.Month.Leaves.if.employee.joins.after.15th");
			Accural = objUtil.getProperty("leavesCalculation", "Accural");
			Monthly = objUtil.getProperty("leavesCalculation", "Monthly");
			Quarterly = objUtil.getProperty("leavesCalculation", "Quarterly");
			Biannually = objUtil.getProperty("leavesCalculation", "Biannually");
			Begin_of_monthORQuarter = objUtil.getProperty("leavesCalculation", "Begin.of.monthORQuarter");
			End_of_monthORQuarter = objUtil.getProperty("leavesCalculation", "End.of.monthORQuarter");

		} catch (Exception e) {
			Reporter("Exception while setting Leave Calculation Scenario", "Fail");
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
			createManageLeaves.selectLeaveCycleDropdown(Leave_Cycle);

			objJavaScrHelper.scrollToElement(driver,
					createManageLeaves.getWebElementDontShowApplyInProbationPeriodCheckbox(),
					"Dont show & apply in Probation Period Checkbox");

			if (Pro_rata.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickCreditOnProRataBasisAccordian();
				createManageLeaves.clickCreditOnProRataBasisYesRadioButton();
				if (From_Joining_date.equalsIgnoreCase("Yes") && After_Probation_period.equalsIgnoreCase("No")) {
					createManageLeaves.clickCalculateFromJoiningDateRadioButton();
				} else if (From_Joining_date.equalsIgnoreCase("No") && After_Probation_period.equalsIgnoreCase("Yes")) {
					createManageLeaves.clickCalculateAfterProbationPeriodRadioButton();
				}

				if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
					createManageLeaves.clickHalfMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
					createManageLeaves.clickFullMidJoiningLeavesCheckBox();
				} else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
						&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
					createManageLeaves.clickHalfMidJoiningLeavesCheckBox();
					createManageLeaves.clickFullMidJoiningLeavesCheckBox();
				}
			} else {
				Reporter("Pro Rata condition is set to No", "Paas");
			}

			if (Accural.equalsIgnoreCase("Yes")) {
				createManageLeaves.clickCreditOnAccrualBasisAccordian();
				createManageLeaves.clickCreditOnAccrualBasisYesRadioButton();				
				if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
						&& Biannually.equalsIgnoreCase("No")) {
					createManageLeaves.clickAccuralTimeFrameMonthRadioButton();
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						createManageLeaves.clickAccuralPointBeginOfMonthRadioButton();
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						createManageLeaves.clickAccuralPointEndOfMonthRadioButton();
					} else {
						Reporter("By default Begin of Month Accural Point is selected", "Pass");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
						&& Biannually.equalsIgnoreCase("No")) {
//					objWait.waitElementToBeClickable(createManageLeaves.getWebElementAccuralPointBeginOfQuarterRadioButton());
					createManageLeaves.clickAccuralTimeFrameQuarterRadioButton();
					if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes")
							&& End_of_monthORQuarter.equalsIgnoreCase("No")) {
						createManageLeaves.clickAccuralPointBeginOfQuarterRadioButton();
					} else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
							&& End_of_monthORQuarter.equalsIgnoreCase("Yes")) {
						createManageLeaves.clickAccuralPointEndOfQuarterRadioButton();
					} else {
						Reporter("By default Begin of Quarter Accural Point is selected", "Pass");
					}
				} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
						&& Biannually.equalsIgnoreCase("No")) {
					createManageLeaves.clickAccuralTimeFrameBiannualRadioButton();
				} else {
					Reporter("Accuarl Time frame selected is not proper.", "Fail");
					throw new RuntimeException();
				}
			} else {
				Reporter("Accural is selected as No", "Pass");
			}
			driver.switchTo().defaultContent();
			return true;
		} catch (Exception e) {
			Reporter("Exception while creating leave type with desiered Scenarios", "Fail");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * 
	 * @param employee_Id
	 * @return
	 */
	public double getEmployeesFrontEndLeaveBalance(String leaveType) {
		try {
			String applicationURL = ObjectRepo.reader.getApplication();
			String URL = applicationURL + "emailtemplate/Employeeleave?id=" + EMPID + "&leave=" + leaveType;
			driver.navigate().to(URL);
			String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
			// System.out.println("frontEndLeaveBalance--->"+ frontEndLeaveBalance);
			if (frontEndLeaveBalance.isEmpty()) {
				throw new RuntimeException("Front End Leave balance is empty");
			}
			double actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);

			// Reporter("Employees front end leave balance = " + actualLeaveBalance,
			// "Pass");
			return actualLeaveBalance;
		} catch (Exception e) {
			Reporter("Exception while getting front end leave balance for the employee", "Fail");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param leaveCycle
	 * @return
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
				// System.out.println("leaveCycleEndDate--->" +
				// financialYearEndDateInDateFormat);
				if (financialYearEndDateInDateFormat.isAfter(LocalDate.now())) {
					leaveCycleStartDate = previousYear + "-" + "04" + "-" + "01";
					// System.out.println("leaveCycleStartDate--->>" + leaveCycleStartDate);
				} else {
					leaveCycleStartDate = today.getYear() + "-" + "04" + "-" + "01";
					// System.out.println("leaveCycleStartDate--->>" + leaveCycleStartDate);
				}
			} else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
				// System.out.println("leaveCycleEndDate--->" +
				// calendarYearEndDateInDateFormat);
				leaveCycleStartDate = LocalDate.now().getYear() + "-" + "01" + "-" + "01";
				// System.out.println("leaveCycleStartDate--->>" + leaveCycleStartDate);
			}
			return leaveCycleStartDate;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public void verifyEmployeeLeaveBalanceForWholeLeaveCycle() {

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
			// double expectedBalance =
			// calculateEmployeesExpectedLeaveBalance(maxLeavesAllowedPerMonth,
			// DateOfJoining,
			// scenarioDes, leaveCycle);

			double expectedBalance = calculateLeaveBalance(DateOfJoining);
			double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
			if (expectedBalance != actualBalance) {
				Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
						+ "||Actual Leave Balance=" + actualBalance, "Fail");
			} else {
				Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
						+ "||Actual Leave Balance=" + actualBalance, "Pass");
			}
			i++;
		}
	}

	/**
	 * 
	 * @param DOJ
	 */
	public void verifyEmployeeLeaveBalanceForParticularDOJ(String DOJ) {

		String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
		Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
				+ " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
				+ "' till current date", "Info");

		LocalDate iterationDate = LocalDate.parse(DOJ);

		DateOfJoining = changeEmployeeDOJ(iterationDate);
		double expectedBalance = calculateLeaveBalance(DateOfJoining);
		// System.out.println("expectedBalance--->" + expectedBalance);
		double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
		if (expectedBalance != actualBalance) {
			Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
					+ "||Actual Leave Balance=" + actualBalance, "Fail");
		} else {
			Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance=" + expectedBalance
					+ "||Actual Leave Balance=" + actualBalance, "Pass");
		}
	}

	/**
	 * This method calculates the expected leave balance
	 * 
	 * @param numberOfLeaves
	 * @param DOJ
	 * @return
	 */
	public double calculateEmployeesExpectedLeaveBalance(double numberOfLeaves, String DOJ, String scenarioDes,
			String leaveCycle) {
		try {

			String leaveCycleStartDate = getFirstDayofLeaveCycle(leaveCycle);

			if (scenarioDes.equalsIgnoreCase("Begin of Month")) {
				ExpectedLeaveBalance = ((numberOfLeaves / 12)
						* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(DOJ) + 1));
			} else if (scenarioDes.equalsIgnoreCase("End of Month")) {
				ExpectedLeaveBalance = ((numberOfLeaves / 12)
						* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(DOJ)));
			} else if (scenarioDes.equalsIgnoreCase("Mid Joining End Of Month")) {

				if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("Yes")) {
					ExpectedLeaveBalance = ((numberOfLeaves / 12)
							* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(DOJ)) - ((numberOfLeaves / 24)));
				} else if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("No")) {
					ExpectedLeaveBalance = ((numberOfLeaves / 12)
							* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(DOJ)));
				}
			} else if (scenarioDes.equalsIgnoreCase("Mid Joining Begin Of Month")) {

				if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("Yes")) {
					ExpectedLeaveBalance = ((numberOfLeaves / 12)
							* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(DOJ) + 1) - ((numberOfLeaves / 24)));
				} else if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("No")) {
					ExpectedLeaveBalance = ((numberOfLeaves / 12)
							* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(DOJ) + 1));
				}
			} else if (scenarioDes.equalsIgnoreCase("Accuarl Quarter Begin Of Quarter")) {
				ExpectedLeaveBalance = (((numberOfLeaves / 4)
						* (objDateTimeHelper.getQuarterDiffFromCurrentDate(DOJ) + 1))
						- ((numberOfLeaves / 12) * objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(DOJ)));
			} else if (scenarioDes.equalsIgnoreCase("Accural Quarter End Of Quarter")) {
				ExpectedLeaveBalance = (((numberOfLeaves / 4) * (objDateTimeHelper.getQuarterDiffFromCurrentDate(DOJ)))
						- ((numberOfLeaves / 12) * objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(DOJ)));
			} else if (scenarioDes.equalsIgnoreCase("Accural Quarter Begin Of Quarter Mid Joining Yes")) {
				if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("Yes")) {
					ExpectedLeaveBalance = ((((numberOfLeaves / 4)
							* (objDateTimeHelper.getQuarterDiffFromCurrentDate(DOJ) + 1))
							- ((numberOfLeaves / 12) * objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(DOJ)))
							- (numberOfLeaves / 24));
				} else if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("No")) {
					ExpectedLeaveBalance = (((numberOfLeaves / 4)
							* (objDateTimeHelper.getQuarterDiffFromCurrentDate(DOJ) + 1))
							- ((numberOfLeaves / 12) * objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(DOJ)));
				}
			} else if (scenarioDes.equalsIgnoreCase("Accural Quarter End Of Quarter Mid Joining Yes")) {
				if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("Yes")) {
					ExpectedLeaveBalance = ((((numberOfLeaves / 4)
							* (objDateTimeHelper.getQuarterDiffFromCurrentDate(DOJ)))
							- ((numberOfLeaves / 12) * objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(DOJ)))
							- (numberOfLeaves / 24));
				} else if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("No")) {
					ExpectedLeaveBalance = (((numberOfLeaves / 4)
							* (objDateTimeHelper.getQuarterDiffFromCurrentDate(DOJ)))
							- ((numberOfLeaves / 12) * objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(DOJ)));
				}
			} else if (scenarioDes.equalsIgnoreCase("ProRata No Begin of Month")) {
				ExpectedLeaveBalance = ((numberOfLeaves / 12)
						* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(leaveCycleStartDate) + 1));
			} else if (scenarioDes.equalsIgnoreCase("ProRata No End of Month")) {
				ExpectedLeaveBalance = ((numberOfLeaves / 12)
						* (objDateTimeHelper.getMonthDifferenceFromCurrentDate(leaveCycleStartDate)));
			} else if (scenarioDes.equalsIgnoreCase(" Prorata No Accuarl Quarter Begin Of Quarter")) {
				ExpectedLeaveBalance = (((numberOfLeaves / 4)
						* (objDateTimeHelper.getQuarterDiffFromCurrentDate(leaveCycleStartDate) + 1))
						- ((numberOfLeaves / 12)
								* objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(leaveCycleStartDate)));
			} else if (scenarioDes.equalsIgnoreCase("ProRata No Accural Quarter End Of Quarter")) {
				ExpectedLeaveBalance = (((numberOfLeaves / 4)
						* (objDateTimeHelper.getQuarterDiffFromCurrentDate(leaveCycleStartDate)))
						- ((numberOfLeaves / 12)
								* objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(leaveCycleStartDate)));
			}

			else {

				throw new RuntimeException("Failure while leaves calculation");
			}
			double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance * 100.0) / 100.0;

			if (ExpectedLeaveBalanceRoundOff < 0) {
				ExpectedLeaveBalanceRoundOff = 0;
			}
			return ExpectedLeaveBalanceRoundOff;
		} catch (Exception e) {
			Reporter("Exception while calculating employess expected leave balance", "Fail");
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
			Reporter("Exception while setting Employee id to calculate leave balance", "Fail");
			return false;
		}
	}

	public String changeEmployeeDOJ(LocalDate iterationDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String DOJ = iterationDate.format(formatter);
			String applicationURL = ObjectRepo.reader.getApplication();
			String URL = applicationURL + "emailtemplate/employeedoj?id=" + EMPID + "&date=" + DOJ;
			driver.navigate().to(URL);
			return DOJ;
		} catch (Exception e) {
			Reporter("Exception while changing employees DOJ", "Fail");
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method calculate leave balance
	 * 
	 * @param DOJ
	 * @param leaveType
	 * @return
	 */
	public double calculateLeaveBalance(String DOJ) {
		try {
			String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
			double midJoinigYesLeaves = 0;
			double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
			double perMonthOrQuarterLeaves = 0;
			double MonthOrQuarterDifference = 0;
			double leavesDiffFromFirstDayOfQuarter = 0;

			String leavesCalculationStartDate = "";

			if (Pro_rata.equalsIgnoreCase("Yes")) {
				leavesCalculationStartDate = DOJ;
			} else if (Pro_rata.equalsIgnoreCase("No")) {
				leavesCalculationStartDate = leaveCycleStartDate;
			}

			if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")) {
				perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 12);
				MonthOrQuarterDifference = objDateTimeHelper
						.getMonthDifferenceFromCurrentDate(leavesCalculationStartDate);
				leavesDiffFromFirstDayOfQuarter = 0;
			} else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")) {
				perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 4);
				MonthOrQuarterDifference = objDateTimeHelper.getQuarterDiffFromCurrentDate(leavesCalculationStartDate);
				leavesDiffFromFirstDayOfQuarter = ((perMonthLeaves)
						* objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(leavesCalculationStartDate));
			}

			if (Pro_rata.equalsIgnoreCase("Yes")
					&& Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
				if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("Yes")) {
					midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
				} else if (objDateTimeHelper.verifyDOJMidJoining(DOJ).equalsIgnoreCase("No")) {
					midJoinigYesLeaves = 0;
				}
			} else if (Pro_rata.equalsIgnoreCase("Yes")
					&& Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")) {
				midJoinigYesLeaves = 0;
			}

			if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")) {
				MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
			}

			ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
					- (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves));

			double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance * 100.0) / 100.0;

			if (ExpectedLeaveBalanceRoundOff < 0) {
				ExpectedLeaveBalanceRoundOff = 0;
			}
			return ExpectedLeaveBalanceRoundOff;
		} catch (Exception e) {
			Reporter("Exception while calculating employess expected leave balance", "Fail");
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// public void calculateBiannualLeavebalance(String DATEIN_YYYY_MM_DD_format,
	// String leaveCycle) {
	// try {
	// String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
	// int year = Integer.parseInt(arr[0]);
	// int month = Integer.parseInt(arr[1]);
	// int day = Integer.parseInt(arr[2]);
	// String biannualHalf;
	//
	// LocalDate DOJ = LocalDate.parse(DATEIN_YYYY_MM_DD_format);
	//
	//
	// if(leaveCycle.equalsIgnoreCase("Financial Year")) {
	// if() {
	// biannualHalf = "First";
	// }else if(month >= 10 && month )
	// }else if(leaveCycle.equalsIgnoreCase("Calendar Year")){
	//
	// }else {
	// Reporter("Leave cycle is not correct.", "Fail");
	// throw new RuntimeException();
	// }
	//
	// }catch(Exception e) {
	// e.printStackTrace();
	// Reporter("Exception while calculating Biannual Leave Balance","Fail");
	// throw new RuntimeException();
	// }
	// }

}
