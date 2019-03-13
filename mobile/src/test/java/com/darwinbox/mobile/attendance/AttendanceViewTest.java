package com.darwinbox.mobile.attendance;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobileAttendanceView;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceView;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class AttendanceViewTest extends TestBase {

    MobileAttendanceView mobileAttendanceView;
    WebAttendanceView webAttendanceView;
    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
        System.out.println(" Master Sheet record obtained ");
    }

    @BeforeMethod
    public void initializeObjects() {
        webAttendanceView = PageFactory.initElements(driver, WebAttendanceView.class);
        mobileAttendanceView = PageFactory.initElements(driver, MobileAttendanceView.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void getAttendanceViewAPICheck(Map<String, String> data) throws Exception {
        HashMap webData = webAttendanceView.getWebAttendanceView();
        log.info("********************* Web Data Fetched **********************");
        log.info("Web Data -> "+webData);

        HashMap mobData = mobileAttendanceView.getAttendanceView();
        log.info("********************* Mobile Data Fetched **********************");
        log.info("Mob Data -> "+mobData);
//        for (int i=5;i<=5;i++)
        for (int i=1;i<=webData.size();i++)
        {
            HashMap webDataForOneRow =(HashMap)webData.get(i);
            HashMap mobDataForOneRow =(HashMap)mobData.get(i);

            //InTime Comparision
            try {
                Assert.assertEquals(mobDataForOneRow.get("intime").toString().trim(), webDataForOneRow.get("inTime").toString().trim());
                log.info("Mobile Attendance InTime for the date "+i+" in Pass Case --> "+mobDataForOneRow.get("intime").toString().trim());
                log.info("Web Attendance InTime for the date "+i+" in Pass Case --> "+webDataForOneRow.get("inTime").toString().trim());
                Reporter("Pass at Attendance InTime for the date "+i, "Pass");
            } catch (AssertionError e) {
                // e.printStackTrace();
                Reporter("Failed at Attendance InTime for the date "+i, "Fail");
                log.info("Mobile Attendance InTime for the date "+i+" in Fail Case --> "+mobDataForOneRow.get("intime").toString().trim());
                log.info("Web Attendance InTime for the date "+i+" in Fail Case --> "+webDataForOneRow.get("inTime").toString().trim());
            }

            //OutTime Comparision
            try {
                Assert.assertEquals(mobDataForOneRow.get("outtime").toString().trim(), webDataForOneRow.get("outTime").toString().trim());
                log.info("Mobile Attendance OutTime for the date "+i+" in Pass Case --> "+mobDataForOneRow.get("outtime".toString().trim()));
                log.info("Web Attendance OutTime for the date "+i+" in Pass Case --> "+webDataForOneRow.get("outTime").toString().trim());
                Reporter("Pass at Attendance OutTime for the date "+i, "Pass");
            } catch (AssertionError e) {
                //  e.printStackTrace();
                Reporter("Failed at Attendance OutTime for the date "+i, "Fail");
                log.info("Mobile Attendance OutTime for the date "+i+" in Fail Case --> "+mobDataForOneRow.get("outtime").toString().trim());
                log.info("Web Attendance OutTime for the date "+i+" in Fail Case --> "+webDataForOneRow.get("outTime").toString().trim());
            }

            //Work Duration Comparision
            try {
                Assert.assertEquals(mobDataForOneRow.get("work_duration").toString().trim(), webDataForOneRow.get("workDuration").toString().trim());
                log.info("Mobile Attendance Work Duration for the date "+i+" in Pass Case --> "+mobDataForOneRow.get("work_duration").toString().trim());
                log.info("Web Attendance Work Duration for the date "+i+" in Pass Case --> "+webDataForOneRow.get("workDuration").toString().trim());
                Reporter("Pass at Attendance Work Duration for the date "+i,"Pass");
            } catch (AssertionError e) {
                //  e.printStackTrace();
                Reporter("Failed at Attendance Work Duration for the date "+i,"Fail");
                log.info("Mobile Attendance Work Duration for the date "+i+" in Fail Case --> "+mobDataForOneRow.get("work_duration").toString().trim());
                log.info("Web Attendance Work Duration for the date "+i+" in Fail Case --> "+webDataForOneRow.get("workDuration").toString().trim());
            }

            //Status Comparision
            try {
                Assert.assertEquals(mobDataForOneRow.get("status").toString().trim(), webDataForOneRow.get("status").toString().trim());
                log.info("Mobile Attendance Status for the date "+i+" in Pass Case --> "+mobDataForOneRow.get("status").toString().trim());
                log.info("Web Attendance Status for the date "+i+" in Pass Case --> "+webDataForOneRow.get("status").toString().trim());
                Reporter("Pass at Attendance Status for the date "+i,"Pass");
            } catch (AssertionError e) {
                // e.printStackTrace();
                Reporter("Failed at Attendance Status for the date "+i,"Fail");
                log.info("Mobile Attendance Status for the date "+i+" in Fail Case --> "+mobDataForOneRow.get("status").toString().trim());
                log.info("Web Attendance Status for the date "+i+" in Fail Case --> "+webDataForOneRow.get("status").toString().trim());
            }

        }
    }
}