package com.darwinbox.reimbursement;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbUnits;
import com.darwinbox.reimbursement.services.ReimbUnitService;
import com.github.javafaker.Bool;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class TestValidateDuplicateReimbUnits extends TestBase {


    LoginPage loginPage;
    ReimbUnitService reimbUnitService;

    @BeforeClass
    public void BeforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        reimbUnitService = new ReimbUnitService();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void ValidateReimbUnitActions(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to admin unsuccessful");

        String unitType = testData.get("Unit Type");

        ReimbUnits reimbUnits = new ReimbUnits();
        reimbUnits.toObject(testData);

        Map allReimbUnits = reimbUnitService.getAllReimbUnits();

        if (allReimbUnits.containsKey(reimbUnits.getUnitType())) {
            unitType = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");
            reimbUnits.setUnitType(unitType);
        }

        reimbUnitService.createReimbUnit(reimbUnits);
        Reporter("Reimbursement unit created by the name: " + unitType, "INFO");
        reimbUnits = reimbUnitService.getReimbUnitIdByName(unitType);

        reimbUnitService.createReimbUnit(reimbUnits);
        Reporter("Reimbursement unit created by the name: " + unitType + " again", "INFO");
        reimbUnitService.deleteReimbUnit(reimbUnits);
        Reporter("Reimbursement units deleted by the name: " + unitType, "INFO");

        reimbUnits = reimbUnitService.getReimbUnitIdByName(unitType);
        Assert.assertNull(reimbUnits, "Reimbursement unit has been duplicated");

    }
}
