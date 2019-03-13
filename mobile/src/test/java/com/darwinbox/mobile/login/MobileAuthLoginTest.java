package com.darwinbox.mobile.login;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobileAttendanceSummary;
import com.darwinbox.mobile.dataFetching.mobileData.login.MobileAuthLogin;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceSummary;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class MobileAuthLoginTest extends TestBase {


    MobileAuthLogin mobileAuthLogin;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
        System.out.println(" Master Sheet record obtained ");
    }

    @BeforeMethod
    public void initializeObjects() {
        mobileAuthLogin = PageFactory.initElements(driver, MobileAuthLogin.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void getMobileAuthLoginTest(Map<String, String> data) throws Exception {
        HashMap mobData = mobileAuthLogin.getMobileAuthLoginData();
        log.info("********************* Mobile Data Fetched **********************");
        Reporter("********************* Mobile Data Fetched **********************", "Pass");
        log.info("Mobile Data -> " + mobData);

        if (mobData.get("caseType").equals("Invalid")) {
            try {
                Assert.assertEquals(mobData.get("status").toString().trim(), "0");
                log.info("Mobile Login Status pass at Invalid username or password");
                Reporter("Mobile Login Status pass at Invalid username or password", "Pass");
                try {
                    Assert.assertEquals(mobData.get("message").toString().trim(), "Invalid Login");
                    log.info("Mobile Login message pass at Invalid username or password");
                    Reporter("Mobile Login message pass at Invalid username or password", "Pass");
                }
                catch (AssertionError e){
                    e.printStackTrace();
                    log.info("Mobile Login message at Invalid username or password failed case -->" + mobData.get("message").toString().trim());
                    Reporter("Mobile Login message failed at Invalid username or password", "Fail");
                }

            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile Login Status at Invalid username or password failed case -->" + mobData.get("status").toString().trim());
                Reporter("Mobile Login Status failed at Invalid username or password", "Fail");

            }
        }
        else if (mobData.get("caseType").equals("AppAllowInvalid"))
        {
            try {
                Assert.assertEquals(mobData.get("status").toString().trim(), "0");
                log.info("Mobile Login Status pass when App not allowed");
                Reporter("Mobile Login Status pass when App not allowed", "Pass");
                try {
                    Assert.assertEquals(mobData.get("message").toString().trim(), "Not Allowed to use Mobile App");
                    log.info("Mobile Login message pass when App not allowed");
                    Reporter("Mobile Login message pass when App not allowed", "Pass");
                }
                catch (AssertionError e){
                    e.printStackTrace();
                    log.info("Mobile Login message when App not allowed failed case -->" + mobData.get("message").toString().trim());
                    Reporter("Mobile Login message failed when App not allowed", "Fail");
                }

            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile Login Status when App not allowed failed case -->" + mobData.get("status").toString().trim());
                Reporter("Mobile Login Status failed when App not allowed", "Fail");

            }
        }
        else if (mobData.get("caseType").equals("Valid"))
        {
            try {
                Assert.assertEquals(mobData.get("status").toString().trim(), "1");
                log.info("Mobile Login Status pass at valid username and password");
                Reporter("Mobile Login Status pass at valid username and password", "Pass");
                Reporter("Toke in pass case", mobData.get("token").toString().trim());

            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile Login Status failed at valid username and password -->" + mobData.get("status").toString().trim());
                Reporter("Mobile Login Status failed at valid username or password", "Fail");

            }
        }
        else if (mobData.get("caseType").equals("sendOTP_Invalid"))
        {
            try {
                Assert.assertEquals(mobData.get("status").toString().trim(), "0");
                log.info("Mobile send OTP status pass when user try to send OTP for non registered mobile numebr");
                Reporter("Mobile send OTP Status pass when user try to send OTP for non registered mobile numebr", "Pass");
                Reporter("Status in pass case when user try to send OTP for non registered mobile numebr", mobData.get("status").toString().trim());

            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile send OTP Status failed when user try to send OTP for non registered mobile numebr -->" + mobData.get("status").toString().trim());
                Reporter("Mobile send OTP Status failed when user try to send OTP for non registered mobile numebr", "Fail");

            }
            try {
                Assert.assertEquals(mobData.get("message").toString().trim(), "Login via. OTP is not enabled for the number entered by you. Please contact your HR or Administrator for more details");
                log.info("Mobile send OTP message pass when user try to send OTP for non registered mobile numebr");
                Reporter("Mobile send OTP message pass when user try to send OTP for non registered mobile numebr", "Pass");
                Reporter("Message in pass case when user try to send OTP for non registered mobile numebr", mobData.get("message").toString().trim());

            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile send OTP message failed when user try to send OTP for non registered mobile numebr -->" + mobData.get("message").toString().trim());
                Reporter("Mobile send OTP message failed when user try to send OTP for non registered mobile numebr", "Fail");
            }
        }
        else {
            try {
                Assert.assertEquals(mobData.get("status").toString().trim(), "1");
                log.info("Mobile send OTP status pass when user try to send OTP for registered mobile numebr");
                Reporter("Mobile send OTP Status pass when user try to send OTP for registered mobile numebr", "Pass");
                Reporter("Status in pass case when user try to send OTP for registered mobile numebr", mobData.get("status").toString().trim());

            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile send OTP Status failed when user try to send OTP for registered mobile numebr -->" + mobData.get("status").toString().trim());
                Reporter("Mobile send OTP Status failed when user try to send OTP for registered mobile numebr", "Fail");

            }
            try {
                Assert.assertEquals(mobData.get("message").toString().trim(), "Sent OTP to requested Mobile Number");
                log.info("Mobile send OTP message pass when user try to send OTP for registered mobile numebr");
                Reporter("Mobile send OTP message pass when user try to send OTP for registered mobile numebr", "Pass");
                Reporter("Message in pass case when user try to send OTP for registered mobile numebr", mobData.get("message").toString().trim());

            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile send OTP message failed when user try to send OTP for registered mobile numebr -->" + mobData.get("message").toString().trim());
                Reporter("Mobile send OTP message failed when user try to send OTP for registered mobile numebr", "Fail");
            }
        }
    }
}

