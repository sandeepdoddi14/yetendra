package com.darwinbox.attendance.overtime;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.overTime.HolidayOption;
import com.darwinbox.attendance.objects.overTime.OverTimePolicy;
import com.darwinbox.attendance.objects.overTime.WeekDayOption;
import com.darwinbox.attendance.objects.overTime.WeekendOption;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
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
import java.util.Map;

public class TestCompoffPerWeekHours extends TestBase {

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
    // cronServices.runCompoffPolicyCron();
        overTimePolicy.getHoliday().setMaxOTPerWeek("40:00");
        overTimePolicy.getHoliday().setRestrictionOnAccrual(true);
        if(overTimePolicy.getHoliday().isRestrictionOnAccrual())
            Reporter("One Time compoff is selected","INFO");
        else
            Reporter("Accrual compoff is selected","INFO");

        String maxDuration=  overTimePolicy.getHoliday().getMaxOTPerWeek();
        long maxOTPerWeek = dateTimeHelper.parseTimeFromHHMMFormat(maxDuration)/60;
        Reporter("Max OT considered per week, as per policy :"+maxOTPerWeek,"INFO");

        String duration = overTimePolicy.getHoliday().getMinDurationForOT();
        long minDurForOT = dateTimeHelper.parseTimeFromHHMMFormat(duration)/60;
        Reporter("Min Duration to be considered for overtime :"+minDurForOT,"INFO");

        String dur = overTimePolicy.getHoliday().getDurationToCreditForHalfDay();
        long durForHalfDay = dateTimeHelper.parseTimeFromHHMMFormat(dur)/60;
        Reporter("Min Duration to be considered for Half Day compoff :"+durForHalfDay,"INFO");

        String d = overTimePolicy.getHoliday().getMinDurationToCreditForDay();
        long durForFullDay = dateTimeHelper.parseTimeFromHHMMFormat(d)/60;
        Reporter("Min Duration to be considered for Full Day Compoff :"+durForFullDay,"INFO");


        Employee employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");

        Date date;
        date = dateTimeHelper.addDays(new Date(),-5);
        for (int i = 1; i <= 5; i++) {
            atb.createHoliday(date);
            date = dateTimeHelper.getNextDate(date);
        }
        Reporter("Holidays Created", "INFO");
        date = dateTimeHelper.addDays(new Date(),-5);
        long overTime = 0;
        int fullDayCompoff = 0;
        int halfDayCompoff=0;
        for (int i = 1; i <= 5; i++) {
            String starHours = dateTimeHelper.parseTime(shift.getStartTime());
            String endHours = dateTimeHelper.parseTime(shift.getEndTime() );

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
            date = dateTimeHelper.getNextDate(date);

            long start = dateTimeHelper.parseTime(starHours);
            long end = dateTimeHelper.parseTime(endHours);
            long totalDur = end - start;
            Reporter("Over Time on Holiday  :"+date+" is :"+totalDur/60,"INFO");
            overTime = totalDur + overTime;
            Reporter("Updated OverTime of emp :"+(overTime/60),"INFO");

            if((overTime/60)<=maxOTPerWeek) {
                if (totalDur > durForHalfDay && !(totalDur > durForFullDay)) {
                    halfDayCompoff = 1+ halfDayCompoff;
                    Reporter("Updated Half day compoff earned by emp :" + halfDayCompoff, "INFO");
                }
                if (totalDur > durForHalfDay && totalDur > durForFullDay) {
                    fullDayCompoff = 1+ fullDayCompoff;
                    Reporter("Updated Full day compoff earned by emp :" + fullDayCompoff, "INFO");
                }
            }
          //total compoff earned day by day
            if((overTime/60)>maxOTPerWeek) {
                overTime = maxOTPerWeek;
                Reporter("OverTime of emp considered upto:" + overTime, "INFO");
                break;
            }

        }


        displaySettingsPage.navigateToLeavesPage(employee.getUserID());
        Reporter("Compoff availed by emp :"+displaySettingsPage.getCompoffBalance(),"INFO");


        }






    }
