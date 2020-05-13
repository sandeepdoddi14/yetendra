package com.darwinbox.leaves.Accural.WorkingDays.MultipleAllotment.MAWithCF;

import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

public class WorkingDaysWithMultipleAllotmentCF extends LeaveAccuralBase {

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;



    LocalDate doj=null;

    LeavesAction leavesAction = null;

   Double expecetedLeaveBalacne=null;
   Double actualLeaveBalance=null;
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

        //maually enter date ranges
        //for empluee changes
        // every 3 months


        leaveCycleStartDate = LocalDate.parse("2019-04-01");
        leaveCycleEndDate = LocalDate.parse("2020-03-31");


        LocalDate fullTimeStartDate= leaveCycleStartDate;
        LocalDate fullTimeEndDate= leaveCycleStartDate.plusMonths(3).minusDays(1);

        LocalDate partTimeStartDate= leaveCycleStartDate.plusMonths(3);
        LocalDate partTimeEndDate= leaveCycleStartDate.plusMonths(6).minusDays(1);

        LocalDate contractStartDate= leaveCycleStartDate.plusMonths(6);
        LocalDate contractEndDate= leaveCycleStartDate.plusMonths(9).minusDays(1);

        LeavePolicyObject deactivationLeaveBalance = getWorkingDaysPolicy(testData);

        changeServerDate(contractEndDate);

        super.setLeavePolicyObject(deactivationLeaveBalance);




        Assert.assertTrue(setEmployeeId("W1585513633203"), "Employee ID is set Mnually");
        leavesAction.setEmployeeID("W1585513633203");

       Assert.assertTrue(leavesAction.removeEmployeeLeaveLogs(), "Employees Leave logs removed successfully") ;
        DateOfJoining=leaveCycleStartDate.toString();




        expecetedLeaveBalacne = calculateLeaveBalanceAsPerEmployeeWorkingDays(fullTimeStartDate.toString(), fullTimeEndDate.toString());




        Reporter("Emplyee is chnged to part time "+partTimeStartDate,"Info");
        deactivationLeaveBalance.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[1]));
        super.setLeavePolicyObject(deactivationLeaveBalance);
        expecetedLeaveBalacne = expecetedLeaveBalacne+calculateLeaveBalanceAsPerEmployeeWorkingDays(partTimeStartDate.toString(), partTimeEndDate.toString());


        Reporter("Emplyee is chnged to contract time "+contractStartDate,"Info");
        deactivationLeaveBalance.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[2]));
        super.setLeavePolicyObject(deactivationLeaveBalance);
        expecetedLeaveBalacne = expecetedLeaveBalacne+calculateLeaveBalanceAsPerEmployeeWorkingDays(contractStartDate.toString(), contractEndDate.toString());



        actualLeaveBalance= new LeaveBalanceAPI(EmployeeId,deactivationLeaveBalance.getLeave_Type()).getBalance();



        Reporter("Expected Leave Balance is -->"+expecetedLeaveBalacne,"Info");
        Reporter("Actual Leave Balance is -->"+actualLeaveBalance,"Info");

        Assert.assertTrue(expecetedLeaveBalacne==actualLeaveBalance,"Actual and Expected Are Not Same");



        leaveCycleStartDate.plusYears(1);

        leavesAction.runCarryFrowardCronByEndPointURL();

        expecetedLeaveBalacne = getCarryFowardBalance(expecetedLeaveBalacne);
        actualLeaveBalance= new LeaveBalanceAPI(EmployeeId,deactivationLeaveBalance.getLeave_Type()).getCarryForwardBalance();


        Reporter("Expected CF Balance is -->"+expecetedLeaveBalacne,"Info");
        Reporter("Actual CF Balance is -->"+actualLeaveBalance,"Info");

        Assert.assertTrue(expecetedLeaveBalacne==actualLeaveBalance,"Actual and Expected Are Not Same");


    }


}
