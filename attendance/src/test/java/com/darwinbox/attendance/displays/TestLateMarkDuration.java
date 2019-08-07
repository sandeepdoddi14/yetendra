package com.darwinbox.attendance.displays;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.others.DisplayFlags;
import com.darwinbox.attendance.pages.settings.AttendanceImportPage;
import com.darwinbox.attendance.pages.settings.DisplaySettingsPage;
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
import java.util.Random;

import static com.darwinbox.attendance.pages.settings.DisplaySettingsPage.coloumnName;
import static java.lang.Thread.sleep;

public class TestLateMarkDuration extends TestBase {


    LoginPage loginPage;
    DateTimeHelper dateHelper;
    EmployeeServices empService;
    DisplaySettingsPage displaySettingsPage;

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
        dateHelper = new DateTimeHelper();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, retryAnalyzer = TestBase.class)
    public void testlatemark(Map<String, String> testData) throws InterruptedException {

        try {
            Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("DisplaySettingsFields.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");
        DisplayFlags displayFlags = policy.getDisplayFlags();

        Employee employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");

        long graceTimeIn= shift.getStartTime()+ policy.getPolicyInfo().getGraceTimeIn();
        long graceTimeOut=shift.getEndTime()- policy.getPolicyInfo().getGraceTimeOut();

        Date date = dateHelper.getPreviousDate(new Date());
        String inTimeDate = dateHelper.formatDateTo(date, "dd-MM-yyyy");
        String inTime = dateHelper.parseTime((int)graceTimeIn+1);
        String outTimeDate = shift.isOverNightShift() ? dateHelper.formatDateTo(new Date(), "dd-MM-yyyy") : inTimeDate;
        String outTime = dateHelper.parseTime((int)graceTimeOut-1);
        String breakTime = dateHelper.parseTime(new Random().nextInt(60));

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

        displaySettingsPage.navigateToAttendancePage(employee.getUserID());
        Reporter("Navigated to user attendance page", "INFO");
        boolean isDisplayed = displaySettingsPage.headersDisplay(testData.get("header"));
        boolean check = displayFlags.isShowLateBy();

        if (isDisplayed && (check != isDisplayed)) {

            Reporter("Time In assigned is "+inTime,"INFO");
            displaySettingsPage.selectMonth(dateHelper.formatDateTo(date, "YYYY-MMM"));
            displaySettingsPage.searchByDate(dateHelper.formatDateTo(date, "dd MMM"));

            String userEndLateBy = displaySettingsPage.verifyColoumnValue(date, testData.get("header"));
            Reporter("Late Mark from system is " + userEndLateBy, "INFO");
            long attPage = dateHelper.parseTime(userEndLateBy);

            String inTimeDisplay = displaySettingsPage.verifyColoumnValue(date, "Time In");
            Reporter("Time In from system is " + inTimeDisplay, "INFO");
            long inTimeuserEnd = dateHelper.parseTime(dateHelper.formatDateTo(dateHelper.formatStringToDate("hh:mm:ss aa", inTimeDisplay), "HH:mm:ss"));
            long res = inTimeuserEnd - (graceTimeIn*60);

            if (attPage == res)
                Reporter("Late By duration is as expected " + userEndLateBy, "PASS");
             else {
                Reporter("Late By duration is not as expected " + userEndLateBy, "FAIL");
                Reporter("Expected is " + inTime + " Actual is " + attPage, "INFO");
            }
        }
    }
}

