package com.sample.framework.uiautomation.Utility;

import com.sample.framework.uiautomation.base.TestBase;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    public static int rowNo = -1;
    public String fileName;
    public String sheetName;
    private Workbook workbook;
    private Sheet sheet;
    private List<Integer> tcIds = new ArrayList<Integer>();
    private String testDataRow = "";

    public void setExcelFileObject() {

        try {

            FileInputStream inputStream = new FileInputStream(fileName);
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
                throw new Exception(" Invalid file extension fo parsing ");
            }
        } catch (NullPointerException ne) {
            throw new RuntimeException("File Name or Sheet Name is not defined.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFilenameAndSheetName(Map masterData) {
        fileName = masterData.get("FileName").toString();
        sheetName = masterData.get("SheetName").toString();
        testDataRow = masterData.get("TestDataRow").toString();
        fileName = TestBase.datapath + "src/main/resources/TestData/" + fileName;
        setExcelFileObject();
    }

    private String[] getColumnHeaders(String sheetName) {
        try {

            sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);

            int cols = row.getLastCellNum();
            String[] header = new String[cols];

            for (int j = 0; j < cols; j++)
                header[j] = row.getCell(j).getStringCellValue();
            return header;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception in getting column headers");
        }
    }

    public List<Map<String, String>> getExcelData() {

        List<Map<String, String>> excelData = new ArrayList<>();
        boolean all = false;
        if (testDataRow.equalsIgnoreCase("all"))
            all = true;
        else
            parseDataRowIds(testDataRow);
        try {

            sheet = workbook.getSheet(sheetName);
            int rows = sheet.getLastRowNum();

            String[] header = getColumnHeaders(sheetName);
            int cols = header.length;

            for (int i = 1; i <= rows; i++) {

                if (all || tcIds.contains(i)) {

                    Row row = sheet.getRow(i);
                    HashMap<String, String> data = new HashMap<>();

                    for (int j = 0; j < cols; j++) {
                        String cellValue = getCellData(row.getCell(j));
                        if( header[j].equalsIgnoreCase("RunMode") ) {
                            if (cellValue.toLowerCase().equalsIgnoreCase("no")){
                                data = null;
                                break;
                            }
                        }
                        data.put(header[j], cellValue);
                    }

                    if (data != null && data.size() != 0)
                        excelData.add(data);
                }
            }
        } catch (Exception e) {
        }
        return excelData;
    }


    public String getCellData(Cell cell) {

        String cellText = "";

        try {

            CellType type = cell.getCellTypeEnum();

            if (type == CellType.STRING)
                cellText = cell.getStringCellValue();
            else if (type == CellType.NUMERIC)
                cellText = cell.getNumericCellValue() + "";
            else if (type == CellType.BOOLEAN)
                cellText = cell.getBooleanCellValue() + "";
            else
                cellText = "";

        } catch (Exception e) {

        }

        return cellText;
    }

    public List<Map<String, String>> getExcelDataByRow() {

        List<Map<String, String>> excelData = new ArrayList<>();

        if (rowNo == -1) {
            rowNo = 1;
        }
        try {

            sheet = workbook.getSheet(sheetName);

            String[] header = getColumnHeaders(sheetName);
            int cols = header.length;

            Row row = sheet.getRow(rowNo);

            HashMap<String, String> data = new HashMap<>();

            for (int j = 0; j < cols; j++) {

                String cellValue = getCellData(row.getCell(j));
                data.put(header[j], cellValue);
            }


            excelData.add(data);
            rowNo = -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excelData;
    }

    private void parseDataRowIds(String dataRow) {

        if (dataRow.isEmpty() || dataRow.equalsIgnoreCase("all"))
            return;

        String[] tcidlist = dataRow.split(",");

        for (String id : tcidlist) {
            if (id.contains("-")) {
                String tcRange[] = id.split("-");
                int low = Integer.parseInt(tcRange[0]);
                int high = Integer.parseInt(tcRange[1]);
                while (low <= high) {
                    tcIds.add(low++);
                }
                continue;
            }

            int idnum = Integer.parseInt(id);
            tcIds.add(idnum);

            String tcidStr = "";
            for (Integer tcId : tcIds) {
                tcidStr = tcidStr + "," + tcId;
            }
        }

    }
}