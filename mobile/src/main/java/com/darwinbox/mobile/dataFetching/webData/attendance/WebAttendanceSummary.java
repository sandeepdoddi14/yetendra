package com.darwinbox.mobile.dataFetching.webData.attendance;

//import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.AttendanceSummaryPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class WebAttendanceSummary extends TestBase {
//    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    GenericHelper objgenhelper;
    AttendanceSummaryPage attendanceSummaryPage;

    public WebAttendanceSummary(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        attendanceSummaryPage = PageFactory.initElements(driver, AttendanceSummaryPage.class);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
//        homepage = PageFactory.initElements(driver, HomePage.class);
    }

    /*
    Fetching valaues in Attendance Page from Web
     */
    public HashMap getWebAttendanceSummary() {
        loginpage.loginAsEmployee(getData("UserName"),getData("Password"));
        objgenhelper.navigateTo("attendance/index/index/view/list");

        ArrayList<String> heading = new ArrayList<String>();
        ArrayList<String> status = new ArrayList<String>();
        ArrayList<String> description = new ArrayList<String>();

        objgenhelper.sleep(10);
        HashMap webAttendanceSummaryData = new HashMap();
        webAttendanceSummaryData.put("leaveDays", attendanceSummaryPage.getLeaveDays());
        webAttendanceSummaryData.put("presentDays", attendanceSummaryPage.getPresentDays());
        webAttendanceSummaryData.put("absentDays", attendanceSummaryPage.getAbsentDays());
        webAttendanceSummaryData.put("clockInPriority", attendanceSummaryPage.getClockInPriority());
        webAttendanceSummaryData.put("shiftName", attendanceSummaryPage.getShiftName());
        webAttendanceSummaryData.put("shiftDescBeginTime", attendanceSummaryPage.getStartShiftTimings());
        webAttendanceSummaryData.put("shiftDescOutTime", attendanceSummaryPage.getEndShiftTimings());
        webAttendanceSummaryData.put("policyName", attendanceSummaryPage.getPolicyName());
        webAttendanceSummaryData.put("weeklyOffName", attendanceSummaryPage.getWeeklyOffName());
        webAttendanceSummaryData.put("weeklyOffDesc", attendanceSummaryPage.getWeeklyOffDesc());
        webAttendanceSummaryData.put("checkINStatus", attendanceSummaryPage.getCheckINStatus());
        attendanceSummaryPage.clickAttendancePolicy();
        for (int i=1;i<=attendanceSummaryPage.getRowsCount();i++) {
            heading.add(attendanceSummaryPage.getPolicyAttribute(i));
            status.add(attendanceSummaryPage.getPolicyStatus(i));
            description.add(attendanceSummaryPage.getPolicyDescription(i));
        }
        webAttendanceSummaryData.put("heading", heading);
        webAttendanceSummaryData.put("status", status);
        webAttendanceSummaryData.put("desc", description);
        objgenhelper.clearCookiesAndLoad();
        return webAttendanceSummaryData;
    }
}

