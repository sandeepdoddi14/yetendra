package com.darwinbox.Settings;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.dawinbox.common.utils.TestEngine;
import com.dbox.commonPage.CommonPageObject;
import com.dbox.settings.Settings_Attendance_Shift;

public class TC_02_Verify_New_Shifts_with_begin_time_greater_than_end_time_can_not_be_created_NEG
		extends TestEngine {

	@BeforeClass
	public void Initilazation() {
		setExcelFileName("Settings");
		setExcelSheetName("TC_02");
	}

	@Test(dataProvider = "TestRuns", priority = 1, groups = "Login_Tests")
	public void Verify_New_Shifts_with_begin_time_greater_than_end_time_can_not_be_created_NEG(String input,
			String output) throws Exception {
		WebDriver driver = getWebDriver();
		CommonPageObject objcomm = new CommonPageObject(driver);
		Settings_Attendance_Shift objatd = new Settings_Attendance_Shift(driver);
		Assert.assertTrue(objcomm.launchApp(), "Launch Application");
		Assert.assertTrue(
				objcomm.loginToApplication(config("login", "admin.username"), config("login", "admin.password")),
				"User Loggin to Application as Admin");
		Assert.assertTrue(objcomm.clickUserProfileIcon(), "Click User Profile Icon");
		Assert.assertTrue(objcomm.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
		Assert.assertTrue(objcomm.clickUserProfileIconAdmin(), "Click User Profile Icon");
		Assert.assertTrue(objcomm.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(objatd.clickAttendance(), "Click on Attendance link");
		Assert.assertTrue(objatd.clickShifts(), "Click on Shifts link");
		Assert.assertTrue(objatd.clickCreateShiftBtn(), "Click on Create Shifts button");
		Assert.assertTrue(objatd.createShift(), "Create Valid Shift");
		Assert.assertTrue(objatd.verifyShiftBeginTimeGreaterThanEndTimeErrorMsg(),
				"Begin Time Greater Than End Time Error Msg is displayed correctly");
	}
}
