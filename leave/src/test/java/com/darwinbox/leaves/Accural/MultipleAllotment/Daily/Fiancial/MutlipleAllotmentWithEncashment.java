package com.darwinbox.leaves.Accural.MultipleAllotment.Daily.Fiancial;


import com.darwinbox.Services;
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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class MutlipleAllotmentWithEncashment extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LocalDate doj=null;

    LeavesAction leavesAction = null;

    double tenureLeaves=0.0D;
    double firstYearBalance=0.0D;
    double totalLeaves=0.0D;

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
        int noOFEncashment=2;


        testData.put("Leave_Encashment","true");
        testData.put("Consider","both");
        testData.put("Encash_Min","0.5");
        testData.put("Encash_Max","0");
        testData.put("Encash_MinLeaveBalLeft","0");
        leavePolicyObject = getMultipleAllotmentLeavePolicy(testData);


        Reporter("Leave Type is"+leavePolicyObject.getLeave_Type(),"Info");

        //always start from previous year
        leaveCycleStartDate=LocalDate.parse("2019-04-01");
        leaveCycleEndDate=LocalDate.parse("2020-03-31");


        HashMap<String,String> empTypes=new Services().getEmployeeTypes();

        //to generate employee
        //it will create a full time employee
        changeServerDate(LocalDate.now().toString());
        try {
            employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no");
        }
        catch (Exception e){
            try {
                employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no");
            }
            catch (Exception e1){
                employee = new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no");

            }
        }
       super.setEmployee(employee);

        Reporter("Employee DOJ is -->"+employee.getDoj(),"Info");

       Boolean prorata_afterProbation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;



        //BEFORE FIRST TRANSFER
        changeServerDate(leaveCycleStartDate.plusMonths(5).minusDays(1).toString());

        applyEnacashment(noOFEncashment);
        double beforeFirstTransfer = calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate)-noOFEncashment;
        double actualBeforeFirstTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),leavePolicyObject.getLeave_Type()).getBalance();

        Reporter("Before first Transfer Balance"+beforeFirstTransfer,"Info");
        Reporter("Actaul Before first Transfer Balance"+actualBeforeFirstTransfer,"Info");


        //CALCULATE DEACTIVATION BALANCE FOR 1ST TRANSFER
        deActiavation=true;
        //making default to begin of month for calculation
        if(leavePolicyObject.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=leavePolicyObject.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            leavePolicyObject.setCredit_on_accural_basis(credit_on_accural_basis);
        }
        super.setLeavePolicyObject(leavePolicyObject);

        double firstDeactivagtionBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate)-noOFEncashment;
        double firstDeactivagtionBalanceValue=firstDeactivagtionBalance;
        deActiavation=false;



        //reset deactivation policy
        if(testData.get("Accrual").equalsIgnoreCase("yes")?true:false){
            Credit_On_Accural_Basis credit_on_accural_basis= new Credit_On_Accural_Basis();
            credit_on_accural_basis.setIndicator(true);

            if(!testData.get("Monthly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setMonthlyAccuralSetting(false,false,false);
            else
                credit_on_accural_basis.setMonthlyAccuralSetting(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            if(!testData.get("Quarterly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            else
                credit_on_accural_basis.setQuarterlyAccural(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            credit_on_accural_basis.setBiAnnual(testData.get("Biannually").equalsIgnoreCase("yes")?true:false);

            leavePolicyObject.setCredit_on_accural_basis(credit_on_accural_basis);
        }




        //EMPLOYEE WILL BE CHANGED FROM FULL TIME TO PART TIME AT THIS DATE
        changeServerDate(leaveCycleStartDate.plusMonths(5).plusDays(1).toString());



        //part time
       new EmployeeServices().addUserEmployment(employee.getMongoID(),"4",empTypes.entrySet().stream().filter(x->x.getKey().equalsIgnoreCase("part time")).findFirst().get().getValue(),leaveCycleStartDate.plusMonths(5).toString());
        leavePolicyObject.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[1]));

       leaveCycleStartDate=leaveCycleStartDate.plusMonths(5);
       leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);



        applyEnacashment(noOFEncashment);
       double afterFirstTransferDate = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());
        double secondDeactionValue = 24.0;
       double actualAfterFirstTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),leavePolicyObject.getLeave_Type()).getBalance();


        if(!leavePolicyObject.getCredit_on_accural_basis().getIndicator()){

            afterFirstTransferDate=(afterFirstTransferDate/12.0)*7;

        }
        if(leavePolicyObject.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterFirstTransferDate=(afterFirstTransferDate/3.0)*1;
        }

        if(leavePolicyObject.getCredit_on_accural_basis().getBeginOfMonth()){
            afterFirstTransferDate=(afterFirstTransferDate/12.0)*1;
        }

        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth()){
            afterFirstTransferDate=(afterFirstTransferDate/12.0)*0.0;
        }

        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter())
        {
            afterFirstTransferDate=0.0D;
            firstDeactivagtionBalance=(firstDeactivagtionBalance/5.0)*3.0;
        }
        if(leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()){
            afterFirstTransferDate = (24/12.0)*1.0;
        }


        double afterFirstTransferBalance=afterFirstTransferDate+firstDeactivagtionBalance-noOFEncashment;

       Reporter("after First Transfer Date Balance"+(afterFirstTransferBalance),"Info");
       Reporter("Actual After First Transfer Date Balance"+actualAfterFirstTransfer,"Info");
        Assert.assertTrue(afterFirstTransferBalance==actualAfterFirstTransfer,"Not Same Actal And Expectd");



        changeServerDate(leaveCycleStartDate.plusMonths(5).minusDays(1).toString());

       //set for probation = true
      // if(true)
        //   afterFirstTransferDatetemp1 = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());
        if(!leavePolicyObject.getCredit_on_accural_basis().getIndicator()){

            secondDeactionValue=(secondDeactionValue/12.0)*7;

        }
        if(leavePolicyObject.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            secondDeactionValue=(secondDeactionValue/12.0)*7;
        }
        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter())
        {
            secondDeactionValue=(secondDeactionValue/12.0)*4;
        }

        if(leavePolicyObject.getCredit_on_accural_basis().getBeginOfMonth()){
            secondDeactionValue=(secondDeactionValue/12.0)*5.0;
        }


        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth()){
            secondDeactionValue=(secondDeactionValue/11.0)*4.0;
        }
        if(leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()){
            secondDeactionValue = (24.0/12.0)*7.0;
        }


        secondDeactionValue = secondDeactionValue -noOFEncashment;

        double actualBeforeSecondTrnsfer= new LeaveBalanceAPI(employee.getEmployeeID(),leavePolicyObject.getLeave_Type()).getBalance();

        Reporter("before seccond transfer balnce"+(firstDeactivagtionBalanceValue+secondDeactionValue),"Info");

        Reporter("actual before seccond transfer balnce"+(actualBeforeSecondTrnsfer),"Info");
        Assert.assertTrue(firstDeactivagtionBalanceValue+secondDeactionValue==actualBeforeSecondTrnsfer,"Not Same Actal And Expectd");



        //employee changes to contract from this date
        changeServerDate(leaveCycleStartDate.plusMonths(5).toString());


       //contract
        new EmployeeServices().addUserEmployment(employee.getMongoID(),"4",empTypes.entrySet().stream().filter(x->x.getKey().equalsIgnoreCase("contract")).findFirst().get().getValue(),leaveCycleStartDate.plusMonths(5).toString());


        leavePolicyObject.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[2]));

        leaveCycleStartDate=leaveCycleStartDate.plusMonths(5);
        leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);


        applyEnacashment(noOFEncashment);
        double afterSecondTransferDate = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());
        double actualAfterSecondTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),leavePolicyObject.getLeave_Type()).getBalance();


        if(!leavePolicyObject.getCredit_on_accural_basis().getIndicator()){

            afterSecondTransferDate=(afterSecondTransferDate/12.0)*2;

        }
        if(leavePolicyObject.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterSecondTransferDate=((afterSecondTransferDate/3.0))*2;
        }

        if(leavePolicyObject.getCredit_on_accural_basis().getBeginOfMonth()){
            afterSecondTransferDate=(afterSecondTransferDate/12.0)*1;
        }

        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth()){
            afterSecondTransferDate=(afterSecondTransferDate/12.0)*0.0;
        }

        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter())
        {
            afterSecondTransferDate=0.0D;
            //firstDeactivagtionBalance=(firstDeactivagtionBalance/5.0)*3.0;
        }
        if(leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()){
            afterSecondTransferDate = (36.0/12.0)*2.0;
        }


        afterSecondTransferDate = afterSecondTransferDate - noOFEncashment;

        double afterSecondTransferBalance = 0.0D;

        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter())
        {
            afterSecondTransferBalance = afterSecondTransferDate + actualBeforeSecondTrnsfer;
        }
        else
         afterSecondTransferBalance=afterSecondTransferDate+firstDeactivagtionBalance+secondDeactionValue;

        Reporter("after Second Transfer Date Balance"+(afterSecondTransferBalance),"Info");
        Reporter("Actual Second  Transfer Date Balance"+actualAfterSecondTransfer,"Info");


        Assert.assertTrue(afterSecondTransferBalance==actualAfterSecondTransfer,"Not Same Actal And Expectd");

    }

}

