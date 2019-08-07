package com.darwinbox.attendance.leavedeductions.multiple.lateduration;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.EarlyDuration;
import com.darwinbox.attendance.objects.policy.leavedeductions.LateDuration;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.objects.policy.leavedeductions.WorkDuration;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestLateDurationAndWorkDurationDeduction extends TestBase {

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

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "LateDuration,WorkDuration,LeaveDeduction", retryAnalyzer = TestBase.class)
    public void testforLateDurationAndWorkDuration(Map<String, String> testData) {

        String title = " With Late Duration and Early Duration ";

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

        LateDuration lateDuration = policy.getLateDuration();
        WorkDuration workDuration = policy.getWorkDuration();

        if (lateDuration == null) {
            Reporter("LateDuration deduction is disabled ", "FAIL");
            return;
        }

        if (workDuration == null) {
            Reporter("WorkDuration deduction is disabled ", "FAIL");
            return;
        }

        List<LeaveDeductionsBase> list = new ArrayList<>();
        list.add(lateDuration);
        list.add(workDuration);

        Reporter(" Test Scenario  : " + title, "INFO");

        date = dateHelper.getNextDate(date);

        Map<String,String> body = atb.createEntry(employee.getEmployeeID(),policy,shift,list,date,false);
        atb.importBackdated(body);

        String date_test = " >> Date :" + body.get("UserAttendanceImportBack[2][1]");
        String status = atb.getAttendanceStatus(employee.getMongoID(), date);

        Reporter(" Actual Status " + date_test + " " + status.replaceAll("\\<.*?>", ""), "INFO");

        for ( LeaveDeductionsBase ldbase : list) {
            atb.validateLeave(!ldbase.isApprovalRequired(),true,status,atb.getLeaveNameById(ldbase.getLeaveId()),this);
        }

        validateDate();

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