package com.darwinbox.mobile.dataFetching.mobileData.attendance;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.pages.attendance.AttendanceViewPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

public class MobileAttendanceView extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    WaitHelper objWaitHelper;
    AttendanceViewPage attendanceViewPage;
    GenericMethodsInDataFetching genericMethodsInDataFetching;

    public MobileAttendanceView(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        attendanceViewPage = PageFactory.initElements(driver, AttendanceViewPage.class);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        genericMethodsInDataFetching = PageFactory.initElements(driver, GenericMethodsInDataFetching.class);
    }

    /*
    Get Attendance Attendance View API response
     */
    public HashMap getAttendanceView() {
        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String) loginResponseData.get("token");
        String user_id = (String) loginResponseData.get("user_id");
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("for_month", genericMethodsInDataFetching.getCurrentMonth("yyyy-MM"));
        request.body(jsonObject.toString());
        Response response = request.post(getData("API"));
        log.info("API : " + getData("API"));
        HashMap getCurrentMonthDates = genericMethodsInDataFetching.getAllCurrentMonthDates("dd MMM yyyy, E");
        HashMap mobAttendanceViewData = new HashMap();
        mobAttendanceViewData.put("content", response.path("content"));
        HashMap getContent = (HashMap) mobAttendanceViewData.get("content");
        HashMap getForParticularDate = new HashMap();
        for (int i = 1; i <= getCurrentMonthDates.size(); i++) {
            getForParticularDate.put(i, getContent.get(getCurrentMonthDates.get(i)));
        }
        return getForParticularDate;
    }
}