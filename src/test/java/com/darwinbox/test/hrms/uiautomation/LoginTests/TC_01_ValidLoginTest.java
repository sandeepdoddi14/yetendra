package com.darwinbox.test.hrms.uiautomation.LoginTests;

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
import com.darwinbox.test.hrms.uiautomation.configreader.ObjectRepo;
import com.darwinbox.test.hrms.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;

public class TC_01_ValidLoginTest extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
    RightMenuOptionsPage rightMenuOption;
    private static final Logger log = Logger.getLogger(TC_01_ValidLoginTest.class);
    
	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Login_TestData.xlsx", "TC_01");
	}
	
	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		rightMenuOption=PageFactory.initElements(driver, RightMenuOptionsPage.class);
	}
	
	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class ,groups = "Login_Tests")
	public void Login_To_Application(String input, String output) throws Exception {
		
		if(excel.getTestInput("RunMode").equalsIgnoreCase("No")) {
			throw new SkipException("user marked this record as no run");
		}		
		try {			
			Assert.assertTrue(launchApplication(), "Exception while launching Application");
			Assert.assertTrue(loginpage.EnterUsername(ObjectRepo.reader.getAdminUserName()),
					" Enter Username Successfully.");
			Assert.assertTrue(loginpage.EnterPassword(ObjectRepo.reader.getAdminPassword()),
					"Enter Password Successfully");
			Assert.assertTrue(loginpage.clickSignIn(), "Exception while clicking SignIn");
			Assert.assertTrue(homepage.verifyProfileIcontDisplay(), "Exception fininding User Profile Icon");
			Assert.assertTrue(homepage.clickUserProfileIcon(),"Exception cliking on UserProfileIcon");
			Assert.assertTrue(rightMenuOption.clickSidebarLogout(),"Logout from application successfully");

		}
		catch (Exception e) {
			Reporter("Exception occured: " + e.getMessage(),"Fail");
			throw (e);
		}
	}

	

	
}
