package com.darwinbox.attendance.policies;

import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.others.PolicyInfo;
import com.darwinbox.attendance.services.settings.AttendancePolicyService;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class CreatePolicyWithSameName extends TestBase {

    LoginPage loginPage;
    GenericHelper genHelper;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        genHelper = new GenericHelper(driver);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "policies, crud", retryAnalyzer = TestBase.class)
    public void testCreatePolicyWithSameName(Map<String, String> testData) {

        String title = " Test For Creating Attendance policy With Same Name ";
        Reporter(title,"INFO");

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendancePolicyService srvc = new AttendancePolicyService();

        String policy_name = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");

        boolean isParent = testData.get("IsParent").equalsIgnoreCase("yes");
        String compName = isParent ?  TestBase.data.get("@@parent") : TestBase.data.get("@@group");

        Reporter("Creating policy : " + policy_name + " in company " + compName, "INFO");

        String grpCompany = srvc.getGroupCompanyIds().get(compName);

        AttendancePolicy policy = new AttendancePolicy();
        PolicyInfo info = policy.getPolicyInfo();
        info.setPolicyName(policy_name);
        info.setCompanyID(isParent ? ""  : grpCompany );

        srvc.createAttendancePolicy(policy);
        Reporter("Attendance Policy Created Successfully", "PASS");

        JSONObject response = new JSONObject(srvc.createPolicy(policy));
        String errorMsg = response.getString("error").replaceAll("\\<.*?>","");

        Assert.assertEquals(response.getString("status"),"failure","Failure is as expected");
        Assert.assertEquals(response.getString("message"),"Please resolve the errors.","Message is as expected ");
        Assert.assertTrue(errorMsg.contains(policy_name + " already exist."),"Message is as expected ");

        Reporter("Unable to create policy with same name ","PASS");

        String policy_id  = srvc.getAttendancePolicyId(policy_name, compName);
        policy = srvc.getAttendancePolicy(policy_id);
        srvc.deleteAttendancePolicy(policy);

        Reporter("Policy Deleted Successfully ","INFO");
    }
}