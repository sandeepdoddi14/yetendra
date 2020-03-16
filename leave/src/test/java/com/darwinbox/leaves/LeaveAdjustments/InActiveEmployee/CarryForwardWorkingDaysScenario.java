package com.darwinbox.leaves.LeaveAdjustments.InActiveEmployee;

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

public class CarryForwardWorkingDaysScenario extends LeaveAccuralBase {


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

        leaveAdjustmentPolicies=getLeaveAdjustmentPoliciesForCarryForward();

        leaveAdjustmentPolicy=leaveAdjustmentPolicies.stream().filter(x->x.getLeave_Type().contains(testData.get("Leave_Type"))).findFirst().get();

        super.setLeavePolicyObject(leaveAdjustmentPolicy);

        new DateTimeHelper().changeServerDate(driver,LocalDate.now().toString());
        serverChangedDate = LocalDate.now().toString();
        serverDateInFormat = LocalDate.parse(serverChangedDate);

        Assert.assertTrue(setEmployeeId("Y1576663572866"), "Employee ID is set Mnually");

        leavesAction.setEmployeeID("Y1576663572866");
        String userId="213805";
        Assert.assertTrue(leavesAction.removeEmployeeLeaveLogs(), "Employees Leave logs removed successfully") ;

        super.setEmployee(employee);

        leaveCycleStartDate=LocalDate.parse("2019-01-01");
        leaveCycleEndDate = LocalDate.parse("2019-12-31");


        //just making employee doj = leave cycle start date
        DateOfJoining = leaveCycleStartDate.toString();
        changeEmployeeDOJ(leaveCycleStartDate);

        new LeaveSettings().showLeaveAdjustments(leaveAdjustmentPolicy.getLeave_Type());



        new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.plusDays(1).toString());
        serverChangedDate = leaveCycleEndDate.plusDays(1).toString();
        serverDateInFormat = LocalDate.parse(serverChangedDate);

        carryForward = true;

        //leave validity also needs to be set to zero for carry forward
        if(leaveAdjustmentPolicy.getProbation_period_before_leave_validity().custom &&
                !leaveAdjustmentPolicy.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
            leaveAdjustmentPolicy.getProbation_period_before_leave_validity().customMonths=0;


        super.setLeavePolicyObject(leaveAdjustmentPolicy);
        //carry forward balance
       long expecetedCarrytForwardBalance = Math.round(calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining, leaveCycleEndDate.toString()));


        leavesAction.runCarryFrowardCronByEndPointURL();


        Double actualCarryForwardBalance = new LeaveBalanceAPI(EmployeeId,leaveAdjustmentPolicy.getLeave_Type()).getCarryForwardBalance();



        Reporter("Expected CarryForward_one_zero Balance........"+expecetedCarrytForwardBalance+".....Actual CarryForward_one_zero Balance"+actualCarryForwardBalance,"Info");

        Assert.assertTrue(new BigDecimal(actualCarryForwardBalance).setScale(2).equals(new BigDecimal(expecetedCarrytForwardBalance).setScale(2))
                ,"Expected Adjusted Balance = " + actualCarryForwardBalance + "Actual Adjusted Carry Forward Balance = "+ actualCarryForwardBalance+" .. Expected Adjusted Carry Forward Balance ="+expecetedCarrytForwardBalance);

        carryForward = false;

        leaveCycleStartDate= leaveCycleStartDate.plusYears(1);
        leaveCycleEndDate = leaveCycleEndDate.plusYears(1);

        Double expectedTotalBalance=calculateLeaveBalanceAsPerEmployeeWorkingDays(leaveCycleStartDate.toString(),serverChangedDate)+expecetedCarrytForwardBalance;

        Reporter("Employee Working Days Balance as on "+serverChangedDate+"-->"    + expectedTotalBalance,"Info");



        Double adjustedBalance=Double.parseDouble(testData.get("LeaveAdjustment"));

        new ImportServices().importLeaveAdjustmentBalance(EmployeeId,leaveAdjustmentPolicy.getLeave_Type(),adjustedBalance+"",getServerOrLocalDate().getYear()+"");

        Reporter("Import is Performed for adjustment  of "+adjustedBalance +"On "+serverChangedDate ,"Info");



        adjustedBalance = (expectedTotalBalance - adjustedBalance);



        new EmployeeServices().deActivateEmployee(userId,serverChangedDate,serverChangedDate);

        Reporter("Employee Deactivated Date is .."+serverChangedDate.toString(),"Info");

        Double actualCurrentDeactivatedBalance = new LeaveBalanceAPI(EmployeeId,leaveAdjustmentPolicy.getLeave_Type()).getBalance();


        Reporter("Expected Adjusted Balance  -->" +adjustedBalance+ " Actual Adjusted Balance -->"+actualCurrentDeactivatedBalance,"Info");

        Assert.assertTrue(new BigDecimal(adjustedBalance).setScale(2, RoundingMode.HALF_EVEN).equals(new BigDecimal(actualCurrentDeactivatedBalance).setScale(2,RoundingMode.HALF_EVEN)),"Expected Adjusted Balance and Actual Adjusted Balance are Not Same");



    }
}
