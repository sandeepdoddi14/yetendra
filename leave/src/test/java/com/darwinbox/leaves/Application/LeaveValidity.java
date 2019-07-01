package com.darwinbox.leaves.Application;

import Objects.Employee;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.EmployeeServices;
import Service.LeaveAdmin;
import Service.LeaveService;
import Service.Service;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.Utils.ERROR_MESSAGES;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class LeaveValidity extends LeaveBase {


    EmployeeServices employeeServices;
    LeaveService leaveService;
    LeavePolicyObject leavePolicyObject;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;


    LeavesPage leavePage;
    LoginPage loginpage;
    CommonAction commonAction;

    Employee employee;
    String employeeProbation = null;

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
    public void LeaveValidity(HashMap<String, String> data) {

        String field = data.get("Field");
        leavePolicyObject = new LeavePolicyObject();

        if (field.equalsIgnoreCase("customMonths") || field.equalsIgnoreCase("dontshowandallowinprobationperiod")) {
            mandatoryFields = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);
            mandatoryFields.put("Leaves[p1_waiting_after_doj_status]", "1");
            mandatoryFields.put("Leaves[p1_waiting_after_doj]", data.get("Probation_period_before_leave_validity_months"));

            if (field.contains("dontshowandallowinprobationperiod"))
                mandatoryFields.put("Leaves[dont_show_in_probation_period]", "1");

            defaultBody.putAll(mandatoryFields);

            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

            String dateOfJoining = null;
            int duration = 0;
            String customMonths = data.get("Probation_period_before_leave_validity_months");
            duration = Integer.parseInt(customMonths.replace(".0", ""));
            dateOfJoining = LocalDate.now().minusMonths(duration).plusDays(1).toString();

            employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), dateOfJoining, "no");
            leavePage.setFromAndToDatesWithoutProperty(1, LocalDate.now());


            if (field.contains("dontshowandallowinprobationperiod")) {
                if (!new LeaveAdmin().GetLeavesByOnBehalf(employee.getMongoID()).keySet().contains(leavePolicyObject.getLeave_Type()))
                    Reporter("PASS -- Leave type is not shown to user with DOJ =" + dateOfJoining + "Custom Months =" + duration + "     With Check Box " +
                            "  Dont Show and Apply in Probation Enabled", "Pass");
                else if (new LeaveAdmin().GetLeavesByOnBehalf(employee.getMongoID()).keySet().contains(leavePolicyObject.getLeave_Type()))
                    Reporter("FAIL -- Leave type is shown to user with DOJ =" + dateOfJoining + "Custom Months =" + duration + "     With Check Box " +
                            "  Dont Show and Apply in Probation Enabled", "Pass");

            }

            if (!field.contains("dontshowandallowinprobationperiod")) {
                if (leavePolicyObject.leaveValidtyCustomMonths(employee) == false) {
                    String fail = applyLeave(employee, leavePolicyObject,leavePage.workingDays);
                    if (fail.contains(ERROR_MESSAGES.OverUtilization)) {
                        Reporter("PASS -- Error Message Is Verfied for Over Utilizattion Employee DOJ is    " + employee.getDoj() +
                                "----Custom Month is   " + duration, "Pass");
                    } else {
                        Reporter("FAIL -- Error Message Is not for Over Utilizattion Employee DOJ is    " + employee.getDoj() +
                                "----Custom Month is   " + duration, "Fail");
                    }
                }
                dateOfJoining = LocalDate.now().minusMonths(duration).minusDays(1).toString();
                employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), dateOfJoining, "no");
                leavePage.setFromAndToDatesWithoutProperty(1, LocalDate.now());

                if (leavePolicyObject.leaveValidtyCustomMonths(employee) == true) {
                    String pass = applyLeave(employee, leavePolicyObject,leavePage.workingDays);
                    if (pass.contains("success")) {
                        Reporter("PASS -- Leave Applied Successfully Employee DOJ is    " + employee.getDoj() +
                                "----Custom Month is   " + duration, "Pass");
                    } else {
                        Reporter("FAIL -- Unable to apply Leave Employee DOJ is    " + employee.getDoj() +
                                "----Custom Month is   " + duration, "Fail");
                    }

                }
            }
        }

        if (field.equalsIgnoreCase("probation") || field.equalsIgnoreCase("dontshowandallowinprobationperiod")) {
            mandatoryFields = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);
            mandatoryFields.put("Leaves[p1_waiting_after_doj_status]", "0");

            if (field.contains("dontshowandallowinprobationperiod"))
                mandatoryFields.put("Leaves[dont_show_in_probation_period]", "1");

            defaultBody.putAll(mandatoryFields);
            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

            String empDateOfJoining = null;
            HashMap<String, String> probation = new Service().getProbations().get(employeeProbation);
            String ProbationID = probation.values().toString().replace("[", "").replace("]", "");
            int probationDays = Integer.parseInt(probation.keySet().toString().replace("[", "").replace("]", ""));
            empDateOfJoining = LocalDate.now().minusDays(probationDays).plusDays(2).toString();
            leavePage.setFromAndToDatesWithoutProperty(1, LocalDate.now());

            employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), empDateOfJoining, ProbationID);

            if (field.contains("dontshowandallowinprobationperiod")) {
                if (!new LeaveAdmin().GetLeavesByOnBehalf(employee.getMongoID()).keySet().contains(leavePolicyObject.getLeave_Type()))
                    Reporter("PASS -- Leave type is not shown to user with DOJ =" + empDateOfJoining + "Probation period =" + probationDays + "     With Check Box " +
                            "  Dont Show and Apply in Probation Enabled", "Pass");
                else if (new LeaveAdmin().GetLeavesByOnBehalf(employee.getMongoID()).keySet().contains(leavePolicyObject.getLeave_Type()))
                    Reporter("FAIL -- Leave type is shown to user with DOJ =" + empDateOfJoining + "Probation period =" + probationDays + "     With Check Box " +
                            "  Dont Show and Apply in Probation Enabled", "Pass");

            }

            if (!field.contains("dontshowandallowinprobationperiod")) {

                String errorReposne = applyLeave(employee, leavePolicyObject,leavePage.workingDays);
                if (leavePolicyObject.probation(employee) && errorReposne.contains(ERROR_MESSAGES.OverUtilization)) {
                    Reporter("PASS -- Error Message is Verified Successfully For the User  with DOJ =" + empDateOfJoining + "Probation period =" + probationDays + "     With Check Box " +
                            "  Dont Show and Apply in Probation Disabled", "Pass");
                } else {
                    Reporter("FAIL -- Unable to verify error Messages For the User with DOJ=" + empDateOfJoining + "Probation period =" + probationDays + "   With Check Box" +
                            "Dont Show And Apply in Probation Disabled", "Fail");
                }
                empDateOfJoining = LocalDate.now().minusDays(probationDays).minusDays(2).toString();
                employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), empDateOfJoining, ProbationID);
                String successResponse = applyLeave(employee, leavePolicyObject,leavePage.workingDays);
                if (!leavePolicyObject.probation(employee) && successResponse.contains("success")) {
                    Reporter("PASS --- Leave Applied Successfully For the User  With DOJ = " + empDateOfJoining + "Probation period = " + probationDays + "     With Check Box " +
                            "  Dont Show and Apply in Probation Disabled", "Pass");
                } else {
                    Reporter("FAIL --- Unable to apply leave for the User  With DOJ = " + empDateOfJoining + "Probation period = " + probationDays + "     With Check Box " +
                            "  Dont Show and Apply in Probation Disabled", "Fail");
                }
            }
        }


        if (field.equalsIgnoreCase("dontshowandallowinnoticeperiod")) {
            mandatoryFields = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);
            mandatoryFields.put("Leaves[allow_in_notice_period]", "1");
            defaultBody.putAll(mandatoryFields);

            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

            employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), "random", "no");

            HashMap<String, String> requestBody = new HashMap<String, String>();
            requestBody.put("user_id", employee.getUserID());
            requestBody.put("mongo_id", employee.getMongoID());
            requestBody.put("notice_period_days", "2");
            requestBody.put("notice_period_days_get", "2");
            // new DateTimeHelper().changeLocalDateToSeperationDateFormat(LocalDate.now())
            requestBody.put("Separation[date_of_resignation]", "17-04-2019");
            requestBody.put("Separation[requested_last_day]", "18-04-2019");
            requestBody.put("Separation[recovery_days]", "1");
            requestBody.put("Separation[manager_proposed_last_day]", "18-04-2019");
            requestBody.put("Separation[manager_proposed_recovery_days]", "1");
            requestBody.put("Separation[final_recovery_days]", "1");

            Map<String, String> empLeaves = new LeaveAdmin().GetLeavesByOnBehalf(employee.getMongoID());
            if (empLeaves.keySet().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Pass -- Leave Type  is Shown to Employee", "FAIL");
            } else if (!empLeaves.keySet().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Fail -- Leave Type  is Not Shown to Employee", "Pass");
            }

            new LeaveAdmin().resignOnBehalf(requestBody);

            Map<String, String> empLeavesAfterResignation = new LeaveAdmin().GetLeavesByOnBehalf(employee.getMongoID());
            if (empLeavesAfterResignation.keySet().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Fail -- Leave Type  is Shown to Employee", "FAIL");
            } else if (!empLeavesAfterResignation.keySet().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Pass -- Leave Type  is Not Shown to Employee", "Pass");
            }

        }
        if (field.contains("dontshowinfrontend")) {
            mandatoryFields = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);
            mandatoryFields.put("Leaves[dont_show_in_front_end]", "0");
            defaultBody.putAll(mandatoryFields);
            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

            driver.manage().deleteAllCookies();
            driver.navigate().refresh();
            loginpage.empLoginToApplication();
            leavePage.navigateToLeavePage();
            Reporter("INFO : Dont Show And Apply in front End is Disabled", "Info");
            if (driver.getPageSource().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Pass -- Leave Type is Shown in Front End", "PASS");
            } else if (!driver.getPageSource().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Fail -- Leave Type is Not Shown in Front End", "Fail");
            }
            driver.manage().deleteAllCookies();
            driver.navigate().refresh();

            loginpage.loginToApplication();
            mandatoryFields.put("Leaves[dont_show_in_front_end]", "1");
            defaultBody.putAll(mandatoryFields);
            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

            driver.manage().deleteAllCookies();
            driver.navigate().refresh();

            loginpage.empLoginToApplication();
            leavePage.navigateToLeavePage();


            Reporter("INFO : Dont Show And Apply in front End is Enabled", "Info");
            if (!driver.getPageSource().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Pass -- Leave Type is Not Shown in Front End", "PASS");
            } else if (driver.getPageSource().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Fail -- Leave Type is Shown in Front End", "Fail");
            }


        }

        if (field.contains("dontallowapplication") || field.contains("isthisspecialleave")) {
            mandatoryFields = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);

            if (field.contains("dontallowapplication")) {
                mandatoryFields.put("Leaves[dont_show_in_application]", "0");
                defaultBody.putAll(mandatoryFields);


                leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

                Map<String, String> leaves = leaveAdmin.GetLeavesByOnBehalf(UtilityHelper.getProperty("config", "Employee.mongoId"));

                Reporter("Info -- Check Box Dont Allow Application is Not Selected  For Policy " + leavePolicyObject.getLeave_Type(), "Info");
                if (leaves.keySet().contains(leavePolicyObject.getLeave_Type())) {
                    Reporter("Pass --" + leavePolicyObject.getLeave_Type() + "   is Enabled For User",
                            "Pass");
                } else {
                    Reporter("Fail --" + leavePolicyObject.getLeave_Type() + "   is Disabled For User",
                            "Fail");
                }
            }

            if(field.contains("isthisspecialleave")){
                mandatoryFields.put("Leaves[is_special_leave]","1");
            }

            mandatoryFields.put("Leaves[dont_show_in_application]", "1");
            defaultBody.putAll(mandatoryFields);

            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);
            Map<String, String> leavesForNegitiveCase = leaveAdmin.GetLeavesByOnBehalf(UtilityHelper.getProperty("config", "Employee.mongoId"));

            Reporter("Info -- Check Box    "+field+"     is Selected  For Policy " + leavePolicyObject.getLeave_Type(), "Info");
            if (!leavesForNegitiveCase.keySet().contains(leavePolicyObject.getLeave_Type())) {
                Reporter("Pass --" + leavePolicyObject.getLeave_Type() + "   is Disabled For User",
                        "Pass");
            } else {
                Reporter("Fail --" + leavePolicyObject.getLeave_Type() + "   is Enabled For User",
                        "Fail");
            }

        }
    }


}

