package com.darwinbox.customflows;

import com.CustomFlowTestBase;
import com.darwinbox.customflows.objects.customflow.CustomFlow;
import com.darwinbox.customflows.objects.forms.CFForm;
import com.darwinbox.customflows.services.CFFormService;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestCreateCFForm extends TestBase {

    LoginPage loginPage;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);

    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void createSlaSettingsTest(Map<String, String> testData) throws Exception {

        //Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        //Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        CustomFlowTestBase.getObject();

    }


}
