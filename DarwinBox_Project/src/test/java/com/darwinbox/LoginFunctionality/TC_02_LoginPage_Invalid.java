package com.darwinbox.LoginFunctionality;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dawinbox.common.utils.TestEngine;
import com.dbox.commonPage.CommonPageObject;


public class TC_02_LoginPage_Invalid extends TestEngine {

	@BeforeClass
	public void Initilazation() {
		setExcelFileName("LoginFunctionality");
		setExcelSheetName("TC_02");
	}

	@Test(dataProvider = "TestRuns", priority = 1, groups = "Login_Tests")
	public void Verify_Invalid_Login_To_Application(String input, String output) {
		try {
			if(excelInput("RunMode").equalsIgnoreCase("Yes")) {
			WebDriver driver = getWebDriver();
			CommonPageObject objcommon = new CommonPageObject(driver);
			Assert.assertTrue(objcommon.launchApp(), "Exception while launching Application");
			Assert.assertTrue(objcommon.inputUsername(excelInput("UserName")), "Exception while inserting username");
			Assert.assertTrue(objcommon.inputPassword(excelInput("PassWord")), "Exception while inserting password");
			Assert.assertTrue(objcommon.checkInvalidSignIn(excelInput("ErrorMessage")),
					"Exception while clicking SignIn");
			}else {
				throw new SkipException("Run Mode set to 'No'");
			}
		} catch (Exception e) {
			throw new RuntimeException("Test Case Failed");
		}
	}
}
