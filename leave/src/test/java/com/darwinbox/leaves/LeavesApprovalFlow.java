package com.darwinbox.leaves;

import Objects.Employee;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Objects.RoleHolders;
import Service.*;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;

import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.http.NameValuePair;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LeavesApprovalFlow extends LeaveBase {


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
    public void verifyRestrictions(Map<String, String> testData) {

        level1Action=testData.get("Level1_Action");
        level2Action=testData.get("Level2_Action");
        level3Action=testData.get("Level3_Action");

        level1LeaveAction = testData.get("Level1_LeaveAction");
        level2LeaveAction = testData.get("Level2_LeaveAction");
        level3LeaveAction = testData.get("Level3_LeaveAction");

        ApprovalFlow approvalFlowObject = getApprovalFlow(testData.get("ApprovalFlowName"));

        LeavePolicyObject autoApprovalFlowPolicy = new LeavePolicyObject();
        autoApprovalFlowPolicy.setAssignment_Type("company wise");
        autoApprovalFlowPolicy.setLeave_Type("AutoApprovalFlowPolicy");
        autoApprovalFlowPolicy.setGroup_Company("Working Days (DO NOT TOUCH)");
        autoApprovalFlowPolicy.setDescription("AutomationCreatedLeavePolicy");
        autoApprovalFlowPolicy.setMaximum_leave_allowed_per_year(12);
        autoApprovalFlowPolicy.setApprovalFlow(approvalFlowObject);

        List<NameValuePair> req = autoApprovalFlowPolicy.createRequest();
        new LeaveService().createLeaveForPolicy(req, autoApprovalFlowPolicy);

        employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");

        if(testData.get("Level 1")!="")
            setLevelManager(approvalFlowObject.getLevel1());

        if(testData.get("Level 2")!="")
            setLevelManager(approvalFlowObject.getLevel2());

        if(testData.get("Level 3")!="")
            setLevelManager(approvalFlowObject.getLevel3());

        double balanceBeforeApplyingLeave = new LeaveBalanceAPI(employee.getEmployeeID(), autoApprovalFlowPolicy.getLeave_Type()).getBalance();

        if(testData.get("ApplyLeave").equalsIgnoreCase("emp"))
        applyLeaveResponse = applyLeaveWithEmpSession(employee, autoApprovalFlowPolicy, LocalDate.now(), LocalDate.now());

        if(testData.get("ApplyLeave").equalsIgnoreCase("onbehalf"))
        applyLeaveResponse = applyLeave(employee, autoApprovalFlowPolicy, LocalDate.now(), LocalDate.now());

        if(applyLeaveResponse.contains("success"))
            Reporter("Leave Applied Successfully..","Info");
        else
            throw new RuntimeException("Unable to apply Leave");

        Reporter("Intial Leave Balance "+balanceBeforeApplyingLeave,"Info");
        messageId = new LeaveAdmin().getMessageId(employee, autoApprovalFlowPolicy, LocalDate.now().toString());


        if(level1Action!=null && level1Action!="")
            setLevelAction(level1Action,level1LeaveAction,approvalFlowObject.getLevel1());

        if(level2Action!=null && level2Action!="")
            setLevelAction(level2Action,level2LeaveAction,approvalFlowObject.getLevel2());

        if(level3Action!=null && level3Action!="")
            setLevelAction(level3Action,level3LeaveAction,approvalFlowObject.getLevel3());

        double balanceAfterApplyingLeave = new LeaveBalanceAPI(employee.getEmployeeID(), autoApprovalFlowPolicy.getLeave_Type()).getBalance();
        Reporter("Balance after Approvers Actions"+balanceAfterApplyingLeave,"Info");


       deductLeave=verifyLeaveDeduction(level1LeaveAction);
       deductLeave=verifyLeaveDeduction(level2LeaveAction);
       deductLeave=verifyLeaveDeduction(level3LeaveAction);


       if(deductLeave)
           Assert.assertTrue(balanceBeforeApplyingLeave>balanceAfterApplyingLeave,"Expect Leave to be deducted after approval flow is accepted");
       if(!deductLeave)
           Assert.assertTrue(balanceBeforeApplyingLeave==balanceAfterApplyingLeave,"Expect Leave to be same after approval flow is declined");


    }


    public boolean verifyLeaveDeduction(String levelAction){
        if (levelAction != "") {
            if(levelAction.equalsIgnoreCase("accept"))
                return true;
            if(levelAction.equalsIgnoreCase("decline"))
                return false;
        }
        return deductLeave;
    }

    public void setLevelManager(ApprovalLevels approvalLevel){
        for (String roleHolder : approvalLevel.getRoleHolders()) {
            if (roleHolder.equalsIgnoreCase(RoleHolders.l1Manager) && employee.getL1Manager()==null) {
                l1Manager = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
                assignManagerToEmployee(employee, l1Manager, LocalDate.now());
            }
            if (roleHolder.equalsIgnoreCase(RoleHolders.l2Manager)) {
                if (employee.getL1Manager()==null) {
                    l1Manager = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
                    assignManagerToEmployee(employee, l1Manager, LocalDate.now());
                }
                if(l1Manager.getL1Manager()==null) {
                    l2Manager = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
                    assignManagerToEmployee(l1Manager, l2Manager, LocalDate.now());

                    Reporter(l2Manager.getFirstName()+"is assigned as L2Manager to "+employee.getFirstName(),"Info");

                }
            }

            if(roleHolder.equalsIgnoreCase(RoleHolders.hod)){
                if(hod==null)
                    hod=employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");

               String response= new LeaveAdmin().updateDepartment(employee.getDepartmentId(),"Account",hod);
               if(response.contains("success")){
                   Reporter(hod.getFirstName()+"----"+hod.getEmployeeID()+"--is assigned as HOD to the department of Employee","Info");
               }
               else
                   throw  new RuntimeException("Error in updating HOD to the employee department");
            }
        }
    }


    public void setLevelAction(String levelAction,String leaveAction, ApprovalLevels approvalLevel) {
        //String leave1Action = null;
        if (levelAction.equalsIgnoreCase("roleHolder")) {
            String roleHoler = approvalLevel.getRoleHolders().get(new Random().nextInt(approvalLevel.getRoleHolders().size()));

            //  leave1Action =testData.get("Level1_LeaveAction");
            String response=null;
            if (roleHoler.equalsIgnoreCase(RoleHolders.l1Manager))
               response= new LeaveAdmin().leaveAction(l1Manager, messageId, leaveAction);

            if (roleHoler.equalsIgnoreCase(RoleHolders.l2Manager))
                response=new LeaveAdmin().leaveAction(l2Manager, messageId, leaveAction);

            if (roleHoler.equalsIgnoreCase(RoleHolders.admin))
                response=new LeaveAdmin().leaveAction(null, messageId, leaveAction);

            if (roleHoler.equalsIgnoreCase(RoleHolders.hod))
                response=new LeaveAdmin().leaveAction(hod, messageId, leaveAction);

            if(response.contains("success"))
                Reporter("RoleHolder--- "+roleHoler+"----"+ leaveAction +"Leave Application","Info");
            else
                throw new RuntimeException("UNable to perform "+leaveAction);

        }

        if (levelAction.equalsIgnoreCase("emp")) {
            Employee employee = approvalLevel.getEmployees().get(new Random().nextInt(approvalLevel.getEmployees().size()));

            //leave1Action = testData.get("Level2_LeaveAction");

            String response=new LeaveAdmin().leaveAction(employee, messageId, leaveAction);

            if(response.contains("success"))
                Reporter("Employee---"+employee.getFirstName()+  "("+  employee.getEmployeeID() +")    "+ leaveAction +"Leave Application","Info");
            else
                throw new RuntimeException("UNable to perform "+leaveAction);

        }
    }
}
