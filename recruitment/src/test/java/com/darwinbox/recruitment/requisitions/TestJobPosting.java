package com.darwinbox.recruitment.requisitions;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.Services;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.objects.jobsPages.HiringTeam;
import com.darwinbox.recruitment.objects.jobsPages.JobApplication;
import com.darwinbox.recruitment.services.JobsPagesService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class TestJobPosting extends TestBase {

    LoginPage loginPage;
    Requisition requisition;
    RequisitionService requisitionService;
    Services services;
    EmployeeServices empService;
    JobsPagesService jobsPagesService;
    JobApplication jobApplication;
    GenericHelper genericHelper;
    HiringTeam hiringTeam;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }
    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        genericHelper = new GenericHelper(driver);
        requisition = new Requisition();
        requisitionService = new RequisitionService();
        services = new Services();
        empService = new EmployeeServices();
        jobsPagesService = new JobsPagesService();
        jobApplication = new JobApplication();
        hiringTeam = new HiringTeam();

    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testRequisition(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        Employee employee = empService.createAnEmployee(true);
        Reporter("Employee created :" + employee.getUserID(), "INFO");

        requisition.toObject(testData);

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

        requisitionService.createRequisition(requisition);
        requisition.toObjectSearch(employee.getFirstName()+" "+employee.getLastName());
        String reqID = requisitionService.searchRequisition(requisition);

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(data.get("@@admin"), data.get("@@password"));
        driver.navigate().refresh();

        requisition.toObjectJobSearch(reqID);
        String jobID =requisitionService.searchJobsPage(requisition);

        requisition.toObjectPostJob(jobID,testData.get("HiringLead"));
        requisitionService.postJob(requisition);

        //page-1, get job id
         //either call requisition object or create new objects

        requisition.toObject(testData);
        jobsPagesService.jobPosting(requisition,jobID);

        genericHelper.navigateTo("/recruitment/recruitment/requisitionstageone/id/"+jobID+"/edit/1");
       Reporter("Navigated to Page-1 of Job Posting","INFO");
       int numOfPositionsRaised = Integer.parseInt(testData.get("TotalPositions"));
       List<WebElement> element = driver.findElements(By.xpath("//*[@id=\"position_table\"]/tbody/tr"));
       int numOfPositionsReflecting = element.size();

       if(numOfPositionsRaised==numOfPositionsReflecting)
           Reporter("Number of Openings raised as per requisition raised","INFO");
       else
           Reporter("Openings are not raised as per requisition raised","FAIL");

       //page-2

         jobApplication.toObjectSecondPage(testData);
         jobsPagesService.jobApplication(jobApplication,jobID);
         //page-3


        //page-4
        hiringTeam.toObjectFourthPage(testData);
        jobsPagesService.hiringTeam(hiringTeam,jobID);
    }
    }
