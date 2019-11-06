package com.darwinbox.leaves.Application;


import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Holiday;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.HolidayService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class SandwitchLeaves extends LeaveBase {

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

        leavePage= new LeavesPage(driver);
        employeeServices= new EmployeeServices();
        leaveService = new LeaveService();
        sandwitchLeavePolicy = new LeavePolicyObject();
        mapUtils = new MapUtils();
        leaveAdmin = new LeaveAdmin();
    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyRestrictions(Map<String, String> testData) {

        String applicationType = testData.get("applicationType");
        int noOfPrefixLeaves = Integer.parseInt(testData.get("noOfPrefixLeaves"));
        int noOfSuffixLeaves = Integer.parseInt(testData.get("noOfSuffixLeaves"));

        String typeOfSandwitch = testData.get("typeOfSandwitch");
        int noOfSandwitchLeaves = Integer.parseInt(testData.get("noOfSandwitchLeaves"));

        sandwitchLeavePolicy=getSandwitchLeavePolicy(testData.get("Policy"));

        int totalLeaves = noOfPrefixLeaves + noOfSandwitchLeaves + noOfSuffixLeaves;

        //deleteAllHolidays();
        leavePage.setFromAndToDatesWithoutProperty(totalLeaves, LocalDate.now());

        LocalDate[] workingDays = leavePage.workingDays;

       // employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");

        if (typeOfSandwitch.equalsIgnoreCase("weeklyOff")) {
            List<String> weeklyOff = null;
            for (int i = 1; i <= noOfSandwitchLeaves; i++) {
                weeklyOff.add(LocalDate.now().plusDays(noOfPrefixLeaves+i).toString());
            }
            new Services().createWeeklyOff(String.join(",", weeklyOff));

            employee.setWeeklyOff(weeklyOff);

            if (applicationType.equalsIgnoreCase("single"))
                applyLeave(employee, sandwitchLeavePolicy, workingDays[0], workingDays[workingDays.length - 1]);



        } else if (typeOfSandwitch.equalsIgnoreCase("Holiday")) {
            LocalDate[] holidayDates=new LocalDate[noOfSandwitchLeaves];

            for(int i=0;i<noOfSandwitchLeaves;i++){
                holidayDates[i]=LocalDate.now().plusDays(noOfPrefixLeaves+i);
            }

            createHolidays(holidayDates);


            if (applicationType.equalsIgnoreCase("single"))
                applyLeave(employee, sandwitchLeavePolicy, workingDays[0], workingDays[workingDays.length - 1]);



        } else if (typeOfSandwitch.equalsIgnoreCase("weeklyOffAndHoliday")) {
            if (noOfSandwitchLeaves > 1) {
                //sandwitch --> first half = holidays , second half=weeklyOffs
                Holiday prefixHolidayObject = new Holiday();
                for (int i = 1; i <= noOfSandwitchLeaves / 2; i++) {
                    prefixHolidayObject.setDate(LocalDate.now().plusDays(noOfPrefixLeaves + i).toString());
                    prefixHolidayObject.setName("AutomationCreatedHoliday On" + LocalDate.now().plusDays(noOfPrefixLeaves + i).toString());
                    prefixHolidayObject.setOptional(false);
                    new HolidayService().createHoliday(prefixHolidayObject);
                }

                List<String> postWeeklyOff = null;

                for (int i = noOfSandwitchLeaves / 2 + 1; i <= noOfSandwitchLeaves; i++) {

                    postWeeklyOff.add(LocalDate.now().plusDays(i).toString());

                }

                new Services().createWeeklyOff(String.join(",", postWeeklyOff));

                employee.setWeeklyOff(postWeeklyOff);

                if (applicationType.equalsIgnoreCase("single"))
                    applyLeave(employee, sandwitchLeavePolicy, workingDays[0], workingDays[workingDays.length - 1]);

                if (applicationType.equalsIgnoreCase("multiple"))
                    applyLeaveMultiple(employee,sandwitchLeavePolicy,workingDays);

            }


        }


    }
}

