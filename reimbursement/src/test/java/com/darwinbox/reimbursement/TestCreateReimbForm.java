package com.darwinbox.reimbursement;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbLimitsBody;
import com.darwinbox.reimbursement.services.ReimbFormService;
import com.darwinbox.reimbursement.services.ReimbUnitService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

/*@Test
    public String CheckReimbFormActions()
    {
        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");
        String response = null;
         response = reimbFormService.getReimbFormIdByName("Automation","Cab Allowances"); // null : false
       if(response!= null)
           Reporter("Reimbursement Form is already present, ID is :"+response, "INFO");
       else
           Reporter("Reimbursement Form is not present", "INFO");

        return  response;
    }*/

    @Test
    public void CreateReimbFormActions() {
        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");

        ReimbForm reimbForm = new ReimbForm();
        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("TenantReimbursement[name]", "Demo"));
        body.add(new BasicNameValuePair("Reimb_set[0][designation][]", "ALL_0"));
        body.add(new BasicNameValuePair("Reimb_set[0][location][0][]", "ALL_0"));
        body.add(new BasicNameValuePair("Reimb_set[0][upper_cap_unit]", "6787"));

        String response = reimbFormService.createReimbform(reimbForm);
        Assert.assertNotNull(response, "Error while Creating Reimbursement");

        if (response.contains("Error while Creating Reimbursement"))
            Reporter("Error in creating Reimbursement", "INFO");
        //String fname = reimbFormService.getReimbFormIdByName(reimbForm).getName();
        // Assert.assertEquals(formName, fname, "Reimbursement form has been created successfully");
    }
}

