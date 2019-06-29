package com.darwinbox.attendance.displays;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.AttendanceSettingsPage;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.pages.settings.DisplaySettingsPage;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.AttendanceSettingsServices;
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

import static java.lang.Thread.sleep;

public class TestPolicyDisplay extends TestBase {

    LoginPage loginPage;
    GenericHelper genHelper;
    DateTimeHelper dateHelper;
    EmployeeServices empService;
    AttendanceSettingsServices attendanceSettingsServices;
    AttendanceSettingsPage attendanceSettingsPage;
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
        genHelper = new GenericHelper(driver);
        empService = new EmployeeServices();
        dateHelper = new DateTimeHelper();
        displaySettingsPage=new DisplaySettingsPage(driver);
        attendanceSettingsServices = new AttendanceSettingsServices();
        attendanceSettingsPage= new AttendanceSettingsPage();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testPoiliciesDisplay(Map<String, String> testData) throws InterruptedException {

        try {
            Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        attendanceSettingsPage.setFixedDuration(false);
        attendanceSettingsPage.setHideAttendancePolicy(true);
        attendanceSettingsPage.setHideOTPolicy(true);
        attendanceSettingsServices.createSettings(attendanceSettingsPage);
        Reporter("Selected policies to hide","INFO");

        AttendanceTestBase atb = AttendanceTestBase.getObject("Settings.xlsx");
        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));

        Employee employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
        atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj());

        Reporter("Employee created " + employee.getUserID(), "INFO");

        displaySettingsPage.navigateToAttendancePage(employee.getUserID());
        Reporter("Navigated to user attendance page", "INFO");
        Assert.assertTrue(displaySettingsPage.checkPolicies(),"not as expected");
    }
}
