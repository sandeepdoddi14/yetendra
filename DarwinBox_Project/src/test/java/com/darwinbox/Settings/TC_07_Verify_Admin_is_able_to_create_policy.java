package com.darwinbox.Settings;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dawinbox.common.utils.TestEngine;
import com.dbox.commonPage.CommonPageObject;
import com.dbox.settings.Settings_Attendance_Policy;

public class TC_07_Verify_Admin_is_able_to_create_policy extends TestEngine {

	@BeforeClass
	public void Initilazation() {
		setExcelFileName("Settings");
		setExcelSheetName("TC_07");
	}

	@Test(dataProvider = "TestRuns", priority = 1, groups = "Login_Tests")
	public void Verify_Admin_is_able_to_create_policy(String input,String output) throws Exception {
		if(excelInput("RunMode").equalsIgnoreCase("Yes")) {
			WebDriver driver = getWebDriver();
			CommonPageObject objcomm = new CommonPageObject(driver);
			Settings_Attendance_Policy objpol = new Settings_Attendance_Policy(driver);
			Assert.assertTrue(objcomm.launchApp(), "Launch Application");
			Assert.assertTrue(objcomm.loginToApplication(config("login","admin.username"), config("login","admin.password")),
					"User Loggin to Application as Admin");
			Assert.assertTrue(objcomm.clickUserProfileIcon(),"Click User Profile Icon");
			Assert.assertTrue(objcomm.clickSidebarSwitchToAdmin(),"Click on Switch to Admin");
			Assert.assertTrue(objcomm.clickUserProfileIconAdmin(),"Click User Profile Icon");
			Assert.assertTrue(objcomm.clickSidebarSettings(),"Click on Settings link");
			Assert.assertTrue(objpol.clickAttendance(),"Click on Attendance link");
			Assert.assertTrue(objpol.clickPoliciesLink(),"Click on Policies link");
			Assert.assertTrue(objpol.clickCreatePolicyButton(),"Click on Create Policy button");
			Assert.assertTrue(objpol.insertPolicyName(excelInput("Policy Name_TextBox")),"Insert Policy Name.");
			Assert.assertTrue(objpol.enableOrDisableLeaveDeductionPolicy(excelInput("Leave Deduction Policy_Dropdown")),"Select Leave deduction policy");
			Assert.assertTrue(objpol.enableOrDisableLateMarkPolicy(excelInput("Late Mark Policy_Dropdown")),"Select Late Mark policy");
			Assert.assertTrue(objpol.enableOrDisableWorkDurationPolicy(excelInput("Work Duration Policy_Dropdown")),"Select Work Duration policy");
			Assert.assertTrue(objpol.enableOrDisableEarlyMarkPolicy(excelInput("Early Mark Policy_Dropdown")),"Select Early Mark Duration policy");
			Assert.assertTrue(objpol.clickSaveButton(),"Save button clicked successfully");
			Assert.assertTrue(objpol.searchPolicyName(),"Search for Policy Name created");
			Assert.assertTrue(objpol.checkPolicyDetails(),"Policy details created successfully");
//			Assert.assertTrue(objatd.editShiftDetails(),"Shift details edited successfully");			
			Assert.assertTrue(objpol.deletePolicy(),"Policy details deleted successfully");
		} 
	}
	}


