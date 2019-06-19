package com.darwinbox.attendance.emails;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.*;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.services.*;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.mail.Message;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestClockInReminder extends TestBase {

    LoginPage loginPage;
    GenericHelper genHelper;
    DateTimeHelper dateHelper;
    EmployeeServices empService;
    private static Employee employee;
    private static Date date = null;

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
    public void testClockInReminder(Map<String, String> testData) {

        String title = " Test Clockin Reminder ";

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), " Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), " Switch to Admin Unsuccessfull ");

        AttendanceTestBase atb = AttendanceTestBase.getObject();

        AttendancePolicy policy = atb.getAttendancePolicy("Default");
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");

        if(employee == null ) {
            atb.changeDate(new Date());
            employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
            date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
            Map<String, String> data = new HashMap<>();
            data.put("ClockIn", "yes");
            empService.updateProfileSettings(employee, data);
            Reporter("Employee profile updated " + employee.getUserID(), "INFO");
        }

      //  timestamp = dateHelper.getPreviousDate(new Date());
        atb.changeDate(date);

        atb.assignPolicyAndShift(employee.getUserID(), dateHelper.formatDateTo(date, "yyyy-MM-dd"), shift, policy, weekoffId);

        int mins = shift.getStartTime() % 60;
        int hrs = shift.getStartTime() / 60;

        int quarter = ((mins/15) + 1) * 15;

        if ( (mins % 15) == 0)
            quarter --;

        int total = ( hrs*60 ) + quarter;

        date =  dateHelper.setTime(new Date(),total  );

        atb.changeDate(date);
        atb.runClockInreminder();

        MailService msrvc = MailService.getObject();
        Message msgs[] = msrvc.getMails("Clockin reminder", "shift time has begun but we noticed that you have not clocked in yet");

        boolean match = false;

        for (Message msg : msgs) {

                try {
                    match = msg.getContent().toString().replaceAll("\\<.*?>", "").trim().contains(employee.getFirstName());
                    if (match)
                        break;
                } catch (Exception e) { }
        }

        if (match)
            Reporter("Clock in reminder triggered for shift : " + dateHelper.parseTime(shift.getStartTime()) + " - " + dateHelper.parseTime(shift.getEndTime()), "PASS");
        else
            Reporter("Clock in reminder triggered for shift : " + dateHelper.parseTime(shift.getStartTime()) + " - " + dateHelper.parseTime(shift.getEndTime()), "FAIL");

        date = dateHelper.getNextDate(date);
        genHelper.sleep(3);
    }
}