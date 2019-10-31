package com.darwinbox.leaves.Application;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class TestLeavePolicyCreation  extends LeaveBase {

    static List<LeavePolicyObject> policies;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;
    LeavesPage leavesPage;
    LoginPage loginpage;
    CommonAction commonAction;
    Employee employee;

    EmployeeServices employeeServices;
    LeaveService leaveService;


    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

        leaveService = new LeaveService();


        mapUtils = new MapUtils();
        leaveAdmin = new LeaveAdmin();
        if (policies == null)
            policies = getOverUtilizationPolicies();


        leavesPage = PageFactory.initElements(driver, LeavesPage.class);
        employeeServices = new EmployeeServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyPolicyCreation(Map<String, String> data) {

        for (LeavePolicyObject leavePolicyObject : policies) {
            driver.navigate().to(data.get("@@url") + "/settings/leaves");

            if(driver.getPageSource().toString().contains(leavePolicyObject.getLeave_Type())){
                Reporter("Leave Policy "+leavePolicyObject.getAssignment_Type()+"created Successfully","Info");
            }
            else{
                Reporter("ERROR IN CREATING LEAVE POLICY"+leavePolicyObject.getAssignment_Type(),"Info");
            }

        }
    }

    }
