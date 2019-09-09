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

public class TestAccrualCreditMoreThanOneCompoff extends TestBase {

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

        date = dateTimeHelper.addDays(new Date(),-3);
        for (int i = 1; i <= 2; i++) {
            WorkDuration wd = new WorkDuration();
            Map<String, String> body = new HashMap<>();
            body = wd.getWorkDuration(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift, date, false, false, true);
            atb.importBackdated(body);
            date = dateTimeHelper.getNextDate(date);
        }
        overTimePolicy.getWeekoff().setMinDurationForAccrue("2:00");
        overTimePolicy.getWeekoff().setMinDurationToCreditForDay("4:00");

        Reporter("Min duration to credit full day compoff, as per policy :"+overTimePolicy.getWeekoff().getMinDurationToCreditForDay(),"INFO");
        Reporter("Min duration for accrual, as per policy :"+overTimePolicy.getWeekoff().getMinDurationForAccrue(),"INFO");

        if(overTimePolicy.getWeekoff().isAllowCreateMoreThan1CompOff())
            Reporter("Credit more than one compoff per day is selected", "INFO");
        else
            Reporter("Credit more than one compoff per day is NOT selected", "INFO");

        if(overTimePolicy.getWeekoff().isRestrictionOnAccrual())
            Reporter("One Time compoff is selected","INFO");
        else
            Reporter("Accrual compoff is selected","INFO");

        String duration = overTimePolicy.getWeekoff().getMinDurationToCreditForDay();
        long durForFullDay = dateTimeHelper.parseTimeFromHHMMFormat(duration) / 60;

        date = dateTimeHelper.addDays(new Date(),-3);
        long totalDuration = 0;
        long compoff=0;
        long accrual=0;
        for (int i = 1; i <= 2; i++) {
            String starHours = dateTimeHelper.parseTime(shift.getStartTime());
            String endHours = dateTimeHelper.parseTime(shift.getEndTime()+120);

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
            long empOT = totalDur/60;
            Reporter("Work Duration in hours of emp on date :"+date+" is :"+empOT,"INFO");
            totalDuration = totalDur + totalDuration;
            date = dateTimeHelper.getNextDate(date);

            accrual=(empOT/durForFullDay)+accrual;

            if (empOT > durForFullDay) {
                compoff =  empOT/durForFullDay+compoff;
                Reporter("Total Updated fullday compoff emp earned :"+compoff, "INFO");
                if(accrual>=durForFullDay){
                    Reporter("Total Updated fullday compoff including accrual emp earned :"+(compoff+1), "INFO");
                }
            }
        }

        displaySettingsPage.navigateToLeavesPage(employee.getUserID());
        Reporter("Compoff availed by emp :"+displaySettingsPage.getCompoffBalance(),"INFO");

      /*  long a= compoff;
        long b= Long.parseLong(displaySettingsPage.getCompoffBalance());

        if(a==b)
            Reporter("Accrual and Credit more than one compoff is as expected","PASS");
        else
            Reporter("Accrual and Credit more than one compoff is NOT as expected","FAIL");*/
    }
}
