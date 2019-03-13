package com.darwinbox.mobile.helpdesk;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.helpDesk.MobileHelpdeskCategoriesList;
import com.darwinbox.mobile.dataFetching.mobileData.login.MobileAuthLogin;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class HelpdeskCategoryList extends TestBase {


    MobileHelpdeskCategoriesList mobilehelpdeskCategory;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
        System.out.println(" Master Sheet record obtained ");
    }

    @BeforeMethod
    public void initializeObjects() {
        mobilehelpdeskCategory = PageFactory.initElements(driver, MobileHelpdeskCategoriesList.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void getMobileHelpdeskCategoryList(Map<String, String> data) throws Exception {
        HashMap mobData = mobilehelpdeskCategory.getMobHelpdeskCategoriesList();
        log.info("********************* Mobile Data Fetched **********************");
        Reporter("********************* Mobile Data Fetched **********************", "Pass");
        log.info("Mobile Data -> " + mobData);

        try {
            Assert.assertEquals(mobData.get("status").toString().trim(), "1");
            log.info("Mobile Category List pass case: "+mobData.get("status"));
            Reporter("Mobile Category List pass case", "Pass");
        }
        catch (AssertionError e){
            e.printStackTrace();
            log.info("Mobile Category List failed case -->" + mobData.get("status").toString().trim());
            Reporter("Mobile Category List failed case", "Fail");
        }

    }
}

