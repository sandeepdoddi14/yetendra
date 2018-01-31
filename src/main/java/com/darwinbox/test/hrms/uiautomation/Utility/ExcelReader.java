/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.Utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;

/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: ExcelReader.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class ExcelReader extends TestBase{

	public FileOutputStream fileOut = null;
	public String path;
	public FileInputStream fis;
	private static  Workbook ExlWorkbook;
	private static Sheet ExlSheet;
	private static Row ExlRow;
	private static Cell ExlCell;
	
	private static int totalNoOfRows;
	public static String FileName;
	public static String SheetName;
	public static String FilePath;
	
	public static void setExcelFileObject() {
		try {
			FileInputStream inputStream = new FileInputStream(FilePath);
			String fileExtensionName = FilePath.substring(FilePath.indexOf("."));
			if (fileExtensionName.equals(".xlsx")) {
				ExlWorkbook = new XSSFWorkbook(inputStream);
			} else if (fileExtensionName.equals(".xls")) {
				ExlWorkbook = new HSSFWorkbook(inputStream);
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
	
	/**
	 * This Method will return 2D object Data for each record in excel sheet.
	 * 
	 * @param sheetName
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	public String[][] getDataFromSheet(String sheetName, String ExcelName) {
		String dataSets[][] = null;
			try {
				// get sheet from excel workbook
				Sheet sheet = ExlWorkbook.getSheet(sheetName);
				// count number of active tows
				int totalRow = sheet.getLastRowNum() + 1;
				// count number of active columns in row
				int totalColumn = sheet.getRow(0).getLastCellNum();
				// Create array of rows and column
				dataSets = new String[totalRow - 1][totalColumn];
				// Run for loop and store data in 2D array
				// This for loop will run on rows
				for (int i = 1; i < totalRow; i++) {
					Row rows = sheet.getRow(i);
					// This for loop will run on columns of that row
					for (int j = 0; j < totalColumn; j++) {
						// get Cell method will get cell
					Cell cell = rows.getCell(j);
					
						// If cell of type String , then this if condition will work
						if (cell.getCellType() == ExlCell.CELL_TYPE_STRING)
							dataSets[i - 1][j] = cell.getStringCellValue();
						// If cell of type Number , then this if condition will work
						else if (cell.getCellType() == ExlCell.CELL_TYPE_NUMERIC) {
							String cellText = String.valueOf(cell.getNumericCellValue());
							dataSets[i - 1][j] = cellText;
						} else
							// If cell of type boolean , then this if condition will work
							dataSets[i - 1][j] = String.valueOf(cell.getBooleanCellValue());
					}

				}
				return dataSets;
			} catch (Exception e) {
				System.out.println("Exception in reading Xlsx file" + e.getMessage());
				e.printStackTrace();
			}
			return dataSets;
		}


	@SuppressWarnings("deprecation")
	public String getCellData(String sheetName, String colName, int rowNum) {
		try {
			int col_Num = 0;
			int index = ExlWorkbook.getSheetIndex(sheetName);
			ExlSheet = ExlWorkbook.getSheetAt(index);
			Row row = ExlSheet.getRow(0);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				if (row.getCell(i).getStringCellValue().equals(colName)) {
					col_Num = i;
					break;
				}
			}
			row = ExlSheet.getRow(rowNum - 1);
			
			Cell cell = row.getCell(col_Num);
			if (cell.getCellType() == ExlCell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (cell.getCellType() == ExlCell.CELL_TYPE_BLANK) {
				return "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
//	public void setExcelFileObject() {
//		try {
//			FileInputStream inputStream = new FileInputStream(FilePath);
//			String fileExtensionName = FilePath.substring(FilePath.indexOf("."));
//			if (fileExtensionName.equals(".xlsx")) {
//				Workbook = new XSSFWorkbook(inputStream);
//			} else if (fileExtensionName.equals(".xls")) {
//				Workbook = new HSSFWorkbook(inputStream);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
   
	
	public String getTestInput(String columnName) throws Exception {
		try {
			// Access the required test data sheet
			ExlSheet = ExlWorkbook.getSheet(SheetName);	
			int colNum = getColumnNo(columnName);
			String testDataValue = getCellData(colNum, testDataRowNo);
			if (testDataValue.isEmpty()) {
				return null;
			} else {
				return testDataValue;
			}
		} catch (FileNotFoundException e) {
			Reporter("Could not read the Excel sheet", "Fail");
			e.printStackTrace();
		} catch (IOException e) {
			Reporter("Could not read the Excel sheet", "Fail");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (null);
	}

	public String getTestInput(String columnName, int rowNumber) throws Exception {
		String testDataValue = " ";
		try {			
			ExlSheet = ExlWorkbook.getSheet(SheetName);
			int colNum = getColumnNo(columnName);
			testDataValue = getCellData(colNum, rowNumber);
			return testDataValue;
		} catch (FileNotFoundException e) {
			Reporter("Could not read the Excel sheet", "Fail");
			e.printStackTrace();
		} catch (IOException e) {
			Reporter("Could not read the Excel sheet", "Fail");
			e.printStackTrace();
		}
		return testDataValue;
	}

	public int getColumnNo(String columnName) throws Exception {
		try {
			int rowToCheck = 0;
			ExlRow = ExlSheet.getRow(rowToCheck);
			int totalColumns = ExlRow.getLastCellNum();
			int startCol = 0;
			int colNum = 0;

			for (int i = startCol; i <= totalColumns; i++) {
				ExlCell = ExlRow.getCell(i);
				String CellData = ExlCell.getStringCellValue().trim();
				if (CellData.equalsIgnoreCase(columnName)) {
					colNum = i;
					return colNum;
				}
			}
		} catch (Exception e) {
			Reporter("Could not read the Excel sheets column " + columnName, "Fail");
			throw new RuntimeException("Could not read the Excel sheets column " + e.getMessage());
		}
		return 0;
	}

	@SuppressWarnings("deprecation")
	public String getCellData(int ColNum, int rowNumber) throws Exception {
		try {
			ExlRow = ExlSheet.getRow(rowNumber);
			ExlCell = ExlRow.getCell(ColNum);
//			int dataType = Cell.getCellType();
//			if (dataType == 3) {
//				return "";
//			} else {
//				String CellData = Cell.getStringCellValue();
//				return CellData;
//			}
			if (ExlCell.getCellType() == ExlCell.CELL_TYPE_STRING)
				return ExlCell.getStringCellValue();
			// If cell of type Number , then this if condition will work
			else if (ExlCell.getCellType() == ExlCell.CELL_TYPE_NUMERIC) {
				String cellText = String.valueOf(ExlCell.getNumericCellValue());
				return cellText;
			} else if (ExlCell.getCellType() == ExlCell.CELL_TYPE_BLANK) {
				String cellText = "";
				return cellText;
			} else {
				// If cell of type boolean , then this if condition will work
				String cellText = String.valueOf(ExlCell.getBooleanCellValue());
				return cellText;
			}
		} catch (Exception e) {
			Reporter("Exception while getting the Excel cell data: " + e.getMessage(), "Fail");
			throw (e);
		}
	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters

	public void setCellData(String Result, int RowNum, int ColNum) throws Exception {

		try {
			// Row = Sheet.getRow(RowNum);
			ExlCell = ExlRow.getCell(ColNum);
			if (ExlCell == null) {
				ExlCell = ExlRow.createCell(ColNum);
				ExlCell.setCellValue(Result);
			} else {
				ExlCell.setCellValue(Result);
			}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(ResourceHelper.getBaseResourcePath() + "//src//main//resources//testData//TestData.xlsx");
			ExlWorkbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);
		}
	}

	// Excel Code

	public String[][] getExcelData() {
		String[][] arrayExcelData = null;
		try {	
			ExlSheet = ExlWorkbook.getSheet(SheetName);
			int totalNoOfCols = 2;
			totalNoOfRows = ExlSheet.getLastRowNum() + 1;
			arrayExcelData = new String[totalNoOfRows - 1][totalNoOfCols];
			for (int i = 1; i < totalNoOfRows; i++) {
				for (int j = 0; j < totalNoOfCols; j++) {
					ExlCell = ExlSheet.getRow(i).getCell(j);
					arrayExcelData[i - 1][j] = ExlCell.getStringCellValue();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return arrayExcelData;
	}
	
	public int getTotalNoOfRows() {
		return totalNoOfRows;
	}
}
