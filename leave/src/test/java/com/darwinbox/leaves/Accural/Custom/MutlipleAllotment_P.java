package com.darwinbox.leaves.Accural.Custom;


import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class MutlipleAllotment_P extends LeaveAccuralBase {

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

        LeavePolicyObject multipleAllotmentLeavePolicy = getMultipleAllotmentLeavePolicy(testData);
        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);

        Reporter("Leave Type is"+multipleAllotmentLeavePolicy.getLeave_Type(),"Info");

        //always start from previous year
        LocalDate firstLeaveCycleStartDate=LocalDate.parse("2018-08-01");
        LocalDate firstLeaveCyclceEndDate=LocalDate.parse("2019-07-31");

        leaveCycleStartDate = firstLeaveCycleStartDate;
        leaveCycleEndDate = firstLeaveCyclceEndDate;

        HashMap<String,String> empTypes=new Services().getEmployeeTypes();

        //to generate employee
        //it will create a full time employee
        changeServerDate(LocalDate.now().toString());

        if(multipleAllotmentLeavePolicy.getProbation_period_before_leave_validity().probation)
        employee = new EmployeeServices().generateAnFullTimeEmployee("no","Working Days (DO NOT TOUCH)",leaveCycleStartDate.toString(),"5afd30be47f23");


       super.setEmployee(employee);

        Boolean prorata_afterProbation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;



        //BEFORE FIRST TRANSFER
        changeServerDate(leaveCycleStartDate.plusMonths(5).minusDays(1).toString());
        double beforeFirstTransfer = calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double actualBeforeFirstTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getBalance();

        Reporter("Before first Transfer Balance"+beforeFirstTransfer,"Info");
        Reporter("Actaul Before first Transfer Balance"+actualBeforeFirstTransfer,"Info");


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

        double firstDeactivagtionBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate);
        double firstDeactivagtionBalanceValue=5;
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

            multipleAllotmentLeavePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }




        //EMPLOYEE WILL BE CHANGED FROM FULL TIME TO PART TIME AT THIS DATE
        changeServerDate(leaveCycleStartDate.plusMonths(5).plusDays(1).toString());

        //part time
       new EmployeeServices().addUserEmployment(employee.getMongoID(),"4",empTypes.get(empTypes.keySet().toArray()[1].toString()),leaveCycleStartDate.plusMonths(5).toString());
       multipleAllotmentLeavePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[1]));

       leaveCycleStartDate=leaveCycleStartDate.plusMonths(5);
       leaveCycleEndDate=leaveCycleStartDate.plusYears(1).minusDays(1);

       super.setLeavePolicyObject(multipleAllotmentLeavePolicy);
       double afterFirstTransferDate = calculateLeaveBalance(leaveCycleStartDate.toString(),leaveCycleEndDate.toString());
        double afterFirstTransferDatetemp1 = 24.0;
       double actualAfterFirstTransfer= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getBalance();


        if(!multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()){

            afterFirstTransferDate=(afterFirstTransferDate/12.0)*7;

        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfQuarter())
        {
            afterFirstTransferDate=(afterFirstTransferDate/3.0)*1;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfMonth()){
            afterFirstTransferDate=(afterFirstTransferDate/12.0)*1;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfMonth()){
            afterFirstTransferDate=(afterFirstTransferDate/12.0)*0.0;
        }

        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfQuarter())
        {
            afterFirstTransferDate=0.0D;
            firstDeactivagtionBalance=(firstDeactivagtionBalance/5.0)*3.0;
        }
        if(multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBiAnnual()){
            afterFirstTransferDate = (24/12.0)*1.0;
        }


        double afterFirstTransferBalance=afterFirstTransferDate+firstDeactivagtionBalance;

       Reporter("after First Transfer Date Balance"+(afterFirstTransferBalance),"Info");
       Reporter("Actual After First Transfer Date Balance"+actualAfterFirstTransfer,"Info");


       changeServerDate(firstLeaveCycleStartDate.plusMonths(10).minusDays(1).toString());

       //set for probation = true
      // if(true)


        if(!multipleAllotmentLeavePolicy.getProbation_period_before_leave_validity().probation) {
            if (!multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()) {

                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 7;

            }
            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfQuarter()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 7;
            }
            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfQuarter()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 4;
            }

            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfMonth()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 5.0;
            }


            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfMonth()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 11.0) * 4.0;
            }
            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBiAnnual()) {
                afterFirstTransferDatetemp1 = (24.0 / 12.0) * 7.0;
            }
        }
        else
        {
            //probation=6 months, settig 1st five months =0 ;
            firstDeactivagtionBalanceValue = 0.0D;
            if (!multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getIndicator()) {

                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 6;

            }
            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfQuarter()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 6;
            }
            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfQuarter()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 4;
            }

            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBeginOfMonth()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 12.0) * 4.0;
            }


            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getEndOfMonth()) {
                afterFirstTransferDatetemp1 = (afterFirstTransferDatetemp1 / 11.0) * 3.0;
            }
            if (multipleAllotmentLeavePolicy.getCredit_on_accural_basis().getBiAnnual()) {
                afterFirstTransferDatetemp1 = (24.0 / 12.0) * 6.0;
            }
            if(multipleAllotmentLeavePolicy.getCredit_on_pro_rata_basis().indicator){
                if(multipleAllotmentLeavePolicy.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th){
                    if(LocalDate.parse(employee.getDoj()).plusDays(180).getDayOfMonth()>15)
                    {
                        afterFirstTransferDatetemp1 = afterFirstTransferDatetemp1 +1;
                    }

                }
                if(multipleAllotmentLeavePolicy.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th){
                    if(LocalDate.parse(employee.getDoj()).plusDays(180).getDayOfMonth()>15)
                    {
                        afterFirstTransferDatetemp1 = afterFirstTransferDatetemp1 +2;
                    }

                }

            }
        }


        double actualBeforeSecondTrnsfer= new LeaveBalanceAPI(employee.getEmployeeID(),multipleAllotmentLeavePolicy.getLeave_Type()).getBalance();

        Reporter("before seccond transfer balnce"+(firstDeactivagtionBalanceValue+afterFirstTransferDatetemp1),"Info");

        Reporter("actual before seccond transfer balnce"+(actualBeforeSecondTrnsfer),"Info");






       //contract
        //new EmployeeServices().addUserEmployment(employee.getMongoID(),"4",empTypes.get(empTypes.keySet().toArray()[2].toString()),leaveCycleStartDate.plusMonths(10).toString());










    }

    public void changeServerDate(String date){
        new DateTimeHelper().changeServerDate(driver,date);
        serverDateInFormat=LocalDate.parse(date);
        serverChangedDate=serverDateInFormat.toString();


    }

    }

