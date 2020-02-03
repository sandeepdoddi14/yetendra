package com.darwinbox.recruitment.staffingModel.NoStaffingModel;

import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.Designations;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.DesignationsService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestNoStaffingModelByAddingEmployee extends TestBase {


    LoginPage loginPage;
    DateTimeHelper dateTimeHelper;
    DesignationNames designationNames;
    Designations designations;
    DesignationsService designationsService;
    DesignationNamesServices designationNamesServices;
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
        dateTimeHelper = new DateTimeHelper();
        designationNames = new DesignationNames();
        designationNamesServices = new DesignationNamesServices();
        designations = new Designations();
        designationsService = new DesignationsService();
        employeeServices = new EmployeeServices();
        requisition = new Requisition();
        requisitionService = new RequisitionService();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class,enabled = false)
    public void testStaffingModel(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        String designationName = designationNames.toObject();
        designationNamesServices.createDesignationName(designationNames);
        Reporter("Designation Name created is : " + designationName, "INFO");

        Assert.assertNotNull(designationName, "Designation Name is NOT created");

        designations.setDefaultForDesignation(designationName,testData);
      //  designations.setStaffingModel("");
        designationsService.createDesignation(designations, "No");

        String designationID = designationsService.getDesignationID(designationName);

        Assert.assertNotNull(designationID, "Designation is NOT created");

        requisition.toObject(testData);
        requisition.setDesignationAndDepartment(designationID);
        requisitionService.createRequisition(requisition);


    }
    }
