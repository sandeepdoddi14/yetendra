package com.darwinbox.leaves.ProductionIssues.DBX_8612;


import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;

import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class createRequestsForEmp extends LeaveAccuralBase {

    Employee employee = new Employee();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;


    LocalDate doj = null;

    LeavesAction leavesAction = null;

    LocalDate deActicvationDate = null;
    Double ExpectedLeaveBalance = 0.0D;
    Double finalMonthBalance = null;


    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");
        leavesAction = new LeavesAction(driver);

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void createRequests(Map<String, String> testData) {
        LeavePolicyObject multipleAllotmentLeavePolicy = getCarryForwardPolicy(testData);
        super.setLeavePolicyObject(multipleAllotmentLeavePolicy);

        Reporter("Leave Type is"+multipleAllotmentLeavePolicy.getLeave_Type(),"Info");



        //to generate employee
        //it will create a full time employee
        changeServerDate(LocalDate.now().toString());
        try {
            employee= new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", serverDateInFormat.minusDays(15).toString(), "no");
        } catch (Exception e) {
            try {
                employee= new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", serverDateInFormat.minusDays(15).toString(), "no");
            } catch (Exception e1) {
                employee= new EmployeeServices().generateAnFullTimeEmployee("no", "Working Days (DO NOT TOUCH)", serverDateInFormat.minusDays(15).toString(), "no");

            }
        }

        super.setEmployee(employee);

        Reporter("Employee DOJ is ---->" + employee.getDoj(), "Info");



        for(int i=0 ; i<13000;i++){
            logoutFromSession();

            try {


                loginpage.loginToApplication(employee.getEmailID(), employee.getPassword());
            }
            catch(Exception e){

            }

            applyLeaveWithEmpSession(employee,multipleAllotmentLeavePolicy,LocalDate.now().minusDays(0),LocalDate.now().minusDays(0));


            logoutFromSession();
            loginpage.loginToApplication();
            loginpage.switchToAdmin();

            String messageId = new LeaveAdmin().getMessageId(employee, multipleAllotmentLeavePolicy, LocalDate.now().toString());

            new LeaveAdmin().leaveAction(null, messageId, "decline");
            System.out.println("i -->"+i);
        }


    }
}

