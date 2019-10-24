package com.darwinbox.leaves.Accural.Custom;


import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;


public class Deactivation extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;


    static LocalDate serverDateInFormat = null;



    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyDeactivationBalance(Map<String, String> testData) {

        LeavePolicyObject deactivationLeaveBalance = getLeaveBalancePolicy(testData);

        //making default to begin of month for calculation
        if(deactivationLeaveBalance.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=deactivationLeaveBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            deactivationLeaveBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        super.setLeavePolicyObject(deactivationLeaveBalance);

        leaveCycleStartDate = LocalDate.parse("2018-11-01");
        leaveCycleEndDate = LocalDate.parse("2019-10-31");



        new DateTimeHelper().changeServerDate(driver, LocalDate.now().toString());

        try {
            employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no"));
        } catch (Exception e) {
            try {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no"));
            } catch (Exception e1) {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no"));


            }
        }

            super.employee = employee;


            new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.toString());
            serverChangedDate = leaveCycleEndDate.toString();


            serverDateInFormat = LocalDate.parse(serverChangedDate);

            double actualLeaveBalance = 0.0D;
            double expecetedLeaveBalance = 0.0D;



            while (!serverDateInFormat.isBefore(leaveCycleStartDate)) {
                if (new LeavesAction().iterationDateFourTimesPerMonth(serverDateInFormat) == true) {
                    new DateTimeHelper().changeServerDate(driver, serverDateInFormat.toString());

                    super.serverChangedDate = serverDateInFormat.toString();
                    super.deActiavation = true;


                    expecetedLeaveBalance = calculateLeaveBalance(employee.getDoj(), serverDateInFormat.toString());
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

