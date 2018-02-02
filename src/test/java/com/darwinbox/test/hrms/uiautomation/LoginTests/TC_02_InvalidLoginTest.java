package com.darwinbox.test.hrms.uiautomation.LoginTests;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import java.util.Map;

public class TC_02_InvalidLoginTest extends TestBase {

	LoginPage loginpage;

	private static final Logger log = Logger.getLogger(TC_02_InvalidLoginTest.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Login_TestData.xlsx", "TC_02");
	}
	
	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
	}
	
	@Test(dataProvider = "TestRuns", dataProviderClass=TestDataProvider.class, groups = "Login_Tests")
	public void Verify_Invalid_Login_To_Application(Map<String,String> data) throws Exception {

		try {
			Assert.assertTrue(loginpage.EnterUsername(data.get("UserName")), "Exception while inserting username");
			Assert.assertTrue(loginpage.EnterPassword(data.get("Password")), "Exception while inserting password");
			Assert.assertTrue(loginpage.checkInvalidSignIn(data.get("ErrorMessage")),"Exception while clicking SignIn");
		}
		   catch (Exception we) {
			throw new RuntimeException("Test Case Failed");
		}
	}	
}
