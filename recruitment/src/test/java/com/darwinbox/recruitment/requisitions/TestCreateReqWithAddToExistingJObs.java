package com.darwinbox.recruitment.requisitions;

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

public class TestCreateReqWithAddToExistingJObs extends TestBase {


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

        requisition.toObject(testData);
        requisitionService.createRequisition(requisition);
        Reporter("Active job is selected","INFO");
        /*requisitionService.createRequisition(requisition);
        requisition.toObjectSearch("Automation Admin");
        String reqID = requisitionService.searchRequisition(requisition);
*/


        requisition.toObjectJobSearch("REQ_24");
        //String jobID =requisitionService.searchJobsPage(requisition);
        String jobID =requisitionService.searchActiveJobs();

        requisition.toObjectPostJob(jobID,testData.get("HiringLead"));
        requisitionService.postJob(requisition);

        Reporter("Job is posted","INFO");


    }
}
