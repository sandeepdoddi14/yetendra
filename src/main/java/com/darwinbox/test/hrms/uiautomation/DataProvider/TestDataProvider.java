
package com.darwinbox.test.hrms.uiautomation.DataProvider;

import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import org.testng.annotations.DataProvider;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;

import java.util.*;

/**
 * @author balaji
 * @Creation_Date: 20 Nov 2017
 * @ClassName: TestDataProvider.java
 * @LastModified_Date: 20 Nov 2017
 */
public class TestDataProvider {

    ExcelReader excel = new ExcelReader();

    @DataProvider(name = "TestRuns")
    public Iterator<Object[]> getEntireSheetData() {

        List<Map<String, String>> testRecords = excel.getExcelData();
        Collection<Object[]> collection = new ArrayList<Object[]>();

        for (Map<String, String> testRecord : testRecords)
            collection.add(new Map[]{testRecord});

        TestBase.setDataItem(testRecords);

        return collection.iterator();
    }

    @DataProvider(name = "specifcRow")
    public Iterator<Object[]> getSpecificRowdata() {

        String sheetName = excel.SheetName;


        List<Map<String, String>> testRecords = excel.getExcelData();
        Collection<Object[]> collection = new ArrayList<Object[]>();

        for (Map<String, String> testRecord : testRecords)
            collection.add(new Map[]{testRecord});

        return collection.iterator();
    }
}
