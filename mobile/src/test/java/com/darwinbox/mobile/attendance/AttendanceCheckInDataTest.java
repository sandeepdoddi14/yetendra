package com.darwinbox.mobile.attendance;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobileCheckInShow;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebCheckInShow;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceCheckInDataTest extends TestBase {


    MobileCheckInShow mobileCheckInShow;
    WebCheckInShow webCheckInShow;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
        System.out.println(" Master Sheet record obtained ");
    }

    @BeforeMethod
    public void initializeObjects() {
        webCheckInShow = PageFactory.initElements(driver, WebCheckInShow.class);
        mobileCheckInShow = PageFactory.initElements(driver, MobileCheckInShow.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void getAttendanceCheckInDataAPICheck(Map<String, String> data) throws Exception {
        int count = 1;
        for (int i=0; i<12; i++) {
            HashMap webData = webCheckInShow.getWebCheckInShow(i);
            log.info("********************* Web Data Fetched **********************");
            System.out.println(webData);
            HashMap mobData = mobileCheckInShow.getCheckinData(count);
            log.info("********************* Mobile Data Fetched **********************");
            System.out.println(mobData);

            ArrayList<String> mobLocation = (ArrayList<String>) mobData.get("location");
            ArrayList<String> mobDate = (ArrayList<String>) mobData.get("date");
            ArrayList<String> mobTime = (ArrayList<String>) mobData.get("time");
            //ArrayList<String> mobTitle = (ArrayList<String>) mobData.get("title");
            ArrayList<String> mobDescription = (ArrayList<String>) mobData.get("description");

            if (webData != null) {
                ArrayList<String> webTime = (ArrayList<String>) webData.get("time");
                ArrayList<String> webLocation = (ArrayList<String>) webData.get("location");
                ArrayList<String> webDescription = (ArrayList<String>) webData.get("description");
                for (int j=0;j<webTime.size();j++) {
                    try {
                        Assert.assertEquals(mobTime.get(j), webTime.get(j));
                        log.info("Mobile Check IN Time for the "+j+" Check IN in Pass Case for the date "+mobDate.get(j)+" -->" + mobTime.get(j));
                        log.info("Web Check IN Time for the "+j+" Check IN in Pass Case for the date "+mobDate.get(j)+" -->" + webTime.get(j));
                        Reporter("Pass at CheckIN Time","Pass");
                    } catch (AssertionError e) {
//            e.printStackTrace();
                        Reporter("Failed at CheckIN Time","Fail");
                        log.info("Mobile Check IN Time for the "+j+" Check IN in Fail Case for the date "+mobDate.get(j)+" -->" + mobTime.get(j));
                        log.info("Web Check IN Time for the "+j+" Check IN in Fail Case for the date "+mobDate.get(j)+" -->" + webTime.get(j));
                    }
                    try {
                        Assert.assertEquals(mobLocation.get(j), webLocation.get(j));
                        log.info("Mobile Check IN Location for the "+j+" Check IN in Pass Case for the date "+mobDate.get(j)+" -->" + mobLocation.get(j));
                        log.info("Web Check IN Location for the "+j+" Check IN in Pass Case for the date "+mobDate.get(j)+" -->" + webLocation.get(j));
                        Reporter("Pass at CheckIN Location","Pass");
                    } catch (AssertionError e) {
//            e.printStackTrace();
                        Reporter("Failed at CheckIN Location","Fail");
                        log.info("Mobile Check IN Location for the "+j+" Check IN in Fail Case for the date "+mobDate.get(j)+" -->" + mobLocation.get(j));
                        log.info("Web Check IN Location for the "+j+" Check IN in Fail Case for the date "+mobDate.get(j)+" -->" + webLocation.get(j));
                    }
                    try {
                        Assert.assertEquals(mobDescription.get(j), webDescription.get(j));
                        log.info("Mobile Check IN Description for the "+j+" Check IN in Pass Case for the date "+mobDate.get(j)+" -->" + mobDescription.get(j));
                        log.info("Web Check IN Description for the "+j+" Check IN in Pass Case for the date "+mobDate.get(j)+" -->" + webDescription.get(j));
                        Reporter("Pass at CheckIN Description","Pass");
                    } catch (AssertionError e) {
//            e.printStackTrace();
                        Reporter("Failed at CheckIN Description","Fail");
                        log.info("Mobile Check IN Description for the "+j+" Check IN in Fail Case for the date "+mobDate.get(j)+" -->" + mobDescription.get(j));
                        log.info("Web Check IN Description for the "+j+" Check IN in Fail Case for the date "+mobDate.get(j)+" -->" + webDescription.get(j));
                    }
                }
            } else {
                try {
                    Assert.assertEquals(mobTime.size(), 0);
                    log.info("Mobile Check IN data when there are no Check IN's in Pass Case for the date --> " + mobData);
                    log.info("Web Check IN data when there are no Check IN's in Pass Case -->" + webData);
                    Reporter("Pass at CheckIN when there are no Check-IN's","Pass");
                } catch (AssertionError e) {
//            e.printStackTrace();
                    Reporter("Failed at CheckIN when there are no Check-IN's","Fail");
                    log.info("Mobile Check IN data when there are no Check IN's in Failed Case --> " + mobData);
                    log.info("Web Check IN data when there are no Check IN's in Failed Case --> " + webData);
                }
            }
            count--;
        }
    }
}