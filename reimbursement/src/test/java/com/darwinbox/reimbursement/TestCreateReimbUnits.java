package com.darwinbox.reimbursement;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbUnits;
import com.darwinbox.reimbursement.services.ReimbUnitService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestCreateReimbUnits extends TestBase {

    LoginPage loginPage;
    ReimbUnits reimbUnits;
    ReimbUnitService reimbUnitService;

    @BeforeClass
    public void BeforeClass()
    {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects()
    {
        loginPage = new LoginPage(driver);
        reimbUnits = new ReimbUnits();
        reimbUnitService = new ReimbUnitService();
    }

    @Test(dataProvider = "TestRuns" , dataProviderClass = TestDataProvider.class)
    public void CreateReimbUnitActions(Map<String, String> testData) throws Exception
    {
        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User is unable to login to application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to admin unsuccessful");

        reimbUnits.toObject(testData);
        reimbUnitService.createReimbUnit(reimbUnits);
       String  expected = reimbUnits.getUnitType();
        String actual = reimbUnitService.getReimbUnitByName(testData.get("Unit Type")).getUnitType();

        Assert.assertEquals(expected, actual, "Reimbursement unit has been created successfully");

        }
        }

