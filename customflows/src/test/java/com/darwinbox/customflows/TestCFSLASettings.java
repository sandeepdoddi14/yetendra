package com.darwinbox.customflows;

import com.CustomFlowTestBase;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestCFSLASettings extends TestBase {


    LoginPage loginPage;


    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        CustomFlowTestBase.getObject("TestData.xlsx");
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void createSLASettingsTest(Map<String, String> testData) throws Exception {

        try {
            Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");


        Reporter("Selected policies to hide","INFO");

        CustomFlowTestBase cfTB = CustomFlowTestBase.getObject("SLASettings.xlsx");


    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void updateSLASettingsTest(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void deleteSLASettingsTest(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");
    }
}
