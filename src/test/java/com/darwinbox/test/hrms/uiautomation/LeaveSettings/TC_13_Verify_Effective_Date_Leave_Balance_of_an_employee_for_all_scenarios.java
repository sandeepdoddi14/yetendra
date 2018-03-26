package com.darwinbox.test.hrms.uiautomation.LeaveSettings;

import java.time.LocalDate;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
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

public class TC_13_Verify_Effective_Date_Leave_Balance_of_an_employee_for_all_scenarios extends TestBase {

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
	private static int i = 1;
	
	private static final Logger log = Logger.getLogger(TC_13_Verify_Effective_Date_Leave_Balance_of_an_employee_for_all_scenarios.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("EffectiveDate.xlsx", "Test");
	}

	@BeforeTest
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
		
		Assert.assertTrue(loginpage.loginToApplication(), "User Loggin to Application as Admin");
		Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click on Settings link");		
		Assert.assertTrue(leavesAction.setEmployeeID("WIP001"), "Employee ID is set successfully to test");		
		
		
		leavesAction.getAllEmployeeTypes();
		leavesAction.getEmployeeData();
		Assert.assertTrue(leavesAction.changeEmployeeType(), "Click on Settings link");	
		int tem = currentData;
		Assert.assertTrue(leavesAction.setLeaveType1(), "Leave scenario is set successfully");		
			i = 0;
		while (i < dataCounter) {
			currentData = i;
			try {
			this.data = dataItem.get(currentData);
			wrapperMethod();
			}catch(Exception e) {		
			}
			i++;
		}
		currentData = tem;
		Assert.assertTrue(leavesAction.changeEmployeeTypeBackToOriginal(), "Click on Settings link");
		Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile(), "Leave scenario is set successfully");		
		leavesAction.verifyEffectiveDateLeaveBalanceForParticularDOJ(LocalDate.now().toString());							
		
	}
	
	public void wrapperMethod() {
		try {
		Assert.assertTrue(leavesAction.setLeaveType2(), "Leave scenario is set successfully");		
		Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile(), "Leave scenario is set successfully");		
		leavesAction.verifyEffectiveDateLeaveBalanceForParticularDOJ(LocalDate.now().toString());			
		}catch(Exception e) {			
			}
		}
}
