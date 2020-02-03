package com.darwinbox.recruitment.requisitions;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.staffingModel.PositionSettings;
import com.darwinbox.recruitment.objects.addCandidates.AddCandidates;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class TestAddCandidates extends TestBase {

    LoginPage loginPage;
    AddCandidates addCandidates;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        addCandidates = new AddCandidates();

    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testAddCandidates(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        new PositionSettings().UpdatePositions();

        /*activate(JObs page-1) and post job(jobs page-4) to add candidates*/
        /*candidates are added*/

         String JobID=  addCandidates.filter();
         Reporter("Add candidates is done for Job - "+ JobID,"PASS");
        Assert.assertNotNull(JobID,"Job ID is NOT captured");

         /*get ID of a candidate added*/

      String candidateID =  addCandidates.filterByGetAddedCandidates();
      Assert.assertNotNull(candidateID,"candidate ID is NOT captured");

        /*update a candidate details*/



    }
    }
