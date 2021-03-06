package com.darwinbox.leaves.CarryForward;

import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.dashboard.pageObjectRepo.settings.CommonSettingsPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class Create_Carry_Forward_Leave_Data extends TestBase {

    private static final Logger log = Logger.getLogger(Create_Carry_Forward_Leave_Data.class);
    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    CommonSettingsPage commonSettings;
    LeavesSettingsPage leaveSettings;
    CreateAndManageLeavePoliciesPage createManageLeaves;
    RightMenuOptionsPage rightMenuOption;
    LeavesAction leavesAction;
    CommonAction commonAction;
    UtilityHelper objUtil;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
        leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
        createManageLeaves = PageFactory.initElements(driver, CreateAndManageLeavePoliciesPage.class);
        rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);
        objUtil = PageFactory.initElements(driver, UtilityHelper.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Create_Carry_Forward_Leave_Data(Map<String, String> data) throws Exception {

        Assert.assertTrue(leavesAction.setLeaveType(), "Leave Type is set successfully");
        Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile(), "Leave scenario is set successfully");
        Assert.assertTrue(leavesAction.setEmployeeID(UtilityHelper.getProperty("config", "Employee.id")), "Employee ID is set successfully to test");
        Assert.assertTrue(loginpage.loginToApplication(), "User Loggin to Application as Admin");
        Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
        Assert.assertTrue(leavesAction.navigateToSettings_Leaves(), "Navigated to Leaves link");
        Assert.assertTrue(leavesAction.deleteLeaveTypeIfAlreadyPresent(), "Leave Type is presnt are deleted successfully");
        Assert.assertTrue(leaveSettings.clickCreateLeavePolicies(), "Clicked on Create Leave Policies link");
        Assert.assertTrue(createManageLeaves.selectGroupCompanyDropdown(1), "Select Group Company");
        Assert.assertTrue(leavesAction.createLeaveTypeWithMentionedScenarios(), "Leaves type with mentioned scenarios is created");
        Assert.assertTrue(leavesAction.setCarryForwardScenario(), "Set carry forward scemario successfully");
        Assert.assertTrue(createManageLeaves.clickCreateLeavePolicySaveButton(), "Click on Create Leave Policy Save Button");
    }

    @AfterMethod
    public void clearTestData(){
        Assert.assertTrue(leavesAction.deleteLeaveTypeIfAlreadyPresent(), "Leave Type is presnt are deleted successfully");
    }
}
