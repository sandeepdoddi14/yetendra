package com.darwinbox.reimbursement;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbUnits;
import com.darwinbox.reimbursement.services.ReimbUnitService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class TestUpdateReimbUnits extends TestBase {

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
    public void UpdateReimbUnitActions(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplicationAsAdmin(), "User is unable to login to application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to admin unsuccessful");

        ReimbUnits reimbUnits = new ReimbUnits();
        reimbUnits.toObject(testData);
        String unitType = testData.get("Unit Type");

        Map allReimbUnits = reimbUnitService.getAllReimbUnits();

        if (allReimbUnits.containsKey(reimbUnits.getUnitType())) {
            unitType = "Default_Create_" + new DateTimeHelper().formatDateTo(new Date(), "YYYYMMdd_HHmmss");
            reimbUnits.setUnitType(unitType);
        }

        reimbUnitService.createReimbUnit(reimbUnits);
        reimbUnits = reimbUnitService.getReimbUnitIdByName(unitType);
        Reporter("Reimbursement unit created by the name: "+reimbUnits.getUnitType(), "INFO");

        reimbUnits.setUnitType(unitType+"_updated");
        String response= reimbUnitService.updateReimbUnit(reimbUnits);
        Reporter("Reimbursement: "+reimbUnits.getUnitType() + " has been updated to :" + reimbUnits.getUnitType() ,"INFO");
        Assert.assertTrue(response.contains("Reimbursement Unit has been updated!"),"Error in updating Reimbursement unit." );

        reimbUnits = reimbUnitService.getReimbUnitIdByName(unitType);
        Assert.assertNull(reimbUnits, "Reimbursement unit has been updated successfully");

    }
}
