package com.darwinbox.reimbursement;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.Expenses;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import com.darwinbox.reimbursement.services.ReimbExpenseService;
import com.darwinbox.reimbursement.services.ReimbFormService;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestCreateAnExpenseAsEmployeeWithoutAttachment extends TestBase {
    LoginPage loginPage;
    Expenses expenses;
    ReimbFormService reimbFormService;
    ReimbForm reimbForm;
    CommonAction commonAction;
    ReimbExpenseService reimbExpenseService;


    @BeforeClass
    public void BeforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        expenses = new Expenses();
        reimbForm = new ReimbForm();
        reimbFormService = new ReimbFormService();
        reimbExpenseService = new ReimbExpenseService();
        //reimbFormService.createReimbform(reimbForm);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testCreateExpense(Map<String, String> testData) throws Exception {
       Assert.assertTrue(loginPage.loginToApplicationAsAdmin(),"User login to Application failed");

       commonAction.changeApplicationAccessMode("Employee");
       expenses.toObject(testData);
       String date = expenses.getDate();
       String inputmonth = date.split("-")[1]+"-"+date.split("-")[2];
       String validateResponse = reimbExpenseService.validateExpenseAsEmployee(expenses);
      // Assert.assertTrue(validateResponse.contains(expenses.getFormType()),"please check your inputs & retry");

       String createResponse = reimbExpenseService.createExpenseAsEmployee(expenses);
       Assert.assertFalse(createResponse.contains("failure"),"Expense created successfully");
       Reporter("Expense has been created :"+ expenses.getTitle(), "INFO");

       String getByStatusresponse = reimbExpenseService.getAllExpensesOnFiltersByEmployee("Pending", inputmonth);
        Assert.assertTrue(getByStatusresponse.contains(expenses.getTitle()));
        Reporter("Expense has been created successfully","INFO");


    }
    }
