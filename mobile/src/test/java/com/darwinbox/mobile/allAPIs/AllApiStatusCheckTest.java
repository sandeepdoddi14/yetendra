package com.darwinbox.mobile.allAPIs;

import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.mobile.dataFetching.mobileData.all_APIs.AllApiStatusCheck;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class AllApiStatusCheckTest extends TestBase {


    AllApiStatusCheck allApiStatusCheck;
    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
        System.out.println(" Master Sheet record obtained ");
    }

    @BeforeMethod
    public void initializeObjects() {
        allApiStatusCheck = PageFactory.initElements(driver, AllApiStatusCheck.class);
    }

    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class)
    public void allMobileAPIsTest(Map<String, String> data) throws Exception {
        HashMap mobData = allApiStatusCheck.getAllApiStatusCheck();
        log.info("********************* Mobile Data Fetched **********************");
        Reporter("********************* Mobile Data Fetched **********************", "Pass");
        log.info("Mobile Data -> " + mobData);
        String moduleName = mobData.get("module").toString().trim();
        String apiName = mobData.get("apiName").toString().trim();
            try {
                Assert.assertEquals(mobData.get("status").toString().trim(), getData("Expected Status"));
                log.info("Mobile APIs status pass for the module: "+moduleName+", API Name: "+apiName);
                Reporter("Mobile APIs status pass for the module: "+moduleName+", API Name: "+apiName, "Pass");
            } catch (AssertionError e) {
                e.printStackTrace();
                log.info("Mobile APIs status fail for the module: "+moduleName+", API Name: "+apiName);
                Reporter("Mobile APIs status fail for the module: "+moduleName+", API Name: "+apiName, "Fail");
            }
    }
}

