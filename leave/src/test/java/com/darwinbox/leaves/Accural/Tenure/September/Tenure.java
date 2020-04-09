package com.darwinbox.leaves.Accural.Tenure.September;

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
import java.time.Period;
import java.util.Map;


public class Tenure extends LeaveAccuralBase {

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

        //always start from previous year
        LocalDate firstLeaveCycleStartDate=LocalDate.parse("2018-04-01");
        LocalDate firstLeaveCyclceEndDate=LocalDate.parse("2019-08-31");

        leaveCycleStartDate = firstLeaveCycleStartDate;
        leaveCycleEndDate = firstLeaveCyclceEndDate;

        //to generate employee
        changeServerDate(LocalDate.now().toString());
        employee = new EmployeeServices().generateAnEmployee("no","Working Days (DO NOT TOUCH)",leaveCycleStartDate.plusMonths(2).toString(),"no");
        super.setEmployee(employee);



        //1 st tenure checks
        changeServerDate(leaveCycleEndDate.toString());

        double firstCycleEndDate=calculateLeaveBalance(employee.getDoj(),serverChangedDate);
        double actualfirstCycleEndDate = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();

        Reporter(" first cycle Balance "+firstCycleEndDate ,"Info");
        Reporter("Actual  first cycle Balance "+actualfirstCycleEndDate ,"Info");


        //moving to second leave cycle
        leaveCycleStartDate = firstLeaveCycleStartDate.plusYears(1);
        leaveCycleEndDate = firstLeaveCyclceEndDate.plusYears(1);

        changeServerDate(leaveCycleStartDate.plusDays(1).toString());
        double firstTenureafterCycleEnd=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double actualfirstTenureafterCycleEnd = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();


        Reporter("after  first cycle Balance "+firstTenureafterCycleEnd ,"Info");
        Reporter("Actual after  first cycle Balance "+actualfirstTenureafterCycleEnd ,"Info");

        LocalDate beforeFirstAnniveryDate=LocalDate.parse(employee.getDoj()).plusYears(1).minusDays(2);
        changeServerDate(beforeFirstAnniveryDate.toString());

        double beforeFirstAnniversery=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double actualbeforeFirstAnniversery = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();

        Reporter("before first Anniversery Balance "+beforeFirstAnniversery ,"Info");
        Reporter("Actual before first Anniversery Balance "+actualbeforeFirstAnniversery ,"Info");


        LocalDate afterFirstAnniveryDate=LocalDate.parse(employee.getDoj()).plusYears(1).plusDays(2);
        LocalDate firstAnniverseryDate=LocalDate.parse(employee.getDoj()).plusYears(1).minusDays(1);


        changeServerDate(firstAnniverseryDate.toString());


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
        double firstDeactivagtionBalanceValue=firstDeactivagtionBalance;
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

        changeServerDate(afterFirstAnniveryDate.toString());

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



       LocalDate secondLeaveCycleEndDate=firstLeaveCyclceEndDate.plusYears(1);
       changeServerDate(secondLeaveCycleEndDate.toString());


       double secondLeaveCycleEndDateBalance=calculateLeaveBalance(firstAnniverseryDate.plusDays(1).toString(),firstAnniverseryDate.plusYears(1).toString());

        if(!tenureLeaveBalancePolicy.getCredit_on_accural_basis().getIndicator()
         || tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBeginOfQuarter()){

            secondLeaveCycleEndDateBalance=(secondLeaveCycleEndDateBalance/12.0)*10.0;

        }

        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBeginOfMonth()){
            secondLeaveCycleEndDateBalance =(secondLeaveCycleEndDateBalance/12.0)*10.0;
        }

        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getEndOfMonth()){
            secondLeaveCycleEndDateBalance =(secondLeaveCycleEndDateBalance/11.0)*9.0;
        }

        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBiAnnual()){
            secondLeaveCycleEndDateBalance = (secondLeaveCycleEndDateBalance/12.0)*10.0;
        }

        //calculate per month leaves and add 7 month leave to deactivation balance
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            secondLeaveCycleEndDateBalance =(secondLeaveCycleEndDateBalance/9.0)*7.0;
        }


        Reporter("firstDeactivationBalance"+firstDeactivagtionBalanceValue,"Info");
        Reporter("SecondCycleEndBalance"+secondLeaveCycleEndDateBalance,"Info");

        double actualSecondCycleEndBalance = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();


        Reporter("ActualSecondCycleEndBalance"+actualSecondCycleEndBalance,"Info");

        leaveCycleStartDate=firstLeaveCycleStartDate.plusYears(2);
        leaveCycleEndDate=firstLeaveCyclceEndDate.plusYears(2);



        changeServerDate(leaveCycleStartDate.plusDays(1).toString());
        double actualThirdCycleStartDate=new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();

        double thirdCycleStartDate =  calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);



        Reporter("actualThirdCycleStartDate"+actualThirdCycleStartDate,"Info");
        Reporter("thirdCycleStartDate Balance"+thirdCycleStartDate,"Info");

        LocalDate secondAnniverseryDate=LocalDate.parse(employee.getDoj()).plusYears(2).minusDays(1);
        LocalDate afterSecondAnniverseryDate=secondAnniverseryDate.plusDays(3);

        changeServerDate(secondAnniverseryDate.toString());


        double beforeSecondDeactivationBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),secondAnniverseryDate.toString());
        double actualBeforeSecondDeactivationDateBalance=new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();

        Reporter("Second  BeforeAnnivery Date Balance"+beforeSecondDeactivationBalance,"info");
        Reporter("Actual BeforeSecond Annivery Date Balance"+actualBeforeSecondDeactivationDateBalance,"info");

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
        double secondDeactivagtionBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),secondAnniverseryDate.toString());
        double secondDeactivagtionBalanceValue=firstDeactivagtionBalance;
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




        double bedoreMOnths1=Period.between(leaveCycleStartDate,afterFirstAnniveryDate).getMonths();
        double aferMonths1=12.0-bedoreMOnths;

        leaveCycleStartDate=secondAnniverseryDate.plusDays(1);
        leaveCycleEndDate=secondAnniverseryDate.plusYears(1);

        //tenureLeaveBalancePolicy.setMaximum_leave_allowed_per_year((int)(beforeLeaves+afterLeaves));
        tenureLeaveBalancePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Credit No of Leaves").split(",")[1].trim()));
        super.setLeavePolicyObject(tenureLeaveBalancePolicy);

        changeServerDate(afterSecondAnniverseryDate.toString());

        double afterSecondAnniverseryDateBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),afterSecondAnniverseryDate.toString());
        if(!tenureLeaveBalancePolicy.getCredit_on_accural_basis().getIndicator()){

            afterSecondAnniverseryDateBalance=(afterSecondAnniverseryDateBalance/12.0)*aferMonths;

        }
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterSecondAnniverseryDateBalance=afterSecondAnniverseryDateBalance/3.0;
        }

        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            secondDeactivagtionBalance=0.0D;
            afterSecondAnniverseryDateBalance=0.0D;
        }
        if(tenureLeaveBalancePolicy.getCredit_on_accural_basis().getBiAnnual()){
            afterSecondAnniverseryDateBalance = (afterSecondAnniverseryDateBalance/6.0)*4.0;
        }


        double actualafterSecondAniverseryBalance = new LeaveBalanceAPI(employee.getEmployeeID(),tenureLeaveBalancePolicy.getLeave_Type()).getBalance();


        Reporter("After second Anniversery Balance "+(secondDeactivagtionBalance+afterSecondAnniverseryDateBalance) ,"Info");
        Reporter("Actual After second Anniversery Balance "+actualafterSecondAniverseryBalance ,"Info");

























    }

    public void changeServerDate(String date){
        new DateTimeHelper().changeServerDate(driver,date);
        serverDateInFormat=LocalDate.parse(date);
        serverChangedDate=serverDateInFormat.toString();


    }

    }

