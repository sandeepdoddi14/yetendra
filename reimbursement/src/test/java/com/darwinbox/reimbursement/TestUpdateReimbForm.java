package com.darwinbox.reimbursement;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.services.ReimbFormService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class TestUpdateReimbForm extends TestBase {
    LoginPage loginPage;
    ReimbFormService reimbFormService;

    @BeforeClass
    public void BeforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        reimbFormService = new ReimbFormService();
    }
/*

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void UpdateReimbFormActions(Map<String, String> testdata) throws Exception {
        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");

        ReimbForm reimbForm = new ReimbForm();
        reimbForm.toObject(testdata);
        String formname = testdata.get("Name");
        reimbForm = reimbFormService.getReimbFormIdByName(testdata.get("GroupCompany"), formname);

        if (reimbForm.getId() != null) {
            formname = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");
            reimbForm.setName(formname);
        }
            String createResponse = reimbFormService.createReimbform(reimbForm);
            Reporter("Reimbursement form created " + reimbForm.getName(), "INFO");

            reimbForm.setName(formname + "_updated");
            String updateresponse = reimbFormService.updateReimbForm(reimbForm);
            Assert.assertNotNull(updateresponse, "Error while Updating Reimbursement");

            Reporter("Reimbursement: " + formname + " has been updated to :" + reimbForm.getName(), "INFO");
            Assert.assertFalse(updateresponse.contains("Please resolve the errors."), "Reimbursement Unit has been updated!");

        }
*/

}
