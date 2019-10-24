package com.darwinbox.leaves.Application;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Holiday;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.HolidayService;
import com.darwinbox.core.Services;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Fields.CountInterveningHolidaysWeeklyOff;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Fields.PastDatedLeave;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Fields.PrefixSuffixSetting;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class    Prefix_Suffix extends LeaveBase {

    EmployeeServices employeeServices;
    LeaveService leaveService;
   // LeavePolicyObject leavePolicyObject;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;

    LeavesPage leavePage;
    LoginPage loginpage;
    CommonAction commonAction;

    Employee employee;
    String employeeProbation = null;

    List<Map<String, Object>> expectedLeaveDetails = null;


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
    public void verifyPrefixSufix(Map<String, String> data) {


        Double noOfLeaves = Double.parseDouble(data.get("noOfLeaves"));
        Boolean isPrefixHoliday = false;
        int prefixHolidayCount = 0;
        Boolean isSuffixHoliday = false;
        int suffixHolidayCount = 0;
        Boolean isPrefixWeeklyOff = false;
        int prefixWeeklyOffCount = 0;
        Boolean isSuffixWeeklyOff = false;
        int suffixWeeklyOffCount = 0;
        try {
            isPrefixHoliday = Boolean.parseBoolean(data.get("isPrefixHoliday"));
            prefixHolidayCount = Integer.parseInt(data.get("prefixHolidayCount"));
            isSuffixHoliday = Boolean.parseBoolean(data.get("isSufixHoliday"));
            suffixHolidayCount = Integer.parseInt(data.get("suffixHolidayCount"));
            isPrefixWeeklyOff = Boolean.parseBoolean(data.get("isPrefixWeeklyOff"));
            prefixWeeklyOffCount = Integer.parseInt(data.get("prefixWeeklyOffCount"));
            isSuffixWeeklyOff = Boolean.parseBoolean(data.get("isSuffixWeeklyOff"));
            suffixWeeklyOffCount = Integer.parseInt(data.get("suffixWeeklyOffCount"));
        }
        catch (NumberFormatException e){
            //ignoring empty string in excel sheet for above fields
        }

        new HolidayService().deleteHolidays();


        leavePage.setFromAndToDatesWithoutProperty(noOfLeaves.intValue(), LocalDate.now());
        LocalDate[] workingDays = leavePage.workingDays;
        LocalDate fromDate = workingDays[0];
        LocalDate toDate = workingDays[workingDays.length - 1];


        if (isPrefixHoliday) {
            Holiday prefixHolidayObject = new Holiday();
            for (int i = 1; i <= prefixHolidayCount; i++) {
                prefixHolidayObject.setDate(fromDate.minusDays(i).toString());
                prefixHolidayObject.setName("AutomationCreatedHoliday On" + fromDate.minusDays(i).toString());
                prefixHolidayObject.setOptional(false);
                new HolidayService().createHoliday(prefixHolidayObject);
            }
        }


        if (isSuffixHoliday) {
            Holiday prefixHolidayObject = new Holiday();
            for (int i = 1; i <= suffixHolidayCount; i++) {
                prefixHolidayObject.setDate(toDate.plusDays(i).toString());
                prefixHolidayObject.setName("AutomationCreatedHoliday On" + toDate.plusDays(i).toString());
                prefixHolidayObject.setOptional(false);
                new HolidayService().createHoliday(prefixHolidayObject);
            }
        }

        List<String> prefixWeeklyOffDays = null;
        if (isPrefixWeeklyOff) {
            prefixWeeklyOffDays = new ArrayList<>();
            while (prefixWeeklyOffCount > 0) {
                prefixWeeklyOffDays.add(fromDate.minusDays(prefixWeeklyOffCount).getDayOfWeek().toString());
                prefixWeeklyOffCount--;
            }

            new Services().createWeeklyOff(String.join(",", prefixWeeklyOffDays));

        }

        List<String> suffixWeeklyOffDays = null;
        if (isSuffixWeeklyOff) {
            suffixWeeklyOffDays = new ArrayList<>();
            for (int i = 1; i <= suffixWeeklyOffCount; i++) {
                suffixWeeklyOffDays.add(toDate.getDayOfWeek().plus(i).toString());
            }
            new Services().createWeeklyOff(String.join(",", suffixWeeklyOffDays));

        }


        employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
        if(isPrefixWeeklyOff)
        {
            String weeklyOffID = new Services().getWeeklyOFFlist().get(String.join("And", prefixWeeklyOffDays));
            employeeServices.createWeeklyOffForAnEmp(employee.getUserID(), weeklyOffID);
            employee.setWeeklyOff(prefixWeeklyOffDays);
        }

        if(isSuffixWeeklyOff && !prefixWeeklyOffDays.toString().equalsIgnoreCase(suffixWeeklyOffDays.toString())){
            String weeklyOffID = new Services().getWeeklyOFFlist().get(String.join("And", suffixWeeklyOffDays));
            employeeServices.createWeeklyOffForAnEmp(employee.getUserID(), weeklyOffID);
            employee.setWeeklyOff(suffixWeeklyOffDays);

        }
        //   new Service().createWeeklyOff(weeklyOff);
        //String weeklyOffID = new Service().getWeeklyOFFlist().get(weeklyOff);
        //employeeServices.createWeeklyOffForAnEmp(employee.getUserID(), weeklyOffID);


        LeavePolicyObject prefixSuffixaPolicy = new LeavePolicyObject();
        prefixSuffixaPolicy.setAssignment_Type("company wise");
        prefixSuffixaPolicy.setLeave_Type("Auto_Prefix_Suffix");
        prefixSuffixaPolicy.setGroup_Company("Working Days (DO NOT TOUCH)");
        prefixSuffixaPolicy.setDescription("AutomationCreatedLeavePolicy");
        prefixSuffixaPolicy.setMaximum_leave_allowed_per_year(12);
        PrefixSuffixSetting prefixSuffixSetting = new LeavePolicyObject().getPrefixSuffixObject(data);
        prefixSuffixaPolicy.setPrefixSuffixSetting(prefixSuffixSetting);

        PastDatedLeave pastDatedLeave= new PastDatedLeave();
        pastDatedLeave.indicator=true;
        pastDatedLeave.maxNumOfDaysPostLeave=10;
        prefixSuffixaPolicy.setPastDatedLeave(pastDatedLeave);


        CountInterveningHolidaysWeeklyOff countIntervening= new CountInterveningHolidaysWeeklyOff();
        countIntervening.indicator=true;
        countIntervening.CountWeeklyOffs=true;
        countIntervening.CountHolidays=true;
        prefixSuffixaPolicy.setCount_intervening_holidays_weeklys_offs_as_leave(countIntervening);

        new LeaveService().createLeaveForPolicy(prefixSuffixaPolicy.createRequest(), prefixSuffixaPolicy);


        expectedLeaveDetails = new LeaveService().calculateDuration(employee.getMongoID(), fromDate, toDate, new LeaveService().getLeaveID(prefixSuffixaPolicy.getLeave_Type(), prefixSuffixaPolicy.groupCompanyMongoId), false);

        Object noOfLeavesDeducted=expectedLeaveDetails.get(0).get("days");
        Object leaveFromDate=expectedLeaveDetails.get(0).get("from_date");
        Object leaveToDate=expectedLeaveDetails.get(0).get("to_date");


        String response=applyLeave(employee,prefixSuffixaPolicy,fromDate,toDate);

        Reporter("Actual Leave Response is"+response,"Info");
        Reporter("Actual Days"+noOfLeavesDeducted,"Info");
        Reporter("Actual fromDate"+leaveFromDate,"Info");
        Reporter("Actual toDate"+leaveToDate,"Info");



        //test code to verify actual
        //try {
            verifyLeavesForPrefixSuffix(fromDate, toDate, employee, prefixSuffixaPolicy);
        //} catch (RuntimeException e) {
          //  Reporter("Prefix Suffix Method Thrown Exception" + e, "Info");

        //}


    }
}
