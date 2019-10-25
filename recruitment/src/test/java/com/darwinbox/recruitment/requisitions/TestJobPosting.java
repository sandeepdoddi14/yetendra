package com.darwinbox.recruitment.requisitions;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.Services;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestJobPosting extends TestBase {

    LoginPage loginPage;
    Requisition requisition;
    RequisitionService requisitionService;
    Services services;
    EmployeeServices empService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }
    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        requisition = new Requisition();
        requisitionService = new RequisitionService();
        services = new Services();
        empService = new EmployeeServices();

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

        requisition.toObject(testData);
        // job posting class-> tomap and toobject



    }
    }
