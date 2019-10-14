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

public class TestUpdateReimbUnits extends TestBase {

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
    public void UpdateReimbUnitActions(Map<String, String> testData) throws Exception {

        reimbUnitService.updateReimbUnit(reimbUnits);
        Map map = reimbUnitService.getAllReimbUnits();
        Assert.assertEquals(map.get(reimbUnits.getId()), reimbUnits, "Reimbursement unit has been updated successfully");

    }
    }
