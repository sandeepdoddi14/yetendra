package com.darwinbox.core.BandServices;

import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.services.BandServices;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

public class create extends TestBase {

    BandServices bandServices = new BandServices();
    Band band=null;




    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }
    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void createBand(HashMap<String,String> testData){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginToApplication();
        loginPage.switchToAdmin();
            band = new Band();
            band.setBandName(testData.get("BandName"));
            band.setDescription(testData.get("Description"));


            bandServices.createBand(band);


            HashMap<String,String> bands=bandServices.getBands();


        Assert.assertTrue(bands.get(band.getBandName())!=null,"Band Not Created Successfully");


    }
}
