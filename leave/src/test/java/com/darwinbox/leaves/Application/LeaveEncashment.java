package com.darwinbox.leaves.Application;

import Objects.*;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.EmployeeServices;
import Service.LeaveAdmin;
import Service.LeaveService;
import Service.*;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.Utils.ERROR_MESSAGES;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class LeaveEncashment extends LeaveBase {

    EmployeeServices employeeServices;
    LeaveService leaveService;
    LeavePolicyObject leaveEncashmentPolicy;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;


    LeavesPage leavePage;
    LoginPage loginpage;
    CommonAction commonAction;

    Employee employee;
    String employeeProbation = null;
    String[] leaves = new String[]{"randomLeaveType1", "randomLeaveTyp2"};
    Clubbings clubbing = null;
    String action = null;
    String mongoIdForCompOff = "5cc278f8d61ea";
    String empIDForCompOff = "compoff";
    Boolean CompOff = false;


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

        defaultBody = leaveService.getDefaultforLeaveDeduction();
        employeeProbation = UtilityHelper.getProperty("employeeConfig", "employeeProbation");

        leavePage = PageFactory.initElements(driver, LeavesPage.class);
        employeeServices = new EmployeeServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void leaveEncashment(Map<String, String> testData) {

        leaveEncashmentPolicy = new LeavePolicyObject();
        leaveEncashmentPolicy.setFields(testData);
        leaveEncashmentPolicy.setEncashmentData(testData);
        List<NameValuePair> body = leaveEncashmentPolicy.createRequest();
        new LeaveService().createLeaveForPolicy(body, leaveEncashmentPolicy);


        employee = new EmployeeServices().generateAnEmployee("no",leaveEncashmentPolicy.getGroup_Company(),"random","no");

        double totalLeaves=new LeaveBalanceAPI(employee.getEmployeeID(),leaveEncashmentPolicy.getLeave_Type()).getTotalBalance();

        double leaveBalnce=new LeaveBalanceAPI(employee.getEmployeeID(),leaveEncashmentPolicy.getLeave_Type()).getBalance();

        double carryForwardBalance=new LeaveBalanceAPI(employee.getEmployeeID(),leaveEncashmentPolicy.getLeave_Type()).getCarryForwardBalance();

        double expectedEncashedLeves=0.0D;

        String error=null;

        double noOfLeaves=Double.parseDouble(testData.get("noOfLeaves"));

        if(leaveEncashmentPolicy.getLeaveEncashment().indicatoer){
            if(leaveEncashmentPolicy.getLeaveEncashment().encashAll.equalsIgnoreCase("carryforward")){
                expectedEncashedLeves=carryForwardBalance;
            }
            if(leaveEncashmentPolicy.getLeaveEncashment().encashAll.equalsIgnoreCase("both")) {
                expectedEncashedLeves=totalLeaves;
            }

            if(leaveEncashmentPolicy.getLeaveEncashment().encashAll.equalsIgnoreCase("accured")) {
                expectedEncashedLeves=leaveBalnce;
            }

            if(noOfLeaves>=leaveEncashmentPolicy.getLeaveEncashment().Encash_Min)
            {
                expectedEncashedLeves=expectedEncashedLeves;
            }
            else
            {
                error="Cannot apply for leave encashment as minimum leaves to be encash is "+leaveEncashmentPolicy.getLeaveEncashment().Encash_Min;
            }

            if(noOfLeaves>=leaveEncashmentPolicy.getLeaveEncashment().Encash_Max &&  leaveEncashmentPolicy.getLeaveEncashment().Encash_Max!=0)
            {
                error="Cannot apply for leave encashment more than allowed balance to encash";
            }
            if(!(leaveBalnce-expectedEncashedLeves>=leaveEncashmentPolicy.getLeaveEncashment().minLeaveBalance))
            {
                error="Cannot apply for leave encashment as minimum balance to retain after encashment is "+leaveEncashmentPolicy.getLeaveEncashment().minLeaveBalance;
            }



        }


        if(expectedEncashedLeves<noOfLeaves)
            error = "Cannot apply for leave encashment due to low balance";
        //apply for encashment
        String leaveID = leaveService.getLeaveID(leaveEncashmentPolicy.getLeave_Type(), leaveEncashmentPolicy.groupCompanyMongoId);

        String response=new LeaveAdmin().applyEncashmentWithEmpSession(employee,LocalDate.now().toString(),noOfLeaves+"",leaveID);

        if(error!=null){
            if(response.contains(error))
                Reporter("Pass -- response contains"+error,"PASS");
            else
                Reporter("Fail -- response doesnot contains "+error,"Fail");

        }
        else
        {
            if(response.contains("success"))
                Reporter("Pass -- response contains success","PASS");
            else
                Reporter("Fail -- response doesnot contains success","Fail");

        }


    }
}