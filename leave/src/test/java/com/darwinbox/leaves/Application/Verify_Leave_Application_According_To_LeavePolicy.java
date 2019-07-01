/*
package com.darwinbox.leaves.Application;

import Objects.Employee;
import Objects.LeavePolicyObject.LeavePolicyObject;
import Service.EmployeeServices;
import Service.LeaveAdmin;
import Service.LeaveService;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.leaves.actionClasses.LeaveApplicationHelper;
import com.darwinbox.leaves.actionClasses.LeaveApplicationHelperBackupObject;
import com.darwinbox.leaves.actionClasses.LeavesAction;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesSettingsPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Verify_Leave_Application_According_To_LeavePolicy extends TestBase {

    private static final Logger log = Logger.getLogger(Verify_Leave_Application_According_To_LeavePolicy.class);
    LeavesAction leavesAction;
    LeavesSettingsPage leaveSettings;
    LoginPage loginpage;
    CommonAction commonAction;
    LeavesPage leavePage;
    LeaveApplicationHelperBackupObject leaveApplicationHelperBackup;
    CreateLeavePolicy_ExcelMapper excelMapper;
    LeaveService leaveService;
    LeavePolicyObject leavePolicyObject;

    Map<String, String> defaultBody;
    Map<String, String> mandatoryFields;

    EmployeeServices employeeServices;
    Employee employee = null;
    LeaveApplicationHelper leaveApplicationHelper;


    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        leaveSettings = PageFactory.initElements(driver, LeavesSettingsPage.class);
        leavesAction = PageFactory.initElements(driver, LeavesAction.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);
        leavePage = PageFactory.initElements(driver, LeavesPage.class);

        loginpage.loginToApplication();
        commonAction.changeApplicationAccessMode("Admin");

        excelMapper = new CreateLeavePolicy_ExcelMapper();
        leaveApplicationHelperBackup = new LeaveApplicationHelperBackupObject();
        leaveService = new LeaveService();
        leavePolicyObject = new LeavePolicyObject();

        mandatoryFields = leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);
        defaultBody = leaveService.getDefaultforLeaveDeduction();


        employeeServices = new EmployeeServices();
        leaveApplicationHelper = new LeaveApplicationHelper();

    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verify_LeaveApplication(Map<String, String> data) throws Exception {
        String employeeProbation = UtilityHelper.getProperty("employeeConfig", "employeeProbation");

        String field = data.get("Field");

        //main fields
        CreateLeavePolicy_ExcelMapper.assignment_Type = data.get("Assignment_Type");
        CreateLeavePolicy_ExcelMapper.group_Company = data.get("Group_Company");
        CreateLeavePolicy_ExcelMapper.leave_Type = data.get("Leave_Type");
        CreateLeavePolicy_ExcelMapper.description = data.get("Description");
        CreateLeavePolicy_ExcelMapper.leave_cycle = data.get("Leave_cycle");
        CreateLeavePolicy_ExcelMapper.push_all_these_leave_requests_to_admin = data.get("Push_all_these_leave_requests_to_admin");
        CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_year = data.get("Maximum_leave_allowed_per_year");



        if (field.equalsIgnoreCase("consequtiveallowedandmaxmonth")) {
            CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_month = data.get("Maximum_leave_allowed_per_month");
            CreateLeavePolicy_ExcelMapper.consecutive_leave_allowed = data.get("Consecutive_leave_allowed");

            Assert.assertTrue(leaveService.createLeave(excelMapper.createLeaveRequestConsecutiveLeaveAllwdAndMaxMonth(), CreateLeavePolicy_ExcelMapper.leave_Type, CreateLeavePolicy_ExcelMapper.groupCompanyMongoId),
                    "Error While Creating/Updating Leave Policy");
            Assert.assertTrue(leaveApplicationHelperBackup.verifyConsequtiveLeaveAllowedAndMaxLeaveAllowedPerMonth(), "Error in verifing Consecutive Leave Allowed And Maximum Leave Allowed");
        }

        if (field.equalsIgnoreCase("MaximumAllowedPerYearAndMaxAllowedPerMonth")) {
            CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_month = data.get("Maximum_leave_allowed_per_month");

            Assert.assertTrue(leaveService.createLeave(excelMapper.createRequestForMaxAllowedPerYearAndMaxAlldPerMnth(), CreateLeavePolicy_ExcelMapper.leave_Type, CreateLeavePolicy_ExcelMapper.groupCompanyMongoId),
                    "Error While Creating/Updating Leave Policy");
            Assert.assertTrue(leaveApplicationHelperBackup.verifyMaximumAllowedPerYearAndMaxAllowedPerMpnth(), "Error in Verifing Max Allowed Per Year And Max Month");
        }

        if (field.equalsIgnoreCase("ConsequtiveAllowedAndRestrictWeekdays")) {
            CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_month = data.get("Maximum_leave_allowed_per_month");
            CreateLeavePolicy_ExcelMapper.consecutive_leave_allowed = data.get("Consecutive_leave_allowed");

            Assert.assertTrue(leaveService.createLeave(excelMapper.createRequestForConsecutiveLeaveAllowedAndRestrictDays(), CreateLeavePolicy_ExcelMapper.leave_Type, CreateLeavePolicy_ExcelMapper.groupCompanyMongoId),
                    "Error While Creating/Updating Leave Policy");
            Assert.assertTrue(leaveApplicationHelperBackup.verifyConsequitiveDaysAllowedAndRestrictDays(), "Error in verifing Consecutive Leave Allowed And Restrict Days");

        }

        if (field.equalsIgnoreCase("ConsequtiveAllowdWithMax/MinAllowedInSingleApplication")) {
            CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_month = data.get("Maximum_leave_allowed_per_month");
            CreateLeavePolicy_ExcelMapper.consecutive_leave_allowed = data.get("Consecutive_leave_allowed");
            CreateLeavePolicy_ExcelMapper.minimum_consecutive_days_allowed_in_a_single_application = data.get("Minimum_consecutive_days_allowed_in_a_single_application");
            CreateLeavePolicy_ExcelMapper.maximum_consecutive_days_allowed_in_a_single_application = data.get("Maximum_consecutive_days_allowed_in_a_single_application");

            Assert.assertTrue(leaveService.createLeave(excelMapper.createRequestForConsecutiveLeaveAllowedWithMaxMinCombiation(), CreateLeavePolicy_ExcelMapper.leave_Type, CreateLeavePolicy_ExcelMapper.groupCompanyMongoId),
                    "Error While Creating/Updating Leave Policy");
            Assert.assertTrue(leaveApplicationHelperBackup.verifyConsequtiveDaysAllowedAndMaxMinCombination(), "Error in verifing ConsecutiveLeaveAllowedWithMaxMinCombiation");
        }
        if (field.equalsIgnoreCase("ConsequtiveDaysAllowedInBetweenMonths")) {
            CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_month = data.get("Maximum_leave_allowed_per_month");
            CreateLeavePolicy_ExcelMapper.consecutive_leave_allowed = data.get("Consecutive_leave_allowed");

            Assert.assertTrue(leaveService.createLeave(excelMapper.creteRequestForConsecutiveAllowedInBetweenMonths(), CreateLeavePolicy_ExcelMapper.leave_Type, CreateLeavePolicy_ExcelMapper.groupCompanyMongoId),
                    "Error While Creating/Updating Leave Policy");
            Assert.assertTrue(leaveApplicationHelperBackup.verifyConsequtiveLeaveAllowedInBetweenMonths(),
                    "Error in verifing Consecutive Leave Allowed In Between Months");
        }

}
*/
