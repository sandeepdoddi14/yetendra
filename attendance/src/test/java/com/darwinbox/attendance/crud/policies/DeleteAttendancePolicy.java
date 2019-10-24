package com.darwinbox.attendance.crud.policies;

import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.others.PolicyInfo;
import com.darwinbox.attendance.services.settings.AttendancePolicyService;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class DeleteAttendancePolicy extends TestBase {

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
    public void testDeletePolicy(Map<String, String> testData) {

        String title = " Test For Deleting Attendance policy With Same Name ";
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

        String policy_id  = srvc.getAttendancePolicyId(policy_name, compName);
        policy = srvc.getAttendancePolicy(policy_id);
        srvc.deleteAttendancePolicy(policy);

        policy_id  = srvc.getAttendancePolicyId(policy_name, compName);
        Assert.assertNull(policy_id,"Policy Not Deleted Successfully ");

        Reporter("Attendance Policy Deleted Successfully", "PASS");
    }
}
