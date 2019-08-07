package com.darwinbox.attendance.shifts;

import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.services.settings.ReportsDashboardServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Map;

public class CRUDOperations extends TestBase {

    LoginPage loginPage;
    ReportsDashboardServices reportsDashboardServices;
    Reports reports;
    Date date;

    @BeforeClass
    public void beforeClass() {
        ms.getDataFromMasterSheet(this.getClass().getName());
        super.beforeClass();
    }

    @BeforeTest
    public void initializeObjects() {
        loginPage = new LoginPage(driver);
        reportsDashboardServices=new ReportsDashboardServices();
        reports=new Reports();
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void testCRUD(Map<String, String> testData) throws Exception {

        Assert.assertTrue(loginPage.loginToApplication(data.get("@@admin"), data.get("@@password")), "User not Loggin to Application as Admin");
        Assert.assertTrue(loginPage.switchToAdmin(), "Switch to Admin Unsuccessful ");

        reports.setReportType("opt_roster_11");
        reports.setModule("opt_main_roster_1");
        reports.setReportFilter("div_5bc0e9fa764db");
        reports.setSubCriteria(String.valueOf(Reports.employeeTypes.activeEmployees));
        reports.setMonthAndYear("2019-06");
        reportsDashboardServices.getReportLateMarkSummary(reports);



    }

}
