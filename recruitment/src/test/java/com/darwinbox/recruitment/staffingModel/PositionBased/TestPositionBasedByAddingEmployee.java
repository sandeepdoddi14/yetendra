package com.darwinbox.recruitment.staffingModel.PositionBased;

import com.darwinbox.attendance.objects.Employee;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestPositionBasedByAddingEmployee extends TestBase {


    LoginPage loginPage;
    DateTimeHelper dateTimeHelper;
    PositionSettings positionSettings;
    DesignationNames designationNames;
    Designations designations;
    DesignationsService designationsService;
    DesignationNamesServices designationNamesServices;
    PositionData positionData;
    EmployeeWorkRole employeeWorkRole;
    EmployeeServices employeeServices;
    Requisition requisition;
    RequisitionService requisitionService;
    PositionDataService positionDataService;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        dateTimeHelper = new DateTimeHelper();
        positionSettings = new PositionSettings();
        designationNames = new DesignationNames();
        designationNamesServices = new DesignationNamesServices();
        designations = new Designations();
        designationsService = new DesignationsService();
        positionData = new PositionData();
        employeeWorkRole = new EmployeeWorkRole();
        employeeServices = new EmployeeServices();
        requisition = new Requisition();
        requisitionService = new RequisitionService();
        positionDataService = new PositionDataService();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testStaffingModel(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        List<String> des = new ArrayList<>();

        /*Default position settings, only turn-on setting*/

        positionSettings.UpdatePositions();

        String designationName = designationNames.toObject();
        designationNamesServices.createDesignationName(designationNames);
        Reporter("Designation Name created is : "+designationName,"INFO");

        Assert.assertNotNull(designationName,"Designation Name is NOT created");

        designations.setDefaultForDesignation(designationName);
        designations.setStaffingModel("2");
        designations.setNumberOfPositions("2"); // if num of pos increase, check pos status code to get for every position raised
        designationsService.createDesignation(designations,"PositionBased");

       //assertions whether IDs contains in response  response.contains()

        String designationID = designationsService.getDesignationID(designationName);

        designations.setNumberOfPositions("2");
        designationsService.createPositionStageOne(designations,designationID);
        //designations.setPositionID(""); pos name
        designationsService.createPositionStageTwo(designations,designationID);

        positionData.defaultToPositionData();
        positionData= positionDataService.getPositionID(designationID,positionData);

        Assert.assertNotNull(positionData.getPositionID(),"Position ID is NOT captured");

        String status = positionData.getPositionStatus();
        if(!status.contains("Active"))
            Reporter("Position status is NOT active/occupied","ERROR");
        else
            Reporter("Position status is ACTIVE as expected","INFO");

         Employee employee = new Employee();
         employee = employeeServices.createAnEmployee(true);
         employeeWorkRole.updateEmpWorkRole(employee.getMongoID(),designationID,positionData.getPositionID());

         status = positionData.getPositionStatus();
        if(!status.contains("Occupied"))
            Reporter("Position status is NOT active/occupied","ERROR");
        else
            Reporter("Position status is OCCUPIED as expected","INFO");

        /*raise requisition*/

        requisition.toObject(testData);
        requisition.setDesignationAndDepartment(designationID);
        requisition.setPositionID(positionData.getPositionID());
        requisitionService.createRequisition(requisition);


//wrong position ID, below reqID should throw exception , create des again and assign to get Occupied status also
        // and to pass this pos ID into req to validate. put as List<> des and create 2

        requisition.toObjectSearch(designationName);
        String reqID = requisitionService.searchRequisition(requisition);
        Reporter("Requisition created : "+reqID,"INFO");
        requisition.toObjectJobSearch(reqID);
        String jobID =requisitionService.searchJobsPage(requisition);

        Assert.assertNotNull(jobID,"Job ID is not captured");

        //jobs page-1 Filled status



    }
    }
