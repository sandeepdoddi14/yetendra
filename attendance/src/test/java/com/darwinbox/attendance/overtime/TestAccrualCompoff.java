package com.darwinbox.attendance.overtime;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.overTime.HolidayOption;
import com.darwinbox.attendance.objects.overTime.OverTimePolicy;
import com.darwinbox.attendance.objects.overTime.WeekDayOption;
import com.darwinbox.attendance.objects.overTime.WeekendOption;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
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

public class TestAccrualCompoff extends TestBase {

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

        Date date;


        date = dateTimeHelper.addDays(new Date(),-5);
        for (int i = 1; i <= 4; i++) {
            atb.createHoliday(date);
            date = dateTimeHelper.getNextDate(date);
        }
        Reporter("Holidays Created", "INFO");
        date = dateTimeHelper.addDays(new Date(),-5);
        long totalDuration = 0;
        for (int i = 1; i <= 4; i++) {
            String starHours = dateTimeHelper.parseTime(shift.getStartTime());
            String endHours = dateTimeHelper.parseTime(shift.getEndTime()-180);

            AttendanceImportPage attendanceImportPage = new AttendanceImportPage();
            attendanceImportPage.setEmployeeId(employee.getEmployeeID());
            attendanceImportPage.setShitDate(date);
            attendanceImportPage.setInTimeDate(dateTimeHelper.formatDateTo(date, "dd-MM-yyyy"));
            attendanceImportPage.setInTime(starHours);
            attendanceImportPage.setOutTimeDate(dateTimeHelper.formatDateTo(date, "dd-MM-yyyy"));
            attendanceImportPage.setOutTime(endHours);
            attendanceImportPage.setShiftName(shift.getShiftName());
            attendanceImportPage.setWeekoff(false);
            attendanceImportPage.setPolicyName(policy.getPolicyInfo().getPolicyName());
            attendanceImportPage.setBreakDuration("00:00:00");
            atb.importBackdated(attendanceImportPage.getWorkDuration());

            long start = dateTimeHelper.parseTime(starHours);
            long end = dateTimeHelper.parseTime(endHours);
            long totalDur = end - start;
            Reporter("Work Duration in hours of emp on date :"+date+" is :"+totalDur/60,"INFO");
            totalDuration = totalDur + totalDuration;
            date = dateTimeHelper.getNextDate(date);
   /*Given wd of emp does not exceed half/full compoff limit , for each of given dates*/
        }
        long totalHours = (totalDuration)/60;

        if(overTimePolicy.getHoliday().isRestrictionOnAccrual())
            Reporter("One Time compoff is selected","INFO");
        else
            Reporter("Accrual compoff is selected","INFO");

       Reporter("Min duration for accrual, as per policy :"+overTimePolicy.getHoliday().getMinDurationForAccrue(),"INFO");
       Reporter("Min duration to credit full day compoff, as per policy :"+overTimePolicy.getHoliday().getMinDurationToCreditForDay(),"INFO");

        String dur = overTimePolicy.getHoliday().getMinDurationToCreditForDay();
        long durForFullDay = dateTimeHelper.parseTimeFromHHMMFormat(dur)/60;

        Reporter("Total compoffs accrued by emp are :"+(totalHours/durForFullDay),"INFO");

        displaySettingsPage.navigateToLeavesPage(employee.getUserID());
        Reporter("Compoff availed by emp :"+displaySettingsPage.getCompoffBalance(),"INFO");


      /* long a= totalHours/durForFullDay;
       long b= Long.parseLong(displaySettingsPage.getCompoffBalance());

       if(a==b)
           Reporter("Accrual Compoff is as expected","PASS");
       else
           Reporter("Accrual Compoff is NOT as expected","FAIL");
*/
    }
    }
