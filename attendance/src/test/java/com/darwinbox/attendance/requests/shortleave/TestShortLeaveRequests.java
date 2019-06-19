package com.darwinbox.attendance.requests.shortleave;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.objects.policy.others.RequestFlags;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestShortLeaveRequests extends TestBase {

    LoginPage loginPage;
    GenericHelper genHelper;
    DateTimeHelper dateHelper;
    EmployeeServices empService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        genHelper = new GenericHelper(driver);
        empService = new EmployeeServices();
        dateHelper = new DateTimeHelper();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Absent,LeaveDeduction", retryAnalyzer = TestBase.class)
    public void testRequestsforFutureDays(Map<String, String> testData) {

        String title = " Validate ClockIn requests ";

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendanceTestBase atb = AttendanceTestBase.getObject();

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("ShiftName"));

        Date date = new Date();
        int days = Integer.parseInt(testData.get("Days"));
        boolean isFuture = testData.get("Future").equalsIgnoreCase("yes");

        LeaveDeductionsBase.DAYSTATUS day = LeaveDeductionsBase.DAYSTATUS.valueOf(testData.get("CurrentDay"));

        boolean isholiday = day.equals(LeaveDeductionsBase.DAYSTATUS.HOLIDAY);
        boolean isboth = day.equals(LeaveDeductionsBase.DAYSTATUS.WH);
        boolean isWeekoff = day.equals(LeaveDeductionsBase.DAYSTATUS.WEEKOFF);
        boolean empty = day.equals(LeaveDeductionsBase.DAYSTATUS.EMPTY);

        isholiday = isholiday || isboth;
        isWeekoff = isWeekoff || isboth;

        String weekoffId = atb.getWeeklyOff("None");

        if (isWeekoff ) {
            weekoffId = atb.getWeeklyOff("All");
            Reporter("Testing for WeeklyOff", "INFO");
        }

        if (isholiday){
            atb.createHoliday(date);
            Reporter("Testing for Holiday", "INFO");
        }

        int location = RequestFlags.Location.valueOf(testData.get("Location")).ordinal();

        Employee employee = empService.createAnEmployee(true);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(),shift,policy,weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");

        Map<String,String> headers = new HashMap<>();
        headers.put("Cookie",employee.getPhpSessid());
        headers.put("X-Requested-With", "XMLHttpRequest");

        long mins = dateHelper.getMins(date)-2;
        date = dateHelper.addDays(date,days );

        if (isFuture)
            mins += 5;

        String response = atb.applyClockin(headers,(int)mins,location, dateHelper.formatDateTo(date, "dd-MM-yyyy")).get("error").toString();

        Reporter(" Response : " + response, "INFO");

        if (isFuture) {
            Reporter(" Validating error message for ", "INFO");
            Assert.assertTrue(response.contains(atb.clockin_future), "Future ClockIn request Warning Message doesnt appear");
            return;
        }

        RequestFlags flags = policy.getReqFlags();

        boolean validate = true;

        boolean proceed = (isWeekoff && !flags.isReq_weekoff());

        if (proceed) {
            Assert.assertTrue(response.contains(atb.clockin_weekOffs), "ClockIn request Warning Message for doesn't appears");
            validate = false;
        }

        proceed = (isholiday && !flags.isReq_holiday());

        if (proceed) {
            Assert.assertTrue(response.contains(atb.clockin_holiday), "ClockIn request Warning Message for Holiday doesn't appears");
            validate = false;
        }

        if ( validate ) {
            Assert.assertTrue(response.contains(atb.clockin_Success), "ClockIn request Success Message for Holiday appears");
        }

        String id = atb.getUserAttendanceId(employee);

        atb.approveAttendance(id);




    }

}