package com.darwinbox.test.hrms.uiautomation.Utility;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;

/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017
 * @ClassName: ExcelReader.java
 * @LastModified_Date:  02 Feb 2018
 */
public class ExcelReader extends TestBase{

	private static  Workbook ExlWorkbook;
	private static Sheet ExlSheet;
	public static String FileName;
	public static String SheetName;
	public static String FilePath;
	public static int rowNo = -1;

	public static void setExcelFileObject() {
		try {

			FileInputStream inputStream = new FileInputStream(FilePath);
			String fileExtensionName = FilePath.substring(FilePath.indexOf("."));
			if (fileExtensionName.endsWith(".xlsx")) {
				ExlWorkbook = new XSSFWorkbook(inputStream);
			} else if (fileExtensionName.endsWith(".xls")) {
				ExlWorkbook = new HSSFWorkbook(inputStream);
			} else {
				throw new Exception(" Invalid file extension fo parsing ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setFilenameAndSheetName(String fileName, String sheetName){
		FileName = fileName;
		SheetName = sheetName;
		FilePath = ResourceHelper.getBaseResourcePath() + "//src//main//resources//TestData//" + FileName ;
		setExcelFileObject();
	}

	public String[] getColumnHeaders(String sheetName){

		ExlSheet = ExlWorkbook.getSheet(sheetName);
		Row row = ExlSheet.getRow(0);

		int cols = row.getLastCellNum();
		String [] header = new String[cols];

		for (int j = 0; j < cols; j++)
			header[j] = row.getCell(j).getStringCellValue();
		return header;
	}

	public List<Map<String, String>> getExcelData() {

		List<Map<String,String>> excelData = new ArrayList<>();

		if(SheetName.contains(":")) {
			String temp [] = SheetName.split(":");
			SheetName = temp[0];
			rowNo = Integer.parseInt(temp[1]);
			return getExcelDataByRow();
		}

		try {

			ExlSheet = ExlWorkbook.getSheet(SheetName);
			int rows = ExlSheet.getLastRowNum();

			String [] header = getColumnHeaders(SheetName);
			int cols = header.length;

			for (int i = 1; i <= rows; i++) {

				Row row = ExlSheet.getRow(i);

				HashMap<String,String> data = new HashMap<>();

				for (int j = 0; j < cols; j++) {

					String cellValue = getCellData(row.getCell(j));
					if( j == 2) {

						if (cellValue.toLowerCase().equalsIgnoreCase("no")){
							data = null;
							break;
						}
					}
					data.put(header[j],cellValue);
				}

				if(data != null)
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
				cellText = cell.getNumericCellValue()+"";
			else if (type == CellType.BOOLEAN)
				cellText = cell.getBooleanCellValue()+"";
			else
				cellText = "";

		} catch (Exception e) {

		}

		return cellText;
	}

	public List<Map<String, String>> getExcelDataByRow() {

		List<Map<String,String>> excelData = new ArrayList<>();

		if( rowNo == -1){
			rowNo = 1;
		}
		try {

			ExlSheet = ExlWorkbook.getSheet(SheetName);

			String [] header = getColumnHeaders(SheetName);
			int cols = header.length;

			Row row = ExlSheet.getRow(rowNo);

			HashMap<String,String> data = new HashMap<>();

			for (int j = 0; j < cols; j++) {

				String cellValue = getCellData(row.getCell(j));
				data.put(header[j],cellValue);
			}


			excelData.add(data);
			rowNo = -1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelData;
	}
}