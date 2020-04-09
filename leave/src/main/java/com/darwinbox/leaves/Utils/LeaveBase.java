package com.darwinbox.leaves.Utils;

import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Holiday;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.HolidayService;
import com.darwinbox.attendance.services.settings.LeaveSettings;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.leaves.Objects.ApprovalFlow;
import com.darwinbox.leaves.Objects.ApprovalLevels;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Fields.CountInterveningHolidaysWeeklyOff;
import com.darwinbox.leaves.Objects.LeavePolicyObject.Fields.PrefixSuffixSetting;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.ApprovalFlowServices;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.http.NameValuePair;

import java.time.LocalDate;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class LeaveBase extends TestBase {
    LeaveService leaveService = new LeaveService();


    private static List<Map<String, String>> readDatafromSheet(String sheetname) {

        HashMap<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "/Application/LeaveApplication.xlsx");
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }

    private static List<Map<String, String>> readApprovalFlowDataSheet(String sheetname) {

        HashMap<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "/ApprovalFlow/ApprovalFlow.xlsx");
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }


    protected static List<LeavePolicyObject> getOverUtilizationPolicies() {
        List<Map<String, String>> excelData = readDatafromSheet("OverUtilizationPolicies");
        List<LeavePolicyObject> leavePolicies = new ArrayList<>();

        for (Map<String, String> data : excelData) {
            LeavePolicyObject policyObject = new LeavePolicyObject();
            policyObject.setFields(data);
            List<NameValuePair> body = policyObject.createRequest();
            //new LeaveService().createLeaveForPolicy(body, policyObject);
            leavePolicies.add(policyObject);
        }

        //return leavePolicies.stream().filter(x -> x.getLeave_Type().contains(policy)).findFirst().get();
        return leavePolicies;
    }

    protected static List<LeavePolicyObject> getOverUtilizationPoliciesForHourly() {
        List<Map<String, String>> excelData = readDatafromSheet("OverUtilizationPoliciesHourly");
        List<LeavePolicyObject> leavePolicies = new ArrayList<>();

        for (Map<String, String> data : excelData) {
            LeavePolicyObject policyObject = new LeavePolicyObject();
            policyObject.setFields(data);
            List<NameValuePair> body = policyObject.createRequest();
            new LeaveService().createLeaveForPolicy(body, policyObject);
            leavePolicies.add(policyObject);
        }

        //return leavePolicies.stream().filter(x -> x.getLeave_Type().contains(policy)).findFirst().get();
        return leavePolicies;
    }


    protected static LeavePolicyObject getSandwitchLeavePolicy(String policyName) {
        List<Map<String, String>> excelData = readDatafromSheet("SandwitchPolicies");

        Map<String, String> data = excelData.stream().filter(x -> x.get("Policy").equals(policyName)).findFirst().get();


        LeavePolicyObject sandwitchPolicyObject = new LeavePolicyObject();


        sandwitchPolicyObject.setAssignment_Type("company wise");
        sandwitchPolicyObject.setLeave_Type(data.get("Policy"));
        sandwitchPolicyObject.setGroup_Company("Working Days (DO NOT TOUCH)");
        sandwitchPolicyObject.setDescription("AutomationCreatedLeavePolicy");
        sandwitchPolicyObject.setMaximum_leave_allowed_per_year(12);


        CountInterveningHolidaysWeeklyOff countIntervening = new CountInterveningHolidaysWeeklyOff();
        countIntervening.indicator = Boolean.parseBoolean(data.get("CountIntervening"));
        countIntervening.CountHolidays = Boolean.parseBoolean(data.get("Count Holidays"));
        countIntervening.CountWeeklyOffs = Boolean.parseBoolean(data.get("Count WeeklyOff"));

        sandwitchPolicyObject.setCount_intervening_holidays_weeklys_offs_as_leave(countIntervening);

        new LeaveService().createLeaveForPolicy(sandwitchPolicyObject.createRequest(), sandwitchPolicyObject);


        Map<String, String> leaveSettings = new HashMap<>();
        leaveSettings.put("TenantLeavesSettings[enable_sandwich_validation]", "1");
        new LeaveSettings().saveLeaveSettings(leaveSettings);

        //return leavePolicies.stream().filter(x -> x.getLeave_Type().contains(policy)).findFirst().get();
        return sandwitchPolicyObject;

    }

    protected static ApprovalFlow getApprovalFlow(String approvalFlowName) {
        List<Map<String, String>> createApprovalFlowData = readApprovalFlowDataSheet("CreateApprovalFlow");
        List<Map<String, String>> approvalLevelsData = readApprovalFlowDataSheet("ApprovalLevels");

        Map<String, String> approvalFlowObject = createApprovalFlowData.stream().filter(x -> x.get("ApprovalFlowName").equalsIgnoreCase(approvalFlowName)).findFirst().get();


        ApprovalLevels level1 = null;
        ApprovalLevels level2 = null;
        ApprovalLevels level3 = null;

        if (!approvalFlowObject.get("Level 1").equalsIgnoreCase("")) {
            level1 = new ApprovalLevels();
            Map<String, String> level = approvalLevelsData.stream().filter(x -> x.get("LevelName").equalsIgnoreCase(approvalFlowObject.get("Level 1"))).findFirst().get();
            level1.setRoleHolders(Arrays.asList(level.get("RoleHolders").split(",")));

            if (Boolean.parseBoolean(level.get("isSelectOthersInLevelApprovers"))) {
                int noOfEmp = Integer.parseInt(level.get("OtherLevel1Approvers"));
                List<Employee> employees = new ArrayList<Employee>(noOfEmp);
                for (int i = 0; i < noOfEmp; i++) {
                    Employee employee = new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
                    employees.add(employee);
                }
                level1.setEmployees(employees);

            }

            level1.setAllowRevoke(Boolean.parseBoolean(level.get("isRevokeOptionEnabled")));


        }

        if (!approvalFlowObject.get("Level 2").equalsIgnoreCase("")) {
            level2 = new ApprovalLevels();
            Map<String, String> level = approvalLevelsData.stream().filter(x -> x.get("LevelName").equalsIgnoreCase(approvalFlowObject.get("Level 2"))).findFirst().get();
            level2.setRoleHolders(Arrays.asList(level.get("RoleHolders").split(",")));

            if (Boolean.parseBoolean(level.get("isSelectOthersInLevelApprovers"))) {
                int noOfEmp = Integer.parseInt(level.get("OtherLevel1Approvers"));
                List<Employee> employees = new ArrayList<Employee>(noOfEmp);
                for (int i = 0; i < noOfEmp; i++) {
                    Employee employee = new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
                    employees.add(employee);
                }
                level2.setEmployees(employees);

            }

            level2.setAllowRevoke(Boolean.parseBoolean(level.get("isRevokeOptionEnabled")));


        }

        if (!approvalFlowObject.get("Level 3").equalsIgnoreCase("")) {
            level3 = new ApprovalLevels();
            Map<String, String> level = approvalLevelsData.stream().filter(x -> x.get("LevelName").equalsIgnoreCase(approvalFlowObject.get("Level 3"))).findFirst().get();
            level3.setRoleHolders(Arrays.asList(level.get("RoleHolders").split(",")));

            if (Boolean.parseBoolean(level.get("isSelectOthersInLevelApprovers"))) {
                int noOfEmp = Integer.parseInt(level.get("OtherLevel1Approvers"));
                List<Employee> employees = new ArrayList<Employee>(noOfEmp);
                for (int i = 0; i < noOfEmp; i++) {
                    Employee employee = new EmployeeServices().generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");
                    employees.add(employee);
                }
                level3.setEmployees(employees);

            }

            level3.setAllowRevoke(Boolean.parseBoolean(level.get("isRevokeOptionEnabled")));


        }


        ApprovalFlow createApprovalFlow = new ApprovalFlow();
        createApprovalFlow.setName(approvalFlowObject.get("ApprovalFlowName"));
        if (approvalFlowObject.get("SelectModule").equalsIgnoreCase("Leaves"))
            createApprovalFlow.setType("2");
            createApprovalFlow.setType("2");
        if (level1 != null)
            createApprovalFlow.setLevel1(level1);
        if (level2 != null)
            createApprovalFlow.setLevel2(level2);
        if (level3 != null)
            createApprovalFlow.setLevel3(level3);

      //List<NameValuePair> requestBody= createApprovalFlow.getMap();


        if(new ApprovalFlowServices().getApprovalFlowByname(approvalFlowName)!=null){
            new ApprovalFlowServices().updateApprovalFlow(createApprovalFlow);
            return  createApprovalFlow;
        }
      new ApprovalFlowServices().createApprovalFlow(createApprovalFlow);


      return createApprovalFlow;
    }

    /*
    Applies Leave with respect to number of days given in argument
  */
    public String applyLeave(LeavePolicyObject leavePolicyObject, LocalDate... workingDays) {

        LocalDate fromDate = workingDays[0];
        LocalDate toDate = workingDays[workingDays.length - 1];
        String leaveID = leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
        String userMongoId = UtilityHelper.getProperty("config", "Employee.mongoId");
        return new LeaveAdmin().ApplyLeave(userMongoId, fromDate.toString(), toDate.toString(), leaveID);
    }

    public String applyLeave(String userMongoId, LeavePolicyObject leavePolicyObject, LocalDate... workingDays) {

        LocalDate fromDate = workingDays[0];
        LocalDate toDate = workingDays[workingDays.length - 1];
        String leaveID;
        if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("Compensatory Off"))
            leaveID = "5cc266f645bc6";

        else
            leaveID = leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);

        return new LeaveAdmin().ApplyLeave(userMongoId, fromDate.toString(), toDate.toString(), leaveID);
    }

    /*
    @override with emoloyee
     */
    public String applyLeave(Employee e, LeavePolicyObject leavePolicyObject, LocalDate... workingDays) {
        String leaveID = null;
        if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("unpaid")) {
            leaveID = "unpaid";
        } else if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("unpaid")) {
            leaveID = UtilityHelper.getProperty("", "");
        } else {
            leaveID = leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
        }
        LocalDate fromDate = workingDays[0];
        LocalDate toDate = workingDays[workingDays.length - 1];


        return new LeaveAdmin().ApplyLeave(e.getMongoID(), fromDate.toString(), toDate.toString(), leaveID);
    }

    public String applyLeaveWithEmpSession(Employee e, LeavePolicyObject leavePolicyObject, LocalDate fromDate, LocalDate toDate){
        String leaveID = null;
        if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("unpaid")) {
            leaveID = "unpaid";
        } else if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("unpaid")) {
            leaveID = UtilityHelper.getProperty("", "");
        } else {
            leaveID = leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
           // leaveID = leaveService.getLeaveID("taco", new Services().getGroupCompanyIds().get("Working Days (DO NOT TOUCH)"));
        }

            return new LeaveAdmin().applyLeaveWithEmpSession(e, fromDate.toString(), toDate.toString(), leaveID);
    }
    public String applyLeave(Employee e, LeavePolicyObject leavePolicyObject, LocalDate fromDate, LocalDate toDate) {
        String leaveID = null;
        if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("unpaid")) {
            leaveID = "unpaid";
        } else if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("compoff")) {
            leaveID = UtilityHelper.getProperty("", "");
        } else {
             leaveID = leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
            //leaveID = leaveService.getLeaveID("taco", new Services().getGroupCompanyIds().get("Working Days (DO NOT TOUCH)"));
        }





        return new LeaveAdmin().ApplyLeave(e.getMongoID(), fromDate.toString(), toDate.toString(), leaveID);
    }

    public String applyLeaveHourly(Employee e, LeavePolicyObject leavePolicyObject, LocalDate fromDate, LocalDate toDate,int hours) {
        String leaveID = null;
        if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("unpaid")) {
            leaveID = "unpaid";
        } else if (leavePolicyObject.getLeave_Type().equalsIgnoreCase("unpaid")) {
            leaveID = UtilityHelper.getProperty("", "");
        } else {
            leaveID = leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);
        }



        return new LeaveAdmin().ApplyLeave(e.getMongoID(), fromDate.toString(), toDate.toString(), leaveID);
    }




    public void applyLeaveMultiple(Employee e, LeavePolicyObject leavePolicyObject, LocalDate[] leaveDays) {
        for (LocalDate date : leaveDays) {
            applyLeave(e, leavePolicyObject, date, date);
        }
    }

    /*
    @override for half day leave
     */
    public String applyLeave(Employee e, LeavePolicyObject leavePolicyObject, LocalDate workingDay, String whichHalfOfDay) {
        LocalDate fromDate = workingDay;
        LocalDate toDate = workingDay;
        String leaveID = leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId);

        return new LeaveAdmin().ApplyLeave(e.getMongoID(), fromDate.toString(), toDate.toString(), leaveID, whichHalfOfDay);

    }

    /*
    apply leave with day of the week
     */
    public String applyLeave(Employee e, LeavePolicyObject leavePolicyObject, String day) {

        LocalDate tempDate;
        for (int i = 0; i < 7; i++) {
            tempDate = LocalDate.now().plusDays(i);
            if (tempDate.getDayOfWeek().toString().equalsIgnoreCase(day)) {
                return applyLeave(e, leavePolicyObject, tempDate);
            }
        }
        return "Error in Applying leave On" + day;
    }

    protected Map<String, String> HalfDayScenarios(String key) {
        List<Map<String, String>> scenarioes = readDatafromSheet("HalfDay");
        for (Map<String, String> scenario : scenarioes) {
            if (scenario.get("HalfDay").equalsIgnoreCase(key)) {
                return scenario;
            }
        }
        throw new RuntimeException("Cannot Find Half Day Scenario In Excel Sheet");
    }

    protected List<Map<String, String>> overUtilizationPolicies() {
        try {
            return readDatafromSheet("OverUtilizationPolicies");
        } catch (Exception e) {
            throw new RuntimeException("Cannot OverUtilizationPolicies in Excel Sheet");
        }
    }

    protected Map<String, String> getRequestForCountIntervening(boolean indicator, boolean weeklyOff, boolean countHoliday) {
        Map<String, String> accordion = new HashMap<>();

        if (indicator) {
            if (indicator)
                accordion.put("LeavePolicy_InterveningHolidays[status]", "1");

            if (weeklyOff & !countHoliday)
                accordion.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_weekly_off]", "1");

            if (countHoliday && !weeklyOff)
                accordion.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_public_holiday]", "1");


            if (countHoliday && weeklyOff) {
                accordion.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_weekly_off]", "1");
                accordion.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_public_holiday]", "1");
            }
        }
        if (!indicator) {
            accordion.put("LeavePolicy_InterveningHolidays[status]", "0");
        }

        return accordion;
    }

    protected int getIntegerValueOfWeekDay(String weekDay) {
        if (weekDay.equalsIgnoreCase("monday"))
            return 1;
        if (weekDay.equalsIgnoreCase("tuesday"))
            return 2;
        if (weekDay.equalsIgnoreCase("wednesday"))
            return 3;
        if (weekDay.equalsIgnoreCase("thursday"))
            return 4;
        if (weekDay.equalsIgnoreCase("friday"))
            return 5;
        if (weekDay.equalsIgnoreCase("saturday"))
            return 6;
        if (weekDay.equalsIgnoreCase("sunday"))
            return 7;

        return 0;
    }

    public void maximumLeaveApplicationsAllowedPerMonth(LeavePolicyObject leavePolicyObject) {
        LeavesPage leavesPage = new LeavesPage(driver);
        int field = leavePolicyObject.getMaximum_Leave_application_settings().maximum_leave_applications_allowed_per_month;
        String leaveCycleStartDate = new LeavesAction().getFirstDayofLeaveCycle(leavePolicyObject.getLeave_cycle());
        LocalDate serverChangeDate = LocalDate.parse(leaveCycleStartDate);
        // new DateTimeHelper().changeServerDate(driver, LocalDate.parse(leaveCycleStartDate).toString());

        for (int i = 0; i < field; i++) {
            leavesPage.setFromAndToDatesWithoutProperty(1, LocalDate.now().plusDays(i));
            driver.get(data.get("@@url"));
            new CommonAction(driver).changeApplicationAccessMode("Admin");
            String successResponse = applyLeave(leavePolicyObject, leavesPage.workingDays);
            //leaveApplicationHelper.verifyLeaveResponse(response, "pass");
            if (successResponse.contains("success"))
                Reporter("Pass --Leave Applied Successfully for Employee;  No Of Leave Application are" + (i + 1) +
                        "     Maximum Leave Applications Allowed Per Month is " + field, "Pass");
            else
                Reporter("Fail --Cannot Apply leave for Employee;  No Of Leave Application are" + (i + 1) +
                        "     Maximum Leave Applications Allowed Per Month is " + field, "Fail");
        }

        //failure case
        leavesPage.setFromAndToDatesWithoutProperty(1, LocalDate.now().plusDays(field + 1));
        String errorResponse = applyLeave(leavePolicyObject, leavesPage.workingDays);
        // Assert.assertTrue(errorResponse.contains(ERROR_MESSAGES.Maximum_Leave_Applications_Per_Month), "Error Message for MaximumLeaveApplicationAllowedPerYear is INCORRECT");
        if (errorResponse.contains(ERROR_MESSAGES.Maximum_Leave_Applications_Per_Month))
            Reporter("Pass  --User Tried to apply leave On    " + leavesPage.workingDays[0].toString() + "" +
                    "     Error Message Verified Successfully for Max Leave Application Per Month", "Pass");
        else
            Reporter("Fail  --Leave Applied Successfully On    " + leavesPage.workingDays[0].toString(), "Fail");
    }

    public void maximumLeaveApplicationsAllowedPerYear(LeavePolicyObject leavePolicyObject) {
        LeavesPage leavesPage = new LeavesPage(driver);
        //employee = employeeServices.generateAnEmployee("no", group_Company, "random", "no");
        int field = leavePolicyObject.getMaximum_Leave_application_settings().maximum_leave_applications_allowed_per_year;
        String leaveCycleStartDate = new LeavesAction().getFirstDayofLeaveCycle(leavePolicyObject.getLeave_cycle());
        LocalDate serverChangeDate = LocalDate.parse(leaveCycleStartDate);
        // new DateTimeHelper().changeServerDate(driver, LocalDate.parse(leaveCycleStartDate).toString());

        for (int i = 0; i < field; i++) {
            leavesPage.setFromAndToDatesWithoutProperty(1, LocalDate.now().plusDays(i));
            String successResponse = applyLeave(leavePolicyObject, leavesPage.workingDays);
            //leaveApplicationHelper.verifyLeaveResponse(response, "pass");
            if (successResponse.contains("success"))
                Reporter("Pass --Leave Applied Successfully for Employee;  No Of Leave Application are" + (i + 1) +
                        "     Maximum Leave Applications Allowed Per Year is " + field, "Pass");
            else
                Reporter("Fail --Cannot Apply leave for Employee;  No Of Leave Application are" + (i + 1) +
                        "     Maximum Leave Applications Allowed Per Year is " + field, "Fail");
        }

        //failure case
        leavesPage.setFromAndToDatesWithoutProperty(1, LocalDate.now().plusDays(field + 1));
        String errorResponse = applyLeave(leavePolicyObject, leavesPage.workingDays);
        // Assert.assertTrue(errorResponse.contains(ERROR_MESSAGES.Maximum_Leave_Applications_Per_Year), "Error Message for MaximumLeaveApplicationAllowedPerYear is INCORRECT");
        if (errorResponse.contains(ERROR_MESSAGES.Maximum_Leave_Applications_Per_Year))
            Reporter("Pass  --User Tried to apply leave On    " + leavesPage.workingDays[0].toString() + "" +
                    "     Error Message Verified Successfully for Max Leave Applications Per Year", "Pass");
        else
            Reporter("Fail  --Leave Applied Successfully On    " + leavesPage.workingDays[0].toString(), "Fail");
    }

    public void maxLeaveApplicationAllowedInTenure(LeavePolicyObject leavePolicyObject) {
        Employee employee = null;
        LeavesPage leavesPage = new LeavesPage(driver);
        employee = new EmployeeServices().generateAnEmployee("no", leavePolicyObject.getGroup_Company(), "random", "no");
        int maxApplicatinsInTenure = leavePolicyObject.getMaximum_Leave_application_settings().maximum_leave_applications_allowed_in_tenure;
        leavesPage.setFromAndToDatesWithoutProperty(maxApplicatinsInTenure + 1, LocalDate.now());

        //succes apply
        for (int i = 0; i < maxApplicatinsInTenure; i++) {
            String response = applyLeave(leavePolicyObject, leavesPage.workingDays[i]);
            if (response.contains("success")) {
                Reporter("PASS -- Leave is Applied Successfully On" + leavesPage.workingDays[i].toString()
                        + "No Of leave Application are " + (i + 1)
                        + "Max Leave Application Allowed In Tenure is " + maxApplicatinsInTenure, "Pass");
            } else {
                Reporter("FAIL -- FAIL to apply Leave On " + leavesPage.workingDays[i]
                        + "No Of leave Application are " + (i + 1)
                        + "Max Leave Application Allowed In Tenure is " + maxApplicatinsInTenure + "Response is " + response, "Fail");
            }
        }

        //failure case
        String failResponse = applyLeave(leavePolicyObject, leavesPage.workingDays[leavesPage.workingDays.length - 1]);
        if (failResponse.contains(ERROR_MESSAGES.Maximum_Leave_Application_Allowed_In_Tenure))
            Reporter("Pass  --User Tried to apply leave On    " + leavesPage.workingDays[0].toString() + "" +
                    "     Error Message Verified Successfully for Max Leave Applications In Tenure", "Pass");
        else
            Reporter("Fail  --Leave Applied Successfully On    " + leavesPage.workingDays[0].toString(), "Fail");
    }


    protected double verifyLeavesForConsecutiveLeaveAllowedField(LeavePolicyObject leavePolicyObject, double noOfLeaves) throws Exception {
        if (leavePolicyObject.getConsecutive_leave_allowed() != 0) {
            if (leavePolicyObject.getConsecutive_leave_allowed() >= noOfLeaves)
                noOfLeaves = noOfLeaves;
            else
                throw new Exception(ERROR_MESSAGES.ConsecutiveLeaveAllowed);
        }
        return noOfLeaves;

    }

    protected double verifyLeavesForMaxConsecutiveAllowedPerSingleApplicationField(LeavePolicyObject leavePolicyObject, double noOfLeaves) throws Exception {
        if (leavePolicyObject.getMaximum_consecutive_days_allowed_in_a_single_application() != 0) {
            if (leavePolicyObject.getMaximum_consecutive_days_allowed_in_a_single_application() >= noOfLeaves)
                noOfLeaves = noOfLeaves;
            else if (noOfLeaves > leavePolicyObject.getMaximum_consecutive_days_allowed_in_a_single_application())
                throw new Exception(ERROR_MESSAGES.Maximum_ConsecutiveDaysAllowedInSingleApplication + leavePolicyObject.getMaximum_consecutive_days_allowed_in_a_single_application());
        }
        return noOfLeaves;
    }

    protected double verifyLeavesForMaxHoursAllowedInASingleDay(LeavePolicyObject leavePolicyObject, double noOfHours) throws Exception {
        if (leavePolicyObject.getMaxNumberOfHoursInaSingleDay() != "") {
            if (Integer.parseInt(leavePolicyObject.getMaxNumberOfHoursInaSingleDay()) >= noOfHours)
                noOfHours = noOfHours;
            else if (noOfHours > Integer.parseInt(leavePolicyObject.getMaxNumberOfHoursInaSingleDay()))
                throw new Exception(ERROR_MESSAGES.maximumHoursToBeAppliedInSingleDay + leavePolicyObject.getMaxNumberOfHoursInaSingleDay());
        }
        return noOfHours;
    }

    protected double verifyLeavesForDontAllowMoreThanYearlyAllocation(LeavePolicyObject leavePolicyObject, double noOfLeaves) throws Exception {
        if (leavePolicyObject.getOverUtilization().dontAllowMoreThanYearlyAllocation != false) {
            if (leavePolicyObject.getMaximum_leave_allowed_per_year() >= noOfLeaves)
                noOfLeaves = noOfLeaves;
            else
                throw new Exception(ERROR_MESSAGES.OverUtilization_YearlyEndowment);
        }
        return noOfLeaves;
    }


    protected double verifyLeavesForDontAllowMoreThanYearlyAccural(LeavePolicyObject leavePolicyObject, Employee employee, double noOfLeaves) throws Exception {
        if (leavePolicyObject.getOverUtilization().dontAllowMoreThanYearlyAccural != false) {
            double carryForwardBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getCarryForwardBalance();
            int yearAllocation = leavePolicyObject.getMaximum_leave_allowed_per_year();
            double yearAccural = carryForwardBalance + yearAllocation;
            if (leavePolicyObject.getMaximum_Number_of_Leave_which_can_be_accrued() != 0) {
                yearAccural = leavePolicyObject.getMaximum_Number_of_Leave_which_can_be_accrued();
            }


            if (noOfLeaves > yearAccural)
                throw new Exception(ERROR_MESSAGES.OverUtilization_YearlyAccural);
        }
        return noOfLeaves;

    }


    protected void verifyFixedOverUtilization(LeavePolicyObject leavePolicyObject, double balance, double noOfLeaves) throws Exception {
        if (leavePolicyObject.getOverUtilization().utilizeFrom) {
            if (balance < 1) {
                if (noOfLeaves >= 0.5)
                    throw new Exception(ERROR_MESSAGES.OverUtilization_MinimumBalance);
            }
        }
    }


    protected void verifyLeavesForFixedOverUtilization(LeavePolicyObject leavePolicyObject, double empBalance, double noOfLeaves) throws Exception {
        if (leavePolicyObject.getOverUtilization().indicator) {
            if (leavePolicyObject.getOverUtilization().fixedOverUtilization != 0) {
                if (noOfLeaves > empBalance + leavePolicyObject.getOverUtilization().fixedOverUtilization) {
                    throw new Exception(ERROR_MESSAGES.OverUtilization_fixedOverUtilization + leavePolicyObject.getOverUtilization().fixedOverUtilization);
                }
            }
        }
    }

    protected void verifyLeavesForPastDatedApplication(LeavePolicyObject leavePolicyObject, double empBalance, Boolean pastDatedLeave, LocalDate leaveStartDate) throws Exception {
        if (pastDatedLeave) {
            if (leavePolicyObject.getPastDatedLeave().indicator) {
                if (leavePolicyObject.getPastDatedLeave().indicator) {
                    //  if (leavePolicyObject.getPastDatedLeave().maxNumOfDaysPostLeave != 0) {
                    long noOfPastLeaves = DAYS.between(leaveStartDate, LocalDate.now());
                    if (noOfPastLeaves > leavePolicyObject.getPastDatedLeave().maxNumOfDaysPostLeave) {
                        throw new Exception(ERROR_MESSAGES.PastDaysError);
                        //   }
                    }
                }
            } else
                throw new Exception(ERROR_MESSAGES.PastDaysError);

        }


    }

    protected double verifyLeavesForRestrictToWeekDays(LeavePolicyObject leavePolicyObject, Double noOfLeaves) {
        if (leavePolicyObject.getRestrict_to_Week_Days() != null) {
            LeavesPage workingDays = new LeavesPage(driver);
            workingDays.setFromAndToDatesWithoutProperty(noOfLeaves.intValue(), LocalDate.now());
            LocalDate[] noOfLeaveDates = workingDays.workingDays;
            int i = 0;
            String[] days = leavePolicyObject.getRestrict_to_Week_Days().split(",");
            for (LocalDate date : noOfLeaveDates) {
                if (!Arrays.stream(days).anyMatch(x -> x.equalsIgnoreCase(date.getDayOfWeek().toString()))) {
                    break;
                } else {
                    i++;
                }
            }
            return i;
        }
        return noOfLeaves;
    }


    protected double verifyLeavesForMaximumLeavesAllowedPerMonth(LeavePolicyObject leavePolicyObject, double noOfLeaves) {
        if (leavePolicyObject.getMaximum_leave_allowed_per_month() != 0) {
            int monthLeaves = 0;
            int i = 1;
            boolean carryForward = false;//defines max month satosfying condtion
            if (noOfLeaves > leavePolicyObject.getMaximum_leave_allowed_per_month()) {
                int remaningDaysInCurrentMonth = LocalDate.now().lengthOfMonth() - LocalDate.now().getDayOfMonth();
                if (leavePolicyObject.getMaximum_leave_allowed_per_month() < remaningDaysInCurrentMonth) {
                    monthLeaves = leavePolicyObject.getMaximum_leave_allowed_per_month();
                } else if (leavePolicyObject.getMaximum_leave_allowed_per_month() >= remaningDaysInCurrentMonth) {
                    carryForward = true;
                    monthLeaves = monthLeaves + remaningDaysInCurrentMonth;
                    while (carryForward) {
                        if (leavePolicyObject.getMaximum_leave_allowed_per_month() < LocalDate.now().plusMonths(i).lengthOfMonth()) {
                            monthLeaves = monthLeaves + leavePolicyObject.getMaximum_leave_allowed_per_month();
                            carryForward = false;
                        } else if (leavePolicyObject.getMaximum_leave_allowed_per_month() >= LocalDate.now().plusMonths(i).lengthOfMonth()) {
                            carryForward = true;
                            monthLeaves = monthLeaves + LocalDate.now().plusMonths(i).lengthOfMonth();

                        }
                    }
                }
                noOfLeaves = monthLeaves;
            } else if (noOfLeaves <= leavePolicyObject.getMaximum_leave_allowed_per_month()) {
                noOfLeaves = noOfLeaves;
            }

        }
        return noOfLeaves;

    }

    protected double verifyOverUtilization(Employee employee, LeavePolicyObject leavePolicyObject, double noOfLeaves, List<LeavePolicyObject> cascadingList) throws Exception {

        LocalDate[] noOfLeaveDates = null;

        noOfLeaves = Double.valueOf(new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance()).intValue();


        try {
            noOfLeaves = verifyLeavesForConsecutiveLeaveAllowedField(leavePolicyObject, noOfLeaves);
        } catch (Exception e) {
            noOfLeaves = leavePolicyObject.getConsecutive_leave_allowed();
        }

        try {
            noOfLeaves = verifyLeavesForMaxConsecutiveAllowedPerSingleApplicationField(leavePolicyObject, noOfLeaves);
        } catch (Exception e) {
            noOfLeaves = leavePolicyObject.getMaximum_consecutive_days_allowed_in_a_single_application();
        }

        try {
            noOfLeaves = verifyLeavesForDontAllowMoreThanYearlyAllocation(leavePolicyObject, noOfLeaves);
        } catch (Exception e) {
            noOfLeaves = leavePolicyObject.getMaximum_leave_allowed_per_year();

        }
        noOfLeaves = verifyMinimumConsecutiveDaysAllowedInSingleApplication(leavePolicyObject, noOfLeaves);

        noOfLeaves = checkAllowDay(leavePolicyObject, noOfLeaves);

        if (leavePolicyObject.isDontShowAndApplyInNoticePeriod()) {
            if (employee.getServingNoticePeriod()) {
                throw new Exception("Notice Period Check Failed");
            }
        }


        if (cascadingList.size() > 1 && cascadingList != null) {
            if (!checkClubbingCompatability(cascadingList.get(cascadingList.size() - 2), cascadingList.get(cascadingList.size() - 1))) {
                throw new Exception("Clubbing is not Allowed");
            }

        }

        try {
            noOfLeaves = verifyLeavesForDontAllowMoreThanYearlyAccural(leavePolicyObject, employee, noOfLeaves);
        } catch (Exception e) {
            noOfLeaves = leavePolicyObject.getMaximum_Number_of_Leave_which_can_be_accrued();

        }
        noOfLeaves = verifyLeavesForRestrictToWeekDays(leavePolicyObject, noOfLeaves);


        noOfLeaves = verifyMinimumConsecutiveDaysAllowedInSingleApplication(leavePolicyObject, noOfLeaves);


        //yet to implement
        noOfLeaves = verifyLeavesForMaximumLeavesAllowedPerMonth(leavePolicyObject, noOfLeaves);


        noOfLeaves = verifyMinimumConsecutiveDaysAllowedInSingleApplication(leavePolicyObject, noOfLeaves);

        return noOfLeaves;
    }

    public LocalDate[] pickDaysFromPresentMonth(LocalDate[] dates) {
        LocalDate[] currentMonthDates = null;
        for (int i = 0; i < dates.length; i++) {
            if (dates[i].getMonth() == LocalDate.now().getMonth()) {
                currentMonthDates[i] = dates[i];
            } else
                break;
        }
        return currentMonthDates;
    }

    protected double verifyMinimumConsecutiveDaysAllowedInSingleApplication(LeavePolicyObject leavePolicyObject, double noOfLeaves) throws Exception {
        if (leavePolicyObject.getMinimum_consecutive_days_allowed_in_a_single_application() != 0) {
            if (leavePolicyObject.getMinimum_consecutive_days_allowed_in_a_single_application() <= noOfLeaves)
                return noOfLeaves;
            else
                throw new Exception(ERROR_MESSAGES.Minimum_ConsecutiveDaysAllowedInSingleApplication + leavePolicyObject.getMinimum_consecutive_days_allowed_in_a_single_application());
        } else
            return noOfLeaves;
    }


    protected double verifyMinimumHoursToBeAppliedInASingleDay(LeavePolicyObject leavePolicyObject, double noOfLeaves) throws Exception {

        if (leavePolicyObject.getMinNumbersOfHoursInaSingleDay() != "") {
            if (Integer.parseInt(leavePolicyObject.getMinNumbersOfHoursInaSingleDay()) <= noOfLeaves)
                return noOfLeaves;
            else
                throw new Exception(ERROR_MESSAGES.minimumHoursToBeAppliedInSingleDay + leavePolicyObject.getMinNumbersOfHoursInaSingleDay());
        } else
            return noOfLeaves;
    }

    protected double checkAllowDay(LeavePolicyObject leavePolicyObject, double noOfLeaves) throws Exception {
        if (noOfLeaves == 0.5) {
            if (leavePolicyObject.getAllow_half_day()) {
                return noOfLeaves;
            } else if (!leavePolicyObject.getAllow_half_day()) {
                throw new Exception(ERROR_MESSAGES.HalfDay + leavePolicyObject.getLeave_Type());

            }
        }
        return noOfLeaves;
    }

    protected boolean checkClubbingCompatability(LeavePolicyObject leavePolicyObject1, LeavePolicyObject leavePolicyObject2) {

        if (leavePolicyObject2.getLeave_Type().equalsIgnoreCase("unpaid") ||
                leavePolicyObject2.getLeave_Type().equalsIgnoreCase("Compensatory Off")) {
            return true;
        }

        Reporter("Info -- Leave Policy    " + leavePolicyObject2.getLeave_Type() +
                "Policy Settings are   Clubbing Allowed =" + leavePolicyObject2.getClubbing().indicator +
                "Clubbing With Any Leave Type   " + leavePolicyObject2.getClubbing().clubWithAnyLeave +
                "Clubbing With Following Type  " + leavePolicyObject2.getClubbing().clubWithFollowingLeave +
                "Leave List  " + leavePolicyObject2.getClubbing().leaveList.replace(",", "  And  "), "Info");

        if (leavePolicyObject2.getClubbing().indicator) {

            if (leavePolicyObject2.getClubbing().clubWithFollowingLeave) {

                for (String leave : leavePolicyObject2.getClubbing().leaveList.split(",")) {
                    if (leave == leavePolicyObject1.getLeave_Type()) {
                        return true;
                    }
                }
                return false;
            }
            return leavePolicyObject2.getClubbing().clubWithAnyLeave;
        } else if (!leavePolicyObject2.getClubbing().indicator) {
            if (leavePolicyObject1.getClubbing().clubWithFollowingLeave) {
                for (String leave : leavePolicyObject1.getClubbing().leaveList.split(",")) {
                    if (leave == leavePolicyObject2.getLeave_Type()) {
                        return true;
                    }
                }
                return false;
            }

            return leavePolicyObject1.getClubbing().clubWithAnyLeave;
        }

        return false;
    }


    protected List<LocalDate> verifyLeavesForPrefixSuffix(LocalDate fromDate, LocalDate toDate, Employee employee, LeavePolicyObject leavePolicy) {
        boolean prefixHoliday = false;
        boolean prefixWeeklyOff = false;
        boolean suffixHoliday = false;
        boolean suffixWeeklyOff = false;

        PrefixSuffixSetting prefixSuffixSetting = leavePolicy.getPrefixSuffixSetting();

        if (employee.getWeeklyOff() != null) {
            for (String weeklyOff : employee.getWeeklyOff()) {
                if (weeklyOff.equalsIgnoreCase(toDate.plusDays(1).getDayOfWeek().toString())) {
                    suffixWeeklyOff = true;
                }
                if (weeklyOff.equalsIgnoreCase(fromDate.minusDays(1).getDayOfWeek().toString())) {
                    prefixWeeklyOff = true;
                }
            }
        }

        String holidays = new HolidayService().getHolidaysForUser(employee.getUserID());
        if (holidays.toLowerCase().contains(new DateTimeHelper().changeDateFormatForHolidays(toDate.plusDays(1)).toLowerCase()))
            suffixHoliday = true;

        if (holidays.toLowerCase().contains(new DateTimeHelper().changeDateFormatForHolidays(fromDate.minusDays(1)).toLowerCase()))
            prefixHoliday = true;

        if (prefixSuffixSetting.indicator) {
            if (prefixWeeklyOff) {
                if (prefixSuffixSetting.weeklyOffPrefix.allowLeaveAfterWeeklyOff) {

                }
                if (prefixSuffixSetting.weeklyOffPrefix.countPrefixedAsLeave) {
                    for (int i = 1; i < 7; i++) {
                        int count = i;
                        LocalDate tempFromDate = fromDate;
                        Boolean prefixWeeklyOffCheck = employee.getWeeklyOff().stream().filter(x -> x.equalsIgnoreCase(tempFromDate.minusDays(count).getDayOfWeek().toString())).findAny().isPresent();
                        if (prefixWeeklyOffCheck)
                            fromDate = fromDate.minusDays(i);

                        else
                            break;
                    }
                }
                if (prefixSuffixSetting.weeklyOffPrefix.blockLeaveAfterWeeklyOff) {
                    throw new RuntimeException("Weekly Off Preffix Setting is set to BLOCK");//replace with application error
                }
            }
            if (suffixWeeklyOff) {
                if (prefixSuffixSetting.weeklyOffSuffix.allowLeaveBeforeWeeklyOff) {

                }
                if (prefixSuffixSetting.weeklyOffSuffix.countSuffixedWeeklyOffAsLeave) {
                    for (int i = 1; i < 7; i++) {
                        int count = i;
                        LocalDate tempToDate = toDate;
                        Boolean prefixWeeklyOffCheck = employee.getWeeklyOff().stream().filter(x -> x.equalsIgnoreCase(tempToDate.plusDays(count).getDayOfWeek().toString())).findAny().isPresent();
                        if (prefixWeeklyOffCheck)
                            toDate = toDate.plusDays(i);

                        else
                            break;
                    }
                }
                if (prefixSuffixSetting.weeklyOffSuffix.blockLeaveBeforeWeeklyOff) {
                    throw new RuntimeException("Weekly Off Suffix Setting is set to BLOCK");//replace this with application erroe
                }
            }
            if (prefixHoliday) {
                if (prefixSuffixSetting.holidayPrefix.allowLeaveAfterHoliday) {

                }
                if (prefixSuffixSetting.holidayPrefix.countPrefixedHolidayAsLeave) {
                    for (int i = 1; i < 7; i++) {
                        int count = i;
                        LocalDate tempFromDate = fromDate;
                        Boolean prefixWeeklyOffCheck = holidays.equalsIgnoreCase(new DateTimeHelper().changeDateFormatForHolidays(fromDate.minusDays(count)));
                        if (prefixWeeklyOffCheck)
                            fromDate = fromDate.minusDays(i);

                        else
                            break;
                    }
                }
                if (prefixSuffixSetting.weeklyOffPrefix.blockLeaveAfterWeeklyOff) {
                    throw new RuntimeException("Holiday  Preffix Setting is set to BLOCK");//replace with application error
                }
            }
            if (suffixHoliday) {
                if (prefixSuffixSetting.holidaySuffix.allowLeaveBeforeHoliday) {

                }
                if (prefixSuffixSetting.holidaySuffix.countSuffixedHolidayAsLeave) {
                    for (int i = 1; i < 7; i++) {
                        int count = i;
                        LocalDate tempToDate = toDate;
                        Boolean prefixWeeklyOffCheck = holidays.equalsIgnoreCase(new DateTimeHelper().changeDateFormatForHolidays(toDate.plusDays(count)));
                        if (prefixWeeklyOffCheck)
                            toDate = toDate.plusDays(i);

                        else
                            break;
                    }
                }
                if (prefixSuffixSetting.holidaySuffix.blockLeaveBeforeHoliday) {
                    throw new RuntimeException("Holiday Suffix Setting is set to BLOCK");//replace this with application erroe
                }
            }
        }

        List<LocalDate> modifiedLeaveDates = new ArrayList<>();
        modifiedLeaveDates.add(fromDate);
        modifiedLeaveDates.add(toDate);

        return modifiedLeaveDates;
    }

    //****Holiday Functions////
    protected void deleteAllHolidays() {
        new HolidayService().deleteHolidays();
    }

    protected void createHolidays(LocalDate... holidays) {
        //Holiday holidayObject = null;

        for (LocalDate holidayDate : holidays) {


            Holiday holidayObject = new Holiday();
            holidayObject.setDate(holidayDate.toString());
            holidayObject.setName("AutomationCreatedHoliday On" + holidayDate);
            holidayObject.setOptional(false);

            new HolidayService().createHoliday(holidayObject);

        }

    }


    //********************/////
    public enum Clubbings {NO, YESWITHANYLEAVE, YESWITHSAME, YESWITHDIFFERENT}


    public enum ClubbingsForUnpaidAndComp {YESWITHANYLEAVE}


    public void assignManagerToEmployee(Employee employee,Employee manager,LocalDate effectiveDate){
        new LeaveAdmin().changeManagerBulk(employee.getUserID(),manager.getMongoID(),effectiveDate);
        employee.setL1Manager(manager);
        Reporter(manager.getFirstName()+"is assigned as L1Manager to "+employee.getFirstName(),"Info");
    }


}
