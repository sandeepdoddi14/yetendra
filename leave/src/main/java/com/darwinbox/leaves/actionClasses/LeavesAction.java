package com.darwinbox.leaves.actionClasses;

import Service.LeaveBalanceAPI;
import com.codoid.products.fillo.Recordset;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.ExcelWriter;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.configreader.ObjectRepo;
import com.darwinbox.framework.uiautomation.helper.Action.ActionHelper;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class LeavesAction extends TestBase {

    public LeavesAction(){

    }

    public static final Logger log = Logger.getLogger(LeavesAction.class);

    public static String Leave_Type = "";
    public static String Max_Leaves_Allowed_Per_Year;
    public static String Max_Leaves_Allowed_Per_Month;
    public static String Min_Consecutive_Days_in_Single_Application;
    public static String Max_Consecutive_Days_in_Single_Application;
    public static String Consecutive_Days_Allowed;
    public static String Restrict_To_WeekDay;
    public static double Leaves_Allowed_Per_Year;
    public static String Leave_Cycle;
    public static String Pro_rata = null;
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
    public static String LeaveAccrualBasedOnWorkingDays;
    public static String CountPresentDays;
    public static String CountAbsentDays;
    public static String CountWeeklyoffDays;
    public static String CountHolidayDays;
    public static String CountOptionalHolidayDays;
    public static String CountLeaveDays;
    public static String WorkingDaysEndOfMonth;
    public static String WorkingDaysEndOfQuarter;
    public static String WorkingDaysEndOfBiannual;
    public static String EndOfYear;
    public static String CarryForward;
    public static String OverUtilization;
    public static String CarryForwardAllOrFixedOrPercentage;
    public static String FixedOrPercentageValue;
    public static String MultipleAllotmentEnabled;
    public static String MultipleAllotmentRestrictions;
    public static String MultipleAllotmentLeaves;
    public static int sequenceNo = 0;
    public static String authToken;
    public static String EMPID;
    public static String EmployeeDataEmployeeNo;
    public static String EmployeeDataEmployeeType;
    public static String EmployeeDataEmployeeTypeID;
    public static String EmployeeDataDesignation;
    public static String EmployeeDataDesignationID;
    public static HashMap<String, String> EmployeeDataHmap = new HashMap<String, String>();
    public static String LastDayOfCalculation = "LocalDate";
    public static List<String> EmployeeTypeName = new ArrayList<String>();
    public static List<String> EmployeeTypeId = new ArrayList<String>();
    public static HashMap<String, String> EmployeeTypesHmap = new HashMap<String, String>();


    static boolean beforeProbationFlag = false;
    public String multipleAllotmentCheckOnDayOfTransfer = "No";
    WaitHelper objWait;
    GenericHelper objGenHelper;
    DropDownHelper objDropDownHelper;
    JavaScriptHelper objJavaScrHelper;
    AlertHelper objAlertHelper;
    // public static String WriteResultToExcel = "No";
    WebDriver driver;
    DateTimeHelper objDateTimeHelper;
    LoginPage loginPage;
    UtilityHelper objUtil;
    CreateAndManageLeavePoliciesPage createManageLeaves;
    LeavesSettingsPage leaveSettings;
    ActionHelper objActionHelper;
    BrowserHelper objBrowserHelper;
    ExcelWriter objExcelWriter;
    CommonAction commonAction;
    double ExpectedLeaveBalance = 0;
    double ActualLeaveBalance;
    Date leaveCycleFirstDay = null;
    String DateOfJoining = "";
    int tenureDOJFlag = 0;

    public LeavesAction(WebDriver driver) {
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
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);
    }


    //Navigate To  Leave Settings Page
    public boolean navigateToSettings_Leaves() {
        try {
            objGenHelper.navigateTo("/settings/leaves");
            objWait.waitForPageToLoad();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //set the accordion for Leave Settings
    public boolean setOverutilizationScenario() throws InterruptedException {
        try {
            {
                if(OverUtilization.toLowerCase()=="yes"){
                    Reporter("Over Utilization is Set to Yes", "Ok");
                    createManageLeaves.clickCanEmpApplyForMoreThanTheirAvailableLveBalAccordion();
                    Thread.sleep(2000);
                    createManageLeaves.clickCanEmpApplyForMoreThanTheirAvailableLveBalYesButton();
                    setOptionsForCanEmpApplyMoreThanAvbLveBalance();
                } else if (OverUtilization.toLowerCase()=="no"){
                    Reporter("Over Utilization is Set to NO", "Ok");
                }
            }
            return true;
        } catch (Exception e) {
            Reporter("Exception in Setting Accordion For Leave Policy", "Error");
            throw e;
        }

    }


    //selects options for Accordion
    //Can Emplyoee Apply More Than Available Leave Balance
    //based on Input Field  Accordion Options in EXCEL Sheet
    public void setOptionsForCanEmpApplyMoreThanAvbLveBalance() {
        String radioButtonOption = data.get("Options").split(",")[0];

        if (radioButtonOption.equalsIgnoreCase("count excess leave as paid by default")) {
            createManageLeaves.clickCountExcessLeaveAsPaidRadioButton();
        } else if (radioButtonOption.equalsIgnoreCase("count excess leave as unpaid by default")) {
            createManageLeaves.clickCountExcessLeaveAsUnPaidRadioButton();
        } else if (radioButtonOption.equalsIgnoreCase("utilize from")) {
            createManageLeaves.clickUtilizeFromRadioButton();
            createManageLeaves.selectOverUtilizationPolicyDropDown(data.get("utilize from policy"));

        }

        if (data.get("Accordion Options").split(",").length == 2) {
            String ChckBox = data.get("Accordion Options").split(",")[1];
            if (ChckBox.equalsIgnoreCase("dont allow leave more than yearly allocation")) {
                createManageLeaves.clickDontAllowLeaveMoreThanYearlyAllocationChkBox();
            } else if (ChckBox.equalsIgnoreCase("dont allow leave more than yearly accrual")) {
                createManageLeaves.clickDontAllowMoreThanYearlyAccuralChkBox();
            }
        }

        if (data.get("Accordion Options").split(",").length == 3 && data.get("Accordion Options").split(",")[1].equalsIgnoreCase("dont allow leave more than yearly allocation")
                && data.get("Accordion Options").split(",")[2].equalsIgnoreCase("dont allow leave more than yearly accrual")) {

            {
                createManageLeaves.clickDontAllowLeaveMoreThanYearlyAllocationChkBox();
                createManageLeaves.clickDontAllowMoreThanYearlyAccuralChkBox();
            }

        }
        createManageLeaves.typeMaxLeaveOverUtilizationTxtBox(data.get("Max Leave Overutilization"));

    }

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
            Leave_Type = getData("Leave_Type");
            Max_Leaves_Allowed_Per_Year = getData("Max_Leaves_Allowed_Per_Year");
            try {
                Max_Leaves_Allowed_Per_Month = getData("Max_Leaves_Allowed_Per_Month");
                Min_Consecutive_Days_in_Single_Application = getData("Minimum consecutive days allowed in a single application");
                Max_Consecutive_Days_in_Single_Application = getData("Maximum consecutive days allowed in a single application");
                Consecutive_Days_Allowed = getData("Consecutive leave allowed");
                Restrict_To_WeekDay = getData("Restrict to Week Days");
            } catch (Exception e) {
                //not throwing an exception because the field is not mandatory
            }
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

            LeaveAccrualBasedOnWorkingDays = getData("Leave Accrual based on Working days");
            CountPresentDays = getData("Count Present Days");
            CountAbsentDays = getData("Count Absent Days");
            CountWeeklyoffDays = getData("Count Weeklyoff Days");
            CountHolidayDays = getData("Count Holiday Days");
            CountOptionalHolidayDays = getData("Count Optional Holiday Days");
            CountLeaveDays = getData("Count Leave Days");
            WorkingDaysEndOfMonth = getData("End of Month");
            WorkingDaysEndOfQuarter = getData("End of Quarter");
            WorkingDaysEndOfBiannual = getData("End Of Biannual");
            EndOfYear = getData("End of Year");

            CreditOnTenureBasis = getData("Credit On Tenure Basis");
            CreditFromYear = getData("Credit From Year");
            CreditToYear = getData("Credit To Year");
            CreditNoOfLeaves = getData("Credit No of Leaves");

            CarryForward = getData("Carry forward");
            CarryForwardAllOrFixedOrPercentage = getData("Carry forward All/Fixed/Percentage");
            FixedOrPercentageValue = getData("Fixed/Percentage value");

            MultipleAllotmentEnabled = getData("Use Multiple allotment restrictions");
            MultipleAllotmentRestrictions = getData("Multiple Allotment Restrictions");
            MultipleAllotmentLeaves = getData("Alloted Leaves");

            OverUtilization = getData("OverUilization");

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
            if (Pro_rata.equalsIgnoreCase("Yes") && Pro_rata != null) {
                LeaveScenario.append(",\nProbation Status: ");
                if (Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveScenario.append("Start calculating leaves from joining date");
                } else if (Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    LeaveScenario.append("Start calculating leaves after probation period");
                }

                if (Half_Month_Leaves_if_employee_joins_after_15th != null) {
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
            }
            LeaveScenario.append(",\nAccrual: " + Accrual);
            if (Accrual.equalsIgnoreCase("Yes")) {
                LeaveScenario.append(",\nAccrual time frame: ");
                if ((LeaveAccrualBasedOnWorkingDays != null)
                        && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                    LeaveScenario.append("Based on Working Days with: ");
                    if (CountPresentDays.equalsIgnoreCase("Yes")) {
                        LeaveScenario.append("Present Days,");
                    }
                    if (CountAbsentDays.equalsIgnoreCase("Yes")) {
                        LeaveScenario.append("Absent Days,");
                    }
                    if (CountWeeklyoffDays.equalsIgnoreCase("Yes")) {
                        LeaveScenario.append("Weekly Off Days,");
                    }
                    if (CountHolidayDays.equalsIgnoreCase("Yes")) {
                        LeaveScenario.append("Holiday Days,");
                    }
                    if (CountOptionalHolidayDays.equalsIgnoreCase("Yes")) {
                        LeaveScenario.append("Optional Holiday Days,");
                    }
                    if (CountLeaveDays.equalsIgnoreCase("Yes")) {
                        LeaveScenario.append("Leave Days");
                    }
                    if (EndOfYear.equalsIgnoreCase("Yes")) {
                        LeaveScenario.append(",Accrual Point: End of Year");
                    }
                } else if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
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
                    Reporter("Accrual Time frame selected is not proper.", "Fail");
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

            if (CarryForward != null) {
                if (CarryForward.equalsIgnoreCase("Yes")) {
                    LeaveScenario.append(",\nCarry Forward: " + CarryForwardAllOrFixedOrPercentage + " leaves");
                    if (CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("Fixed") ||
                            CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("Percentage")) {
                        LeaveScenario.append("," + CarryForwardAllOrFixedOrPercentage + ": " + FixedOrPercentageValue);
                    }
                }
            }

            if (MultipleAllotmentEnabled != null) {
                if (MultipleAllotmentEnabled.equalsIgnoreCase("Yes")) {
                    LeaveScenario.append(".\nMultiple allotment restrictions: " + MultipleAllotmentEnabled);
                    LeaveScenario.append(", Restrictions: " + MultipleAllotmentRestrictions);
                    LeaveScenario.append(". Allotted leaves: " + MultipleAllotmentLeaves + " respectively");
                }
            }

            ReporterForCodeBlock(LeaveScenario.toString(), "Info");
            // writeInLeaveScenarioToExcel("Leave_Sce_Des", LeaveScenario.toString(),
            // DateTimeHelper.getCurrentLocalDateAndTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInLeaveScenarioToExcel(String sheetName, String LeaveScenario, String CurrentDateTime) {

        String[] dataToWrite = new String[4];
        dataToWrite[0] = LeaveScenario;
        dataToWrite[1] = CurrentDateTime;
        dataToWrite[2] = Leave_Type;

        try {
            ExcelWriter.writeToExcel(TestBase.resultsDir, "ExportExcel.xlsx", sheetName, dataToWrite);
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
                //Leave_Type = Leave_Type + objGenHelper.getRandomNumber(100);
                Leave_Type = Leave_Type;
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
    public boolean deleteAllLeaveType() {
        try {
            leaveSettings.clickManageLeavePolicies();
            String leaveTypeNameXpath = "//*[contains(@id,'leaveContainerModal')][text()='" + "L" + "']";
            List<WebElement> leaveTypeNameList = driver.findElements(By.xpath(leaveTypeNameXpath));
            String leaveTypeNameDeleteButtonXpath = "//*[contains(@id,'leaveContainerModal')][contains(text(),'" + "LTI"
                    + "')]/following::a[contains(@id, 'delete-leave')]";
            List<WebElement> leaveTypeNameDeleteButtonList = driver
                    .findElements(By.xpath(leaveTypeNameDeleteButtonXpath));
            int i = leaveTypeNameDeleteButtonList.size();
            System.out.println("i--->" + i);
            leaveTypeNameDeleteButtonList.get(0).click();
            objAlertHelper.acceptAlert();
            Thread.sleep(3000);
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
    public boolean editLeaveType() {
        try {
            String leaveTypeNameEditButtonXpath = "//*[contains(@id,'leaveContainerModal')][contains(text(),'"
                    + Leave_Type + "')]/following::a[contains(@id, 'editleave')]/i";
            WebElement leaveTypeEditButton = driver.findElement(By.xpath(leaveTypeNameEditButtonXpath));

            objJavaScrHelper.scrollToElement(driver, leaveTypeEditButton, "Leave edit button");
            objJavaScrHelper.customScrollVertically("-20");
            objWait.waitElementToBeClickable(leaveTypeEditButton);
            objGenHelper.elementClick(leaveTypeEditButton, "Leave edit button");
            objActionHelper.pause(driver, 5000);

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
    public boolean checkIfLeaveTypeIsPresent() {
        try {
            String leaveTypeNameXpath = "//*[contains(@id,'leaveContainerModal')][text() = '" + Leave_Type + "']";
            WebElement leaveTypeName = driver.findElement(By.xpath(leaveTypeNameXpath));
            return objGenHelper.checkVisbilityOfElement(leaveTypeName, Leave_Type + " is present");
        } catch (NoSuchElementException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while checking already present same leave type", "Error");
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
                        DateTimeHelper.getCurrentLocalDateAndTime());
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

    public boolean setMultipleAllotmentScenario() {
        try {
            if (MultipleAllotmentEnabled != null && MultipleAllotmentEnabled.equalsIgnoreCase("Yes")) {
                createManageLeaves.toggleMultipleAllotmentLeaveCheckBox("Enable");
                List<String> multipleAllotmentRestrictionsList = Arrays.asList(MultipleAllotmentRestrictions.split(","));
                List<String> allotedLeavesList = Arrays.asList(MultipleAllotmentLeaves.split(","));

                if (allotedLeavesList.size() == multipleAllotmentRestrictionsList.size()) {
                    for (int i = 0; i < allotedLeavesList.size(); i++) {
                        if (i > 0) {
                            createManageLeaves.clickAddNewMultipleAllotmentRestrictionButton();
                        }
                        createManageLeaves.insertRestrictionInMultipleAllotmentElasticSearch(multipleAllotmentRestrictionsList.get(i), i);
                        createManageLeaves.insertMultipleAllotmentLeavesTextBox(allotedLeavesList.get(i), i);
                    }
                } else {
                    Reporter("Restriction and Leaves alloted size is not equal", "Fail");
                    throw new RuntimeException("Restriction and Leaves alloted size is not equal");
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while setting Multiple Allotment scenario", "Error");
            return false;
        }
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

                if (Half_Month_Leaves_if_employee_joins_after_15th != null) {
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
                }
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
     * This method set Credit on Accrual Basis Scenario
     *
     * @return
     */
    public boolean setCreditOnAccrualBasis() {
        try {
            if (Accrual.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCreditOnAccrualBasisAccordion();
                createManageLeaves.clickCreditOnAccrualBasisYesRadioButton();

                if (LeaveAccrualBasedOnWorkingDays != null && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                    Monthly = "No";
                    Quarterly = "No";
                    Biannually = "No";
                    Begin_of_monthORQuarter = "No";
                    End_of_monthORQuarter = "No";

                    if (WorkingDaysEndOfMonth.equalsIgnoreCase("Yes")) {
                        Monthly = "Yes";
                        Quarterly = "No";
                        Biannually = "No";
                        Begin_of_monthORQuarter = "No";
                        End_of_monthORQuarter = "Yes";
                    } else if (WorkingDaysEndOfQuarter.equalsIgnoreCase("Yes")) {
                        Monthly = "No";
                        Quarterly = "Yes";
                        Biannually = "No";
                        Begin_of_monthORQuarter = "No";
                        End_of_monthORQuarter = "Yes";
                    } else {
                        Monthly = "Yes";
                        Quarterly = "No";
                        Biannually = "No";
                        Begin_of_monthORQuarter = "Yes";
                        End_of_monthORQuarter = "No";
                    }
                }

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

                if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                        && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                    setCreditOnAccrualBasisWorkingDaysScenario();
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
     * This method set Credit on Accrual Basis Working Days Scenario
     *
     * @return
     */
    public boolean setCreditOnAccrualBasisWorkingDaysScenario() {
        try {
            createManageLeaves.clickLeaveAccrualBasedOnWorkingDaysCheckBox();

            if (CountPresentDays.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCountPresentDaysCheckBox();
            }
            if (CountAbsentDays.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCountAbsentDaysCheckBox();
            }
            if (CountWeeklyoffDays.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCountWeeklyOffDaysCheckBox();
            }
            if (CountHolidayDays.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCountHolidayDaysCheckBox();
            }
            if (CountOptionalHolidayDays.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCountOptionalHolidayDaysCheckBox();
            }
            if (CountLeaveDays.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCountLeaveDaysCheckBox();
            }

            if (EndOfYear.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickEndOfYearCheckBox();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while setting Working Days scenario. ", "Error");
            return false;
        }
    }

    /**
     * This method clicks on End Of Year Checkbox
     *
     * @return
     */
    public boolean editLeaveAndClickEndOfYearAccrualCheckbox() {
        try {

            objJavaScrHelper.customScrollVertically("20");
            objJavaScrHelper.scrollToElement(driver,
                    createManageLeaves.getWebElementDontShowApplyInProbationPeriodCheckbox(),
                    "Dont show & apply in Probation Period Checkbox");
            if (Accrual.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCreditOnAccrualBasisAccordion();
                createManageLeaves.clickEndOfYearCheckBox();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method sets carry forward scenario
     *
     * @return boolean
     */
    public boolean setCarryForwardScenario() {
        try {
            if (CarryForward != null && CarryForward.equalsIgnoreCase("Yes")) {
                createManageLeaves.clickCarryForwardAccordion();
                createManageLeaves.clickCarryForwardAccordionYesButton();

                if (CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("All")) {
                    createManageLeaves.clickCarryForwardAllUnusedLeavesRadioButton();
                } else {
                    createManageLeaves.clickCarryForwardOnlyRadioButton();
                    if (CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("Fixed")) {
                        createManageLeaves.selectFromCarryForwardPolicyUnusedCarryAmountTypeDropdown("Fixed");
                    } else if (CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("Percentage")) {
                        createManageLeaves.selectFromCarryForwardPolicyUnusedCarryAmountTypeDropdown("Percentage");
                    } else {
                        throw new RuntimeException("Carry forward scenario test data is not correct");
                    }
                    createManageLeaves.insertUnusedCarryoverAmountTextbox(FixedOrPercentageValue);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception in setting Carry Forward Balance", "Error");
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

            setMultipleAllotmentScenario();
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
                    if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")) {
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
     * This method sets Mandatory Leave scenarios,also included custom fields with null check
     *
     * @return
     */
    public boolean setMandatoryLeaveScenarios() {
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

            if (Max_Leaves_Allowed_Per_Month != null) {
                createManageLeaves.insertMaximumLeavesAllowedPerMonth(Max_Leaves_Allowed_Per_Month);
            }

            if (Min_Consecutive_Days_in_Single_Application != null) {
                createManageLeaves.insertMimConsecutiveDaysAllowedInSingleApplication(Min_Consecutive_Days_in_Single_Application);
            }

            if (Max_Consecutive_Days_in_Single_Application != null) {
                createManageLeaves.insertMaxConsecutiveDaysAllowedInSingleApplication(Max_Consecutive_Days_in_Single_Application);
            }

            if (Consecutive_Days_Allowed != null) {
                createManageLeaves.inertConsecitiveDaysAllowed(Consecutive_Days_Allowed);
            }

            if (Restrict_To_WeekDay != null && !Restrict_To_WeekDay.equalsIgnoreCase("na")) {
                createManageLeaves.insertRestrictToWeekDaysDropDown(Restrict_To_WeekDay.split(","));
            }


            createManageLeaves.selectLeaveCycleDropdown(Leave_Cycle);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while creating mandatory Leave scenarios", "Error");
            return false;
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

                int i = 0;
                int flag = 1;
                while (flag == 1) {
                    String CreditFromYearVar = creditFromYearListitr.next();
                    String CreditToYearVar = creditToYearListitr.next();
                    String CreditNoOfLeavesVar = creditNoOfLeavesListitr.next();

                    createManageLeaves.selectCreditOnTenureBasisFromYearFromDropdown(CreditFromYearVar, i);
                    createManageLeaves.selectCreditOnTenureBasisToYearFromDropdown(CreditToYearVar, i);
                    createManageLeaves.insertCreditOnTenureBasisNumberOfLeavesTextBox(CreditNoOfLeavesVar, i);

                    if (creditFromYearListitr.hasNext() && creditToYearListitr.hasNext()
                            && creditNoOfLeavesListitr.hasNext()) {
                        createManageLeaves.clickCreditOnTenureBasisAddNewIcon();
                        i++;
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
     * This method runs Carry forward cron
     *
     * @return Leave Balance
     * @author shikhar
     */
    public boolean runCarryFrowardCronByEndPointURL() {
             double actualLeaveBalance = 0;
            String applicationURL = data.get("@@url");
            String URL = UtilityHelper.getProperty("allAPIRepository", "Run.Cron.API") + "CarryforwardLeavesnew"+"&type=2&eno="+EMPID;
            objUtil.getHTMLTextFromAPI(driver, URL);
            String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
            if (frontEndLeaveBalance.isEmpty()) {
                Reporter("Carry Forward cron has not ran successfully", "Error");
                throw new RuntimeException("Carry Forward cron has not ran successfully");
            }
            //actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            if(frontEndLeaveBalance.toLowerCase().contains("job finished")){
                Reporter("Cron for Carry Forward is Run Successfully", "INFO");
                return true;
            }
            else{
                Reporter("Exception while running cron for carry forward", "Error");
                return false;
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
            int year = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";
            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate.parse(calendarYearEndDate);
            LocalDate today = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            int previousYear = year - 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    leaveCycleStartDate = previousYear + "-" + "04" + "-" + "01";
                } else {
                    leaveCycleStartDate = today.getYear() + "-" + "04" + "-" + "01";
                }
            } else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleStartDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear() + "-" + "01" + "-"
                        + "01";
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
            int year = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";
            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate.parse(calendarYearEndDate);
            LocalDate today = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            int nextYear = year + 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    leaveCycleEndDate = today.getYear() + "-" + "03" + "-" + "31";
                } else {
                    leaveCycleEndDate = nextYear + "-" + "03" + "-" + "31";
                }
            } else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleEndDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear() + "-" + "12" + "-"
                        + "31";
            }
            return leaveCycleEndDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This method returns first day of Leave Cycle provided
     *
     * @param leaveCycle
     * @return String leaveCycleStartDate
     */
    public String getFirstDayofLeaveCycle(String leaveCycle, String calculationDate) {
        try {
            String leaveCycleStartDate = "";
            int year = LocalDate.parse(calculationDate).getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";
            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate today = LocalDate.parse(calculationDate);
            int previousYear = year - 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(today)
                        || financialYearEndDateInDateFormat.isEqual(today)) {
                    leaveCycleStartDate = previousYear + "-" + "04" + "-" + "01";
                } else {
                    leaveCycleStartDate = today.getYear() + "-" + "04" + "-" + "01";
                }
            } else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleStartDate = today.getYear() + "-" + "01" + "-" + "01";
            }
            return leaveCycleStartDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
    // public void writeLeavesResultToHmap(String DateOfJoining, double
    // expectedBalance, double actualBalance,
    // String result, String dateTime) {
    //
    // String[] dataToWrite = new String[6];
    // dataToWrite[0] = LeaveScenario.toString();
    // dataToWrite[1] = DateOfJoining;
    // dataToWrite[2] = String.valueOf(expectedBalance);
    // dataToWrite[3] = String.valueOf(actualBalance);
    // dataToWrite[4] = result;
    // dataToWrite[5] = dateTime;
    //
    // HashMap<String, String> excelHmap = new HashMap<>();
    //
    // try {
    // ExcelWriter.writeToExcel(TestBase.resultsDIR, "ExportExcel.xlsx",
    // "Leave_Balance", dataToWrite);
    // } catch (IOException e) {
    // e.printStackTrace();
    // } catch (Exception e) {
    // Reporter("Exception while sending data to Excel", "Error");
    // }
    // }

    /**
     * This method returns last day of Leave Cycle
     *
     * @param leaveCycle
     * @return String leaveCycleEndDate
     */
    public String getLastDayofLeaveCycle(String leaveCycle, String calculationDate) {
        try {
            String leaveCycleEndDate = "";

            int year = LocalDate.parse(calculationDate).getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";
            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate.parse(calendarYearEndDate);
            LocalDate today = LocalDate.parse(calculationDate);
            int nextYear = year + 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(today)
                        || financialYearEndDateInDateFormat.isEqual(today)) {
                    leaveCycleEndDate = today.getYear() + "-" + "03" + "-" + "31";
                } else {
                    leaveCycleEndDate = nextYear + "-" + "03" + "-" + "31";
                }
            } else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleEndDate = today.getYear() + "-" + "12" + "-" + "31";
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
    public boolean verifyDeactivationLeaveBalanceForFourEdgeDaysAndDeactivationAsCustomDate(
            String[] deactivationDateArray) {
        try {
            int testFlag = 0;
            double expectedBalance = 0;
            int arraysize = deactivationDateArray.length;
            int j = 0;
            String result = "";
            int flag = 0;

            while (j < arraysize) {
                String deactivationDate = deactivationDateArray[j];
                String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
                String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
                LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);

                LocalDate deactivationIterationDate = LocalDate.parse(deactivationDate);

                Reporter("Leave Balance will be checked for Decativate date '" + deactivationIterationDate.toString()
                        + "'", "Info");
                Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                        + " hence leave balance will be verifed from leave cycle end date '" + leaveCycleEndDate
                        + "' till leave cycle start date '" + leaveCycleStartDate, "Info");

                LocalDate iterationDate = deactivationIterationDate;
                int i = 1;
                while (iterationDate.isAfter(leaveCycleStartDateInDateFormat.minusMonths(18))) {
                    iterationDate = deactivationIterationDate.minusDays(i);

                    if (iterationDate.isEqual(LocalDate.parse("2017-03-31"))) {
                        testFlag = 1;
                    }
//					 && testFlag == 1

                    if ((iterationDateFourTimesPerMonth(iterationDate) == true) && testFlag == 1) {
                        DateOfJoining = changeEmployeeDOJ(iterationDate);
                        if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                            expectedBalance = calculateDeactivationLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                    deactivationIterationDate.toString());
                        } else {
                            expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                    deactivationIterationDate.toString(), "Before");
                        }
                        expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

                        double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                                deactivationIterationDate.toString());
                        if (expectedBalance != actualBalance) {
                            Reporter(
                                    "Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                            + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                            + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                    "Fail");
                            result = "Fail";
                            flag++;
                        } else {
                            Reporter(
                                    "Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                            + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                            + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                    "Pass");
                            result = "Pass";
                        }

                        if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                            writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(), DateOfJoining,
                                    expectedBalance, actualBalance, result,
                                    DateTimeHelper.getCurrentLocalDateAndTime());
                        }
                    }
                    i++;
                }
                j++;
            }
            return flag <= 0;

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
    public boolean verifyEffectiveDateDeactivationLeaveBalanceAsCustomDate(String effectiveDate,
                                                                           String[] deactivationDateArray) {
        try {
            int testFlag = 0;
            double expectedBalance = 0;
            int arraysize = deactivationDateArray.length;
            int j = 0;
            String result = "";
            int flag = 0;

            while (j < arraysize) {
                String deactivationDate = deactivationDateArray[j];
                String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
                String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
                LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                LocalDate leaveCycleEndDateInDateFormat = LocalDate.parse(leaveCycleEndDate);

                LocalDate deactivationIterationDate = LocalDate.parse(deactivationDate);

                Reporter("Leave Balance will be checked for Decativate date '" + deactivationIterationDate.toString()
                        + "'", "Info");
                Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                        + " hence leave balance will be verifed from leave cycle end date '" + leaveCycleEndDate
                        + "' till leave cycle start date '" + leaveCycleStartDate, "Info");

                LocalDate iterationDate = deactivationIterationDate;
                int i = 1;
//				while (iterationDate.isBefore(leaveCycleEndDateInDateFormat)) {
//					iterationDate = deactivationIterationDate.minusDays(-i);

//					 if(iterationDate.isEqual(LocalDate.parse("2017-03-31"))){
//					 testFlag = 1;
//					 }
//					 && testFlag == 1

//					if ((iterationDateFourTimesPerMonth(iterationDate) == true)) {
//						DateOfJoining = changeEmployeeDOJ(iterationDate);
                DateOfJoining = "2018-04-02";
                if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                        && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                    expectedBalance = calculateDeactivationLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                            deactivationIterationDate.toString());
                } else {
                    expectedBalance = calculateEffectiveDateDeactivationLeaveBalance(DateOfJoining,
                            deactivationIterationDate.toString(), "Before", effectiveDate);
                }
                expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

                double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                        deactivationIterationDate.toString());
                if (expectedBalance != actualBalance) {
                    Reporter(
                            "Failed||" + "Effective Date '" + effectiveDate + "'||" + "Expected Leave Balance="
                                    + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                    + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                            "Fail");
                    result = "Fail";
                    flag++;
                } else {
                    Reporter(
                            "Passed||" + "Effective Date '" + effectiveDate + "'||" + "Expected Leave Balance="
                                    + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                    + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                            "Pass");
                    result = "Pass";
                }

                if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                    writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(), DateOfJoining,
                            expectedBalance, actualBalance, result,
                            DateTimeHelper.getCurrentLocalDateAndTime());
                }
//					}
//					i++;
//				}
                j++;
            }
            return flag <= 0;

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
    public boolean verifyEffectiveDeactivationLeaveBalanceForWholeLeaveCycleForFourEdgeDays() {
        try {
            String result = "";
            int flag = 0;
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
            LocalDate leaveCycleEndDateInDateFormat = LocalDate.parse(leaveCycleEndDate);

            LocalDate deactivationIterationDate = leaveCycleEndDateInDateFormat;
            // LocalDate deactivationIterationDate =
            // LocalDate.parse(DateTimeHelper.getCurrentLocalDate());

            int j = 0;
            while (deactivationIterationDate.isAfter(leaveCycleStartDateInDateFormat.minusMonths(-6))) {
                deactivationIterationDate = leaveCycleEndDateInDateFormat.minusMonths(3).minusDays(j);
                if (iterationDateTwoTimesPerMonth(deactivationIterationDate) == true) {
                    Reporter("Leave Balance will be checked for Decativate date '"
                            + deactivationIterationDate.toString() + "'", "Info");
                    Reporter(
                            "Leave Cycle defined is '" + Leave_Cycle + "',"
                                    + " hence leave balance will be verifed from leave cycle end date '"
                                    + leaveCycleEndDate + "' till leave cycle start date '" + leaveCycleStartDate,
                            "Info");

                    LocalDate iterationDate = deactivationIterationDate;
                    int i = 1;
                    while (iterationDate.isAfter(leaveCycleStartDateInDateFormat.minusMonths(6))) {
                        iterationDate = deactivationIterationDate.minusDays(i);

                        if ((iterationDateTwoTimesPerMonth(iterationDate) == true)) {
//							DateOfJoining = changeEmployeeDOJ(iterationDate);
                            DateOfJoining = "2018-04-02";
                            double expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                    deactivationIterationDate.toString(), "Before");
                            double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                                    deactivationIterationDate.toString());
                            if (expectedBalance != actualBalance) {
                                Reporter(
                                        "Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                                + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                        "Fail");
                                result = "Fail";
                                flag++;
                            } else {
                                Reporter(
                                        "Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                                + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                        "Pass");
                                result = "Pass";
                            }

                            if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(),
                                        DateOfJoining, expectedBalance, actualBalance, result,
                                        DateTimeHelper.getCurrentLocalDateAndTime());
                            }
                        }
                        i++;
                    }
                }
                j++;
            }
            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }
    }

    public boolean iterationDateFourTimesPerMonth(LocalDate iterationDate) {
        LocalDate todaysDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
        LocalDate lastDayOfIterationMonth = iterationDate.withDayOfMonth(iterationDate.lengthOfMonth());
        LocalDate firstDayOfIterationMonth = iterationDate.with(firstDayOfMonth());
        LocalDate fifteenThOfIterationMonth = iterationDate.withDayOfMonth(15);
        LocalDate sixteenThOfIterationMonth = iterationDate.withDayOfMonth(16);

        if (iterationDate.lengthOfMonth() == 29) {
            lastDayOfIterationMonth = iterationDate.withDayOfMonth(28);
        }

        return iterationDate.equals(todaysDate) || iterationDate.equals(lastDayOfIterationMonth)
                || iterationDate.equals(firstDayOfIterationMonth) || iterationDate.equals(fifteenThOfIterationMonth)
                || iterationDate.equals(sixteenThOfIterationMonth);
    }

    public boolean iterationDateFourTimesPerMonth(LocalDate iterationDate, String excludeLastDay) {
        LocalDate todaysDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
        LocalDate lastDayOfIterationMonth = iterationDate.withDayOfMonth(iterationDate.lengthOfMonth());
        if (excludeLastDay.equalsIgnoreCase("Yes")) {
            lastDayOfIterationMonth = iterationDate.withDayOfMonth(iterationDate.lengthOfMonth()).minusDays(1);
        }
        LocalDate firstDayOfIterationMonth = iterationDate.with(firstDayOfMonth());
        LocalDate fifteenThOfIterationMonth = iterationDate.withDayOfMonth(15);
        LocalDate sixteenThOfIterationMonth = iterationDate.withDayOfMonth(16);

        if (iterationDate.lengthOfMonth() == 29) {
            lastDayOfIterationMonth = iterationDate.withDayOfMonth(28);
        }

        return iterationDate.equals(todaysDate) || iterationDate.equals(lastDayOfIterationMonth)
                || iterationDate.equals(firstDayOfIterationMonth) || iterationDate.equals(fifteenThOfIterationMonth)
                || iterationDate.equals(sixteenThOfIterationMonth);
    }


    @SuppressWarnings("unused")
    public boolean iterationDateTwoTimesPerMonth(LocalDate iterationDate) {
        LocalDate todaysDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
        LocalDate lastDayOfIterationMonth = iterationDate.withDayOfMonth(iterationDate.lengthOfMonth());
        LocalDate firstDayOfIterationMonth = iterationDate.with(firstDayOfMonth());
        LocalDate fifteenThOfIterationMonth = iterationDate.withDayOfMonth(15);
        LocalDate sixteenThOfIterationMonth = iterationDate.withDayOfMonth(16);

        if (iterationDate.lengthOfMonth() == 29) {
            lastDayOfIterationMonth = iterationDate.withDayOfMonth(28);
        }

        return iterationDate.equals(todaysDate) || iterationDate.equals(fifteenThOfIterationMonth)
                || iterationDate.equals(sixteenThOfIterationMonth);
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
            ExcelWriter.writeToExcel(TestBase.resultsDir, "ExportExcel.xlsx", "Leave_Balance", dataToWrite);
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
    public void writeDeactivationLeavesResultToExcel(String deactivationDate, String DateOfJoining,
                                                     double expectedBalance, double actualBalance, String result, String dateTime) {

        String[] dataToWrite = new String[8];
        dataToWrite[0] = LeaveScenario.toString();
        dataToWrite[1] = deactivationDate;
        dataToWrite[2] = DateOfJoining;
        dataToWrite[3] = String.valueOf(expectedBalance);
        dataToWrite[4] = String.valueOf(actualBalance);
        dataToWrite[5] = result;
        dataToWrite[6] = dateTime;

        try {
            ExcelWriter.writeToExcel(TestBase.resultsDir, "ExportExcel.xlsx", "Deactivation_Leave_Balance",
                    dataToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            ExcelWriter.writeToExcel(TestBase.resultsDir, "ExportExcel.xlsx", "Effective Date", dataToWrite);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Reporter("Exception while sending data to Excel", "Error");
        }
    }

    /**
     * This method calculate leave balance
     *
     * @param DOJ
     * @return double
     */
    public double calculateLeaveBalance(String DOJ) {
        try {
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle); //Get first day of Leave cycle
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle); //Get last day of Leave cycle
            double midJoinigYesLeaves = 0;
            double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
            double perMonthOrQuarterLeaves = 0;
            double MonthOrQuarterDifference = 0;
            double leavesDiffFromFirstDayOfQuarter = 0;
            String leavesCalculationStartDate = "";
            String midYearEndDate;
            double biannualLeave = 0;

            //This checks whether employee is in probation period or outside. It also calculates Probation End Date/
            // Confirmation date of employee
            if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
                ExpectedLeaveBalance = 0; // If employee is under probation period his expected balance will be 0
            } else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {

                if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    DOJ = leaveCycleStartDate; //This sets DOJ for calculation as Leave Cycle start date even if his his actual DOJ is in past year
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    Leave_Probation_End_Date = leaveCycleStartDate; //This sets Probation end date for calculation as Leave Cycle start date even if his his actual Probation end date is in past year
                }

                /**
                 * This function sets Leave calculation starts date based on Pro Rata
                 */
                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ; //If Pro Rata is Yes and calculation is started from joining date, Leave calculation begin date is from employees date of joining
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    LeaveCalBeginningDate = Leave_Probation_End_Date; //If Pro Rata is Yes and calculation is started after probabtion period, Leave calculation begin date is from employees date of joining
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ; //If Pro Rata is No, Leave calculation begin date is from employees date of joining
                }

                /**
                 * This function takes care of all combination of Pro-Rata and accrual
                 */
                if (Pro_rata.equalsIgnoreCase("Yes")) {
                    if (Accrual.equalsIgnoreCase("Yes")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        MonthOrQuarterDifference = 12;
                    }
                }
                /*
                 * This function determines checks whether employee has joined before or after 15th of month and
                 * based on that sets midJoiningYesLeaves variable
                 */
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
                            // midoinigYesLeaves = 0;
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
                /**
                 * This function deals with all cases of Accrual Month, Quarter and Biannual
                 */
                if (Accrual.equalsIgnoreCase("Yes")) {
                    /*
                    This function calculates month leave balance
                     */
                    if (Monthly.equalsIgnoreCase("Yes") && Quarterly.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("No")) {
                        perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 12); //Per month leave is No. of Leaves divided by 12 months
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceFromCurrentDate(leavesCalculationStartDate); //This function calculates month difference between Current date and employees Date of Joining
                        leavesDiffFromFirstDayOfQuarter = 0;
                    }
                    /*
                    This function calculates quarter leave balance
                     */
                    else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("Yes")
                            && Biannually.equalsIgnoreCase("No")) {
                        perMonthOrQuarterLeaves = (Leaves_Allowed_Per_Year / 4); //Per Quarter leave is No. of Leaves divided by 4 months
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getQuarterDiffFromCurrentDate(leavesCalculationStartDate); //This function calculates quarter difference between Current date and employees Date of Joining
                        leavesDiffFromFirstDayOfQuarter = ((perMonthLeaves)
                                * objDateTimeHelper.getMonthDiffFromFirstDayOfQuarter(leavesCalculationStartDate)); //This function calculates month difference between date of joining and first day of quarter
                    }/*
                    This function calculates biannual leave balance
                     */ else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("Yes")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate); //This checks DOJ is in which binannual half
                        String currentDateBiannualHalf = checkBiannualHalfOfDate(
                                LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).toString()); //This checks current date is in which binannual half
                        midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString(); //Calculate biannual end date
                        String biannualEndDate = "";
                        /*
                       Below code assigns biannualEndDate as per biannual half
                         */
                        if (DOJBiannualHalf.equalsIgnoreCase("First")) {
                            biannualEndDate = midYearEndDate;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualEndDate = leaveCycleEndDate;
                        }
                        /*
                        Below code calculates month difference between leave calculation start date and biannual end date
                         */
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

                        /*
                        Below logic calculates whether to credit Halfl  year leave based on DOJ and Current date biannual half
                         */
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

                    /*
                    Below code adds 1 to no. of months multipler if needs to begin of month or quarter scenaro
                     */
                    if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("No")) {
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    }
                }
                /*
                Formula to calculate leave balance
                 */
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

            if (monthsDiff >= 6) {
                biannualHalf = "First";
            } else if (monthsDiff < 6) {
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
     * This method calculate and returns whether Date falls in First or Second
     * Biannual Half
     *
     * @param DATEIN_YYYY_MM_DD_format
     * @return
     */
    public String checkBiannualHalfOfDate(String DATEIN_YYYY_MM_DD_format, String customCurrentDate) {
        try {
            String biannualHalf = "";
            String employeeConfirmationdate = LocalDate.parse(DATEIN_YYYY_MM_DD_format).toString();
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle, customCurrentDate);
            double monthsDiff = objDateTimeHelper.getMonthDifferenceBetweenTwoDates(employeeConfirmationdate,
                    leaveCycleEndDate);

            if (monthsDiff >= 6) {
                biannualHalf = "First";
            } else if (monthsDiff < 6) {
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
            if (EndOfYear != null && EndOfYear.isEmpty() && EndOfYear.equalsIgnoreCase("Yes")) {
                return verifyEmployeeTenureBasedLeaveBalanceForFourEdgeDaysEndOfYear();
            } else {
                Reporter("System Date Time is set to : '" + DateTimeHelper.getCurrentLocalDateAndTime(), "Info");
                String result = "";
                int flag = 0;
                double expectedBalance = 0;
                double expectedLeaveBalanceBeforeAnniversary = 0;
                double expectedLeaveBalanceAfterAnniversary = 0;
                Long TempCreditFromYearLong;
                String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
                String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
                List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
                List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
                List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

                int anniversaryIterator = 0;
                while ((anniversaryIterator < creditFromYearList.size())
                        && (anniversaryIterator < creditToYearList.size())
                        && (anniversaryIterator < creditNoOfLeavesList.size())) {

                    String CreditFromYearVar;
                    String CreditToYearVar;
                    String CreditNoOfLeavesVar;

                    long CreditFromYearVarLong;
                    long CreditToYearVarLong;
                    long CreditNoOfLeavesVarLong;

                    CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                    CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                    CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                    CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                    CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                    CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                    if (anniversaryIterator > 0) {
                        Leaves_Allowed_Per_Year = Double
                                .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
                    }
                    int tenure = 0;
                    if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
                        LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                        LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

                        LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
                                .minusYears(CreditToYearVarLong - 1);

                        LocalDate lastDateForCalculation;
                        Long firstCreditFromYear = Long.valueOf(creditFromYearList.get(0).trim());
                        LocalDate tenureFirstLastDate = leaveCycleLastDateInDateFormat.minusYears(firstCreditFromYear);

                        if (anniversaryIterator == 0 && CreditFromYearVarLong != 1) {
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(1);
                        } else if (anniversaryIterator == creditFromYearList.size() - 1) {
                            startDateForCalculation = leaveCycleStartDateInDateFormat.minusYears(CreditToYearVarLong);
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                        } else {
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                        }

                        int anniversaryYear = anniversaryIterator + 1;

                        Reporter("Leaves will be calculated for ~" + anniversaryYear + "~ anniversary", "Info");
                        Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                                + " hence leave balance will be verifed from '" + lastDateForCalculation + "' till '"
                                + startDateForCalculation, "Info");

                        int i = 0;
                        LocalDate iterationDate = lastDateForCalculation;

                        TempCreditFromYearLong = CreditFromYearVarLong;

                        while ((iterationDate.isAfter(startDateForCalculation.minusDays(-1))
                                || iterationDate.isEqual(startDateForCalculation.minusDays(-1)))
                                && (iterationDate.isBefore(lastDateForCalculation.minusDays(-1))
                                || iterationDate.isEqual(lastDateForCalculation.minusDays(-1)))) {
                            iterationDate = lastDateForCalculation.minusDays(i);

                            // if (iterationDate.isEqual(LocalDate.parse("2015-07-15"))) {
                            // System.out.println("Stop");
                            // tenureDOJFlag = 1;
                            // }

                            LocalDate tenureLastDate = leaveCycleLastDateInDateFormat
                                    .minusYears(CreditFromYearVarLong + 1);
                            LocalDate tenureStartDate = leaveCycleStartDateInDateFormat
                                    .minusYears(CreditToYearVarLong - 1);

                            if ((iterationDate.isBefore(tenureLastDate) && iterationDate.isAfter(tenureStartDate))
                                    /* || iterationDate.isEqual(tenureStartDate) */ || iterationDate
                                    .isEqual(tenureLastDate)
                                    || iterationDate.isAfter(tenureFirstLastDate)) {
                                tenure = 1;
                            } else {
                                tenure = 0;
                            }

                            LocalDate lastDayOfIterationLeaveCycle = LocalDate
                                    .parse(getLastDayofLeaveCycle(Leave_Cycle, iterationDate.toString()));
                            if (iterationDate.isEqual(lastDayOfIterationLeaveCycle) && i != 0
                                    && iterationDate.isBefore(tenureFirstLastDate)) {
                                TempCreditFromYearLong = TempCreditFromYearLong + 1;
                                Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                // removeEmployeeLeaveLogs();
                            }
                            // && tenureDOJFlag == 1
                            if ((iterationDateFourTimesPerMonth(iterationDate) == true
                                    /* || tenureCurrentMonthOfAnyYear(iterationDate) == true */)) {
                                DateOfJoining = changeEmployeeDOJ(iterationDate);
                                // calculateLeaveBalanceAsPerEmployeeWorkingDays

                                LocalDate anniversaryDateInDateFormat = iterationDate
                                        .minusYears(-TempCreditFromYearLong);

                                if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                        && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {

                                    if (anniversaryDateInDateFormat
                                            .isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                        expectedBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                                DateTimeHelper.getCurrentLocalDate());
                                    } else if (tenure == 1) {
                                        expectedBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                                DateTimeHelper.getCurrentLocalDate());
                                    } else {

                                        String anniversaryDate = anniversaryDateInDateFormat.toString();
                                        String anniversaryDateMinusOne = anniversaryDateInDateFormat.minusDays(1)
                                                .toString();
                                        expectedLeaveBalanceBeforeAnniversary = calculateTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                DateOfJoining, anniversaryDateMinusOne, "Before");
                                        double temLeaveVar = Leaves_Allowed_Per_Year;
                                        Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                        if (anniversaryDateInDateFormat
                                                .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))
                                                || anniversaryDateInDateFormat.isEqual(
                                                LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                            expectedLeaveBalanceAfterAnniversary = calculateTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                    anniversaryDate, DateTimeHelper.getCurrentLocalDate(), "After");
                                        } else {
                                            expectedLeaveBalanceAfterAnniversary = 0;
                                        }
                                        Leaves_Allowed_Per_Year = temLeaveVar;
                                        expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                                + expectedLeaveBalanceAfterAnniversary;
                                    }
                                } else {
                                    if (anniversaryDateInDateFormat
                                            .isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                        expectedBalance = calculateLeaveBalance(DateOfJoining);
                                    } else if (tenure == 1) {
                                        expectedBalance = calculateLeaveBalance(DateOfJoining);
                                    } else {

                                        String anniversaryDate = anniversaryDateInDateFormat.toString();
                                        expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
                                                DateOfJoining, anniversaryDate, "Before");
                                        double temLeaveVar = Leaves_Allowed_Per_Year;
                                        Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                        if (anniversaryDateInDateFormat
                                                .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))
                                                || anniversaryDateInDateFormat.isEqual(
                                                LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                            expectedLeaveBalanceAfterAnniversary = calculateTenureBasedLeaveBalance(
                                                    anniversaryDate, DateTimeHelper.getCurrentLocalDate(), "After");
                                        } else {
                                            expectedLeaveBalanceAfterAnniversary = 0;
                                        }
                                        Leaves_Allowed_Per_Year = temLeaveVar;
                                        expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                                + expectedLeaveBalanceAfterAnniversary;
                                    }
                                }
                                expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

                                double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
                                if (expectedBalance != actualBalance) {
                                    Reporter(
                                            "Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                    + expectedBalance + "||Actual Leave Balance=" + actualBalance,
                                            "Fail");
                                    result = "Fail";
                                    flag++;
                                } else {
                                    Reporter(
                                            "Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                    + expectedBalance + "||Actual Leave Balance=" + actualBalance,
                                            "Pass");
                                    result = "Pass";
                                }

                                if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                    writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
                                            DateTimeHelper.getCurrentLocalDateAndTime());
                                }
                            }
                            i++;
                        }
                    } else {
                        Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                    }
                    anniversaryIterator++;
                }
                return flag <= 0;
            }
        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyEmployeeTenureBasedLeaveBalanceForFourEdgeDaysEndOfYear() {
        try {
            Reporter("System Date Time is set to : '" + DateTimeHelper.getCurrentLocalDateAndTime(), "Info");
            String result = "";
            int flag = 0;
            double expectedBalance = 0;
            double expectedLeaveBalanceBeforeAnniversary = 0;
            double expectedLeaveBalanceAfterAnniversary = 0;
            Long TempCreditFromYearLong;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
            List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
            List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
            List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

            int anniversaryIterator = 0;
            while ((anniversaryIterator < creditFromYearList.size()) && (anniversaryIterator < creditToYearList.size())
                    && (anniversaryIterator < creditNoOfLeavesList.size())) {

                String CreditFromYearVar;
                String CreditToYearVar;
                String CreditNoOfLeavesVar;

                long CreditFromYearVarLong;
                long CreditToYearVarLong;
                long CreditNoOfLeavesVarLong;

                CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                if (anniversaryIterator > 0) {
                    Leaves_Allowed_Per_Year = Double
                            .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
                }
                int tenure = 0;
                if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
                    LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                    LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

                    LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
                            .minusYears(CreditToYearVarLong - 1);

                    LocalDate lastDateForCalculation;
                    Long firstCreditFromYear = Long.valueOf(creditFromYearList.get(0).trim());
                    LocalDate tenureFirstLastDate = leaveCycleLastDateInDateFormat.minusYears(firstCreditFromYear);

                    if (anniversaryIterator == 0 && CreditFromYearVarLong != 1) {
                        lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(1);
                    } else if (anniversaryIterator == creditFromYearList.size() - 1) {
                        startDateForCalculation = leaveCycleStartDateInDateFormat.minusYears(CreditToYearVarLong);
                        lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                    } else {
                        lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                    }

                    int anniversaryYear = anniversaryIterator + 1;

                    Reporter("Leaves will be calculated for ~" + anniversaryYear + "~ anniversary", "Info");
                    Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                            + " hence leave balance will be verifed from '" + lastDateForCalculation + "' till '"
                            + startDateForCalculation, "Info");

                    int i = 0;
                    LocalDate iterationDate = lastDateForCalculation;

                    TempCreditFromYearLong = CreditFromYearVarLong;

                    while ((iterationDate.isAfter(startDateForCalculation.minusDays(-1))
                            || iterationDate.isEqual(startDateForCalculation.minusDays(-1)))
                            && (iterationDate.isBefore(lastDateForCalculation.minusDays(-1))
                            || iterationDate.isEqual(lastDateForCalculation.minusDays(-1)))) {
                        iterationDate = lastDateForCalculation.minusDays(i);

//						if (iterationDate.isEqual(LocalDate.parse("2017-01-01"))) {
//							System.out.println("Stop");
//							tenureDOJFlag = 1;
//						}

                        LocalDate tenureLastDate = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong + 1);
                        LocalDate tenureStartDate = leaveCycleStartDateInDateFormat.minusYears(CreditToYearVarLong - 1);

                        if ((iterationDate.isBefore(tenureLastDate) && iterationDate.isAfter(tenureStartDate))
                                /* || iterationDate.isEqual(tenureStartDate) */ || iterationDate.isEqual(tenureLastDate)
                                || iterationDate.isAfter(tenureFirstLastDate)) {
                            tenure = 1;
                        } else {
                            tenure = 0;
                        }

                        LocalDate lastDayOfIterationLeaveCycle = LocalDate
                                .parse(getLastDayofLeaveCycle(Leave_Cycle, iterationDate.toString()));
                        if (iterationDate.isEqual(lastDayOfIterationLeaveCycle) && i != 0
                                && iterationDate.isBefore(tenureFirstLastDate)) {
                            TempCreditFromYearLong = TempCreditFromYearLong + 1;
                            // Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                            // removeEmployeeLeaveLogs();
                        }
                        // && tenureDOJFlag == 1
                        if ((iterationDateFourTimesPerMonth(iterationDate) == true
                                /* || tenureCurrentMonthOfAnyYear(iterationDate) == true */)) {
                            DateOfJoining = changeEmployeeDOJ(iterationDate);
                            // calculateLeaveBalanceAsPerEmployeeWorkingDays
                            if (TempCreditFromYearLong > CreditFromYearVarLong
                                    && TempCreditFromYearLong < CreditToYearVarLong) {
                                TempCreditFromYearLong = CreditFromYearVarLong;
                            }
                            LocalDate anniversaryDateInDateFormat = iterationDate.minusYears(-TempCreditFromYearLong);

                            if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                    && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {

                                if (anniversaryDateInDateFormat
                                        .isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate())) && tenure != 1) {
                                    expectedBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                            DateTimeHelper.getCurrentLocalDate());
                                } /*
                                 * else if (tenure == 1) { expectedBalance =
                                 * calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                 * DateTimeHelper.getCurrentLocalDate()); }
                                 */ else {

                                    String anniversaryDate = anniversaryDateInDateFormat.toString();
                                    String anniversaryDateMinusOne = anniversaryDateInDateFormat.minusDays(1)
                                            .toString();
                                    expectedLeaveBalanceBeforeAnniversary = calculateTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                            DateOfJoining, anniversaryDateMinusOne, "Before");
                                    double temLeaveVar = Leaves_Allowed_Per_Year;
                                    Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                    if (anniversaryDateInDateFormat
                                            .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))
                                            || anniversaryDateInDateFormat
                                            .isEqual(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                        expectedLeaveBalanceAfterAnniversary = calculateTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                anniversaryDate, DateTimeHelper.getCurrentLocalDate(), "After");
                                    } else {
                                        expectedLeaveBalanceAfterAnniversary = 0;
                                    }
                                    Leaves_Allowed_Per_Year = temLeaveVar;
                                    expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                            + expectedLeaveBalanceAfterAnniversary;
                                }
                            } else {
                                if (anniversaryDateInDateFormat
                                        .isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                    expectedBalance = calculateLeaveBalance(DateOfJoining);
                                } else if (tenure == 1) {
                                    expectedBalance = calculateLeaveBalance(DateOfJoining);
                                } else {

                                    String anniversaryDate = anniversaryDateInDateFormat.toString();
                                    expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
                                            DateOfJoining, anniversaryDate, "Before");
                                    double temLeaveVar = Leaves_Allowed_Per_Year;
                                    Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                    if (anniversaryDateInDateFormat
                                            .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))
                                            || anniversaryDateInDateFormat
                                            .isEqual(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                        expectedLeaveBalanceAfterAnniversary = calculateTenureBasedLeaveBalance(
                                                anniversaryDate, DateTimeHelper.getCurrentLocalDate(), "After");
                                    } else {
                                        expectedLeaveBalanceAfterAnniversary = 0;
                                    }
                                    Leaves_Allowed_Per_Year = temLeaveVar;
                                    expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                            + expectedLeaveBalanceAfterAnniversary;
                                }
                            }
                            expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

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
                                        DateTimeHelper.getCurrentLocalDateAndTime());
                            }
                        }
                        i++;
                    }
                } else {
                    Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                }
                anniversaryIterator++;
            }
            return flag <= 0;

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
            double expectedLeaveBalanceBeforeAnniversary = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
            double expectedLeaveBalanceAfterAnniversary = 0;
            List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
            List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
            List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

            int anniversaryIterator = 0;
            while ((anniversaryIterator < creditFromYearList.size()) && (anniversaryIterator < creditToYearList.size())
                    && (anniversaryIterator < creditNoOfLeavesList.size())) {
                String CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                String CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                String CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                long CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                long CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                long CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                if (anniversaryIterator > 0) {
                    Leaves_Allowed_Per_Year = Double
                            .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
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

                        if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                            expectedLeaveBalanceBeforeAnniversary = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                    leaveCycleStartDate, anniversaryDate);
                            double temLeaveVar = Leaves_Allowed_Per_Year;
                            Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                            if (anniversaryDateInDateFormat
                                    .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                expectedLeaveBalanceAfterAnniversary = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                        anniversaryDate, DateTimeHelper.getCurrentLocalDate());
                            } else {
                                expectedLeaveBalanceAfterAnniversary = 0;
                            }
                            Leaves_Allowed_Per_Year = temLeaveVar;
                        } else {
                            expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
                                    leaveCycleStartDate, anniversaryDate, "Before");
                            double temLeaveVar = Leaves_Allowed_Per_Year;
                            Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                            if (anniversaryDateInDateFormat
                                    .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                expectedLeaveBalanceAfterAnniversary = calculateTenureBasedLeaveBalance(anniversaryDate,
                                        DateTimeHelper.getCurrentLocalDate(), "After");
                            } else {
                                expectedLeaveBalanceAfterAnniversary = 0;
                            }
                            Leaves_Allowed_Per_Year = temLeaveVar;
                        }

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
                                    DateTimeHelper.getCurrentLocalDateAndTime());
                        }
                    }
                    i++;
                } else {
                    Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                }
                anniversaryIterator++;
            }

            return flag <= 0;

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
    public boolean verifyCarryForwardInCaseOfTenureBasedLeaveBalance() {
        try {

            String result = "";
            int flag = 0;
            double expectedLeaveBalanceBeforeAnniversary = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
            double expectedLeaveBalanceAfterAnniversary = 0;
            double actualCarryForwardBalance = 0;
            double expectedCarryForwardBalance = 0;

            List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
            List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
            List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

            objDateTimeHelper.changeServerDate(driver, LocalDate.parse(leaveCycleLastDate).minusDays(-1).toString());
            int anniversaryIterator = 0;
            while ((anniversaryIterator < creditFromYearList.size()) && (anniversaryIterator < creditToYearList.size())
                    && (anniversaryIterator < creditNoOfLeavesList.size())) {
                String CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                String CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                String CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                long CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                long CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                long CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                if (anniversaryIterator > 0) {
                    Leaves_Allowed_Per_Year = Double
                            .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
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

                        if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                            expectedLeaveBalanceBeforeAnniversary = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                    leaveCycleStartDate, anniversaryDate);
                            double temLeaveVar = Leaves_Allowed_Per_Year;
                            Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                            if (anniversaryDateInDateFormat
                                    .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                expectedLeaveBalanceAfterAnniversary = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                        anniversaryDate, DateTimeHelper.getCurrentLocalDate());
                            } else {
                                expectedLeaveBalanceAfterAnniversary = 0;
                            }
                            Leaves_Allowed_Per_Year = temLeaveVar;
                        } else {
                            expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
                                    leaveCycleStartDate, anniversaryDate, "Before");
                            double temLeaveVar = Leaves_Allowed_Per_Year;
                            Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                            if (anniversaryDateInDateFormat
                                    .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                expectedLeaveBalanceAfterAnniversary = calculateTenureBasedLeaveBalance(anniversaryDate,
                                        DateTimeHelper.getCurrentLocalDate(), "After");
                            } else {
                                expectedLeaveBalanceAfterAnniversary = 0;
                            }
                            Leaves_Allowed_Per_Year = temLeaveVar;
                        }

                        double expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                + expectedLeaveBalanceAfterAnniversary;

                        expectedCarryForwardBalance = calculateCarryForwardBalance(expectedBalance);
                        expectedCarryForwardBalance = Math.round(expectedCarryForwardBalance * 100.0) / 100.0;
                        removeEmployeeCarryForwardLeaveLogs();
                        runCarryFrowardCronByEndPointURL();
                        actualCarryForwardBalance = getEmployeesFrontEndCarryForwardLeaveBalance(Leave_Type); //This gets employees leave balance from frontend
                    /*
                    In below code we are comparing calculated balance to actual balance shown in frontend
                     */
                        if (expectedCarryForwardBalance != actualCarryForwardBalance) {
                            Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                    + expectedCarryForwardBalance + "||Actual Leave Balance=" + actualCarryForwardBalance, "Fail");
                            result = "Fail";
                            flag++;
                        } else {
                            Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                    + expectedCarryForwardBalance + "||Actual Leave Balance=" + actualCarryForwardBalance, "Pass");
                            result = "Pass";
                        }

                        if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                            writeLeavesResultToExcel(DateOfJoining, expectedCarryForwardBalance, actualCarryForwardBalance, result,
                                    DateTimeHelper.getCurrentLocalDateAndTime());
                        }
                    }
                    i++;
                } else {
                    Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                }
                anniversaryIterator++;
            }

            return flag <= 0;

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
                String CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                String CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                String CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                long CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                long CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                long CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                if (anniversaryIterator > 0) {
                    Leaves_Allowed_Per_Year = Double
                            .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
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
                                    DateTimeHelper.getCurrentLocalDateAndTime());
                        }

                    }

                } else {
                    Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                }
                anniversaryIterator++;
            }

            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }
    }

    /**
     * This method calculate leave balance
     *
     * @param DOJ
     * @param LastDayOfCalculation
     * @param beforeAfter
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

            if (beforeAfter.equalsIgnoreCase("Before")) {
                beforeProbationFlag = checkDOJisUnderLeaveProbationPeriod(DOJ);
            }

            if (beforeProbationFlag == true) {
                ExpectedLeaveBalance = 0;

            } else if (beforeProbationFlag == false) {

                if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    DOJ = leaveCycleStartDate;
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    Leave_Probation_End_Date = leaveCycleStartDate;
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        LeaveCalBeginningDate = Leave_Probation_End_Date;
                    } else {
                        if (LocalDate.parse(Leave_Probation_End_Date).isAfter(LocalDate.parse(DOJ))) {
                            LeaveCalBeginningDate = Leave_Probation_End_Date;
                        } else {
                            LeaveCalBeginningDate = DOJ;
                        }
                    }
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }

                if (Pro_rata.equalsIgnoreCase("Yes")) {

                    if (Accrual.equalsIgnoreCase("Yes")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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

                        if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                                && Calculate_after_probation_period.equalsIgnoreCase("Yes") && LocalDate
                                .parse(Leave_Probation_End_Date).isAfter(LastDayOfCalculationInDateFormat)) {
                            MonthOrQuarterDifference = 0;
                        }
                    }

                } else if (Pro_rata.equalsIgnoreCase("No")) {

                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        leavesCalculationStartDate = leaveCycleStartDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        }
                    } else if (beforeAfter.equalsIgnoreCase("After")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }

                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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
                        if (beforeAfter.equalsIgnoreCase("Before") && !MultipleAllotmentEnabled.equals("Yes")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        } else {
                            if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = 0;
                            }
                        }
                    } else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before") && !MultipleAllotmentEnabled.equals("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else {
                            midJoinigYesLeaves = 0;
                        }
                    } else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before") && !MultipleAllotmentEnabled.equals("Yes")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes") && !MultipleAllotmentEnabled.equals("Yes")) {
                                midJoinigYesLeaves = 0;
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        } else {
                            if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = 0;
                            }
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

                        if (beforeAfter.equalsIgnoreCase("After")) {
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
                            && (beforeAfter.equalsIgnoreCase("Before") || multipleAllotmentCheckOnDayOfTransfer.equalsIgnoreCase("Yes"))) {
                        {
                            if (Monthly.equalsIgnoreCase("Yes")) {
                                if (multipleAllotmentCheckOnDayOfTransfer.equalsIgnoreCase("Yes")) {
                                    tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat, LastDayOfCalculationInDateFormat.toString());
                                } else {
                                    tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate());
                                }
                            } else if (Quarterly.equalsIgnoreCase("Yes")) {
                                if (multipleAllotmentCheckOnDayOfTransfer.equalsIgnoreCase("Yes")) {
                                    tempFlag = checkIfDayIsLastDayOfCurrentQuarter(LastDayOfCalculationInDateFormat, LastDayOfCalculationInDateFormat.toString());
                                } else {
                                    tempFlag = checkIfDayIsLastDayOfCurrentQuarter(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate());
                                }
                            } else {
                                tempFlag = false;
                            }
                        }
                        if (tempFlag == true) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        } else if (tempFlag == false) {
                            midJoinigYesLeaves = 0;
                            if (checkIfDOJFallsInCurrentQuarter(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate()) == true) {
                                truncateLeaves = 0;
                                midJoinigYesLeaves = 0;
                            }
                        }
                    }
                }

                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave);
            }
            // double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance *
            // 100.0) / s100.0;

            double ExpectedLeaveBalanceRoundOff = ExpectedLeaveBalance;

            if (ExpectedLeaveBalanceRoundOff < 0) {
                ExpectedLeaveBalanceRoundOff = 0;
            }

            if (beforeAfter.equalsIgnoreCase("After")) {
                beforeProbationFlag = false;
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
     * @param LastDayOfCalculation
     * @param beforeAfter
     * @return double
     */
    public double calculateMultipleAllotmentLeaveBalance(String DOJ, String LastDayOfCalculation, String customCurrentDate, String beforeAfter) {
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
            double midDeactivationLeaves = 0;
            LocalDate LastDayOfCalculationInDateFormat = LocalDate.parse(LastDayOfCalculation);
            Boolean tempFlag = false;

            if (beforeAfter.equalsIgnoreCase("Before")) {
                beforeProbationFlag = checkDOJisUnderLeaveProbationPeriod(DOJ, customCurrentDate);
            }

            if (beforeProbationFlag == true) {
                ExpectedLeaveBalance = 0;

            } else if (beforeProbationFlag == false) {

                if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    DOJ = leaveCycleStartDate;
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    Leave_Probation_End_Date = leaveCycleStartDate;
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        LeaveCalBeginningDate = Leave_Probation_End_Date;
                    } else {
                        if (LocalDate.parse(Leave_Probation_End_Date).isAfter(LocalDate.parse(DOJ))) {
                            LeaveCalBeginningDate = Leave_Probation_End_Date;
                        } else {
                            LeaveCalBeginningDate = DOJ;
                        }
                    }
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }

                if (Pro_rata.equalsIgnoreCase("Yes")) {

                    if (Accrual.equalsIgnoreCase("Yes")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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

                        if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                                && Calculate_after_probation_period.equalsIgnoreCase("Yes") && LocalDate
                                .parse(Leave_Probation_End_Date).isAfter(LastDayOfCalculationInDateFormat)) {
                            MonthOrQuarterDifference = 0;
                        }
                    }

                } else if (Pro_rata.equalsIgnoreCase("No")) {

                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        leavesCalculationStartDate = leaveCycleStartDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        }
                    } else if (beforeAfter.equalsIgnoreCase("After")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }

                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                                midDeactivationLeaves = (Leaves_Allowed_Per_Year / 24);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midDeactivationLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        }

                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }

                    } else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            midDeactivationLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else {
                            midJoinigYesLeaves = 0;
                        }
                    } else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                                midDeactivationLeaves = 0;
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midDeactivationLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        }
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
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

                        if (beforeAfter.equalsIgnoreCase("After")) {
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
                            && (beforeAfter.equalsIgnoreCase("Before") || multipleAllotmentCheckOnDayOfTransfer.equalsIgnoreCase("Yes"))) {
                        {
                            if (Monthly.equalsIgnoreCase("Yes")) {
                                tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat, customCurrentDate);
                            } else if (Quarterly.equalsIgnoreCase("Yes")) {
                                tempFlag = checkIfDayIsLastDayOfCurrentQuarter(LastDayOfCalculationInDateFormat, customCurrentDate);
                            } else {
                                tempFlag = false;
                            }
                        }
                        if (tempFlag == true) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        } else if (tempFlag == false) {
                            midDeactivationLeaves = 0;

                            if (checkIfDOJFallsInCurrentQuarter(LastDayOfCalculationInDateFormat, customCurrentDate) == true) {
                                truncateLeaves = 0;
                                midDeactivationLeaves = 0;
                            }
                        }
                    }
                }

                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave - midDeactivationLeaves);
            }
            // double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance *
            // 100.0) / s100.0;

            double ExpectedLeaveBalanceRoundOff = ExpectedLeaveBalance;

            if (ExpectedLeaveBalanceRoundOff < 0) {
                ExpectedLeaveBalanceRoundOff = 0;
            }

            if (beforeAfter.equalsIgnoreCase("After")) {
                beforeProbationFlag = false;
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
     * @param LastDayOfCalculation
     * @param beforeAfter
     * @return double
     */
    public double calculateBeforeTenureBasedDeactivationLeaveBalance(String DOJ, String LastDayOfCalculation,
                                                                     String beforeAfter) {
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

            if (beforeAfter.equalsIgnoreCase("Before")) {
                beforeProbationFlag = checkDOJisUnderLeaveProbationPeriod(DOJ);
            }

            if (beforeProbationFlag == true) {
                ExpectedLeaveBalance = 0;

            } else if (beforeProbationFlag == false) {

                if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    DOJ = leaveCycleStartDate;
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    Leave_Probation_End_Date = leaveCycleStartDate;
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        LeaveCalBeginningDate = Leave_Probation_End_Date;
                    } else {
                        if (LocalDate.parse(Leave_Probation_End_Date).isAfter(LocalDate.parse(DOJ))) {
                            LeaveCalBeginningDate = Leave_Probation_End_Date;
                        } else {
                            LeaveCalBeginningDate = DOJ;
                        }
                    }
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }

                if (Pro_rata.equalsIgnoreCase("Yes")) {

                    if (Accrual.equalsIgnoreCase("Yes")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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

                        if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                                && Calculate_after_probation_period.equalsIgnoreCase("Yes") && LocalDate
                                .parse(Leave_Probation_End_Date).isAfter(LastDayOfCalculationInDateFormat)) {
                            MonthOrQuarterDifference = 0;
                        }
                    }

                } else if (Pro_rata.equalsIgnoreCase("No")) {

                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        leavesCalculationStartDate = leaveCycleStartDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        }
                    } else if (beforeAfter.equalsIgnoreCase("After")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }

                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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

                        if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                                && Calculate_after_probation_period.equalsIgnoreCase("Yes") && LocalDate
                                .parse(Leave_Probation_End_Date).isAfter(LastDayOfCalculationInDateFormat)) {
                            MonthOrQuarterDifference = 0;
                        }
                    }
                }

                if (Pro_rata.equalsIgnoreCase("Yes")) {
                    if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        } else {
                            if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = 0;
                            }
                        }
                    } else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else {
                            midJoinigYesLeaves = 0;
                        }
                    } else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = 0;
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        } else {
                            if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = 0;
                            }
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

                        if (beforeAfter.equalsIgnoreCase("After")) {
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
                            tempFlag = true;
                        } else tempFlag = Quarterly.equalsIgnoreCase("Yes");
                        if (tempFlag == true) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        } else if (tempFlag == false) {
                            midJoinigYesLeaves = 0;
                            if (checkIfDOJFallsInCurrentQuarter(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate()) == true) {
                                truncateLeaves = 0;
                                midJoinigYesLeaves = 0;
                            }
                        }
                    }
                }

                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave);
            }
            // double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance *
            // 100.0) / 100.0;

            double ExpectedLeaveBalanceRoundOff = ExpectedLeaveBalance;

            if (ExpectedLeaveBalanceRoundOff < 0) {
                ExpectedLeaveBalanceRoundOff = 0;
            }

            if (beforeAfter.equalsIgnoreCase("After")) {
                beforeProbationFlag = false;
            }
            return ExpectedLeaveBalanceRoundOff;
        } catch (Exception e) {
            Reporter("Exception while calculating employess expected leave balance", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public boolean checkIfDOJFallsInCurrentQuarter(LocalDate LastDateOfCalculation, String baseValueDate) {
        LocalDate todaysDate = LocalDate.parse(baseValueDate);
        LocalDate currentQuarterLastMonth = objDateTimeHelper.getFirstDayOfLastMonthOfCurrentQuarter(todaysDate.toString());
        LocalDate lastDayOfCurrentQuarterLastMonth = currentQuarterLastMonth
                .withDayOfMonth(currentQuarterLastMonth.lengthOfMonth());
        LocalDate firstDayOfCurrentQuarter = objDateTimeHelper.getFirstDayOfQuarter(todaysDate.toString());

        return (LastDateOfCalculation.isAfter(firstDayOfCurrentQuarter)
                && LastDateOfCalculation.isBefore(lastDayOfCurrentQuarterLastMonth))
                || LastDateOfCalculation.isEqual(firstDayOfCurrentQuarter);
    }

    public boolean checkIfDOJFallsInCurrentQuarterCustomCurrentDate(String leaveCalculationStartDate,
                                                                    String LastDateOfCalculationString) {
        LocalDate todaysDate = LocalDate.parse(leaveCalculationStartDate);
        LocalDate LastDateOfCalculation = LocalDate.parse(LastDateOfCalculationString);
        LocalDate currentQuarterLastMonth = objDateTimeHelper.getFirstDayOfLastMonthOfCurrentQuarter(todaysDate.toString());
        LocalDate lastDayOfCurrentQuarterLastMonth = currentQuarterLastMonth
                .withDayOfMonth(currentQuarterLastMonth.lengthOfMonth());
        LocalDate firstDayOfCurrentQuarter = objDateTimeHelper.getFirstDayOfQuarter(todaysDate.toString());

        return (LastDateOfCalculation.isAfter(firstDayOfCurrentQuarter)
                && LastDateOfCalculation.isBefore(lastDayOfCurrentQuarterLastMonth))
                || LastDateOfCalculation.isEqual(firstDayOfCurrentQuarter);
    }

    /**
     * This method calculate leave balance
     *
     * @param DOJ
     * @param LastDayOfCalculation
     * @param beforeAfter
     * @return double
     */
    public double calculateDeactivationLeaveBalance(String DOJ, String LastDayOfCalculation, String beforeAfter) {
        try {
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle, LastDayOfCalculation);
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle, LastDayOfCalculation);
            double midJoinigYesLeaves = 0;
            double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
            double perMonthOrQuarterLeaves = 0;
            double MonthOrQuarterDifference = 0;
            double leavesDiffFromFirstDayOfQuarter = 0;
            String leavesCalculationStartDate = "";
            String midYearEndDate;
            double biannualLeave = 0;
            double truncateLeaves = 0;
            double midDeactivationLeaves = 0;
            LocalDate LastDayOfCalculationInDateFormat = LocalDate.parse(LastDayOfCalculation);
            boolean tempFlag = false;

            if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
                ExpectedLeaveBalance = 0;
            } else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {

                if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    DOJ = leaveCycleStartDate;
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    Leave_Probation_End_Date = leaveCycleStartDate;
                }

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
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            MonthOrQuarterDifference = objDateTimeHelper
                                    .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
                        } else if (beforeAfter.equalsIgnoreCase("After")) {
                            MonthOrQuarterDifference = objDateTimeHelper
                                    .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
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

                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        // MonthOrQuarterDifference = 12;
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            MonthOrQuarterDifference = objDateTimeHelper.getMonthDifferenceBetweenTwoDates(
                                    leavesCalculationStartDate, LastDayOfCalculation);
                        } else if (beforeAfter.equalsIgnoreCase("After")) {
                            MonthOrQuarterDifference = objDateTimeHelper
                                    .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
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

                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midDeactivationLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midDeactivationLeaves = (Leaves_Allowed_Per_Year / 24);
                        }
                    } else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        midJoinigYesLeaves = 0;
                        midDeactivationLeaves = 0;
                    } else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }

                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midDeactivationLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midDeactivationLeaves = (Leaves_Allowed_Per_Year / 12);
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

                        truncateLeaves = (perMonthLeaves
                                * objDateTimeHelper.getMonthDiffFromLastMonthOfQuarter(LastDayOfCalculation));

                    } else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("Yes")) {

                        perMonthOrQuarterLeaves = perMonthLeaves;
                        String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate, LastDayOfCalculation);
                        String currentDateBiannualHalf = checkBiannualHalfOfDate(LastDayOfCalculation,
                                LastDayOfCalculation);

                        midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString();
                        String biannualEndDate = "";

                        if (DOJBiannualHalf.equalsIgnoreCase("First")) {
                            biannualEndDate = midYearEndDate;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualEndDate = leaveCycleEndDate;
                        }
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

                        truncateLeaves = perMonthLeaves * objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(LastDayOfCalculation, biannualEndDate);
                    }

                    if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("No")) {
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    } else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
                            && End_of_monthORQuarter.equalsIgnoreCase("Yes") && Biannually.equalsIgnoreCase("No")) {
                        if (Quarterly.equalsIgnoreCase("Yes")) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        } else if (Monthly.equalsIgnoreCase("Yes")) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        }
                    }
                }
                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave
                        - (midDeactivationLeaves));
            }
            double ExpectedLeaveBalanceRoundOff = ExpectedLeaveBalance;
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
     * @param LastDayOfCalculation
     * @param beforeAfter
     * @param effectiveDate
     * @return double
     */
    public double calculateEffectiveDateDeactivationLeaveBalance(String DOJ, String LastDayOfCalculation, String beforeAfter, String effectiveDate) {
        try {
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle, LastDayOfCalculation);
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle, LastDayOfCalculation);
            double midJoinigYesLeaves = 0;
            double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
            double perMonthOrQuarterLeaves = 0;
            double MonthOrQuarterDifference = 0;
            double leavesDiffFromFirstDayOfQuarter = 0;
            String leavesCalculationStartDate = "";
            String midYearEndDate;
            double biannualLeave = 0;
            double truncateLeaves = 0;
            double midDeactivationLeaves = 0;
            LocalDate LastDayOfCalculationInDateFormat = LocalDate.parse(LastDayOfCalculation);
            boolean tempFlag = false;

            if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
                ExpectedLeaveBalance = 0;
            } else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {

                if (!Pro_rata.equalsIgnoreCase("No") || !Accrual.equalsIgnoreCase("No")) {
                    DOJ = effectiveDate;
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(effectiveDate))) {
                    Leave_Probation_End_Date = effectiveDate;
                }

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
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            MonthOrQuarterDifference = objDateTimeHelper
                                    .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
                        } else if (beforeAfter.equalsIgnoreCase("After")) {
                            MonthOrQuarterDifference = objDateTimeHelper
                                    .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
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

                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        // MonthOrQuarterDifference = 12;
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            MonthOrQuarterDifference = objDateTimeHelper.getMonthDifferenceBetweenTwoDates(
                                    leavesCalculationStartDate, LastDayOfCalculation);
                        } else if (beforeAfter.equalsIgnoreCase("After")) {
                            MonthOrQuarterDifference = objDateTimeHelper
                                    .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);
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

                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midDeactivationLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midDeactivationLeaves = (Leaves_Allowed_Per_Year / 24);
                        }
                    } else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        midJoinigYesLeaves = 0;
                        midDeactivationLeaves = 0;
                    } else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }

                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midDeactivationLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midDeactivationLeaves = (Leaves_Allowed_Per_Year / 12);
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

                        truncateLeaves = (perMonthLeaves
                                * objDateTimeHelper.getMonthDiffFromLastMonthOfQuarter(LastDayOfCalculation));

                    } else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("Yes")) {

                        perMonthOrQuarterLeaves = perMonthLeaves;
                        String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate, LastDayOfCalculation);
                        String currentDateBiannualHalf = checkBiannualHalfOfDate(LastDayOfCalculation,
                                LastDayOfCalculation);

                        midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString();
                        String biannualEndDate = "";

                        if (DOJBiannualHalf.equalsIgnoreCase("First")) {
                            biannualEndDate = midYearEndDate;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualEndDate = leaveCycleEndDate;
                        }
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

                        truncateLeaves = perMonthLeaves * objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(LastDayOfCalculation, biannualEndDate);
                    }

                    if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("No")) {
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    } else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
                            && End_of_monthORQuarter.equalsIgnoreCase("Yes") && Biannually.equalsIgnoreCase("No")) {
                        if (Quarterly.equalsIgnoreCase("Yes")) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        } else if (Monthly.equalsIgnoreCase("Yes")) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        }
                    }
                }
                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave
                        - (midDeactivationLeaves));
            }
            double ExpectedLeaveBalanceRoundOff = ExpectedLeaveBalance;
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
     * @param LastDayOfCalculation
     * @param beforeAfter
     * @return double
     */
    public double calculateTenureDeactivationBasedLeaveBalance(String DOJ, String LastDayOfCalculation,
                                                               String beforeAfter) {
        try {
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle, LastDayOfCalculation);
            double midJoinigYesLeaves = 0;
            double perMonthLeaves = (Leaves_Allowed_Per_Year / 12);
            double perMonthOrQuarterLeaves = 0;
            double MonthOrQuarterDifference = 0;
            double leavesDiffFromFirstDayOfQuarter = 0;
            String leavesCalculationStartDate = "";
            String midYearEndDate;
            double biannualLeave = 0;
            double truncateLeaves = 0;
            boolean tempFlag = false;
            double midDeactivationLeaves = 0;
            LocalDate LastDayOfCalculationInDateFormat = LocalDate.parse(LastDayOfCalculation);

            if (beforeAfter.equalsIgnoreCase("Before")) {
                beforeProbationFlag = checkDOJisUnderLeaveProbationPeriod(DOJ);
            }

            if (beforeProbationFlag == true) {
                ExpectedLeaveBalance = 0;

            } else if (beforeProbationFlag == false) {

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        LeaveCalBeginningDate = Leave_Probation_End_Date;
                    } else {
                        if (LocalDate.parse(Leave_Probation_End_Date).isAfter(LocalDate.parse(DOJ))) {
                            LeaveCalBeginningDate = Leave_Probation_End_Date;
                        } else {
                            LeaveCalBeginningDate = DOJ;
                        }
                    }
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }

                if (Pro_rata.equalsIgnoreCase("Yes")) {

                    if (Accrual.equalsIgnoreCase("Yes")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);

                        if (MonthOrQuarterDifference < 0) {
                            MonthOrQuarterDifference = -MonthOrQuarterDifference;
                        }
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    }

                } else if (Pro_rata.equalsIgnoreCase("No")) {

                    leavesCalculationStartDate = LeaveCalBeginningDate;
                    if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                        midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                    } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("No")) {
                        midJoinigYesLeaves = 0;
                    }
                    //
                    // if
                    // (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes"))
                    // {
                    // midDeactivationLeaves = 0;
                    // } else if
                    // (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No"))
                    // {
                    // midDeactivationLeaves = (Leaves_Allowed_Per_Year / 12);
                    // }
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, LastDayOfCalculation);

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

                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midDeactivationLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midDeactivationLeaves = (Leaves_Allowed_Per_Year / 24);
                        }

                    } else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        midJoinigYesLeaves = 0;
                        midDeactivationLeaves = 0;

                    } else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }

                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midDeactivationLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midDeactivationLeaves = (Leaves_Allowed_Per_Year / 12);
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

                        truncateLeaves = (perMonthLeaves
                                * objDateTimeHelper.getMonthDiffFromLastMonthOfQuarter(LastDayOfCalculation));

                    } else if (Monthly.equalsIgnoreCase("No") && Quarterly.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("Yes")) {

                        perMonthOrQuarterLeaves = perMonthLeaves;
                        String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate, LastDayOfCalculation);
                        String currentDateBiannualHalf = checkBiannualHalfOfDate(LastDayOfCalculation,
                                LastDayOfCalculation);

                        midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString();
                        String biannualEndDate = "";

                        if (DOJBiannualHalf.equalsIgnoreCase("First")) {
                            biannualEndDate = midYearEndDate;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualEndDate = leaveCycleEndDate;
                        }
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

                        if (beforeAfter.equalsIgnoreCase("After")) {
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
                        }

                        String biannualCurrentEndDate = "";
                        if (currentDateBiannualHalf.equalsIgnoreCase("First")) {
                            biannualCurrentEndDate = midYearEndDate;
                        } else if (currentDateBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualCurrentEndDate = leaveCycleEndDate;
                        }

                        truncateLeaves = perMonthLeaves * objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(LastDayOfCalculation, biannualCurrentEndDate);

                        // if(truncateLeaves < 0) {
                        // truncateLeaves = -truncateLeaves;
                        // }
                    }

                    if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") && End_of_monthORQuarter.equalsIgnoreCase("No")
                            && Biannually.equalsIgnoreCase("No")) {
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    } else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
                            && End_of_monthORQuarter.equalsIgnoreCase("Yes") && Biannually.equalsIgnoreCase("No")) {
                        if (Quarterly.equalsIgnoreCase("Yes")) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        } else if (Monthly.equalsIgnoreCase("Yes")) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        }
                    }

                    // if (Begin_of_monthORQuarter.equalsIgnoreCase("Yes") &&
                    // End_of_monthORQuarter.equalsIgnoreCase("No")
                    // && Biannually.equalsIgnoreCase("No")) {
                    // MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    // } else if (Begin_of_monthORQuarter.equalsIgnoreCase("No")
                    // && End_of_monthORQuarter.equalsIgnoreCase("Yes") &&
                    // Biannually.equalsIgnoreCase("No")){
                    // if (Monthly.equalsIgnoreCase("Yes")) {
                    // tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat);
                    // } else if (Quarterly.equalsIgnoreCase("Yes")) {
                    // tempFlag =
                    // checkIfDayIsLastDayOfCurrentQuarter(LastDayOfCalculationInDateFormat);
                    // } else {
                    // tempFlag = false;
                    // }
                    // if (tempFlag == true) {
                    // MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    // } else if (tempFlag == false) {
                    // midJoinigYesLeaves = 0;
                    // if (checkIfDOJFallsInCurrentQuarter(LastDayOfCalculationInDateFormat) ==
                    // true) {
                    // truncateLeaves = 0;
                    // midJoinigYesLeaves = 0;
                    // }
                    // }
                    // }
                }

                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave
                        - (midDeactivationLeaves));
            }

            double ExpectedLeaveBalanceRoundOff = ExpectedLeaveBalance;

            if (ExpectedLeaveBalanceRoundOff < 0) {
                ExpectedLeaveBalanceRoundOff = 0;
            }

            if (beforeAfter.equalsIgnoreCase("After")) {
                beforeProbationFlag = false;
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
     * @param LastDayOfCalculation
     * @param beforeAfter
     * @param EffectiveDate
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
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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

                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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
                            tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate());
                        } else if (Quarterly.equalsIgnoreCase("Yes")) {
                            tempFlag = checkIfDayIsLastDayOfCurrentQuarter(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate());
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

    public boolean checkIfDayIsLastDayOfMonth(LocalDate LastDateOfCalculationInDateFormat, String baseDateValue) {
        try {
            LocalDate todaysDate = LocalDate.parse(baseDateValue);
            LocalDate lastDayOfCurrentMonth = todaysDate.withDayOfMonth(todaysDate.lengthOfMonth());
            LocalDate firstDayOfCurrentMonth = todaysDate.with(firstDayOfMonth());

            if (LastDateOfCalculationInDateFormat.isBefore(firstDayOfCurrentMonth)) {
                return true;
            } else if (LastDateOfCalculationInDateFormat.isAfter(firstDayOfCurrentMonth)
                    || LastDateOfCalculationInDateFormat.isEqual(firstDayOfCurrentMonth)) {
                return LastDateOfCalculationInDateFormat.isEqual(lastDayOfCurrentMonth);
            } else {
                throw new RuntimeException("Date is not before or after 1st day of current month. Please check");
            }
        } catch (Exception e) {
            Reporter("Exception while calculating Last Day of Month", "Error");
            throw new RuntimeException();
        }
    }

    public boolean checkIfDayIsLastDayOfCurrentQuarter(LocalDate LastDateOfCalculationInDateFormat, String baseDateValue) {
        try {
            LocalDate todaysDate = LocalDate.parse(baseDateValue);
            LocalDate currentQuarterLastMonth = objDateTimeHelper.getFirstDayOfLastMonthOfCurrentQuarter(todaysDate.toString());
            LocalDate lastDayOfCurrentQuarterLastMonth = currentQuarterLastMonth
                    .withDayOfMonth(currentQuarterLastMonth.lengthOfMonth());
            LocalDate firstDayOfCurrentQuarter = objDateTimeHelper.getFirstDayOfQuarter(todaysDate.toString());

            if (LastDateOfCalculationInDateFormat.isBefore(firstDayOfCurrentQuarter)) {
                return true;
            } else if (LastDateOfCalculationInDateFormat.isAfter(firstDayOfCurrentQuarter)
                    || LastDateOfCalculationInDateFormat.isEqual(firstDayOfCurrentQuarter)) {
                return LastDateOfCalculationInDateFormat.isEqual(lastDayOfCurrentQuarterLastMonth);
            } else {
                throw new RuntimeException("Date is not before or after 1st day of current month. Please check");
            }
        } catch (Exception e) {
            Reporter("Exception while calculating Last Day of Quarter", "Error");
            throw new RuntimeException();
        }
    }


    public boolean setLeaveType() {
        Leave_Type = getData("Leave_Type");
        Reporter("Leave Type is : '" + Leave_Type + "'", "Info");
        return true;
    }

    public boolean setLeaveType1() {
        Leave_Type = getData("Leave Type");
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
     * @param effectiveDate
     * @return boolean
     * @author shikhar
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
//			DateOfJoining = changeEmployeeDOJ(LocalDate.parse("2018-04-01"));
            DateOfJoining = "2018-04-01";
            double expectedBalance = calculateEffectiveDateLeaveBalance(DateOfJoining,
                    DateTimeHelper.getCurrentLocalDate(), "After", effectiveDate);
            Leave_Type = "LTD17";
            double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
//			removeEmployeeLeaveLogs();
            if (expectedBalance != actualBalance) {
                flag++;
                Reporter("Failed||" + "Effective Date '" + effectiveDate + "'||" + "Expected Leave Balance="
                        + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
                result = "Fail";
            } else {
                Reporter("Passed||" + "Effective Date '" + effectiveDate + "'||" + "Expected Leave Balance="
                        + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
                result = "Pass";
            }

            if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                writeEffectiveDateResultToExcel(effectiveDate, expectedBalance, actualBalance, result,
                        DateTimeHelper.getCurrentLocalDateAndTime());
            }

            return flag <= 0;
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
            LocalDate iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            while (iterationDate.isAfter(leaveCycleStartDateInDateFormat)) {
                iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).minusDays(i);
                DateOfJoining = changeEmployeeDOJ(iterationDate);
                double expectedBalance = calculateEffectiveDateLeaveBalance(DateOfJoining,
                        DateTimeHelper.getCurrentLocalDate(), "After", effectiveDate);
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
                            DateTimeHelper.getCurrentLocalDateAndTime());
                }
            }
            return flag <= 0;

        } catch (

                Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }
    }


    /**
     * This method provides Current month of any tenure year
     */
    public boolean tenureCurrentMonthOfAnyYear(LocalDate iterationDate) {
        try {
            return iterationDate.getMonthValue() == LocalDate.parse(DateTimeHelper.getCurrentLocalDate())
                    .getMonthValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while getting Tenure Current Month of any Year");
        }
    }

    public boolean tenureCurrentDayOfAnyYear(LocalDate iterationDate) {
        try {
            return (iterationDate.getMonthValue() == LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getMonthValue())
                    && (iterationDate.getDayOfMonth() == LocalDate.parse(DateTimeHelper.getCurrentLocalDate())
                    .getDayOfMonth());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while getting Tenure Current Month of any Year");
        }
    }

    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyTenureDeactivationLeaveBalanceForFourEdgeDays() {
        try {

            String result = "";
            int flag = 0;
            double expectedBalance = 0;

            Long TempCreditFromYearLong;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
            double expectedLeaveBalanceAfterAnniversary;

            LocalDate leaveCycleStartDateInDateFormatPrimary = LocalDate.parse(leaveCycleStartDate);
            LocalDate leaveCycleEndDateInDateFormatPrimary = LocalDate.parse(leaveCycleLastDate);

            LocalDate deactivationIterationDate = leaveCycleEndDateInDateFormatPrimary;

            int j = 0;
            while (deactivationIterationDate.isAfter(leaveCycleStartDateInDateFormatPrimary)) {
                deactivationIterationDate = leaveCycleEndDateInDateFormatPrimary.minusDays(j);
                if (iterationDateTwoTimesPerMonth(deactivationIterationDate) == true) {
                    Reporter("Leave Balance will be checked for Decativate date '"
                            + deactivationIterationDate.toString() + "'", "Info");

                    // LocalDate iterationDate = deactivationIterationDate;

                    List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
                    List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
                    List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

                    int anniversaryIterator = 0;
                    while ((anniversaryIterator < creditFromYearList.size())
                            && (anniversaryIterator < creditToYearList.size())
                            && (anniversaryIterator < creditNoOfLeavesList.size())) {

                        String CreditFromYearVar;
                        String CreditToYearVar;
                        String CreditNoOfLeavesVar;

                        long CreditFromYearVarLong;
                        long CreditToYearVarLong;
                        long CreditNoOfLeavesVarLong;

                        CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                        CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                        CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                        CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                        CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                        CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                        if (anniversaryIterator > 0) {
                            Leaves_Allowed_Per_Year = Double
                                    .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
                        }
                        int tenure = 0;
                        if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
                            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                            LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

                            LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
                                    .minusYears(CreditToYearVarLong - 1);

                            LocalDate lastDateForCalculation;
                            Long firstCreditFromYear = Long.valueOf(creditFromYearList.get(0).trim());
                            LocalDate tenureFirstLastDate = leaveCycleLastDateInDateFormat
                                    .minusYears(firstCreditFromYear);

                            if (anniversaryIterator == 0 && CreditFromYearVarLong != 1) {
                                lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(1);
                            } else if (anniversaryIterator == creditFromYearList.size() - 1) {
                                startDateForCalculation = leaveCycleStartDateInDateFormat
                                        .minusYears(CreditToYearVarLong);
                                lastDateForCalculation = leaveCycleLastDateInDateFormat
                                        .minusYears(CreditFromYearVarLong);
                            } else {
                                lastDateForCalculation = leaveCycleLastDateInDateFormat
                                        .minusYears(CreditFromYearVarLong);
                            }

                            int anniversaryYear = anniversaryIterator + 1;

                            Reporter("Leaves will be calculated for ~" + anniversaryYear + "~ anniversary", "Info");
                            Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                                    + " hence leave balance will be verifed from '" + lastDateForCalculation
                                    + "' till '" + startDateForCalculation, "Info");

                            int i = 0;
                            LocalDate iterationDate = lastDateForCalculation;

                            TempCreditFromYearLong = CreditFromYearVarLong;

                            while ((iterationDate.isAfter(startDateForCalculation.minusDays(-1))
                                    || iterationDate.isEqual(startDateForCalculation.minusDays(-1)))
                                    && (iterationDate.isBefore(lastDateForCalculation.minusDays(-1))
                                    || iterationDate.isEqual(lastDateForCalculation.minusDays(-1)))) {
                                iterationDate = lastDateForCalculation.minusDays(i);

                                // if (iterationDate.isEqual(LocalDate.parse("2016-01-02"))) {
                                // System.out.println("Stop");
                                // tenureDOJFlag = 1;
                                // }

                                LocalDate tenureLastDate = leaveCycleLastDateInDateFormat
                                        .minusYears(CreditFromYearVarLong + 1);
                                LocalDate tenureStartDate = leaveCycleStartDateInDateFormat
                                        .minusYears(CreditToYearVarLong - 1);

                                if ((iterationDate.isBefore(tenureLastDate) && iterationDate.isAfter(tenureStartDate))
                                        /* || iterationDate.isEqual(tenureStartDate) */ || iterationDate.isEqual(
                                        tenureLastDate)
                                        || iterationDate.isAfter(tenureFirstLastDate)) {
                                    tenure = 1;
                                } else {
                                    tenure = 0;
                                }

                                LocalDate lastDayOfIterationLeaveCycle = LocalDate
                                        .parse(getLastDayofLeaveCycle(Leave_Cycle, iterationDate.toString()));
                                if (iterationDate.isEqual(lastDayOfIterationLeaveCycle) && i != 0
                                        && iterationDate.isBefore(tenureFirstLastDate)) {
                                    TempCreditFromYearLong = TempCreditFromYearLong + 1;
                                    Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                    // removeEmployeeLeaveLogs();
                                }
                                // && tenureDOJFlag == 1 || tenureCurrentMonthOfAnyYear(iterationDate) == true
                                if ((iterationDateFourTimesPerMonth(iterationDate) == true)) {
                                    DateOfJoining = changeEmployeeDOJ(iterationDate);

                                    LocalDate anniversaryDateInDateFormat = iterationDate
                                            .minusYears(-TempCreditFromYearLong);

                                    if (anniversaryDateInDateFormat.isAfter(deactivationIterationDate)) {
                                        expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                                deactivationIterationDate.toString(), "Before");
                                    } else if (tenure == 1) {
                                        expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                                deactivationIterationDate.toString(), "Before");
                                    } else {

                                        String anniversaryDate = anniversaryDateInDateFormat.toString();
                                        double expectedLeaveBalanceBeforeAnniversary = calculateTenureBasedLeaveBalance(
                                                DateOfJoining, anniversaryDate, "Before");
                                        double temLeaveVar = Leaves_Allowed_Per_Year;
                                        Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                        if (anniversaryDateInDateFormat.isBefore(deactivationIterationDate)
                                                || anniversaryDateInDateFormat.isEqual(deactivationIterationDate)) {
                                            expectedLeaveBalanceAfterAnniversary = calculateTenureDeactivationBasedLeaveBalance(
                                                    anniversaryDate, deactivationIterationDate.toString(), "After");
                                        } else {
                                            expectedLeaveBalanceAfterAnniversary = 0;
                                        }
                                        Leaves_Allowed_Per_Year = temLeaveVar;
                                        expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                                + expectedLeaveBalanceAfterAnniversary;
                                        expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

                                    }
                                    double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                                            deactivationIterationDate.toString());
                                    if (expectedBalance != actualBalance) {
                                        Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||"
                                                + "Expected Leave Balance=" + expectedBalance
                                                + "||Actual Leave Balance=" + actualBalance + "||Deactivation Date '"
                                                + deactivationIterationDate.toString() + "'", "Fail");
                                        result = "Fail";
                                        flag++;
                                    } else {
                                        Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||"
                                                + "Expected Leave Balance=" + expectedBalance
                                                + "||Actual Leave Balance=" + actualBalance + "||Deactivation Date '"
                                                + deactivationIterationDate.toString() + "'", "Pass");
                                        result = "Pass";
                                    }

                                    if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                        writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(),
                                                DateOfJoining, expectedBalance, actualBalance, result,
                                                DateTimeHelper.getCurrentLocalDateAndTime());
                                    }
                                }
                                i++;
                            }
                        } else {
                            Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                        }
                        anniversaryIterator++;
                    }
                }
                j++;
            }
            return flag <= 0;

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
    public boolean verifyTenureDeactivationLeaveBalanceForFourEdgeDaysCustomDatesNewFomEndOfYear(
            String[] deactivationDateArray) {
        try {

            String result = "";
            int flag = 0;
            double expectedBalance = 0;

            Long TempCreditFromYearLong;
            double expectedLeaveBalanceBeforeAnniversary = 0;
            double expectedLeaveBalanceAfterAnniversary = 0;

            double tempVar = Leaves_Allowed_Per_Year;
            int arraysize = deactivationDateArray.length;
            int k = 0;

            while (k < arraysize) {
                removeEmployeeLeaveLogs();
                Leaves_Allowed_Per_Year = tempVar;
                String deactivationDate = deactivationDateArray[k];
                String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle, deactivationDate);
                String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle, deactivationDate);

                LocalDate deactivationIterationDate = LocalDate.parse(deactivationDate);
                Reporter("Leave Balance will be checked for Decativate date '" + deactivationIterationDate.toString()
                        + "'", "Info");

                // LocalDate iterationDate = deactivationIterationDate;

                List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
                List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
                List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

                int anniversaryIterator = 0;
                while ((anniversaryIterator < creditFromYearList.size())
                        && (anniversaryIterator < creditToYearList.size())
                        && (anniversaryIterator < creditNoOfLeavesList.size())) {

                    String CreditFromYearVar;
                    String CreditToYearVar;
                    String CreditNoOfLeavesVar;

                    long CreditFromYearVarLong;
                    long CreditToYearVarLong;
                    long CreditNoOfLeavesVarLong;

                    CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                    CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                    CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                    CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                    CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                    CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                    if (anniversaryIterator > 0) {
                        Leaves_Allowed_Per_Year = Double
                                .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
                    }
                    int tenure = 0;
                    if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
                        LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                        LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleEndDate);

                        LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
                                .minusYears(CreditToYearVarLong - 1);

                        LocalDate lastDateForCalculation;
                        Long firstCreditFromYear = Long.valueOf(creditFromYearList.get(0).trim());
                        LocalDate tenureFirstLastDate = leaveCycleLastDateInDateFormat.minusYears(firstCreditFromYear);

                        if (anniversaryIterator == 0 && CreditFromYearVarLong != 1) {
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(1);
                        } else if (anniversaryIterator == creditFromYearList.size() - 1) {
                            startDateForCalculation = leaveCycleStartDateInDateFormat.minusYears(CreditToYearVarLong);
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                        } else {
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                        }

                        int anniversaryYear = anniversaryIterator + 1;

                        Reporter("Leaves will be calculated for ~" + anniversaryYear + "~ anniversary", "Info");
                        Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                                + " hence leave balance will be verifed from '" + lastDateForCalculation + "' till '"
                                + startDateForCalculation, "Info");

                        int i = 0;
                        LocalDate iterationDate = lastDateForCalculation;

                        TempCreditFromYearLong = CreditFromYearVarLong;

                        while ((iterationDate.isAfter(startDateForCalculation.minusDays(-1))
                                || iterationDate.isEqual(startDateForCalculation.minusDays(-1)))
                                && (iterationDate.isBefore(lastDateForCalculation.minusDays(-1))
                                || iterationDate.isEqual(lastDateForCalculation.minusDays(-1)))) {
                            iterationDate = lastDateForCalculation.minusDays(i);

                            // if (iterationDate.isEqual(LocalDate.parse("2015-01-15"))) {
                            // System.out.println("Stop");
                            // tenureDOJFlag = 1;
                            // }

                            LocalDate tenureLastDate = leaveCycleLastDateInDateFormat
                                    .minusYears(CreditFromYearVarLong + 1);
                            LocalDate tenureStartDate = leaveCycleStartDateInDateFormat
                                    .minusYears(CreditToYearVarLong - 1);

                            if ((iterationDate.isBefore(tenureLastDate) && iterationDate.isAfter(tenureStartDate))
                                    /* || iterationDate.isEqual(tenureStartDate) */ || iterationDate
                                    .isEqual(tenureLastDate)
                                    || iterationDate.isAfter(tenureFirstLastDate)) {
                                tenure = 1;
                            } else {
                                tenure = 0;
                            }
                            int check = 0;
                            LocalDate lastDayOfIterationLeaveCycle = LocalDate
                                    .parse(getLastDayofLeaveCycle(Leave_Cycle, iterationDate.toString()));
                            if (iterationDate.isEqual(lastDayOfIterationLeaveCycle) && i != 0
                                    && iterationDate.isBefore(tenureFirstLastDate)) {
                                TempCreditFromYearLong = TempCreditFromYearLong + 1;
                                // removeEmployeeLeaveLogs();
                                check = 1;
                            }
                            if (tenureCurrentDayOfAnyYear(iterationDate) == true && check == 1) {
                                Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                            }

                            // && tenureDOJFlag == 1 || tenureCurrentMonthOfAnyYear(iterationDate) == true
                            if ((iterationDateFourTimesPerMonth(iterationDate) == true)) {
                                DateOfJoining = changeEmployeeDOJ(iterationDate);

                                if (TempCreditFromYearLong > CreditFromYearVarLong
                                        && TempCreditFromYearLong < CreditToYearVarLong) {
                                    TempCreditFromYearLong = CreditFromYearVarLong;
                                }
                                LocalDate anniversaryDateInDateFormat = iterationDate
                                        .minusYears(-TempCreditFromYearLong);

                                if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                        && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                                    if (anniversaryDateInDateFormat.isAfter(deactivationIterationDate)
                                            && (tenure != 1)) {
                                        expectedBalance = calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                DateOfJoining, deactivationIterationDate.toString(), "Before");
                                    } /*
                                     * else if (tenure == 1) { expectedBalance =
                                     * calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                     * DateOfJoining, deactivationIterationDate.toString(), "Before"); }
                                     */ else {
                                        String anniversaryDate = anniversaryDateInDateFormat.toString();
                                        String anniversaryDateMinusOne = anniversaryDateInDateFormat.minusDays(1)
                                                .toString();
                                        expectedLeaveBalanceBeforeAnniversary = calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                DateOfJoining, anniversaryDateMinusOne, "Before");
                                        double temLeaveVar = Leaves_Allowed_Per_Year;
                                        Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                        if (anniversaryDateInDateFormat.isBefore(deactivationIterationDate)
                                                || anniversaryDateInDateFormat.isEqual(deactivationIterationDate)) {
                                            expectedLeaveBalanceAfterAnniversary = calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                    anniversaryDate, deactivationIterationDate.toString(), "After");
                                        } else {
                                            expectedLeaveBalanceAfterAnniversary = 0;
                                        }
                                        Leaves_Allowed_Per_Year = temLeaveVar;
                                        expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                                + expectedLeaveBalanceAfterAnniversary;
                                    }
                                } else {
                                    if (anniversaryDateInDateFormat.isAfter(deactivationIterationDate)) {
                                        expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                                deactivationIterationDate.toString(), "Before");
                                    } else if (tenure == 1) {
                                        expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                                deactivationIterationDate.toString(), "Before");
                                    } else {
                                        String anniversaryDate = anniversaryDateInDateFormat.toString();
                                        expectedLeaveBalanceBeforeAnniversary = calculateBeforeTenureBasedDeactivationLeaveBalance(
                                                DateOfJoining, anniversaryDate, "Before");
                                        double temLeaveVar = Leaves_Allowed_Per_Year;
                                        Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                        if (anniversaryDateInDateFormat.isBefore(deactivationIterationDate)
                                                || anniversaryDateInDateFormat.isEqual(deactivationIterationDate)) {
                                            expectedLeaveBalanceAfterAnniversary = calculateTenureDeactivationBasedLeaveBalance(
                                                    anniversaryDate, deactivationIterationDate.toString(), "After");
                                        } else {
                                            expectedLeaveBalanceAfterAnniversary = 0;
                                        }
                                        Leaves_Allowed_Per_Year = temLeaveVar;
                                    }
                                    expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                            + expectedLeaveBalanceAfterAnniversary;
                                }
                                expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;
                                double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                                        deactivationIterationDate.toString());
                                if (expectedBalance != actualBalance) {
                                    Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                    + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                                    + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                            "Fail");
                                    result = "Fail";
                                    flag++;
                                } else {
                                    Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                    + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                                    + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                            "Pass");
                                    result = "Pass";
                                }

                                if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                    writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(),
                                            DateOfJoining, expectedBalance, actualBalance, result,
                                            DateTimeHelper.getCurrentLocalDateAndTime());
                                }
                            }
                            i++;
                        }
                    } else {
                        Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                    }
                    anniversaryIterator++;
                }
                k++;
            }

            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * This method calculate leave balance in case of working days
     *
     * @return
     */
    public boolean verifyWorkingDaysLeaveBalanceForWholeLeaveCycle() {
        try {
            double expectedBalance = 0;
            double expectedBalanceWithoutRoundOff = 0;
            double actualBalance = 0;
            double calculatedLeaveBalance = 0;
            String result = "";
            int flag = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                    + " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
                    + "' till current date", "Info");
            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
            int i = 0;
            LocalDate iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            while (iterationDate.isAfter(leaveCycleStartDateInDateFormat.minusMonths(18))) {
                iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).minusDays(i);
                if (iterationDateFourTimesPerMonth(iterationDate) == true) {
                    DateOfJoining = changeEmployeeDOJ(iterationDate);
                    expectedBalanceWithoutRoundOff = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                            DateTimeHelper.getCurrentLocalDate());
                    expectedBalance = Math.round(expectedBalanceWithoutRoundOff * 100.0) / 100.0;
                    actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
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
                                DateTimeHelper.getCurrentLocalDateAndTime());
                    }
                }
                i++;
            }
            return flag <= 0;
        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }

    }


    /**
     * This method calculates working days leave balance
     *
     * @param DOJ
     * @param toDate
     * @return
     */
    public double calculateLeaveBalanceAsPerEmployeeWorkingDays(String DOJ, String toDate, String customCurrentDate) {
        try {
            double workingDaysBalance = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
            int currentYearInEndOfYearFlag = 0;
            double daysConsiderForCalculation = 0;

            if (checkDOJisUnderLeaveProbationPeriod(DOJ, customCurrentDate) == true) {
                workingDaysBalance = 0;
            } else if (checkDOJisUnderLeaveProbationPeriod(DOJ, customCurrentDate) == false) {

                if (EndOfYear.equalsIgnoreCase("No")) {
                    if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        DOJ = leaveCycleStartDate;
                    }

                    if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        Leave_Probation_End_Date = leaveCycleStartDate;
                    }
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    LeaveCalBeginningDate = Leave_Probation_End_Date;
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                    // LeaveCalBeginningDate = leaveCycleStartDate;
                }


                if (EndOfYear.equalsIgnoreCase("Yes")) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate).minusYears(1))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate).minusYears(1).toString();
                    }
                    if (LocalDate.parse(LeaveCalBeginningDate).isAfter(LocalDate.parse(leaveCycleStartDate))) {
                        return workingDaysBalance = 0;
                    } else if (LocalDate.parse(LeaveCalBeginningDate).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        if (LocalDate.parse(toDate).isAfter(LocalDate.parse(leaveCycleStartDate))) {
                            toDate = LocalDate.parse(leaveCycleStartDate).minusDays(1).toString();
                        }
                    }
                } else if (EndOfYear.equalsIgnoreCase("No")) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate).toString();
                    }

                    if (WorkingDaysEndOfMonth.equalsIgnoreCase("Yes") &&
                            WorkingDaysEndOfQuarter.equalsIgnoreCase("No") && WorkingDaysEndOfBiannual.equalsIgnoreCase("No")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle).minusMonths(1).toString();
                        }
                    } else if (WorkingDaysEndOfMonth.equalsIgnoreCase("No") && WorkingDaysEndOfQuarter.equalsIgnoreCase("Yes")
                            && WorkingDaysEndOfBiannual.equalsIgnoreCase("No")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle).minusMonths(3).toString();
                        }
                    } else if (WorkingDaysEndOfMonth.equalsIgnoreCase("No") && WorkingDaysEndOfQuarter.equalsIgnoreCase("No")
                            && WorkingDaysEndOfBiannual.equalsIgnoreCase("Yes")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle).minusMonths(6).toString();
                        }
                    }
                }

                if (EndOfYear.equalsIgnoreCase("Yes")) {
                    daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).minusYears(1)
                            .lengthOfYear();
                } else {
                    daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).lengthOfYear();
                }

                double workingDays = getWorkingDaysToConsiderForCalculation(LeaveCalBeginningDate, toDate);
                workingDaysBalance = Leaves_Allowed_Per_Year * (workingDays / daysConsiderForCalculation);

            }
            return workingDaysBalance;
        } catch (Exception e) {
            Reporter("Exception while calculating working days leave balance", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * This method calculates working days leave balance
     *
     * @param DOJ
     * @param toDate
     * @return
     */
    public double calculateDeactivationLeaveBalanceAsPerEmployeeWorkingDays(String DOJ, String toDate) {
        try {
            double workingDaysBalance = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            //String leaveCycleStartDate =""
            if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
                workingDaysBalance = 0;
            } else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {

                if (!EndOfYear.equalsIgnoreCase("Yes")) {
                    if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                       // DOJ = leaveCycleStartDate;
                    }

                    if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                       // Leave_Probation_End_Date = leaveCycleStartDate;
                    }
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    LeaveCalBeginningDate = Leave_Probation_End_Date;
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }

                if (EndOfYear.equalsIgnoreCase("Yes")) {
                    if ((LocalDate.parse(LeaveCalBeginningDate)).isBefore(LocalDate.parse(leaveCycleStartDate).minusYears(1))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate).minusYears(1).toString();
                    }
                }

                double daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate())
                        .lengthOfYear();
               double workingDays = getWorkingDaysToConsiderForCalculation(LeaveCalBeginningDate, toDate);
                workingDaysBalance = Leaves_Allowed_Per_Year * (workingDays / daysConsiderForCalculation);
            }
            return workingDaysBalance;
        } catch (Exception e) {
            Reporter("Exception while calculating working days leave balance", "Error");
            throw new RuntimeException();
        }
    }

    /**
     * This method calculates working days leave balance
     *
     * @param DOJ
     * @param toDate
     * @return
     */
    public double calculateTenureLeaveBalanceAsPerEmployeeWorkingDays(String DOJ, String toDate, String beforeAfter) {
        try {
            double workingDaysBalance = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
            double daysConsiderForCalculation;

            if (beforeAfter.equalsIgnoreCase("Before")) {
                beforeProbationFlag = checkDOJisUnderLeaveProbationPeriod(DOJ);
            }

            if (beforeProbationFlag == true) {
                workingDaysBalance = 0;
            } else if (beforeProbationFlag == false) {

                if (EndOfYear.equalsIgnoreCase("No")) {
                    if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        DOJ = leaveCycleStartDate;
                    }
                    if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))
                            && beforeAfter.equalsIgnoreCase("Before")) {
                        Leave_Probation_End_Date = leaveCycleStartDate;
                    } else if (beforeAfter.equalsIgnoreCase("After") && ((LocalDate.parse(Leave_Probation_End_Date))
                            .isBefore(LocalDate.parse(leaveCycleStartDate))
                            || (LocalDate.parse(Leave_Probation_End_Date))
                            .isEqual(LocalDate.parse(leaveCycleStartDate)))) {
                        Leave_Probation_End_Date = DOJ;
                    }
                }
                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    LeaveCalBeginningDate = Leave_Probation_End_Date;
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }

                if (EndOfYear.equalsIgnoreCase("Yes")) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate).minusYears(1))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate).minusYears(1).toString();
                    }
                    if (LocalDate.parse(LeaveCalBeginningDate).isAfter(LocalDate.parse(leaveCycleStartDate))) {
                        return workingDaysBalance = 0;
                    } else if (LocalDate.parse(LeaveCalBeginningDate).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        if (LocalDate.parse(toDate).isAfter(LocalDate.parse(leaveCycleStartDate))) {
                            toDate = LocalDate.parse(leaveCycleStartDate).minusDays(1).toString();
                        }
                    }
                } else if (EndOfYear.equalsIgnoreCase("No")) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate).toString();
                    }

                    if (WorkingDaysEndOfMonth.equalsIgnoreCase("Yes") &&
                            WorkingDaysEndOfQuarter.equalsIgnoreCase("No") && WorkingDaysEndOfBiannual.equalsIgnoreCase("No")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle).minusMonths(1).toString();
                        }
                    } else if (WorkingDaysEndOfMonth.equalsIgnoreCase("No") && WorkingDaysEndOfQuarter.equalsIgnoreCase("Yes")
                            && WorkingDaysEndOfBiannual.equalsIgnoreCase("No")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle).minusMonths(3).toString();
                        }
                    } else if (WorkingDaysEndOfMonth.equalsIgnoreCase("No") && WorkingDaysEndOfQuarter.equalsIgnoreCase("No")
                            && WorkingDaysEndOfBiannual.equalsIgnoreCase("Yes")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle).minusMonths(6).toString();
                        }
                    }
                }

                if (EndOfYear.equalsIgnoreCase("Yes")) {
                    daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).minusYears(1)
                            .lengthOfYear();
                } else {
                    daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).lengthOfYear();
                }

                double workingDays = getWorkingDaysToConsiderForCalculation(LeaveCalBeginningDate, toDate);
                workingDaysBalance = Leaves_Allowed_Per_Year * (workingDays / daysConsiderForCalculation);
                if (beforeAfter.equalsIgnoreCase("After")) {
                    beforeProbationFlag = false;
                }
            }
            return workingDaysBalance;
        } catch (Exception e) {
            Reporter("Exception while calculating working days leave balance", "Error");
            throw new RuntimeException();
        }
    }

    /**
     * This method calculates working days leave balance
     *
     * @param DOJ
     * @param toDate
     * @return
     */
    public double calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(String DOJ, String toDate,
                                                                                  String beforeAfter) {
        try {
            double workingDaysBalance = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);

            if (beforeAfter.equalsIgnoreCase("Before")) {
                beforeProbationFlag = checkDOJisUnderLeaveProbationPeriod(DOJ);
            }

            if (beforeProbationFlag == true) {
                workingDaysBalance = 0;
            } else if (beforeProbationFlag == false) {

                if (!EndOfYear.equalsIgnoreCase("Yes")) {
                    if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        DOJ = leaveCycleStartDate;
                    }

                    if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))
                            && beforeAfter.equalsIgnoreCase("Before")) {
                        Leave_Probation_End_Date = leaveCycleStartDate;
                    } else if (beforeAfter.equalsIgnoreCase("After") && ((LocalDate.parse(Leave_Probation_End_Date))
                            .isBefore(LocalDate.parse(leaveCycleStartDate))
                            || (LocalDate.parse(Leave_Probation_End_Date))
                            .isEqual(LocalDate.parse(leaveCycleStartDate)))) {
                        Leave_Probation_End_Date = DOJ;
                    }
                } else if (EndOfYear.equalsIgnoreCase("Yes")) {
                    if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate).minusYears(1))) {
                        DOJ = LocalDate.parse(leaveCycleStartDate).minusYears(1).toString();
                    }
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    LeaveCalBeginningDate = Leave_Probation_End_Date;
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }
                double daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate())
                        .lengthOfYear();
                double workingDays = getWorkingDaysToConsiderForCalculation(LeaveCalBeginningDate, toDate);
                workingDaysBalance = Leaves_Allowed_Per_Year * (workingDays / daysConsiderForCalculation);
            }

            if (beforeAfter.equalsIgnoreCase("After")) {
                beforeProbationFlag = false;
            }

            return workingDaysBalance;
        } catch (Exception e) {
            Reporter("Exception while calculating working days leave balance", "Error");
            throw new RuntimeException();
        }
    }

    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyEffectiveTenureBasedLeaveBalanceForFourEdgeDays(String effectiveDate) {
        try {
            if (EndOfYear != null && EndOfYear.isEmpty() && EndOfYear.equalsIgnoreCase("Yes")) {
                return verifyEmployeeTenureBasedLeaveBalanceForFourEdgeDaysEndOfYear();
            } else {
                Reporter("System Date Time is set to : '" + DateTimeHelper.getCurrentLocalDateAndTime(), "Info");
                String result = "";
                int flag = 0;
                double expectedBalance = 0;
                double expectedLeaveBalanceBeforeAnniversary = 0;
                double expectedLeaveBalanceAfterAnniversary = 0;
                Long TempCreditFromYearLong;
                String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
                String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
                List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
                List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
                List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

                int anniversaryIterator = 0;
                while ((anniversaryIterator < creditFromYearList.size())
                        && (anniversaryIterator < creditToYearList.size())
                        && (anniversaryIterator < creditNoOfLeavesList.size())) {

                    String CreditFromYearVar;
                    String CreditToYearVar;
                    String CreditNoOfLeavesVar;

                    long CreditFromYearVarLong;
                    long CreditToYearVarLong;
                    long CreditNoOfLeavesVarLong;

                    CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                    CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                    CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                    CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                    CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                    CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                    if (anniversaryIterator > 0) {
                        Leaves_Allowed_Per_Year = Double
                                .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
                    }
                    int tenure = 0;
                    if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
                        LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                        LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

                        LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
                                .minusYears(CreditToYearVarLong - 1);

                        LocalDate lastDateForCalculation;
                        Long firstCreditFromYear = Long.valueOf(creditFromYearList.get(0).trim());
                        LocalDate tenureFirstLastDate = leaveCycleLastDateInDateFormat.minusYears(firstCreditFromYear);

                        if (anniversaryIterator == 0 && CreditFromYearVarLong != 1) {
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(1);
                        } else if (anniversaryIterator == creditFromYearList.size() - 1) {
                            startDateForCalculation = leaveCycleStartDateInDateFormat.minusYears(CreditToYearVarLong);
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                        } else {
                            lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(CreditFromYearVarLong);
                        }

                        int anniversaryYear = anniversaryIterator + 1;

                        Reporter("Leaves will be calculated for ~" + anniversaryYear + "~ anniversary", "Info");
                        Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                                + " hence leave balance will be verifed from '" + lastDateForCalculation + "' till '"
                                + startDateForCalculation, "Info");

                        int i = 0;
                        LocalDate iterationDate = lastDateForCalculation;

                        TempCreditFromYearLong = CreditFromYearVarLong;

                        while ((iterationDate.isAfter(startDateForCalculation.minusDays(-1))
                                || iterationDate.isEqual(startDateForCalculation.minusDays(-1)))
                                && (iterationDate.isBefore(lastDateForCalculation.minusDays(-1))
                                || iterationDate.isEqual(lastDateForCalculation.minusDays(-1)))) {
                            iterationDate = lastDateForCalculation.minusYears(i);

                            // if (iterationDate.isEqual(LocalDate.parse("2015-07-15"))) {
                            // System.out.println("Stop");
                            // tenureDOJFlag = 1;
                            // }

                            LocalDate tenureLastDate = leaveCycleLastDateInDateFormat
                                    .minusYears(CreditFromYearVarLong + 1);
                            LocalDate tenureStartDate = leaveCycleStartDateInDateFormat
                                    .minusYears(CreditToYearVarLong - 1);

                            if ((iterationDate.isBefore(tenureLastDate) && iterationDate.isAfter(tenureStartDate))
                                    /* || iterationDate.isEqual(tenureStartDate) */ || iterationDate
                                    .isEqual(tenureLastDate)
                                    || iterationDate.isAfter(tenureFirstLastDate)) {
                                tenure = 1;
                            } else {
                                tenure = 0;
                            }

                            LocalDate lastDayOfIterationLeaveCycle = LocalDate
                                    .parse(getLastDayofLeaveCycle(Leave_Cycle, iterationDate.toString()));
                            if (iterationDate.isEqual(lastDayOfIterationLeaveCycle) && i != 0
                                    && iterationDate.isBefore(tenureFirstLastDate)) {
                                TempCreditFromYearLong = TempCreditFromYearLong + 1;
                                Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                // removeEmployeeLeaveLogs();
                            }
                            // && tenureDOJFlag == 1
//							if ((iterationDateFourTimesPerMonth(iterationDate) == true
//							/* || tenureCurrentMonthOfAnyYear(iterationDate) == true */)) {
//								DateOfJoining = changeEmployeeDOJ(iterationDate);
                            DateOfJoining = "2016-05-04";
//								getAllEmployeeTypesInInstance();
//								Assert.assertTrue(changeEmployeeType("Full Time"), "Click on Settings link");
//								getEmployeeData();
//								Assert.assertTrue(changeEmployeeType("Part Time"), "Click on Settings link");

                            LocalDate anniversaryDateInDateFormat = iterationDate
                                    .minusYears(-TempCreditFromYearLong);

                            if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                    && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {

                                if (anniversaryDateInDateFormat
                                        .isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                    expectedBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                            DateTimeHelper.getCurrentLocalDate());
                                } else if (tenure == 1) {
                                    expectedBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                            DateTimeHelper.getCurrentLocalDate());
                                } else {

                                    String anniversaryDate = anniversaryDateInDateFormat.toString();
                                    String anniversaryDateMinusOne = anniversaryDateInDateFormat.minusDays(1)
                                            .toString();
                                    expectedLeaveBalanceBeforeAnniversary = calculateTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                            DateOfJoining, anniversaryDateMinusOne, "Before");
                                    double temLeaveVar = Leaves_Allowed_Per_Year;
                                    Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                    if (anniversaryDateInDateFormat
                                            .isBefore(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))
                                            || anniversaryDateInDateFormat.isEqual(
                                            LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                        expectedLeaveBalanceAfterAnniversary = calculateTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                anniversaryDate, DateTimeHelper.getCurrentLocalDate(), "After");
                                    } else {
                                        expectedLeaveBalanceAfterAnniversary = 0;
                                    }
                                    Leaves_Allowed_Per_Year = temLeaveVar;
                                    expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                            + expectedLeaveBalanceAfterAnniversary;
                                }
                            } else {
                                if (anniversaryDateInDateFormat
                                        .isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                                    expectedBalance = calculateEffectiveDateLeaveBalance(DateOfJoining,
                                            DateTimeHelper.getCurrentLocalDate(), "After", effectiveDate);
                                } else if (tenure == 1) {
                                    expectedBalance = calculateEffectiveDateLeaveBalance(DateOfJoining,
                                            DateTimeHelper.getCurrentLocalDate(), "After", effectiveDate);
                                } else {

                                    String anniversaryDate = anniversaryDateInDateFormat.toString();
                                    expectedBalance = calculateEffectiveDateLeaveBalance(DateOfJoining,
                                            DateTimeHelper.getCurrentLocalDate(), "After", effectiveDate);
                                    double temLeaveVar = Leaves_Allowed_Per_Year;
                                    Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                    Leaves_Allowed_Per_Year = temLeaveVar;
//										expectedBalance = expectedLeaveBalanceBeforeAnniversary
//												+ expectedLeaveBalanceAfterAnniversary;
                                }
                            }
                            expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

                            double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
                            if (expectedBalance != actualBalance) {
                                Reporter(
                                        "Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                + expectedBalance + "||Actual Leave Balance=" + actualBalance,
                                        "Fail");
                                result = "Fail";
                                flag++;
                            } else {
                                Reporter(
                                        "Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                + expectedBalance + "||Actual Leave Balance=" + actualBalance,
                                        "Pass");
                                result = "Pass";
                            }

                            if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
                                        DateTimeHelper.getCurrentLocalDateAndTime());
                            }
//							}
                            i++;
                        }
                    } else {
                        Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                    }
                    anniversaryIterator++;
                }
                return flag <= 0;
            }
        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method calculate leave balance
     *
     * @param DOJ
     * @param LastDayOfCalculation
     * @param beforeAfter
     * @param effectiveDate
     * @return double
     */
    public double calculateEffectiveDateTenureBasedLeaveBalance(String DOJ, String LastDayOfCalculation, String beforeAfter, String effectiveDate) {
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

            if (beforeAfter.equalsIgnoreCase("Before")) {
                beforeProbationFlag = checkDOJisUnderLeaveProbationPeriod(DOJ);
            }

            if (beforeProbationFlag == true) {
                ExpectedLeaveBalance = 0;

            } else if (beforeProbationFlag == false) {

                DOJ = effectiveDate;
                if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    DOJ = leaveCycleStartDate;
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    Leave_Probation_End_Date = effectiveDate;
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        LeaveCalBeginningDate = Leave_Probation_End_Date;
                    } else {
                        if (LocalDate.parse(Leave_Probation_End_Date).isAfter(LocalDate.parse(DOJ))) {
                            LeaveCalBeginningDate = Leave_Probation_End_Date;
                        } else {
                            LeaveCalBeginningDate = DOJ;
                        }
                    }
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                }

                if (Pro_rata.equalsIgnoreCase("Yes")) {

                    if (Accrual.equalsIgnoreCase("Yes")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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

                        if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                                && Calculate_after_probation_period.equalsIgnoreCase("Yes") && LocalDate
                                .parse(Leave_Probation_End_Date).isAfter(LastDayOfCalculationInDateFormat)) {
                            MonthOrQuarterDifference = 0;
                        }
                    }

                } else if (Pro_rata.equalsIgnoreCase("No")) {

                    if (beforeAfter.equalsIgnoreCase("Before")) {
                        leavesCalculationStartDate = leaveCycleStartDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        }
                    } else if (beforeAfter.equalsIgnoreCase("After")) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }

                    if (Accrual.equalsIgnoreCase("No") || Accrual.equalsIgnoreCase("WorkingDays")) {
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
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        } else {
                            if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 24);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = 0;
                            }
                        }
                    } else if (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
                            && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                        } else {
                            midJoinigYesLeaves = 0;
                        }
                    } else if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
                            && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
                        if (beforeAfter.equalsIgnoreCase("Before")) {
                            if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = 0;
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LastDayOfCalculation)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            }
                        } else {
                            if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                                midJoinigYesLeaves = (Leaves_Allowed_Per_Year / 12);
                            } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                    .equalsIgnoreCase("No")) {
                                midJoinigYesLeaves = 0;
                            }
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

                        if (beforeAfter.equalsIgnoreCase("After")) {
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
                            tempFlag = checkIfDayIsLastDayOfMonth(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate());
                        } else if (Quarterly.equalsIgnoreCase("Yes")) {
                            tempFlag = checkIfDayIsLastDayOfCurrentQuarter(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate());
                        } else {
                            tempFlag = false;
                        }
                        if (tempFlag == true) {
                            MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        } else if (tempFlag == false) {
                            midJoinigYesLeaves = 0;
                            if (checkIfDOJFallsInCurrentQuarter(LastDayOfCalculationInDateFormat, DateTimeHelper.getCurrentLocalDate()) == true) {
                                truncateLeaves = 0;
                                midJoinigYesLeaves = 0;
                            }
                        }
                    }
                }

                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) - (truncateLeaves) + biannualLeave);
            }
            // double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance *
            // 100.0) / 100.0;

            double ExpectedLeaveBalanceRoundOff = ExpectedLeaveBalance;

            if (ExpectedLeaveBalanceRoundOff < 0) {
                ExpectedLeaveBalanceRoundOff = 0;
            }

            if (beforeAfter.equalsIgnoreCase("After")) {
                beforeProbationFlag = false;
            }
            return ExpectedLeaveBalanceRoundOff;
        } catch (Exception e) {
            Reporter("Exception while calculating employess expected leave balance", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public LocalDate getLastDayOfMonth_Quarter_Biannual(String date, String monthQuarterBiannual, String leaveCycle) {
        try {
            if (monthQuarterBiannual.equalsIgnoreCase("Month")) {
                return LocalDate.parse(date).withDayOfMonth(LocalDate.parse(date).lengthOfMonth());
            } else if (monthQuarterBiannual.equalsIgnoreCase("Quarter")) {
                return objDateTimeHelper.getLastDayOfQuarter(date);
            } else if (monthQuarterBiannual.equalsIgnoreCase("Biannual")) {
                return getLastDayOfBiannual(date, leaveCycle);
            } else {
                throw new RuntimeException("Variable is not month, quarter or biannual");
            }

        } catch (Exception e) {
            throw new RuntimeException("Exception while getting last day of " + monthQuarterBiannual + ":" + e);
        }
    }

    public LocalDate getLastDayOfBiannual(String DATEIN_YYYY_MM_DD_format, String leaveCycle) {
        try {
            String[] arr = DATEIN_YYYY_MM_DD_format.split("-");
            int year = Integer.parseInt(arr[0]);
            LocalDate lastDayOfBiannual = null;
            int biannualEndMonth = 0;
            if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                biannualEndMonth = 6;
            } else if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                biannualEndMonth = 9;
            }

            LocalDate biannualEndDate = LocalDate.of(year, biannualEndMonth, 01).with(lastDayOfMonth());

            if (biannualEndDate.isAfter(LocalDate.parse(DATEIN_YYYY_MM_DD_format))) {
                lastDayOfBiannual = biannualEndDate;
            } else if (biannualEndDate.isBefore(LocalDate.parse(DATEIN_YYYY_MM_DD_format))) {
                lastDayOfBiannual = LocalDate.parse(getLastDayofLeaveCycle(leaveCycle));
            } else {
                lastDayOfBiannual = biannualEndDate;
            }

            return lastDayOfBiannual;
        } catch (Exception e) {
            Reporter("Exception while calculation last day of Biannual Half", "Error");
            throw new RuntimeException("Exception while calculation last day of Biannual Half");
        }
    }

    /**
     * This method calculate carry forward balance as per parameter all unused leave/ fixed/ percentage
     *
     * @param expectedLeaveBalance
     * @return
     */
    public double calculateCarryForwardBalance(double expectedLeaveBalance) {
        try {
            double expectedCarryForwardBalance = 0;

            if (CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("All")) {
                expectedCarryForwardBalance = expectedLeaveBalance;
            } else if (CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("Fixed")) {
                double fixedValue = Double.valueOf(FixedOrPercentageValue);
                if (fixedValue > expectedLeaveBalance) {
                    expectedCarryForwardBalance = expectedLeaveBalance;
                } else if (fixedValue <= expectedLeaveBalance) {
                    expectedCarryForwardBalance = fixedValue;
                }
            } else if (CarryForwardAllOrFixedOrPercentage.equalsIgnoreCase("Percentage")) {
                double percentageValue = Double.valueOf(FixedOrPercentageValue);
                expectedCarryForwardBalance = ((expectedLeaveBalance * percentageValue) / 100);
            } else {
                throw new RuntimeException("Parameters provided to calculate carry forward balance are not proper.");
            }
            return expectedCarryForwardBalance;
        } catch (Exception e) {
            Reporter("Exception while calculating carry forward leave balance", "Error");
            throw new RuntimeException("Exception while calculating carry forward leave balance :" + e);
        }
    }

    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyMultipleAllotmentLeaveTransferBalanceForCurrentCycle() {
        try {
            String result = "";
            int flag = 0;
            double expectedLeaveBalanceBeforeTransfer = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
            double expectedLeaveBalanceAfterTransfer = 0;
            LocalDate customCurrentDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            List<String> multipleAllotmentRestrictionsList = Arrays.asList(MultipleAllotmentRestrictions.split(","));
            List<String> allotedLeavesList = Arrays.asList(MultipleAllotmentLeaves.split(","));

            if (MultipleAllotmentEnabled.equalsIgnoreCase("Yes")) {
                LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

                LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat;
                LocalDate lastDateForCalculation = leaveCycleLastDateInDateFormat;

                Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                        + " hence leave balance will be verified from leave cycle start date '" + leaveCycleStartDate
                        + "' till " + lastDateForCalculation, "Info");
                int debugFlag = 0;
                DateOfJoining = changeEmployeeDOJ(startDateForCalculation); //Static for now
                int dojIterator = 0;
                while (LocalDate.parse(DateOfJoining).isBefore(startDateForCalculation.minusMonths(-2))) {
                    DateOfJoining = LocalDate.parse(DateOfJoining).minusDays(-dojIterator).toString();
                    if (iterationDateFourTimesPerMonth(LocalDate.parse(DateOfJoining)) == true) {
                        //                      LocalDate customCurrentDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate( ));

                        int customCurrentDateIterator = 0;
                        while (customCurrentDate.isAfter(LocalDate.parse(DateOfJoining).minusDays(-1))
                                && customCurrentDate.isBefore(lastDateForCalculation.minusDays(-1))) {
                            customCurrentDate = lastDateForCalculation.minusDays(customCurrentDateIterator);
                            int transferDateIterator = 1;
                            if (iterationDateFourTimesPerMonth(customCurrentDate) == true) {
                                objDateTimeHelper.changeServerDate(driver, customCurrentDate.toString());
                                LocalDate transferDate = customCurrentDate;
                                while (transferDate.isAfter(startDateForCalculation.minusDays(-1))
                                        && transferDate.isBefore(lastDateForCalculation.minusDays(-1))) {
                                    transferDate = customCurrentDate.minusDays(transferDateIterator);
/*
                                    if (transferDate.toString( ).equals("2018-01-15")) {
                                        System.out.println("Stop");
                                        debugFlag++;
                                    }*/
                                    if (iterationDateFourTimesPerMonth(transferDate) == true /*&& debugFlag > 0*/) {
                                        changeEmployeeType(multipleAllotmentRestrictionsList.get(1).trim(), multipleAllotmentRestrictionsList.get(0).trim());
                                        removeEmployeeLeaveLogs();
                                        changeEmployeeType(multipleAllotmentRestrictionsList.get(0).trim(), multipleAllotmentRestrictionsList.get(1).trim());
                                        changeLeaveTransferDate(transferDate.toString());

                                        if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                                && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                                            Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(0));
                                            expectedLeaveBalanceBeforeTransfer = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                                    DateOfJoining, transferDate.toString());
                                            Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(1));
                                            expectedLeaveBalanceAfterTransfer = calculateLeaveBalanceAsPerEmployeeWorkingDays(transferDate.toString(), transferDate.toString());

                                        } else {
                                            Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(0));
                                            expectedLeaveBalanceBeforeTransfer = calculateMultipleAllotmentLeaveBalance(
                                                    DateOfJoining, transferDate.toString(), customCurrentDate.toString(), "Before");
                                            Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(1));
                                            expectedLeaveBalanceAfterTransfer = calculateMultipleAllotmentLeaveBalance(
                                                    transferDate.toString(), customCurrentDate.toString(), customCurrentDate.toString(), "After");
                                        }

                                        double expectedBalance = expectedLeaveBalanceBeforeTransfer
                                                + expectedLeaveBalanceAfterTransfer;
                                        double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
                                        if (expectedBalance != actualBalance) {
                                            Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||Leave Transfer Date '" + transferDate.toString() + "||Current Date=" + customCurrentDate
                                                    + "||Expected Leave Balance=" + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
                                            result = "Fail";
                                            flag++;
                                        } else {
                                            Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||Leave Transfer Date '" + transferDate.toString() + "||Current Date=" + customCurrentDate + "||Expected Leave Balance="
                                                    + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
                                            result = "Pass";
                                        }
                                        if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                            writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
                                                    DateTimeHelper.getCurrentLocalDateAndTime());
                                        }
                                    }
                                    transferDateIterator++;
                                }
                            }
                            customCurrentDateIterator++;
                        }
                    }
                    dojIterator++;
                }

            } else {
                Reporter("Multiple Allotment Leave is selected as 'NO' or is not assigned", "Fail");
            }

            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing Multiple Allotment leave balance", "Error");
            return false;
        }
    }

    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyMultipleAllotmentLeaveTransferBalanceForMultipleTransfers() {
        try {
            String result = "";
            int flag = 0;
            double expectedLeaveBalanceBeforeFirstTransfer = 0;
            double expectedLeaveBalanceBeforeSecondTransfer = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
            double expectedLeaveBalanceAfterTransfer = 0;
            LocalDate customCurrentDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            List<String> multipleAllotmentRestrictionsList = Arrays.asList(MultipleAllotmentRestrictions.split(","));
            List<String> allotedLeavesList = Arrays.asList(MultipleAllotmentLeaves.split(","));

            if (MultipleAllotmentEnabled.equalsIgnoreCase("Yes")) {
                LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

                LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat;
                LocalDate lastDateForCalculation = leaveCycleLastDateInDateFormat;

                Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                        + " hence leave balance will be verified from leave cycle start date '" + leaveCycleStartDate
                        + "' till " + lastDateForCalculation, "Info");
                int debugFlag = 0;
                DateOfJoining = changeEmployeeDOJ(startDateForCalculation); //Static for now
                int dojIterator = 0;
                while (LocalDate.parse(DateOfJoining).isBefore(startDateForCalculation.minusMonths(-2))) {
                    DateOfJoining = LocalDate.parse(DateOfJoining).minusDays(-dojIterator).toString();
                    if (iterationDateFourTimesPerMonth(LocalDate.parse(DateOfJoining)) == true) {
                        //                      LocalDate customCurrentDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate( ));

                        int customCurrentDateIterator = 0;
                        while (customCurrentDate.isAfter(LocalDate.parse(DateOfJoining).minusDays(-1))
                                && customCurrentDate.isBefore(lastDateForCalculation.minusDays(-1))) {
                            customCurrentDate = lastDateForCalculation.minusDays(customCurrentDateIterator);
                            int firstTransferDateIterator = 1;
                            if (iterationDateFourTimesPerMonth(customCurrentDate) == true) {
                                objDateTimeHelper.changeServerDate(driver, customCurrentDate.toString());
                                LocalDate firstTransferDate = customCurrentDate;
                                while (firstTransferDate.isAfter(startDateForCalculation.minusDays(-1))
                                        && firstTransferDate.isBefore(lastDateForCalculation.minusDays(-1))) {
                                    firstTransferDate = customCurrentDate.minusDays(firstTransferDateIterator);
/*                                    if (transferDate.toString( ).equals("2018-01-15")) {
                                        System.out.println("Stop");
                                        debugFlag++;
                                    }*/
                                    if (iterationDateFourTimesPerMonth(firstTransferDate) == true /*&& debugFlag > 0*/) {

                                        int secondTransferDateIterator = 1;
                                        LocalDate secondTransferDate = firstTransferDate;
                                        while (secondTransferDate.isAfter(firstTransferDate.minusDays(1))
                                                && secondTransferDate.isBefore(lastDateForCalculation.minusDays(-1))) {
                                            secondTransferDate = customCurrentDate.minusDays(secondTransferDateIterator);
                                            if (iterationDateFourTimesPerMonth(secondTransferDate) == true /*&& debugFlag > 0*/) {
                                                changeEmployeeType(multipleAllotmentRestrictionsList.get(1).trim(), multipleAllotmentRestrictionsList.get(0).trim());
                                                removeEmployeeLeaveLogs();
                                                changeEmployeeType(multipleAllotmentRestrictionsList.get(0).trim(), multipleAllotmentRestrictionsList.get(1).trim());
                                                changeLeaveTransferDate(firstTransferDate.toString());

                                                changeEmployeeType(multipleAllotmentRestrictionsList.get(1).trim(), multipleAllotmentRestrictionsList.get(2).trim());
                                                changeLeaveTransferDate(secondTransferDate.toString());

                                                if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                                        && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(0));
                                                    expectedLeaveBalanceBeforeFirstTransfer = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                                            DateOfJoining, firstTransferDate.toString());
                                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(1));
                                                    expectedLeaveBalanceBeforeSecondTransfer = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                                            firstTransferDate.toString(), secondTransferDate.toString());
                                                    expectedLeaveBalanceAfterTransfer = calculateLeaveBalanceAsPerEmployeeWorkingDays(secondTransferDate.toString(), customCurrentDate.toString());

                                                } else {
                                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(0));
                                                    expectedLeaveBalanceBeforeFirstTransfer = calculateMultipleAllotmentLeaveBalance(
                                                            DateOfJoining, firstTransferDate.toString(), customCurrentDate.toString(), "Before");
                                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(1));
                                                    expectedLeaveBalanceBeforeSecondTransfer = calculateMultipleAllotmentLeaveBalance(
                                                            firstTransferDate.toString(), secondTransferDate.toString(), customCurrentDate.toString(), "Before");
                                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(2));
                                                    expectedLeaveBalanceAfterTransfer = calculateMultipleAllotmentLeaveBalance(
                                                            secondTransferDate.toString(), customCurrentDate.toString(), customCurrentDate.toString(), "After");
                                                }

                                                System.out.println(expectedLeaveBalanceBeforeFirstTransfer + ":" + expectedLeaveBalanceBeforeSecondTransfer
                                                        + ":" + expectedLeaveBalanceAfterTransfer);
                                                double expectedBalance = expectedLeaveBalanceBeforeFirstTransfer + expectedLeaveBalanceBeforeSecondTransfer
                                                        + expectedLeaveBalanceAfterTransfer;
                                                double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
                                                if (expectedBalance != actualBalance) {
                                                    Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||First Leave Transfer Date '" + firstTransferDate.toString() + "'||Second Leave Transfer Date '" + secondTransferDate.toString() + "||Current Date=" + customCurrentDate
                                                            + "||Expected Leave Balance=" + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
                                                    result = "Fail";
                                                    flag++;
                                                } else {
                                                    Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||First Leave Transfer Date '" + firstTransferDate.toString() + "'||Second Leave Transfer Date '" + secondTransferDate.toString() + "||Current Date=" + customCurrentDate + "||Expected Leave Balance="
                                                            + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
                                                    result = "Pass";
                                                }
                                                if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                                    writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
                                                            DateTimeHelper.getCurrentLocalDateAndTime());
                                                }
                                            }
                                            secondTransferDateIterator++;
                                        }
                                    }
                                    firstTransferDateIterator++;
                                }
                            }
                            customCurrentDateIterator++;
                        }
                    }
                    dojIterator++;
                }

            } else {
                Reporter("Multiple Allotment Leave is selected as 'NO' or is not assigned", "Fail");
            }

            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing Multiple Allotment leave balance", "Error");
            return false;
        }
    }


    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyMultipleAllotmentLeaveTransferBalanceOnDayOfTransfer() {
        try {
            multipleAllotmentCheckOnDayOfTransfer = "Yes";
            String result = "";
            int flag = 0;
            int debugFlag = 0;
            double expectedLeaveBalanceBeforeTransfer = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleLastDate = getLastDayofLeaveCycle(Leave_Cycle);
            double expectedLeaveBalanceAfterTransfer = 0;
            List<String> multipleAllotmentRestrictionsList = Arrays.asList(MultipleAllotmentRestrictions.split(","));
            List<String> allotedLeavesList = Arrays.asList(MultipleAllotmentLeaves.split(","));

            if (MultipleAllotmentEnabled.equalsIgnoreCase("Yes")) {
                LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleLastDate);

                LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat;
                LocalDate lastDateForCalculation = leaveCycleLastDateInDateFormat;

                Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                        + " hence leave balance will be verified from leave cycle start date '" + leaveCycleStartDate
                        + "' till " + lastDateForCalculation, "Info");
                DateOfJoining = changeEmployeeDOJ(startDateForCalculation);
                //Static for now
                int dojIterator = 0;
                while (LocalDate.parse(DateOfJoining).isBefore(startDateForCalculation.minusMonths(-2))) {
                    DateOfJoining = startDateForCalculation.minusDays(-dojIterator).toString();
                    if (iterationDateTwoTimesPerMonth(LocalDate.parse(DateOfJoining)) == true) {
                        DateOfJoining = changeEmployeeDOJ(LocalDate.parse(DateOfJoining));
                        int transferDateIterator = 1;
                        LocalDate transferDate = lastDateForCalculation;
                        LocalDate startDateForTransfer;
                        while (transferDate.isAfter(LocalDate.parse(DateOfJoining).minusDays(-1))
                                && transferDate.isBefore(lastDateForCalculation.minusDays(-1))) {
                            transferDate = lastDateForCalculation.minusDays(transferDateIterator);

/*
                            if (DateOfJoining.equals("2019-02-16") && transferDate.toString( ).equals("2019-03-16")) {
                                System.out.println("Stop");
                                debugFlag++;
                            }
*/

                            if (iterationDateFourTimesPerMonth(transferDate, "Yes") == true /*&& debugFlag > 0*/) {
                                changeEmployeeType(multipleAllotmentRestrictionsList.get(1).trim(), multipleAllotmentRestrictionsList.get(0).trim());
                                removeEmployeeLeaveLogs();
                                objDateTimeHelper.changeServerDate(driver, transferDate.toString());
                                changeEmployeeType(multipleAllotmentRestrictionsList.get(0).trim(), multipleAllotmentRestrictionsList.get(1).trim());

                                if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                        && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(0));
                                    expectedLeaveBalanceBeforeTransfer = calculateLeaveBalanceAsPerEmployeeWorkingDays(
                                            DateOfJoining, transferDate.toString());
                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(1));
                                    expectedLeaveBalanceAfterTransfer = calculateLeaveBalanceAsPerEmployeeWorkingDays(transferDate.toString(), transferDate.toString());

                                } else {
                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(0));
                                    expectedLeaveBalanceBeforeTransfer = calculateMultipleAllotmentLeaveBalance(
                                            DateOfJoining, transferDate.toString(), transferDate.toString(), "Before");
                                    Leaves_Allowed_Per_Year = Double.valueOf(allotedLeavesList.get(1));
                                    expectedLeaveBalanceAfterTransfer = calculateMultipleAllotmentLeaveBalance(
                                            transferDate.toString(), transferDate.toString(), transferDate.toString(), "After");
                                }

                                double expectedBalance = expectedLeaveBalanceBeforeTransfer
                                        + expectedLeaveBalanceAfterTransfer;
                                double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type);
                                if (expectedBalance != actualBalance) {
                                    Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||Leave Transfer Date '" + transferDate.toString() + "||Expected Leave Balance="
                                            + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
                                    result = "Fail";
                                    flag++;
                                } else {
                                    Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||Leave Transfer Date '" + transferDate.toString() + "||Expected Leave Balance="
                                            + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
                                    result = "Pass";
                                }
                                if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                    writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
                                            DateTimeHelper.getCurrentLocalDateAndTime());
                                }
                            }
                            transferDateIterator++;
                        }
                    }
                    dojIterator++;
                }

            } else {
                Reporter("Multiple Allotment Leave is selected as 'NO' or is not assigned", "Fail");
            }

            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing Multiple Allotment leave balance", "Error");
            return false;
        }
    }

    // LeaveCalBeginningDate = leaveCycleStartDate;
    // System.out.println("LeaveCalBeginningDate--->"+ LeaveCalBeginningDate);
    // System.out.println("toDate---->"+ toDate);
    // if (Pro_rata.equalsIgnoreCase("Yes")) {
    // if (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
    // && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
    // if
    // (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes"))
    // {
    // midJoinigDeductionDays = 15;
    // } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
    // .equalsIgnoreCase("No")) {
    // midJoinigDeductionDays = 0;
    // }
    // } else if
    // (Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("Yes")
    // && Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
    // midJoinigDeductionDays = 0;
    // } else if
    // (Half_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")
    // && Full_Month_Leaves_if_employee_joins_after_15th.equalsIgnoreCase("No")) {
    // if
    // (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes"))
    // {
    // midJoinigDeductionDays = 15;
    // // midJoinigYesLeaves = 0;
    // } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
    // .equalsIgnoreCase("No")) {
    // midJoinigDeductionDays = 0;
    // }
    // }
    // }

    /**
     * This method gets employees leave balance shown in front end
     *
     * @param leaveType
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndLeaveBalance(String leaveType) {
        try {
            double actualLeaveBalance = 0;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/emailtemplate/Employeeleave?id=" + EMPID + "&leave=" + leaveType;
                driver.navigate().to(URL);
                String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
                if (frontEndLeaveBalance.isEmpty()) {
                    Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }
                actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                actualLeaveBalance = getEmployeesFrontEndLeaveBalanceUsingAPIs(leaveType);
            }
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean getMobileAuthToken() {
        try {
            RestAssured.baseURI = data.get("@@url") + "Mobileapi/auth";

            RequestSpecification request = RestAssured.given();

            JSONObject params = new JSONObject();
            params.put("username", ObjectRepo.reader.getAdminUserName());
            params.put("password", ObjectRepo.reader.getAdminPassword());

            request.body(params.toString());

            Response response = request.post();
            ResponseBody body = response.getBody();

            MobileAuthReponse responsebody = body.as(MobileAuthReponse.class);
            authToken = responsebody.token;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while getting authentication token", "Error");
            throw new RuntimeException();
        }
    }


    /**
     * This method gets employees leave balance shown in front end
     *
     * @param leaveType
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndLeaveBalanceUsingAPIs(String leaveType) {
        try {
            String applicationURL = data.get("@@url");
            String URL = applicationURL + "Mobileapi/Employeeleave";
            RestAssured.baseURI = URL;

            RequestSpecification request = RestAssured.given();

            JSONObject params = new JSONObject();
            params.put("token", authToken);
            params.put("id", EMPID);
            params.put("leave", leaveType);

            request.body(params.toString());

            Response response = request.post();

            String frontEndLeaveBalance = response.body().asString().trim();
            if (frontEndLeaveBalance.isEmpty()) {
                Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
            }
            double actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets employees leave balance shown in front end
     *
     * @param leaveType
     * @param deactivationDate
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndDeactivationLeaveBalanceUsingAPIs(String leaveType, String deactivationDate) {
        try {
            String applicationURL = data.get("@@url");
            String URL = applicationURL + "Mobileapi/Employeeleaved";
            RestAssured.baseURI = URL;

            RequestSpecification request = RestAssured.given();

            JSONObject params = new JSONObject();
            params.put("token", authToken);
            params.put("id", EMPID);
            params.put("leave", leaveType);
            params.put("date", deactivationDate);

            request.body(params.toString());

            Response response = request.post();

            String frontEndLeaveBalance = response.body().asString().trim();
            if (frontEndLeaveBalance.isEmpty()) {
                Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
            }
            double actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public JSONObject getWorkingDaysDetailsOfEmployeeFromApplication(String fromDate, String toDate) {
        try {
            JSONObject jsonObject = null;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/Emailtemplate/Workingdays?id=" + EMPID + "&from=" + fromDate
                        + "&enddate=" + toDate;
                driver.navigate().to(URL);
                String frontEndValue = driver.findElement(By.xpath("//body")).getText();
                if (frontEndValue.isEmpty()) {
                    Reporter("May be Employee id is not present", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }

                JSONParser parser = new JSONParser();
                jsonObject = (JSONObject) parser.parse(frontEndValue);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                // actualLeaveBalance =
                // getEmployeesFrontEndDeactivationLeaveBalanceUsingAPIs(leaveType,
                // deactivationDate);
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while getting working days details from API.", "Error");
            throw new RuntimeException();
        }
    }

    /**
     * This method gets employees leave balance shown in front end
     *
     * @param leaveType
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndCarryForwardLeaveBalance(String leaveType) {
        try {
            double actualLeaveBalance = 0;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/emailtemplate/Employeeleave?id=" + EMPID + "&leave=" + leaveType;
                driver.navigate().to(URL);
                String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
                if (frontEndLeaveBalance.isEmpty()) {
                    Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }
                actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                actualLeaveBalance = getEmployeesFrontEndLeaveBalanceUsingAPIs(leaveType);
            }
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets employees leave balance shown in front end
     *
     * @param DOJ
     * @param leaveType
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndLeaveBalanceByDOJ(String DOJ, String leaveType) {
        try {
            double actualLeaveBalance = 0;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "emailtemplate/leave?doj=" + DOJ + "&id=" + EMPID + "&leave=" + leaveType;
                driver.navigate().to(URL);
                String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
                if (frontEndLeaveBalance.isEmpty()) {
                    Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }
                actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                actualLeaveBalance = getEmployeesFrontEndLeaveBalanceUsingAPIs(leaveType);
            }
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets employees deActivation leave balance shown in front end
     *
     * @param leaveType
     * @param deactivationDate
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndDeactivationLeaveBalance(String leaveType, String deactivationDate) {
        try {
            double actualLeaveBalance = 0;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "emailtemplate/Employeeleaved?id=" + EMPID + "&leave=" + leaveType
                        + "&date=" + deactivationDate;
                driver.navigate().to(URL);
                String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
                if (frontEndLeaveBalance.isEmpty()) {
                    Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }
                actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                actualLeaveBalance = getEmployeesFrontEndDeactivationLeaveBalanceUsingAPIs(leaveType, deactivationDate);
            }
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets employees deActivation leave balance shown in front end
     *
     * @param DOJ
     * @param leaveType
     * @param deactivationDate
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndDeactivationLeaveBalanceByDOJ(String DOJ, String leaveType,
                                                                    String deactivationDate) {
        try {
            double actualLeaveBalance = 0;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "emailtemplate/Leaved?doj=" + DOJ + "&id=" + EMPID + "&leave=" + leaveType
                        + "&date=" + deactivationDate;
                driver.navigate().to(URL);
                String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
                if (frontEndLeaveBalance.isEmpty()) {
                    Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }
                actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                actualLeaveBalance = getEmployeesFrontEndDeactivationLeaveBalanceUsingAPIs(leaveType, deactivationDate);
            }
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
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
            LocalDate iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            while (iterationDate.isAfter(leaveCycleStartDateInDateFormat)) {
                iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).minusDays(i);
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
                            DateTimeHelper.getCurrentLocalDateAndTime());
                }
            }

            return flag <= 0;

        } catch (

                Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }
    }

    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean  verifyEmployeeLeaveBalanceForWholeLeaveCycleForFourEdgeDays() {
        try {
            String result = "";
            int flag = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle); //Get first day of leave cycle
            Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                    + " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
                    + "' till current date", "Info");
            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate); //Get first day of leave cycle in Local date Format
            int i = 0;
            LocalDate iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()); //Set iteration starting date as Today's date
            while (iterationDate.isAfter(leaveCycleStartDateInDateFormat.minusYears(1))) { //This row iterates Date of Joining from current date till leave cycle start date
                iterationDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).minusDays(i); // Decrease iteration date by 1 day
                if (iterationDateFourTimesPerMonth(iterationDate) == true) {
                    DateOfJoining = changeEmployeeDOJ(iterationDate); //This function change employee DOJ
////                    if(DateOfJoining.equals("2018-09-30")){
//                        System.out.println("Stop");
//                    }
                    double expectedBalance = 0;
                    if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                            && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                        expectedBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                DateTimeHelper.getCurrentLocalDate()); //This function calculates leave balance in case of working days
                    } else {
                        expectedBalance = calculateLeaveBalance(DateOfJoining); //This important function calculates leave balance
                    }
                    double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type); //This gets employees leave balance from frontend
                    expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;
                    /*
                    In below code we are comparing calculated balance to actual balance shown in frontend
                     */
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
                                DateTimeHelper.getCurrentLocalDateAndTime());
                    }
                }
                i++;
            }
            return flag <= 0;

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
    public boolean verifyEmployeeDeactivationLeaveBalanceForWholeLeaveCycleForFourEdgeDays() {
        try {
            String result = "";
            int flag = 0;
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
            LocalDate leaveCycleEndDateInDateFormat = LocalDate.parse(leaveCycleEndDate);

            LocalDate deactivationIterationDate = leaveCycleEndDateInDateFormat;
            // LocalDate deactivationIterationDate =
            // LocalDate.parse(DateTimeHelper.getCurrentLocalDate());

            int j = 0;
            while (deactivationIterationDate.isAfter(leaveCycleStartDateInDateFormat.minusMonths(-6))) {
                deactivationIterationDate = leaveCycleEndDateInDateFormat.minusMonths(3).minusDays(j);
                if (iterationDateTwoTimesPerMonth(deactivationIterationDate) == true) {
                    Reporter("Leave Balance will be checked for Decativate date '"
                            + deactivationIterationDate.toString() + "'", "Info");
                    Reporter(
                            "Leave Cycle defined is '" + Leave_Cycle + "',"
                                    + " hence leave balance will be verifed from leave cycle end date '"
                                    + leaveCycleEndDate + "' till leave cycle start date '" + leaveCycleStartDate,
                            "Info");

                    LocalDate iterationDate = deactivationIterationDate;
                    int i = 1;
                    while (iterationDate.isAfter(leaveCycleStartDateInDateFormat.minusMonths(6))) {
                        iterationDate = deactivationIterationDate.minusDays(i);

                        if ((iterationDateTwoTimesPerMonth(iterationDate) == true)) {
                            DateOfJoining = changeEmployeeDOJ(iterationDate);
                            double expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                    deactivationIterationDate.toString(), "Before");
                            double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                                    deactivationIterationDate.toString());
                            if (expectedBalance != actualBalance) {
                                Reporter(
                                        "Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                                + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                        "Fail");
                                result = "Fail";
                                flag++;
                            } else {
                                Reporter(
                                        "Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                                + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                                + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                        "Pass");
                                result = "Pass";
                            }

                            if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(),
                                        DateOfJoining, expectedBalance, actualBalance, result,
                                        DateTimeHelper.getCurrentLocalDateAndTime());
                            }
                        }
                        i++;
                    }
                }
                j++;
            }
            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }
    }

    /**
     * This method verifies leave balance of an employee for a particular DOJ
     *
     * @param DOJ
     * @return boolean
     * @author shikhar
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
            return flag <= 0;
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
    public String  changeEmployeeDOJ(LocalDate iterationDate) {
        try {
            String DOJ = null;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DOJ = iterationDate.format(formatter);
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/emailtemplate/employeedoj?id=" + EMPID + "&date=" + DOJ;
                driver.navigate().to(URL);
                String frontEndDOJ = driver.findElement(By.xpath("//body")).getText();

                if (frontEndDOJ.trim().equals("DOJ Not changed")) {
                    for (int i = 0; i < 3; i++) {
                        driver.navigate().to(URL);
                        frontEndDOJ = driver.findElement(By.xpath("//body")).getText();
                        if (frontEndDOJ.trim().equals(DOJ)) {
                            break;
                        }
                    }
                }

                if (!frontEndDOJ.trim().equals(DOJ)) {
                    Reporter("DOJ not changed to '" + DOJ + "'", "Warning");
                }
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                DOJ = changeEmployeeDOJ(iterationDate);
            }
            return DOJ;
        } catch (Exception e) {
            Reporter("Exception while changing employees DOJ", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * This method changes Employee Date of Joining
     *
     * @param iterationDate
     * @return DOJ as String
     */
    public String changeEmployeeDOJToString(LocalDate iterationDate) {
        try {
            String DOJ = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DOJ = iterationDate.format(formatter);
            return DOJ;
        } catch (Exception e) {
            Reporter("Exception while changing employees DOJ", "Error");
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
            String todaysDate = DateTimeHelper.getCurrentLocalDate();
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

            return flag > 0;
        } catch (Exception e) {
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
    public boolean checkDOJisUnderLeaveProbationPeriod(String DOJ, String customCurrentDate) {
        try {
            String todaysDate = customCurrentDate;
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
                double daysDiff = objDateTimeHelper.getDaysDifferenceBetweenDOJAndCurrentDate(DOJ, customCurrentDate);
                double Employee_probation_period_Int = Double.valueOf(Employee_probation_period);
                if (daysDiff < Employee_probation_period_Int) {
                    flag++;
                }
                long daysToSubtract = (long) (-Employee_probation_period_Int);
                Leave_Probation_End_Date = LocalDate.parse(DOJ).minusDays(daysToSubtract).toString();
            } else {

                flag = 0;
            }

            return flag > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


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

    public void getAllEmployeeTypesInInstance() {
        try {
            String text = "emailtemplate/GetAllEmployeeTypes";
            String frontEndEmployeeType = objUtil.getHTMLTextFromAPI(driver, text);
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

    public void getEmployeeData() {
        try {
            String text = "emailtemplate/GetEmployeeData/id/" + EMPID;
            String frontEndEmployeeDataDetails = objUtil.getHTMLTextFromAPI(driver, text);
            String[] frontEndEmployeeDataDetailsSplit = frontEndEmployeeDataDetails.split("\\r?\\n");

            for (String line : frontEndEmployeeDataDetailsSplit) {
                String key = line.substring(0, line.indexOf("<"));
                String value = line.substring(line.indexOf(">") + 1);
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

            String text = "emailtemplate/SetEmployeeData/id/" + EMPID + "type/" + anotherEmployeeTypeID;
            objUtil.callTheAPI(driver, text);

            Reporter("Changed emaployees Employee Type from '" + EmployeeDataHmap.get(EmployeeDataEmployeeType)
                    + "' to '" + EmployeeTypeName.get(i), "Pass");
            String currentURL = driver.getCurrentUrl();
            String getEmployeeURL = data.get("@@url") + "emailtemplate/GetEmployeeData/id/" + EMPID;

            return currentURL.trim().equals(getEmployeeURL);

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
            String getEmployeeURL = data.get("@@url") + "emailtemplate/GetEmployeeData/id/" + EMPID;

            return currentURL.trim().equals(getEmployeeURL);

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

            Reporter("Changed employees Employee Type from '" + EmployeeDataHmap.get(EmployeeDataEmployeeType)
                    + "' to '" + empType, "Pass");
            String currentURL = driver.getCurrentUrl();
            String getEmployeeURL = data.get("@@url") + "emailtemplate/GetEmployeeData/id/" + EMPID;

            //    return currentURL.trim( ).equals(getEmployeeURL);
            return true;
        } catch (Exception e) {
            Reporter("Exception while changing Employee Type", "Error");
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeEmployeeType(String empType1, String empType2) {
        try {
            String anotherEmployeeTypeID = EmployeeTypesHmap.get(empType2.trim());

            String text = "emailtemplate/SetEmployeeData/id/" + EMPID + "/type/" + anotherEmployeeTypeID;
            objUtil.callTheAPI(driver, text);

/*
            Reporter("Changed employee's Employee Type from '" + empType1
                    + "' to '" + empType2, "Pass");
*/
            String currentURL = driver.getCurrentUrl();
            String getEmployeeURL = data.get("@@url") + "emailtemplate/GetEmployeeData/id/" + EMPID;

            return currentURL.trim().equals(getEmployeeURL);

        } catch (Exception e) {
            Reporter("Exception while changing Employee Type", "Error");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * This method removes Leave Log for employee
     *
     * @return
     */
    public boolean removeEmployeeLeaveLogs() {
        String URL = UtilityHelper.getProperty("allAPIRepository", "Remove.Leave.Log.API") + EMPID;
        return objUtil.callTheAPI(driver, URL);
    }

    /**
     * This method removes Carry forward Log for employee
     *
     * @return
     */
    public boolean removeEmployeeCarryForwardLeaveLogs() {
        String URL = UtilityHelper.getProperty("allAPIRepository", "Remove.Carry.Log.API") + EMPID;
        return objUtil.callTheAPI(driver, URL);
    }


    /**
     * This method change transfer date
     *
     * @return
     */
    public boolean changeLeaveTransferDate(String transferDate) {
        long epochTime = objDateTimeHelper.convertLocalDateToEpochDay(transferDate);
        String URL = UtilityHelper.getProperty("allAPIRepository", "Change.Transfer.Date") + EMPID + "&leave=" + Leave_Type + "&time=" + String.valueOf(epochTime);
        String htmlText = objUtil.getHTMLTextFromAPI(driver, URL);
        if (htmlText.contains("updated")) {
            return true;
        } else {
            Reporter("Leave Transfer date is not changed successfully, please check msg : " + htmlText, "Error");
            throw new RuntimeException("Leave Transfer date is not changed successfully, please check msg : " + htmlText);
        }
    }


    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyTenureDeactivationLeaveBalanceForFourEdgeDaysAndDeactivationAsCustomDate(
            String[] deactivationDateArray) {
        try {

            int arraysize = deactivationDateArray.length;
            int k = 0;
            String result = "";
            int flag = 0;

            while (k < arraysize) {
                String deactivationDate = deactivationDateArray[k];
                String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
                String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
                LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);

                LocalDate deactivationIterationDate = LocalDate.parse(deactivationDate);

                Reporter("Leave Balance will be checked for Decativate date '" + deactivationIterationDate.toString()
                        + "'", "Info");
                Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                        + " hence leave balance will be verifed from leave cycle end date '" + leaveCycleEndDate
                        + "' till leave cycle start date '" + leaveCycleStartDate, "Info");

                LocalDate iterationDate = deactivationIterationDate;
                int i = 1;
                while (iterationDate.isAfter(leaveCycleStartDateInDateFormat)) {
                    iterationDate = deactivationIterationDate.minusDays(i);
                    if (iterationDateFourTimesPerMonth(iterationDate) == true) {
                        DateOfJoining = changeEmployeeDOJ(iterationDate);
                        double expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                deactivationIterationDate.toString(), "Before");
                        double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                                deactivationIterationDate.toString());
                        if (expectedBalance != actualBalance) {
                            Reporter(
                                    "Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                            + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                            + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                    "Fail");
                            result = "Fail";
                            flag++;
                        } else {
                            Reporter(
                                    "Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                            + expectedBalance + "||Actual Leave Balance=" + actualBalance
                                            + "||Deactivation Date '" + deactivationIterationDate.toString() + "'",
                                    "Pass");
                            result = "Pass";
                        }

                        if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                            writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(), DateOfJoining,
                                    expectedBalance, actualBalance, result,
                                    DateTimeHelper.getCurrentLocalDateAndTime());
                        }
                    }
                    i++;
                }
                k++;
            }
            return flag <= 0;

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
    public boolean verifyTenureDeactivationLeaveBalanceForFourEdgeDaysCustomDatesNew(String[] deactivationDateArray) {
        try {
            if (EndOfYear.equalsIgnoreCase("Yes")) {
                return verifyTenureDeactivationLeaveBalanceForFourEdgeDaysCustomDatesNewFomEndOfYear(
                        deactivationDateArray);
            } else {
                String result = "";
                int flag = 0;
                double expectedBalance = 0;

                long TempCreditFromYearLong;
                double expectedLeaveBalanceBeforeAnniversary = 0;
                double expectedLeaveBalanceAfterAnniversary = 0;

                double tempVar = Leaves_Allowed_Per_Year;
                int arraysize = deactivationDateArray.length;
                int k = 0;

                while (k < arraysize) {
                    removeEmployeeLeaveLogs();
                    Leaves_Allowed_Per_Year = tempVar;
                    String deactivationDate = deactivationDateArray[k];
                    String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle, deactivationDate);
                    String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle, deactivationDate);

                    LocalDate deactivationIterationDate = LocalDate.parse(deactivationDate);
                    Reporter("Leave Balance will be checked for Decativate date '"
                            + deactivationIterationDate.toString() + "'", "Info");

                    // LocalDate iterationDate = deactivationIterationDate;

                    List<String> creditFromYearList = Arrays.asList(CreditFromYear.split(","));
                    List<String> creditToYearList = Arrays.asList(CreditToYear.split(","));
                    List<String> creditNoOfLeavesList = Arrays.asList(CreditNoOfLeaves.split(","));

                    int anniversaryIterator = 0;
                    while ((anniversaryIterator < creditFromYearList.size())
                            && (anniversaryIterator < creditToYearList.size())
                            && (anniversaryIterator < creditNoOfLeavesList.size())) {

                        String CreditFromYearVar;
                        String CreditToYearVar;
                        String CreditNoOfLeavesVar;

                        long CreditFromYearVarLong;
                        long CreditToYearVarLong;
                        long CreditNoOfLeavesVarLong;

                        CreditFromYearVar = creditFromYearList.get(anniversaryIterator).trim();
                        CreditToYearVar = creditToYearList.get(anniversaryIterator).trim();
                        CreditNoOfLeavesVar = creditNoOfLeavesList.get(anniversaryIterator).trim();

                        CreditFromYearVarLong = Long.valueOf(CreditFromYearVar);
                        CreditToYearVarLong = Long.valueOf(CreditToYearVar);
                        CreditNoOfLeavesVarLong = Long.valueOf(CreditNoOfLeavesVar);

                        if (anniversaryIterator > 0) {
                            Leaves_Allowed_Per_Year = Double
                                    .valueOf(creditNoOfLeavesList.get(anniversaryIterator - 1).trim());
                        }
                        int tenure = 0;
                        if (CreditOnTenureBasis.equalsIgnoreCase("Yes")) {
                            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate);
                            LocalDate leaveCycleLastDateInDateFormat = LocalDate.parse(leaveCycleEndDate);

                            LocalDate startDateForCalculation = leaveCycleStartDateInDateFormat
                                    .minusYears(CreditToYearVarLong - 1);

                            LocalDate lastDateForCalculation;
                            Long firstCreditFromYear = Long.valueOf(creditFromYearList.get(0).trim());
                            LocalDate tenureFirstLastDate = leaveCycleLastDateInDateFormat
                                    .minusYears(firstCreditFromYear);

                            if (anniversaryIterator == 0 && CreditFromYearVarLong != 1) {
                                lastDateForCalculation = leaveCycleLastDateInDateFormat.minusYears(1);
                            } else if (anniversaryIterator == creditFromYearList.size() - 1) {
                                startDateForCalculation = leaveCycleStartDateInDateFormat
                                        .minusYears(CreditToYearVarLong);
                                lastDateForCalculation = leaveCycleLastDateInDateFormat
                                        .minusYears(CreditFromYearVarLong);
                            } else {
                                lastDateForCalculation = leaveCycleLastDateInDateFormat
                                        .minusYears(CreditFromYearVarLong);
                            }

                            int anniversaryYear = anniversaryIterator + 1;

                            Reporter("Leaves will be calculated for ~" + anniversaryYear + "~ anniversary", "Info");
                            Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                                    + " hence leave balance will be verifed from '" + lastDateForCalculation
                                    + "' till '" + startDateForCalculation, "Info");

                            int i = 0;
                            LocalDate iterationDate = lastDateForCalculation;

                            TempCreditFromYearLong = CreditFromYearVarLong;

                            while ((iterationDate.isAfter(startDateForCalculation.minusDays(-1))
                                    || iterationDate.isEqual(startDateForCalculation.minusDays(-1)))
                                    && (iterationDate.isBefore(lastDateForCalculation.minusDays(-1))
                                    || iterationDate.isEqual(lastDateForCalculation.minusDays(-1)))) {
                                iterationDate = lastDateForCalculation.minusDays(i);

                                // if (iterationDate.isEqual(LocalDate.parse("2016-04-30"))) {
                                // System.out.println("Stop");
                                // tenureDOJFlag = 1;
                                // }

                                LocalDate tenureLastDate = leaveCycleLastDateInDateFormat
                                        .minusYears(CreditFromYearVarLong + 1);
                                LocalDate tenureStartDate = leaveCycleStartDateInDateFormat
                                        .minusYears(CreditToYearVarLong - 1);

                                if ((iterationDate.isBefore(tenureLastDate) && iterationDate.isAfter(tenureStartDate))
                                        /* || iterationDate.isEqual(tenureStartDate) */ || iterationDate.isEqual(
                                        tenureLastDate)
                                        || iterationDate.isAfter(tenureFirstLastDate)) {
                                    tenure = 1;
                                } else {
                                    tenure = 0;
                                }

                                LocalDate lastDayOfIterationLeaveCycle = LocalDate
                                        .parse(getLastDayofLeaveCycle(Leave_Cycle, iterationDate.toString()));
                                if (iterationDate.isEqual(lastDayOfIterationLeaveCycle) && i != 0
                                        && iterationDate.isBefore(tenureFirstLastDate)) {
                                    TempCreditFromYearLong = TempCreditFromYearLong + 1;
                                    Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                    // removeEmployeeLeaveLogs();
                                }
                                // && tenureDOJFlag == 1 || tenureCurrentMonthOfAnyYear(iterationDate) == true
                                if ((iterationDateFourTimesPerMonth(iterationDate) == true)) {
                                    DateOfJoining = changeEmployeeDOJ(iterationDate);

                                    LocalDate anniversaryDateInDateFormat = iterationDate
                                            .minusYears(-TempCreditFromYearLong);

                                    if (LeaveAccrualBasedOnWorkingDays != null
                                            && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                                            && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                                        if (anniversaryDateInDateFormat.isAfter(deactivationIterationDate)) {
                                            expectedBalance = calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                    DateOfJoining, deactivationIterationDate.toString(), "Before");
                                        } else if (tenure == 1) {
                                            expectedBalance = calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                    DateOfJoining, deactivationIterationDate.toString(), "Before");
                                        } else {
                                            String anniversaryDate = anniversaryDateInDateFormat.toString();
                                            String anniversaryDateMinusOne = anniversaryDateInDateFormat.minusDays(1)
                                                    .toString();
                                            expectedLeaveBalanceBeforeAnniversary = calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                    DateOfJoining, anniversaryDateMinusOne, "Before");
                                            double temLeaveVar = Leaves_Allowed_Per_Year;
                                            Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                            if (anniversaryDateInDateFormat.isBefore(deactivationIterationDate)
                                                    || anniversaryDateInDateFormat.isEqual(deactivationIterationDate)) {
                                                expectedLeaveBalanceAfterAnniversary = calculateDeactivationTenureLeaveBalanceAsPerEmployeeWorkingDays(
                                                        anniversaryDate, deactivationIterationDate.toString(), "After");
                                            } else {
                                                expectedLeaveBalanceAfterAnniversary = 0;
                                            }
                                            Leaves_Allowed_Per_Year = temLeaveVar;
                                            expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                                    + expectedLeaveBalanceAfterAnniversary;
                                        }
                                    } else {
                                        if (anniversaryDateInDateFormat.isAfter(deactivationIterationDate)) {
                                            expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                                    deactivationIterationDate.toString(), "Before");
                                        } else if (tenure == 1) {
                                            expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining,
                                                    deactivationIterationDate.toString(), "Before");
                                        } else {
                                            String anniversaryDate = anniversaryDateInDateFormat.toString();
                                            expectedLeaveBalanceBeforeAnniversary = calculateBeforeTenureBasedDeactivationLeaveBalance(
                                                    DateOfJoining, anniversaryDate, "Before");
                                            double temLeaveVar = Leaves_Allowed_Per_Year;
                                            Leaves_Allowed_Per_Year = CreditNoOfLeavesVarLong;
                                            if (anniversaryDateInDateFormat.isBefore(deactivationIterationDate)
                                                    || anniversaryDateInDateFormat.isEqual(deactivationIterationDate)) {
                                                expectedLeaveBalanceAfterAnniversary = calculateTenureDeactivationBasedLeaveBalance(
                                                        anniversaryDate, deactivationIterationDate.toString(), "After");
                                            } else {
                                                expectedLeaveBalanceAfterAnniversary = 0;
                                            }
                                            Leaves_Allowed_Per_Year = temLeaveVar;
                                        }
                                        expectedBalance = expectedLeaveBalanceBeforeAnniversary
                                                + expectedLeaveBalanceAfterAnniversary;
                                    }
                                    expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;
                                    double actualBalance = getEmployeesFrontEndDeactivationLeaveBalance(Leave_Type,
                                            deactivationIterationDate.toString());
                                    if (expectedBalance != actualBalance) {
                                        Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||"
                                                + "Expected Leave Balance=" + expectedBalance
                                                + "||Actual Leave Balance=" + actualBalance + "||Deactivation Date '"
                                                + deactivationIterationDate.toString() + "'", "Fail");
                                        result = "Fail";
                                        flag++;
                                    } else {
                                        Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||"
                                                + "Expected Leave Balance=" + expectedBalance
                                                + "||Actual Leave Balance=" + actualBalance + "||Deactivation Date '"
                                                + deactivationIterationDate.toString() + "'", "Pass");
                                        result = "Pass";
                                    }

                                    if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                                        writeDeactivationLeavesResultToExcel(deactivationIterationDate.toString(),
                                                DateOfJoining, expectedBalance, actualBalance, result,
                                                DateTimeHelper.getCurrentLocalDateAndTime());
                                    }
                                }
                                i++;
                            }
                        } else {
                            Reporter("Credit on Tenure Basis is selected as 'NO' or is not assigned", "Fail");
                        }
                        anniversaryIterator++;
                    }
                    k++;
                }

                return flag <= 0;
            }
        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            e.printStackTrace();
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    public String changeEmployeeDOJUsingAPIs(LocalDate iterationDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String DOJ = iterationDate.format(formatter);
            String applicationURL = data.get("@@url");
            String URL = applicationURL + "Mobileapi/employeedoj";
            RestAssured.baseURI = URL;

            RequestSpecification request = RestAssured.given();

            JSONObject params = new JSONObject();
            params.put("token", authToken);
            params.put("id", EMPID);
            params.put("date", DOJ);

            request.body(params.toString());

            Response response = request.post();
            ResponseBody body = response.getBody();

            return DOJ;
        } catch (Exception e) {
            Reporter("Exception while changing employees DOJ", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }

    }


    public double getWorkingDaysToConsiderForCalculation(String fromDate, String toDate) {
        try {
            double daysToAdd = 0;
            JSONObject workingDaysJsonObj = getWorkingDaysDetailsOfEmployeeFromApplication(fromDate, toDate);
            Double presentDays = Double.valueOf(workingDaysJsonObj.get("present").toString());
            Double weeklyoff = Double.valueOf(workingDaysJsonObj.get("weeklyoff").toString());
            Double optional = Double.valueOf(workingDaysJsonObj.get("optional").toString());
            Double holiday = Double.valueOf(workingDaysJsonObj.get("holiday").toString());
            Double leave = Double.valueOf(workingDaysJsonObj.get("leave").toString());
            Double absent = Double.valueOf(workingDaysJsonObj.get("absent").toString());

            StringBuffer workingDaysDetailsString = new StringBuffer();

            if (LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                if (CountPresentDays.equalsIgnoreCase("Yes")) {
                    daysToAdd = daysToAdd + presentDays;
                    workingDaysDetailsString.append("Present=" + presentDays + ",");
                }
                if (CountAbsentDays.equalsIgnoreCase("Yes")) {
                    daysToAdd = daysToAdd + absent;
                    workingDaysDetailsString.append("Absent=" + absent + ",");
                }
                if (CountWeeklyoffDays.equalsIgnoreCase("Yes")) {
                    daysToAdd = daysToAdd + weeklyoff;
                    workingDaysDetailsString.append("WeeklyOff=" + weeklyoff + ",");
                }
                if (CountHolidayDays.equalsIgnoreCase("Yes")) {
                    daysToAdd = daysToAdd + holiday;
                    workingDaysDetailsString.append("Holiday=" + holiday + ",");
                }
                if (CountOptionalHolidayDays.equalsIgnoreCase("Yes")) {
                    daysToAdd = daysToAdd + optional;
                    workingDaysDetailsString.append("Optional=" + optional + ",");
                }
                if (CountLeaveDays.equalsIgnoreCase("Yes")) {
                    daysToAdd = daysToAdd + leave;
                    workingDaysDetailsString.append("Leaves=" + leave + ",");
                }
            } else {
                daysToAdd = 0;
                Reporter("Leave Accrual Based on Working Days is set to 'NO'", "Info");
            }

            workingDaysDetailsString.append("Total Days=" + daysToAdd);
            Reporter(workingDaysDetailsString.toString(), "Pass");
            return daysToAdd;
        } catch (Exception e) {
            Reporter("Exception while calculating working Days leave balance", "Error");
            throw new RuntimeException();
        }
    }

    /**
     * This method calculates working days leave balance
     *
     * @param DOJ
     * @param toDate
     * @return
     */
    public double calculateLeaveBalanceAsPerEmployeeWorkingDays(String DOJ, String toDate) {
        try {
            double workingDaysBalance = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle);
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
            int currentYearInEndOfYearFlag = 0;
            double daysConsiderForCalculation = 0;

            if (checkDOJisUnderLeaveProbationPeriod(DOJ) == true) {
                workingDaysBalance = 0;
            } else if (checkDOJisUnderLeaveProbationPeriod(DOJ) == false) {

                if (EndOfYear.equalsIgnoreCase("No")) {
                    if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        DOJ = leaveCycleStartDate;
                    }

                    if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        Leave_Probation_End_Date = leaveCycleStartDate;
                    }
                }

                if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("Yes")
                        && Calculate_after_probation_period.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                } else if (Pro_rata.equalsIgnoreCase("Yes") && Calculate_from_joining_date.equalsIgnoreCase("No")
                        && Calculate_after_probation_period.equalsIgnoreCase("Yes")) {
                    LeaveCalBeginningDate = Leave_Probation_End_Date;
                } else if (Pro_rata.equalsIgnoreCase("No")) {
                    LeaveCalBeginningDate = DOJ;
                    // LeaveCalBeginningDate = leaveCycleStartDate;
                }


                if (EndOfYear.equalsIgnoreCase("Yes")) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate).minusYears(1))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate).minusYears(1).toString();
                    }
                    if (LocalDate.parse(LeaveCalBeginningDate).isAfter(LocalDate.parse(leaveCycleStartDate))) {
                        return workingDaysBalance = 0;
                    } else if (LocalDate.parse(LeaveCalBeginningDate).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        if (LocalDate.parse(toDate).isAfter(LocalDate.parse(leaveCycleStartDate))) {
                            toDate = LocalDate.parse(leaveCycleStartDate).minusDays(1).toString();
                        }
                    }
                } else if (EndOfYear.equalsIgnoreCase("No")) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate).toString();
                    }

                    if (WorkingDaysEndOfMonth.equalsIgnoreCase("Yes") &&
                            WorkingDaysEndOfQuarter.equalsIgnoreCase("No") && WorkingDaysEndOfBiannual.equalsIgnoreCase("No")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", Leave_Cycle).minusMonths(1).toString();
                        }
                    } else if (WorkingDaysEndOfMonth.equalsIgnoreCase("No") && WorkingDaysEndOfQuarter.equalsIgnoreCase("Yes")
                            && WorkingDaysEndOfBiannual.equalsIgnoreCase("No")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", Leave_Cycle).minusMonths(3).toString();
                        }
                    } else if (WorkingDaysEndOfMonth.equalsIgnoreCase("No") && WorkingDaysEndOfQuarter.equalsIgnoreCase("No")
                            && WorkingDaysEndOfBiannual.equalsIgnoreCase("Yes")) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", Leave_Cycle).minusMonths(6).toString();
                        }
                    }
                }

                if (EndOfYear.equalsIgnoreCase("Yes")) {
                    daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).minusYears(1)
                            .lengthOfYear();
                } else {
                    daysConsiderForCalculation = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).lengthOfYear();
                }

                double workingDays = getWorkingDaysToConsiderForCalculation(LeaveCalBeginningDate, toDate);
                workingDaysBalance = Leaves_Allowed_Per_Year * (workingDays / daysConsiderForCalculation);

            }
            return workingDaysBalance;
        } catch (Exception e) {
            Reporter("Exception while calculating working days leave balance", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyEmployeeCarryForward() {
        try {
            String result = "";
            int flag = 0;
            double expectedBalance = 0;
            double actualCarryForwardBalance = 0;
            double expectedCarryForwardBalance = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(Leave_Cycle); //Get first day of leave cycle
            String leaveCycleEndDate = getLastDayofLeaveCycle(Leave_Cycle);
            Reporter("Leave Cycle defined is '" + Leave_Cycle + "',"
                    + " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
                    + "' till current date", "Info");
            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate); //Get first day of leave cycle in Local date Format
//            objDateTimeHelper.changeServerDate(driver, LocalDate.parse(leaveCycleEndDate).minusDays(-1).toString( ));

            int i = 0;
            LocalDate iterationDate = LocalDate.parse(leaveCycleEndDate); //Set iteration starting date as Today's date
            while (iterationDate.isAfter(leaveCycleStartDateInDateFormat.minusYears(1))) { //This row iterates Date of Joining from current date till leave cycle start date
                iterationDate = LocalDate.parse(leaveCycleEndDate).minusYears(1).minusDays(i); // Decrease iteration date by 1 day
                if (iterationDateFourTimesPerMonth(iterationDate) == true) {
                    DateOfJoining = changeEmployeeDOJ(iterationDate); //This function change employee DOJ
//                    DateOfJoining = iterationDate.toString(); //This function change employee DOJ

                    if (LeaveAccrualBasedOnWorkingDays != null && !LeaveAccrualBasedOnWorkingDays.isEmpty()
                            && LeaveAccrualBasedOnWorkingDays.equalsIgnoreCase("Yes")) {
                        expectedBalance = calculateDeactivationLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                                LocalDate.parse(leaveCycleEndDate).minusYears(1).toString()); //This function calculates leave balance in case of working days
                    } else {
                        expectedBalance = calculateDeactivationLeaveBalance(DateOfJoining, LocalDate.parse(leaveCycleEndDate).minusYears(1).toString(), "Before"); //This important function calculates leave balance
                    }

                    expectedCarryForwardBalance = calculateCarryForwardBalance(expectedBalance);
                    expectedCarryForwardBalance = Math.round(expectedCarryForwardBalance * 100.0) / 100.0;

                    //removeEmployeeCarryForwardLeaveLogs();//no need to run deleted from vizjay
                    runCarryFrowardCronByEndPointURL();
                    //actualCarryForwardBalance = getEmployeesFrontEndCarryForwardLeaveBalance(Leave_Type); //This gets employees leave balance from frontend
                    actualCarryForwardBalance = new LeaveBalanceAPI(EMPID,Leave_Type).getCarryForwardBalance();
                    /*
                    In below code we are comparing calculated balance to actual balance shown in frontend
                     */
                    if (expectedCarryForwardBalance != actualCarryForwardBalance) {
                        Reporter("Failed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                + expectedCarryForwardBalance + "||Actual Leave Balance=" + actualCarryForwardBalance, "Fail");
                        result = "Fail";
                        flag++;
                    } else {
                        Reporter("Passed||" + "DOJ '" + DateOfJoining + "'||" + "Expected Leave Balance="
                                + expectedCarryForwardBalance + "||Actual Leave Balance=" + actualCarryForwardBalance, "Pass");
                        result = "Pass";
                    }

                    if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                        writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualCarryForwardBalance, result,
                                DateTimeHelper.getCurrentLocalDateAndTime());
                    }
                }
                i++;
            }
            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }
    }


    ////////////Over Utilization////////////////////////////////
    public double calculateleavesToBeAppliedForOverUtilization(String empId,String leaveType,Recordset r){
        String scenario=data.get("scenario").toLowerCase();

        double currentBalance=new EmployeeAction(empId).getCurrentLeaveBalanceOfEmp(leaveType);
        int overUtilization=getOverUtilizationLeaveForEmp(scenario,r);

        return currentBalance+overUtilization;
     }

    //set OverUtilization Based On Policy Created From Excel
    //for OverUtilization Scenario
    public int getOverUtilizationLeaveForEmp(String scenario,Recordset leavePolicy) {
        int Overutilization = 0;
        try{
        if (leavePolicy.getField("Max Leave Overutilization").equalsIgnoreCase("0") & leavePolicy.getField("Max Leave Overutilization") != null) {
            Overutilization = Integer.parseInt(objGenHelper.getRandomNumber(2));// try adding only +1()
        } else {
            Overutilization = Integer.parseInt(leavePolicy.getField("Max Leave Overutilization"));
        }

        if (scenario.contains("current balance + over(")) {
            if (scenario.split("\\+")[1].contains(">")) {
                Overutilization = Overutilization + 1;
            } else if (scenario.split("\\+")[1].contains("<")) {
                Overutilization = Overutilization - 1;
            }
        }
        return Overutilization;
    }catch (Exception e){
            Reporter("Error in getting Policy Record from excel","Error");
            throw new RuntimeException();
        }
}
}
