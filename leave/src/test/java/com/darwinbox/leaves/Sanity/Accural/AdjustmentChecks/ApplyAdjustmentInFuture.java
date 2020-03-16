package com.darwinbox.leaves.Sanity.Accural.AdjustmentChecks;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.ImportServices;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Services.LeaveSettings;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

public class ApplyAdjustmentInFuture extends LeaveAccuralBase {


    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LocalDate doj = null;

    LeavesPage leavePage;
    LeavesAction leavesAction = null;


    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");
        leavesAction = new LeavesAction(driver);

        leavePage = PageFactory.initElements(driver, LeavesPage.class);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void VerifyCarryForwardForTwoCyclesWithLeaves(Map<String, String> testData) {


        int noOfLeaves = 3;
        //always give current year
        leaveCycleStartDate = LocalDate.parse("2020-01-01");
        leaveCycleEndDate = LocalDate.parse("2020-12-31");

        LeavePolicyObject carryForwardBalance = getCarryForwardPolicy(testData);
        super.carryForward = true;
        //making default to begin of month for calculation
        if (carryForwardBalance.getCredit_on_accural_basis().getIndicator()) {
            Credit_On_Accural_Basis credit_on_accural_basis = carryForwardBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true, true, false);
            credit_on_accural_basis.setQuarterlyAccural(false, false, false);
            credit_on_accural_basis.setBiAnnual(false);
            carryForwardBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        //leave validity also needs to be set to zero for carry forward
        if (carryForwardBalance.getProbation_period_before_leave_validity().custom &&
                !carryForwardBalance.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            carryForwardBalance.getProbation_period_before_leave_validity().customMonths = 0;

        //if(carryForwardBalance.getProbation_period_before_leave_validity().probation)
        ///carryForwardBalance.getProbation_period_before_leave_validity()

        super.setLeavePolicyObject(carryForwardBalance);


        changeServerDate(LocalDate.now());


        try {
            employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no"));
        } catch (Exception e) {
            try {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no"));
            } catch (Exception e1) {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no"));

            }
        }

        leavesAction.setEmployeeID(employee.getEmployeeID());
        super.employee = employee;

        Reporter("Employee DOJ is --> " + employee.getDoj(), "INFO");

        int adjustedBalance = -2;
        new LeaveSettings().showLeaveAdjustments(carryForwardBalance.getLeave_Type());
        new ImportServices().importLeaveAdjustmentBalance(employee.getEmployeeID(), carryForwardBalance.getLeave_Type(), adjustedBalance + "", getServerOrLocalDate().getYear() + "");

        double actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(), carryForwardBalance.getLeave_Type()).getBalance();
        double expectedBalance = carryForwardBalance.getMaximum_leave_allowed_per_year()-adjustedBalance;

        Reporter("Leave Adjusted Year is -->"+getServerOrLocalDate().getYear() ,"Info");
        Reporter("Adjusted Balace is -->"+adjustedBalance,"Info");
        Reporter("Leave Balance after adjustment is"+actualLeaveBalance,"Info");

        Assert.assertTrue(expectedBalance==actualLeaveBalance,"FAILED |||| Actual and Expected are Not Same");

        int adjustedBalance1 = -5;
        new LeaveSettings().showLeaveAdjustments(carryForwardBalance.getLeave_Type());
        new ImportServices().importLeaveAdjustmentBalance(employee.getEmployeeID(), carryForwardBalance.getLeave_Type(), adjustedBalance + "", getServerOrLocalDate().getYear()+1 + "");

        double actualLeaveBalance1 = new LeaveBalanceAPI(employee.getEmployeeID(), carryForwardBalance.getLeave_Type()).getBalance();
        double expectedBalance1 = expectedBalance;

        Reporter("Leave Adjusted Year is -->"+(getServerOrLocalDate().getYear()+1) ,"Info");
        Reporter("Adjusted Balace is -->"+adjustedBalance1,"Info");
        Reporter("Leave Balance after adjustment is"+actualLeaveBalance1,"Info");

        Assert.assertTrue(expectedBalance1==actualLeaveBalance1,"FAILED |||| Actual and Expected are Not Same");



    }
    }

