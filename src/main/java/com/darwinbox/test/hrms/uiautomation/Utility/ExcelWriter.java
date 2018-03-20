package com.darwinbox.test.hrms.uiautomation.Utility;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

	public void writeExcel(String filePath, String fileName, String sheetName, String[] dataToWrite)
			throws IOException {

		File file = new File(filePath + "//" + fileName);
		FileInputStream inputStream = new FileInputStream(file);
		Workbook excelWorkbook = null;
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		if (fileExtensionName.equals(".xlsx")) {
			excelWorkbook = new XSSFWorkbook(inputStream);
		} else if (fileExtensionName.equals(".xls")) {
			excelWorkbook = new HSSFWorkbook(inputStream);
		}

		Sheet sheet = excelWorkbook.getSheet(sheetName);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		Row row = sheet.getRow(0);
		Row newRow = sheet.createRow(rowCount + 1);

		for (int j = 0; j < row.getLastCellNum(); j++) {

			Cell cell = newRow.createCell(j);
			cell.setCellValue(dataToWrite[j]);
		}

		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(file);
		excelWorkbook.write(outputStream);
		outputStream.close();
	}

	/**
	 * This method used to write to Excel
	 * @param valueToWrite
	 * @param sheetName
	 * @throws IOException
	 */
	public static void writeToExcel(String[] valueToWrite) throws IOException {
		ExcelWriter objExcelFile = new ExcelWriter();
		objExcelFile.writeExcel(ResourceHelper.getBaseResourcePath() + "//src//main//resources//TestData",
				"ExportExcel.xlsx", "Leave_Balance", valueToWrite);
	}
	
	/**
	 * This method used to write in Excel
	 * @param valueToWrite
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	public static void writeToExcel(String fileName, String sheetName, String[] valueToWrite ) throws IOException {

		ExcelWriter objExcelFile = new ExcelWriter();
		objExcelFile.writeExcel(ResourceHelper.getBaseResourcePath() + "//src//main//resources//TestData",
				fileName, sheetName, valueToWrite);
	}

}