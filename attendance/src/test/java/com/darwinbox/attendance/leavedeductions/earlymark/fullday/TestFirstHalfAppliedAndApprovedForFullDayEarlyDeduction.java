package com.darwinbox.attendance.leavedeductions.earlymark.fullday;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.EarlyMark;
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
import java.util.List;
import java.util.Map;

public class TestFirstHalfAppliedAndApprovedForFullDayEarlyDeduction extends TestBase {

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
    public void testFirstHalfAppliedAndApproved(Map<String, String> testData) {

        String title = " With First Half Applied and Approved for Early Marks ";
        String dataSetFile = "EarlyMarks.xlsx";

        boolean isApproved = true;
        boolean isFirst = true;
        boolean isSecond = false;

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendanceTestBase atb = AttendanceTestBase.getObject(dataSetFile);

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));

        String leaveName = testData.get("Leave Name");
        String leaveToApply = testData.get("ApplyLeave");

        EarlyMark earlyMark = policy.getEarlyMark();

        if (earlyMark == null) {
            Assert.assertFalse(true, "Leave deductions for EarlyMark is not enabled");
        }

        title += " >> Attendance ";

        title += earlyMark.isWeekoff() ? " WeeklyOff " : "";
        title += earlyMark.isHoliday() ? " Holiday " : "";

        if ((!earlyMark.isWeekoff()) && (!earlyMark.isHoliday())) {
            title += " Empty ";
        }

        Reporter(" Test Scenario  : " + title, "INFO");

        String payCycle = testData.get("PayCycle");
        if (payCycle.equalsIgnoreCase("no"))
            payCycle = "1";
        else
            Reporter(" Using Paycycle based deduction ", "INFO");

        boolean isParent = policy.getPolicyInfo().getCompanyID().length() == 0;

        int count = earlyMark.getCount();

        for (LeaveDeductionsBase.DAYSTATUS day : LeaveDeductionsBase.DAYSTATUS.values()) {

            String temp = " >> Status ";
            boolean isholiday = day.equals(LeaveDeductionsBase.DAYSTATUS.HOLIDAY);
            boolean isboth = day.equals(LeaveDeductionsBase.DAYSTATUS.WH);
            boolean isWeekoff = day.equals(LeaveDeductionsBase.DAYSTATUS.WEEKOFF);

            isholiday = isholiday || isboth;
            isWeekoff = isWeekoff || isboth;

            temp += isWeekoff || isboth ? " WeeklyOff " : "";
            temp += isholiday || isboth ? " Holiday " : "";
            boolean empty = false;

            if ((!isWeekoff) && (!isholiday)) {
                temp += " Empty ";
                empty = true;
            }

            for (int i = 0; i < 2; i++) {

                validateDate(payCycle,isParent);

                int validation_cnt = 0;

                List<Date> dates = earlyMark.getDatesforDeduction(date);

                for (Date attendanceDate : dates) {

                    String leaveid = atb.getLeaveId(leaveToApply);
                    atb.applyLeave(attendanceDate, employee, leaveid, isFirst, isSecond, isApproved);

                    if (isholiday) {
                        atb.createHoliday(attendanceDate);
                    }

                    Map<String, String> body = earlyMark.getEarlyMark(employee.getEmployeeID(), policy.getPolicyInfo(), shift, attendanceDate, isWeekoff);
                    atb.importBackdated(body);

                    String temp_1 = temp;
                    validation_cnt++;
                    String date_test = " >> Date :" + dateHelper.formatDateTo(attendanceDate, "dd-MM-yyyy");
                    temp_1 += date_test;

                    String status = atb.getAttendanceStatus(employee.getMongoID(), attendanceDate);

                    Reporter(" Day Status " + temp, "INFO");
                    Reporter(" Actual Status " + date_test + " " + status.replaceAll("\\<.*?>", ""), "INFO");

                    try {

                        if (isholiday) {
                            Assert.assertTrue(status.contains(atb.holiday), "Holiday is not marked");
                        }

                        if (isWeekoff) {
                            Assert.assertTrue(status.contains(atb.weekoff), "WeekOff is not marked");
                        }

                        Assert.assertTrue(status.contains(atb.approved_halfday + "(" + leaveToApply + ")"), "No Leave is Deducted and in Pending state");

                        boolean proceed1 = ((validation_cnt >= count)) && ((earlyMark.isForEvery()) || (validation_cnt % count == 0));
                        boolean proceed = earlyMark.isInDay() && ((earlyMark.isHoliday() && isholiday)
                                || (earlyMark.isWeekoff() && isWeekoff) || empty) && proceed1;

                        if (proceed) {
                            if (earlyMark.isApprovalRequired())
                                Assert.assertTrue(status.contains(atb.pending_halfday + "(" + leaveName + ")"), "No Leave is Deducted and in Pending state");
                            else
                                Assert.assertTrue(status.contains(atb.approved_halfday + "(" + leaveName + ")"), "No Leave is Deducted and Approved");
                        } else {
                            Assert.assertFalse(status.contains(leaveName), "No Leave is Deducted and Approved");
                        }

                        Reporter(title + temp_1, "Pass");

                    } catch (Exception e) {
                        Reporter(title + temp_1 + "/n" + e.getMessage(), "Fail");
                    }
                }
            }

        }
    }


    private void validateDate(String payCycle, boolean isParent) {

        employee = empService.createAnEmployee(isParent);
        AttendanceTestBase.getObject().assignPolicyAndShift(employee.getUserID(), employee.getDoj());
        date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());

        Reporter("Employee created " + employee.getUserID(), "INFO");

        int start_day = Integer.parseInt(payCycle);

        date = dateHelper.getNextMonthFirst(date);

        if( start_day != 1) {
            date = dateHelper.setDayofMonth(date, start_day);
        }

    }
}