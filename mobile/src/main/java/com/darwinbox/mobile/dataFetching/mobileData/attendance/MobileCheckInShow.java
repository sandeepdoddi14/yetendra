package com.darwinbox.mobile.dataFetching.mobileData.attendance;

//import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
//import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.pages.attendance.CheckInShowPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class MobileCheckInShow extends TestBase {
//    LoginPage loginpage;
    GenericHelper objgenhelper;
//    HomePage homepage;
    WaitHelper objWaitHelper;
    CheckInShowPage checkInShowPage;
    GenericMethodsInDataFetching genericMethodsInDataFetching;

    public MobileCheckInShow(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        checkInShowPage = PageFactory.initElements(driver, CheckInShowPage.class);
//        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
//        homepage = PageFactory.initElements(driver, HomePage.class);
        genericMethodsInDataFetching = PageFactory.initElements(driver, GenericMethodsInDataFetching.class);
    }
/*
Get Check IN data API response
 */
    public HashMap getCheckinData(int monthSelect) {
        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String) loginResponseData.get("token");
        String user_id = (String) loginResponseData.get("user_id");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("user_id", user_id);
        request.body(jsonObject.toString());
        Response response = request.post(getData("API"));

        log.info("API : " + getData("API"));
        String monthFilter = genericMethodsInDataFetching.getRequiredMonth("yyyy-MM", monthSelect);
        System.out.println("Month Filter : " + monthFilter);
        ArrayList<String> locationBeforeMonthFilter = response.path("checkin_data.location");
        ArrayList<String> dateBeforeMonthFilter = response.path("checkin_data.date");
        ArrayList<String> timeBeforeMonthFilter = response.path("checkin_data.time");
        ArrayList<String> titleBeforeMonthFilter = response.path("checkin_data.title");
        ArrayList<String> descriptionBeforeMonthFilter = response.path("checkin_data.description");


        ArrayList<String> date = new ArrayList<String>();
        ArrayList<String> location = new ArrayList<String>();
        ArrayList<String> time = new ArrayList<String>();
        ArrayList<String> title = new ArrayList<String>();
        ArrayList<String> description = new ArrayList<String>();
        for (int i = 0; i < dateBeforeMonthFilter.size(); i++) {
            if (dateBeforeMonthFilter.get(i).contains(monthFilter)) {
                location.add(locationBeforeMonthFilter.get(i));
                date.add(dateBeforeMonthFilter.get(i));
                time.add(timeBeforeMonthFilter.get(i));
                title.add(titleBeforeMonthFilter.get(i));
                description.add(descriptionBeforeMonthFilter.get(i));
            }
        }

        HashMap checkinData = new HashMap();
        checkinData.put("location", location);
        checkinData.put("date", date);
        checkinData.put("time", time);
        checkinData.put("title", title);
        checkinData.put("description", description);

        return checkinData;
    }
}
