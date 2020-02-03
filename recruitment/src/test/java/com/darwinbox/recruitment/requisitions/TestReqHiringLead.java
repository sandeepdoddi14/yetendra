package com.darwinbox.recruitment.requisitions;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.recruitment.RequisitionTestBase;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestReqHiringLead extends RequisitionTestBase {

    LoginPage loginPage;
    EmployeeServices employeeServices;
    Requisition requisition;
    RequisitionService requisitionService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        employeeServices = new EmployeeServices();
        requisition = new Requisition();
        requisitionService = new RequisitionService();

    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testRequisition(Map<String, String> testData) throws Exception {


        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        RequisitionTestBase requisitionTestBase = RequisitionTestBase.getObject("RequisitionBase.xlsx");
        String type = testData.get("reqType");

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication("W1573283285241@yopmail.com", "123456Aa!");
        driver.navigate().refresh();

       String beforeRecords = requisitionService.getTotalRequisitions(requisition);

        requisition = requisitionTestBase.createRequisition(type);

        String afterRecords = requisitionService.getTotalRequisitions(requisition);

        int beforeRequisitions = Integer.parseInt(beforeRecords);
        int afterRequisitions = Integer.parseInt(afterRecords);

        if(afterRequisitions==(beforeRequisitions+1))
            Reporter("Requisition is raised by Hiring Lead, Total number of records updated are ::"+afterRequisitions,"PASS");
        else
            Reporter("Requisition is NOT raised, Total number of records reflecting are ::"+beforeRequisitions,"ERROR");


        //* 4.validate across all design in dept ??

    }


    }
