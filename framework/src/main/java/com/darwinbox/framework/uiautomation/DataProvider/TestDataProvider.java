
package com.darwinbox.framework.uiautomation.DataProvider;

import com.darwinbox.framework.uiautomation.base.TestBase;
import org.testng.annotations.DataProvider;
import com.darwinbox.framework.uiautomation.Utility.ExcelReader;

import java.util.*;

public class TestDataProvider {

    @DataProvider(name = "TestRuns")
    public Iterator<Object[]> getEntireSheetData() {

        ExcelReader excel = TestBase.ms.reader;

        List<Map<String, String>> testRecords = excel.getExcelData();
        Collection<Object[]> collection = new ArrayList<Object[]>();

        for (Map<String, String> testRecord : testRecords) {
            collection.add(new Map[]{testRecord});
            TestBase.dataItem.add(testRecord);
        }

        return collection.iterator();
    }

    @SuppressWarnings("unused")
	@DataProvider(name = "specifcRow")
    public Iterator<Object[]> getSpecificRowdata() {

        ExcelReader excel = TestBase.ms.reader;

        List<Map<String, String>> testRecords = excel.getExcelData();
        Collection<Object[]> collection = new ArrayList<Object[]>();

        for (Map<String, String> testRecord : testRecords)
            collection.add(new Map[]{testRecord});

        return collection.iterator();
    }
}
