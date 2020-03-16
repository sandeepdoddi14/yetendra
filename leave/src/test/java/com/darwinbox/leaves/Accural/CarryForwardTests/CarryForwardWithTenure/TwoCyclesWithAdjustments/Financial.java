package com.darwinbox.leaves.Accural.CarryForwardTests.CarryForwardWithTenure.TwoCyclesWithAdjustments;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Accural.Credit_On_Accural_Basis;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.ImportServices;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Services.LeaveSettings;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;


public class Financial extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LeavesPage leavePage;

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

        leavePage = PageFactory.initElements(driver, LeavesPage.class);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyCFWithTenure(Map<String, String> testData) {

        LeavePolicyObject carryForwardBalance = getTenureWithCarryForward(testData);

        int adjustedBalance = -2;



        //always start from current cycle
        leaveCycleStartDate=LocalDate.parse("2019-04-01");
        leaveCycleEndDate=LocalDate.parse("2020-03-31");


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

       //changeServerDate(employee.getDoj());

        new LeaveSettings().showLeaveAdjustments(carryForwardBalance.getLeave_Type());
        //leavePage.setFromAndToDatesWithoutProperty(noOfLeaves, LocalDate.parse(employee.getDoj()));

        String leaveResponse;
        Double expectedBalnce=0.0;

            leaveResponse=new ImportServices().importLeaveAdjustmentBalance(employee.getEmployeeID(),carryForwardBalance.getLeave_Type(),adjustedBalance+"",leaveCycleStartDate.getYear()
                    +"");

        if(leaveResponse.contains("failure"))
        {
            throw new Error("Error in Applying Leaves"+leaveResponse);
        }

        Reporter("Import is Performed for adjustment  of "+adjustedBalance +"-->On "+serverChangedDate ,"Info");




        //before first anniversy
           expectedBalnce = (leavePolicyObject.getMaximum_leave_allowed_per_year()/12.0)*(Period.between(leaveCycleStartDate,firstAnniverseryDate).getMonths()+0.0D);


        //after 1sst aniversery endownment changes to 18
        leavePolicyObject.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Credit No of Leaves").split(",")[0]));
       super.setLeavePolicyObject(leavePolicyObject);
        expectedBalnce = expectedBalnce + (leavePolicyObject.getMaximum_leave_allowed_per_year()/12.0)*(Period.between(firstAnniverseryDate.minusDays(1),leaveCycleEndDate).getMonths()+0.0D);



        //balance = balnce - adjusted
        expectedBalnce = expectedBalnce - adjustedBalance;



        //first carry forward
        changeServerDate(leaveCycleEndDate.plusDays(1).toString());


        Reporter("Leave Cycle Start Date is   -->"+leaveCycleStartDate,"Info");
        Reporter("Leave Cycle End Date is   -->"+leaveCycleEndDate,"Info");
        leavesAction.runCarryFrowardCronByEndPointURL();
        Reporter("CarryForward Cron is Run at -->"+serverChangedDate,"Info");





       // carryForwardBalance.setMaximum_leave_allowed_per_year(adjustedBalance);
        super.setLeavePolicyObject(carryForwardBalance);

        double expectedCarryForwardBalance=getCarryFowardBalance(expectedBalnce);
        expectedCarryForwardBalance=new BigDecimal(expectedCarryForwardBalance).setScale(2, RoundingMode.HALF_UP).doubleValue();

        double actualCarryForwardBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getCarryForwardBalance();


        Reporter("Expected CarryForward Balance --> "+expectedCarryForwardBalance,"Info");
        Reporter("Actual CarryForward Balance --> "+actualCarryForwardBalance,"Info");

        Assert.assertTrue(expectedCarryForwardBalance==actualCarryForwardBalance,"Expected And Actual Carry Forward Balances Are Not Same");


        //second cycle
        LocalDate secondCycleStartDate = leaveCycleStartDate.plusYears(1);
        LocalDate secondCycleEndDate = leaveCycleEndDate.plusYears(1);




        //before second anniversy
        expectedBalnce = expectedCarryForwardBalance+(leavePolicyObject.getMaximum_leave_allowed_per_year()/12.0)*(Period.between(secondCycleStartDate,secondAnniverseryDate).getMonths()+0.0D);


        //after 1sst aniversery endownment changes to 24
        leavePolicyObject.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Credit No of Leaves").split(",")[1].trim()));
        super.setLeavePolicyObject(leavePolicyObject);
        expectedBalnce = expectedBalnce + (leavePolicyObject.getMaximum_leave_allowed_per_year()/12.0)*(Period.between(secondAnniverseryDate.minusDays(1),secondCycleEndDate).getMonths()+0.0D);






        changeServerDate(secondCycleEndDate.plusDays(1));
        leavesAction.runCarryFrowardCronByEndPointURL();
        expectedBalnce= getCarryFowardBalance(expectedBalnce);
        expectedCarryForwardBalance=new BigDecimal(expectedBalnce).setScale(2, RoundingMode.HALF_UP).doubleValue();

        actualCarryForwardBalance = new LeaveBalanceAPI(employee.getEmployeeID(),carryForwardBalance.getLeave_Type()).getCarryForwardBalance();
        actualCarryForwardBalance=new BigDecimal(actualCarryForwardBalance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();




        Reporter("Expected CarryForward Balance --> "+expectedCarryForwardBalance,"Info");
        Reporter("Actual CarryForward Balance --> "+actualCarryForwardBalance,"Info");

        Assert.assertTrue(expectedCarryForwardBalance==actualCarryForwardBalance,"Expected And Actual Carry Forward Balances Are Not Same");






    }

    public void changeServerDate(String date){
        new DateTimeHelper().changeServerDate(driver,date);
        serverDateInFormat=LocalDate.parse(date);
        serverChangedDate=serverDateInFormat.toString();

//LocalDate.now().
    }

    }

