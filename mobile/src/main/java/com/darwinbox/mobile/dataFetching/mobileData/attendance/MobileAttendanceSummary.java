package com.darwinbox.mobile.dataFetching.mobileData.attendance;

import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceSummary;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.*;

import java.util.HashMap;

public class MobileAttendanceSummary extends WebAttendanceSummary {

    GenericMethodsInDataFetching genericMethodsInDataFetching = new GenericMethodsInDataFetching();

    public HashMap getMobAttendanceSummary() {
        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String) loginResponseData.get("token");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        request.body(jsonObject.toString());
        Response response = request.post("http://test2.darwinbox.io/Mobileapi/AttendanceDetails");

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
        return mobAttendanceSummaryData;
    }
}
