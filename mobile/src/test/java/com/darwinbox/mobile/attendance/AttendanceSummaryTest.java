package com.darwinbox.mobile.attendance;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobileAttendanceSummary;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceSummary;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;
import java.util.HashMap;

public class AttendanceSummaryTest extends TestBase {

    MobileAttendanceSummary mobAttendanceSummary = new MobileAttendanceSummary();
    WebAttendanceSummary webAttendanceSummary = new WebAttendanceSummary();

    @Test
    public void getAttendanceSummaryAPICheck() {
        HashMap webData = webAttendanceSummary.getWebAttendanceSummary();
        log.info("********************* Web Data Fetched **********************");
        log.info("Web Data -> "+webData);

        HashMap mobData = mobAttendanceSummary.getMobAttendanceSummary();
        log.info("********************* Mobile Data Fetched **********************");
        log.info("Mob Data -> "+mobData);

        //Mobile Response data
        String mobLeaveDays = mobData.get("leaveDays").toString();
        String mobPresentDays = mobData.get("presentDays").toString();
        String mobAbsentDays = mobData.get("absentDays").toString();
        String mobClockInPriority = mobData.get("clockInPriority").toString();
        String mobShiftName = mobData.get("shiftName").toString();
        String mobPolicyName = mobData.get("policyName").toString();
        String mobWeeklyOffName = mobData.get("weeklyOffName").toString();
        String mobWeeklyOffDesc = mobData.get("weeklyOffDesc").toString();

        //Web Elements data
        String webLeaveDays = webData.get("leaveDays").toString();
        String webPresentDays = mobData.get("presentDays").toString();
        String webAbsentDays = mobData.get("absentDays").toString();
        String webClockInPriority = mobData.get("clockInPriority").toString();
        String webShiftName = mobData.get("shiftName").toString();
        String webPolicyName = mobData.get("policyName").toString();
        String webWeeklyOffName = mobData.get("weeklyOffName").toString();
        String webWeeklyOffDesc = mobData.get("weeklyOffDesc").toString();

        try {
            Assert.assertEquals(mobLeaveDays, webLeaveDays);
            log.info("Mobile Leave Days Count in Pass Case -->"+mobLeaveDays);
            log.info("Web Leave Days Count in Pass Case -->"+webLeaveDays);
        } catch (AssertionError e) {
            e.printStackTrace();
            Reporter.log("Failed at Leave Days Count");
            log.info("Mobile Leave Days Count in Failed Case -->"+mobLeaveDays);
            log.info("Web Leave Days Count in Failed Case -->"+webLeaveDays);
        }

        try {
            Assert.assertEquals(mobPresentDays, webPresentDays);
            log.info("Mobile Present Days Count in Pass Case -->"+mobPresentDays);
            log.info("Web Present Days Count in Pass Case -->"+webPresentDays);
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at Present Days Count");
            log.info("Mobile Present Days Count in Failed Case -->"+mobPresentDays);
            log.info("Web Present Days Count in Failed Case -->"+webPresentDays);
        }

        try {
            Assert.assertEquals(mobAbsentDays, webAbsentDays);
            log.info("Mobile Absent Days Count in Pass Case -->"+mobAbsentDays);
            log.info("Web Absent Days Count in Pass Case -->"+webAbsentDays);
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at Absent Days Count");
            log.info("Mobile Absent Days Count in Failed Case -->"+mobAbsentDays);
            log.info("Web Absent Days Count in Failed Case -->"+webAbsentDays);
        }

        try {
            Assert.assertEquals(mobClockInPriority, webClockInPriority);
            log.info("Mobile ClockInPriority in Pass Case -->"+mobClockInPriority);
            log.info("Web ClockInPriority in Pass Case -->"+webClockInPriority);
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at ClockInPriority");
            log.info("Mobile ClockInPriority in Failed Case -->"+mobClockInPriority);
            log.info("Web ClockInPriority in Failed Case -->"+webClockInPriority);
        }

        try {
            Assert.assertEquals(mobShiftName, webShiftName);
            log.info("Mobile Shift Name in Pass Case -->"+mobShiftName);
            log.info("Web Shift Name in Pass Case -->"+webShiftName);
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at Shift Name");
            log.info("Mobile Shift Name in Failed Case -->"+mobShiftName);
            log.info("Web Shift Name in Failed Case -->"+webShiftName);
        }
/**********************************************************************/
        /*
        Shift Times Comparision pending
        */
/**********************************************************************/
        try {
            Assert.assertEquals(mobPolicyName, webPolicyName);
            log.info("Mobile Policy Name in Pass Case -->"+mobPolicyName);
            log.info("Web Policy Name in Pass Case -->"+webPolicyName);
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at Policy Name");
            log.info("Mobile Policy Name in Failed Case -->"+mobPolicyName);
            log.info("Web Policy Name in Failed Case -->"+webPolicyName);
        }

        try {
            Assert.assertEquals(mobWeeklyOffName, webWeeklyOffName);
            log.info("Mobile WeeklyOff Name in Pass Case -->"+mobWeeklyOffName);
            log.info("Web WeeklyOff Name in Pass Case -->"+webWeeklyOffName);
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at WeeklyOff Name");
            log.info("Mobile WeeklyOff Name in Failed Case -->"+mobWeeklyOffName);
            log.info("Web WeeklyOff Name in Failed Case -->"+webWeeklyOffName);
        }

        try {
            Assert.assertEquals(mobWeeklyOffDesc, webWeeklyOffDesc);
            log.info("Mobile WeeklyOff Description in Pass Case -->"+mobWeeklyOffDesc);
            log.info("Web WeeklyOff Description in Pass Case -->"+webWeeklyOffDesc);
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at WeeklyOff Description");
            log.info("Mobile WeeklyOff Description in Failed Case -->"+mobWeeklyOffDesc);
            log.info("Web WeeklyOff Description in Failed Case -->"+webWeeklyOffDesc);
        }

    }
}