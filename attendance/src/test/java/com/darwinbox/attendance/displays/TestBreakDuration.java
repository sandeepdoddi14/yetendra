package com.darwinbox.attendance.displays;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
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

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static com.darwinbox.attendance.pages.settings.DisplaySettingsPage.coloumnName;
import static com.darwinbox.attendance.pages.settings.DisplaySettingsPage.dateAndMonth;
import static java.lang.Thread.sleep;

public class TestBreakDuration extends TestBase {

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
    public void testBreakDuration(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("DisplaySettingsFields.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");
        DisplayFlags displayFlags = policy.getDisplayFlags();

        Employee employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
        Reporter("Employee created " + employee.getUserID(), "INFO");

        Date date = dateHelper.getPreviousDate(new Date());
        String inTimeDate = dateHelper.formatDateTo(date,"dd-MM-yyyy");
        String inTime = dateHelper.parseTime(shift.getStartTime());
        String outTimeDate = shift.isOverNightShift() ? dateHelper.formatDateTo(new Date(),"dd-MM-yyyy") : inTimeDate;
        String outTime = dateHelper.parseTime(shift.getEndTime());
        String breakTime = dateHelper.parseTime( new Random().nextInt(60));

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
        Reporter("Navigated to user attendance page","INFO");

        boolean isDisplayed = displaySettingsPage.headersDisplay(testData.get("header"));
        boolean check = displayFlags.isShowBreakDuration();

        if ( isDisplayed && ( check != isDisplayed) ) {

            long breakDur = dateHelper.parseTime(breakTime);

            displaySettingsPage.selectMonth(dateHelper.formatDateTo(date, "YYYY-MMM"));
            displaySettingsPage.searchByDate(dateHelper.formatDateTo(date, "dd MMM"));
            sleep(3000);
            String userEnd =displaySettingsPage.verifyColoumnValue(date,testData.get("header"));
            Reporter("Break Duration from system is "+userEnd,"INFO");

            long attPage= dateHelper.parseTime(userEnd);

            if ( breakDur == attPage)
                Reporter("Break Duration is as expected "+userEnd,"PASS");
            else {

                Reporter("Break Duration is not as expected "+userEnd,"FAIL");
                Reporter("Expected is "+breakDur+" Actual is "+attPage,"INFO");

            }
        }
    }
}
