package com.darwinbox.mobile.dataFetching.webData.attendance;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
//import com.darwinbox.dashboard.pageObjectRepo.generic.MainMenuNavigationPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.AttendanceSummaryPage;
import com.darwinbox.mobile.pages.attendance.AttendanceViewPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import java.util.HashMap;

public class WebAttendanceView extends TestBase {
    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    GenericHelper objgenhelper;
    AttendanceViewPage attendanceViewPage;

    public WebAttendanceView(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        attendanceViewPage = PageFactory.initElements(driver, AttendanceViewPage.class);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
    }


    /*
    Fetching valaues in Attendance Page from Web
     */
    public HashMap getWebAttendanceView() {
        loginpage.loginAsEmployee(getData("UserName"),getData("Password"));
        objgenhelper.navigateTo("/attendance/index/index/view/list");
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

