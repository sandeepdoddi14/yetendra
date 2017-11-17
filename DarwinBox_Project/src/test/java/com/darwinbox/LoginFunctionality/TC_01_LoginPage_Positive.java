package com.darwinbox.LoginFunctionality;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dawinbox.common.utils.TestEngine;
import com.dbox.commonPage.CommonPageObject;


public class TC_01_LoginPage_Positive extends TestEngine {

	@BeforeClass
	public void Initilazation() {
		setExcelFileName("LoginFunctionality");
		setExcelSheetName("TC_01");
	}

	@Test(dataProvider = "TestRuns",groups = "Login_Tests")
	public void Login_To_Application(String input, String output) throws Exception {
		try {
			if(excelInput("RunMode").equalsIgnoreCase("Yes")) {
			WebDriver driver = getWebDriver();
			CommonPageObject objcommon = new CommonPageObject(driver);
			Assert.assertTrue(objcommon.launchApp(), "Exception while launching Application");
			Assert.assertTrue(objcommon.inputUsername(excelInput("UserName")),
					"Exception while inserting username");
			Assert.assertTrue(objcommon.inputPassword(excelInput("PassWord")),
					"Exception while inserting password");
			Assert.assertTrue(objcommon.clickSignIn(), "Exception while clicking SignIn");
		}
		}catch (Exception e) {
			Reporter("Exception occured: " + e.getMessage(),"Fail");
			throw (e);
		}
	}
}
