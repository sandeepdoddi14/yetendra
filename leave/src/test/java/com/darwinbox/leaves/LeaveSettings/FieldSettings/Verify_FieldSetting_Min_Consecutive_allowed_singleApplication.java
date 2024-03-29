package com.darwinbox.leaves.LeaveSettings.FieldSettings;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class Verify_FieldSetting_Min_Consecutive_allowed_singleApplication extends TestBase {

    private static final Logger log = Logger.getLogger(Verify_FieldSetting_Min_Consecutive_allowed_singleApplication.class);

    LoginPage loginpage;
    LeavesAction leavesAction;
    LeavesPage leavePage;


    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects() {
        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        leavePage = PageFactory.initElements(driver, LeavesPage.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void Verify_Min_Consecutive_allowed_Single_Application(Map<String, String> data) throws Exception {
        Assert.assertTrue(loginpage.empLoginToApplication(), "Unable to do employee login");
        Assert.assertTrue(leavePage.navigateToReqestTask(), "Error in navigating to Request Task Page");
        Assert.assertTrue(leavePage.revokeRequests(), "Error in revoking requests");
        Assert.assertTrue(leavePage.navigateToLeavePage(), "Unable to navigate to Leave Page");
        Assert.assertTrue(leavePage.setFromAndToDates(Integer.parseInt(data.get("MinConsecutiveDays"))), "Unable to set Leave Dates");
        Assert.assertTrue(leavePage.applyLeave(), "Unable to apply Leave");

        if (data.get("properties").trim().contains("cant apply leave"))
            Assert.assertTrue(leavePage.errorMessage().equalsIgnoreCase(data.get("ExpectedMessage").trim()), "Expected and Actual Error Message are Different");

        else if (data.get("properties").trim().contains("can apply leave"))
            Assert.assertFalse(leavePage.isErrorMessageDisplayed(), "Error Message Model is Displayed");
    }
}
