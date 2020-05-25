
package com.sample.framework.uiautomation.DataProvider;

import com.sample.framework.uiautomation.base.TestBase;
import com.sample.framework.uiautomation.Utility.ExcelReader;
import org.testng.annotations.DataProvider;

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
