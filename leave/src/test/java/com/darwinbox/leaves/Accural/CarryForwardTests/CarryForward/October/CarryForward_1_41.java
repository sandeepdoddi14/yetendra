package com.darwinbox.leaves.Accural.CarryForwardTests.CarryForward.October;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;


public class CarryForward_1_41 extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;


    static LocalDate serverDateInFormat = null;
    LocalDate doj=null;

    LeavesAction leavesAction = null;



    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");
        leavesAction= new LeavesAction(driver);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyCarryForwardBalance(Map<String, String> testData) {

        LeavePolicyObject carryForwardBalance = getCarryForwardPolicy(testData);
        super.carryForward=true;
        //making default to begin of month for calculation
        if(carryForwardBalance.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=carryForwardBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            carryForwardBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        //leave validity also needs to be set to zero for carry forward
        if(carryForwardBalance.getProbation_period_before_leave_validity().custom &&
            !carryForwardBalance.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            carryForwardBalance.getProbation_period_before_leave_validity().customMonths=0;

        //if(carryForwardBalance.getProbation_period_before_leave_validity().probation)
            ///carryForwardBalance.getProbation_period_before_leave_validity()

        super.setLeavePolicyObject(carryForwardBalance);

        leaveCycleStartDate = LocalDate.parse("2019-10-01");
        leaveCycleEndDate = LocalDate.parse("2020-09-30");



        new DateTimeHelper().changeServerDate(driver, LocalDate.now().toString());

        try {
            employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.toString(), "no"));

        } catch (Exception e) {
            try {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.toString(), "no"));
            } catch (Exception e1) {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.toString(), "no"));

            }
        }

            super.employee = employee;


            new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.plusDays(1).toString());
            serverChangedDate = leaveCycleEndDate.plusDays(1).toString();
            serverDateInFormat = LocalDate.parse(serverChangedDate);

            double actualLeaveBalance = 0.0D;
            double expecetedLeaveBalance = 0.0D;

            doj= LocalDate.parse(employee.getDoj());
            leavesAction.setEmployeeID(employee.getEmployeeID());
            while (!doj.isBefore(leaveCycleStartDate)) {
                if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {

                    changeEmployeeDOJ(doj,employee);
                    employee.setDoj(doj.toString());


                    expecetedLeaveBalance = calculateLeaveBalance(doj.toString(), leaveCycleEndDate.toString());
                    Reporter("Expected Leave Balance is --" + expecetedLeaveBalance, "Info");


                    //leavesAction.removeEmployeeCarryForwardLeaveLogs();
                    leavesAction.runCarryFrowardCronByEndPointURL();

                        actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getCarryForwardBalance();
                        Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");

                        if (expecetedLeaveBalance == actualLeaveBalance)
                            Reporter("Passed |||| actual and expected are same", "Pass");
                        else
                            Reporter("Failed |||| actual and expected are not same", "Fail");


                    }
                doj = doj.minusDays(1);
            }


        }
    }

