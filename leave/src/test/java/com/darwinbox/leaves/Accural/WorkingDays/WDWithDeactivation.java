package com.darwinbox.leaves.Accural.WorkingDays;


import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;


public class WDWithDeactivation extends LeaveAccuralBase {

    Employee employee = new Employee();
    LeavesAction leavesAction=null;


    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;


    static LocalDate serverDateInFormat = null;



    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);
        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyDeactivationBalance(Map<String, String> testData) {

        LeavePolicyObject deactivationLeaveBalance = getWorkingDaysPolicy(testData);

        //making default to begin of month for calculation
        if(deactivationLeaveBalance.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=deactivationLeaveBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            deactivationLeaveBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        super.setLeavePolicyObject(deactivationLeaveBalance);

        leaveCycleStartDate = LocalDate.parse("2019-04-01");
        leaveCycleEndDate = LocalDate.parse("2020-03-31");



        new DateTimeHelper().changeServerDate(driver, LocalDate.now().toString());

        Assert.assertTrue(setEmployeeId("Deactivate_Balance"), "Employee ID is set Mnually");

        leavesAction.setEmployeeID("Deactivate_Balance");
        Assert.assertTrue(leavesAction.removeEmployeeLeaveLogs(), "Employees Leave logs removed successfully") ;
        DateOfJoining=leaveCycleStartDate.toString();




            new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.toString());
            serverChangedDate = leaveCycleEndDate.toString();


            serverDateInFormat = LocalDate.parse(serverChangedDate);

            double actualLeaveBalance = 0.0D;
            double expecetedLeaveBalance = 0.0D;


            //changeEmployeeDOJ(Leavec)

            while (!serverDateInFormat.isBefore(leaveCycleStartDate)) {
                if (new LeavesAction().iterationDateFourTimesPerMonth(serverDateInFormat) == true) {

                    //leavesAction.removeEmployeeLeaveLogs();
                   // leavesAction.removeEmployeeCarryForwardLeaveLogs()

                    new DateTimeHelper().changeServerDate(driver, serverDateInFormat.toString());

                    super.serverChangedDate = serverDateInFormat.toString();
                    super.deActiavation = true;


                    expecetedLeaveBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(leaveCycleStartDate.toString(), serverDateInFormat.toString());
                    expecetedLeaveBalance =BigDecimal.valueOf(expecetedLeaveBalance).setScale(2, RoundingMode.HALF_EVEN.HALF_UP).doubleValue();
                    Reporter("Expected Leave Balance is --" + expecetedLeaveBalance, "Info");


                    actualLeaveBalance = getEmployeesFrontEndDeactivationLeaveBalance(deactivationLeaveBalance.getLeave_Type(), serverDateInFormat.toString());
                    Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");

                    if (expecetedLeaveBalance == actualLeaveBalance)
                        Reporter("Passed |||| actual and expected are same", "Pass");
                    else
                        Reporter("Failed |||| actual and expected are not same", "Fail");


                }
                serverDateInFormat = serverDateInFormat.minusDays(1);
            }


        }
    }

