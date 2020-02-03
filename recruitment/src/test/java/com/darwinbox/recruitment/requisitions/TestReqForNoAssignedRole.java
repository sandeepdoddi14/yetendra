package com.darwinbox.recruitment.requisitions;

import com.darwinbox.Services;
import com.darwinbox.core.employee.objects.Department;
import com.darwinbox.core.services.DepartmentServices;
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

public class TestReqForNoAssignedRole extends RequisitionTestBase {

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

        employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
        Reporter("Emp is created " + employee.getUserID(), "INFO");

        designations = requisitionTestBase.defaultRecruitmentSettings(type);

        //other than above des 's dept

        Services services = new Services();
        String gcName = "Working Days (DO NOT TOUCH)";
        Object[] departments = services.getDepartments(services.getGroupCompanyIds().get(gcName)).values().toArray();

        for(Object dep : departments){

            String deptFound = dep.toString().replace("DEPT_","");

            if(!deptFound.equalsIgnoreCase(employee.getDepartmentId())){
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
            Reporter("Access Role Holder can raise Requisition for other Dept., NOT as Expected","FAIL");
        else
            Reporter("Access Role Holder Cannot raise Requisition for other Dept, As Expected","PASS");


    }
    }
