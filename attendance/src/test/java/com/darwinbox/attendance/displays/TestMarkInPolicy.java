package com.darwinbox.attendance.displays;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.AttendanceSettingsPage;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.others.DisplayFlags;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestMarkInPolicy extends TestBase {


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
        displaySettingsPage = new DisplaySettingsPage(driver);
        attendanceSettingsServices = new AttendanceSettingsServices();
        attendanceSettingsPage = new AttendanceSettingsPage();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, retryAnalyzer = TestBase.class,enabled = false)
    public void testMarkIn(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("MarkInPolicySetting.xlsx");

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("None");

        List<Employee> empParentCompany=new ArrayList<>();
        List<Employee> empGroupCompany=new ArrayList<>();

        for(int i=1; i<=1000 ;i++) {

            Employee employee = empService.createAnEmployee(true);
            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
            Reporter("Employee created " + employee.getUserID(), "INFO");
            Reporter("Employee mongo ID :" + employee.getMongoID() + " for Parent company, emp-" + i,"INFO");

            empParentCompany.add(employee);

        }

       /* for(int i=1; i<=100 ;i++) {

            Employee employee1 = empService.createAnEmployee(false);
            atb.assignPolicyAndShift(employee1.getUserID(), employee1.getDoj(), shift, policy, weekoffId);
            Reporter("Employee created " + employee1.getUserID(), "INFO");
            Reporter("Employee mongo ID :" + employee1.getMongoID()+" for Group company, emp-"+i,"INFO");

            empGroupCompany.add(employee1);

        }*/

    }
}