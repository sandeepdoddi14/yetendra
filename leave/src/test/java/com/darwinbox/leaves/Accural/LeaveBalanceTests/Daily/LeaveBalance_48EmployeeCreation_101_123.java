package com.darwinbox.leaves.Accural.LeaveBalanceTests.Daily;


import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Utils.LeaveAccuralBase;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LeaveBalance_48EmployeeCreation_101_123 extends LeaveAccuralBase {

    List<Employee> employees = new ArrayList<>();

    LoginPage loginpage = null;
    CommonAction commonAction = null;
    Boolean runTest = true;

    static LocalDate serverDateInFormat=null;


    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

    }

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyLeaveBalance(Map<String, String> testData) {
        if (runTest) {



            List<LeavePolicyObject> leaveBalancePolicies = getLeaveBalancePolicies("101-123");

            leaveCycleStartDate = LocalDate.parse("2019-04-01");
            leaveCycleEndDate = LocalDate.parse("2020-03-31");
            LocalDate doj = leaveCycleEndDate;

            new DateTimeHelper().changeServerDate(driver, LocalDate.now().toString());
            while (doj.isAfter(leaveCycleStartDate)) {
                try{
                    if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {
                       employees.add(new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", doj.toString(), "no"));
                    }
                }
                catch (Exception e){
                    try {
                        if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {
                           employees.add(new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", doj.toString(), "no"));
                        }
                    }
                    catch(Exception e1){

                        try{
                        if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {
                           employees.add(new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", doj.toString(), "no"));
                        }}
                        catch (Exception e2){
                            try {
                                if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {
                                    employees.add(new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", doj.toString(), "no"));
                                }
                            }catch(Exception e3){
                                if (new LeavesAction().iterationDateFourTimesPerMonth(doj) == true) {
                                    employees.add(new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", doj.toString(), "no"));
                                }

                            }
                        }
                    }
                }

                doj = doj.minusDays(1);
            }


            serverChangedDate = leaveCycleEndDate.toString();
            new DateTimeHelper().changeServerDate(driver, leaveCycleEndDate.toString());


            serverDateInFormat  = LocalDate.parse(serverChangedDate);

            double actualLeaveBalance=0.0D;
            double expecetedLeaveBalance=0.0D;



            while (serverDateInFormat.isAfter(leaveCycleStartDate)) {
                if (new LeavesAction().iterationDateFourTimesPerMonth(serverDateInFormat) == true) {
                    serverChangedDate = serverDateInFormat.toString();
                    new DateTimeHelper().changeServerDate(driver, serverDateInFormat.toString());
                    for (LeavePolicyObject leavePolicyObject : leaveBalancePolicies) {
                       for (Employee employee : employees) {
                            if (LocalDate.parse(employee.getDoj()).isBefore(serverDateInFormat)) {
                                super.setEmployee(employee);
                                Reporter("Employee is" + employee.getEmployeeID() + "...DOJ is ....." + employee.getDoj(), "info");
                                super.setLeavePolicyObject(leavePolicyObject);
                                Reporter("Leave Type is" + leavePolicyObject.getLeave_Type(), "Info");
                                Reporter("Server Date is" + serverDateInFormat.toString(), "Info");


                                //making sure it wont fail on exception bec of JSON PARSER
                                try {
                                    actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
                                }
                                catch (Exception e){
                                    try {
                                        actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
                                    }
                                    catch (Exception e1){
                                        actualLeaveBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();

                                    }
                                }
                                Reporter("Actual Leave Balance  is  ..." + actualLeaveBalance, "Info");
                                expecetedLeaveBalance=calculateLeaveBalance(employee.getDoj(), getServerOrLocalDate().toString());
                                Reporter("Expected Leave Balance is " + expecetedLeaveBalance, "Info");
                                   if(actualLeaveBalance==expecetedLeaveBalance)
                                    Reporter("Passed || actual leave balance and expected are same","Pass");
                                else
                                    Reporter("FAILED || actual leave balance and expected are not same","Fail");

                            }
                       }
                    }
                }
                serverDateInFormat=serverDateInFormat.minusDays(1);

            }
            runTest = false;
        }

/*            super.setEmployee(employees.get(0));
            for (LeavePolicyObject leavePolicyObject : leaveBalancePolicies) {
                super.setLeavePolicyObject(leavePolicyObject);

                if (!checkDOJisUnderLeaveProbationPeriod()) {
                    Reporter("Actual Leave Balance  is  " + leavePolicyObject.getLeave_Type() + "...." + new LeaveBalanceAPI(employees.get(0).getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance(), "Info");
                    Reporter("Leave Balance is " + calculateLeaveBalance(employees.get(0).getDoj(), getServerOrLocalDate().toString()), "Info");



                } else
                    Reporter("Employee is in probation...", "Info");
            }
        }
 */
        //}
    }
}
