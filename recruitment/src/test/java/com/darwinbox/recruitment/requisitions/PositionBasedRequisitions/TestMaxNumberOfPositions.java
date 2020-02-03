package com.darwinbox.recruitment.requisitions.PositionBasedRequisitions;

import com.darwinbox.core.employee.objects.DesignationNames;
import com.darwinbox.core.services.DesignationNamesServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.recruitment.RequisitionTestBase;
import com.darwinbox.recruitment.objects.Designations;
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

import java.util.Map;

public class TestMaxNumberOfPositions extends RequisitionTestBase {

    LoginPage loginPage;
    Requisition requisition;
    RequisitionService requisitionService;
    Designations designations;


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
        designations = new Designations();
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testRequisition(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        RequisitionTestBase requisitionTestBase = RequisitionTestBase.getObject("RequisitionBase.xlsx");
        String type = testData.get("reqType");

      requisition= requisitionTestBase.designationWithTimeStamp(type);

/*1.des with time stamp and num of pos
* 2.req with more pos than above
* 3.validate number of rec*/

        String beforeRecords = requisitionService.getTotalRequisitions(requisition);
        requisition = requisitionTestBase.desgHirLead(type, designations);
        String afterRecords = requisitionService.getTotalRequisitions(requisition);

        int beforeRequisitions = Integer.parseInt(beforeRecords);
        int afterRequisitions = Integer.parseInt(afterRecords);

        if(afterRequisitions==(beforeRequisitions+1))
            Reporter("Can raise Requisition more than limit set, NOT as Expected","FAIL");
        else
            Reporter("Cannot raise Requisition for more than limit set, As Expected","PASS");



    }
    }
