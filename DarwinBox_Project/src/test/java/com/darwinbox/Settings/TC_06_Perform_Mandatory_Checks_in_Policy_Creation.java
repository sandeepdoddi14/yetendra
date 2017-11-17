package com.darwinbox.Settings;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dawinbox.common.utils.TestEngine;
import com.dbox.commonPage.CommonPageObject;
import com.dbox.settings.Settings_Attendance_Policy;
import com.dbox.settings.Settings_Attendance_Shift;

public class TC_06_Perform_Mandatory_Checks_in_Policy_Creation extends TestEngine {

	@BeforeClass
	public void Initilazation() {
		setExcelFileName("Settings");
		setExcelSheetName("TC_06");
	}

	@Test(dataProvider = "TestRuns", priority = 1, groups = "Login_Tests")
	public void Perform_Mandatory_Checks_in_Policy_Creation(String input,String output) throws Exception {
			WebDriver driver = getWebDriver();
			CommonPageObject objcomm = new CommonPageObject(driver);
			Settings_Attendance_Policy objpol = new Settings_Attendance_Policy(driver);
			Assert.assertTrue(objcomm.launchApp(), "Launch Application");
			Assert.assertTrue(objcomm.loginToApplication(config("login","admin.username"), config("login","admin.password")),
					"User Loggin to Application as Admin");
			Assert.assertTrue(objcomm.clickUserProfileIcon(),"Click User Profile Icon");
			Assert.assertTrue(objcomm.clickSidebarSwitchToAdmin(),"Click on Switch to Admin");
			Assert.assertTrue(objcomm.clickUserProfileIcon(),"Click User Profile Icon");
			Assert.assertTrue(objcomm.clickSidebarSettings(),"Click on Settings link");
			Assert.assertTrue(objpol.clickAttendance(),"Click on Attendance link");
			Assert.assertTrue(objpol.clickPoliciesLink(),"Click on Shifts link");
//			Assert.assertTrue(objatd.clickCreateShiftBtn(),"Click on Create Shifts button");
//			Assert.assertTrue(objatd.createShift(),"Create Valid Shift");
//			Assert.assertTrue(objatd.searchShiftName(),"Search for Shift Name created");
//			Assert.assertTrue(objatd.checkShiftDetails(),"Shift details created successfully");
//			Assert.assertTrue(objatd.editShiftDetails(),"Shift details edited successfully");			
//			Assert.assertTrue(objatd.deleteShift(),"Shift details deleted successfully");
		} 
	}


