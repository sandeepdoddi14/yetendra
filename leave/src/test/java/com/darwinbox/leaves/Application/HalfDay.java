package com.darwinbox.leaves.Application;


import Objects.Employee;
import Objects.Holiday;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.*;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

public class HalfDay extends LeaveBase {

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

        leavePage = PageFactory.initElements(driver,LeavesPage.class);
        employeeServices = new EmployeeServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void Allow_HalfDay(Map<String, String> data) {
        leavePolicyObject = new LeavePolicyObject();
        Map<String,String> scenario=HalfDayScenarios(data.get("Allow_half-day"));

        mandatoryFields = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);
        mandatoryFields.put("LeavePolicy_HalfDays[status]", "1");
        defaultBody.putAll(mandatoryFields);

        leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

        employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), "random", "no");

        if(scenario.get("Scenario").equalsIgnoreCase("halfday")) {
            Reporter("Info -- Scenario Applying Leave On"+LocalDate.now()    +   scenario.get("Step"),"Info");

            double balanceBefore = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
            Reporter("Info -- Leave Banlance Before Applying Leave is     "+balanceBefore ,"Info");

            String response = applyLeave(employee, leavePolicyObject,LocalDate.now(), scenario.get("Step"));
            if (response.contains("failure"))
                Reporter("Fail -- Unable to Apply Leave on "+scenario.get("Step")+" For the Employee  -" + employee.getFirstName()
                                + "  Leave Date -" + LocalDate.now().toString()
                        , "Fail");

            else
                Reporter("Pass --Leave Applied Successfully on "+scenario.get("Step")+" For the Employee  -" + employee.getFirstName()
                                + "  Leave Date -" + LocalDate.now().toString()
                        , "Pass");

            double balanceAfter = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
            Reporter("Info -- Leave Banlance After Applying Leave is     "+balanceAfter ,"Info");
        }


        if(scenario.get("Scenario").equalsIgnoreCase("Holiday")) {
            Reporter("Info --Scenario -- Applying Half Day Leave On Holiday", "Info");

            Holiday holiday = new Holiday();
            holiday.setDate(LocalDate.now().toString());
            holiday.setName("testHalfDayLeave");
            holiday.setOptional(false);

            String createHolioday = new HolidayService().createHoliday(holiday);

            if (!createHolioday.contains("failure")) {
                Reporter("Info  --Holiday Response" + createHolioday, "Info");
                Reporter("Info -- Holiday Created Successfully On" + LocalDate.now().toString(), "Info");
            } else {
                Reporter("Error --Holiday Response" + createHolioday, "Error");
                Reporter("Error -- Unable to create Holoiday On" + LocalDate.now().toString(), "Error");
            }

            employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), "random", "no");

            double balanceBeforeHoliday = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
            Reporter("Info -- Leave Balance Before applying Leave For Employee   " + employee.getFirstName() + "On Holiday is"
                    + balanceBeforeHoliday, "Info");


            String firstHalfHolidayResponse = applyLeave(employee, leavePolicyObject,LocalDate.now(), scenario.get("Step"));
            if (firstHalfHolidayResponse.contains("failure"))
                Reporter("Fail -- Unable to Apply Leave on Holiday,"+scenario.get("Step")+" For the Employee  -" + employee.getFirstName()
                                + "  Leave Date -" + LocalDate.now().toString()
                        , "Fail");

            else if (balanceBeforeHoliday == new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance())
                Reporter("Pass --Leave Applied On Holiday,"+scenario.get("Step")+ " For the Employee  -" + employee.getFirstName()
                                + "  Leave Date -" + LocalDate.now().toString()
                        , "Pass");

            Reporter("Info -- Leave Balance After applying Leave On Holiday,"+scenario.get("Step")+" is"
                    + new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance(), "Info");

        }

        if(scenario.get("Scenario").equalsIgnoreCase("WeeklyOff")) {
            Reporter("Info --Scenario -- Applying Half Day Leave On Holiday", "Info");

            employee = employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(), "random", "no");

            String weeklyOffId = UtilityHelper.getProperty("employeeConfig","weeklyOffId");
            String assignWeekOff=employeeServices.createWeeklyOffForAnEmp(employee.getUserID(), weeklyOffId);
            Reporter("Info -- Assign Weekly Off Response is  "+assignWeekOff ,"Info");
            Reporter("Info -- Weekly Off Assigned to the User Successfully","Info");

            LocalDate today=LocalDate.now();
            while(today.getDayOfWeek().toString().equalsIgnoreCase("saturday") ||
                    today.getDayOfWeek().toString().equalsIgnoreCase("sunday"))
            {
                today=LocalDate.now().plusDays(1);
            }

            Reporter("Info -- Leave Balance before applying Leave On Week Off,"+scenario.get("Step")+" is"
                    + new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance(), "Info");

            double balanceBeforeWeekOff = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();

            String response=applyLeave(employee, leavePolicyObject,today,scenario.get("Step"));

            if (response.contains("failure"))
                Reporter("Fail -- Unable to Apply Leave  On Week Off,"+scenario.get("Step")+" For the Employee  -" + employee.getFirstName()
                                + "  Leave Date -" + LocalDate.now().toString()
                        , "Fail");

            else if (balanceBeforeWeekOff == new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance())
                Reporter("Pass --Leave Applied Successfully On Week Off,"+scenario.get("Step")+" For the Employee  -" + employee.getFirstName()
                                + "  Leave Date -" + LocalDate.now().toString()
                        , "Pass");

            Reporter("Info -- Leave Balance After applying Leave On Week Off,"+scenario.get("Step")+
                    + new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance(), "Info");

        }

    }
}
