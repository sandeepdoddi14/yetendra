package com.darwinbox.test.hrms.uiautomation.AttendanceSettings;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.AttendanceSettingsPolicyPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;


public class TC_06_Perform_Mandatory_Checks_in_Policy_Creation extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
	CommonSettingsPage commonSettings;
	AttendanceSettingsPolicyPage attPolicySettings;
	RightMenuOptionsPage rightMenuOption;

	private static final Logger log = Logger
			.getLogger(TC_06_Perform_Mandatory_Checks_in_Policy_Creation.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Attendance_Settings_TestData.xlsx", "TC_06");
	}
	
	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
		attPolicySettings = PageFactory.initElements(driver, AttendanceSettingsPolicyPage.class);
		rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
	}

	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Attendance_Settings")
	public void Perform_Mandatory_Checks_in_Policy_Creation(String input,String output) throws Exception {
		if (excel.getTestInput("RunMode").equalsIgnoreCase("No")) {
			throw new SkipException("user marked this record as no run");
		}
		
		Assert.assertTrue(launchApplication(), "Launch Application");
		Assert.assertTrue(loginpage.loginToApplication(), "User Loggin to Application as Admin");
		Assert.assertTrue(homepage.clickUserProfileIcon(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickAttendance(), "Click on Attendance link");
		Assert.assertTrue(attPolicySettings.clickPoliciesLink(),"Click on Shifts link");
//			Assert.assertTrue(objpol.clickCreatePolicyBtn(),"Click on Create Shifts button");
//			Assert.assertTrue(objatd.createShift(),"Create Valid Shift");
//			Assert.assertTrue(objatd.searchShiftName(),"Search for Shift Name created");
//			Assert.assertTrue(objatd.checkShiftDetails(),"Shift details created successfully");
//			Assert.assertTrue(objatd.editShiftDetails(),"Shift details edited successfully");			
//			Assert.assertTrue(objatd.deleteShift(),"Shift details deleted successfully");
		
	}
}


