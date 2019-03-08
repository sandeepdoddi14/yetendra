package com.darwinbox.mobile.dataFetching.mobileData.attendance;

import com.darwinbox.mobile.dataFetching.genericMethods.GenericMethodsInDataFetching;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebCheckInShow;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MobileCheckInShow extends WebCheckInShow {

    GenericMethodsInDataFetching genericMethodsInDataFetching = new GenericMethodsInDataFetching();

    public HashMap getCheckinData() {
        HashMap loginResponseData = genericMethodsInDataFetching.loginResponse();
        String token = (String)loginResponseData.get("token");
        String user_id = (String)loginResponseData.get("user_id");

        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        jsonObject.put("user_id", user_id);
        request.body(jsonObject.toString());
        Response response = request.post("http://test2.darwinbox.io/Mobileapi/CheckInshow");

        String monthFilter = genericMethodsInDataFetching.getCurrentMonth("yyyy-MM");
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
        for(int i=0; i<dateBeforeMonthFilter.size();i++){
            if(dateBeforeMonthFilter.get(i).contains(monthFilter)){
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
