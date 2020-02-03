package com.darwinbox.recruitment;

import com.darwinbox.attendance.objects.Employee;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.recruitment.objects.ReferAndIJP;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.objects.userAssignments.UserAssignments;
import com.darwinbox.recruitment.services.ReferAndIJPService;
import com.darwinbox.recruitment.services.RequisitionService;
import com.darwinbox.recruitment.services.userAssignments.UserAssignmentService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class TestAccessForReferAndIJP extends TestBase {

    LoginPage loginPage;
    GenericHelper genericHelper;
    JobsPage jobsPage;
    UserAssignments userAssignments;
    UserAssignmentService userAssignmentService;
    EmployeeServices employeeServices;
    Requisition requisition;
    RequisitionService requisitionService;
    ReferAndIJP referAndIJP;
    ReferAndIJPService referAndIJPService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        genericHelper = new GenericHelper(driver);
        jobsPage = new JobsPage(driver);
        userAssignments = new UserAssignments();
        userAssignmentService = new UserAssignmentService();
        employeeServices = new EmployeeServices();
        requisition = new Requisition();
        requisitionService = new RequisitionService();
        referAndIJP = new ReferAndIJP();
        referAndIJPService = new ReferAndIJPService();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testAccess(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        userAssignments.toObject(testData);
        userAssignments=  userAssignmentService.createUserAssignment(userAssignments);
        Reporter("User Assignment is created - "+userAssignments.getAssignmentName(),"INFO");

        Employee employee = new Employee();
        employee =userAssignmentService.generateAnEmployee(userAssignments,"no", "Working Days (DO NOT TOUCH)", "random", "no");
        Reporter("Emp is created " +employee.getUserID(),"INFO");

        if(employee.getDesignationID().equalsIgnoreCase(userAssignments.getDesignation())&&
                employee.getEmployeeTypeID().equalsIgnoreCase(userAssignments.getEmployeeType().replace("TYP_",""))&&
                    employee.getLocationID().equalsIgnoreCase(userAssignments.getLocation().replace("LOC_",""))&&
                      employee.getJobLevel().equalsIgnoreCase(userAssignments.getJobLevels()))
                      {
                         Reporter("Created Employee assignments are as expected","INFO");
                      }
        else {
            Reporter("Created Employee assignments are NOT same as expected","ERROR");
        }
        String activeJobId = requisitionService.searchActiveJobs();

        Reporter("Active Job mongo ID is ::"+activeJobId,"INFO");

        genericHelper.navigateTo("/recruitment/recruitment/requisitionstageone/id/"+activeJobId+"/edit/1");

        String displayName = "jobDisplayName--"+new Date();
        jobsPage.setDisplayNameForJob(displayName);

        Assert.assertTrue(jobsPage.clickOnPostToRefer(),"Refer is not selected");
        Assert.assertTrue(jobsPage.clickOnPostToIJP(),"IJP is not selected");

        Assert.assertTrue(jobsPage.assignReferFramework(userAssignments.getAssignmentName()),"Refer Framework is not assigned");
        Assert.assertTrue(jobsPage.assignIJPFramework(userAssignments.getAssignmentName()),"IJP Framework is not assigned");

        Assert.assertTrue(jobsPage.clickOnSave(),"Unable to click on Save and Continue button");

        //emp login

        String jobIDReferPage= referAndIJPService.searchJobsInIJPPage(displayName);
        String jobIDIJPPage = referAndIJPService.searchJobsInIJPPage(displayName);

        if(activeJobId.equalsIgnoreCase(jobIDReferPage) && activeJobId.equalsIgnoreCase(jobIDIJPPage))
            Reporter("User Assignment assigned to job is working as expected for Refer and IJP","PASS");
        else
            Reporter("User Assignment assigned to job is NOT working as expected for Refer and IJP","FAIL");



    }
    }
