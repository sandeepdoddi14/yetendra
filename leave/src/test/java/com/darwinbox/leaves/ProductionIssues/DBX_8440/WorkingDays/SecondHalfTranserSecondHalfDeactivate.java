package com.darwinbox.leaves.ProductionIssues.DBX_8440.WorkingDays;


import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SecondHalfTranserSecondHalfDeactivate extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LocalDate doj=null;

    LeavesAction leavesAction = null;

    LocalDate deActicvationDate=null;
    Double ExpectedLeaveBalance=0.0D;
    Double finalMonthBalance=null;


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
    public void verifyMultipleAllotmentBalance(Map<String, String> testData) throws InterruptedException {


        //always start from previous year
        leaveCycleStartDate=LocalDate.parse("2019-04-01");
        leaveCycleEndDate=LocalDate.parse("2020-03-31");



        Double perMonthLeavesFullTime=0.0D;
        Double perMonthLeavesPartTime=0.0D;
        String empMongoID="5e7cb30cdb64e";
        String empDoj=leaveCycleStartDate.minusDays(15).toString();
           DateOfJoining=empDoj;



        LeavePolicyObject multipleAllotmentLeavePolicy = getMultipleAllotmentLeavePolicyWithWorkingDays(testData);
        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);

        Reporter("Leave Type is"+multipleAllotmentLeavePolicy.getLeave_Type(),"Info");



        Reporter("Leave Cycle Start Date is -->"+leaveCycleStartDate,"INfo");
        Reporter("Leave Cycle End Date is -->"+leaveCycleEndDate,"INfo");

        HashMap<String,String> empTypes=new Services().getEmployeeTypes();

        //to generate employee
        //it will create a full time employee
        //changeServerDate(LocalDate.now().toString());

        //auto 3
        //emp id should be created new every time while running script
        Assert.assertTrue(setEmployeeId("P1585230825401"), "Employee ID is set Mnually");

        leavesAction.setEmployeeID("P1585230825401");
        //Assert.assertTrue(leavesAction.removeEmployeeLeaveLogs(), "Employees Leave logs removed successfully") ;

        String userID="242073";


        changeServerDate(DateOfJoining);
        driver.get(data.get("@@url")+"/employeement/employeementdetails/id/"+userID);


        Reporter("Employee DOJ is ---->"+empDoj,"Info");

        Boolean prorata_afterProbation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;



         perMonthLeavesFullTime = leavePolicyObject.getMaximum_leave_allowed_per_year()/12.0;

        changeServerDate(LocalDate.parse(empDoj).plusMonths(6));

        ExpectedLeaveBalance=calculateLeaveBalanceAsPerEmployeeWorkingDays(leaveCycleStartDate.toString(),serverChangedDate);



        //EMPLOYEE WILL BE CHANGED FROM FULL TIME TO PART TIME AT THIS DATE
        //employee also deactivates in same day
        changeServerDate(LocalDate.parse(empDoj).plusMonths(6).plusDays(1).toString());
        deActicvationDate = serverDateInFormat;
        Reporter("Employee Tranfer Date  is ---->"+serverChangedDate,"Info");
        Reporter("Employee WDWithDeactivation Date is ---->"+serverChangedDate,"Info");

        //part time
        new EmployeeServices().addUserEmployment(empMongoID,"4",empTypes.entrySet().stream().filter(x->x.getKey().equalsIgnoreCase("part time")).findFirst().get().getValue(),serverChangedDate);
       multipleAllotmentLeavePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[1]));

      // leaveCycleStartDate=leaveCycleStartDate.plusMonths(5);
       //leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);

       super.setLeavePolicyObject(multipleAllotmentLeavePolicy);





       perMonthLeavesPartTime=multipleAllotmentLeavePolicy.getMaximum_leave_allowed_per_year()/12.0;






       /*if(leavePolicyObject.getCredit_on_pro_rata_basis().indicator && !leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th
       && !leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th || !leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
           ExpectedLeaveBalance = ExpectedLeaveBalance + perMonthLeavesFullTime;

       else if(leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
            ExpectedLeaveBalance = ExpectedLeaveBalance + perMonthLeavesPartTime;

       else if(leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th) {
           perMonthLeavesFullTime=proRataCheck(perMonthLeavesFullTime);
           perMonthLeavesPartTime=proRataCheck(perMonthLeavesPartTime);
           ExpectedLeaveBalance = ExpectedLeaveBalance + perMonthLeavesFullTime + perMonthLeavesPartTime;
       }



*/

        ExpectedLeaveBalance=ExpectedLeaveBalance + calculateLeaveBalanceAsPerEmployeeWorkingDays(serverChangedDate,serverChangedDate);

        Double ActualDeactivationBalance=getEmployeesFrontEndDeactivationLeaveBalance(multipleAllotmentLeavePolicy.getLeave_Type(), serverDateInFormat.toString());


            Reporter("Expected Leave Balance is   --->"+ExpectedLeaveBalance,"Info");
            Reporter("Actual Leave Balance is   --->"+ActualDeactivationBalance,"Info");

        Assert.assertTrue(ExpectedLeaveBalance.equals(ActualDeactivationBalance),"Expected Leave Balance And Actual Leave Balance Are Not Same");


    }



    public Double proRataCheck(Double perMonthLeaves){
        if(leavePolicyObject.getCredit_on_pro_rata_basis().indicator){
            if(leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th)
            {
                if(deActicvationDate.getDayOfMonth()>=15)
                    perMonthLeaves = perMonthLeaves/2;
            }

            else if(leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
            {
                if(deActicvationDate.getDayOfMonth()>=15)
                    perMonthLeaves =  perMonthLeaves;
            }

            else if(!leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th
                    && !leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
                perMonthLeaves = perMonthLeaves ;
        }
       else{
           perMonthLeaves = perMonthLeaves;
        }

       return perMonthLeaves;
    }



    }

