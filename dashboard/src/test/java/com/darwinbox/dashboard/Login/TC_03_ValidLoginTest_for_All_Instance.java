package com.darwinbox.dashboard.Login;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.framework.uiautomation.configreader.ObjectRepo;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class TC_03_ValidLoginTest_for_All_Instance extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
    RightMenuOptionsPage rightMenuOption;
	private static final Logger log = Logger.getLogger(TC_03_ValidLoginTest_for_All_Instance.class);
    
	@BeforeClass
	public void setup() throws Exception {
		ms.getDataFromMasterSheet(this.getClass().getName());
	}
	
	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		rightMenuOption=PageFactory.initElements(driver, RightMenuOptionsPage.class);
	}
	
	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class ,groups = "Login_Tests")
	public void Login_To_Application(Map<String,String> data) throws Exception {
		
		try {
			
			Assert.assertTrue(loginpage.EnterUsername(ObjectRepo.reader.getAdminUserName()),
					" Enter Username Successfully.");
			Assert.assertTrue(loginpage.EnterPassword(ObjectRepo.reader.getAdminPassword()),
					"Enter Password Successfully");
			Assert.assertTrue(loginpage.clickSignIn(), "Exception while clicking SignIn");
			Assert.assertTrue(homepage.verifyProfileIcontDisplay(), "Exception fininding User Profile Icon");
			Assert.assertTrue(homepage.clickUserProfileIcon(),"Exception cliking on UserProfileIcon");

		}
		catch (Exception e) {
			Reporter("Exception occured: " + e.getMessage(),"Fail");
			throw (e);
		}
	}
}
