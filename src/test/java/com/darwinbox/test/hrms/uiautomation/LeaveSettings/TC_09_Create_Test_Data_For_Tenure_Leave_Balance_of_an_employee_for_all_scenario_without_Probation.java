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

public class TC_09_Create_Test_Data_For_Tenure_Leave_Balance_of_an_employee_for_all_scenario_without_Probation extends TestBase {

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
	
	private static final Logger log = Logger.getLogger(TC_09_Create_Test_Data_For_Tenure_Leave_Balance_of_an_employee_for_all_scenario_without_Probation.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Tenure_Leave_Scenarios.xlsx", "All_Without_Probation");
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
		objUtil = PageFactory.initElements(driver, UtilityHelper.class);
		commonAction = PageFactory.initElements(driver, CommonActionClass.class);
	}

	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
	public void Verify_Leave_Balance_is_calculated_correctly(Map<String,String> data) throws Exception {
		
		Assert.assertTrue(leavesAction.setLeaveType(), "Leave Type is set successfully");				
		Assert.assertTrue(leavesAction.setLeaveScenarioFromExcelFile(), "Leave scenario is set successfully");			
		Assert.assertTrue(loginpage.loginToApplication(),"User Loggin to Application as Admin");
		Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click on Settings link");		
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickLeaves(), "Click on Leaves link");		
		Assert.assertTrue(leaveSettings.clickCreateLeavePolicies(), "Clicked on Create Leave Policies link");
		Assert.assertTrue(createManageLeaves.selectGroupCompanyDropdown(1), "Select Group Company");
		Assert.assertTrue(createManageLeaves.insertDescription(data.get("Test_Description")), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.insertMaxLeaveAllowedPerYear(), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.insertLeaveType(), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.insertMaxLeaveAllowedPerYear(), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.setLeaveProbationPeriod(), "Leaves type with mentioned scenarios is created");				
		Assert.assertTrue(leavesAction.setLeaveCycle(), "Leaves type with mentioned scenarios is created");				
		Assert.assertTrue(createManageLeaves.insertRestrictionDepartmentEmployeeTypeLocationElasticSearch("Part Time"), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.setCreditOnProRataBasis(), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.setCreditOnAccrualBasis(), "Leaves type with mentioned scenarios is created");		
		Assert.assertTrue(leavesAction.setCreditOnTenureBasisLeaveScenario(), "Leaves type with mentioned scenarios is created");						
		Assert.assertTrue(createManageLeaves.clickCreateLeavePolicySaveButton(), "Click on Create Leave Policy Save Button");
		Assert.assertTrue(leavesAction.getLeaveTypeIdAndWriteToExcel("Leave_Type2_Repo"), "Leave Balance for whole leave cycle calculated successfully") ;
	}
}
