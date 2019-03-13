package com.darwinbox.leaves.LeaveSettings.FieldSettings;

import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.dashboard.pageObjectRepo.settings.CommonSettingsPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.leaves.MultipleAllotment.Create_Leaves_for_Multiple_Allotment_Leave_Transfer;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class Create_LeavePolicy_for_FieldSettings extends TestBase {
    private static final Logger log = Logger.getLogger(Create_LeavePolicy_for_FieldSettings.class);
    Create_Leaves_for_Multiple_Allotment_Leave_Transfer createLeavePolicy;


    @BeforeMethod
    public void initializeObjects() {
        createLeavePolicy = new Create_Leaves_for_Multiple_Allotment_Leave_Transfer();
        createLeavePolicy.loginpage = PageFactory.initElements(driver, LoginPage.class);
        createLeavePolicy.objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        createLeavePolicy.homepage = PageFactory.initElements(driver, HomePage.class);
        createLeavePolicy.commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
        createLeavePolicy.leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
        createLeavePolicy.createManageLeaves = PageFactory.initElements(driver, CreateAndManageLeavePoliciesPage.class);
        createLeavePolicy.rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
        createLeavePolicy.leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        createLeavePolicy.objUtil = PageFactory.initElements(driver, UtilityHelper.class);
        createLeavePolicy.commonAction = PageFactory.initElements(driver, CommonAction.class);
    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Create_Leaves_for_Multiple_Allotment_Leave_Transfer(Map<String, String> data) throws Exception {
        createLeavePolicy.Create_Leaves_for_Multiple_Allotment_Leave_Transfer(data);
    }

}
