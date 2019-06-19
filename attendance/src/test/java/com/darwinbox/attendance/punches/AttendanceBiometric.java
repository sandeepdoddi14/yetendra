package com.darwinbox.attendance.punches;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Attendance;
import com.darwinbox.attendance.objects.AttendancePunch;
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
import com.darwinbox.framework.utils.DatabaseUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;
import java.util.Random;

public class AttendanceBiometric extends TestBase {

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

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "punches, biometric", retryAnalyzer = TestBase.class)
    public void testBiometricData(Map<String, String> testData) {

        String title = " Test For biometric Punches ";

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendanceTestBase atb = AttendanceTestBase.getObject();

        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));

        if (employee == null) {
            employee = empService.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);
            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj());
            date = dateHelper.formatStringToDate("yyyy-MM-dd", employee.getDoj());
        }

        Reporter("Employee Under Test " + employee.getUserID(), "INFO");

        boolean isEmcure = testData.get("Emcure").contains("yes");
        boolean isDirection = testData.get("Direction").contains("yes");

        char order[] =  testData.get("Order").toCharArray();

        Attendance attobj = new Attendance();
        attobj.setPolicy(policy);
        attobj.setShift(shift);
        attobj.setEmcure(isEmcure);
        attobj.setDirectionality(isDirection);
        attobj.setShiftDate(date);

        attobj.validateBuffer();

        attobj.addPreBuffer();
        attobj.addPostBuffer();

        Date timestamp = date;

        for ( char c: order) {

            AttendancePunch punch = null;
            int random = new Random().nextInt(60);

            timestamp = dateHelper.addTime(timestamp, random);
            int status = isDirection ? Integer.parseInt(c+""): 99;
            punch = new AttendancePunch(timestamp,status);

            attobj.addPunch(punch);

        }

         String query = attobj.getBiometricQuery(employee.getEmployeeID());

        try {
            DatabaseUtils dbu = new DatabaseUtils();
            dbu.loadDriver("com.mysql.jdbc.Driver");
            dbu.getConnection("jdbc:mysql://biometric.qa.darwinbox.io:3306/biometric","darwinbox","darwinbox123");
            dbu.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }

        atb.runBiometricCron( );

        attobj.validatePunches();
        atb.runCron(attobj, employee);

        Map<String,String> body = atb.getAttendanceLog(employee.getMongoID(),date );

        for(String s : body.keySet()){

            System.out.println(s + " - " + body.get(s));
        }











    }
}