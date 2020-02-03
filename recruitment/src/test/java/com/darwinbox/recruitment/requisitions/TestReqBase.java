package com.darwinbox.recruitment.requisitions;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.core.services.DepartmentServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.recruitment.JobsPage;
import com.darwinbox.recruitment.RequisitionTestBase;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestReqBase extends TestBase {

    LoginPage loginPage;
    RequisitionService requisitionService;
    EmployeeServices empService;
    GenericHelper genericHelper;
    JobsPage jobsPage;
    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        requisitionService  = new RequisitionService();
        empService = new EmployeeServices();
        genericHelper = new GenericHelper(driver);
        jobsPage = new JobsPage(driver);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testRequisition(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        RequisitionTestBase requisitionTestBase = RequisitionTestBase.getObject("RequisitionBase.xlsx");
        //requisitionTestBase.createRequisition("reqType").getData("");
        String type = testData.get("reqType");

        Employee employee = new Employee();
        employee = empService.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
        Reporter("Emp is created " + employee.getUserID(), "INFO");

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

        Requisition requisition= new Requisition();
        requisition = requisitionTestBase.createRequisition(type);

        requisition.toObjectSearch(employee.getFirstName()+" "+employee.getLastName());
        String reqID = requisitionService.searchRequisition(requisition);
        Assert.assertNotNull(reqID,"Requisition is NOT created");
        if(reqID!=null)
            Reporter("Requisition ID created is ::"+reqID,"INFO");
        else
            Reporter("Requisition ID is not captured","ERROR");

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(data.get("@@admin"), data.get("@@password"));
        driver.navigate().refresh();

        requisition.toObjectJobSearch(reqID);
        String jobID =requisitionService.searchActiveJobs();
        Assert.assertNotNull(jobID,"Job is NOT created");
        if(jobID!=null)
            Reporter("Job is created for requisition raised ","INFO");
        else
            Reporter("Job ID is NOT generated, check in jobs page","ERROR");

        int numOfPositionsRaised = Integer.parseInt(requisition.getTotalPositions());

        genericHelper.navigateTo("/recruitment/recruitment/requisitionstageone/id/"+jobID+"/edit/1");
        Reporter("Navigated to Page-1 of Job Posting","INFO");

        int actualPositions = jobsPage.getNumberOfPositions();

        if(numOfPositionsRaised==actualPositions)
            Reporter("Number of Openings are reflecting in jobs page-1, as per requisition raised","PASS");
        else
            Reporter("Number of Openings are NOT reflecting in jobs page-1, as per requisition raised","FAIL");


    }
    }
