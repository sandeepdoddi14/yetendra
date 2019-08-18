package com.darwinbox.leaves.Utils;

import Objects.Employee;
import Objects.LeavePolicyObject.Accural.*;
import Objects.LeavePolicyObject.Fields.ProbationPeriodForLeaveValidity;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.EmployeeServices;
import Service.LeaveBalanceAPI;
import Service.LeaveService;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.github.javafaker.Bool;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.NameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class LeaveAccuralBase extends  LeaveBase {

    public static String serverChangedDate=null;

    //manual employee data
    public  static String DateOfJoining = "";
    public static String  EmployeeId=null;

    static LeavePolicyObject leavePolicyObject=null;
    public static LocalDate leaveCycleStartDate=null;
    public static LocalDate leaveCycleEndDate=null;
    static String Leave_Probation_End_Date=null;
    static String LeaveCalBeginningDate=null;

   public  static Employee employee=null;

   public Boolean deActiavation=false;

    DateTimeHelper objDateTimeHelper= new DateTimeHelper();

    static double ExpectedLeaveBalance = 0;

    public void setLeavePolicyObject(LeavePolicyObject leavePolicyObject){
        this.leavePolicyObject=leavePolicyObject;
        //this.leaveCycleStartDate=LocalDate.parse(getFirstDayofLeaveCycle(leavePolicyObject.getLeave_cycle().toString()));
        //this.leaveCycleEndDate=LocalDate.parse(getLastDayofLeaveCycle(leavePolicyObject.getLeave_cycle().toString()));
    }

    public LeavePolicyObject getWorkingDaysPolicy(Map<String,String> testData){

        LeavePolicyObject leaveBalancePolicy=new LeavePolicyObject();
        leaveBalancePolicy.setAssignment_Type("company wise");
        leaveBalancePolicy.setGroup_Company("Working Days (DO NOT TOUCH)");
        leaveBalancePolicy.setLeave_Type(testData.get("Leave_Type"));
        leaveBalancePolicy.setDescription("AutomationCreatedLeavePolicy");
        leaveBalancePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Max_Leaves_Allowed_Per_Year").replace(".0","")));
        leaveBalancePolicy.setLeave_cycle(testData.get("Leave Cycle"));
        leaveBalancePolicy.setCustomLeaveCycleMonth(testData.get("CustomLeaveCycleMonth"));

        ProbationPeriodForLeaveValidity probationPeriodForLeaveValidity = new ProbationPeriodForLeaveValidity();
        probationPeriodForLeaveValidity.custom=testData.get("Leave Probation Period according to Custom Months").equalsIgnoreCase("yes")?true:false;
        probationPeriodForLeaveValidity.probation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;
        //if(probationPeriodForLeaveValidity.probation)
        //employeeProbation=testData.get("Employee Probation Period");
        if(probationPeriodForLeaveValidity.custom)
            probationPeriodForLeaveValidity.customMonths=Integer.parseInt(testData.get("Probation period before leave validity months").replace(".0",""));

        leaveBalancePolicy.setProbation_period_before_leave_validity(probationPeriodForLeaveValidity);


        if(testData.get("Pro rata").equalsIgnoreCase("yes")?true:false){
            Credit_On_Pro_Rata_Basis pro_rata_basis= new Credit_On_Pro_Rata_Basis();
            pro_rata_basis.indicator=true;
            pro_rata_basis.calculateFromJoiningDate=testData.get("From Joining date").equalsIgnoreCase("yes")?true:false;
            pro_rata_basis.calculateAfterProbationPeriod=testData.get("After Probation period").equalsIgnoreCase("yes")?true:false;
            //  pro_rata_basis.creditHalfMonthsLeavesIfEmpJoinsAfter15Th=testData.get("Half Month Leaves if employee joins after 15th").equalsIgnoreCase("yes")?true:false;
            // pro_rata_basis.creditfullMonthsLeavesIfEmpJoinsAfter15Th=testData.get("Full Month Leaves if employee joins after 15th").equalsIgnoreCase("yes")?true:false;

            leaveBalancePolicy.setCredit_on_pro_rata_basis(pro_rata_basis);
        }

        if(testData.get("Accrual").equalsIgnoreCase("yes")?true:false){
            Credit_On_Accural_Basis credit_on_accural_basis= new Credit_On_Accural_Basis();
            credit_on_accural_basis.setIndicator(true);

            if(testData.get("Leave Accrual based on Working days").equalsIgnoreCase("yes")?true:false)
            {
                ConsiderWorkingDays considerWorkingDays = new ConsiderWorkingDays();
                considerWorkingDays.indicator=true;
                if(testData.get("Count Present Days").equalsIgnoreCase("yes")?true:false)
                    considerWorkingDays.presentDays=true;

                if(testData.get("Count Absent Days").equalsIgnoreCase("yes")?true:false)
                    considerWorkingDays.absentDaysAndUnpaidLeave=true;

                if(testData.get("Count Weeklyoff Days").equalsIgnoreCase("yes")?true:false)
                    considerWorkingDays.weeklyOffs=true;

                if(testData.get("Count Holiday Days").equalsIgnoreCase("yes")?true:false)
                    considerWorkingDays.holidays=true;

                if(testData.get("Count Optional Holiday Days").equalsIgnoreCase("yes")?true:false)
                    considerWorkingDays.optionalHoliodays=true;

                if(testData.get("Count Leave Days").equalsIgnoreCase("yes")?true:false)
                    considerWorkingDays.leaveDays=true;

                if(testData.get("End of Year").equalsIgnoreCase("yes")?true:false)
                    considerWorkingDays.endOfYear=true;

                credit_on_accural_basis.setConsiderWorkingDays(considerWorkingDays);

            }


            if(testData.get("End of Month").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setMonthlyAccuralSetting(true,false,true);



            if(testData.get("End of Quarter").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setQuarterlyAccural(true,false,true);


            if(testData.get("End Of Biannual").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setBiAnnual(true);
            //end of biannual??

            leaveBalancePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }
       /* if(testData.get("Credit On Tenure Basis").equalsIgnoreCase("yes")){
            TenureBasis tenureBasis = new TenureBasis();
            tenureBasis.indicator=true;
            for(String fromYear: testData.get("Credit From Year").split(",")){
                tenureBasis.fromYear.add(Integer.parseInt(fromYear));
            }

            for(String toYear: testData.get("Credit To Year").split(",")){
                tenureBasis.toYear.add(Integer.parseInt(toYear));
            }


            for(String leaves: testData.get("Credit No of Leaves").split(",")){
                tenureBasis.noOfLeaves.add(Integer.parseInt(leaves.trim()));
            }

            leaveBalancePolicy.setTenureBasis(tenureBasis);

        }*/




        List<NameValuePair> request=leaveBalancePolicy.createRequest();

        new LeaveService().createLeaveForPolicy(request,leaveBalancePolicy);

        return  leaveBalancePolicy;
    }


    public Boolean setEmployeeId(String employeeId){
        this.EmployeeId=employeeId;
        return  true;
    }

    public void setEmployee(Employee employee){
        this.employee=employee;
    }
    public LeavePolicyObject getCarryForwardPolicy(Map<String,String> testData){

        LeavePolicyObject leaveBalancePolicy=new LeavePolicyObject();
        leaveBalancePolicy.setAssignment_Type("company wise");
        leaveBalancePolicy.setGroup_Company("Working Days (DO NOT TOUCH)");
        leaveBalancePolicy.setLeave_Type(testData.get("Leave_Type"));
        leaveBalancePolicy.setDescription("AutomationCreatedLeavePolicy");
        leaveBalancePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Max_Leaves_Allowed_Per_Year").replace(".0","")));
        leaveBalancePolicy.setLeave_cycle(testData.get("Leave Cycle"));
        leaveBalancePolicy.setCustomLeaveCycleMonth(testData.get("CustomLeaveCycleMonth"));

        ProbationPeriodForLeaveValidity probationPeriodForLeaveValidity = new ProbationPeriodForLeaveValidity();
        probationPeriodForLeaveValidity.custom=testData.get("Leave Probation Period according to Custom Months").equalsIgnoreCase("yes")?true:false;
        probationPeriodForLeaveValidity.probation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;
        //if(probationPeriodForLeaveValidity.probation)
        //employeeProbation=testData.get("Employee Probation Period");
        if(probationPeriodForLeaveValidity.custom)
            probationPeriodForLeaveValidity.customMonths=Integer.parseInt(testData.get("Probation period before leave validity months").replace(".0",""));

        leaveBalancePolicy.setProbation_period_before_leave_validity(probationPeriodForLeaveValidity);


        if(testData.get("Pro rata").equalsIgnoreCase("yes")?true:false){
            Credit_On_Pro_Rata_Basis pro_rata_basis= new Credit_On_Pro_Rata_Basis();
            pro_rata_basis.indicator=true;
            pro_rata_basis.calculateFromJoiningDate=testData.get("From Joining date").equalsIgnoreCase("yes")?true:false;
            pro_rata_basis.calculateAfterProbationPeriod=testData.get("After Probation period").equalsIgnoreCase("yes")?true:false;
            pro_rata_basis.creditHalfMonthsLeavesIfEmpJoinsAfter15Th=testData.get("Half Month Leaves if employee joins after 15th").equalsIgnoreCase("yes")?true:false;
            pro_rata_basis.creditfullMonthsLeavesIfEmpJoinsAfter15Th=testData.get("Full Month Leaves if employee joins after 15th").equalsIgnoreCase("yes")?true:false;

            leaveBalancePolicy.setCredit_on_pro_rata_basis(pro_rata_basis);
        }

        if(testData.get("Accrual").equalsIgnoreCase("yes")?true:false){
            Credit_On_Accural_Basis credit_on_accural_basis= new Credit_On_Accural_Basis();
            credit_on_accural_basis.setIndicator(true);

            if(!testData.get("Monthly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setMonthlyAccuralSetting(false,false,false);
            else
                credit_on_accural_basis.setMonthlyAccuralSetting(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            if(!testData.get("Quarterly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            else
                credit_on_accural_basis.setQuarterlyAccural(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            credit_on_accural_basis.setBiAnnual(testData.get("Biannually").equalsIgnoreCase("yes")?true:false);

            leaveBalancePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }


        if(testData.get("Carry forward").equalsIgnoreCase("yes")){
            CarryForwardUnusedLeave carryForwardUnusedLeave = new CarryForwardUnusedLeave();
            carryForwardUnusedLeave.indicator=testData.get("Carry forward").equalsIgnoreCase("yes")?true:false;

            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("all"))
            {
                carryForwardUnusedLeave.carryForwardAllUnusedLeave=true;
            }
            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("fixed")){
                carryForwardUnusedLeave.carryForwardOnly=true;
                carryForwardUnusedLeave.fixed=true;
                carryForwardUnusedLeave.fixedValue=Integer.parseInt(testData.get("Fixed/Percentage value"));
            }
            if(testData.get("Carry forward All/Fixed/Percentage").equalsIgnoreCase("percentage")){
                carryForwardUnusedLeave.carryForwardOnly=true;
                carryForwardUnusedLeave.percentage=true;
                carryForwardUnusedLeave.percentageValue=Integer.parseInt(testData.get("Fixed/Percentage value"));
            }

            leaveBalancePolicy.setCarryForwardUnusedLeave(carryForwardUnusedLeave);

        }


        List<NameValuePair> request=leaveBalancePolicy.createRequest();

        new LeaveService().createLeaveForPolicy(request,leaveBalancePolicy);

        return  leaveBalancePolicy;
    }

    private static List<Map<String, String>> readDatafromSheet(String sheetname) {

        HashMap<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "/Accural/LeaveBalance.xlsx");
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }


    public List<LeavePolicyObject> getLeaveBalancePolicies(){
        List<Map<String, String>> excelData = readDatafromSheet("LeaveBalance");
        List<LeavePolicyObject> leaveBalancePolicies = new ArrayList<>();

        for(Map<String,String> testData : excelData){
            leaveBalancePolicies.add(getLeaveBalancePolicy(testData));
        }

        return leaveBalancePolicies;

    }


    public LeavePolicyObject getLeaveBalancePolicy(Map<String,String> testData){

        LeavePolicyObject leaveBalancePolicy=new LeavePolicyObject();
        leaveBalancePolicy.setAssignment_Type("company wise");
        leaveBalancePolicy.setGroup_Company("Working Days (DO NOT TOUCH)");
        leaveBalancePolicy.setLeave_Type(testData.get("Leave_Type"));
        leaveBalancePolicy.setDescription("AutomationCreatedLeavePolicy");
        leaveBalancePolicy.setMaximum_leave_allowed_per_year(Integer.parseInt(testData.get("Max_Leaves_Allowed_Per_Year").replace(".0","")));
        leaveBalancePolicy.setLeave_cycle(testData.get("Leave Cycle"));
        leaveBalancePolicy.setCustomLeaveCycleMonth(testData.get("CustomLeaveCycleMonth"));

        ProbationPeriodForLeaveValidity probationPeriodForLeaveValidity = new ProbationPeriodForLeaveValidity();
        probationPeriodForLeaveValidity.custom=testData.get("Leave Probation Period according to Custom Months").equalsIgnoreCase("yes")?true:false;
        probationPeriodForLeaveValidity.probation=testData.get("Leave Probation Period according to Employee Probation Period").equalsIgnoreCase("yes")?true:false;
        //if(probationPeriodForLeaveValidity.probation)
        //employeeProbation=testData.get("Employee Probation Period");
        if(probationPeriodForLeaveValidity.custom)
            probationPeriodForLeaveValidity.customMonths=Integer.parseInt(testData.get("Probation period before leave validity months").replace(".0",""));

        leaveBalancePolicy.setProbation_period_before_leave_validity(probationPeriodForLeaveValidity);


        if(testData.get("Pro rata").equalsIgnoreCase("yes")?true:false){
            Credit_On_Pro_Rata_Basis pro_rata_basis= new Credit_On_Pro_Rata_Basis();
            pro_rata_basis.indicator=true;
            pro_rata_basis.calculateFromJoiningDate=testData.get("From Joining date").equalsIgnoreCase("yes")?true:false;
            pro_rata_basis.calculateAfterProbationPeriod=testData.get("After Probation period").equalsIgnoreCase("yes")?true:false;
            pro_rata_basis.creditHalfMonthsLeavesIfEmpJoinsAfter15Th=testData.get("Half Month Leaves if employee joins after 15th").equalsIgnoreCase("yes")?true:false;
            pro_rata_basis.creditfullMonthsLeavesIfEmpJoinsAfter15Th=testData.get("Full Month Leaves if employee joins after 15th").equalsIgnoreCase("yes")?true:false;

            leaveBalancePolicy.setCredit_on_pro_rata_basis(pro_rata_basis);
        }

        if(testData.get("Accrual").equalsIgnoreCase("yes")?true:false){
            Credit_On_Accural_Basis credit_on_accural_basis= new Credit_On_Accural_Basis();
            credit_on_accural_basis.setIndicator(true);

            if(!testData.get("Monthly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setMonthlyAccuralSetting(false,false,false);
            else
                credit_on_accural_basis.setMonthlyAccuralSetting(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            if(!testData.get("Quarterly").equalsIgnoreCase("yes")?true:false)
                credit_on_accural_basis.setQuarterlyAccural(false,false,false);
            else
                credit_on_accural_basis.setQuarterlyAccural(true,testData.get("Begin of month/Quarter").equalsIgnoreCase("yes")?true:false,testData.get("End of month/Quarter").equalsIgnoreCase("yes")?true:false);

            credit_on_accural_basis.setBiAnnual(testData.get("Biannually").equalsIgnoreCase("yes")?true:false);

            leaveBalancePolicy.setCredit_on_accural_basis(credit_on_accural_basis);
        }


        List<NameValuePair> request=leaveBalancePolicy.createRequest();

        new LeaveService().createLeaveForPolicy(request,leaveBalancePolicy);

        return  leaveBalancePolicy;
    }






    /**
     * This method verifies leaves balance for whole leave cycle of the employee
     *
     * @return boolean
     */
    public boolean verifyEmployeeLeaveBalanceForWholeLeaveCycleForFourEdgeDays() {
        try {
            //Employee employee=null;
            String result = "";
            int flag = 0;
            String leaveCycleStartDate = getFirstDayofLeaveCycle(leavePolicyObject.getLeave_cycle()); //Get first day of leave cycle
            Reporter("Leave Cycle defined is '" + leavePolicyObject.getLeave_cycle() + "',"
                    + " hence leave balance will be verifed from leave cycle start date '" + leaveCycleStartDate
                    + "' till current date", "Info");
            LocalDate leaveCycleStartDateInDateFormat = LocalDate.parse(leaveCycleStartDate); //Get first day of leave cycle in Local date Format
            int i = 0;
            double expectedBalance = 0;
            if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator) {
                expectedBalance = calculateLeaveBalanceAsPerEmployeeWorkingDays(DateOfJoining,
                        getServerOrLocalDate().toString()); //This function calculates leave balance in case of working days
            } else {
                expectedBalance = calculateLeaveBalance(employee.getDoj(),getServerOrLocalDate().toString()); //This important function calculates leave balance
            }
            //double actualBalance = getEmployeesFrontEndLeaveBalance(Leave_Type); //This gets employees leave balance from frontend
            double actualBalance = new LeaveBalanceAPI(employee.getEmployeeID(),leavePolicyObject.getLeave_Type()).getBalance();
            expectedBalance = Math.round(expectedBalance * 100.0) / 100.0;
                    /*
                    In below code we are comparing calculated balance to actual balance shown in frontend
                     */
            if (expectedBalance != actualBalance) {
                Reporter("Failed||" + "DOJ '" + employee.getDoj() + "'||" + "Expected Leave Balance="
                        + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Fail");
                result = "Fail";
                flag++;
            } else {
                Reporter("Passed||" + "DOJ '" + employee.getDoj() + "'||" + "Expected Leave Balance="
                        + expectedBalance + "||Actual Leave Balance=" + actualBalance, "Pass");
                // + expectedBalance + "||Actual Leave Balance=" + "mock test", "Pass");
                result = "Pass";
            }

            if (WriteResultToExcel.equalsIgnoreCase("Yes")) {
                // writeLeavesResultToExcel(DateOfJoining, expectedBalance, actualBalance, result,
                // DateTimeHelper.getCurrentLocalDateAndTime());
            }
            //   }
            //i++;
            //}
            return flag <= 0;

        } catch (Exception e) {
            Reporter("Exception while comparing leave balance", "Error");
            return false;
        }
    }



    /**
     * This method calculates working days leave balance
     *
     * @param DOJ
     * @param toDate
     * @return
     */
    public double calculateLeaveBalanceAsPerEmployeeWorkingDays(String DOJ, String toDate) {
        try {
            double workingDaysBalance = 0;
            int currentYearInEndOfYearFlag = 0;
            double daysConsiderForCalculation = 0;

            if (checkDOJisUnderLeaveProbationPeriod() == true) {
                workingDaysBalance = 0;
            } else if (checkDOJisUnderLeaveProbationPeriod() == false) {
                Leave_Probation_End_Date=DOJ;
                if (!leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().endOfYear) {
                    if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate.toString()))) {
                        DOJ = leaveCycleStartDate.toString();
                    }

                    if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate.toString()))) {
                        Leave_Probation_End_Date = leaveCycleStartDate.toString();
                    }

                }

                if(leavePolicyObject.getCredit_on_pro_rata_basis().indicator) {
                    if (leavePolicyObject.getCredit_on_pro_rata_basis().calculateFromJoiningDate)
                        LeaveCalBeginningDate = DOJ;
                    if (leavePolicyObject.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
                    {
                        if(leavePolicyObject.getProbation_period_before_leave_validity().custom)
                        {
                            LeaveCalBeginningDate=LocalDate.parse(Leave_Probation_End_Date).plusMonths(leavePolicyObject.getProbation_period_before_leave_validity().customMonths).toString();
                        }
                        else
                        LeaveCalBeginningDate = Leave_Probation_End_Date;
                    }
                }
                else{
                    LeaveCalBeginningDate = leaveCycleStartDate.toString();
                }

                /*
                Working Days
                 */

                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().endOfYear) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate.toString()).minusYears(1))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate.toString()).minusYears(1).toString();
                    }
                    if (LocalDate.parse(LeaveCalBeginningDate).isAfter(LocalDate.parse(leaveCycleStartDate.toString()))) {
                        return workingDaysBalance = 0;
                    } else if (LocalDate.parse(LeaveCalBeginningDate).isBefore(LocalDate.parse(leaveCycleStartDate.toString()))) {
                        if (LocalDate.parse(toDate).isAfter(LocalDate.parse(leaveCycleStartDate.toString()))) {
                            toDate = LocalDate.parse(leaveCycleStartDate.toString()).minusDays(1).toString();
                        }
                    }
                } else if (!leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().endOfYear) {
                    if (LocalDate.parse(LeaveCalBeginningDate)
                            .isBefore(LocalDate.parse(leaveCycleStartDate.toString()))) {
                        LeaveCalBeginningDate = LocalDate.parse(leaveCycleStartDate.toString()).toString();
                    }

                    if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator &&
                            leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()) {
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Month", leavePolicyObject.getLeave_cycle()))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", leavePolicyObject.getLeave_cycle()).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Month", leavePolicyObject.getLeave_cycle()).minusMonths(1).toString();
                        }
                    } else if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator &&
                            !leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth() &&
                            leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()){
                        if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", leavePolicyObject.getLeave_cycle()))) {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", leavePolicyObject.getLeave_cycle()).toString();
                        } else {
                            toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Quarter", leavePolicyObject.getLeave_cycle()).minusMonths(3).toString();
                        }
                    }

                    else if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator &&
                            !leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter() &&
                            leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()) {
                        //if (LocalDate.parse(toDate).isEqual(getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", leavePolicyObject.getLeave_cycle()))) {
                           // toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", leavePolicyObject.getLeave_cycle()).toString();
                            toDate=leaveCycleEndDate.minusDays(1).toString();
                        //} else {
                          //  toDate = getLastDayOfMonth_Quarter_Biannual(toDate, "Biannual", leavePolicyObject.getLeave_cycle()).minusMonths(6).toString();
                        //}
                    }
                }

                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator) {
                    //cahnge the logic
                    //calculate the number of days from start date to end date
                    daysConsiderForCalculation = getServerOrLocalDate().lengthOfYear();
                } else {
                    daysConsiderForCalculation = getServerOrLocalDate().lengthOfYear();
                }

                double workingDays = getWorkingDaysToConsiderForCalculation(LeaveCalBeginningDate, toDate);
                workingDaysBalance = leavePolicyObject.getMaximum_leave_allowed_per_year() * (workingDays / daysConsiderForCalculation);

            }
            return workingDaysBalance;
        } catch (Exception e) {
            Reporter("Exception while calculating working days leave balance", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * This method checks if employee DOJ is under Leave Probation period
     *
     * @return
     */
    public boolean checkDOJisUnderLeaveProbationPeriod() {
        try {
            String todaysDate = getServerOrLocalDate().toString();

            int flag = 0;

            if(employee!=null) {
                if (leavePolicyObject.getProbation_period_before_leave_validity().custom
                        && !leavePolicyObject.getProbation_period_before_leave_validity().probation) {
                    double probation_Months = Double.valueOf(leavePolicyObject.getProbation_period_before_leave_validity().customMonths);
                    double monthsDiff = objDateTimeHelper.getExactMonthDifferenceBetweenTwoDates(employee.getDoj(), todaysDate);
                    if (monthsDiff < probation_Months) {
                        flag++;
                    }
                    long longPBMonth = (long) (-probation_Months);
                    Leave_Probation_End_Date = LocalDate.parse(employee.getDoj()).minusMonths(longPBMonth).toString();
                }

                if (!leavePolicyObject.getProbation_period_before_leave_validity().custom
                        && leavePolicyObject.getProbation_period_before_leave_validity().probation) {
                    double daysDiff = objDateTimeHelper.getDaysDifferenceBetweenTwoDates(employee.getDoj(), todaysDate);
                    double Employee_probation_period_Int = Double.valueOf(employee.getProbation());

                    if (daysDiff < Employee_probation_period_Int) {
                        flag++;
                    }

                    long daysToSubtract = (long) (-Employee_probation_period_Int);
                    Leave_Probation_End_Date = LocalDate.parse(employee.getDoj()).minusDays(daysToSubtract).toString();

                }

                if (employee.getProbation() != null && !employee.getProbation().equalsIgnoreCase("no")) {

                    double daysDiff1 = objDateTimeHelper.getDaysDifferenceBetweenTwoDates(employee.getDoj(), todaysDate);
                    double Employee_probation_period_Int1 = Double.valueOf(employee.getProbation());
                    if (daysDiff1 < Employee_probation_period_Int1) {
                        flag++;
                    }


                    long daysToSubtract = (long) (-Employee_probation_period_Int1);
                    Leave_Probation_End_Date = LocalDate.parse(employee.getDoj()).minusDays(daysToSubtract).toString();
                }
            }
            if(employee==null & EmployeeId!=null){
                if (leavePolicyObject.getProbation_period_before_leave_validity().custom
                        && !leavePolicyObject.getProbation_period_before_leave_validity().probation)
                {
                    int custom_Months = Integer.valueOf(leavePolicyObject.getProbation_period_before_leave_validity().customMonths);
                    if(LocalDate.parse(DateOfJoining).plusMonths(custom_Months).isAfter(getServerOrLocalDate()))

                        return true;
                    else
                        return false;
                }
            }

            return flag > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public LocalDate getLastDayOfMonth_Quarter_Biannual(String date, String monthQuarterBiannual, String leaveCycle) {
        try {
            if (monthQuarterBiannual.equalsIgnoreCase("Month")) {
                return LocalDate.parse(date).withDayOfMonth(LocalDate.parse(date).lengthOfMonth());
            } else if (monthQuarterBiannual.equalsIgnoreCase("Quarter")) {
                return getLastDayOfQuarter(date);
            } else if (monthQuarterBiannual.equalsIgnoreCase("Biannual")) {
                return getLastDayOfBiannual(date, leaveCycle);
            } else {
                throw new RuntimeException("Variable is not month, quarter or biannual");
            }

        } catch (Exception e) {
            throw new RuntimeException("Exception while getting last day of " + monthQuarterBiannual + ":" + e);
        }
    }

    public LocalDate getLastDayOfBiannual(String DATEIN_YYYY_MM_DD_format, String leaveCycle) {
        try {
          return   leaveCycleEndDate;

        } catch (Exception e) {
            Reporter("Exception while calculation last day of Biannual Half", "Error");
            throw new RuntimeException("Exception while calculation last day of Biannual Half");
        }
    }


    public double getWorkingDaysToConsiderForCalculation(String fromDate, String toDate) {
        try {
            double daysToAdd = 0;
            JSONObject workingDaysJsonObj=null;

            if(employee!=null)
             workingDaysJsonObj = getWorkingDaysDetailsOfEmployeeFromApplication(fromDate, toDate, employee.getEmployeeID());
            else
             workingDaysJsonObj = getWorkingDaysDetailsOfEmployeeFromApplication(fromDate, toDate, EmployeeId);

            Double presentDays = Double.valueOf(workingDaysJsonObj.get("present").toString());
            Double weeklyoff = Double.valueOf(workingDaysJsonObj.get("weeklyoff").toString());
            Double optional = Double.valueOf(workingDaysJsonObj.get("optional").toString());
            Double holiday = Double.valueOf(workingDaysJsonObj.get("holiday").toString());
            Double leave = Double.valueOf(workingDaysJsonObj.get("leave").toString());
            Double absent = Double.valueOf(workingDaysJsonObj.get("absent").toString());

            StringBuffer workingDaysDetailsString = new StringBuffer();

            if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator) {
                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().presentDays) {
                    daysToAdd = daysToAdd + presentDays;
                    workingDaysDetailsString.append("Present=" + presentDays + ",");
                }
                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().absentDaysAndUnpaidLeave) {
                    daysToAdd = daysToAdd + absent;
                    workingDaysDetailsString.append("Absent=" + absent + ",");
                }
                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().weeklyOffs) {
                    daysToAdd = daysToAdd + weeklyoff;
                    workingDaysDetailsString.append("WeeklyOff=" + weeklyoff + ",");
                }
                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().holidays) {
                    daysToAdd = daysToAdd + holiday;
                    workingDaysDetailsString.append("Holiday=" + holiday + ",");
                }
                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().optionalHoliodays) {
                    daysToAdd = daysToAdd + optional;
                    workingDaysDetailsString.append("Optional=" + optional + ",");
                }
                if (leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().leaveDays) {
                    daysToAdd = daysToAdd + leave;
                    workingDaysDetailsString.append("Leaves=" + leave + ",");
                }
            } else {
                daysToAdd = 0;
                Reporter("Leave Accrual Based on Working Days is set to 'NO'", "Info");
            }

            workingDaysDetailsString.append("Total Days=" + daysToAdd);
            Reporter(workingDaysDetailsString.toString(), "Pass");
            return daysToAdd;
        } catch (Exception e) {
            Reporter("Exception while calculating working Days leave balance", "Error");
            throw new RuntimeException();
        }
    }

    public JSONObject getWorkingDaysDetailsOfEmployeeFromApplication(String fromDate, String toDate,String empId) {
        try {
            JSONObject jsonObject = null;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/Emailtemplate/Workingdays?id=" + empId + "&from=" + fromDate
                        + "&enddate=" + toDate;
                driver.navigate().to(URL);
                String frontEndValue = driver.findElement(By.xpath("//body")).getText();
                if (frontEndValue.isEmpty()) {
                    Reporter("May be Employee id is not present", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }

                JSONParser parser = new JSONParser();
                jsonObject = (JSONObject) parser.parse(frontEndValue);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                // actualLeaveBalance =
                // getEmployeesFrontEndDeactivationLeaveBalanceUsingAPIs(leaveType,
                // deactivationDate);
            }
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while getting working days details from API.", "Error");
            throw new RuntimeException();
        }

    }

    /**
     * This method calculate leave balance
     *
     * @param DOJ
     * @return double
     */
    public double calculateLeaveBalance(String DOJ) {
        try {
            String leaveCycleStartDate = getFirstDayofLeaveCycle(leavePolicyObject.getLeave_cycle());
            String leaveCycleEndDate = getLastDayofLeaveCycle(leavePolicyObject.getLeave_cycle());
            double midJoinigYesLeaves = 0;
            double perMonthLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12);
            double perMonthOrQuarterLeaves = 0;
            double MonthOrQuarterDifference = 0;
            double leavesDiffFromFirstDayOfQuarter = 0;
            String leavesCalculationStartDate = "";
            String midYearEndDate;
            double biannualLeave = 0;

            //This checks whether employee is in probation period or outside. It also calculates Probation End Date/
            // Confirmation date of employee
            if (checkDOJisUnderLeaveProbationPeriod() == true) {
                ExpectedLeaveBalance = 0; // If employee is under probation period his expected balance will be 0
            } else if (checkDOJisUnderLeaveProbationPeriod() == false) {
                Leave_Probation_End_Date=DOJ;
                if ((LocalDate.parse(DOJ)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    DOJ = leaveCycleStartDate; //This sets DOJ for calculation as Leave Cycle start date even if his his actual DOJ is in past year
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(LocalDate.parse(leaveCycleStartDate))) {
                    Leave_Probation_End_Date = leaveCycleStartDate; //This sets Probation end date for calculation as Leave Cycle start date even if his his actual Probation end date is in past year
                }

                /**
                 * This function sets Leave calculation starts date based on Pro Rata
                 */
                if (leavePolicyObject.getCredit_on_pro_rata_basis().indicator
                        && leavePolicyObject.getCredit_on_pro_rata_basis().calculateFromJoiningDate
                        && !leavePolicyObject.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod) {
                    LeaveCalBeginningDate = DOJ; //If Pro Rata is Yes and calculation is started from joining date, Leave calculation begin date is from employees date of joining
                } else if (leavePolicyObject.getCredit_on_pro_rata_basis().indicator
                        && !leavePolicyObject.getCredit_on_pro_rata_basis().calculateFromJoiningDate
                        && leavePolicyObject.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod) {
                    LeaveCalBeginningDate = Leave_Probation_End_Date; //If Pro Rata is Yes and calculation is started after probabtion period, Leave calculation begin date is from employees date of joining
                } else if (!leavePolicyObject.getCredit_on_pro_rata_basis().indicator) {
                    LeaveCalBeginningDate = DOJ; //If Pro Rata is No, Leave calculation begin date is from employees date of joining
                }

                /**
                 * This function takes care of all combination of Pro-Rata and accrual
                 */
                if (leavePolicyObject.getCredit_on_pro_rata_basis().indicator) {
                    if (leavePolicyObject.getCredit_on_accural_basis().getIndicator()) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    if (!leavePolicyObject.getCredit_on_accural_basis().getIndicator() || leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, leaveCycleEndDate);
                        if (MonthOrQuarterDifference < 0) {
                            MonthOrQuarterDifference = -MonthOrQuarterDifference;
                        }
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    }
                } else if (!leavePolicyObject.getCredit_on_pro_rata_basis().indicator) {
                    leavesCalculationStartDate = leaveCycleStartDate;
                    if (!leavePolicyObject.getCredit_on_accural_basis().getIndicator() ||
                            leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        MonthOrQuarterDifference = 12;
                    }
                }
                /*
                 * This function determines checks whether employee has joined before or after 15th of month and
                 * based on that sets midJoiningYesLeaves variable
                 */
                if (leavePolicyObject.getCredit_on_pro_rata_basis().indicator) {
                    if (leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th  &&
                            !leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th) {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12)*0.5;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    } else if (leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th
                            && !leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th) {
                        midJoinigYesLeaves = 0;
                    } else if (!leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th
                            && !leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th) {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12);
                            // midoinigYesLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    } else if (leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th
                            && leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th) {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 24);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }
                }
                /**
                 * This function deals with all cases of Accrual Month, Quarter and Biannual
                 */
                if (leavePolicyObject.getCredit_on_accural_basis().getIndicator()) {
                    /*
                    This function calculates month leave balance
                     */
                    if (leavePolicyObject.getCredit_on_accural_basis().getMonth() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getQuarter() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()) {
                        perMonthOrQuarterLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12); //Per month leave is No. of Leaves divided by 12 months
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceFromCurrentDate(leavesCalculationStartDate,leaveCycleEndDate); //This function calculates month difference between Current date and employees Date of Joining
                        leavesDiffFromFirstDayOfQuarter = 0;
                    }
                    /*
                    This function calculates quarter leave balance
                     */
                    else if (!leavePolicyObject.getCredit_on_accural_basis().getMonth() &&
                            leavePolicyObject.getCredit_on_accural_basis().getQuarter() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()) {
                        perMonthOrQuarterLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 4); //Per Quarter leave is No. of Leaves divided by 4 months
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getQuarterDiffFromCurrentDate(DOJ,leaveCycleStartDate);
                        //This function calculates quarter difference between Current date and employees Date of Joining
                        if(leaveCycleStartDate!=leavesCalculationStartDate)
                            leavesDiffFromFirstDayOfQuarter = ((perMonthLeaves)
                                    * getMonthDiffFromFirstDayOfQuarter(leavesCalculationStartDate)); //This function calculates month difference between date of joining and first day of quarter
                        else
                            leavesDiffFromFirstDayOfQuarter=0;
                    }/*
                    This function calculates biannual leave balance
                     */ else if (!leavePolicyObject.getCredit_on_accural_basis().getMonth() &&
                            !leavePolicyObject.getCredit_on_accural_basis().getQuarter() &&
                            leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate); //This checks DOJ is in which binannual half
                        String currentDateBiannualHalf =
                                checkBiannualHalfOfDate(
                                        LocalDate.parse(serverChangedDate).toString()); //This checks current date is in which binannual half
                        midYearEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(6).toString(); //Calculate biannual end date
                        String biannualEndDate = "";
                        /*
                       Below code assigns biannualEndDate as per biannual half
                         */
                        if (DOJBiannualHalf.equalsIgnoreCase("First")) {
                            biannualEndDate = midYearEndDate;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualEndDate = leaveCycleEndDate;
                        }
                        /*
                        Below code calculates month difference between leave calculation start date and biannual end date
                         */
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(leavesCalculationStartDate, biannualEndDate) + 1;

                        /*
                        Below logic calculates whether to credit Halfl  year leave based on DOJ and Current date biannual half
                         */
                        if (DOJBiannualHalf.equalsIgnoreCase("First")
                                && currentDateBiannualHalf.equalsIgnoreCase("First")) {
                            biannualLeave = 0;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("First")
                                && currentDateBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualLeave = leavePolicyObject.getMaximum_leave_allowed_per_year() / 2;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")
                                && currentDateBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualLeave = 0;
                        } else {
                            Reporter("Exception while calculating Biannual Leaves", "Error");
                            throw new RuntimeException();
                        }
                    }

                    /*
                    Below code adds 1 to no. of months multipler if needs to begin of month or quarter scenaro
                     */
                    if ((leavePolicyObject.getCredit_on_accural_basis().getMonth() || leavePolicyObject.getCredit_on_accural_basis().getQuarter())
                            && !(leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth() || leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter())
                            && !leavePolicyObject.getCredit_on_accural_basis().getBiAnnual()) {
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    }
                }
                /*
                Formula to calculate leave balance
                 */
                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) + biannualLeave);
            }
            double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance * 100.0) / 100.0;

            if (ExpectedLeaveBalanceRoundOff < 0) {
                ExpectedLeaveBalanceRoundOff = 0;
            }

            return ExpectedLeaveBalanceRoundOff;
        } catch (Exception e) {
            Reporter("Exception while calculating employess expected leave balance", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
     * This method calculate leave balance
     *
     * @param DOJ,toDate
     * @return double
     */
    public double calculateLeaveBalance(String DOJ,String toDate) {
        try {
            //String leaveCycleStartDate = "2019-08-01";
            //String leaveCycleEndDate = "2020-07-31";
            double midJoinigYesLeaves = 0;
            double perMonthLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12);
            double perMonthOrQuarterLeaves = 0;
            double MonthOrQuarterDifference = 0;
            double leavesDiffFromFirstDayOfQuarter = 0;
            String leavesCalculationStartDate = "";
            String midYearEndDate;
            double biannualLeave = 0;

            if (checkDOJisUnderLeaveProbationPeriod() == true) {
                ExpectedLeaveBalance = 0;
            } else if (checkDOJisUnderLeaveProbationPeriod() == false) {
                Leave_Probation_End_Date = DOJ;
                if ((LocalDate.parse(DOJ)).isBefore(leaveCycleStartDate)) {
                    DOJ = leaveCycleStartDate.toString();
                }

                if ((LocalDate.parse(Leave_Probation_End_Date)).isBefore(leaveCycleStartDate)) {
                    Leave_Probation_End_Date = leaveCycleStartDate.toString();
                }

                /*
                pro rata no and accural no
                 */
                if (!leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
                {
                    leavesCalculationStartDate = leaveCycleStartDate.toString();
                    LeaveCalBeginningDate = leaveCycleStartDate.toString();
                    if (!leavePolicyObject.getCredit_on_accural_basis().getIndicator() ||
                            leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        MonthOrQuarterDifference = 12;
                    }
                }

                /*
                pro rata - yes
                 */
                if (leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
                {
                    if(leavePolicyObject.getCredit_on_pro_rata_basis().calculateFromJoiningDate)
                        LeaveCalBeginningDate = DOJ;

                    if(leavePolicyObject.getCredit_on_pro_rata_basis().calculateAfterProbationPeriod)
                    {
                        if (leavePolicyObject.getProbation_period_before_leave_validity().custom)
                            LeaveCalBeginningDate = LocalDate.parse(DOJ).plusMonths(leavePolicyObject.getProbation_period_before_leave_validity().customMonths).toString();
                        else if(leavePolicyObject.getProbation_period_before_leave_validity().probation)
                            LeaveCalBeginningDate = Leave_Probation_End_Date;

                        else
                            LeaveCalBeginningDate = Leave_Probation_End_Date;
                    }

                }


                /*
                pro rata yes and accural no
                 */
                if (leavePolicyObject.getCredit_on_pro_rata_basis().indicator)
                {
                    if (leavePolicyObject.getCredit_on_accural_basis().getIndicator()) {
                        leavesCalculationStartDate = LeaveCalBeginningDate;
                    }
                    /*
                    Working Days
                     */
                    if (!leavePolicyObject.getCredit_on_accural_basis().getIndicator() || leavePolicyObject.getCredit_on_accural_basis().getConsiderWorkingDays().indicator) {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(LeaveCalBeginningDate, leaveCycleEndDate.toString());
                        if (MonthOrQuarterDifference < 0) {
                            MonthOrQuarterDifference = -MonthOrQuarterDifference;
                        }
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    }
                }




                if (leavePolicyObject.getCredit_on_pro_rata_basis().indicator) {
                    if (leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th )
                    {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12)*0.5;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }
                    else if (leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
                    {
                        midJoinigYesLeaves = 0;
                    }
                    else if (!leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th
                            && !leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
                    {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12);
                            // midoinigYesLeaves = 0;
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }
                    else if (leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th
                            && leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
                    {
                        if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate).equalsIgnoreCase("Yes")) {
                            midJoinigYesLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 24);
                        } else if (objDateTimeHelper.verifyDOJMidJoining(LeaveCalBeginningDate)
                                .equalsIgnoreCase("No")) {
                            midJoinigYesLeaves = 0;
                        }
                    }
                }
                /**
                 * This function deals with all cases of Accrual Month, Quarter and Biannual
                 */
                if (leavePolicyObject.getCredit_on_accural_basis().getIndicator()) {
                    /*
                    This function calculates month leave balance
                     */
                    if (leavePolicyObject.getCredit_on_accural_basis().getMonth()) {
                        perMonthOrQuarterLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 12);
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceFromCurrentDate(leavesCalculationStartDate,toDate);
                        leavesDiffFromFirstDayOfQuarter = 0;
                    }
                    /*
                    This function calculates quarter leave balance
                     */
                    else if (leavePolicyObject.getCredit_on_accural_basis().getQuarter())
                    {
                        perMonthOrQuarterLeaves = (leavePolicyObject.getMaximum_leave_allowed_per_year() / 4);
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getQuarterDiffFromCurrentDate(serverChangedDate,leavesCalculationStartDate);

                        if(leavePolicyObject.getCredit_on_accural_basis().getEndOfQuarter())
                            leavesDiffFromFirstDayOfQuarter = 0;
                        else
                            leavesDiffFromFirstDayOfQuarter = ((perMonthLeaves)
                                    * getMonthDiffFromFirstDayOfQuarter(leavesCalculationStartDate));
                    }
                    /*
                    This function calculates biannual leave balance
                     */
                    else if (leavePolicyObject.getCredit_on_accural_basis().getBiAnnual())
                    {
                        perMonthOrQuarterLeaves = perMonthLeaves;
                        String DOJBiannualHalf = checkBiannualHalfOfDate(LeaveCalBeginningDate); //This checks DOJ is in which binannual half
                        String currentDateBiannualHalf = checkBiannualHalfOfDate(
                                leaveCycleEndDate.toString()); //This checks current date is in which binannual half
                        midYearEndDate = LocalDate.parse(leaveCycleEndDate.toString()).minusMonths(6).toString(); //Calculate biannual end date
                        String biannualEndDate = "";
                        /*
                       Below code assigns biannualEndDate as per biannual half
                         */
                        if (DOJBiannualHalf.equalsIgnoreCase("First")) {
                            biannualEndDate = midYearEndDate;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualEndDate = leaveCycleEndDate.toString();
                        }
                        /*
                        Below code calculates month difference between leave calculation start date and biannual end date
                         */
                        MonthOrQuarterDifference = objDateTimeHelper
                                .getMonthDifferenceBetweenTwoDates(biannualEndDate, leavesCalculationStartDate);

                        /*
                        Below logic calculates whether to credit Halfl  year leave based on DOJ and Current date biannual half
                         */
                        if (DOJBiannualHalf.equalsIgnoreCase("First")
                                && currentDateBiannualHalf.equalsIgnoreCase("First")) {
                            biannualLeave = 0;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("First")
                                && currentDateBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualLeave = leavePolicyObject.getMaximum_leave_allowed_per_year() / 2;
                        } else if (DOJBiannualHalf.equalsIgnoreCase("Second")
                                && currentDateBiannualHalf.equalsIgnoreCase("Second")) {
                            biannualLeave = 0;
                        } else {
                            Reporter("Exception while calculating Biannual Leaves", "Error");
                            throw new RuntimeException();
                        }
                    }

                    /*
                    Below code adds 1 to no. of months multipler if needs to begin of month or quarter scenaro
                     */
                    if (((leavePolicyObject.getCredit_on_accural_basis().getMonth() || leavePolicyObject.getCredit_on_accural_basis().getQuarter())
                            && (leavePolicyObject.getCredit_on_accural_basis().getBeginOfMonth() || leavePolicyObject.getCredit_on_accural_basis().getBeginOfQuarter()))
                            || (leavePolicyObject.getCredit_on_accural_basis().getBiAnnual())) {
                        MonthOrQuarterDifference = MonthOrQuarterDifference + 1;
                    }
                }
                /*
                Formula to calculate leave balance
                 */
                ExpectedLeaveBalance = (((perMonthOrQuarterLeaves) * (MonthOrQuarterDifference))
                        - (leavesDiffFromFirstDayOfQuarter) - (midJoinigYesLeaves) + biannualLeave);

                if(deActiavation)
                {
                    if(leavePolicyObject.getCredit_on_accural_basis().getIndicator()){
                     //   ExpectedLeaveBalance = leavePolicyObject.getMaximum_leave_allowed_per_year();
                    }
                    if(LocalDate.parse(toDate).getDayOfMonth()<=15){
                        int  months=0;
                        //add +1 for current month
                    // if(!leavePolicyObject.getCredit_on_accural_basis().getEndOfMonth())
                        if(!leavePolicyObject.getCredit_on_accural_basis().getIndicator())
                      months= Period.between(LocalDate.parse(toDate),leaveCycleEndDate).getMonths()+1;
                     //else
                       //  months=Period.between(LocalDate.parse(toDate),leaveCycleEndDate).getMonths();
                      ExpectedLeaveBalance = ExpectedLeaveBalance - months*perMonthLeaves;
                      if(leavePolicyObject.getCredit_on_pro_rata_basis().indicator){
                          if(leavePolicyObject.getCredit_on_pro_rata_basis().creditHalfMonthsLeavesIfEmpJoinsAfter15Th)
                              ExpectedLeaveBalance = ExpectedLeaveBalance + 0.5;
                          else if(leavePolicyObject.getCredit_on_pro_rata_basis().creditfullMonthsLeavesIfEmpJoinsAfter15Th)
                              ExpectedLeaveBalance = ExpectedLeaveBalance + perMonthLeaves;
                          else
                              ExpectedLeaveBalance = ExpectedLeaveBalance - perMonthLeaves;
                      }
                    }
                    else {
                        int months=0;
                        if(!leavePolicyObject.getCredit_on_accural_basis().getIndicator())
                         months= Period.between(LocalDate.parse(toDate),leaveCycleEndDate).getMonths();
                        ExpectedLeaveBalance = ExpectedLeaveBalance - months*perMonthLeaves;
                    }
                }

            }
            double ExpectedLeaveBalanceRoundOff = Math.round(ExpectedLeaveBalance * 100.0) / 100.0;

            if (ExpectedLeaveBalanceRoundOff < 0) {
                ExpectedLeaveBalanceRoundOff = 0;
            }

            return ExpectedLeaveBalanceRoundOff;
        } catch (Exception e) {
            Reporter("Exception while calculating employess expected leave balance", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }




    /**
     * This method calculate and returns whether Date falls in First or Second
     * Biannual Half
     *
     * @param DATEIN_YYYY_MM_DD_format
     * @return
     */
    public String checkBiannualHalfOfDate(String DATEIN_YYYY_MM_DD_format) {
        try {
            String biannualHalf = "";
            String employeeConfirmationdate = LocalDate.parse(DATEIN_YYYY_MM_DD_format).toString();
            String leaveCycleEndDate = getLastDayofLeaveCycle(leavePolicyObject.getLeave_cycle());
            double monthsDiff = objDateTimeHelper.getMonthDifferenceBetweenTwoDates(leaveCycleEndDate,
                    employeeConfirmationdate);

            if (monthsDiff >= 6) {
                biannualHalf = "First";
            } else if (monthsDiff < 6) {
                biannualHalf = "Second";
            }
            return biannualHalf;
        } catch (Exception e) {
            e.printStackTrace();
            Reporter("Exception while calculating Biannual Leave Balance", "Error");
            throw new RuntimeException();
        }
    }


    /**
     * This method changes Employee Date of Joining
     *
     * @param iterationDate
     * @return DOJ as String
     */
    public String  changeEmployeeDOJ(LocalDate iterationDate,Employee employee) {
        try {
            String DOJ = null;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DOJ = iterationDate.format(formatter);
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/emailtemplate/employeedoj?id=" + employee.getEmployeeID() + "&date=" + DOJ;
                driver.navigate().to(URL);
                String frontEndDOJ = driver.findElement(By.xpath("//body")).getText();

                if (frontEndDOJ.trim().equals("DOJ Not changed")) {
                    for (int i = 0; i < 3; i++) {
                        driver.navigate().to(URL);
                        frontEndDOJ = driver.findElement(By.xpath("//body")).getText();
                        if (frontEndDOJ.trim().equals(DOJ)) {
                            break;
                        }
                    }
                }

                if (!frontEndDOJ.trim().equals(DOJ)) {
                    Reporter("DOJ not changed to '" + DOJ + "'", "Warning");
                }
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                DOJ = changeEmployeeDOJ(iterationDate,employee);
            }
            return DOJ;
        } catch (Exception e) {
            Reporter("Exception while changing employees DOJ", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
     * This method returns first day of Leave Cycle provided
     *
     * @param leaveCycle
     * @return String leaveCycleStartDate
     */
    public String getFirstDayofLeaveCycle(String leaveCycle) {
        try {
            String leaveCycleStartDate = "";

            int year = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";
            String days=null;
            if(year/4 == 0 && leavePolicyObject.getCustomLeaveCycleMonth().equalsIgnoreCase("febrauary"))
                days="29";
            else
                days=getMonthLength(leavePolicyObject.getCustomLeaveCycleMonth());

            String month=getMonthValue(leavePolicyObject.getCustomLeaveCycleMonth());
            String customLeaveCycleStartDate = year + "-" + month + "-" +"01";

            LocalDate customLeaveCycleEndDateInDateFormat=LocalDate.parse(customLeaveCycleStartDate).plusYears(1).minusDays(1);
            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate.parse(calendarYearEndDate);
            LocalDate today = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            int previousYear = year - 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    leaveCycleStartDate = previousYear + "-" + "04" + "-" + "01";
                } else {
                    leaveCycleStartDate = today.getYear() + "-" + "04" + "-" + "01";
                }
            }
            if(leaveCycle.equalsIgnoreCase("custom leave cycle")){
                if (customLeaveCycleEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    leaveCycleStartDate = today.getYear() + "-" + month + "-" + "01";
                } else {
                    leaveCycleStartDate = previousYear + "-" + month + "-" + "01";
                }

            }else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleStartDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear() + "-" + "01" + "-"
                        + "01";
            }
            return leaveCycleStartDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This method changes Employee Date of Joining
     *
     * @param iterationDate
     * @return DOJ as String
     */
    public String  changeEmployeeDOJ(LocalDate iterationDate) {
        try {
            String DOJ = null;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DOJ = iterationDate.format(formatter);
                String applicationURL = data.get("@@url");
                String URL = applicationURL + "/emailtemplate/employeedoj?id=" + EmployeeId + "&date=" + DOJ;
                driver.navigate().to(URL);
                String frontEndDOJ = driver.findElement(By.xpath("//body")).getText();

                if (frontEndDOJ.trim().equals("DOJ Not changed")) {
                    for (int i = 0; i < 3; i++) {
                        driver.navigate().to(URL);
                        frontEndDOJ = driver.findElement(By.xpath("//body")).getText();
                        if (frontEndDOJ.trim().equals(DOJ)) {
                            break;
                        }
                    }
                }

                if (!frontEndDOJ.trim().equals(DOJ)) {
                    Reporter("DOJ not changed to '" + DOJ + "'", "Warning");
                }

            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                DOJ = changeEmployeeDOJ(iterationDate);
            }
            return DOJ;
        } catch (Exception e) {
            Reporter("Exception while changing employees DOJ", "Error");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * This method returns first day of Leave Cycle provided
     *
     * @param leaveCycle
     * @return String leaveCycleStartDate
     */
    public String getFirstDayofLeaveCycle(String leaveCycle, String calculationDate) {
        try {
            String leaveCycleStartDate = "";
            int year = LocalDate.now().getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";


            String days=null;
            if(year/4 == 0 && leavePolicyObject.getCustomLeaveCycleMonth().equalsIgnoreCase("febrauary"))
                days="29";
            else
                days=getMonthLength(leavePolicyObject.getCustomLeaveCycleMonth());

            String month=getMonthValue(leavePolicyObject.getCustomLeaveCycleMonth());
            String customLeaveCycleStartDate = year + "-" + month + "-" +"01";

            LocalDate customLeaveCycleEndDateInDateFormat=LocalDate.parse(customLeaveCycleStartDate).plusYears(1).minusDays(1);

            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate today = LocalDate.parse(calculationDate);
            int previousYear = year - 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(today)
                        || financialYearEndDateInDateFormat.isEqual(today)) {
                    leaveCycleStartDate = previousYear + "-" + "04" + "-" + "01";
                } else {
                    leaveCycleStartDate = today.getYear() + "-" + "04" + "-" + "01";
                }
            }
            if(leaveCycle.equalsIgnoreCase("custom leave cycle")) {
                if (customLeaveCycleEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    leaveCycleStartDate = today.getYear() + "-" + month + "-" + "01";
                } else {
                    leaveCycleStartDate = previousYear + "-" + month + "-" + "01";
                }

            }else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleStartDate = today.getYear() + "-" + "01" + "-" + "01";
            }
            return leaveCycleStartDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



    public String getMonthValue(String month){
        if(month.equalsIgnoreCase("january"))
            return "01";
        if(month.equalsIgnoreCase("february"))
            return "02";
        if(month.equalsIgnoreCase("march"))
            return "03";
        if(month.equalsIgnoreCase("april"))
            return "04";
        if(month.equalsIgnoreCase("may"))
            return "05";
        if(month.equalsIgnoreCase("june"))
            return "06";
        if(month.equalsIgnoreCase("july"))
            return "07";
        if(month.equalsIgnoreCase("august"))
            return "08";
        if(month.equalsIgnoreCase("september"))
            return "09";
        if(month.equalsIgnoreCase("october"))
            return "10";
        if(month.equalsIgnoreCase("november"))
            return "11";
        if(month.equalsIgnoreCase("december"))
            return "12";

        return "00";
    }


    public String getMonthLength(String month){
        if(month.equalsIgnoreCase("january"))
            return "31";
        if(month.equalsIgnoreCase("february"))
            return "28";
        if(month.equalsIgnoreCase("march"))
            return "31";
        if(month.equalsIgnoreCase("april"))
            return "30";
        if(month.equalsIgnoreCase("may"))
            return "31";
        if(month.equalsIgnoreCase("june"))
            return "30";
        if(month.equalsIgnoreCase("july"))
            return "31";
        if(month.equalsIgnoreCase("august"))
            return "31";
        if(month.equalsIgnoreCase("september"))
            return "30";
        if(month.equalsIgnoreCase("october"))
            return "31";
        if(month.equalsIgnoreCase("november"))
            return "30";
        if(month.equalsIgnoreCase("december"))
            return "31";

        return "00";
    }

    /**
     * This method returns last day of Leave Cycle
     *
     * @param leaveCycle
     * @return String leaveCycleEndDate
     */
    public String getLastDayofLeaveCycle(String leaveCycle) {
        try {
            String leaveCycleEndDate = "";
            int year = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";

            String days=null;
            if(year/4 == 0 && leavePolicyObject.getCustomLeaveCycleMonth().equalsIgnoreCase("febrauary"))
                days="29";
            else
                days=getMonthLength(leavePolicyObject.getCustomLeaveCycleMonth());

            String month=getMonthValue(leavePolicyObject.getCustomLeaveCycleMonth());
            String customLeaveCycleStartDate = year + "-" + month + "-" +"01";

            LocalDate customLeaveCycleEndDateInDateFormat=LocalDate.parse(customLeaveCycleStartDate).plusYears(1).minusDays(1);

            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate.parse(calendarYearEndDate);
            LocalDate today = LocalDate.parse(DateTimeHelper.getCurrentLocalDate());
            int nextYear = year + 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    leaveCycleEndDate = today.getYear() + "-" + "03" + "-" + "31";
                } else {
                    leaveCycleEndDate = nextYear + "-" + "03" + "-" + "31";
                }
            }
            if (leaveCycle.equalsIgnoreCase("custom leave cycle")) {
                if (customLeaveCycleEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    //  leaveCycleEndDate = nextYear + "-" + month + "-" + days;
                    //leaveCycleEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(1).toString();
                    leaveCycleEndDate = LocalDate.parse(customLeaveCycleStartDate).plusYears(1).minusDays(1).toString();

                } else {
                    //leaveCycleEndDate = today.getYear() + "-" + month + "-" + days;
                    //leaveCycleEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(1).toString();
                    leaveCycleEndDate = LocalDate.parse(customLeaveCycleStartDate).plusYears(1).minusDays(1).toString();

                }
            }else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleEndDate = LocalDate.parse(DateTimeHelper.getCurrentLocalDate()).getYear() + "-" + "12" + "-"
                        + "31";
            }
            return leaveCycleEndDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * This method returns last day of Leave Cycle
     *
     * @param leaveCycle
     * @return String leaveCycleEndDate
     */
    public String getLastDayofLeaveCycle(String leaveCycle, String calculationDate) {
        try {
            String leaveCycleEndDate = "";

            int year = LocalDate.parse(calculationDate).getYear();

            String calendarYearEndDate = year + "-" + "12" + "-" + "31";
            String financialYearEndDate = year + "-" + "03" + "-" + "31";
            String days=null;
            if(year/4 == 0 && leavePolicyObject.getCustomLeaveCycleMonth().equalsIgnoreCase("febrauary"))
                days="29";
            else
                days=getMonthLength(leavePolicyObject.getCustomLeaveCycleMonth());

            String month=getMonthValue(leavePolicyObject.getCustomLeaveCycleMonth());
            String customLeaveCycleStartDate = year + "-" + month + "-" +"01";

            LocalDate customLeaveCycleEndDateInDateFormat=LocalDate.parse(customLeaveCycleStartDate).plusYears(1).minusDays(1);

            LocalDate financialYearEndDateInDateFormat = LocalDate.parse(financialYearEndDate);
            LocalDate.parse(calendarYearEndDate);
            LocalDate today = LocalDate.parse(calculationDate);
            int nextYear = year + 1;

            if (leaveCycle.equalsIgnoreCase("Financial Year")) {
                if (financialYearEndDateInDateFormat.isAfter(today)
                        || financialYearEndDateInDateFormat.isEqual(today)) {
                    leaveCycleEndDate = today.getYear() + "-" + "03" + "-" + "31";
                } else {
                    leaveCycleEndDate = nextYear + "-" + "03" + "-" + "31";
                }
            }
            if (leaveCycle.equalsIgnoreCase("custom leave cycle")) {
                if (customLeaveCycleEndDateInDateFormat.isAfter(LocalDate.parse(DateTimeHelper.getCurrentLocalDate()))) {
                    leaveCycleEndDate = nextYear + "-" + month + "-" + days;
                    leaveCycleEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(1).toString();

                } else {
                    leaveCycleEndDate = today.getYear() + "-" + month + "-" + days;
                    leaveCycleEndDate = LocalDate.parse(leaveCycleEndDate).minusMonths(1).toString();


                }
            }else if (leaveCycle.equalsIgnoreCase("Calendar Year")) {
                leaveCycleEndDate = today.getYear() + "-" + "12" + "-" + "31";
            }
            return leaveCycleEndDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    protected LocalDate getServerOrLocalDate()
    {
        if(serverChangedDate!=null){
            return LocalDate.parse(serverChangedDate);
        }
        else
            return LocalDate.now();
    }


    /**
     * This method gets employees deActivation leave balance shown in front end
     *
     * @param leaveType
     * @param deactivationDate
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndDeactivationLeaveBalance(String leaveType, String deactivationDate) {
        try {
            double actualLeaveBalance = 0;
            if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("No")) {
                String applicationURL = data.get("@@url");
                String URL=null;
                if(employee!=null)
                URL = applicationURL + "/emailtemplate/Employeeleaved?id=" + employee.getEmployeeID() + "&leave=" + leaveType
                        + "&date=" + deactivationDate;
                else
                    URL = applicationURL + "/emailtemplate/Employeeleaved?id=" + EmployeeId + "&leave=" + leaveType
                            + "&date=" + deactivationDate;
                driver.navigate().to(URL);
                String frontEndLeaveBalance = driver.findElement(By.xpath("//body")).getText();
                if (frontEndLeaveBalance.isEmpty()) {
                    Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                    throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
                }
                actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            } else if (UtilityHelper.getProperty("config", "Work.with.APIs").equalsIgnoreCase("Yes")) {
                actualLeaveBalance = getEmployeesFrontEndDeactivationLeaveBalanceUsingAPIs(leaveType, deactivationDate);
            }
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public double getMonthDiffFromFirstDayOfQuarter(String date) {
        try {
            Period age=Period.between(leaveCycleStartDate,LocalDate.parse(date));
            return age.getMonths()%3;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    /**
     * This method gets employees leave balance shown in front end
     *
     * @param leaveType
     * @param deactivationDate
     * @return Leave Balance
     * @author shikhar
     */
    public double getEmployeesFrontEndDeactivationLeaveBalanceUsingAPIs(String leaveType, String deactivationDate) {
        try {
            String applicationURL = data.get("@@url");
            String URL = applicationURL + "Mobileapi/Employeeleaved";
            RestAssured.baseURI = URL;

            RequestSpecification request = RestAssured.given();

            JSONObject params = new JSONObject();
            //params.put("token", authToken);
            params.put("id", employee.getEmployeeID());
            params.put("leave", leaveType);
            params.put("date", deactivationDate);

            request.body(params.toString());

            Response response = request.post();

            String frontEndLeaveBalance = response.body().asString().trim();
            if (frontEndLeaveBalance.isEmpty()) {
                Reporter("Front End Leave balance is empty/ May be Leave Type is deleted", "Error");
                throw new RuntimeException("Front End Leave balance is empty/ May be Leave Type is deleted");
            }
            double actualLeaveBalance = Double.valueOf(frontEndLeaveBalance);
            return actualLeaveBalance;
        } catch (Exception e) {
            Reporter("Exception while getting front end leave balance for the employee", "Error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * This method will return last Day of Quarter in Local Date format
     *
     * @param DATEIN_YYYY_MM_DD_format
     * @return Local Date
     */
    public LocalDate getLastDayOfQuarter(String DATEIN_YYYY_MM_DD_format) {


        int monthFar=LocalDate.parse(DATEIN_YYYY_MM_DD_format).minusMonths(leaveCycleStartDate.getMonthValue()).getMonthValue();
        if(monthFar<3)
            return leaveCycleStartDate.plusMonths(3).minusDays(1);

        if(monthFar>3 && monthFar<6)
            return leaveCycleStartDate.plusMonths(6).minusDays(1);



        if(monthFar>6 && monthFar<9)
            return leaveCycleStartDate.plusMonths(9).minusDays(1);


        if(monthFar>9 && monthFar<12)
            return leaveCycleStartDate.plusMonths(12).minusDays(1);
       /* LocalDate.parse("2019-10-30").minusMonths(LocalDate.parse("2018-11-01").getMonthValue()).getMonthValue()
        String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
        int year = Integer.parseInt(arr[0]);

        int DOJQuarter = LocalDate.parse(DATEIN_YYYY_MM_DD_format).get(IsoFields.QUARTER_OF_YEAR);
        int quarterMonth = 0;

        if (DOJQuarter == 1) {
            quarterMonth = 3;
        } else if (DOJQuarter == 2) {
            quarterMonth = 6;
        } else if (DOJQuarter == 3) {
            quarterMonth = 9;
        } else if (DOJQuarter == 4) {
            quarterMonth = 12;
        }*/ else {
            throw new RuntimeException("DOJ is not correct");
        }

       // LocalDate lastDate = LocalDate.of(year, quarterMonth, 01).with(lastDayOfMonth());
       // return lastDate;
    }






}