package com.darwinbox.framework.uiautomation.Utility;

import java.io.File;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;

import com.darwinbox.framework.uiautomation.base.TestBase;

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
	 * This method copies Export File to Test_Exceution Results folder
	 */
	public static void copyExportFileToResultsDir() {
		try {
			String scrPath = ResourceHelper.getBaseResourcePath()
					+ "//src//main//resources//TestData//ExportExcel.xlsx";
			String targetPath = TestBase.resultsDir;
			File sourceFile = new File(scrPath);
			String name = sourceFile.getName();

			File targetFile = new File(targetPath);
			FileUtils.copyFileToDirectory(sourceFile, targetFile);

		} catch (Exception e) {
			throw new RuntimeException("Exception while copying file");
		}
	}

	/**
	 * This method used to write to Excel
	 * 
	 * @param valueToWrite
	 * @param sheetName
	 * @throws IOException
	 */
	public static void writeToExcel(String path, String fileName, String sheetName, String[] valueToWrite)
			throws IOException {
		ExcelWriter objExcelFile = new ExcelWriter();
		objExcelFile.writeExcel(path, fileName, sheetName, valueToWrite);
	}

	/**
	 * This method used to write in Excel
	 * 
	 * @param valueToWrite
	 * @param fileName
	 * @param sheetName
	 * @throws IOException
	 */
	public static void writeToExcel(String fileName, String sheetName, String[] valueToWrite) throws IOException {

		ExcelWriter objExcelFile = new ExcelWriter();
		objExcelFile.writeExcel(ResourceHelper.getBaseResourcePath() + "//src//main//resources//TestData", fileName,
				sheetName, valueToWrite);
	}

	/**
	 * This method deletes the file if exists
	 * @param path
	 */
	public void deleteFileIfExists(String path) {
		try {
			Files.deleteIfExists(Paths.get(path));			
		}catch(NoSuchFileException e) {
			Reporter.log("File '"+ path +"' does not exits.", true);
		}catch(DirectoryNotEmptyException e)
        {
            Reporter.log("Directory is not empty.", true);
        }
        catch(IOException e)
        {
            Reporter.log("Invalid permissions.", true);
        }
	}
	

    public static void csvFileWriter(List<String[]> all, String path, String fileName) {
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
        try {
            File file = new File(path +"/" + fileName+ ".csv");
            String filePath = file.getAbsolutePath();
            fileWriter = new FileWriter(filePath);
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
            for (Object[] data : all) {
                csvFilePrinter.printRecord(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}