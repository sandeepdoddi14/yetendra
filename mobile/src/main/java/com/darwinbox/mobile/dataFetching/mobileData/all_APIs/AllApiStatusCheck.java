package com.darwinbox.mobile.dataFetching.mobileData.all_APIs;

//import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
//import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class AllApiStatusCheck extends TestBase {
//    LoginPage loginpage;
    GenericHelper objgenhelper;
//    HomePage homepage;
    WaitHelper objWaitHelper;
    GenericMethodsInDataFetching genericMethodsInDataFetching;

    public AllApiStatusCheck(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
//        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
//        homepage = PageFactory.initElements(driver, HomePage.class);
        genericMethodsInDataFetching = PageFactory.initElements(driver, GenericMethodsInDataFetching.class);
    }

    /*
    Retruns API status as Hashmap
     */
    public HashMap getAllApiStatusCheck() {

        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String) loginResponseData.get("token");
        String user_id = (String) loginResponseData.get("user_id");
        String mongo_id = (String) loginResponseData.get("mongo_id");

        Response response = genericMethodsInDataFetching.getPostResponse(getData("API Body"),getData("API"), token, user_id,mongo_id);
        log.info("API : " + data.get("@@url")+getData("API"));
        HashMap mobApi_statusCheck = new HashMap();
        mobApi_statusCheck.put("status", response.path("status"));
        mobApi_statusCheck.put("module", getData("Module Name"));
        mobApi_statusCheck.put("apiName", getData("API Name"));
        log.info("Response Data : "+response.asString());
        return mobApi_statusCheck;
    }
}