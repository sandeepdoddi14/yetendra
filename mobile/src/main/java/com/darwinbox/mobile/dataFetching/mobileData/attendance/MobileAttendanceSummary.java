package com.darwinbox.mobile.dataFetching.mobileData.attendance;

//import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
//import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.pages.attendance.AttendanceSummaryPage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;

public class MobileAttendanceSummary extends TestBase {
//    LoginPage loginpage;
    GenericHelper objgenhelper;
//    HomePage homepage;
    WaitHelper objWaitHelper;
    AttendanceSummaryPage attendanceSummaryPage;
    GenericMethodsInDataFetching genericMethodsInDataFetching;

    public MobileAttendanceSummary(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        attendanceSummaryPage = PageFactory.initElements(driver, AttendanceSummaryPage.class);
//        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
//        homepage = PageFactory.initElements(driver, HomePage.class);
        genericMethodsInDataFetching = PageFactory.initElements(driver, GenericMethodsInDataFetching.class);
    }

    /*
    Get Attendance Summary API response
     */
    public HashMap getMobAttendanceSummary() {
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

        HashMap mobAttendanceSummaryData = new HashMap();
        mobAttendanceSummaryData.put("leaveDays", response.path("attendance_data.leave_days"));
        mobAttendanceSummaryData.put("presentDays", response.path("attendance_data.present_days"));
        mobAttendanceSummaryData.put("absentDays", response.path("attendance_data.absent_days"));
        mobAttendanceSummaryData.put("clockInPriority", response.path("attendance_data.clckin_priority"));
        mobAttendanceSummaryData.put("shiftName", response.path("attendance_data.shift_name"));
        mobAttendanceSummaryData.put("shiftDescBeginTime", response.path("attendance_data.shift_description_begin_time"));
        mobAttendanceSummaryData.put("shiftDescOutTime", response.path("attendance_data.shift_description_out_time"));
        mobAttendanceSummaryData.put("policyName", response.path("attendance_data.policy_name"));
        mobAttendanceSummaryData.put("weeklyOffName", response.path("attendance_data.weekly_off_name"));
        mobAttendanceSummaryData.put("weeklyOffDesc", response.path("attendance_data.weekly_off_description"));
        mobAttendanceSummaryData.put("checkINStatus", response.path("attendance_data.checkIn"));
        mobAttendanceSummaryData.put("heading", response.path("attendance_data.policy_description.heading"));
        mobAttendanceSummaryData.put("status", response.path("attendance_data.policy_description.status"));
        mobAttendanceSummaryData.put("desc", response.path("attendance_data.policy_description.desc"));
        return mobAttendanceSummaryData;
    }
}
