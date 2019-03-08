package com.darwinbox.leaves.LeaveBalance;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.dashboard.pageObjectRepo.settings.CommonSettingsPage;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class TC_01_Verify_Admin_can_create_Leave_Policies extends TestBase {

    private static final Logger log = Logger.getLogger(TC_01_Verify_Admin_can_create_Leave_Policies.class);
    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    CommonSettingsPage commonSettings;
    LeavesSettingsPage leaveSettings;
    CreateAndManageLeavePoliciesPage createManageLeaves;
    RightMenuOptionsPage rightMenuOption;
    CommonAction commonAction;
    LeavesAction leavesAction;

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
        commonAction = PageFactory.initElements(driver, CommonAction.class);
        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Verify_Admin_is_able_to_create_New_Shifts(Map<String, String> data1) throws Exception {

        Assert.assertTrue(loginpage.loginToApplication(data.get("@@admin"),data.get("@@password")), "User Loggin to Application as Admin");
        Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
        Assert.assertTrue(leavesAction.navigateToSettings_Leaves(), "Navigated to Leaves link");
        Assert.assertTrue(leaveSettings.clickCreateLeavePolicies(), "Clicked on Create Leave Policies link");
        Assert.assertTrue(createManageLeaves.selectGroupCompanyDropdown(0), "Select Group Company");
        Assert.assertTrue(createManageLeaves.insertMaximumLeavesAllowedPerYear(12), "Insert Maximum leaves allowed per year*");
        Assert.assertTrue(createManageLeaves.selectLeaveCycleDropdown("Financial Year"), "Leave Cycle selected");
        Assert.assertTrue(createManageLeaves.insertLeaveType("Automation Testing"), "Inserted Leave Type");
        Assert.assertTrue(createManageLeaves.clickCreditOnProRataBasisAccordion(), "Clicked on Credit On Prorata Basis Accordian");
        Assert.assertTrue(createManageLeaves.clickCreditOnProRataBasisYesRadioButton(), "Click on Yes Button");
        Assert.assertTrue(createManageLeaves.clickCalculateFromJoiningDateRadioButton(), "Click on Calculate from joining date button");
        Assert.assertTrue(createManageLeaves.clickHalfMidJoiningLeavesRadioButton(), "Disable Credit half month's leaves, if employee joins after 15th day of the month checkbox");
//		Assert.assertTrue(createManageLeaves.toggleFullMidJoiningLeavesCheckBox("Disable"), "Enable Credit full month's leaves, if employee joins after 15th day of the month checkbox");		
        Assert.assertTrue(createManageLeaves.clickCreditOnAccrualBasisAccordion(), "Click on Credit On Prorata Basis");
        Assert.assertTrue(createManageLeaves.clickCreditOnAccrualBasisYesRadioButton(), "Click on Credit On Accrual Yes radio button");
        Assert.assertTrue(createManageLeaves.clickCreateLeavePolicySaveButton(), "Click on Create Leave Policy Save Button");

    }
}
