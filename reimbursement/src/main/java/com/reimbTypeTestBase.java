package com;

import com.darwinbox.framework.uiautomation.Utility.ExcelReader;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbLimitsBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reimbTypeTestBase {
    private String fileName;

    public void CreateReimbType()
    {
        List<Map<String,String>> ReimbFormDataList = readDatafromSheet("ReimbForm");
        List<Map<String,String>> ReimbLimitsBodyDataList = readDatafromSheet("ReimbLimitsBody");

    for(Map<String, String> data : ReimbLimitsBodyDataList)
    {
        ReimbLimitsBody reimbLimitsBody = new ReimbLimitsBody();
        reimbLimitsBody.toObject(data);

    }
    }
    private List<Map<String, String>> readDatafromSheet(String sheetname) {

        Map<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", fileName);
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }
}
