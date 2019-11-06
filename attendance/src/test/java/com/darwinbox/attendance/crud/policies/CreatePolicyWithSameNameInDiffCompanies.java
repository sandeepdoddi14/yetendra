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

public class CreatePolicyWithSameNameInDiffCompanies extends TestBase {

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

        String title = " Test For Creating Attendance policy With Same Name in different companies ";
        Reporter(title,"INFO");

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "Login Unsuccessfull ");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessfull ");

        AttendancePolicyService srvc = new AttendancePolicyService();

        String policy_name = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");

        String parent = TestBase.data.get("@@parent");
        String group = TestBase.data.get("@@group");

        Reporter("Creating policy : " + policy_name + " in Parent company " + parent, "INFO");

        AttendancePolicy policy = new AttendancePolicy();
        PolicyInfo info = policy.getPolicyInfo();
        info.setPolicyName(policy_name);
        info.setCompanyID("");

        srvc.createAttendancePolicy(policy);
        Reporter("Attendance Policy Created in Parent Company", "PASS");

        String grpCompany = srvc.getGroupCompanyIds().get(group);
        info.setCompanyID(grpCompany);

        srvc.createAttendancePolicy(policy);
        Reporter("Attendance Policy Created in Group Company", "PASS");

        AttendancePolicy parentPolicy = srvc.getAttendancePolicy(srvc.getAttendancePolicyId(policy_name,parent));
        AttendancePolicy groupPolicy = srvc.getAttendancePolicy(srvc.getAttendancePolicyId(policy_name,group));

        srvc.deleteAttendancePolicy(parentPolicy);

        info.setCompanyID("");
        srvc.createAttendancePolicy(policy);
        Reporter("Attendance Policy Created in Parent Company", "PASS");

        parentPolicy = srvc.getAttendancePolicy(srvc.getAttendancePolicyId(policy_name,parent));

        srvc.deleteAttendancePolicy(parentPolicy);
        srvc.deleteAttendancePolicy(groupPolicy);

        Reporter("Deleted attendance policies that are created", "INFO");

    }
}