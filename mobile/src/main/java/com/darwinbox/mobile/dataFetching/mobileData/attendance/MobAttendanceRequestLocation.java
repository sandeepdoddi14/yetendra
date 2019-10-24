package com.darwinbox.mobile.dataFetching.mobileData.attendance;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.pages.attendance.AttendanceRequestLocationPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class MobAttendanceRequestLocation extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    WaitHelper objWaitHelper;
    AttendanceRequestLocationPage attendanceReqLocaPage;
    GenericMethodsInDataFetching genericMethodsInDataFetching;

    public MobAttendanceRequestLocation(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        attendanceReqLocaPage = PageFactory.initElements(driver, AttendanceRequestLocationPage.class);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        genericMethodsInDataFetching = PageFactory.initElements(driver, GenericMethodsInDataFetching.class);
    }

    /*
    Get Attendance Request location API response
     */
    public HashMap getMobAttendanceRequestLocation() {
        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String) loginResponseData.get("token");
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clock_in_allowed", 1);
        jsonObject.put("token", token);
        request.body(jsonObject.toString());
        Response response = request.post(getData("API"));
        log.info("API : " + getData("API"));
        ArrayList<JSONObject> reqType = response.path("request_type.type");
        ArrayList<JSONObject> reqLocation = response.path("locations.name");
        ArrayList<JSONObject> hrs = response.path("hrs.name");
        ArrayList<JSONObject> mins = response.path("mins.name");
        HashMap mobAttendanceReqLocation = new HashMap();
        mobAttendanceReqLocation.put("reqType", reqType);
        mobAttendanceReqLocation.put("reqLocation", reqLocation);
        mobAttendanceReqLocation.put("hrs", hrs);
        mobAttendanceReqLocation.put("mins", mins);
        return mobAttendanceReqLocation;
    }
}

