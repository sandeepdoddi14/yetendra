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

public class TestValidateDuplicateReimbForm extends TestBase {
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

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void ValidateReimbUnitActions(Map<String, String> testdata) throws Exception {

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");

        ReimbForm reimbForm = new ReimbForm();
        reimbForm.toObject(testdata);
        String formname = testdata.get("Name");
        reimbForm = reimbFormService.getReimbFormIdByName(testdata.get("GroupCompany"), formname);

        if (reimbForm.getId() != null) {
            formname = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");
            reimbForm.setName(formname);
        }
        reimbFormService.createReimbform(reimbForm);
        Reporter("Reimbursement form created by the name: " + reimbForm, "INFO");
        reimbForm = reimbFormService.getReimbFormIdByName(testdata.get("GroupCompany"), formname);

        reimbFormService.createReimbform(reimbForm);
        Reporter("Reimbursement unit created by the name: " + formname + " again", "INFO");
        reimbFormService.deleteReimbForm(reimbForm);
        Reporter("Reimbursement units deleted by the name: " + formname, "INFO");

        reimbForm = reimbFormService.getReimbFormIdByName(testdata.get("GroupCompany"), formname);
        Assert.assertNull(formname, "Reimbursement unit has been duplicated");

    }
}
