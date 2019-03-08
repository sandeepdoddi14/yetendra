package com.darwinbox.mobile.dataFetching.mobileData.attendance;

import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceRequestLocation;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MobAttendanceRequestLocation extends WebAttendanceRequestLocation {

    GenericMethodsInDataFetching genericMethodsInDataFetching = new GenericMethodsInDataFetching();

    public HashMap getMobAttendanceRequestLocation() {

        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String) loginResponseData.get("token");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clock_in_allowed", 1);
        jsonObject.put("token", token);
        request.body(jsonObject.toString());
        Response response = request.post("http://test2.darwinbox.io/Mobileapi/AttendanceRequestLocation");


        ArrayList<JSONObject> request_type = response.path("request_type.type");
        ArrayList<JSONObject> locations = response.path("locations.name");
        ArrayList<JSONObject> hrs = response.path("hrs.name");
        ArrayList<JSONObject> mins = response.path("mins.name");

        HashMap mobAttendanceReqLocation = new HashMap();
        mobAttendanceReqLocation.put("request_type", request_type);
        mobAttendanceReqLocation.put("locations", locations);
        mobAttendanceReqLocation.put("hrs", hrs);
        mobAttendanceReqLocation.put("mins", mins);
        return mobAttendanceReqLocation;
    }
}

