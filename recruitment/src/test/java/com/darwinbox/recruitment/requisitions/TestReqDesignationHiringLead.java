package com.darwinbox.recruitment.requisitions;

import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.recruitment.RequisitionTestBase;
import com.darwinbox.recruitment.objects.Designations;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.RequisitionService;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestReqDesignationHiringLead extends RequisitionTestBase {


    LoginPage loginPage;
    EmployeeServices employeeServices;
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
        employeeServices = new EmployeeServices();
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

        employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
        Reporter("Emp is created " + employee.getUserID(), "INFO");

       designations= requisitionTestBase.createDesignations(type);

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

        requisition = requisitionTestBase.desgHirLead(type,designations);

        requisition.toObjectSearch(employee.getFirstName()+" "+employee.getLastName());
        String reqID = requisitionService.searchRequisition(requisition);
        Assert.assertNotNull(reqID,"Requisition is NOT created");

        if(reqID!=null) {
            Reporter("Requisition ID created is ::" + reqID, "INFO");
             Reporter("Designation Hiring Lead can raise Req as Expected","PASS");
        }
        else
            Reporter("Requisition ID is not captured","ERROR");


        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(data.get("@@admin"), data.get("@@password"));
        driver.navigate().refresh();

        //  Designations assigned to him as Hiring Lead
        /*in desgn D1- there is hir lead h1
        *  in desgn d2 - there is no hir lead
        *  if hir lead logins in to raise req, d2 should not be displayed , d1 only gets displayed in the list*/

        Services services = new Services();
        String gcName = "Working Days (DO NOT TOUCH)";
        JSONObject obj=  services.getDesignations(services.getGroupCompanyIds().get(gcName));

        Object[] keys = obj.keySet().toArray();
        for (int i=0;i<keys.length;i++) {

            JSONObject kv = obj.getJSONObject((String) keys[i]);
            Object[] designation = kv.keySet().toArray();
            if(!designation[i].toString().equalsIgnoreCase(designations.getId())
                    &&(!designation[i].toString().equalsIgnoreCase(employee.getDesignationID()))){

                designations.setId(designation[i].toString());
                Reporter("Selected another designation is ::"+designations.getId(),"INFO");
                 break;
            }

        }
        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

        String beforeRecords = requisitionService.getTotalRequisitions(requisition);
        requisition = requisitionTestBase.desgHirLead(type,designations);
        String afterRecords = requisitionService.getTotalRequisitions(requisition);

        int beforeRequisitions = Integer.parseInt(beforeRecords);
        int afterRequisitions = Integer.parseInt(afterRecords);

        if(afterRequisitions==(beforeRequisitions+1))
            Reporter("Designation Hiring Lead can raise for other Designations, NOT as Expected","FAIL");
        else
            Reporter("Designation Hiring Lead Cannot raise for other Designations, As Expected","PASS");







    }
    }
