package com.darwinbox.mobile.attendance;


import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobileAttendanceSummary;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceSummary;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceSummaryTest extends TestBase {


    MobileAttendanceSummary mobAttendanceSummary;
    WebAttendanceSummary webAttendanceSummary;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
        System.out.println(" Master Sheet record obtained ");
    }

    @BeforeMethod
    public void initializeObjects() {
        webAttendanceSummary = PageFactory.initElements(driver, WebAttendanceSummary.class);
        mobAttendanceSummary = PageFactory.initElements(driver, MobileAttendanceSummary.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void Attendance_Summary(Map<String, String> data) throws Exception {

        HashMap webData = webAttendanceSummary.getWebAttendanceSummary();
        log.info("********************* Web Data Fetched **********************");
        Reporter("********************* Web Data Fetched **********************", "Pass");
        log.info("Web Data -> " + webData);

        HashMap mobData = mobAttendanceSummary.getMobAttendanceSummary();
        log.info("********************* Mobile Data Fetched **********************");
        log.info("Mob Data -> " + mobData);

////        //Mobile Response data

        ArrayList<String> mobPolicyHeading = (ArrayList<String>) mobData.get("heading");
        ArrayList<String> mobPolicyStatus = (ArrayList<String>) mobData.get("status");
        ArrayList<String> mobPolicyDesc = (ArrayList<String>) mobData.get("desc");

//        //Web Elements data


        ArrayList<String> webPolicyHeading = (ArrayList<String>) webData.get("heading");
        ArrayList<String> webPolicyStatus = (ArrayList<String>) webData.get("status");
        ArrayList<String> webPolicyDesc = (ArrayList<String>) webData.get("desc");


        try {
            Assert.assertEquals(mobData.get("leaveDays").toString().trim(), webData.get("leaveDays").toString().trim());
            log.info("Mobile Leave Days Count in Pass Case -->" + mobData.get("leaveDays").toString().trim());
            log.info("Web Leave Days Count in Pass Case -->" + webData.get("leaveDays").toString().trim());
            Reporter("Pass at Leave Days Count","Pass");
        } catch (AssertionError e) {
            e.printStackTrace();
            Reporter("Failed at Leave Days Count","Fail");
            log.info("Mobile Leave Days Count in Failed Case -->" + mobData.get("leaveDays").toString().trim());
            log.info("Web Leave Days Count in Failed Case -->" + webData.get("leaveDays").toString().trim());
        }

        try {
            Assert.assertEquals(mobData.get("presentDays").toString().trim(), webData.get("presentDays").toString().trim());
            log.info("Mobile Present Days Count in Pass Case -->" + mobData.get("presentDays").toString().trim());
            log.info("Web Present Days Count in Pass Case -->" + webData.get("presentDays").toString().trim());
            Reporter("Pass at Present Days Count","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Present Days Count","Fail");
            log.info("Mobile Present Days Count in Failed Case -->" + mobData.get("presentDays").toString().trim());
            log.info("Web Present Days Count in Failed Case -->" + webData.get("presentDays").toString().trim());
        }

        try {
            Assert.assertEquals(mobData.get("absentDays").toString().trim(), webData.get("absentDays").toString().trim());
            log.info("Mobile Absent Days Count in Pass Case -->" + mobData.get("absentDays").toString().trim());
            log.info("Web Absent Days Count in Pass Case -->" + webData.get("absentDays").toString().trim());
            Reporter("Pass at Absent Days Count","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Absent Days Count","Fail");
            log.info("Mobile Absent Days Count in Failed Case -->" + mobData.get("absentDays").toString().trim());
            log.info("Web Absent Days Count in Failed Case -->" + webData.get("absentDays").toString().trim());
        }

        try {
            Assert.assertEquals(mobData.get("clockInPriority").toString().trim(), webData.get("clockInPriority").toString().trim());
            log.info("Mobile ClockInPriority in Pass Case -->" + mobData.get("clockInPriority").toString().trim());
            log.info("Web ClockInPriority in Pass Case -->" + webData.get("clockInPriority").toString().trim());
            Reporter("Pass at ClockInPriority","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at ClockInPriority","Fail");
            log.info("Mobile ClockInPriority in Failed Case -->" + mobData.get("clockInPriority").toString().trim());
            log.info("Web ClockInPriority in Failed Case -->" + webData.get("clockInPriority").toString().trim());
        }

        try {
            Assert.assertEquals(mobData.get("shiftName").toString().trim(), webData.get("shiftName").toString().trim());
            log.info("Mobile Shift Name in Pass Case -->" + mobData.get("shiftName").toString().trim());
            log.info("Web Shift Name in Pass Case -->" + webData.get("shiftName").toString().trim());
            Reporter("Pass at Shift Name","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Shift Name","Fail");
            log.info("Mobile Shift Name in Failed Case -->" + mobData.get("shiftName").toString().trim());
            log.info("Web Shift Name in Failed Case -->" + webData.get("shiftName").toString().trim());
        }
        try {
            Assert.assertEquals(mobData.get("shiftDescBeginTime").toString().trim(), webData.get("shiftDescBeginTime").toString().trim());
            log.info("Mobile Shift Start Time in Pass Case -->" + mobData.get("shiftDescBeginTime").toString().trim());
            log.info("Web Shift Start Time in Pass Case -->" + webData.get("shiftDescBeginTime").toString().trim());
            Reporter("Pass at Shift Start Time","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Shift Start Time","Fail");
            log.info("Mobile Shift Start Time in Failed Case -->" + mobData.get("shiftDescBeginTime").toString().trim());
            log.info("Web Shift Start Time in Failed Case -->" + webData.get("shiftDescBeginTime").toString().trim());
        }
        try {
            Assert.assertEquals(mobData.get("shiftDescOutTime").toString().trim(), webData.get("shiftDescOutTime").toString().trim());
            log.info("Mobile Shift End Time in Pass Case -->" + mobData.get("shiftDescOutTime").toString().trim());
            log.info("Web Shift End Time in Pass Case -->" + webData.get("shiftDescOutTime").toString().trim());
            Reporter("Pass at Shift End Time","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Shift End Time","Fail");
            log.info("Mobile Shift End Time in Failed Case -->" + mobData.get("shiftDescOutTime").toString().trim());
            log.info("Web Shift End Time in Failed Case -->" + webData.get("shiftDescOutTime").toString().trim());
        }
        try {
            Assert.assertEquals(mobData.get("policyName").toString().trim(), webData.get("policyName").toString().trim());
            log.info("Mobile Policy Name in Pass Case -->" + mobData.get("policyName").toString().trim());
            log.info("Web Policy Name in Pass Case -->" + webData.get("policyName").toString().trim());
            Reporter("Pass at Policy Name","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Policy Name","Fail");
            log.info("Mobile Policy Name in Failed Case -->" + mobData.get("policyName").toString().trim());
            log.info("Web Policy Name in Failed Case -->" + webData.get("policyName").toString().trim());
        }

        try {
            Assert.assertEquals(mobData.get("weeklyOffName").toString().trim(), webData.get("weeklyOffName").toString().trim());
            log.info("Mobile WeeklyOff Name in Pass Case -->" + mobData.get("weeklyOffName").toString().trim());
            log.info("Web WeeklyOff Name in Pass Case -->" + webData.get("weeklyOffName").toString().trim());
            Reporter("Pass at WeeklyOff Name","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at WeeklyOff Name","Fail");
            log.info("Mobile WeeklyOff Name in Failed Case -->" + mobData.get("weeklyOffName").toString().trim());
            log.info("Web WeeklyOff Name in Failed Case -->" + webData.get("weeklyOffName").toString().trim());
        }

        try {
            Assert.assertEquals(mobData.get("weeklyOffDesc").toString().trim(), webData.get("weeklyOffDesc").toString().trim());
            log.info("Mobile WeeklyOff Description in Pass Case -->" + mobData.get("weeklyOffDesc").toString().trim());
            log.info("Web WeeklyOff Description in Pass Case -->" + webData.get("weeklyOffDesc").toString().trim());
            Reporter("Pass at WeeklyOff Description","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at WeeklyOff Description","Fail");
            log.info("Mobile WeeklyOff Description in Failed Case -->" + mobData.get("weeklyOffDesc").toString().trim());
            log.info("Web WeeklyOff Description in Failed Case -->" + webData.get("weeklyOffDesc").toString().trim());
        }
        if (mobPolicyHeading.size() == webPolicyHeading.size()) {
            log.info("All the policy details are same in Web and Mobile, Policy details count is matching");

            for (int i = 0; i <= webPolicyHeading.size(); i++) {
                try {
                    Assert.assertEquals(mobPolicyHeading.get(i), webPolicyHeading.get(i));
                    log.info("Mobile Policy Attribute in Pass Case -->" + mobPolicyHeading.get(i));
                    log.info("Web Policy Attribute in Pass Case -->" + webPolicyHeading.get(i));
                    Reporter("Pass at Policy Attribute","Pass");
                } catch (AssertionError e) {
//            e.printStackTrace();
                    Reporter("Failed at Policy Attribute","Fail");
                    log.info("Mobile Policy Attribute in Failed Case -->" + mobPolicyHeading.get(i));
                    log.info("Web Policy Attribute in Failed Case -->" + webPolicyHeading.get(i));
                }
                try {
                    Assert.assertEquals(mobPolicyStatus.get(i), webPolicyStatus.get(i));
                    log.info("Mobile Policy Status in Pass Case -->" + mobPolicyStatus.get(i));
                    log.info("Web Policy Status in Pass Case -->" + webPolicyStatus.get(i));
                    Reporter("Pass at Policy Status","Pass");
                } catch (AssertionError e) {
//            e.printStackTrace();
                    Reporter("Failed at Policy Status","Fail");
                    log.info("Mobile Policy Status in Failed Case -->" + mobPolicyStatus.get(i));
                    log.info("Web Policy Status in Failed Case -->" + webPolicyStatus.get(i));
                }

                try {
                    Assert.assertEquals(mobPolicyDesc.get(i), webPolicyDesc.get(i));
                    log.info("Mobile Policy Desc in Pass Case -->" + mobPolicyDesc.get(i));
                    log.info("Web Policy Desc in Pass Case -->" + webPolicyDesc.get(i));
                    Reporter("Pass at Policy Description", "Pass");
                } catch (AssertionError e) {
//            e.printStackTrace();
                    Reporter("Failed at Policy Description", "Fail");
                    log.info("Mobile Policy Desc in Failed Case -->" + mobPolicyDesc.get(i));
                    log.info("Web Policy Desc in Failed Case -->" + webPolicyDesc.get(i));
                }
            }
        } else {
            log.info("All the policy details are not same in Web and Mobile, Policy details count is not matching");
            Reporter("All the policy details are not same in Web and Mobile, Policy details count is not matching", "Fail");
        }
        try {
            Assert.assertEquals(mobData.get("checkINStatus").toString().trim(), webData.get("checkINStatus").toString().trim());
            log.info("Mobile CheckIN Status in Pass Case -->" + mobData.get("checkINStatus").toString().trim());
            log.info("Web CheckIN Status in Pass Case -->" + webData.get("checkINStatus").toString().trim());
            Reporter("Pass at CheckIN Status", "Pass");
        } catch (AssertionError e) {
            e.printStackTrace();
            Reporter("Failed at CheckIN Status", "Fail");
            log.info("Mobile CheckIN Status in Failed Case -->" + mobData.get("checkINStatus").toString().trim());
            log.info("Web CheckIN Status in Failed Case -->" + webData.get("checkINStatus").toString().trim());
        }
    }
}