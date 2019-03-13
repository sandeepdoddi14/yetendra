package com.darwinbox.leaves.LeaveSettings.Over_Utilization;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class Create_LeavePolicy extends TestBase {
    private static final Logger log = Logger.getLogger(Create_LeavePolicy.class);
    public HomePage homepage;
    public LoginPage loginpage;
    public WaitHelper objWaitHelper;
    public CommonSettingsPage commonSettings;
    public LeavesSettingsPage leaveSettings;
    public CreateAndManageLeavePoliciesPage createManageLeaves;
    public RightMenuOptionsPage rightMenuOption;
    public LeavesAction leavesAction;
    public UtilityHelper objUtil;
    public CommonAction commonAction;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass( ).getName( ));
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
        objUtil = PageFactory.initElements(driver, UtilityHelper.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);
    }
    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Create_Leaves_for_EmpApplyMoreThanAvailableLeaveBalance(Map<String, String> data) throws Exception {
        Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile( ), "Leave scenario is set successfully");
        Assert.assertTrue(loginpage.loginToApplication(), "User Loggin to Application as Admin");
        Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
        Assert.assertTrue(leavesAction.navigateToSettings_Leaves( ), "Navigated to Leaves link");

        if(leavesAction.checkIfLeaveTypeIsPresent())
            Assert.assertTrue(leavesAction.editLeaveType(),"Edit Leave Type is Clicked Successfully");
        else
        Assert.assertTrue(leaveSettings.clickCreateLeavePolicies(), "Clicked on Create Leave Policies link");

        Assert.assertTrue(createManageLeaves.selectGroupCompanyDropdown("Working Days (DO NOT TOUCH)"), "Select Group Company");

        leavesAction.setMandatoryLeaveScenarios();

        leavesAction.setCreditOnAccrualBasis();

        leavesAction.setOverutilizationScenario();
        createManageLeaves.clickCreateLeavePolicySaveButton();


  }
}
