package com.darwinbox.attendance.leavedeductions.absent.fullday;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.Absent;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class TestSecondHalfAppliedAndApprovedForAbsentDeductionFullDay extends TestBase {

    public static Employee employee = null;
    public static Date date;
    LoginPage loginPage;
    GenericHelper genHelper;
    DateTimeHelper dateHelper;
    EmployeeServices empService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        genHelper = new GenericHelper(driver);
        empService = new EmployeeServices();
        dateHelper = new DateTimeHelper();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Absent,LeaveDeduction", retryAnalyzer = TestBase.class)
    public void testSecondHalfAppliedAndApproved(Map<String, String> testData) {

        String title = " With Second Half Applied and Approved ";

        boolean isApproved = true;
        boolean isFirst = false;
        boolean isSecond = true;

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("LeaveDeductionPolicies.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));

        if (employee == null) {
            employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj());
            date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
        }

        Reporter("Employee created " + employee.getUserID(), "INFO");

        String leaveName = testData.get("Leave Name");
        String leaveToApply = testData.get("ApplyLeave");

        Absent absent = policy.getAbsent();

        if (absent == null) {
            Reporter("Absent deduction is disabled ", "FAIL");
            Assert.assertFalse(true, "Leave deductions for Absent is not enabled");
        }

        title += " >> Attendance Policy ";

        title += absent.isWeekoff() ? " >> WeeklyOff " : "";
        title += absent.isHoliday() ? " >> Holiday " : "";

        if ((!absent.isWeekoff()) && (!absent.isHoliday())) {
            title += " >> Empty ";
        }

        Reporter(" Test Scenario  : " + title, "INFO");

        for (LeaveDeductionsBase.DAYSTATUS day : LeaveDeductionsBase.DAYSTATUS.values()) {

            date = dateHelper.getNextDate(date);

            String temp = " >> Status ";

            boolean isboth = day.equals(LeaveDeductionsBase.DAYSTATUS.WH);
            boolean isholiday = day.equals(LeaveDeductionsBase.DAYSTATUS.HOLIDAY) || isboth;
            boolean isWeekoff = day.equals(LeaveDeductionsBase.DAYSTATUS.WEEKOFF) || isboth;

            String leaveid = atb.getLeaveId(leaveToApply);
            atb.applyLeave(date, employee, leaveid, isFirst, isSecond, isApproved);

            temp += isWeekoff ? " >> WeeklyOff " : "";
            temp += isholiday ? " >> Holiday " : "";

            if ((!isWeekoff) && (!isholiday)) {
                temp += " >> Empty ";
            }

            if (isholiday) {
                atb.createHoliday(date);
            }

            Map<String, String> body = absent.getAbsent(employee.getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift.getShiftName(), date, isWeekoff);
            atb.importBackdated(body);

            String date_test = " >> Date :" + body.get("UserAttendanceImportBack[2][1]");
            temp += date_test;

            String status = atb.getAttendanceStatus(employee.getMongoID(), date);

            Reporter(" Day Status " + temp, "INFO");
            Reporter(" Actual Status " + date_test + " " + status.replaceAll("\\<.*?>", ""), "INFO");

            atb.validateHoliday(isholiday, status, this);
            atb.validateWeekoff(isWeekoff, status, this);

            atb.validateLeave(isApproved, isFirst || isSecond, status, leaveToApply, this);

            boolean proceed = absent.getProceed(absent, day);

            if (proceed) {
                atb.validateLeave(!absent.isApprovalRequired(), isFirst || isSecond, status, leaveName, this);
            } else {
                atb.validateNoLeave(status, leaveName, this);
            }

            validateDate();
        }
    }

    private void validateDate() {

        Date local = dateHelper.getNextDate(date);

        String curr = dateHelper.formatDateTo(new Date(), "yyyy-MM-dd");
        String expected = dateHelper.formatDateTo(local, "yyyy-MM-dd");

        if (curr.equals(expected)) {
            employee = empService.createAnEmployee(employee.getCompanyID().equals("main"));
            AttendanceTestBase.getObject().assignPolicyAndShift(employee.getUserID(), employee.getDoj());
            date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
        }
    }
}