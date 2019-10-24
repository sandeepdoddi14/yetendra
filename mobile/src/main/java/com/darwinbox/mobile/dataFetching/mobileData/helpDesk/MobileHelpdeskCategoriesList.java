package com.darwinbox.mobile.dataFetching.mobileData.helpDesk;

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

public class MobileHelpdeskCategoriesList extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    WaitHelper objWaitHelper;
    GenericMethodsInDataFetching genericMethodsInDataFetching;

    public MobileHelpdeskCategoriesList(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        genericMethodsInDataFetching = PageFactory.initElements(driver, GenericMethodsInDataFetching.class);
    }

    /*
    Get Helpdesk Category List
     */
    public HashMap getMobHelpdeskCategoriesList() {

        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String) loginResponseData.get("token");
        String user_id = (String) loginResponseData.get("user_id");
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        request.body(jsonObject.toString());
        Response response = request.post(getData("API"));
        log.info("API : " + getData("API"));
        HashMap mobHelpdeskCategoryListData = new HashMap();
        mobHelpdeskCategoryListData.put("status", response.path("status"));
        mobHelpdeskCategoryListData.put("category", response.path("category"));
        return mobHelpdeskCategoryListData;
    }
}
