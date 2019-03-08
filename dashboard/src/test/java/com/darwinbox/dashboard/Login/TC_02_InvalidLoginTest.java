package com.darwinbox.dashboard.Login;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class TC_02_InvalidLoginTest extends TestBase {

    LoginPage loginpage;

    private static final Logger log = Logger.getLogger(TC_02_InvalidLoginTest.class);

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass( ).getName( ));
    }

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Login_Tests", retryAnalyzer = TestBase.class)
    public void Verify_Invalid_Login_To_Application(Map<String, String> data) throws Exception {

        try {
            Assert.assertTrue(loginpage.EnterUsername(data.get("UserName")), "Exception while inserting username");
            Assert.assertTrue(loginpage.EnterPassword(data.get("Password")), "Exception while inserting password");
            Assert.assertTrue(loginpage.checkInvalidSignIn(data.get("ErrorMessage")), "Exception while clicking SignIn");
        } catch (Exception we) {
            throw new RuntimeException("Test Case Failed");
        }
    }
}
