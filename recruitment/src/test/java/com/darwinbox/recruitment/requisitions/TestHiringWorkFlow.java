package com.darwinbox.recruitment.requisitions;

import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.RecruitmentTestBase;
import com.darwinbox.recruitment.objects.HiringWorkFlow.HiringWorkFlow;
import com.darwinbox.recruitment.objects.HiringWorkFlow.HiringWorkFlowLineItems;
import com.darwinbox.recruitment.objects.addCandidates.AddCandidates;
import com.darwinbox.recruitment.services.HiringWorkFlowService;
import com.darwinbox.recruitment.services.JobsPagesService;
import com.darwinbox.recruitment.services.RequisitionService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestHiringWorkFlow extends TestBase {

    LoginPage loginPage;
    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);

    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class,enabled = false)
    public void testHiringWorkFlow(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

       RecruitmentTestBase recruitmentTestBase = RecruitmentTestBase.getObject("CRUD.xlsx");

       recruitmentTestBase.updateHiringWorkFlow();

       recruitmentTestBase.updateHiringWFThirdPage();


    }
    }
