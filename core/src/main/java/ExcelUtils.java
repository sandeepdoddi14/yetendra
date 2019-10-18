import com.darwinbox.framework.uiautomation.Utility.ExcelReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static List<Map<String, String>> readDatafromSheet(String sheetname) {

        HashMap<String, String> excelDetails = new HashMap<>();
        excelDetails.put("FileName", "/Application/LeaveApplication.xlsx");
        excelDetails.put("TestDataRow", "all");
        excelDetails.put("SheetName", sheetname);

        ExcelReader reader = new ExcelReader();
        reader.setFilenameAndSheetName(excelDetails);
        return reader.getExcelData();
    }
}
