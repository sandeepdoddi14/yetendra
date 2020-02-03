package com.darwinbox.recruitment.requisitions;

import com.darwinbox.Services;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.recruitment.RequisitionTestBase;
import com.darwinbox.recruitment.objects.Designations;
import com.darwinbox.recruitment.objects.Permissions;
import com.darwinbox.recruitment.objects.Requisition;
import com.darwinbox.recruitment.services.PermissionService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestReqRecruitmentRestrictedPermission extends RequisitionTestBase {


    LoginPage loginPage;
    Permissions permissions;
    PermissionService permissionService;
    Designations designations;
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
        designations = new Designations();
        permissions = new Permissions();
        permissionService = new PermissionService();
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

        employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
        Reporter("Emp is created " + employee.getUserID(), "INFO");

        designations=  requisitionTestBase.updatePermissionRoleHolder(type);

         //raise req with des in dept updated in above permission (refer TestReqHOD)

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

        requisition = requisitionTestBase.desgHirLead(type, designations);
        requisition.toObjectSearch(employee.getFirstName() + " " + employee.getLastName());
        String reqID = requisitionService.searchRequisition(requisition);
        Assert.assertNotNull(reqID, "Requisition is NOT created");

        if (reqID != null) {
            Reporter("Requisition ID created is ::" + reqID, "INFO");
            Reporter("Permission roleHolder raised Requisition for same Dept. As Expected","PASS");
        }
        else
            Reporter("Requisition ID is not captured", "ERROR");

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(data.get("@@admin"), data.get("@@password"));
        driver.navigate().refresh();

        Services services = new Services();
        String gcName = "Working Days (DO NOT TOUCH)";
        Object[] departments = services.getDepartments(services.getGroupCompanyIds().get(gcName)).values().toArray();

        for(Object dep : departments){

            String deptFound = dep.toString().replace("DEPT_","");

            if(!deptFound.equalsIgnoreCase(designations.getDepartment())){
                Reporter("Selecting another dept","INFO");
                // update current desg with this dept
                designations= requisitionTestBase.updateDesWithDeptOtherThanExisting(deptFound,type);
                break;
            }
        }

        logoutFromSession();
        Thread.sleep(5000);
        loginPage.loginToApplication(employee.getEmailID(), employee.getPassword());
        driver.navigate().refresh();

        String beforeRecords = requisitionService.getTotalRequisitions(requisition);
        requisition = requisitionTestBase.desgHirLead(type, designations);
        String afterRecords = requisitionService.getTotalRequisitions(requisition);

        int beforeRequisitions = Integer.parseInt(beforeRecords);
        int afterRequisitions = Integer.parseInt(afterRecords);

        if(afterRequisitions==(beforeRequisitions+1))
            Reporter("Permission RoleHolder can raise for other Dept., NOT as Expected","FAIL");
        else
            Reporter("Permission RoleHolder Cannot raise for other Dept, As Expected","PASS");

    }
    }
