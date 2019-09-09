package com.darwinbox.attendance.overtime;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
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

public class TestCreditMoreThanOneCompOff extends TestBase {

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
        WorkDuration wd = new WorkDuration();
        Map<String, String> body = new HashMap<>();
        body = wd.getWorkDuration(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift, date, false, false, true);
        atb.importBackdated(body);
        /*Absent absent = new Absent()   ;
        Map<String, String> body = absent.getAbsent(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift.getShiftName(), dateTimeHelper.addDays(date, 7), isWeekoff);
        atb.importBackdated(body);
        */
        Reporter("Weekoff created","INFO");

        String starHours = dateTimeHelper.parseTime(shift.getStartTime());
        String endHours = dateTimeHelper.parseTime(shift.getEndTime()+300);

        String inTimeDate = dateTimeHelper.formatDateTo(date,"dd-MM-yyyy");
        String outTimeDate = shift.isOverNightShift() ? dateTimeHelper.formatDateTo(new Date(),"dd-MM-yyyy") : inTimeDate;

        AttendanceImportPage attendanceImportPage = new AttendanceImportPage();
        attendanceImportPage.setEmployeeId(employee.getEmployeeID());
        attendanceImportPage.setShitDate(date);
        attendanceImportPage.setInTimeDate(inTimeDate);
        attendanceImportPage.setInTime(starHours);
        attendanceImportPage.setOutTimeDate(outTimeDate);
        attendanceImportPage.setOutTime(endHours);
        attendanceImportPage.setShiftName(shift.getShiftName());
        attendanceImportPage.setWeekoff(false);
        attendanceImportPage.setPolicyName(policy.getPolicyInfo().getPolicyName());
        attendanceImportPage.setBreakDuration(dateTimeHelper.parseTime( new Random().nextInt(60)));
        atb.importBackdated(attendanceImportPage.getWorkDuration());

        long start = dateTimeHelper.parseTime(starHours);
        long end = dateTimeHelper.parseTime(endHours);
        long totalDur = end - start;
        long empOT = totalDur/60;
        Reporter("Work Duration in hours of emp on date :"+date+" is :"+empOT,"INFO");

        Reporter("Min Duration required on weekoff as per OT policy :"+overTimePolicy.getWeekoff().getMinDurationForOT(),"INFO");
        Reporter("Min Duration to credit full day compoff as per OT policy :"+overTimePolicy.getWeekoff().getMinDurationToCreditForDay(),"INFO");
        Reporter("Min Duration to credit half day compoff as per OT policy :"+overTimePolicy.getWeekoff().getDurationToCreditForHalfDay(),"INFO");

        String duration = overTimePolicy.getWeekoff().getMinDurationToCreditForDay();
        long durForFullDay = dateTimeHelper.parseTimeFromHHMMFormat(duration)/60;

        String dur = overTimePolicy.getWeekoff().getDurationToCreditForHalfDay();
        long durForHalfDay = dateTimeHelper.parseTimeFromHHMMFormat(dur)/60;
//overTimePolicy.getWeekoff().setAllowCreateMoreThan1CompOff(false);
        if(overTimePolicy.getWeekoff().isAllowCreateMoreThan1CompOff()){

            Reporter("Credit more than one compoff per day is selected","INFO");
//empOT - rest of bal hours eliglible for another half/full day compoff
            int i=0;
            if (empOT > durForHalfDay) {
                Reporter("Half day compoff earned by emp :"+(i+1), "INFO");
            }
            if (empOT > durForFullDay) {
                Reporter("Full day compoff earned by emp :"+(i+1), "INFO");
            }

        }else {

            Reporter("Credit more than one compoff per day is NOT selected","INFO");
            if (empOT > durForHalfDay && !(empOT > durForFullDay)) {
                Reporter("Emp has earned half day compoff", "INFO");
            }
            if (empOT > durForHalfDay && empOT > durForFullDay) {
                Reporter("Emp has earned full day compoff", "INFO");
            }

        }

        displaySettingsPage.navigateToLeavesPage(employee.getUserID());
        Reporter("Compoff availed by emp :"+displaySettingsPage.getCompoffBalance(),"INFO");

    }
    }
