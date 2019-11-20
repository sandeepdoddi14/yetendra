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

public class TestDeleteReimbForm {


    public class TestCreateReimbForm extends TestBase {
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
        public void DeleteReimbFormActions(Map<String, String> testdata) throws Exception {
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

                String deleteResponse = reimbFormService.deleteReimbForm(reimbForm);
                Reporter("Reimbursement unit by the name: " + reimbForm.getName() + " has been deleted", "INFO");

                Assert.assertTrue(deleteResponse.contains("Reimbursement has been deleted successfully"), "An error occurred in deleting");
                Assert.assertFalse(deleteResponse.contains("Sorry you cannot Delete this Reimbursement because it is already used by user."), "Reimbursement has been deleted successfully");
            }
*/

    }
}
