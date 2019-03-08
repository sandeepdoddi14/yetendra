package com.darwinbox.mobile.attendance;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobileCheckInShow;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebCheckInShow;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class AttendanceCheckInDataTest extends TestBase {

    MobileCheckInShow mobileCheckInShow = new MobileCheckInShow();
    WebCheckInShow webCheckInShow = new WebCheckInShow();

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void getAttendanceCheckInDataAPICheck() {
        HashMap webData = webCheckInShow.getWebCheckInShow();
        log.info("********************* Web Data Fetched **********************");

        HashMap mobData = mobileCheckInShow.getCheckinData();
        log.info("********************* Mobile Data Fetched **********************");


        ArrayList<String> location = (ArrayList<String>) mobData.get("location");
        ArrayList<String> date = (ArrayList<String>) mobData.get("date");
        ArrayList<String> time = (ArrayList<String>) mobData.get("time");
        ArrayList<String> title = (ArrayList<String>) mobData.get("title");
        ArrayList<String> description = (ArrayList<String>) mobData.get("description");

        System.out.println(location.size());
        System.out.println(location.get(0));
        System.out.println(date.get(0));
        System.out.println(time.get(0));
        System.out.println(title.get(0));
        System.out.println(description.get(0));


        ArrayList<String> webTime = (ArrayList<String>) webData.get("time");
        ArrayList<String> webDescription = (ArrayList<String>) webData.get("description");
        ArrayList<String> webLocation = (ArrayList<String>) webData.get("location");

        System.out.println(webTime.get(0));
        System.out.println(webDescription.get(0));
        System.out.println(webLocation.get(0));
    }
}