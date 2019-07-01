/*
package com.darwinbox.leaves.Application;

import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.LeaveAdmin;
import Service.LeaveService;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.leaves.Utils.LeaveBase;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.Map;

public class LeaveApplicationLimitations extends LeaveBase {

    LeaveService leaveService;
    LeavePolicyObject leavePolicyObject;
    LeaveAdmin leaveAdmin;
    LeavesPage leavePage;
    MapUtils mapUtils;
    Map<String,String> defaultBody;
    Map<String,String> mandatoryFields;

    LoginPage loginpage;
    CommonAction commonAction;
    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects(){

        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);


        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");
        leaveService=new LeaveService();
        leavePolicyObject =new LeavePolicyObject();
        mapUtils=new MapUtils();
        leaveAdmin=new LeaveAdmin();
        leavePage = PageFactory.initElements(driver, LeavesPage.class);

        defaultBody=leaveService.getDefaultforLeaveDeduction();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verify_LeaveApplication(Map<String, String> data) throws Exception {
        String field = data.get("Field");
        leavePolicyObject =new LeavePolicyObject();
        mandatoryFields=leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);


        if (field.equalsIgnoreCase("MaximumLeaveApplicationsAllowedPerMonth")) {
            mandatoryFields.put("Leaves[maximum_leave_application]", "1");
            mandatoryFields.put("Leaves[max_leave_application_month]", data.get("Maximum_leave_applications_allowed_per_month"));
            defaultBody.putAll(mandatoryFields);

            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);
            maximumLeaveApplicationsAllowedPerMonth(leavePolicyObject);
        }


        if (field.equalsIgnoreCase("MaximumLeaveApplicationsAllowedPerYear")) {
            mandatoryFields.put("Leaves[maximum_leave_application]", "1");
            mandatoryFields.put("Leaves[max_leave_application_year]", data.get("Maximum_leave_applications_allowed_per_year"));
            defaultBody.putAll(mandatoryFields);

            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);
            maximumLeaveApplicationsAllowedPerYear(leavePolicyObject);
        }

        if (field.equalsIgnoreCase("Maximumleaveapplicationsallowedintenure")) {
            mandatoryFields.put("Leaves[maximum_leave_application]", "1");
            mandatoryFields.put("Leaves[max_leave_application_tenure]", data.get("Maximum_leave_applications_allowed_in_tenure"));
            defaultBody.putAll(mandatoryFields);

            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);
            maxLeaveApplicationAllowedInTenure(leavePolicyObject);
        }

        if (field.equalsIgnoreCase("MinimumNoticeDays")) {
            mandatoryFields.put("Leaves[pre_approved_no_of_days]", data.get("Minimum_Notice_Period_(days)"));
            defaultBody.putAll(mandatoryFields);
            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);

            leavePage.navigateToLeavePage();
            leavePage.clickOnApplyForLeaveButton();
            Thread.sleep(2000);
            leavePage.clickOnApplyForOthers();
            Thread.sleep(2000);
            leavePage.searchEmployee(UtilityHelper.getProperty("config", "Employee.id"));
            Thread.sleep(2000);
            leavePage.selectLeaveType(leavePolicyObject.getLeave_Type());


            for (int i = 1; i <= 2; i++) {
                leavePage.setFromAndToDatesWithoutProperty(i, LocalDate.now());

                if (leavePolicyObject.minimumNoticeDays(leavePage.workingDays[i - 1])) {
                    if (leavePage.checkIfDateisEnabled(new DateTimeHelper().changeDateFormatForTable(leavePage.workingDays[i - 1])) == true) {
                        Reporter("Passed -- Date is enabled for the user, Leave Date=" + leavePage.workingDays[i - 1].toString() +
                                "     Minimum Notice Days=" + leavePolicyObject.getMinimum_Notice_Period(), "PASS");
                    } else
                        Reporter("Failed -- Date is disabled for the user, Leave Date=" + leavePage.workingDays[i - 1].toString() +
                                "     Minimum Notice Days=" + leavePolicyObject.getMinimum_Notice_Period(), "Fail");
                } else {
                    if (leavePage.checkIfDateisEnabled(new DateTimeHelper().changeDateFormatForTable(leavePage.workingDays[i - 1])) == false) {
                        Reporter("Passed -- Date is diabled for the user, Leave Date=" + leavePage.workingDays[i - 1].toString() +
                                "     Minimum Notice Days=" + leavePolicyObject.getMinimum_Notice_Period(), "PASS");
                    } else
                        Reporter("Failed -- Date is enabled for the user, Leave Date=" + leavePage.workingDays[i - 1].toString() +
                                "     Minimum Notice Days=" + leavePolicyObject.getMinimum_Notice_Period(), "Fail");
                }
            }

        }
    }
    @AfterMethod
    public void reset(){
        new DateTimeHelper().changeServerDate(driver, LocalDate.now().toString());
    }

}
*/
