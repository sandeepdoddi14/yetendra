package com.darwinbox.attendance.reports;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.Absent;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.objects.policy.leavedeductions.WorkDuration;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestDailyAttendanceRoster extends TestBase {

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

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, retryAnalyzer = TestBase.class)
    public void testAttendanceRoster(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("CommonSettings.xlsx");
        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("All");

        Employee employee = employeeServices.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");

        Date date=dateTimeHelper.getFirstDateOfPreviousMonth(new Date());

        String leaveToApply = testData.get("ApplyLeave");
        String leaveid = atb.getLeaveId(leaveToApply);

        for (LeaveDeductionsBase.DAYSTATUS day : LeaveDeductionsBase.DAYSTATUS.values()) {

            boolean isboth = day.equals(LeaveDeductionsBase.DAYSTATUS.WH);
            boolean isHoliday = day.equals(LeaveDeductionsBase.DAYSTATUS.HOLIDAY) || isboth;
            boolean isWeekoff = day.equals(LeaveDeductionsBase.DAYSTATUS.WEEKOFF) || isboth;
//leaves + Holiday

            for(int i=1;i<=6;i++){
                atb.createHoliday(dateTimeHelper.addDays(date, i));

            }
            atb.applyLeave(dateTimeHelper.addDays(date, 1), employee, leaveid, false, false, true); //full day present
            atb.applyLeave(dateTimeHelper.addDays(date, 2), employee, "unpaid", false, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 3), employee, leaveid, true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 4), employee, "unpaid", true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 5), employee, leaveid, true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 5), employee, leaveid, false, true, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 6), employee, "unpaid", true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 6), employee, leaveid, false, true, true);

//only week off
            if(isWeekoff) {
                Absent absent = new Absent();
                Map<String, String> body = absent.getAbsent(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift.getShiftName(), dateTimeHelper.addDays(date, 7), isWeekoff);
                atb.importBackdated(body);
            }
//holiday+leaves+attendace
            if(isHoliday){
                for(int j=8;j<=14;j++) {
                    atb.createHoliday(dateTimeHelper.addDays(date, j));
                    WorkDuration wd = new WorkDuration();
                    wd.setWdhrs_fullday("4");
                    Map<String, String> body = wd.getWorkDuration(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift, dateTimeHelper.addDays(date, j), false, false, false);
                    atb.importBackdated(body);
                }
                atb.applyLeave(dateTimeHelper.addDays(date, 9), employee, leaveid, false, false, true); //full day present
                atb.applyLeave(dateTimeHelper.addDays(date, 10), employee, "unpaid", false, false, true);
                atb.applyLeave(dateTimeHelper.addDays(date, 11), employee, leaveid, true, false, true);
                atb.applyLeave(dateTimeHelper.addDays(date, 12), employee, "unpaid", true, false, true);
                atb.applyLeave(dateTimeHelper.addDays(date, 13), employee, leaveid, true, false, true);
                atb.applyLeave(dateTimeHelper.addDays(date, 13), employee, leaveid, false, true, true);
                atb.applyLeave(dateTimeHelper.addDays(date, 14), employee, "unpaid", true, false, true);
                atb.applyLeave(dateTimeHelper.addDays(date, 14), employee, leaveid, false, true, true);

            }
            //W H P
            if(isboth){
                atb.createHoliday(dateTimeHelper.addDays(date, 15));
                WorkDuration wd = new WorkDuration();
                wd.setWdhrs_fullday("4");
                Map<String, String> body =wd.getWorkDuration(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift, dateTimeHelper.addDays(date, 15), false, false, true);
                atb.importBackdated(body);
            }
            //W H L P
            atb.applyLeave(dateTimeHelper.addDays(date, 16), employee, leaveid, false, false, true); //full day present
            atb.createHoliday(dateTimeHelper.addDays(date, 16));
            WorkDuration wd = new WorkDuration();
            wd.setWdhrs_fullday("4");
            Map<String, String> body =wd.getWorkDuration(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift, dateTimeHelper.addDays(date, 16), false, false, true);
            atb.importBackdated(body);
            // leaves


            atb.applyLeave(dateTimeHelper.addDays(date, 17), employee, leaveid, false, false, true); //full day present
            atb.applyLeave(dateTimeHelper.addDays(date, 18), employee, "unpaid", false, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 19), employee, leaveid, true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 20), employee, "unpaid", true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 21), employee, leaveid, true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 21), employee, leaveid, false, true, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 22), employee, "unpaid", true, false, true);
            atb.applyLeave(dateTimeHelper.addDays(date, 22), employee, leaveid, false, true, true);


        }

        reports.setReportType("opt_roster_20");
        reports.setModule("opt_main_roster_1");
        reports.setReportFilter(employee.getUserID());
        reports.setSubCriteria(String.valueOf(Reports.employeeTypes.activeEmployees));
        reports.setMonthAndYear(dateTimeHelper.formatDateTo(date, "YYYY-MM"));
        reportsDashboardServices.getReportDailyAttendanceRoster(reports);
    }
}
