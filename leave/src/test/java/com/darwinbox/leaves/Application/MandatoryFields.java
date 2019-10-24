package com.darwinbox.leaves.Application;


import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.actionClasses.LeaveApplicationHelperBackupObject;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

public class MandatoryFields extends TestBase {

    LeaveService leaveService;
    LeavePolicyObject leavePolicyObject;
    LeaveAdmin leaveAdmin;
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

        mapUtils=new MapUtils();
        leaveAdmin=new LeaveAdmin();

        defaultBody=leaveService.getDefaultforLeaveDeduction();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verify_LeaveApplication(Map<String, String> data) throws Exception {
        String field = data.get("Field");
        leavePolicyObject =new LeavePolicyObject();
        mandatoryFields=leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);


        if (field.equalsIgnoreCase("max leave allowed per year")) {
           /* //CreateLeavePolicy_ExcelMapper.maximum_leave_allowed_per_year = data.get("Maximum_leave_allowed_per_year");
            Assert.assertTrue(leaveService.createLeave(excelMapper.createRequestForMaxLeaveAllowedPerYear(), CreateLeavePolicy_ExcelMapper.leave_Type, CreateLeavePolicy_ExcelMapper.groupCompanyMongoId),
                    "Error While Creating/Updating Leave Policy");
            leaveService.mandatoryFieldsToCreateLeave(leavePolicyObject);*/

            defaultBody.putAll(mandatoryFields);
            leaveService.createLeaveForPolicy(defaultBody, leavePolicyObject);
            Assert.assertTrue(new LeaveApplicationHelperBackupObject().verifyMaxAllowedperYearSetting(), "Error in verifing Maximum Leave Allowed Per Year");
        }
        if (field.equalsIgnoreCase("GenderApplicablity")) {
            defaultBody.putAll(mandatoryFields);
            leavePolicyObject.genderApplicability();
        }
    }

}
