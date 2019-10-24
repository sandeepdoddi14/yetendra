package com.darwinbox.attendance.crud.policies;

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

public class UpdateExistingPolicyWithSameName extends TestBase {

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
        boolean isParent = testData.get("IsParent").equalsIgnoreCase("yes");
        String compName = isParent ?  TestBase.data.get("@@parent") : TestBase.data.get("@@group");

        String grpCompany = srvc.getGroupCompanyIds().get(compName);

        String oldpolicyName = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");
        Reporter("Creating policy : " + oldpolicyName + " in company " + compName, "INFO");
        AttendancePolicy oldpolicy = new AttendancePolicy();
        PolicyInfo oldinfo = oldpolicy.getPolicyInfo();
        oldinfo.setPolicyName(oldpolicyName);
        oldinfo.setCompanyID(isParent ? ""  : grpCompany );
        srvc.createAttendancePolicy(oldpolicy);
        Reporter("Attendance Policy Created Successfully", "INFO");

        String newpolicyName = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");
        AttendancePolicy newPolicy = new AttendancePolicy();
        PolicyInfo info = newPolicy.getPolicyInfo();
        info.setPolicyName(newpolicyName);
        info.setCompanyID(isParent ? ""  : grpCompany );
        Reporter("Creating policy : " + newpolicyName + " in company " + compName, "INFO");
        srvc.createAttendancePolicy(newPolicy);
        Reporter("Attendance Policy Created Successfully", "INFO");

        String policy_id  = srvc.getAttendancePolicyId(newpolicyName, compName);
        newPolicy = srvc.getAttendancePolicy(policy_id);
        newPolicy.getPolicyInfo().setPolicyName(oldpolicyName);

        JSONObject response = new JSONObject(srvc.updatePolicy(newPolicy));
        String errorMsg = response.getString("error").replaceAll("\\<.*?>","");

        Assert.assertEquals(response.getString("status"),"failure","Failure is as expected");
        Assert.assertEquals(response.getString("message"),"Please resolve the errors.","Message is as expected ");
        Assert.assertTrue(errorMsg.contains(oldpolicy + " already exist."),"Message is as expected ");

        Reporter("Unable to create policy with same name ","PASS");


        Reporter("Policy Deleted Successfully ","INFO");
    }
}