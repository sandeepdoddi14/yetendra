package com.darwinbox.leaves.Accural.Custom;

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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Map;


public class WorkingDays extends LeaveAccuralBase {


    LoginPage loginpage=null;
    CommonAction commonAction=null;
    LeavesAction leavesAction;


    LocalDate DOJ=null;
    Double expectedBalance=0.0;

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

    }
    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyWorkingDays(Map<String,String> testData) {


        LeavePolicyObject workingDaysPolicy=getWorkingDaysPolicy(testData);
        super.setLeavePolicyObject(workingDaysPolicy);

        new DateTimeHelper().changeServerDate(driver,LocalDate.now().toString());

        Assert.assertTrue(setEmployeeId("L1563437636571"), "Employee ID is set Mnually");

        leavesAction.setEmployeeID("L1563437636571");
        Assert.assertTrue(leavesAction.removeEmployeeLeaveLogs(), "Employees Leave logs removed successfully") ;


        leaveCycleStartDate=LocalDate.parse("2018-11-01");
        leaveCycleEndDate = LocalDate.parse("2019-10-31");

        new DateTimeHelper().changeServerDate(driver,leaveCycleEndDate.toString());
        serverChangedDate=leaveCycleEndDate.toString();

        DOJ=leaveCycleEndDate;
        while(!DOJ.isBefore(leaveCycleStartDate)){

            if(new LeavesAction().iterationDateFourTimesPerMonth(DOJ)==true)
            {
                leavesAction.removeEmployeeLeaveLogs();
                changeEmployeeDOJ(DOJ);
                DateOfJoining=DOJ.toString();
                Reporter("DOJ is changed to "+DOJ.toString(),"Info");
                if(workingDaysPolicy.getCredit_on_accural_basis().getConsiderWorkingDays().indicator)
                   expectedBalance= calculateLeaveBalanceAsPerEmployeeWorkingDays(DOJ.toString(),leaveCycleEndDate.minusDays(1).toString());
                else
                   expectedBalance= calculateLeaveBalance(DOJ.toString(),leaveCycleEndDate.toString());

               /* DecimalFormat bd = new DecimalFormat("0.00");
                bd.setRoundingMode(RoundingMode.DOWN);*/

                expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

                double actualBalance = new LeaveBalanceAPI(EmployeeId,workingDaysPolicy.getLeave_Type()).getBalance();
                Reporter("Expected Balance =="+ expectedBalance,"Info");
                Reporter("Actual Balance ==" +actualBalance,"Info");

                //expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;

              //  BigDecimal bigDecimal = new BigDecimal()

                if(expectedBalance==actualBalance)
                Reporter("PASSED |||| Expected and actual are same","Pass");
                else
                    Reporter("Failed |||| Expected and actual are not same","Fail");
            }
            DOJ=DOJ.minusDays(1);
        }

    }

}
