package com.darwinbox.test.hrms.uiautomation.reimbursement;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Pages.reimb.ReimbSettings;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class ReimbTest extends TestBase {

	HomePage homepage;
	LoginPage loginpage;
	WaitHelper objWaitHelper;
	CommonSettingsPage commonSettings;
	ReimbSettings reimbSettings;
	RightMenuOptionsPage rightMenuOption;

	private static final Logger log = Logger.getLogger(ReimbTest.class);

	@BeforeClass
	public void setup() throws Exception {
		ExcelReader.setFilenameAndSheetName("Reimbursement_TestData.xlsx", "createUnits");
	}
	
	@BeforeMethod
	public void initializeObjects() {
		loginpage = PageFactory.initElements(driver, LoginPage.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
		reimbSettings = PageFactory.initElements(driver, ReimbSettings.class);
		rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
	}

	@Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class ,groups = {"reimbursement","settings"})
	public void testCreateReimbursements(Map<String,String> data)throws Exception {

		String unit = data.get("Unit") + new Date().getTime();
		String label = data.get("Label");
		String desc = data.get("Desc");

		Assert.assertTrue(loginpage.loginToApplication(), "User Login to Application as Admin");
		Assert.assertTrue(homepage.clickUserProfileIcon(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
		Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click User Profile Icon");
		Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
		Assert.assertTrue(commonSettings.clickReimb(), "Click on REIMB link");

		Assert.assertTrue(reimbSettings.createReimbTypes(unit,label,desc),"Create Reimbursement");
	}
}
