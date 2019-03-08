package com.darwinbox.leaves.LeaveBalance;

import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.dashboard.pageObjectRepo.settings.CommonSettingsPage;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.pageObjectRepo.settings.CreateAndManageLeavePoliciesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class TC_02f_Delete_all_Leave_Type extends TestBase{
 
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

private static final Logger log = Logger.getLogger(TC_02f_Delete_all_Leave_Type.class);

@BeforeClass
public void setup() throws Exception {
	ms.getDataFromMasterSheet(this.getClass().getName());
/*	ExcelReader.setFilenameAndSheetName("Leave_Scenarios_Without_Creation.xlsx", "All_without_Creation");
	WriteResultToExcel = UtilityHelper.getProperty("config", "Write.Result.to.excel");
	if(WriteResultToExcel.equalsIgnoreCase("Yes")) {
		ExcelWriter.copyExportFileToResultsDir();
	}*/
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

@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class ,groups = "Leave_Settings")
public void Verify_Admin_is_able_to_create_New_Shifts(Map<String,String> data) throws Exception {

		Assert.assertTrue(loginpage.loginToApplication(),"User Loggin to Application as Admin");
		Assert.assertTrue(commonAction.changeApplicationAccessMode("Admin"), "Application access changed to Admin mode");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click on Settings link");		
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickLeaves(), "Click on Leaves link");	
		int i = 0;
		while(i<600) {
		Assert.assertTrue(leavesAction.deleteAllLeaveType(), "Leave Type is presnt are deleted successfully");
		i++;
		}
	}
}
