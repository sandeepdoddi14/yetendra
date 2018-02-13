package com.darwinbox.test.hrms.uiautomation.reimbursement;

import com.darwinbox.test.hrms.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.LoginPage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Pages.reimb.ReimbUnitSettings;
import com.darwinbox.test.hrms.uiautomation.Settings.PageObject.CommonSettingsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

public class ReimbUnitSettingsTest extends TestBase {

    private static final Logger log = Logger.getLogger(ReimbUnitSettingsTest.class);

    HomePage homepage;
    LoginPage loginpage;
    CommonSettingsPage commonSettings;
    ReimbUnitSettings reimbUnitSettings;
    RightMenuOptionsPage rightMenuOption;

    @BeforeClass
    public void setup() {
        ExcelReader.setFilenameAndSheetName("Reimbursement_TestData.xlsx", "createUnits");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        commonSettings = PageFactory.initElements(driver, CommonSettingsPage.class);
        reimbUnitSettings = PageFactory.initElements(driver, ReimbUnitSettings.class);
        rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = {"reimbursement", "settings"})
    public void testCreateReimbursements(Map<String, String> data) {

        String unit = data.get("Unit");
        String label = data.get("Label");
        String desc = data.get("Desc");

        Assert.assertTrue(loginpage.loginToApplication(), "User Login to Application as Admin");
        Assert.assertTrue(homepage.clickUserProfileIcon(), "Click User Profile Icon");
        Assert.assertTrue(rightMenuOption.clickSidebarSwitchToAdmin(), "Click on Switch to Admin");
        Assert.assertTrue(homepage.clickUserProfileIconAdmin(), "Click User Profile Icon");
        Assert.assertTrue(rightMenuOption.clickSidebarSettings(), "Click on Settings link");
        Assert.assertTrue(commonSettings.clickReimb(), "Click on REIMB link");

        Assert.assertTrue(reimbUnitSettings.createReimbTypes(unit, label, desc), "Create Reimbursement");
        Assert.assertTrue(reimbUnitSettings.searchReimbursement(unit),"Created reimbursement found");
        Assert.assertTrue(reimbUnitSettings.deleteReimbursement(),"Deleted reimbursement");
        Assert.assertFalse(reimbUnitSettings.searchReimbursement(unit),"Deleted reimbursement found");

    }
}
