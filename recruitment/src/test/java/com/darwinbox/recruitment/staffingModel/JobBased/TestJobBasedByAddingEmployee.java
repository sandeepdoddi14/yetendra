package com.darwinbox.recruitment.staffingModel.JobBased;

import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.Designations;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.objects.staffingModel.EmployeeWorkRole;
import com.darwinbox.recruitment.objects.staffingModel.PositionData;
import com.darwinbox.recruitment.objects.staffingModel.PositionSettings;
import com.darwinbox.recruitment.services.DesignationsService;
import com.darwinbox.recruitment.services.RequisitionService;
import com.darwinbox.recruitment.services.staffingModel.PositionDataService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestJobBasedByAddingEmployee extends TestBase {

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

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testStaffingModel(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        String designationName = designationNames.toObject();
        designationNamesServices.createDesignationName(designationNames);
        Reporter("Designation Name created is : "+designationName,"INFO");

        Assert.assertNotNull(designationName,"Designation Name is NOT created");

        designations.setDefaultForDesignation(designationName);
        designations.setStaffingModel("1");
        designations.setNumberOfPositions("2");
        designationsService.createDesignation(designations,"JobBased");

        String designationID = designationsService.getDesignationID(designationName);

        Assert.assertNotNull(designationID,"Designation is NOT created");

       //validation by over hiring allowed or not?

        requisition.toObject(testData);
        requisition.setDesignationAndDepartment(designationID);
        requisitionService.createRequisition(requisition);

        int positionsInPolicy = Integer.parseInt(designations.getNumberOfPositions());
        int positionsInRaisingReq = Integer.parseInt(requisition.getTotalPositions());

        if(designations.isOverHiringAllowed()){
             if(positionsInRaisingReq > positionsInPolicy ) {
                 Reporter("Positions are raised as Expected","INFO");
             }
         }
        else {
            if(positionsInRaisingReq > positionsInPolicy ) {
                Reporter("Positions are NOT raised as Expected","ERROR");
            }
        }


    }
    }
