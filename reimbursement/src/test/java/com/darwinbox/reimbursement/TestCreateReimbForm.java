package com.darwinbox.reimbursement;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.services.ReimbFormService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

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

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void CreateReimbFormActions(Map<String, String>testdata)
    {
        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");
        String formname = testdata.get("Name");

        ReimbForm reimbForm = new ReimbForm();
        reimbForm.toObject(testdata);

            String createResponse = reimbFormService.createReimbform(reimbForm);
            Reporter("Reimbursement form created " + reimbForm.getName(), "INFO");
            Assert.assertNotNull(createResponse, "Error while Creating Reimbursement");

            String fname = reimbFormService.getReimbFormIdByName(reimbForm.getGrpCompany(),formname).getName();
            Assert.assertEquals(fname, formname, "Reimbursement form has been created successfully");
        }
    }