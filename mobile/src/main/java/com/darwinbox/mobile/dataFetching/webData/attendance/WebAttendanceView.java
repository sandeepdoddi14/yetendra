package com.darwinbox.mobile.dataFetching.webData.attendance;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.MainMenuNavigationPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.AttendanceViewPage;
import org.openqa.selenium.support.PageFactory;
import java.util.HashMap;

public class WebAttendanceView extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    MainMenuNavigationPage menu;
    AttendanceViewPage attendanceViewPage;

    public void beforeMethod() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
    }

    /*
    Fetching valaues in Attendance Page from Web
     */
    public HashMap getWebAttendanceView() {
        loginpage.loginToApplication();
        objgenhelper.navigateTo("attendance/index/index/view/list");
        objgenhelper.sleep(10);
        attendanceViewPage.clickDateToChangeOrder();
        int rowsCount = attendanceViewPage.getCalendarRowCount();
        HashMap webAttendanceViewData = new HashMap();
            for (int i=1;i<=rowsCount;i++) {
            HashMap webAttendanceViewRowData = new HashMap();
            webAttendanceViewRowData.put("inTime", attendanceViewPage.getInTime(i));
            webAttendanceViewRowData.put("outTime", attendanceViewPage.getOutTime(i));
            webAttendanceViewRowData.put("workDuration", attendanceViewPage.getWorkDuration(i));
            webAttendanceViewRowData.put("status", attendanceViewPage.getStatus(i));
            webAttendanceViewData.put(i,webAttendanceViewRowData);
        }
            objgenhelper.clearCookiesAndLoad();
            return webAttendanceViewData;
    }
}

