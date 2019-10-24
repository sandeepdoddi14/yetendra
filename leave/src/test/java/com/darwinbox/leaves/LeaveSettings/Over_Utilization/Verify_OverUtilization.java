package com.darwinbox.leaves.LeaveSettings.Over_Utilization;


import com.codoid.products.fillo.Recordset;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.utils.QueryExcel;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.actionClasses.EmployeeAction;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class Verify_OverUtilization  extends TestBase {

    private static final Logger log = Logger.getLogger(Verify_OverUtilization.class);


    LeavesPage leavePage;
    LeavesAction leavesAction;
    LoginPage loginpage;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass( ).getName( ));
    }


    @BeforeMethod
    public void pageObjInitialization(){
        leavePage = new LeavesPage(driver);
        leavesAction=new LeavesAction(driver);
        loginpage=new LoginPage(driver);
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyOverUtilization(Map<String, String> data){
        String empId= UtilityHelper.getProperty("config","Employee.id");
        String leaveType=data.get("SelectLeaveType");
        String expecteed=data.get("ExpectedMessage");
        Recordset r = new QueryExcel().getOverUtlizationPolicyFromExcelSheet(leaveType);
        double balance=0.0d;
        double approved = 0.0d;
        double pending = 0.0d;


        Assert.assertTrue(loginpage.empLoginToApplication(),"Error in Employee Login");
        Assert.assertTrue(leavePage.navigateToReqestTask(),"Error in navigating to Request Task Page");
        Assert.assertTrue(leavePage.revokeRequests(),"Error in revoking requests");
        Assert.assertTrue(leavePage.navigateToLeavePage(),"Unable to navigate to Leave Page");

        int calculatedLeavesToBeApplid=new Double(leavesAction.calculateleavesToBeAppliedForOverUtilization(empId,leaveType,r)).intValue();

        if(data.get("properties").toLowerCase().contains("make leave balance 12")) {
            new EmployeeAction(empId).setEmpLeaveBal(12,r);
        }
        else if(data.get("properties").toLowerCase().contains("make leave balance-2")){
            new EmployeeAction(empId).setEmpLeaveBal(2,r);
        }

        Assert.assertTrue(leavePage.setFromAndToDates(calculatedLeavesToBeApplid),"Unable to set Leave Dates");
        Assert.assertTrue(leavePage.navigateToLeavePage(),"Unable to navigate to Leave page");
        Assert.assertTrue(leavePage.applyLeave(),"Unable to apply Leave");

        if(!data.get("ExpectedMessage").contains("error")) {
             balance = new LeaveBalanceAPI(empId, leaveType).getBalance();
            System.out.println("Leave Balance for" + leaveType + "is" + balance);
             pending = new LeaveBalanceAPI(empId, leaveType).getPendingLeaves();

            System.out.println("Pending Leave Balance for" + leaveType + "is" + pending);
             approved = new LeaveBalanceAPI(empId, leaveType).getApprovedLeaves();
            System.out.println("Approved Balance for" + leaveType + "is" + approved);
        }
        if(data.get("ExpectedMessage").contains("paid leaves")){
            Assert.assertTrue(pending+approved>balance,"Over Utilized Leaves are Not Paid Leaves");
        }

        if(data.get("ExpectedMessage").contains("errror"))
        Assert.assertTrue(leavePage.errorMessage().equalsIgnoreCase("Over-utilizing this leave is allowed, But you cannot over-utilize more than 4"));

    }
}
