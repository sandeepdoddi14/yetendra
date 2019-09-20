package com.darwinbox.attendance.shifts;

import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.services.settings.ReportsDashboardServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.ArchivePosition;
import com.darwinbox.recruitment.objects.CandidateTags;
import com.darwinbox.recruitment.services.ArchivePositionService;
import com.darwinbox.recruitment.services.CandidateTagsService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class CRUDOperations extends TestBase {

    LoginPage loginPage;
    Date date;
    CandidateTags candidateTags;
    CandidateTagsService candidateTagsService;
    ArchivePosition archivePosition;
    ArchivePositionService archivePositionService;


    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        candidateTags = new CandidateTags();
        candidateTagsService = new CandidateTagsService();
        archivePosition = new ArchivePosition();
        archivePositionService = new ArchivePositionService();

    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testCRUD(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");



    }

}
