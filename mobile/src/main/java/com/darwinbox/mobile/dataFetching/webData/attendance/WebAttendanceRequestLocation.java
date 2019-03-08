package com.darwinbox.mobile.dataFetching.webData.attendance;

import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.MainMenuNavigationPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.AttendanceRequestLocationPage;
import org.openqa.selenium.support.PageFactory;


import java.util.HashMap;

public class WebAttendanceRequestLocation extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    MainMenuNavigationPage menu;
    AttendanceRequestLocationPage attReqLocPage;

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
        objgenhelper.navigateTo("/attendance/index/index/view/list");
        objgenhelper.sleep(10);
        attReqLocPage.attendanceReqApplyBtnClick();
        objgenhelper.sleep(2);

        HashMap webAttReqLocData = new HashMap();

        webAttReqLocData.put("reqType", attReqLocPage.getAttendanceReqType());
        webAttReqLocData.put("reqLocation", attReqLocPage.getAttendanceReqLocation());
        webAttReqLocData.put("reqReason", attReqLocPage.getAttendanceReqReason());
        objgenhelper.clearCookiesAndLoad();
        return webAttReqLocData;

    }
}

