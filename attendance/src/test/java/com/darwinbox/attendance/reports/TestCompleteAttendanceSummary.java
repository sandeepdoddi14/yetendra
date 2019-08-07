package com.darwinbox.attendance.reports;

import com.darwinbox.attendance.AttendanceTestBase;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.leavedeductions.Absent;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.attendance.objects.policy.leavedeductions.WorkDuration;
import com.darwinbox.attendance.pages.settings.DisplaySettingsPage;
import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.attendance.services.settings.ReportsDashboardServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestCompleteAttendanceSummary extends TestBase {

    LoginPage loginPage;
    ReportsDashboardServices reportsDashboardServices;
    Reports reports;
    DateTimeHelper dateTimeHelper;
    EmployeeServices employeeServices;
    DisplaySettingsPage displaySettingsPage;
    Date date;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        reportsDashboardServices=new ReportsDashboardServices();
        reports=new Reports();
        dateTimeHelper = new DateTimeHelper();
        employeeServices = new EmployeeServices();
        displaySettingsPage = new DisplaySettingsPage(driver);
        date = new Date();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testCompleteCombination(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        AttendanceTestBase atb = AttendanceTestBase.getObject("ReportSettings.xlsx");
        AttendancePolicy policy = atb.getAttendancePolicy(testData.get("PolicyName"));
        Shift shift = atb.getShift(testData.get("Shift Name"));
        String weekoffId = atb.getWeeklyOff("All");

        List<Employee> emp=new ArrayList<>();
        List<String> d=new ArrayList<>();
        boolean isHoliday=true;

        for(int j=0;j<4;j++) {

            Employee employee = employeeServices.createAnEmployee(policy.getPolicyInfo().getCompanyID().length() == 0);

            atb.assignPolicyAndShift(employee.getUserID(), employee.getDoj(), shift, policy, weekoffId);
            Reporter("Employee created " + employee.getUserID(), "INFO");
            emp.add(employee);

             String ab=  employee.getUserID();
             d.add(ab);
        }

            String leaveToApply = testData.get("ApplyLeave");
            String leaveid = atb.getLeaveId(leaveToApply);
            int count = 1;

            date = dateTimeHelper.formatStringToDate("yyyy-MM-dd", emp.get(1).getDoj());
           Date date1 = dateTimeHelper.getFirstDateOfNextMonth(date);
           Date date2 = dateTimeHelper.getFirstDateOfNextMonth(date1);



        while (count <= 2) {

            for (LeaveDeductionsBase.DAYSTATUS day : LeaveDeductionsBase.DAYSTATUS.values()) {

                    boolean isboth = day.equals(LeaveDeductionsBase.DAYSTATUS.WH);
                             isHoliday = day.equals(LeaveDeductionsBase.DAYSTATUS.HOLIDAY) || isboth;
                    boolean isWeekoff = day.equals(LeaveDeductionsBase.DAYSTATUS.WEEKOFF) || isboth;

                date = dateTimeHelper.getFirstDateOfNextMonth(date);

            for(int k=0;k<4;k++) {


    atb.applyLeave(dateTimeHelper.addDays(date1, 1), emp.get(k), leaveid, false, false, true); //full day present
    atb.applyLeave(dateTimeHelper.addDays(date1, 2), emp.get(k), "unpaid", false, false, true);
    atb.applyLeave(dateTimeHelper.addDays(date1, 3), emp.get(k), leaveid, true, false, true);
    atb.applyLeave(dateTimeHelper.addDays(date1, 4), emp.get(k), "unpaid", true, false, true);
    atb.applyLeave(dateTimeHelper.addDays(date1, 5), emp.get(k), leaveid, true, false, true);
    atb.applyLeave(dateTimeHelper.addDays(date1, 5), emp.get(k), leaveid, false, true, true);
    atb.applyLeave(dateTimeHelper.addDays(date1, 6), emp.get(k), "unpaid", true, false, true);
    atb.applyLeave(dateTimeHelper.addDays(date1, 6), emp.get(k), leaveid, false, true, true);
   // Reporter(emp.get(k).getUserID() +" Applied Leave from: " + dateTimeHelper.addDays(date, 1) + " To " + dateTimeHelper.addDays(date, 6), "INFO");
                                  }

                    for (int i = 0; i < 7; i++) {
   //add for loop for rest of employees

                        if (isHoliday) {
                            atb.createHoliday(date1);
                          //  Reporter("created holiday for: " + date, "INFO");

                            WorkDuration wd = new WorkDuration();
                            wd.setWdhrs_fullday("4");
                            Map<String, String> body =wd.getWorkDuration(emp.get(1).getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift, date1, false, false, isWeekoff);
                            atb.importBackdated(body);
                            date1 = dateTimeHelper.getNextDate(date1);
                           // Reporter(emp.get(1).getUserID()+" Employee Present for date "+date,"INFO");
                        }
                        if (count == 2) {

                            Absent absent = new Absent();
                            Map<String, String> body = absent.getAbsent(emp.get(2).getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift.getShiftName(), date2, isWeekoff);
                            atb.importBackdated(body);
                            date2 = dateTimeHelper.getNextDate(date2);
                          //  Reporter(emp.get(2).getUserID()+" Employee absent for date "+date,"INFO");

                        } else {
                            WorkDuration wd = new WorkDuration();
                            wd.setWdhrs_fullday("4");
                            Map<String, String> body =wd.getWorkDuration(emp.get(3).getEmployeeID(), policy.getPolicyInfo().getPolicyName(), shift, date1, false, false, isWeekoff);
                            atb.importBackdated(body);
                            date1 = dateTimeHelper.getNextDate(date1);
                          //  Reporter(emp.get(3).getUserID()+" Employee Present for date "+date,"INFO");
                        }
                    }
                    count++;
                }

        }

            reports.setReportType("opt_roster_21");
            reports.setModule("opt_main_roster_1");
            reports.setFilter(d);
            reports.setSubCriteria(String.valueOf(Reports.employeeTypes.activeEmployees));
            reports.setFromDate(dateTimeHelper.formatDateTo(dateTimeHelper.addDays(new Date(), -90), "dd-MM-yyyy"));
            reports.setToDate(dateTimeHelper.formatDateTo(new Date(), "dd-MM-yyyy"));
            reportsDashboardServices.getReportCompleteAttendanceSummaryList(reports);
        }


    }

