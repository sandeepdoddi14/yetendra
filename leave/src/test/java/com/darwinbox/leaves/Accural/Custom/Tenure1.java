package com.darwinbox.leaves.Accural.Custom;

import Objects.Employee;
import Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.EmployeeServices;
import Service.LeaveBalanceAPI;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;


public class Tenure1 extends LeaveAccuralBase {

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
    public void verifyTenureBalance(Map<String, String> testData) {

        LeavePolicyObject tenureLeaveBalancePolicy = getTenureBalancePolicy(testData);
        super.setLeavePolicyObject(tenureLeaveBalancePolicy);

        Reporter("Leave Type is"+tenureLeaveBalancePolicy.getLeave_Type(),"Info");

        leaveCycleStartDate = LocalDate.parse("2019-08-01");
        leaveCycleEndDate = LocalDate.parse("2020-07-31");

        //to generate employee
        new DateTimeHelper().changeServerDate(driver,leaveCycleStartDate.plusMonths(2).toString());
        employee = new EmployeeServices().generateAnEmployee("no","Working Days (DO NOT TOUCH)",leaveCycleStartDate.plusMonths(2).toString(),"no");
        super.setEmployee(employee);



        //1 st tenure checks
        serverDateInFormat=leaveCycleEndDate;
        serverChangedDate=serverDateInFormat.toString();
        new DateTimeHelper().changeServerDate(driver,serverChangedDate);



        double firstCycleEndDate=calculateLeaveBalance(employee.getDoj(),serverChangedDate);
        double actualfirstCycleEndDate = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();




        Reporter(" first cycle Balance "+firstCycleEndDate ,"Info");

        Reporter("Actual  first cycle Balance "+actualfirstCycleEndDate ,"Info");

        leaveCycleStartDate = LocalDate.parse("2020-08-01");
        leaveCycleEndDate = LocalDate.parse("2021-07-31");

        serverDateInFormat=leaveCycleStartDate.plusDays(1);
        serverChangedDate=serverDateInFormat.toString();
        new DateTimeHelper().changeServerDate(driver,serverChangedDate);

        double firstTenureafterCycleEnd=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double actualfirstTenureafterCycleEnd = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();



        Reporter("after  first cycle Balance "+firstTenureafterCycleEnd ,"Info");

        Reporter("Actual after  first cycle Balance "+actualfirstTenureafterCycleEnd ,"Info");

        LocalDate beforeFirstAnniveryDate=LocalDate.parse(employee.getDoj()).plusYears(1).minusDays(2);
        serverDateInFormat=beforeFirstAnniveryDate;
        serverChangedDate=serverDateInFormat.toString();
        new DateTimeHelper().changeServerDate(driver,serverChangedDate);

        double beforeFirstAnniversery=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double actualbeforeFirstAnniversery = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();

        Reporter("before first Anniversery Balance "+beforeFirstAnniversery ,"Info");

        Reporter("Actual before first Anniversery Balance "+actualbeforeFirstAnniversery ,"Info");


        LocalDate afterFirstAnniveryDate=LocalDate.parse(employee.getDoj()).plusYears(1).plusDays(2);
        LocalDate firstAnniverseryDate=LocalDate.parse(employee.getDoj()).plusYears(1).minusDays(1);


        new DateTimeHelper().changeServerDate(driver,firstAnniverseryDate.toString());
        serverDateInFormat=firstAnniverseryDate;
        serverChangedDate=firstAnniverseryDate.toString();


        deActiavation=true;
        //making default to begin of month for calculation
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=tenureLeaveBalancePolicy.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            tenureLeaveBalancePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }
        super.setLeavePolicyObject(tenureLeaveBalancePolicy);
        double firstDeactivagtionBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),firstAnniverseryDate.toString());
        deActiavation=false;


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



            tenureLeaveBalancePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }


        //double beforeLeaves=(tenureLeaveBalancePolicy.getMaximum_leave_allowed_per_year()/12)*(Period.between(leaveCycleStartDate,afterFirstAnniveryDate).getMonths());
        //double afterLeaves=(Double.parseDouble(testData.get("Credit No of Leaves").split(",")[0])/12)*(12-beforeLeaves);
        double bedoreMOnths=Period.between(leaveCycleStartDate,afterFirstAnniveryDate).getMonths();
        double aferMonths=12.0-bedoreMOnths;

        leaveCycleStartDate=firstAnniverseryDate.plusDays(1);
        leaveCycleEndDate=firstAnniverseryDate.plusYears(1);

        //tenureLeaveBalancePolicy.setMaximum_leave_allowed_per_year((int)(beforeLeaves+afterLeaves));
        tenureLeaveBalancePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Credit No of Leaves").split(",")[0]));
        super.setLeavePolicyObject(tenureLeaveBalancePolicy);

        new DateTimeHelper().changeServerDate(driver,afterFirstAnniveryDate.toString());
        serverDateInFormat=afterFirstAnniveryDate;
        serverChangedDate=afterFirstAnniveryDate.toString();

         double afterfirstAniverseryBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),afterFirstAnniveryDate.toString());
         if(!tenureLeaveBalancePolicy.getCredit_on_accural_basis().getIndicator()){
            afterfirstAniverseryBalance=(afterfirstAniverseryBalance/12.0)*aferMonths;

         }
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterfirstAniverseryBalance=afterfirstAniverseryBalance/3.0;
        }

        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            firstDeactivagtionBalance=0.0D;
            afterfirstAniverseryBalance=0.0D;
        }
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBiAnnual()){
            afterfirstAniverseryBalance = (afterfirstAniverseryBalance/6.0)*4.0;
        }


        double actualafterfirstAniverseryBalance = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();



        Reporter("After first Anniversery Balance "+(firstDeactivagtionBalance+afterfirstAniverseryBalance) ,"Info");

        Reporter("Actual After first Anniversery Balance "+actualafterfirstAniverseryBalance ,"Info");


        leaveCycleStartDate=firstAnniverseryDate.plusDays(1);
        leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);

        new DateTimeHelper().changeServerDate(driver,"2021-07-31");
        serverChangedDate=leaveCycleEndDate.minusMonths(3).toString();
        serverDateInFormat=leaveCycleEndDate.minusMonths(3);

        double secondLeaveCycleEndingBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());


        if(!tenureLeaveBalancePolicy.getCredit_on_accural_basis().getIndicator()){
            secondLeaveCycleEndingBalance=(secondLeaveCycleEndingBalance/12.0)*10;

        }
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getEndOfMonth()){
            secondLeaveCycleEndingBalance = (secondLeaveCycleEndingBalance/12.0)*9.0;
        }
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            secondLeaveCycleEndingBalance=(secondLeaveCycleEndingBalance/12.0)/9.0;
        }

        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            //firstDeactivagtionBalance=secondLeaveCycleEndingBalance;
            secondLeaveCycleEndingBalance=(secondLeaveCycleEndingBalance/12.0)/6.0;
        }
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBiAnnual()){
            afterfirstAniverseryBalance = (afterfirstAniverseryBalance/12.0)*10.0;
        }


        double actualatSecondLeaveCycleEndingBalance = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();



        Reporter("before second cycle end Balance "+(2.0+secondLeaveCycleEndingBalance) ,"Info");

        Reporter("actual before second cycle end Balance "+actualatSecondLeaveCycleEndingBalance ,"Info");







    }
    }

