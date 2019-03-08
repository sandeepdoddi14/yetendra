package com.darwinbox.mobile.dataFetching.webData.attendance;


import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.MainMenuNavigationPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.mobile.pages.attendance.CheckInShowPage;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;

public class WebCheckInShow extends TestBase {
    LoginPage loginpage;
    GenericHelper objgenhelper;
    HomePage homepage;
    MainMenuNavigationPage menu;
    CheckInShowPage checkInShowPage;

    public void beforeMethod() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        objgenhelper = PageFactory.initElements(driver, GenericHelper.class);
    }

    /*
    Fetching valaues in Attendance Page from Web
     */
    public HashMap getWebCheckInShow() {
        loginpage.loginToApplication();
        objgenhelper.navigateTo("attendance");
        objgenhelper.sleep(10);
        checkInShowPage.checkInBtnClick();

        ArrayList<String> time = new ArrayList<String>();
        ArrayList<String> description = new ArrayList<String>();
        ArrayList<String> location = new ArrayList<String>();

        time.add(checkInShowPage.getTime(1));
        description.add(checkInShowPage.getComment(1));
        location.add(checkInShowPage.getLocation(1));

        HashMap checkinData = new HashMap();
        checkinData.put("location", location);
        checkinData.put("description", description);
        checkinData.put("time", time);

        objgenhelper.clearCookiesAndLoad();
        return checkinData;
    }
}

