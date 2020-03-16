package com.darwinbox.leaves.Application;

import com.darwinbox.Services;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Holiday;

import com.darwinbox.attendance.services.settings.HolidayService;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.EmployeeServices;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveBalanceAPI;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.json.JSONObject;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class OverUtilization_Hourly extends LeaveBase {
   // static List<LeavePolicyObject> overUtilizationPolicies = null;
    static List<LeavePolicyObject> policies;
    EmployeeServices employeeServices;
    LeaveService leaveService;
    // LeavePolicyObject leavePolicyObject;
    LeaveAdmin leaveAdmin;
    MapUtils mapUtils;
    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;
    LeavesPage leavesPage;
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

        leaveService = new LeaveService();


        mapUtils = new MapUtils();
        leaveAdmin = new LeaveAdmin();
        if (policies == null)
            policies = getOverUtilizationPoliciesForHourly();


        leavesPage = PageFactory.initElements(driver, LeavesPage.class);
        employeeServices = new EmployeeServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyOverUtilizationHourly(Map<String, String> data) {
        // LeavePolicyObject leavePolicyObject = getLeavePolicyObject(data.get("LeavePolicyObject"));
        deleteAllHolidays();

        LeavePolicyObject leavePolicyObject = policies.stream().filter(x -> x.getLeave_Type().equalsIgnoreCase(data.get("LeavePolicy"))).findFirst().get();

        Reporter("Leave Policy Name is   :"+leavePolicyObject.getLeave_Type(),"Info");
        List<LeavePolicyObject> leavePolicyObjectObjects = new ArrayList<>();
        List<Map<String, Object>> expectedLeaveDetails = null;
        List<Map<String, Object>> actualLeaveDeatails = new ArrayList<>();
        List<LocalDate> workingDays = new CopyOnWriteArrayList<>();
        Boolean halfDay = false;
        Boolean pastDatedLleave=false;
        int cascadingCount = 3;


        LocalDate today=LocalDate.now();
        LocalDate fromDate = LocalDate.parse(data.get("from_Date"));

        //1. change this such that it wont go for carry forward or leave cycle end date
        LocalDate leaveStartDate= fromDate;
        //leavesPage.
        if(ChronoUnit.DAYS.between(fromDate,leaveStartDate) < 0)
            pastDatedLleave= true;

        LocalDate toDate = LocalDate.parse(data.get("to_Date"));


        int noOfLeaves = (int)ChronoUnit.DAYS.between(fromDate, toDate)+1;
        int noOfHours =  Integer.parseInt(data.get("noOfHours"));

        Reporter("Number off Hours Applied is "+noOfHours,"Info");
       /* if (diff == 0 || data.get("noOfLeaves").contains("0.5")) {

            noOfLeaves = Double.parseDouble(data.get("noOfLeaves"));
        } else
            noOfLeaves = diff + 1;*/

       //noOfHours=Double.parseDouble(data.get("noOfHours"));

      /*  if (noOfLeaves == 0.5)
            halfDay = true;*/
        Boolean indicator = true;
        //LeavesPage leavesPage = new LeavesPage(driver);
        String weeklyOff = null;


        if (!halfDay) {
            leavesPage.setFromAndToDatesWithoutProperty(noOfLeaves,leaveStartDate);
            workingDays.addAll(Arrays.asList(leavesPage.workingDays));
            Reporter("From Date is   :"+leavesPage.workingDays[0].toString(),"Info");
            Reporter("To Date is   :"+leavesPage.workingDays[leavesPage.workingDays.length-1].toString(),"Info");
        } else if (halfDay) {
            // workingDays= new ArrayList<>();
            workingDays.add(leaveStartDate);
            //   workingDays = new LocalDate[]{LocalDate.now()};
        }

        List<LocalDate> holiday = null;
        if (data.get("holiday") != null && !data.get("holiday").contains("")) {
            holiday = new ArrayList<>();
            for (String holiayDate : data.get("holiday").split(",")) {
                holiday.add(LocalDate.parse(holiayDate));
            }

            for (LocalDate createHoliday : holiday) {
                Holiday holidayObject = new Holiday();
                holidayObject.setDate(createHoliday.toString());
                holidayObject.setName("AutomationCreatedHoliday On" + createHoliday);
                holidayObject.setOptional(false);

                new HolidayService().createHoliday(holidayObject);
            }

            //additional check to see holidays are present in working days
            for (LocalDate checkHolidayInWorkingDays : holiday) {
                if (workingDays.stream().filter(x -> x.toString().contains(checkHolidayInWorkingDays.toString())).findFirst().isPresent()) {
                    if (!leavePolicyObject.getCount_intervening_holidays_weeklys_offs_as_leave().CountHolidays)
                        noOfLeaves = noOfLeaves - 1;
                }

                new SoftAssert().assertEquals(noOfLeaves,data.get("noOfLeaves"));



            }
        }


        // noOfLeaves = Double.parseDouble(data.get("noOfLeaves"));


        if (holiday != null) {
            for (LocalDate removeHolidayFromWorkingDays : holiday) {
                //workingDays.
            }
        }


        employee = employeeServices.generateAnEmployee("no", "Working Days (DO NOT TOUCH)", "random", "no");


        //2. change this such that it wont go for carry forward or leave cycle end date beyond
        //new DateTimeHelper().changeServerDate(driver,LocalDate.now().minusDays(30).toString());

        new DateTimeHelper().changeServerDate(driver,LocalDate.now().toString());
        if (data.get("emp_WeeklyOff") != null & !data.get("emp_WeeklyOff").equalsIgnoreCase("")) {
            weeklyOff = data.get("emp_WeeklyOff");
            new Services().createWeeklyOff(weeklyOff);
            String weeklyOffID = new Services().getWeeklyOFFlist().get(weeklyOff);
            employeeServices.createWeeklyOffForAnEmp(employee.getUserID(), weeklyOffID);
        }

        double employeeBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();

        //  expectedLeaveDetails = new LeaveService().calculateDuration(employee.getMongoID(), workingDays[0], workingDays[workingDays.length - 1], new LeaveService().getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId), halfDay);
        expectedLeaveDetails = new LeaveService().calculateDuration(employee.getMongoID(), leaveStartDate, workingDays.get(workingDays.size()-1), new LeaveService().getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId), halfDay);
        if (employeeBalance >= noOfLeaves) {
            leavePolicyObjectObjects.add(leavePolicyObject);
        } else {

            while (indicator) {
                leavePolicyObjectObjects.add(leavePolicyObject);

         /*   if (employeeBalance > noOfLeaves) {
                applyLeave(employee, leavePolicyObject, workingDays);

            } else {*/

                try {
                    Double response = verifyOverUtilization(employee, leavePolicyObject, noOfLeaves, leavePolicyObjectObjects);
                    Map<String, Object> leaveDetail = new HashMap<>();
                    leaveDetail.put("leave_name", leavePolicyObject.getLeave_Type());
                    leaveDetail.put("days", response);
                    List<LocalDate> leavePolicyDates = new CopyOnWriteArrayList<>();
                    leavePolicyDates.addAll(workingDays.subList(0, response.intValue()));
                    for (LocalDate date : leavePolicyDates) {
                        workingDays.removeIf(x -> x.toString().equalsIgnoreCase(date.toString()));
                    }
                    int weekOffs = 0;
                    Boolean weekOffIndicator = false;


                    if (!leavePolicyObject.getCount_intervening_holidays_weeklys_offs_as_leave().CountWeeklyOffs) {

                        String wOff = weeklyOff;
                        weekOffs = (int) leavePolicyDates.stream().filter(x -> x.getDayOfWeek().toString().equalsIgnoreCase(wOff)).count();

                        noOfLeaves = noOfLeaves - response.intValue() - weekOffs;

                        if (leavePolicyDates.stream().filter(x -> x.getDayOfWeek().toString().equalsIgnoreCase(wOff)).findAny().isPresent()) {

                            while (weekOffs != 0) {
                                if (!workingDays.get(0).getDayOfWeek().toString().equalsIgnoreCase(wOff)) {
                                    leavePolicyDates.add(workingDays.get(0));
                                    workingDays.remove(0);
                                    weekOffs = weekOffs - 1;
                                } else {
                                    leavePolicyDates.add(workingDays.get(0));
                                    workingDays.remove(0);
                                }
                            }
                               /*leavePolicyDates = workingDays.subList(0,
                                       response.intValue() + weekOffs);*/


                        }

                    } else {
                        noOfLeaves = noOfLeaves - response.intValue();
                    }

                   /* for(LocalDate date : leavePolicyDates){
                        workingDays.removeIf(x->x.toString().equalsIgnoreCase(date.toString()));
                    }*/
                    if (response.intValue() != 0) {
                        leaveDetail.put("from_date", leavePolicyDates.get(0));
                        leaveDetail.put("to_date", leavePolicyDates.get(leavePolicyDates.size() - 1));
                    }
                    else if(response.intValue()==0){
                        leaveDetail.put("from_date", workingDays.get(0));
                        leaveDetail.put("to_date", workingDays.get(0));
                    }
                    actualLeaveDeatails.add(leaveDetail);

                } catch (Exception e) {
                    if (leavePolicyObjectObjects.size() == 1) {
                        throw new RuntimeException(e.toString());
                    } else if (leavePolicyObjectObjects.size() > 1) {

                        Map<String, Object> leaveDetail = new HashMap<>();
                        leaveDetail.put("leave_name", leavePolicyObject.getLeave_Type());
                        leaveDetail.put("days", 0);
                        leaveDetail.put("from_date", workingDays.get(0));
                        leaveDetail.put("to_date", workingDays.get(0));
                        actualLeaveDeatails.add(leaveDetail);
                        noOfLeaves = noOfLeaves - 0;
                    }
                }
                if (leavePolicyObject.getOverUtilization().utilizeFrom) {
                    String utilizeFrom = leavePolicyObject.getOverUtilization().utlizeFromDropDown;
                    leavePolicyObject = policies.stream().filter(x -> x.getLeave_Type().equalsIgnoreCase(utilizeFrom)).findFirst().get();

                } else if (!leavePolicyObject.getOverUtilization().utilizeFrom) {

                    indicator = false;

                }
            }

            if (leavePolicyObject.getOverUtilization().indicator) {


                if (leavePolicyObject.getOverUtilization().countExcessAsPaid) {
                    if (!leavePolicyObject.getCount_intervening_holidays_weeklys_offs_as_leave().CountWeeklyOffs) {
                        String woFF = weeklyOff;//jst for predicate below
                        noOfLeaves = noOfLeaves - (int) workingDays.stream().filter(x -> x.getDayOfWeek().toString().equalsIgnoreCase(woFF)).count();

                    }
                    if ((leavePolicyObjectObjects.size() == 1)) {
                        String leaveType = leavePolicyObject.getLeave_Type();
                        Map<String, Object> replaceLeaveDetail = actualLeaveDeatails.stream().filter(x -> x.get("leave_name").toString().equalsIgnoreCase(leaveType)).findFirst().get();
                        double replaceDays = (double) replaceLeaveDetail.get("days") + noOfLeaves;
                        replaceLeaveDetail.put("days", replaceDays);


                        replaceLeaveDetail.put("to_date", workingDays.get(workingDays.size() - 1));
                        actualLeaveDeatails.stream().filter(x -> x.get("leave_name").toString().equalsIgnoreCase(leaveType)).findFirst().get().putAll(replaceLeaveDetail);

                    } else if ((leavePolicyObjectObjects.size() > 1)) {
                        if ((checkClubbingCompatability(leavePolicyObjectObjects.get(leavePolicyObjectObjects.size() - 2), leavePolicyObjectObjects.get(leavePolicyObjectObjects.size() - 1)))) {
                            String leaveType = leavePolicyObject.getLeave_Type();
                            Map<String, Object> replaceLeaveDetail = actualLeaveDeatails.stream().filter(x -> x.get("leave_name").toString().equalsIgnoreCase(leaveType)).findFirst().get();
                            double replaceDays = (double) replaceLeaveDetail.get("days") + noOfLeaves;
                            replaceLeaveDetail.put("days", replaceDays);



                            replaceLeaveDetail.put("to_date", workingDays.get(workingDays.size() - 1));

                            actualLeaveDeatails.stream().filter(x -> x.get("leave_name").toString().equalsIgnoreCase(leaveType)).findFirst().get().putAll(replaceLeaveDetail);

                        }
                        if (!checkClubbingCompatability(leavePolicyObjectObjects.get(leavePolicyObjectObjects.size() - 2), leavePolicyObjectObjects.get(leavePolicyObjectObjects.size() - 1))) {
                            Map<String, Object> unpaid = new HashMap<>();
                            unpaid.put("leave_name", "unpaid");
                            unpaid.put("days", noOfLeaves);
                            unpaid.put("from_date", workingDays.get(0));
                            unpaid.put("to_date", workingDays.get(workingDays.size() - 1));
                            actualLeaveDeatails.add(unpaid);
                        }
                    }
                }
                if (leavePolicyObject.getOverUtilization().countExcessAsUnPaid) {
                    Map<String, Object> unpaid = new HashMap<>();
                    unpaid.put("leave_name", "unpaid");
                    unpaid.put("days", noOfLeaves);
                    unpaid.put("from_date", workingDays.get(0));
                    unpaid.put("to_date", workingDays.get(workingDays.size() - 1));
                    actualLeaveDeatails.add(unpaid);
                }

                Reporter("---------Leave Distribution Information-----", "Info");
                Reporter("---------Expected Leave Details----------", "Info");

                for (Map<String, Object> expectedLeaveDetail : expectedLeaveDetails) {

                    Reporter("-----Leave Name is----------------->" + expectedLeaveDetail.get("leave_name"), "Info");
                    Reporter("-----Leave Days is----------------->" + expectedLeaveDetail.get("days"), "Info");
                    Reporter("-----From Date is----------------->" + expectedLeaveDetail.get("from_date"), "Info");
                    Reporter("-----To Date is----------------->" + expectedLeaveDetail.get("to_date"), "Info");


                }

                Reporter("---------Actual Leave Details----------", "Info");

                for (Map<String, Object> actualLeaveDetail : actualLeaveDeatails) {
                    Reporter("-----Leave Name is----------------->" + actualLeaveDetail.get("leave_name"), "Info");
                    Reporter("-----Leave Days is----------------->" + actualLeaveDetail.get("days"), "Info");
                    Reporter("-----From Date is----------------->" + actualLeaveDetail.get("from_date"), "Info");
                    Reporter("-----To Date is----------------->" + actualLeaveDetail.get("to_date"), "Info");

                }


                Assert.assertTrue(expectedLeaveDetails.size() == actualLeaveDeatails.size(),
                        "Leave Details Are Not Equal In Number  ExpectedLeave Details Size is  " + expectedLeaveDetails.size() +
                                "ActualLeave Detail Size is" + actualLeaveDeatails.size());

                for (int i = 0; i < expectedLeaveDetails.size(); i++) {
                    Assert.assertTrue(expectedLeaveDetails.get(i).get("leave_name").toString().equalsIgnoreCase(actualLeaveDeatails.get(i).get("leave_name").toString()),
                            "Leave Names Are not Same ExpectedLeaveName is " + expectedLeaveDetails.get(i).get("leave_name") +
                                    "ActualLeaveName is" + actualLeaveDeatails.get(i).get("leave_name"));


                    Assert.assertTrue((Double.parseDouble(expectedLeaveDetails.get(i).get("days").toString()) == Double.parseDouble(actualLeaveDeatails.get(i).get("days").toString())),
                            "Leave Days Are not Same ExpectedLeaveName is " + expectedLeaveDetails.get(i).get("days") +
                                    "ActualLeaveName is" + actualLeaveDeatails.get(i).get("days"));
                }

            }
        }
        Reporter("-----Leave Apply Information--------", "Info");
        String applyLeaveResponse=null;
        /*if(pastDatedLleave){
            try {
                leavesPage.navigateToLeavePage();
                leavesPage.clickOnApplyForLeaveButton();
                Thread.sleep(2000);
                leavesPage.clickOnApplyForOthers();
                Thread.sleep(2000);
                leavesPage.searchEmployee(employee.getEmployeeID());
                Thread.sleep(2000);
                leavesPage.selectLeaveType(leavePolicyObjectObjects.get(0).getLeave_Type());
                if(leavesPage.checkIfDateisEnabled(new DateTimeHelper().changeDateFormatForTable(leaveStartDate))){
                    applyLeaveResponse = applyLeave(employee, leavePolicyObjectObjects.get(0), leaveStartDate, workingDays.get(workingDays.size() - 1));
                }
                if(!leavesPage.checkIfDateisEnabled(new DateTimeHelper().changeDateFormatForTable(leaveStartDate))){
                    applyLeaveResponse="Past Dated Leave is not Allowed";
                }

            } catch (Exception e){
                Reporter("Error in Past Dated Leave Verification from UI","fail");
            }
        }*/

      //  else {
            applyLeaveResponse = new LeaveAdmin().ApplyLeaveHourly(employee.getMongoID(),leaveStartDate.toString(), workingDays.get(workingDays.size() - 1).toString(),leaveService.getLeaveID(leavePolicyObject.getLeave_Type(), leavePolicyObject.groupCompanyMongoId),noOfHours);
      //  }
        Reporter("Response" + applyLeaveResponse, "Info");

        Boolean errrorFlag = false;
        int actualErrorCount = 0;

        //applicability checks

        try{
            verifyLeavesForPastDatedApplication(leavePolicyObjectObjects.get(0),employeeBalance,pastDatedLleave,leaveStartDate);
        }catch(Exception e){
            errrorFlag = true;
            actualErrorCount++;
            if(applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:","").trim())){
                Reporter(e.toString()   +"   Error Message is Verified","Info");
        }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }

        try {
            verifyFixedOverUtilization(leavePolicyObjectObjects.get(0), employeeBalance, noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }
        try {
            verifyLeavesForConsecutiveLeaveAllowedField(leavePolicyObjectObjects.get(0), noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }


        try {
            verifyMinimumConsecutiveDaysAllowedInSingleApplication(leavePolicyObjectObjects.get(0), noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }

        try {
            verifyLeavesForMaxConsecutiveAllowedPerSingleApplicationField(leavePolicyObjectObjects.get(0), noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }

        try {
            verifyMinimumHoursToBeAppliedInASingleDay(leavePolicyObjectObjects.get(0), noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;
            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {

                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }


        try {
            verifyLeavesForMaxHoursAllowedInASingleDay(leavePolicyObjectObjects.get(0), noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }


        try {
            verifyLeavesForDontAllowMoreThanYearlyAccural(leavePolicyObjectObjects.get(0), employee, noOfLeaves*noOfHours);
        } catch (Exception e) {

            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }

        try {
            verifyLeavesForDontAllowMoreThanYearlyAllocation(leavePolicyObjectObjects.get(0), noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }


        try {
            verifyLeavesForFixedOverUtilization(leavePolicyObjectObjects.get(0), employeeBalance, noOfLeaves*noOfHours);
        } catch (Exception e) {
            errrorFlag = true;
            actualErrorCount++;

            if (applyLeaveResponse.contains(e.toString().replace("java.lang.Exception:", "").trim())) {
                Reporter(e.toString()   +"   Error Message is Verified","Info");
            }
            else{
                Assert.fail(e.toString()+  "    Not Found In Response");
            }
        }




        //max month


        if (!errrorFlag) {
            Assert.assertTrue(applyLeaveResponse.contains("success"), "Expected Successfull Leave Response But Found "
                    + applyLeaveResponse);


            employeeBalance =new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();

            Double expectedBalance = Double.parseDouble(leavePolicyObject.getMaximum_leave_allowed_per_year()-noOfLeaves*noOfHours+"");
            Reporter("Expected Employee Balance  is -->  "+expectedBalance,"Info");
            Reporter("Actual Employee Balance  is -->  "+employeeBalance,"Info");

            Assert.assertTrue(expectedBalance==employeeBalance,"Expected Balance and Actual Balance are not same");

        }
        if (errrorFlag) {

            Assert.assertTrue(applyLeaveResponse.contains("failure"), "Expected failure Leave Response But Found "
                    + applyLeaveResponse);


            JSONObject response = new JSONObject(applyLeaveResponse);
            String[] expectedErrorss = response.get("error").toString().split("<br/>");

            Assert.assertTrue(actualErrorCount == expectedErrorss.length, "Expected Number Of Errors And Actual Errorss in Apply leave Response Doesnt Match");


        }





    }
}