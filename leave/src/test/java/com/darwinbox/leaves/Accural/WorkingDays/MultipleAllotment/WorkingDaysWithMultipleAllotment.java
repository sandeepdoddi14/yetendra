package com.darwinbox.leaves.Accural.WorkingDays.MultipleAllotment;

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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

public class WorkingDaysWithMultipleAllotment extends LeaveAccuralBase {

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


        leaveCycleStartDate = LocalDate.parse("2019-01-01");
        leaveCycleEndDate = LocalDate.parse("2019-12-31");


        LocalDate fullTimeStartDate= leaveCycleStartDate;
        LocalDate fullTimeEndDate= leaveCycleStartDate.plusMonths(3).minusDays(1);

        LocalDate partTimeStartDate= leaveCycleStartDate.plusMonths(3);
        LocalDate partTimeEndDate= leaveCycleStartDate.plusMonths(6).minusDays(1);

        LocalDate contractStartDate= leaveCycleStartDate.plusMonths(6);
        LocalDate contractEndDate= leaveCycleStartDate.plusMonths(9).minusDays(1);

        LeavePolicyObject deactivationLeaveBalance = getWorkingDaysPolicy(testData);

      /*  //making default to begin of month for calculation
        if(deactivationLeaveBalance.getCredit_on_accural_basis().getIndicator()){
            Credit_On_Accural_Basis credit_on_accural_basis=deactivationLeaveBalance.getCredit_on_accural_basis();
            credit_on_accural_basis.setMonthlyAccuralSetting(true,true,false);
            credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            credit_on_accural_basis.setBiAnnual(false);
            deactivationLeaveBalance.setCredit_on_accural_basis(credit_on_accural_basis);
        }*/

        super.setLeavePolicyObject(deactivationLeaveBalance);




        Assert.assertTrue(setEmployeeId("L1582269310068"), "Employee ID is set Mnually");

        leavesAction.setEmployeeID("L1582269310068");
        Assert.assertTrue(leavesAction.removeEmployeeLeaveLogs(), "Employees Leave logs removed successfully") ;
        DateOfJoining=leaveCycleStartDate.toString();



        changeServerDate(fullTimeEndDate);
        expecetedLeaveBalacne = calculateLeaveBalanceAsPerEmployeeWorkingDays(fullTimeStartDate.toString(), fullTimeEndDate.toString());




        changeServerDate(partTimeStartDate);
        deactivationLeaveBalance.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[1]));
        super.setLeavePolicyObject(deactivationLeaveBalance);



        changeServerDate(partTimeEndDate);
        expecetedLeaveBalacne = expecetedLeaveBalacne+calculateLeaveBalanceAsPerEmployeeWorkingDays(partTimeStartDate.toString(), partTimeEndDate.toString());


        changeServerDate(contractStartDate);
        deactivationLeaveBalance.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Alloted Leaves").split(",")[2]));
        super.setLeavePolicyObject(deactivationLeaveBalance);


        changeServerDate(contractEndDate);
        expecetedLeaveBalacne = expecetedLeaveBalacne+calculateLeaveBalanceAsPerEmployeeWorkingDays(contractStartDate.toString(), contractEndDate.toString());



        actualLeaveBalance= new LeaveBalanceAPI(EmployeeId,deactivationLeaveBalance.getLeave_Type()).getBalance();




    }


}
