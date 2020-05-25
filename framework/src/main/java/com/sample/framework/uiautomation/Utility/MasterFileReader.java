package com.sample.framework.uiautomation.Utility;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.HashMap;

public class MasterFileReader {

    private String fileName;
    private String sheetName;
    private Workbook excelWorkbook;
    private Sheet sheet;
    public ExcelReader reader;

    public MasterFileReader(String fileName, String sheetName) {
        this.fileName = fileName;
        this.sheetName = sheetName;
    }

    public void setExcelFileObject() {
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            if (fileName.endsWith(".xlsx")) {
                excelWorkbook = new XSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xls")) {
                excelWorkbook = new HSSFWorkbook(inputStream);
            } else {
                throw new Exception(" Invalid file extension fo parsing ");
            }
        }catch (NullPointerException ne){
            throw new RuntimeException("File Name or Sheet Name is not defined.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getColumnHeaders(String sheetName) {

        sheet = excelWorkbook.getSheet(sheetName);
        Row row = sheet.getRow(0);

        int cols = row.getLastCellNum();
        String[] header = new String[cols];

        for (int j = 0; j < cols; j++)
            header[j] = row.getCell(j).getStringCellValue();
        return header;
    }


    public void getDataFromMasterSheet(String methodName) {

        try {
            sheet = excelWorkbook.getSheet(sheetName);
            int rows = sheet.getLastRowNum();
            String[] header = getColumnHeaders(sheetName);
            int cols = header.length;
            HashMap<String, String> data = new HashMap<>();

            for (int i = 1; i <= rows; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < cols; j++) {

                    String cellValue = getCellData(row.getCell(j));
                    if (cellValue.contains(methodName)) {
                        data.put(header[j + 1], getCellData(row.getCell(j + 1)));
                        data.put(header[j + 2], getCellData(row.getCell(j + 2)));
                        data.put(header[j + 3], getCellData(row.getCell(j + 3)));
                        i = rows+1;
                        break;
                    }
                }
            }

            if (data != null) {
                reader = new ExcelReader();
                reader.setFilenameAndSheetName(data);
            } else {
                throw new RuntimeException("Class name is not defined");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Class name is not defined: " + e);
        }
    }

    public static String getCellData(Cell cell) {

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
}
