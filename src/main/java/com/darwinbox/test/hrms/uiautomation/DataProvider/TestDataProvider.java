/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.DataProvider;

import org.testng.annotations.DataProvider;

import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;

/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: TestDataProvider.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class TestDataProvider  {
	
	 ExcelReader excel = new ExcelReader();
	@DataProvider(name = "TestRuns")
	public  Object[][] DataProviderClass() throws Exception {
		Object[][] testObjArray = excel.getExcelData();		
		return (testObjArray);
	}

	@DataProvider(name="getvalidlogintestdata")
	public Object[][] getValidLoginTestData(){
		String[][] testRecords = excel.getExcelData();
		return testRecords;
	}
	
	@DataProvider(name="getinvaliddata")
	public Object[][] getInValidData(){
		String[][] testRecords = excel.getExcelData();
		return testRecords;
	}
	
	//get only column value with yes
	/*
	 * implement it later
	 */
	@DataProvider(name = "SheetName") 
    public Object[][] getSpecificColumSet() 
    { 
        Object[][] data = excel.getExcelData();
        int intColCount = 2; //getColumnCount("Order"); write a generic method to get col count

        int j = 0; 
        int arrRowCount=0; 
        for (int i = 0; i < data.length; i++) { 
            if((data[i][intColCount-1]).equals("Y"))
            { 
                arrRowCount++; 

                } 
            } 
               j=0;

               Object[][] retData = new Object[arrRowCount][intColCount]; 
               for (int i = 0; i < data.length; i++) 
               { 
                   if ((data[i][intColCount-1]).equals("Y")) {
                       retData[j] = data[i]; j++; }
               } 
               return retData; 
    }
}
