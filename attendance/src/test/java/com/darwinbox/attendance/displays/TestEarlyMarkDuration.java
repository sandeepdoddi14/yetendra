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

public class TestEarlyMarkDuration extends TestBase {

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
    public void testEarlymark(Map<String, String> testData) throws InterruptedException {

        try {
            Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("CommonSettings.xlsx");

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
        boolean check = displayFlags.isShowEarlyOut();

        if (isDisplayed && (check != isDisplayed)) {

            displaySettingsPage.selectMonth(dateHelper.formatDateTo(date, "YYYY-MMM"));
            displaySettingsPage.searchByDate(dateHelper.formatDateTo(date, "dd MMM")+" present");

            String userEndEarlyBy = displaySettingsPage.verifyColoumnValue(date, testData.get("header"));
            long attPage= dateHelper.parseTimeInMinutes(userEndEarlyBy);

            String outTimeDisplay = displaySettingsPage.verifyColoumnValue(date,"Time Out");
            Reporter("Out Time from system is " + outTimeDisplay, "INFO");
            long outTimeuserEnd = dateHelper.parseTimeIntoSeconds(dateHelper.formatDateTo(dateHelper.formatStringToDate("hh:mm:ss aa", outTimeDisplay), "HH:mm:ss"));
            long outgraceTime = dateHelper.parseTimeIntoSeconds(dateHelper.formatDateTo(dateHelper.formatStringToDate("hh:mm:ss aa", "05:45:00 PM"), "HH:mm:ss"));
            //long res = graceTimeOut*60-outTimeuserEnd;


            if (shift.isOverNightShift()) {
                outgraceTime= dateHelper.parseTimeIntoSeconds("06:45:00");
            }
            long res = outgraceTime-outTimeuserEnd;

                if (attPage == res)
                    Reporter("Early out duration is as expected " + userEndEarlyBy, "PASS");
                else {
                    Reporter("Early out duration is not as expected " + userEndEarlyBy, "FAIL");
                    Reporter("Expected is " + outTime + " Actual is " + attPage, "INFO");
                }
            }
        }
    }
