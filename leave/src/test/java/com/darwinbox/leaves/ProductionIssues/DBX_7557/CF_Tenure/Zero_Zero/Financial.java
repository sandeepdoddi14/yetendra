package com.darwinbox.leaves.ProductionIssues.DBX_7557.CF_Tenure.Zero_Zero;

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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;


public class Financial extends LeaveAccuralBase {

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
    public void verifyCFWithTenure(Map<String, String> testData) {

        LeavePolicyObject carryForwardBalance = getTenureWithCarryForward(testData);


        //****Imporant
        //end date is always (current year-1)
        leaveCycleStartDate=LocalDate.parse("2018-04-01");
        leaveCycleEndDate=LocalDate.parse("2019-03-31");


        //super.carryForward=true;
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


        Reporter("Leave Type is"+carryForwardBalance.getLeave_Type(),"Info");




        //to generate employee
        changeServerDate(LocalDate.now().toString());
        try {
            employee = new EmployeeServices().generateAnEmployee("no","Working Days (DO NOT TOUCH)",leaveCycleStartDate.minusMonths(2).toString(),"no");
        }
        catch(Exception e)
        {
            employee = new EmployeeServices().generateAnEmployee("no","Working Days (DO NOT TOUCH)",leaveCycleStartDate.minusMonths(2).toString(),"no");
        }

        super.setEmployee(employee);
        leavesAction.setEmployeeID(employee.getEmployeeID());



        Reporter("Emp DOJ is --->"+employee.getDoj(),"Info");

        LocalDate firstAnniverseryDate = LocalDate.parse(employee.getDoj()).plusYears(1);
        LocalDate secondAnniverseryDate = LocalDate.parse(employee.getDoj()).plusYears(2);

        //first carry forward
        changeServerDate(leaveCycleStartDate.toString());


        Reporter("Leave Cycle Start Date is   -->"+leaveCycleStartDate,"Info");
        Reporter("Leave Cycle End Date is   -->"+leaveCycleEndDate,"Info");
        leavesAction.runCarryFrowardCronByEndPointURL();
        Reporter("CarryForward Cron is Run at -->"+serverChangedDate,"Info");



        double expectedCarryForwardBalance=getCarryFowardBalance(calculateLeaveBalance(employee.getDoj(),LocalDate.parse(serverChangedDate).minusDays(1).toString()))   ;
        double actualCarryForwardBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getCarryForwardBalance();


        Reporter("Expected CarryForward Balance --> "+expectedCarryForwardBalance,"Info");
        Reporter("Actual CarryForward Balance --> "+actualCarryForwardBalance,"Info");

        Assert.assertTrue(expectedCarryForwardBalance==actualCarryForwardBalance,"Expected And Actual Carry Forward Balances Are Not Same");


        //1-1 combination
        carryForwardBalance=getTenureWithCarryForward(setZeroCarryForwardMapData(testData));
        super.setLeavePolicyObject(carryForwardBalance);

        Double untillFirstAniversery=calculateLeaveBalance(leaveCycleStartDate.toString(),firstAnniverseryDate.minusDays(1).toString());

        int monthDiff=0;
        if(!leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
        {
           monthDiff= (int) new DateTimeHelper().getMonthDifferenceBetweenTwoDates(leaveCycleStartDate.toString(),firstAnniverseryDate.toString());
            untillFirstAniversery = (untillFirstAniversery/12)*monthDiff;
        }

        carryForwardBalance.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Credit No of Leaves").split(",")[0]));
        testData.put("Max_Leaves_Allowed_Per_Year",Integer.parseInt(testData.get("Credit No of Leaves").split(",")[0])+"");


        super.setLeavePolicyObject(carryForwardBalance);


        Double afterFirstAnniverseryBalance = calculateLeaveBalance(firstAnniverseryDate.toString(),leaveCycleEndDate.toString());

        if(!leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
        {
            monthDiff= 12-monthDiff;
            afterFirstAnniverseryBalance = (afterFirstAnniverseryBalance/12)*monthDiff;
        }





        expectedCarryForwardBalance =  getCarryFowardBalance(expectedCarryForwardBalance + untillFirstAniversery +afterFirstAnniverseryBalance);

        expectedCarryForwardBalance=new BigDecimal(expectedCarryForwardBalance).setScale(2, RoundingMode.HALF_UP).doubleValue();

        changeServerDate(leaveCycleEndDate.plusDays(1));

        leavesAction.runCarryFrowardCronByEndPointURL();


        actualCarryForwardBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getCarryForwardBalance();





        Reporter("Expected CarryForward Balance --> "+expectedCarryForwardBalance,"Info");
        Reporter("Actual CarryForward Balance --> "+actualCarryForwardBalance,"Info");

        Assert.assertTrue(expectedCarryForwardBalance==actualCarryForwardBalance,"Expected And Actual Carry Forward Balances Are Not Same");


        //1-1 combination
        carryForwardBalance=getTenureWithCarryForward(setZeroCarryForwardMapData(testData));
        super.setLeavePolicyObject(carryForwardBalance);


        Double untillSecondAnniverseryBalance=calculateLeaveBalance(leaveCycleStartDate.plusYears(1).toString(),secondAnniverseryDate.minusDays(1).toString());


        if(!leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
        {
            monthDiff= (int) new DateTimeHelper().getMonthDifferenceBetweenTwoDates(leaveCycleStartDate.plusYears(1).toString(),secondAnniverseryDate.toString());
            untillSecondAnniverseryBalance = (untillSecondAnniverseryBalance/12)*monthDiff;
        }

        carryForwardBalance.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Credit No of Leaves").split(",")[1].trim()));



        super.setLeavePolicyObject(carryForwardBalance);


        Double afterSecondAnniverseryBalance = calculateLeaveBalance(secondAnniverseryDate.toString(),leaveCycleEndDate.plusYears(1).toString());

        if(!leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
        {
            monthDiff= 12-monthDiff;
            afterSecondAnniverseryBalance = (afterSecondAnniverseryBalance/12)*monthDiff;
        }




        expectedCarryForwardBalance =  (getCarryFowardBalance(expectedCarryForwardBalance + untillSecondAnniverseryBalance +afterSecondAnniverseryBalance));
        expectedCarryForwardBalance = new BigDecimal(expectedCarryForwardBalance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();


        changeServerDate(leaveCycleEndDate.plusYears(1).plusDays(1));

        leavesAction.runCarryFrowardCronByEndPointURL();


        actualCarryForwardBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getCarryForwardBalance();
       actualCarryForwardBalance = new BigDecimal(actualCarryForwardBalance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();


        Reporter("Expected CarryForward Balance --> "+expectedCarryForwardBalance,"Info");
        Reporter("Actual CarryForward Balance --> "+actualCarryForwardBalance,"Info");

        Assert.assertTrue(expectedCarryForwardBalance==actualCarryForwardBalance,"Expected And Actual Carry Forward Balances Are Not Same");
    }

    public void changeServerDate(String date){
        new DateTimeHelper().changeServerDate(driver,date);
        serverDateInFormat=LocalDate.parse(date);
        serverChangedDate=serverDateInFormat.toString();


    }

    }

