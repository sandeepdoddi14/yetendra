package com.darwinbox.leaves;


import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.utils.SAS.SelTestCase;


import com.darwinbox.framework.utils.SAS.SelectorUtil;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestSelUtil extends SelTestCase {


    EmployeeServices employeeServices;
    LeaveService leaveService;
    LeavePolicyObject sandwitchLeavePolicy;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;

    LeavesPage leavePage;
    LoginPage loginpage;
    CommonAction commonAction;

    Employee employee;
    Employee l1Manager = null;
    Employee l2Manager = null;
    Employee hod=null;
    String messageId = null;
    String level1Action = null;
    String level2Action=null;
    String level3Action=null;
    String level1LeaveAction=null;
    String level2LeaveAction=null;
    String level3LeaveAction=null;
    Boolean deductLeave=false;
    String applyLeaveResponse=null;
    Boolean revoke=false;

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

        leavePage = new LeavesPage(driver);
        employeeServices = new EmployeeServices();
        leaveService = new LeaveService();
        sandwitchLeavePolicy = new LeavePolicyObject();
        mapUtils = new MapUtils();
        leaveAdmin = new LeaveAdmin();
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyRestrictions(Map<String, String> testData) throws Exception {


        String url = data.get("@@url")+"/recruitment/recruitment/requisitionstageone/id/5e3bb09486116/edit/1";
      driver.get(url);

        getCurrentFunctionName(true);
        List<String> subStrArr = new ArrayList<String>();
        List<String> valuesArr = new ArrayList<String>();
        //logs.debug(MessageFormat.format("", "checkout"));
        subStrArr.add("RaiseRequisition[employee_type]");
        valuesArr.add("Full Time");
        SelectorUtil.initializeSelectorsAndDoActions(subStrArr, valuesArr);
        getCurrentFunctionName(false);


        List<String> subStrArr1 = new ArrayList<String>();
        List<String> valuesArr2 = new ArrayList<String>();
        //logs.debug(MessageFormat.format("", "checkout"));
        subStrArr1.add("Agni, Karnataka\n" +
                ", India");
        valuesArr2.add("");
        SelectorUtil.initializeSelectorsAndDoActions(subStrArr1, valuesArr2);

    /*    List<String> subStrArr11 = new ArrayList<String>();
        List<String> valuesArr22 = new ArrayList<String>();
        //logs.debug(MessageFormat.format("", "checkout"));
        subStrArr11.add("chosen-container chosen-container-single chosen-with-drop chosen-container-active");
        valuesArr22.add("");
        SelectorUtil.initializeSelectorsAndDoActions(subStrArr11, valuesArr22);*/


        List<String> subStrArr111 = new ArrayList<String>();
        List<String> valuesArr222 = new ArrayList<String>();
        //logs.debug(MessageFormat.format("", "checkout"));
        subStrArr111.add("Select Office Location");
        valuesArr222.add("Agni");
        SelectorUtil.initializeSelectorsAndDoActions(subStrArr111, valuesArr222);


        List<String> subStrArr1111 = new ArrayList<String>();
        List<String> valuesArr2222 = new ArrayList<String>();
        //logs.debug(MessageFormat.format("", "checkout"));
        subStrArr111.add("submit");
        valuesArr222.add("");
        SelectorUtil.initializeSelectorsAndDoActions(subStrArr111, valuesArr222);





    }



    }

