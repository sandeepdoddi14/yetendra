package com.darwinbox.mobile.dataFetching.webData.attendance;


import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.MainMenuNavigationPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.AttendanceViewPage;
import com.darwinbox.mobile.pages.attendance.CheckInShowPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class WebCheckInShow extends TestBase {
//    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    GenericHelper objgenhelper;
    CheckInShowPage checkInShowPage;

    public WebCheckInShow(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        checkInShowPage = PageFactory.initElements(driver, CheckInShowPage.class);
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
//        homepage = PageFactory.initElements(driver, HomePage.class);
    }


    /*
    Fetching valaues in Attendance Page from Web
     */
    public HashMap getWebCheckInShow(int monthFilter) {
        loginpage.loginAsEmployee(getData("UserName"),getData("Password"));
        objgenhelper.navigateTo("attendance/index/index/view/list");
        objgenhelper.sleep(10);
        checkInShowPage.checkInBtnClick();
        objgenhelper.sleep(2);
        checkInShowPage.selectMothFilter(monthFilter);

        ArrayList<String> time = new ArrayList<String>();
        ArrayList<String> description = new ArrayList<String>();
        ArrayList<String> location = new ArrayList<String>();

        if(checkInShowPage.getTime(1) != null) {
            for (int i=1;i<=checkInShowPage.getRowsCount();i++) {
                time.add(checkInShowPage.getTime(i));
                description.add(checkInShowPage.getComment(i));
                location.add(checkInShowPage.getLocation(i));
            }
            HashMap checkinData = new HashMap();
            checkinData.put("location", location);
            checkinData.put("description", description);
            checkinData.put("time", time);
            objgenhelper.clearCookiesAndLoad();
            return checkinData;
        }
        else {
            objgenhelper.clearCookiesAndLoad();
            return null;
        }
    }
}

