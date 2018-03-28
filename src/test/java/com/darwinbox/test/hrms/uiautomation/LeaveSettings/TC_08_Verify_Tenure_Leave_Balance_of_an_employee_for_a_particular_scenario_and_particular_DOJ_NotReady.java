package com.darwinbox.test.hrms.uiautomation.LeaveSettings;

import java.time.LocalDate;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.darwinbox.test.hrms.uiautomation.Common.Action.CommonActionClass;
import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Leaves.Action.LeavesActionClass;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CreateAndManageLeavePoliciesPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.LeavesSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.Utility.UtilityHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;

public class TC_08_Verify_Tenure_Leave_Balance_of_an_employee_for_a_particular_scenario_and_particular_DOJ_NotReady extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
	CommonSettingsPage commonSettings;
	LeavesSettingsPage leaveSettings;
	CreateAndManageLeavePoliciesPage createManageLeaves;
	RightMenuOptionsPage rightMenuOption;
	LeavesActionClass leavesAction;
	UtilityHelper objUtil;
	CommonActionClass commonAction;
	
	private static final Logger log = Logger.getLogger(TC_08_Verify_Tenure_Leave_Balance_of_an_employee_for_a_particular_scenario_and_particular_DOJ_NotReady.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Leave_Settings_TestData.xlsx", "TC_03");
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
		leavesAction = PageFactory.initElements(driver, LeavesActionClass.class);
		objUtil = PageFactory.initElements(driver, UtilityHelper.class);
		commonAction = PageFactory.initElements(driver, CommonActionClass.class);
	}

	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
	public void Verify_Leave_Balance_is_calculated_correctly(Map<String,String> data) throws Exception {

		Assert.assertTrue(leavesAction.setLeaveScenarioFromPropertyFile(), "Leave scenario is set successfully");
		Assert.assertTrue(leavesAction.setEmployeeID(objUtil.getProperty("config","Employee.id")), "Employee ID is set successfully to test");
		Assert.assertTrue(loginpage.loginToApplication(), "User Loggin to Application as Admin");
		Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click on Settings link");		
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickLeaves(), "Click on Leaves link");		
		Assert.assertTrue(leavesAction.deleteLeaveTypeIfAlreadyPresent(), "Leave Type is presnt are deleted successfully");
		Assert.assertTrue(leaveSettings.clickCreateLeavePolicies(), "Clicked on Create Leave Policies link");
		Assert.assertTrue(createManageLeaves.selectGroupCompanyDropdown(0), "Select Group Company");				
		Assert.assertTrue(leavesAction.createLeaveTypeWithMentionedScenarios(), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.setCreditOnTenureBasisLeaveScenario(), "Leaves type with mentioned scenarios is created");						
		Assert.assertTrue(createManageLeaves.clickCreateLeavePolicySaveButton(), "Click on Create Leave Policy Save Button");
		Assert.assertTrue(leavesAction.verifyEmployeeTenureBasedLeaveBalanceForAParticularDOJ("2017-02-29", LocalDate.now().toString()), "Leave Balance for whole leave cycle calculated successfully") ;
	}
}
