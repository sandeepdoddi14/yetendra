package com.darwinbox.recruitment.requisitions;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.Services;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.Permissions;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.PermissionService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestCreateRequisition extends TestBase {

    LoginPage loginPage;
    Requisition requisition;
    RequisitionService requisitionService;
    Services services;
    Permissions permissions;
    PermissionService permissionService;
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
        permissions = new Permissions();
        permissionService = new PermissionService();
        empService = new EmployeeServices();

    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testRequisition(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        Employee employee = empService.createAnEmployee(true);
        Reporter("Employee created :" + employee.getUserID(), "INFO");

        permissions.toObject(testData,employee.getUserID());
        permissionService.createRequisitionPermission(permissions);
        Reporter("Permission to raise requisition is given to :"+employee.getUserID(),"INFO");

        requisition.toObject(testData);

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

        requisitionService.createRequisition(requisition);

        requisition.toObjectSearch(employee.getFirstName()+" "+employee.getLastName());
        String reqID = requisitionService.searchRequisition(requisition);

        if(reqID.isEmpty())
            Reporter("Requisition is not raised","FAIL");
        else
            Reporter("Requisition is created by Permission Role Holder, ID is :"+reqID,"INFO");

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(data.get("@@admin"), data.get("@@password"));
        driver.navigate().refresh();

        //req tab search by ID and ID must found and same, admin end as well search objects and service is same

        requisition.toObjectSearch(reqID);
        String reqIDAdmin = requisitionService.searchRequisition(requisition);

        if(reqID.equalsIgnoreCase(reqIDAdmin))
            Reporter("Req ID is reflected at admin end as expected","PASS");
        else
            Reporter("Requisition ID is not found at admin end","FAIL");



    }
    }
