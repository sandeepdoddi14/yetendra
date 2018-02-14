package com.darwinbox.test.hrms.uiautomation.reimbursement;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Pages.reimb.ReimbTypeSettings;
import com.darwinbox.test.hrms.uiautomation.Pages.reimb.ReimbUnitSettings;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Map;

public class CreateReimbursementTypes extends TestBase {

    private static final Logger log = Logger.getLogger(ReimbUnitSettingsTest.class);

    HomePage homepage;
    LoginPage loginpage;
    CommonSettingsPage commonSettings;
    ReimbTypeSettings reimbTypeSettings;
    RightMenuOptionsPage rightMenuOption;

    @BeforeClass
    public void setup() {
        ExcelReader.setFilenameAndSheetName("Reimbursement_TestData.xlsx", "createReimbursements");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
        reimbTypeSettings = PageFactory.initElements(driver, ReimbTypeSettings.class);
        rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = {"reimbursement", "settings"})
    public void testCreateReimbursements(Map<String, String> data) {

        Assert.assertTrue(loginpage.loginToApplication(), "User Login to Application as Admin");
        Assert.assertTrue(homepage.clickUserProfileIcon(), "Click User Profile Icon");
        Assert.assertTrue(rightMenuOption.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
        Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click User Profile Icon");
        Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
        Assert.assertTrue(commonSettings.clickReimb(), "Click on REIMB link");
        Assert.assertTrue(reimbTypeSettings.createReimbType());


    }
}
