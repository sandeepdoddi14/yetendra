package com.darwinbox.mobile.attendance;

import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobAttendanceRequestLocation;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceRequestLocation;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

public class AttendanceRequestLocationTest extends TestBase {

    private static final Logger log = Logger.getLogger(AttendanceRequestLocationTest.class);
    HomePage homepage;
    LoginPage loginpage;
    WaitHelper objWaitHelper;
    RightMenuOptionsPage rightMenuOption;
    CommonAction commonAction;

    MobAttendanceRequestLocation mobAttReqLoc ;
    WebAttendanceRequestLocation webAttReqLoc ;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
        System.out.println(" Master Sheet record obtained ");
    }

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        homepage = PageFactory.initElements(driver, HomePage.class);
        rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        webAttReqLoc = PageFactory.initElements(driver, WebAttendanceRequestLocation.class);
        mobAttReqLoc = PageFactory.initElements(driver, MobAttendanceRequestLocation.class);
        }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void getAttendanceReqLocationAPICheck(Map<String, String> data) throws Exception {

        HashMap webData = webAttReqLoc.getWebRequestLocation();
        log.info("********************* Web Data Fetched **********************");
        log.info("Web Data -> "+webData);
        HashMap mobData = mobAttReqLoc.getMobAttendanceRequestLocation();
        log.info("********************* Mobile Data Fetched **********************");
        log.info("Mob Data -> "+mobData);

        //Mobile Response data
        ArrayList<String> mobRequestType = (ArrayList<String>) mobData.get("reqType");
        ArrayList<String> mobRequestLocation = (ArrayList<String>) mobData.get("reqLocation");
//        ArrayList<String> mobHrs = (ArrayList<String>) mobData.get("hrs");
//        ArrayList<String> mobMin = (ArrayList<String>) mobData.get("mins");
//
        //Web Response data
        LinkedList<String> webRequestType = (LinkedList<String>) webData.get("reqType");
        LinkedList<String> webRequestLocation = (LinkedList<String>) webData.get("reqLocation");

        try {
            Assert.assertEquals(mobRequestType.size(), webRequestType.size());
            log.info("Total Mobile Request Types Count in Pass Case -->" + mobRequestType.size());
            log.info("Total Web Request Types Count in Pass Case -->" + webRequestType.size());
            int count = 0;
            while (count < mobRequestType.size()) {
                Assert.assertTrue(mobRequestType.contains(webRequestType.get(count)), webRequestType.get(count) + " exists in mobile API");
                count++;
            }
            log.info("Total Mobile Request Types in Pass Case -->" + mobRequestType);
            log.info("Total Web Request Types in Pass Case -->" + webRequestType);
            Reporter("Pass at Request Types Count","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Request Types Count","Fail");
            log.info("Total Mobile Request Types in Failed Case -->" + mobRequestType);
            log.info("Total Web Request Types in Failed Case -->" + webRequestType);
        }

        try {
            Assert.assertEquals(mobRequestLocation.size(), webRequestLocation.size());
            log.info("Total Mobile Request Location Count in Pass Case -->" + mobRequestLocation.size());
            log.info("Total Web Request Location Count in Pass Case -->" + webRequestLocation.size());
            int count = 0;
            while (count < mobRequestLocation.size()) {
                Assert.assertTrue(mobRequestLocation.contains(webRequestLocation.get(count)), webRequestLocation.get(count) + " exists in mobile API");
                count++;
            }
            log.info("Total Mobile Request Location in Pass Case -->" + mobRequestLocation);
            log.info("Total Web Request Location in Pass Case -->" + webRequestLocation);
            Reporter("Pass at Request Location Count","Pass");
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter("Failed at Request Location Count","Fail");
            log.info("Total Mobile Request Location in Failed Case -->" + mobRequestLocation);
            log.info("Total Web Request Location in Failed Case -->" + webRequestLocation);
        }
    }
}