package com.darwinbox.mobile.dataFetching.webData.attendance;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.MainMenuNavigationPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.AttendanceSummaryPage;
import org.openqa.selenium.support.PageFactory;
import java.util.HashMap;

public class WebAttendanceSummary extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    MainMenuNavigationPage menu;
    AttendanceSummaryPage attendanceSummaryPage;

    public void beforeMethod() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
    }

    /*
    Fetching valaues in Attendance Page from Web
     */
    public HashMap getWebAttendanceSummary() {
        loginpage.loginToApplication();
        objgenhelper.navigateTo("attendance");
        objgenhelper.sleep(10);
        HashMap webAttendanceSummaryData = new HashMap();

        webAttendanceSummaryData.put("leaveDays", attendanceSummaryPage.getLeaveDays());
        webAttendanceSummaryData.put("presentDays", attendanceSummaryPage.getPresentDays());
        webAttendanceSummaryData.put("absentDays", attendanceSummaryPage.getAbsentDays());
        webAttendanceSummaryData.put("clockInPriority", attendanceSummaryPage.getClockInPriority());
        webAttendanceSummaryData.put("shiftName", attendanceSummaryPage.getShiftName());
        webAttendanceSummaryData.put("policyName", attendanceSummaryPage.getPolicyName());
        webAttendanceSummaryData.put("weeklyOffName", attendanceSummaryPage.getWeeklyOffName());
        webAttendanceSummaryData.put("weeklyOffDesc", attendanceSummaryPage.getWeeklyOffDesc());
        objgenhelper.clearCookiesAndLoad();
        return webAttendanceSummaryData;

    }
}

