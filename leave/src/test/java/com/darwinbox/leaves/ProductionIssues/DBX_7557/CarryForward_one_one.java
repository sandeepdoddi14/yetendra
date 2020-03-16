package com.darwinbox.leaves.ProductionIssues.DBX_7557;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;


public class CarryForward_one_one extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;


    static LocalDate serverDateInFormat = null;
    LocalDate doj=null;

    LeavesAction leavesAction = null;

    LeavePolicyObject carryForwardBalance=null;

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

       // resetDateFromJenkin();

        setAllCarryForward(testData);

        leaveCycleStartDate = LocalDate.parse("2020-04-01");
        leaveCycleEndDate = LocalDate.parse("2021-03-31");
        double actualLeaveBalance = 0.0D;
        double expecetedLeaveBalance = 0.0D;


        leaveCycleStartDate = leaveCycleStartDate.minusYears(2);
        leaveCycleEndDate = leaveCycleEndDate.minusYears(2);

        changeServerDate(LocalDate.now());

        try {
            employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.minusMonths(1).toString(), "no"));
        } catch (Exception e) {
            try {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.minusMonths(1).toString(), "no"));
            } catch (Exception e1) {
                employee = (new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleEndDate.minusMonths(1).toString(), "no"));


            }
        }

            super.employee = employee;
            leavesAction.setEmployeeID(employee.getEmployeeID());
            Reporter("Employee DOJ is  -->  " +employee.getDoj(),"Info");

            expecetedLeaveBalance = leavePolicyObject.getMaximum_leave_allowed_per_year();

            Reporter("Leave Cycle Start Date is  --> "+leaveCycleStartDate.toString(),"Info");
            Reporter("Leave Cycle End Date is  --> "+leaveCycleEndDate.toString(),"Info");

            changeServerDate(leaveCycleEndDate.plusDays(1));

            Reporter("Carry Forward Cron is Run at -->"+serverChangedDate,"Info");

            leavesAction.runCarryFrowardCronByEndPointURL();


            leavesAction.setEmployeeID(employee.getEmployeeID());


            if(leavePolicyObject.getCarryForwardUnusedLeave().indicator && leavePolicyObject.getCarryForwardUnusedLeave().carryForwardAllUnusedLeave)
            expecetedLeaveBalance = calculateLeaveBalance(employee.getDoj(), leaveCycleEndDate.toString())+leavePolicyObject.getMaximum_leave_allowed_per_year();
            else if(leavePolicyObject.getCarryForwardUnusedLeave().indicator==false)
                expecetedLeaveBalance= leavePolicyObject.getMaximum_leave_allowed_per_year();


            Reporter("Expected Leave Balance is --" + expecetedLeaveBalance, "Info");



            actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getBalance();
            Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");

            Assert.assertTrue(expecetedLeaveBalance==actualLeaveBalance,"Actual and Expected Are Not Same");


            leaveCycleStartDate= leaveCycleStartDate.plusYears(1);
            leaveCycleEndDate = leaveCycleEndDate.plusYears(1);

            Reporter("Leave Cycle Start Date is  --> "+leaveCycleStartDate.toString(),"Info");
            Reporter("Leave Cycle End Date is  --> "+leaveCycleEndDate.toString(),"Info");

            changeServerDate(leaveCycleEndDate.minusMonths(1));

            Reporter("Carry Forward Cron is Run at -->"+serverChangedDate,"Info");
            leavesAction.runCarryFrowardCronByEndPointURL();


            Reporter("Expected Leave Balance is --" + expecetedLeaveBalance, "Info");
            actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getBalance();
            Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");
            Assert.assertTrue(expecetedLeaveBalance==actualLeaveBalance,"Actual and Expected Are Not Same");


           setAllCarryForward(testData);


            leaveCycleStartDate=leaveCycleStartDate.plusYears(1);
            leaveCycleEndDate=leaveCycleEndDate.plusYears(1);

            Reporter("Leave Cycle Start Date is  --> "+leaveCycleStartDate.toString(),"Info");
            Reporter("Leave Cycle End Date is  --> "+leaveCycleEndDate.toString(),"Info");

            changeServerDate(leaveCycleStartDate.plusMonths(1));

            Reporter("Carry Forward Cron is Run at -->"+serverChangedDate,"Info");
            leavesAction.runCarryFrowardCronByEndPointURL();

            if(leavePolicyObject.getCarryForwardUnusedLeave().indicator==false)
            expecetedLeaveBalance = leavePolicyObject.getMaximum_leave_allowed_per_year();
            else if(leavePolicyObject.getCarryForwardUnusedLeave().indicator==true && leavePolicyObject.getCarryForwardUnusedLeave().carryForwardAllUnusedLeave)
                expecetedLeaveBalance= expecetedLeaveBalance+leavePolicyObject.getMaximum_leave_allowed_per_year();


            Reporter("Expected Leave Balance is --" + expecetedLeaveBalance, "Info");
            actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getBalance();
            Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");
            Assert.assertTrue(expecetedLeaveBalance==actualLeaveBalance,"Actual and Expected Are Not Same");





        Reporter("Leave Cycle Start Date is  --> "+leaveCycleStartDate.toString(),"Info");
        Reporter("Leave Cycle End Date is  --> "+leaveCycleEndDate.toString(),"Info");

        changeServerDate(leaveCycleEndDate.minusMonths(1));

        Reporter("Carry Forward Cron is Run at -->"+serverChangedDate,"Info");
        leavesAction.runCarryFrowardCronByEndPointURL();


        Reporter("Expected Leave Balance is --" + expecetedLeaveBalance, "Info");
        actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getBalance();
        Reporter("Actual Leave Balance is ---" + actualLeaveBalance, "Info");
        Assert.assertTrue(expecetedLeaveBalance==actualLeaveBalance,"Actual and Expected Are Not Same");

    }


    public void setZeroCarryForward(Map<String,String> testData){
        testData.put("Carry forward","No");
        testData.put("Carry forward All/Fixed/Percentage","No");
        testData.put("Fixed/Percentage value","No");

        carryForwardBalance = getCarryForwardPolicy(testData);

        super.carryForward=false;

        super.setLeavePolicyObject(carryForwardBalance);
    }

    public void setAllCarryForward(Map<String,String> testData){
        testData.put("Carry forward","Yes");
        testData.put("Carry forward All/Fixed/Percentage","all");
        testData.put("Fixed/Percentage value","No");


        carryForwardBalance = getCarryForwardPolicy(testData);
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
    }

    }

