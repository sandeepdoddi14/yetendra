package com.darwinbox.reimbursement;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.services.ReimbFormService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

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
        @Test
        public void DeleteReimbFormActions() {
            Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");
            ReimbForm reimbForm = new ReimbForm();
            String response = reimbFormService.createReimbform(reimbForm);

            if(response == null)
            {
                String deleteResponse = reimbFormService.deleteReimbForm(reimbForm);
                Assert.assertTrue(deleteResponse.contains("Reimbursement has been deleted successfully"),"An error occurred in deleting");
            }
        }
        }
    }
