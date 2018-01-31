package com.darwinbox.test.hrms.uiautomation.LoginTests;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class TC_02_InvalidLoginTest extends TestBase {

	LoginPage loginpage;
	BrowserHelper objBrowser;
	GenericHelper objHelper;
 
	private static final Logger log = Logger
			.getLogger(TC_02_InvalidLoginTest.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Login_TestData.xlsx", "TC_02");
	}
	
	@BeforeMethod
	public void initializeObjects() {
		objBrowser = PageFactory.initElements(driver, BrowserHelper.class);
		objHelper = PageFactory.initElements(driver, GenericHelper.class);
		loginpage = PageFactory.initElements(driver, LoginPage.class);
	}
	
	@Test(dataProvider = "TestRuns", dataProviderClass=TestDataProvider.class, groups = "Login_Tests")
	public void Verify_Invalid_Login_To_Application(String input, String output) throws Exception {
		
		if(excel.getTestInput("RunMode").equalsIgnoreCase("No")) {
			throw new SkipException("user marked this record as no run");
		}
		try {
			Assert.assertTrue(launchApplication(), "Exception while launching Application");
			Assert.assertTrue(loginpage.EnterUsername(excel.getTestInput("UserName")), "Exception while inserting username");
			Assert.assertTrue(loginpage.EnterPassword(excel.getTestInput("PassWord")), "Exception while inserting password");
			Assert.assertTrue(loginpage.checkInvalidSignIn(excel.getTestInput("ErrorMessage")),
					"Exception while clicking SignIn");
			}
		   catch (Exception e) {
			throw new RuntimeException("Test Case Failed");
		}
	}	
}
