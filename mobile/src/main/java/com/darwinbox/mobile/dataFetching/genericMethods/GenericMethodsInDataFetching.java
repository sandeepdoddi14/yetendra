package com.darwinbox.mobile.dataFetching.genericMethods;

import com.darwinbox.framework.uiautomation.base.TestBase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class GenericMethodsInDataFetching extends TestBase{

    public GenericMethodsInDataFetching(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }
    /*
    Hit the Auth Login API
    Username and Password are taken from excel
     */
    public HashMap loginResponse() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject json = new JSONObject();

        json.put("username", getData("UserName"));
        json.put("password", getData("Password"));

        request.body(json.toString());
        Response response = request.post(getData("API"));

        int code = response.getStatusCode();

        HashMap loginResponse = new HashMap();
        loginResponse.put("token", response.path("token"));
        loginResponse.put("user_id", response.path("user_id"));
        loginResponse.put("tenant_id", response.path("tenant_id"));
        loginResponse.put("expires", response.path("expires"));
        loginResponse.put("is_manager", response.path("is_manager"));
        loginResponse.put("status", response.path("status"));
        loginResponse.put("message", response.path("message"));
        return loginResponse;
    }
/*
Return all the current month dates in the required format passed in parameters

 */
    public HashMap getAllCurrentMonthDates(String format)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat(format);
        HashMap currentMonthDates = new HashMap();
        for (int i = 1; i < maxDay+1; i++)
        {
            cal.set(Calendar.DAY_OF_MONTH, i);
            currentMonthDates.put(i,df.format(cal.getTime()));
        }
        return currentMonthDates;
    }
    /*
    Get current month in the required format passed in parameters
     */
    public String getCurrentMonth(String format)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat df = new SimpleDateFormat(format);
        String dateFormat = df.format(cal.getTime());
        return dateFormat;
    }
    /*
    Get required month in the required format passed in parameters
     */
    public String getRequiredMonth(String format, int i)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, i);
        SimpleDateFormat df = new SimpleDateFormat(format);
        String dateFormat = df.format(cal.getTime());
        return dateFormat;
    }
}
