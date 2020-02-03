package com.darwinbox.recruitment.requisitions;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.recruitment.RequisitionTestBase;
import com.darwinbox.recruitment.objects.Designations;
import com.darwinbox.recruitment.objects.RecruitmentSettings;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.RecruitmentSettingsService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestReqForAssignedRole extends RequisitionTestBase {

    LoginPage loginPage;
    EmployeeServices employeeServices;
    RecruitmentSettings recruitmentSettings;
    RecruitmentSettingsService recruitmentSettingsService;
    Designations designations;
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
        recruitmentSettings = new RecruitmentSettings();
        recruitmentSettingsService = new RecruitmentSettingsService();
        designations = new Designations();
        requisition = new Requisition();
        requisitionService = new RequisitionService();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testRequisition(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        RequisitionTestBase requisitionTestBase = RequisitionTestBase.getObject("RequisitionBase.xlsx");
        String type = testData.get("reqType");

        /*1. set designation in rec settings
         * 2. create emp with same designation
         * 3.login with emp and should be able to raise req for all des in dept
         * 4.Emp with other des should not be able to raise req(in other test class??)*/


        //set one designation in testdata and create emp
        // or create emp randomly and test ??
        //update settings again with to access role and raise req with same emp

        employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
        Reporter("Emp is created " + employee.getUserID(), "INFO");


      designations = requisitionTestBase.defaultRecruitmentSettings(type);

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

      requisition= requisitionTestBase.desgHirLead(type,designations);
//write here for noassigned same as testHOD
        requisition.toObjectSearch(employee.getFirstName() + " " + employee.getLastName());
        String reqID = requisitionService.searchRequisition(requisition);
        Assert.assertNotNull(reqID, "Requisition is NOT created");

        if (reqID != null) {
            Reporter("Requisition ID created is ::" + reqID, "INFO");
            Reporter("Access role Holder can raise requisition As Expected","PASS");
        }
        else
            Reporter("Access role Holder cannot raise requisition NOT As Expected", "ERROR");


       /* String dept = requisitionTestBase.createDesignations("").getDepartment();

       dept.contains("");
*/

    }
    }
