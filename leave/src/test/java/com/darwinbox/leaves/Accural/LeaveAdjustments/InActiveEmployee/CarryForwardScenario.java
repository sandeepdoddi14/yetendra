package com.darwinbox.leaves.Accural.LeaveAdjustments.InActiveEmployee;

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
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CarryForwardScenario extends LeaveAccuralBase {


    Employee employee = null;
    LocalDate dateOfJoining= null;
    List<LeavePolicyObject> leaveAdjustmentPolicies=null;
    LeavePolicyObject leaveAdjustmentPolicy=null;
    String employeeProbation="no";

    LoginPage loginpage=null;
    CommonAction commonAction=null;
    LeavesAction leavesAction=null;

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

        leavesAction = new LeavesAction(driver);

    }
    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyLeaveBalance(Map<String,String> testData) {


        LocalDate leaveAdjustedDate = LocalDate.now();

        leaveAdjustmentPolicies=getLeaveAdjustmentPolicies();

        leaveAdjustmentPolicy=leaveAdjustmentPolicies.stream().filter(x->x.getLeave_Type().contains(testData.get("Leave_Type"))).findFirst().get();

        super.setLeavePolicyObject(leaveAdjustmentPolicy);

        new DateTimeHelper().changeServerDate(driver,LocalDate.now().toString());
        serverChangedDate = LocalDate.now().toString();
        serverDateInFormat = LocalDate.parse(serverChangedDate);

        leaveCycleStartDate=LocalDate.parse("2019-04-01");
        leaveCycleEndDate = LocalDate.parse("2020-03-31");


        employee= new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", leaveCycleStartDate.toString(), "no");
        leavesAction.setEmployeeID(employee.getEmployeeID());

        super.setEmployee(employee);


        new LeaveSettings().showLeaveAdjustments(leaveAdjustmentPolicy.getLeave_Type());



        new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.plusDays(1).toString());
        serverChangedDate = leaveCycleEndDate.plusDays(1).toString();
        serverDateInFormat = LocalDate.parse(serverChangedDate);

        carryForward = true;

        //making default to begin of month for calculation
        if(leaveAdjustmentPolicy.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=leaveAdjustmentPolicy.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            leaveAdjustmentPolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }

        //leave validity also needs to be set to zero for carry forward
        if(leaveAdjustmentPolicy.getProbation_period_before_leave_validity().custom &&
                !leaveAdjustmentPolicy.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            leaveAdjustmentPolicy.getProbation_period_before_leave_validity().customMonths=0;

        //if(carryForwardBalance.getProbation_period_before_leave_validity().probation)
        ///carryForwardBalance.getProbation_period_before_leave_validity()

        Double expecetedCarrytForwardBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),getServerOrLocalDate().toString());
        //call carry forward if accural is "NO"
        //Double carrytForwardBalance=calculateLeaveBalance(leaveAdjustedDate.toString(),getServerOrLocalDate().toString());
        if(leaveAdjustmentPolicy.getCarryForwardUnusedLeave().indicator==false)
            expecetedCarrytForwardBalance =0.0;
        else
        {
            if (leaveAdjustmentPolicy.getCarryForwardUnusedLeave().indicator==true)
            {
                if(leaveAdjustmentPolicy.getCarryForwardUnusedLeave().carryForwardAllUnusedLeave) {
                    expecetedCarrytForwardBalance = expecetedCarrytForwardBalance;
                } else if (leaveAdjustmentPolicy.getCarryForwardUnusedLeave().fixed) {
                    double fixedValue = Double.valueOf(leaveAdjustmentPolicy.getCarryForwardUnusedLeave().fixedValue);
                    if (fixedValue > expecetedCarrytForwardBalance) {
                        expecetedCarrytForwardBalance = expecetedCarrytForwardBalance;
                    } else if (fixedValue <= expecetedCarrytForwardBalance) {
                        expecetedCarrytForwardBalance = fixedValue;
                    }
                } else if (leaveAdjustmentPolicy.getCarryForwardUnusedLeave().percentage) {
                    double percentageValue = Double.valueOf(leaveAdjustmentPolicy.getCarryForwardUnusedLeave().percentageValue);
                    expecetedCarrytForwardBalance = ((expecetedCarrytForwardBalance * percentageValue) / 100);
                } else {
                    throw new RuntimeException("Parameters provided to calculate carry forward balance are not proper.");
                }
            }
        }



        leavesAction.runCarryFrowardCronByEndPointURL();


        Double actualCarryForwardBalance = new LeaveBalanceAPI(employee.getEmployeeID(),leaveAdjustmentPolicy.getLeave_Type()).getCarryForwardBalance();



        Reporter("Expected CarryForward_one_zero Balance........"+expecetedCarrytForwardBalance+".....Actual CarryForward_one_zero Balance"+actualCarryForwardBalance,"Info");

        Assert.assertTrue(new BigDecimal(actualCarryForwardBalance).setScale(2).equals(new BigDecimal(expecetedCarrytForwardBalance).setScale(2))
                ,"Expected Adjusted Balance = " + actualCarryForwardBalance + "Actual Adjusted Carry Forward Balance = "+ actualCarryForwardBalance+" .. Expected Adjusted Carry Forward Balance ="+expecetedCarrytForwardBalance);

        carryForward = false;

        leaveCycleStartDate= leaveCycleStartDate.plusYears(1);
        leaveCycleEndDate = leaveCycleEndDate.plusYears(1);

        Double expectedTotalBalance=calculateLeaveBalance(leaveCycleStartDate.toString(),serverChangedDate)+expecetedCarrytForwardBalance;

        Reporter("Employee Balance as on "+serverChangedDate+"-->"    + expectedTotalBalance,"Info");

        Double adjustedBalance=Double.parseDouble(testData.get("LeaveAdjustment"));

        new ImportServices().importLeaveAdjustmentBalance(employee.getEmployeeID(),leaveAdjustmentPolicy.getLeave_Type(),adjustedBalance+"",leaveCycleStartDate.getYear()+"");

        Reporter("Import is Performed for adjustment  of "+adjustedBalance +"On "+serverChangedDate ,"Info");


        adjustedBalance = (expectedTotalBalance - adjustedBalance);



        new EmployeeServices().deActivateEmployee(employee.getUserID(),serverChangedDate,serverChangedDate);

        Reporter("Employee Deactivated Date is .."+serverChangedDate.toString(),"Info");

        Double actualCurrentDeactivatedBalance = new LeaveBalanceAPI(employee.getEmployeeID(),leaveAdjustmentPolicy.getLeave_Type()).getBalance();


        Reporter("Expected Adjusted Balance  -->" +adjustedBalance+ " Actual Adjusted Balance -->"+actualCurrentDeactivatedBalance,"Info");

        Assert.assertTrue(new BigDecimal(adjustedBalance).setScale(2, RoundingMode.HALF_EVEN).equals(new BigDecimal(actualCurrentDeactivatedBalance).setScale(2,RoundingMode.HALF_EVEN)),"Expected Adjusted Balance and Actual Adjusted Balance are Not Same");



    }
}
