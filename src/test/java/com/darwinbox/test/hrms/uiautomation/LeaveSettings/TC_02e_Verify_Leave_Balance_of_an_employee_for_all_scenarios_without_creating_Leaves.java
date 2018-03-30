package com.darwinbox.test.hrms.uiautomation.LeaveSettings;

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
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelWriter;
import com.darwinbox.test.hrms.uiautomation.Utility.UtilityHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;

public class TC_02e_Verify_Leave_Balance_of_an_employee_for_all_scenarios_without_creating_Leaves extends TestBase{
 
HomePage homepage;
LoginPage loginpage;
WaitHelper objWaitHelper;
CommonSettingsPage commonSettings;
LeavesSettingsPage leaveSettings;
CreateAndManageLeavePoliciesPage createManageLeaves;
RightMenuOptionsPage rightMenuOption;
LeavesActionClass leavesAction;
CommonActionClass commonAction;
UtilityHelper objUtil;

private static final Logger log = Logger.getLogger(TC_02e_Verify_Leave_Balance_of_an_employee_for_all_scenarios_without_creating_Leaves.class);

@BeforeClass
public void setup() throws Exception {
	ExcelReader.setFilenameAndSheetName("Leave_Scenarios.xlsx", "All_without_Creation");
	WriteResultToExcel = UtilityHelper.getProperty("config", "Write.Result.to.excel");
	if(WriteResultToExcel.equalsIgnoreCase("Yes")) {
		ExcelWriter.copyExportFileToResultsDir();					
	}
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
	commonAction = PageFactory.initElements(driver, CommonActionClass.class);
	objUtil = PageFactory.initElements(driver, UtilityHelper.class);
}

@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class ,groups = "Leave_Settings")
public void Verify_Admin_is_able_to_create_New_Shifts(Map<String,String> data) throws Exception {

	Assert.assertTrue(leavesAction.setLeaveType(), "Leave Type is set successfully");				
		Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile(), "Leave scenario is set successfully");			
		Assert.assertTrue(leavesAction.setEmployeeID("WIP002"), "Employee ID is set successfully to test");
		Assert.assertTrue(loginpage.loginToApplication(),"User Loggin to Application as Admin");
		Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
		Assert.assertTrue(leavesAction.verifyEmployeeLeaveBalanceForWholeLeaveCycleForFourEdgeDays(), "Leave Balance for whole leave cycle calculated successfully") ;

	}
}
