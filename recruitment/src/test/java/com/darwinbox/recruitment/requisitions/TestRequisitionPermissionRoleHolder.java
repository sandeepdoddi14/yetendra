package com.darwinbox.recruitment.requisitions;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.Services;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.recruitment.objects.Permissions;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.PermissionService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestRequisitionPermissionRoleHolder extends TestBase {

    LoginPage loginPage;
    Requisition requisition;
    RequisitionService requisitionService;
    Services services;
    Permissions permissions;
    PermissionService permissionService;
    EmployeeServices empService;
    GenericHelper genericHelper;

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
        permissions = new Permissions();
        permissionService = new PermissionService();
        empService = new EmployeeServices();
        genericHelper = new GenericHelper(driver);

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

        /*Below lines of code is to search for requisition in jobs page Drafts filter, at admin end*/

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(data.get("@@admin"), data.get("@@password"));
        driver.navigate().refresh();

        requisition.toObjectJobSearch(reqID);
        String jobID =requisitionService.searchJobsPage(requisition);

        if(jobID.isEmpty())
            Reporter("Job is not created","FAIL");
        else
            Reporter("Job is created, ID is :"+jobID,"INFO");

        genericHelper.navigateTo("/recruitment/recruitment/requisitionstagefour/id/"+jobID+"/edit/1");

        //Reporter("navigated to jobs page-4","INFO");

        //post job

    }
}
