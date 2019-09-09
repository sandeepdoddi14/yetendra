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

import java.util.*;

public class TestEarlyOutSummary extends TestBase {


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
    public void testEarlyOutSummary(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("CommonSettings.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");
        Shift secondShift = atb.getShift(testData.get("Shift Name Second"));

        List<Employee> emp=new ArrayList<>();
        List<String> empIDs = new ArrayList<>();
        for(int j=0;j<2;j++) {

            Employee employee = employeeServices.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
            Reporter("Employee created " + employee.getUserID(), "INFO");
            emp.add(employee);

            String ab=  employee.getUserID();
            empIDs.add(ab);

            if (j==0) {
                long graceTimeIn = shift.getStartTime() + policy.getPolicyInfo().getGraceTimeIn();
                long graceTimeOut = shift.getEndTime() - policy.getPolicyInfo().getGraceTimeOut();

                Date date = dateTimeHelper.getPreviousDate(new Date());
                String inTimeDate = dateTimeHelper.formatDateTo(date, "dd-MM-yyyy");
                String inTime = dateTimeHelper.parseTime((int) graceTimeIn + 1);
                String outTimeDate = shift.isOverNightShift() ? dateTimeHelper.formatDateTo(new Date(), "dd-MM-yyyy") : inTimeDate;
                String outTime = dateTimeHelper.parseTime((int) graceTimeOut - 1);
                String breakTime = dateTimeHelper.parseTime(new Random().nextInt(60));

                AttendanceImportPage attendanceImportPage = new AttendanceImportPage();
                attendanceImportPage.setEmployeeId(emp.get(0).getEmployeeID());
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
                Reporter("Out Time of employee " + emp.get(0).getUserID() + " is: " + outTime, "INFO");

            }
            else {

                secondShift.setOverNightShift(true);
                secondShift.setStartTime("22:00");
                secondShift.setEndTime("05:00");

                long graceTimeIn = secondShift.getStartTime() + policy.getPolicyInfo().getGraceTimeIn();
                long graceTimeOut = secondShift.getEndTime() - policy.getPolicyInfo().getGraceTimeOut();

                Date date = dateTimeHelper.getPreviousDate(new Date());
                Date date1= dateTimeHelper.addDays(date,-3);
                String inTimeDate = dateTimeHelper.formatDateTo(date1, "dd-MM-yyyy");
                String inTime = dateTimeHelper.parseTime((int) graceTimeIn + 1);
                String outTimeDate = secondShift.isOverNightShift() ? dateTimeHelper.formatDateTo(dateTimeHelper.getNextDate(date1), "dd-MM-yyyy") : inTimeDate;
                String outTime = dateTimeHelper.parseTime((int) graceTimeOut - 1);
                String breakTime = dateTimeHelper.parseTime(new Random().nextInt(60));

                AttendanceImportPage attendanceImportPage = new AttendanceImportPage();
                attendanceImportPage.setEmployeeId(emp.get(1).getEmployeeID());
                attendanceImportPage.setShitDate(date1);
                attendanceImportPage.setInTimeDate(inTimeDate);
                attendanceImportPage.setInTime(inTime);
                attendanceImportPage.setOutTimeDate(outTimeDate);
                attendanceImportPage.setOutTime(outTime);
                attendanceImportPage.setShiftName(secondShift.getShiftName());
                attendanceImportPage.setWeekoff(false);
                attendanceImportPage.setPolicyName(policy.getPolicyInfo().getPolicyName());
                attendanceImportPage.setBreakDuration(breakTime);

                atb.importBackdated(attendanceImportPage.getWorkDuration());
                Reporter("Out Time of employee " + emp.get(1).getUserID() + " is: " + outTime, "INFO");

            }
        }



        reports.setReportType("opt_roster_13");
        reports.setModule("opt_main_roster_1");
        reports.setFilter(empIDs);
        reports.setSubCriteria(String.valueOf(Reports.employeeTypes.activeEmployees));
        reports.setMonthAndYear(dateTimeHelper.formatDateTo(date, "YYYY-MM"));
        reportsDashboardServices.getReportEarlyOutSummary(reports);
    }
}
