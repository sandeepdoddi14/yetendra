package com.sample.framework.importExport;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    public String fileName;
    private Workbook workbook;

    public ExcelReader(String fileName) {
        this.fileName = fileName;
    }

    public void setExcelFileObject() {
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getColumnHeaders(String sheetName) {

        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(0);

        int cols = row.getLastCellNum();
        String[] header = new String[cols];

        for (int j = 0; j < cols; j++) {
            header[j] = row.getCell(j).getStringCellValue();
        }
        return header;
    }

    public List<Map<String, String>> getExcelData(String sheetName) {

        List<Map<String, String>> excelData = new ArrayList<Map<String, String>>();

        try {

            Sheet sheet = workbook.getSheet(sheetName);

            int rows = sheet.getLastRowNum();

            String[] header = getColumnHeaders(sheetName);

            int cols = header.length;

            for (int i = 1; i <= rows; i++) {

                Row row = sheet.getRow(i);

                HashMap<String, String> data = new HashMap<>();

                for (int j = 0; j < cols; j++) {

                    String cellValue = getCellData(row.getCell(j));
                    data.put(header[j], cellValue);
                }

                data.put("ROW_ID", i + "");

                if (data != null && data.getOrDefault("RunMode","yes").equalsIgnoreCase("yes"))
                    excelData.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public Map<String, String> getExcelDataByRow(String sheetName, int rowNo) {

        HashMap<String, String> data = new HashMap<>();

        if (rowNo == -1) {
            rowNo = 1;
        }

        try {

            Sheet sheet = workbook.getSheet(sheetName);
            String[] header = getColumnHeaders(sheetName);
            int cols = header.length;

            Row row = sheet.getRow(rowNo-1);

            for (int j = 0; j < cols; j++) {

                String cellValue = getCellData(row.getCell(j));
                data.put(header[j], cellValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void close() {
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
