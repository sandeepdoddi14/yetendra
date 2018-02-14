package com.darwinbox.test.hrms.uiautomation.LeaveSettings;

import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Leaves.Action.LeavesAllActions;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CreateAndManageLeavePoliciesPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.LeavesSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;

public class TC_02_Verify_Leave_Balance_of_an_employee_for_all_scenarios extends TestBase{
 
HomePage homepage;
LoginPage loginpage;
WaitHelper objWaitHelper;
CommonSettingsPage commonSettings;
LeavesSettingsPage leaveSettings;
CreateAndManageLeavePoliciesPage createManageLeaves;
RightMenuOptionsPage rightMenuOption;
LeavesAllActions leavesAllaction;


private static final Logger log = Logger.getLogger(TC_02_Verify_Leave_Balance_of_an_employee_for_all_scenarios.class);

@BeforeClass
public void setup() throws Exception {
	ExcelReader.setFilenameAndSheetName("Leave_Scenarios.xlsx", "TC_02");
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
	leavesAllaction = PageFactory.initElements(driver, LeavesAllActions.class);
}

@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class ,groups = "Attendance_Settings")
public void Verify_Admin_is_able_to_create_New_Shifts(Map<String,String> data) throws Exception {
	
		Assert.assertTrue(loginpage.loginToApplication(),"User Loggin to Application as Admin");
		Assert.assertTrue(homepage.clickUserProfileIcon(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
		
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickLeaves(), "Click on Leaves link");		
		Assert.assertTrue(leaveSettings.clickCreateLeavePolicies(), "Clicked on Create Leave Policies link");
		Assert.assertTrue(createManageLeaves.selectGroupCompanyDropdown(0), "Select Group Company");
		Assert.assertTrue(createManageLeaves.insertMaximumLeavesAllowedPerYear(12), "Insert Maximum leaves allowed per year*");
		Assert.assertTrue(createManageLeaves.selectLeaveCycleDropdown("Financial Year"), "Leave Cycle selected");
		Assert.assertTrue(createManageLeaves.insertLeaveType("Automation Testing"), "Inserted Leave Type");
		Assert.assertTrue(createManageLeaves.clickCreditOnProRataBasisAccordian(), "Clicked on Credit On Prorata Basis Accordian");
		Assert.assertTrue(createManageLeaves.clickCreditOnProRataBasisYesRadioButton(), "Click on Yes Button");
		Assert.assertTrue(createManageLeaves.clickCalculateFromJoiningDateRadioButton(), "Click on Calculate from joining date button");
		Assert.assertTrue(createManageLeaves.toggleHalfMidJoiningLeavesCheckBox("Enable"), "Disable Credit half month's leaves, if employee joins after 15th day of the month checkbox");
		Assert.assertTrue(createManageLeaves.toggleFullMidJoiningLeavesCheckBox("Disable"), "Enable Credit full month's leaves, if employee joins after 15th day of the month checkbox");		
		Assert.assertTrue(createManageLeaves.clickCreditOnAccrualBasisAccordian(), "Click on Credit On Prorata Basis");
		Assert.assertTrue(createManageLeaves.clickCreditOnAccrualBasisYesRadioButton(), "Click on Credit On Accrual Yes radio button");
		Assert.assertTrue(createManageLeaves.clickCreateLeavePolicySaveButton(), "Click on Create Leave Policy Save Button");
//		leavesAllaction.setEmployeeID("BOB434");
//		leavesAllaction.iterateEmployeeDOJ();
}
}
