package com.darwinbox.mobile.attendance;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.attendance.MobAttendanceRequestLocation;
import com.darwinbox.mobile.dataFetching.webData.attendance.WebAttendanceRequestLocation;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceRequestLocationTest extends TestBase {

    private static final Logger log = Logger.getLogger(AttendanceRequestLocationTest.class);
    MobAttendanceRequestLocation mobAttReqLoc = new MobAttendanceRequestLocation();
    WebAttendanceRequestLocation webAttReqLoc = new WebAttendanceRequestLocation();

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass( ).getName( ));
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void getAttendanceReqLocationAPICheck(Map<String,String> data) {

        HashMap webData = webAttReqLoc.getWebAttendanceSummary();
        log.info("********************* Web Data Fetched **********************");
        log.info("Web Data -> "+webData);
        HashMap mobData = mobAttReqLoc.getMobAttendanceRequestLocation();
        log.info("********************* Mobile Data Fetched **********************");
        log.info("Mob Data -> "+mobData);

        //Mobile Response data
        ArrayList<JSONObject> mobRequestType = (ArrayList<JSONObject>) mobData.get("request_type");
        ArrayList<JSONObject> mobRequestLocation = (ArrayList<JSONObject>) mobData.get("locations");
        ArrayList<JSONObject> mobHrs = (ArrayList<JSONObject>) mobData.get("hrs");
        ArrayList<JSONObject> mobMin = (ArrayList<JSONObject>) mobData.get("mins");

        //Web Response data
        ArrayList webRequestType = (ArrayList) webData.get("reqType");
        ArrayList webRequestLocation = (ArrayList) webData.get("reqLocation");

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
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at Request Types Count");
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
        } catch (AssertionError e) {
//            e.printStackTrace();
            Reporter.log("Failed at Request Location Count");
            log.info("Total Mobile Request Location in Failed Case -->" + mobRequestLocation);
            log.info("Total Web Request Location in Failed Case -->" + webRequestLocation);
        }
    }
}