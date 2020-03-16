package com.darwinbox.leaves;

import com.darwinbox.attendance.services.EmployeeServices;
import com.darwinbox.dashboard.actionClasses.CommonAction;
import com.darwinbox.dashboard.pageObjectRepo.generic.LoginPage;
import com.darwinbox.framework.uiautomation.DataProvider.TestDataProvider;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.ExcelWriter;
import com.darwinbox.framework.uiautomation.Utility.ResourceHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.leaves.Objects.LeavePolicyObject.LeavePolicyObject;
import com.darwinbox.leaves.Services.LeaveAdmin;
import com.darwinbox.leaves.Services.LeaveService;
import com.darwinbox.leaves.Utils.MapUtils;
import com.darwinbox.leaves.pageObjectRepo.settings.LeavesPage;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Map;

public class QueryDom  extends TestBase {

    LoginPage loginpage;
    CommonAction commonAction;
    int rowNumber=1;
    String fronEndValue=null;

    @BeforeClass
    public void setup() throws Exception {
        ms.getDataFromMasterSheet(this.getClass().getName());
    }

    @BeforeMethod
    public void initializeObjects() {
        loginpage = PageFactory.initElements(driver, LoginPage.class);
        commonAction = PageFactory.initElements(driver, CommonAction.class);


       // commonAction.changeApplicationAccessMode("Admin");

    }


    @Test(dataProvider = "TestRuns", dataProviderClass = TestDataProvider.class, groups = "Leave_Settings")
    public void verifyRestrictions(Map<String, String> testData)  throws Exception{


        readInstanceDetails(testData.get("instance"));
        driver.get(data.get("url"));
        loginpage.loginToApplication();
        driver.get(testData.get("url"));
    new WaitHelper(driver).waitForPageToLoad();
        Reporter("Url to Navigate is -->"+testData.get("url"),"Info");
        new WaitHelper(driver).waitForPageToLoad();
       fronEndValue = driver.findElement(By.xpath("//body")).getText();
        Reporter("Body Found On Page -->"+fronEndValue,"Info");
       writeToExcel();

       rowNumber++;
    }


    protected void writeToExcel() throws IOException {

        Workbook excelWorkbook = null;
        File file = new File(ResourceHelper.getBaseResourcePath() + "//src//main//resources//TestData//QueryDom.xlsx");
        FileInputStream inputStream = new FileInputStream(file);
        excelWorkbook = new XSSFWorkbook(inputStream);
        Sheet sheet = excelWorkbook.getSheet("url");
        sheet.getRow(rowNumber).createCell(1, CellType.STRING).setCellValue(fronEndValue);
        inputStream.close();
        FileOutputStream outputStream = new FileOutputStream(file);
        excelWorkbook.write(outputStream);
        outputStream.close();
    }


    private void readInstanceDetails(String instance) {

        log.debug(" Reading instance details from config.ini ");
        HierarchicalINIConfiguration properties = null;

        String fileName = respath + "config.ini";

        try {
            properties = new HierarchicalINIConfiguration(fileName);
        } catch (Exception e) {
            log.error(" Exception while reading properties from config.ini ");
            log.error(" Exiting system as reading config failed ");
            log.error(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }


        SubnodeConfiguration c = properties.getSection(instance);

        String url = (c.getProperty("url").toString());


        data.put("@@url",url);
        data.put("@@admin", (c.getProperty("admin").toString()));
        data.put("@@password", (c.getProperty("password").toString()));

        data.put("@@parent", ( (c.getProperty("parent") == null) ? "" : c.getProperty("parent").toString() ));
        data.put("@@group", ( (c.getProperty("group") == null) ? " " : c.getProperty("group").toString() ));


        log.info(" Instance URL ----> " + url);

    }



}