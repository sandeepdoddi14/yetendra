package com.darwinbox.attendance.reports.WorkDurationSummary;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.AttendanceSettingsPage;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.Absent;
import com.darwinbox.attendance.pages.settings.AttendanceImportPage;
import com.darwinbox.attendance.pages.settings.DisplaySettingsPage;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.AttendanceSettingsServices;
import com.darwinbox.attendance.services.settings.ReportsDashboardServices;
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

public class TestSinglePunchAbsentLeave extends TestBase {
    LoginPage loginPage;
    ReportsDashboardServices reportsDashboardServices;
    Reports reports;
    DateTimeHelper dateTimeHelper;
    EmployeeServices employeeServices;
    DisplaySettingsPage displaySettingsPage;
    Date date;
    AttendanceSettingsServices attendanceSettingsServices;
    AttendanceSettingsPage attendanceSettingsPage;
    boolean isWeekoff;
    boolean isPresent;
    boolean isLeaves;
    boolean isHoliday;
    boolean isSinglePunchAbsent;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        reportsDashboardServices = new ReportsDashboardServices();
        reports = new Reports();
        dateTimeHelper = new DateTimeHelper();
        employeeServices = new EmployeeServices();
        displaySettingsPage = new DisplaySettingsPage(driver);
        date = new Date();
        attendanceSettingsPage = new AttendanceSettingsPage();
        attendanceSettingsServices = new AttendanceSettingsServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, retryAnalyzer = TestBase.class)
    public void testWorkDuration(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("CommonSettings.xlsx");
        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("All");

        String leaveToApply = testData.get("ApplyLeave");
        String leaveid = atb.getLeaveId(leaveToApply);

        String dayStatus = testData.get("DayStatus");

        Employee employee = employeeServices.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");

        date = dateTimeHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
        Date date1 = dateTimeHelper.getFirstDateOfNextMonth(date);
        isWeekoff = dayStatus.contains("Weekoff");
        isPresent = dayStatus.contains("Present");
        isLeaves = dayStatus.contains("Leave");
        isHoliday = dayStatus.contains("Holiday");
        isSinglePunchAbsent = dayStatus.contains("SinglePunchAbsent");

        if (isSinglePunchAbsent) {

            for (int j = 1; j <= 6; j++) {

                String inTime = dateTimeHelper.parseTime(shift.getStartTime());

                AttendanceImportPage attendanceImportPage = new AttendanceImportPage();
                attendanceImportPage.setEmployeeId(employee.getEmployeeID());
                attendanceImportPage.setShitDate(date1);
                attendanceImportPage.setInTimeDate(dateTimeHelper.formatDateTo(date1, "dd-MM-yyyy"));
                attendanceImportPage.setInTime(inTime);
                attendanceImportPage.setOutTimeDate(dateTimeHelper.formatDateTo(date1, "dd-MM-yyyy"));
                attendanceImportPage.setOutTime(inTime);
                attendanceImportPage.setShiftName(shift.getShiftName());
                attendanceImportPage.setWeekoff(false);
                attendanceImportPage.setPolicyName(policy.getPolicyInfo().getPolicyName());
                attendanceImportPage.setBreakDuration("00:00:00");
                atb.importBackdated(attendanceImportPage.getWorkDuration());
                date1 = dateTimeHelper.getNextDate(date1);
            }
        }

        if (isLeaves) {
            date1 = dateTimeHelper.getFirstDateOfNextMonth(date);
            atb.applyLeave(date1, employee, leaveid, false, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date1, 1), employee, "unpaid", false, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date1, 2), employee, leaveid, true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date1, 3), employee, "unpaid", true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date1, 4), employee, leaveid, true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date1, 4), employee, leaveid, false, true, true);
            atb.applyLeave(dateTimeHelper.addDays(date1, 5), employee, "unpaid", true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date1, 5), employee, leaveid, false, true, true);
            Reporter("Created Leaves", "INFO");
        }
        if (isHoliday) {
            date1 = dateTimeHelper.getFirstDateOfNextMonth(date);
            for (int i = 1; i <= 6; i++) {
                atb.createHoliday(date1);
                date1=dateTimeHelper.getNextDate(date1);
            }
            Reporter("Holidays Created", "INFO");
        }

        if(isWeekoff) {
            date1 = dateTimeHelper.getFirstDateOfNextMonth(date);
            for (int j = 1; j <= 6; j++) {
                Absent absent = new Absent();
                Map<String, String> body = absent.getAbsent(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift.getShiftName(), date1, isWeekoff);
                atb.importBackdated(body);
                date1 = dateTimeHelper.getNextDate(date1);

            }
            Reporter("WeekOff Created", "INFO");
        }

        attendanceSettingsPage.setWorkDuration(true);
        for (AttendanceSettingsPage.DAYS day : AttendanceSettingsPage.DAYS.values()) {
            boolean isAll = day.equals(AttendanceSettingsPage.DAYS.LHW);
            boolean LH = day.equals(AttendanceSettingsPage.DAYS.LH);
            boolean LW = day.equals(AttendanceSettingsPage.DAYS.LW);
            boolean WH = day.equals(AttendanceSettingsPage.DAYS.WH);
            boolean isHolidaySetting = day.equals(AttendanceSettingsPage.DAYS.HOLIDAY) || LH || WH || isAll;
            boolean isWeekOffSetting = day.equals(AttendanceSettingsPage.DAYS.WEEKOFF) || LW || WH || isAll;
            boolean isLeaveSetting = day.equals(AttendanceSettingsPage.DAYS.LEAVE) || LH || LW || isAll;

            attendanceSettingsPage.setHolidayDays(isHolidaySetting);
            attendanceSettingsPage.setWeeklyoffDays(isWeekOffSetting);
            attendanceSettingsPage.setLeaveDays(isLeaveSetting);
            attendanceSettingsServices.createSettings(attendanceSettingsPage);

            Reporter("Work Duration not considered for- Leaves? " + isLeaveSetting + " Holiday? " + isHolidaySetting + " WeekOff? " + isWeekOffSetting, "INFO");

            reports.setReportType("opt_roster_18");
            reports.setModule("opt_main_roster_1");
            reports.setReportFilter(employee.getUserID());
            reports.setSubCriteria(String.valueOf(Reports.employeeTypes.activeEmployees));
            reports.setMonthAndYear(dateTimeHelper.formatDateTo(date1, "YYYY-MM"));
            reportsDashboardServices.getReportWorkDurationSummary(reports);
        }
    }
}
