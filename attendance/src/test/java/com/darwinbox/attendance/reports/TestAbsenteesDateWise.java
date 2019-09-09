package com.darwinbox.attendance.reports;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
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

public class TestAbsenteesDateWise extends TestBase {

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
        reportsDashboardServices=new ReportsDashboardServices();
        reports=new Reports();
        dateTimeHelper = new DateTimeHelper();
        employeeServices = new EmployeeServices();
        displaySettingsPage = new DisplaySettingsPage(driver);
        date = new Date();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testAbsentDateWise(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("CommonSettings.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");

        Employee employee = employeeServices.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");
        Date date = dateTimeHelper.getFirstDateOfPreviousMonth(new Date());
        String inTimeDate = dateTimeHelper.formatDateTo(date,"dd-MM-yyyy");
        String inTime = dateTimeHelper.parseTime(shift.getStartTime());
        String outTimeDate = shift.isOverNightShift() ? dateTimeHelper.formatDateTo(new Date(),"dd-MM-yyyy") : inTimeDate;
        String outTime = dateTimeHelper.parseTime(shift.getEndTime());
        String breakTime = dateTimeHelper.parseTime( new Random().nextInt(60));

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
        attendanceImportPage.setBreakDuration(breakTime);

        atb.importBackdated(attendanceImportPage.getWorkDuration());

        String leaveToApply = testData.get("ApplyLeave");
        boolean isFirst = false;
        boolean isSecond = true;
        boolean isApproved = true;

        String leaveid = atb.getLeaveId(leaveToApply);
        atb.applyLeave(date, employee, leaveid, isFirst, isSecond, isApproved);
        Reporter("Leave applied for date "+dateTimeHelper.formatDateTo(date,"dd-MM-yyyy"),"INFO");

        reports.setReportType("opt_roster_15");
        reports.setModule("opt_main_roster_1");
        reports.setReportFilter(employee.getUserID());
        reports.setSubCriteria(String.valueOf(Reports.employeeTypes.activeEmployees));
        reports.setMonthAndYear(dateTimeHelper.formatDateTo(date, "YYYY-MM"));
      Object a=  reportsDashboardServices.getReportAbsenteesDateWise(reports);

     Date date1=  dateTimeHelper.getLastDateOfPreviousMonth();
     if(a.equals(dateTimeHelper.formatDateTo(date1,"dd-MMM-yyyy"))){
         Reporter("Absentees Datewise Report is as expected","PASS");
     }else
         Reporter("Absentees Datewise Report is NOT as expected","FAIL");
    }

    }
