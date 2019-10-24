package com.darwinbox.mobile.dataFetching.webData.attendance;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.MainMenuNavigationPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.AttendanceRequestLocationPage;
import com.darwinbox.mobile.pages.attendance.AttendanceSummaryPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;


import java.util.HashMap;

public class WebAttendanceRequestLocation extends TestBase {
    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    GenericHelper objgenhelper;
    AttendanceRequestLocationPage attendanceReqLocPage;

    public WebAttendanceRequestLocation(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        attendanceReqLocPage = PageFactory.initElements(driver, AttendanceRequestLocationPage.class);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
    }

    /*
    Fetching valaues in Attendance Page from Web
     */
    public HashMap getWebRequestLocation() {
        loginpage.loginAsEmployee(getData("UserName"),getData("Password"));
        objgenhelper.navigateTo("attendance/index/index/view/list");
        objgenhelper.sleep(10);
        attendanceReqLocPage.attendanceReqApplyBtnClick();
        objgenhelper.sleep(2);

        HashMap webAttReqLocData = new HashMap();

        webAttReqLocData.put("reqType", attendanceReqLocPage.getAttendanceReqType());
        webAttReqLocData.put("reqLocation", attendanceReqLocPage.getAttendanceReqLocation());
        webAttReqLocData.put("reqReason", attendanceReqLocPage.getAttendanceReqReason());
        objgenhelper.clearCookiesAndLoad();
        return webAttReqLocData;

    }
}

