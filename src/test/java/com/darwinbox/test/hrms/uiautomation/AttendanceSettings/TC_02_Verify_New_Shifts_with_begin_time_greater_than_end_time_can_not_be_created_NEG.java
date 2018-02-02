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
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.AttendanceSettingsShiftPage;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;

import java.util.Map;


public class TC_02_Verify_New_Shifts_with_begin_time_greater_than_end_time_can_not_be_created_NEG extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
	CommonSettingsPage commonSettings;
	AttendanceSettingsShiftPage attShiftSettings;
	RightMenuOptionsPage rightMenuOption;
	
    private static final Logger log = Logger.getLogger(TC_02_Verify_New_Shifts_with_begin_time_greater_than_end_time_can_not_be_created_NEG.class);
    
	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Attendance_Settings_TestData.xlsx", "TC_02");
	}
	
	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
		attShiftSettings = PageFactory.initElements(driver, AttendanceSettingsShiftPage.class);
		rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
	}
		
	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class ,groups = "Attendance_Settings")
	public void Verify_New_Shifts_with_begin_time_greater_than_end_time_can_not_be_created_NEG(Map<String,String> data) throws Exception {

		Assert.assertTrue(loginpage.loginToApplication(),"User Loggin to Application as Admin");
		Assert.assertTrue(homepage.clickUserProfileIcon(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickAttendance(), "Click on Attendance link");
		Assert.assertTrue(attShiftSettings.clickShifts(), "Click on Shifts link");
		Assert.assertTrue(attShiftSettings.clickCreateShiftBtn(), "Click on Create Shifts button");
		Assert.assertTrue(attShiftSettings.createShift(), "Create Valid Shift");
		Assert.assertTrue(attShiftSettings.verifyShiftBeginTimeGreaterThanEndTimeErrorMsg(),
				"Begin Time Greater Than End Time Error Msg is displayed correctly");
	} 
}

