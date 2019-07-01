package com.darwinbox.leaves.Application;

import Objects.Employee;
import Objects.Holiday;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.*;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

public class CountIntervening extends LeaveBase {


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


        leavePage = PageFactory.initElements(driver, LeavesPage.class);
        employeeServices = new EmployeeServices();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void countIntervening(Map<String,String> data){

        String weeklyOff=data.get("WeeklyOff");
        String holiday=data.get("Holiday");
        String holidayDate=null;
        String WeeklyOffDate=null;
        String weeklyOffAndHoliday=data.get("");

        boolean indicator=(Boolean.parseBoolean(data.get("Count Intervening")));
        boolean countWeeklyOFF=(Boolean.parseBoolean(data.get("Count WeeklyOFF")));
        boolean countHoliday
                =(Boolean.parseBoolean(data.get("Count Holiday")));


        LeavePolicyObject leavePolicyObject =new LeavePolicyObject("Auto_CountIntervening");
        mandatoryFields=new LeaveService().mandatoryFieldsToCreateLeave(leavePolicyObject);
        Map<String,String> request=getRequestForCountIntervening(indicator,countWeeklyOFF,countHoliday);
        mandatoryFields.putAll(request);

        new LeaveService().createLeaveForPolicy(mandatoryFields, leavePolicyObject);
        Reporter("Count Intervening Settings are     Count Intervening "+indicator    +"   Count Weekly off    "+countWeeklyOFF
                +"   Count Holiday  "+countHoliday  +"","Info");


        new Service().createWeeklyOff(weeklyOff);
        String weeklyOffID=new Service().getWeeklyOFFlist().get(weeklyOff);

            Holiday holidayObject = new Holiday();
        for(int i=0;i<7;i++){
            if(LocalDate.now().plusDays(i).getDayOfWeek().toString().equalsIgnoreCase(holiday)){
                holidayObject.setDate(LocalDate.now().plusDays(i).toString());
                holidayDate=LocalDate.now().plusDays(i).toString();
                break;
            }
        }



         holidayObject.setName("AutomationCreatedHoliday On"+holidayDate);
        holidayObject.setOptional(false);

        new HolidayService().createHoliday(holidayObject);


       Reporter("    Weekly OFF Created On   "+weeklyOff,"Info");
       Reporter("    Holiday Created On       "+holiday,"Info");


        employee=employeeServices.generateAnEmployee("no", leavePolicyObject.getGroup_Company(),"random","no");
        employeeServices.createWeeklyOffForAnEmp(employee.getUserID(), weeklyOffID);

        double defaultBalance=new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
        double presentBalance=defaultBalance;
        Reporter("Employee Default Leave Balance is  "+defaultBalance,"Info");

        if(weeklyOff==holiday){
            if(countHoliday  || !countHoliday&&countWeeklyOFF){
                Reporter(" Employee Applying Leave On " + weeklyOff, "Info");
                String responseOnWeeklyOff = applyLeave(employee, leavePolicyObject, weeklyOff);
                Reporter("Leave Response On WeeklyOff " + responseOnWeeklyOff, "Info");
            }
            if(countWeeklyOFF){
                Reporter(" Employee Applying Leave On " + holiday, "Info");
                String responseOnHoliday = applyLeave(employee, leavePolicyObject, holiday);
                Reporter("Leave Response On Holiday " + responseOnHoliday, "Info");
            }
            presentBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();

        }
        else if(weeklyOff!=holiday || countHoliday&&countWeeklyOFF) {
            Reporter(" Employee Applying Leave On " + weeklyOff, "Info");
            String responseOnWeeklyOff = applyLeave(employee, leavePolicyObject, weeklyOff);
            Reporter("Leave Response On WeeklyOff " + responseOnWeeklyOff, "Info");

            presentBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
            Reporter("Employee Leave Balance After Applying Leave On  " + weeklyOff + " is  " + presentBalance, "Info");

            Reporter(" Employee Applying Leave On " + holiday, "Info");
            String responseOnHoliday = applyLeave(employee, leavePolicyObject, holiday);
            Reporter("Leave Response On Holiday " + responseOnHoliday, "Info");
            presentBalance = new LeaveBalanceAPI(employee.getEmployeeID(), leavePolicyObject.getLeave_Type()).getBalance();
            Reporter("Employee Leave Balance After Applying Leave On  " + holiday + " is  " + presentBalance, "Info");
        }
        if(!indicator){
            Assert.assertTrue(defaultBalance==presentBalance,"Incorrect Balance when indicator is false");
          //  Assert.assertTrue(defaultBalance==presentBalance,"Incorrect Balance After For Holiday");
        }
        else if (indicator){

            if((countWeeklyOFF && !countHoliday) || (countHoliday && !countWeeklyOFF))
            {
                if(holiday==weeklyOff)
                    Assert.assertTrue(defaultBalance==presentBalance,"Incorrect Balance when holiday/weeklyoff is checked true");

                else
                Assert.assertTrue(defaultBalance-1==presentBalance,"Incorrect Balance when holiday/weeklyoff is checked true");
            }


            if(countHoliday && countWeeklyOFF) {
                if(weeklyOff==holiday)
                    Assert.assertTrue(defaultBalance - 1 == presentBalance, "Incorrect Balance when holiday and weeklyoff are checked TRUE");
                else
                Assert.assertTrue(defaultBalance - 2 == presentBalance, "Incorrect Balance when holiday and weeklyoff are checked TRUE");
            }
        }
    }
}
