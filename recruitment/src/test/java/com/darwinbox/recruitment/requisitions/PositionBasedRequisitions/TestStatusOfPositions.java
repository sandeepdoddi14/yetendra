package com.darwinbox.recruitment.requisitions.PositionBasedRequisitions;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.RequisitionTestBase;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.objects.staffingModel.EmployeeWorkRole;
import com.darwinbox.recruitment.objects.staffingModel.PositionData;
import com.darwinbox.recruitment.objects.staffingModel.PositionSettings;
import com.darwinbox.recruitment.services.RequisitionService;
import com.darwinbox.recruitment.services.staffingModel.PositionDataService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.darwinbox.recruitment.RequisitionTestBase.employee;

public class TestStatusOfPositions extends TestBase {

    LoginPage loginPage;
    DesignationNames designationNames;
    DesignationNamesServices designationNamesServices;
    PositionSettings positionSettings;
    PositionData positionData;
    PositionDataService positionDataService;
    EmployeeWorkRole employeeWorkRole;
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
        positionData = new PositionData();
        positionDataService = new PositionDataService();
        employeeServices = new EmployeeServices();
        employeeWorkRole = new EmployeeWorkRole();
        requisition = new Requisition();
        requisitionService = new RequisitionService();
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testRequisition(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

/*1. create des with positions (create des in run time and put 4 positions) including need to hire as no for 1 pos
* 2.check archived/active/occupied(emp on notice) status with raising req with position matrix
* 3.check selected to hire yes/no and give more number of positions in req than des*/

//** check if emp on notice with occupied status in des and raise req (should allow)


        RequisitionTestBase requisitionTestBase = RequisitionTestBase.getObject("RequisitionBase.xlsx");
        String type = testData.get("reqType");
//driver object call
       // String designationID =  requisitionTestBase.createDesigWithPositions(type);


    // api call
        String designationID = requisitionTestBase.createDesWithPositions(type);

        positionData.defaultToPositionData();
        List<PositionData> list = positionDataService.getPositionID(designationID,positionData);

        for (int i =0;i<list.size();i++) {

            if (list.get(i).getPositionName().equalsIgnoreCase("archived")) {

                positionDataService.archivePosition(designationID, list.get(i).getPositionID());
                if (list.get(i).getPositionID() != null)
                    Reporter("Position status is archived as Expected", "INFO");

            }
            if (list.get(i).getPositionName().equalsIgnoreCase("occupied")) {

                employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
                employeeWorkRole.updateEmpWorkRole(employee.getMongoID(), designationID, list.get(i).getPositionID());
                if (list.get(i).getPositionID() != null)
                    Reporter("Position status is Occupied as Expected", "INFO");
            }

            String beforeRecords = requisitionService.getTotalRequisitions(requisition);
            requisitionTestBase.raiseReqByFetchingDesID(type, designationID, list.get(i).getPositionID());
            String afterRecords = requisitionService.getTotalRequisitions(requisition);

            int beforeRequisitions = Integer.parseInt(beforeRecords);
            int afterRequisitions = Integer.parseInt(afterRecords);

            if (list.get(i).getPositionName().contains("active")) {
                if (afterRequisitions == (beforeRequisitions + 1))
                    Reporter("Requisition can be raised for active positions As Expected", "PASS");
                else
                    Reporter("Requisition cannot be raised for active positions NOT As Expected", "FAIL");

            }

            if (list.get(i).getPositionName().contains("archived")) {
                if (afterRequisitions == beforeRequisitions)
                    Reporter("Requisition cannot be raised for archived positions As Expected", "PASS");
                else
                    Reporter("Requisition can be raised for archived positions NOT As Expected", "FAIL");

            }

            if (list.get(i).getPositionName().contains("occupied")){
                if(afterRequisitions == beforeRequisitions)
                Reporter("Requisition cannot be raised for occupied positions As Expected", "PASS");
            else
                Reporter("Requisition can be raised for occupied positions NOT As Expected", "FAIL");
             }

            if(list.get(i).getPositionName().contains("noHire")) {
                if (afterRequisitions == beforeRequisitions)
                    Reporter("Requisition cannot be raised for no Hire Status for a position As Expected", "PASS");
                else
                    Reporter("Requisition can be raised for no Hire Status for a position NOT As Expected", "FAIL");

            }

        }





    }

    }
