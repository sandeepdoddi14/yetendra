package com.darwinbox.reimbursement;

import com.ReimbTypeTestBase;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.reimbursement.objects.ReimbCreation.Expenses;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.services.ReimbExpenseService;
import com.darwinbox.reimbursement.services.ReimbFormService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

import static com.darwinbox.framework.uiautomation.base.TestBase.driver;
import static com.darwinbox.framework.uiautomation.base.TestBase.ms;

public class TestNoOfDaysPostExpenseCheckAsEmployee {
    LoginPage loginPage;
    Expenses expenses;
    ReimbFormService reimbFormService;
    ReimbForm reimbForm;
    CommonAction commonAction;
    ReimbExpenseService reimbExpenseService;
    ReimbTypeTestBase reimbTypeTestBase;

    @BeforeClass
    public void BeforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());

    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        expenses = new Expenses();
        reimbExpenseService = new ReimbExpenseService();
        reimbForm = new ReimbForm();
        reimbFormService = new ReimbFormService();
        //reimbFormService.createReimbform(reimbForm);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void TestNoOfDaysPostExpense(Map<String, String> testData) {
        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(),"User login to Application failed");
        commonAction.changeApplicationAccessMode("Employee");
        expenses.toObject(testData);
        String date = expenses.getDate();
        String inputmonth = date.split("-")[1]+"-"+date.split("-")[2];
        String validateResponse = reimbExpenseService.validateExpenseAsEmployee(expenses);
      //  Assert.assertTrue(validateResponse.contains(expenses.getFormType()),"please check your inputs & retry");

        String createResponse = reimbExpenseService.createExpenseAsEmployee(expenses);

    }
}
