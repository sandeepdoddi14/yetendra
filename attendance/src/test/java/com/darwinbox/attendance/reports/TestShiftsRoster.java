package com.darwinbox.attendance.reports;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.Absent;
import com.darwinbox.attendance.pages.settings.AttendanceImportPage;
import com.darwinbox.attendance.pages.settings.DisplaySettingsPage;
import com.darwinbox.attendance.services.EmployeeServices;
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
import java.util.Random;

public class TestShiftsRoster extends TestBase {

    LoginPage loginPage;
    ReportsDashboardServices reportsDashboardServices;
    Reports reports;
    DateTimeHelper dateTimeHelper;
    EmployeeServices employeeServices;
    DisplaySettingsPage displaySettingsPage;
    Date date;

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
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testShiftsRoster(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("CommonSettings.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");
        Date date = dateTimeHelper.getFirstDateOfPreviousMonth(new Date());

        Shift secondShift=  atb.getShift(testData.get("Shift Name Second"));

        Employee employee = employeeServices.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);

        atb.assignPolicyAndShift(employee.getUserID(), dateTimeHelper.formatDateTo(date,"yyyy-MM-dd"), shift, policy, weekoffId);
        atb.assignPolicyAndShift(employee.getUserID(), dateTimeHelper.formatDateTo(dateTimeHelper.addDays(date,7),"yyyy-MM-dd"), secondShift, policy, weekoffId);

        Reporter("Employee created " + employee.getUserID(), "INFO");


        String inTimeDate = dateTimeHelper.formatDateTo(date, "dd-MM-yyyy");
        String outTimeDate = shift.isOverNightShift() ? dateTimeHelper.formatDateTo(new Date(), "dd-MM-yyyy") : inTimeDate;
        String breakTime = dateTimeHelper.parseTime(new Random().nextInt(60));

        AttendanceImportPage attendanceImportPage = new AttendanceImportPage();
        attendanceImportPage.setEmployeeId(employee.getEmployeeID());
        attendanceImportPage.setShitDate(date);
        attendanceImportPage.setInTimeDate(dateTimeHelper.formatDateTo(date, "dd-MM-yyyy"));
        attendanceImportPage.setInTime(dateTimeHelper.parseTime(shift.getStartTime()));
        attendanceImportPage.setOutTimeDate(outTimeDate);
        attendanceImportPage.setOutTime(dateTimeHelper.parseTime(shift.getEndTime()));
        attendanceImportPage.setShiftName(shift.getShiftName());
        attendanceImportPage.setWeekoff(false);
        attendanceImportPage.setPolicyName(policy.getPolicyInfo().getPolicyName());
        attendanceImportPage.setBreakDuration(breakTime);

        atb.importBackdated(attendanceImportPage.getWorkDuration());

        AttendanceImportPage attendanceImport = new AttendanceImportPage();
        attendanceImport.setEmployeeId(employee.getEmployeeID());
        attendanceImport.setShitDate(dateTimeHelper.addDays(date,7));
        attendanceImport.setInTimeDate(dateTimeHelper.formatDateTo(dateTimeHelper.addDays(date,7),"dd-MM-yyyy"));
        attendanceImport.setInTime(dateTimeHelper.parseTime(secondShift.getStartTime()));
        attendanceImport.setOutTimeDate(dateTimeHelper.formatDateTo(dateTimeHelper.addDays(date,7),"dd-MM-yyyy"));
        attendanceImport.setOutTime(dateTimeHelper.parseTime(secondShift.getEndTime()));
        attendanceImport.setShiftName(secondShift.getShiftName());
        attendanceImport.setWeekoff(false);
        attendanceImport.setPolicyName(policy.getPolicyInfo().getPolicyName());
        attendanceImport.setBreakDuration(breakTime);

        atb.importBackdated(attendanceImport.getWorkDuration());



        reports.setReportType("opt_roster_97");
        reports.setModule("opt_main_roster_1");
        reports.setReportFilter(employee.getUserID());
        reports.setSubCriteria(String.valueOf(Reports.employeeTypes.activeEmployees));
        reports.setMonthAndYear(dateTimeHelper.formatDateTo(date, "YYYY-MM"));
        Object a= reportsDashboardServices.getReportShiftsRoster(reports);
        String b=testData.get("Shift Name Second");
        if(a.toString().equalsIgnoreCase(b)){
            Reporter("Shifts Roster Report is as expected","PASS");
        }else
            Reporter("Shifts Roster Report is NOT as expected","FAIL");
    }
    }


