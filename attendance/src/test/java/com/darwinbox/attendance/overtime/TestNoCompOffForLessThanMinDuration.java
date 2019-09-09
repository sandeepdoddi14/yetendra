package com.darwinbox.attendance.overtime;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.overTime.HolidayOption;
import com.darwinbox.attendance.objects.overTime.OverTimePolicy;
import com.darwinbox.attendance.objects.overTime.WeekDayOption;
import com.darwinbox.attendance.objects.overTime.WeekendOption;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.Absent;
import com.darwinbox.attendance.objects.policy.leavedeductions.WorkDuration;
import com.darwinbox.attendance.pages.settings.AttendanceImportPage;
import com.darwinbox.attendance.pages.settings.DisplaySettingsPage;
import com.darwinbox.attendance.services.CronServices;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.OverTimePolicyService;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TestNoCompOffForLessThanMinDuration extends TestBase {
    LoginPage loginPage;
    EmployeeServices empService;
    OverTimePolicy overTimePolicy;
    WeekDayOption weekDayOption;
    WeekendOption weekendOption;
    HolidayOption holidayOption;
    OverTimePolicyService overTimePolicyService;
    DateTimeHelper dateTimeHelper;
    DisplaySettingsPage displaySettingsPage;
    CronServices cronServices;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        displaySettingsPage = new DisplaySettingsPage(driver);
        empService = new EmployeeServices();
        overTimePolicy = new OverTimePolicy();
        weekDayOption = new WeekDayOption();
        weekendOption = new WeekendOption();
        holidayOption = new HolidayOption();
        overTimePolicyService = new OverTimePolicyService();
        dateTimeHelper = new DateTimeHelper();
        cronServices = new CronServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, retryAnalyzer = TestBase.class)
    public void testOverTime(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("OverTimePolicyOptions.xlsx");
        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");

        overTimePolicy = atb.createOTPolicy();
        cronServices.runCompoffPolicyCron();

        Employee employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");

        Date date = dateTimeHelper.getPreviousDate(new Date());
        String inTimeDate = dateTimeHelper.formatDateTo(date,"dd-MM-yyyy");
        String inTime = dateTimeHelper.parseTime(shift.getStartTime()-120);
        String outTimeDate = shift.isOverNightShift() ? dateTimeHelper.formatDateTo(new Date(),"dd-MM-yyyy") : inTimeDate;
        String outTime = dateTimeHelper.parseTime(shift.getEndTime());

        AttendanceImportPage attendanceImportPage = new AttendanceImportPage();
        attendanceImportPage.setEmployeeId(employee.getEmployeeID());
        attendanceImportPage.setShitDate(date);
        attendanceImportPage.setInTimeDate(inTimeDate);
        attendanceImportPage.setInTime(inTime);
        attendanceImportPage.setOutTimeDate(outTimeDate);
        attendanceImportPage.setOutTime(outTime);
        attendanceImportPage.setShiftName(shift.getShiftName());
        attendanceImportPage.setWeekoff(false);
        attendanceImportPage.setPolicyName(policy.getPolicyInfo().getPolicyName());
        attendanceImportPage.setBreakDuration(dateTimeHelper.parseTime( new Random().nextInt(60)));
        atb.importBackdated(attendanceImportPage.getWorkDuration());

        Reporter("Min Duration required on weekday as per OT policy :"+overTimePolicy.getWeekDay().getMinDurationForOT(),"INFO");
        Reporter("Min Duration to credit half day compoff as per OT policy :"+overTimePolicy.getWeekDay().getDurationToCreditForHalfDay(),"INFO");
        Reporter("Min Duration to credit full day compoff as per OT policy :"+overTimePolicy.getWeekDay().getMinDurationToCreditForDay(),"INFO");
        Reporter("Overtime assigned on attendance for emp on WeekDay :"+(shift.getStartTime()-dateTimeHelper.parseTime(inTime))/60,"INFO");

        long empOT = (dateTimeHelper.parseTime(outTime) - shift.getEndTime()) / 60;
        String duration = overTimePolicy.getWeekDay().getMinDurationForOT();
        long minDurForOT = dateTimeHelper.parseTimeFromHHMMFormat(duration)/60;

        if (empOT <= minDurForOT)
            Reporter("Emp is not considered for overtime", "PASS");
        else
            Reporter("Check Emp durations and settings","ERROR");

        displaySettingsPage.navigateToLeavesPage(employee.getUserID());
        Reporter("Compoff availed by emp :"+displaySettingsPage.getCompoffBalance(),"INFO");

    }
}