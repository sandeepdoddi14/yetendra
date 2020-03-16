package com.darwinbox.leaves.ProductionIssues.DBX_8440.MultipleAllotmentWithDeactivation;


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
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class FirstHalfTransferSecondHalfDeactivation extends LeaveAccuralBase {

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
    public void verifyMultipleAllotmentBalance(Map<String, String> testData) {



        Double perMonthLeavesFullTime=0.0D;
        Double perMonthLeavesPartTime=0.0D;

        LeavePolicyObject multipleAllotmentLeavePolicy = getMultipleAllotmentLeavePolicy(testData);
        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);

        Reporter("Leave Type is"+multipleAllotmentLeavePolicy.getLeave_Type(),"Info");

        //always start from previous year
        leaveCycleStartDate=LocalDate.parse("2020-01-01");
        leaveCycleEndDate=LocalDate.parse("2020-12-31");


        Reporter("Leave Cycle Start Date is -->"+leaveCycleStartDate,"INfo");
        Reporter("Leave Cycle End Date is -->"+leaveCycleEndDate,"INfo");

        HashMap<String,String> empTypes=new Services().getEmployeeTypes();

        //to generate employee
        //it will create a full time employee
        changeServerDate(LocalDate.now().toString());
        try {
            employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusDays(10).toString(), "no");
        }
        catch (Exception e){
            try {
                employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusDays(10).toString(), "no");
            }
            catch (Exception e1){
                employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.minusDays(10).toString(), "no");

            }
        }

        super.setEmployee(employee);

        Reporter("Employee DOJ is ---->"+employee.getDoj(),"Info");

        Boolean prorata_afterProbation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;




        //CALCULATE DEACTIVATION BALANCE FOR 1ST TRANSFER
        deActiavation=true;
        //making default to begin of month for calculation
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=multipleAllotmentLeavePolicy.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            multipleAllotmentLeavePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }
        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);

        perMonthLeavesFullTime = leavePolicyObject.getMaximum_leave_allowed_per_year()/12.0;

        //changeServerDate(LocalDate.parse(employee.getDoj()).plusMonths(5).plusDays(15));

        ExpectedLeaveBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleStartDate.plusMonths(5).minusDays(1).toString());



        //EMPLOYEE WILL BE CHANGED FROM FULL TIME TO PART TIME AT THIS DATE
        //employee also deactivates in same day
        changeServerDate(LocalDate.parse(employee.getDoj()).plusMonths(5).plusDays(15).toString());

        Reporter("Employee Tranfer Date  is ---->"+serverChangedDate,"Info");

        //part time
        new EmployeeServices().addUserEmployment(employee.getMongoID(),"4",empTypes.entrySet().stream().filter(x->x.getKey().equalsIgnoreCase("part time")).findFirst().get().getValue(),serverChangedDate);
        multipleAllotmentLeavePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[1]));

        // leaveCycleStartDate=leaveCycleStartDate.plusMonths(5);
        //leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);

        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);





        perMonthLeavesPartTime=multipleAllotmentLeavePolicy.getMaximum_leave_allowed_per_year()/12.0;






        if(leavePolicyObject.getCredit_on_pro_rata_basis().indicator && !leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th
                && !leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th || !leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
            ExpectedLeaveBalance = ExpectedLeaveBalance + perMonthLeavesPartTime;

        else if(leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
            ExpectedLeaveBalance = ExpectedLeaveBalance + perMonthLeavesPartTime;

        else if(leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th) {

            ExpectedLeaveBalance = ExpectedLeaveBalance + perMonthLeavesPartTime;
        }






        changeServerDate(leaveCycleStartDate.plusMonths(6).minusDays(15));
        deActicvationDate=serverDateInFormat;
        Reporter("Employee WDWithDeactivation Date is ---->"+serverChangedDate,"Info");




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

