package com.darwinbox.mobile.dataFetching.mobileData.attendance;

import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceView;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.*;

import java.util.*;

public class MobileAttendanceView extends WebAttendanceView {

    GenericMethodsInDataFetching genericMethodsInDataFetching = new GenericMethodsInDataFetching();

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
        Response response = request.post("http://test2.darwinbox.io/Mobileapi/AttendanceView");

        HashMap getCurrentMonthDates = genericMethodsInDataFetching.getAllCurrentMonthDates("dd MMM yyyy, E");
        HashMap mobAttendanceViewData = new HashMap();
        mobAttendanceViewData.put("content", response.path("content"));
        HashMap getContent = (HashMap) mobAttendanceViewData.get("content");
        HashMap getForParticularDate = new HashMap();
        for(int i=1;i<=getCurrentMonthDates.size();i++){
            getForParticularDate.put(i, getContent.get(getCurrentMonthDates.get(i)));
        }
        return getForParticularDate;
    }
}