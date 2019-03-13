package com.darwinbox.mobile.dataFetching.mobileData.login;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

public class MobileAuthLogin extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    WaitHelper objWaitHelper;
    GenericMethodsInDataFetching genericMethodsInDataFetching;
    RequestSpecification request;

    public MobileAuthLogin(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        genericMethodsInDataFetching = PageFactory.initElements(driver, GenericMethodsInDataFetching.class);
        request = RestAssured.given();
    }
/*
Hit mobile login API and return response in Hashmap
 */
    public HashMap getMobileAuthLoginData() {
        HashMap mobileAuthLoginData = new HashMap();
        if (getData("Case Type").equalsIgnoreCase("sendOTP_Invalid") || getData("Case Type").equalsIgnoreCase("sendOTP_Valid")) {
            request.header("Content-Type", "application/json");
            JSONObject json = new JSONObject();

            json.put("mobile_number", getData("Mobile Number"));
            json.put("tenant", getData("Instance"));


            request.body(json.toString());
            Response response = request.post(getData("API"));

            int code = response.getStatusCode();

            mobileAuthLoginData.put("status", response.path("status"));
            mobileAuthLoginData.put("message", response.path("message"));
            mobileAuthLoginData.put("caseType", getData("Case Type"));
        } else {

            HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
            if (getData("Case Type").equalsIgnoreCase("Invalid")) {
                mobileAuthLoginData.put("status", loginResponseData.get("status"));
                mobileAuthLoginData.put("message", loginResponseData.get("message"));
                mobileAuthLoginData.put("caseType", "Invalid");
            } else if (getData("Case Type").equalsIgnoreCase("AppAllowInvalid")) {
                mobileAuthLoginData.put("status", loginResponseData.get("status"));
                mobileAuthLoginData.put("message", loginResponseData.get("message"));
                mobileAuthLoginData.put("caseType", "AppAllowInvalid");
            } else {
                mobileAuthLoginData.put("status", loginResponseData.get("status"));
                mobileAuthLoginData.put("caseType", "Valid");
            }
        }
        log.info("API : " + getData("API"));
        return mobileAuthLoginData;
    }
}
