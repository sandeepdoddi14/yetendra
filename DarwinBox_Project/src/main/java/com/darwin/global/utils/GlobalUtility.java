package com.darwin.global.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class GlobalUtility {

	/** Set the Working User Directory */
	public static String usrdir = System.getProperty("user.dir");
	public static ExtentReports extent = null;
	public static ExtentTest xtReportLog = null;
	public static ExtentHtmlReporter htmlReporter = null;
	public static Logger logger = Logger.getLogger("Logger");
	public static int rowForTestData = 0;

	/**
	 * This method retrieves data from properties file
	 * 
	 * @param popertiesFileName,Key
	 * @return null
	 * @author shikhar
	 * @return
	 * @since 25/10/2017
	 */
	public static String config(String propfile, String key) {

		File file = new File(usrdir + "//src//main//resources//propertiesFile//" + propfile + ".properties");

		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties prop = new Properties();
		// load properties file
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (prop.getProperty(key));
	}

	/**
	 * This method used for Reporting purpose
	 * 
	 * @param text
	 * @param status
	 * @author shikhar
	 */
	public static void Reporter(String text, String status) {
		if (status.equalsIgnoreCase("Pass")) {
			xtReportLog.log(Status.PASS, text);
			logger.info(text);
		} else if (status.equalsIgnoreCase("Fail")) {
			xtReportLog.log(Status.FAIL, text);
			logger.error(text);
		} else {
			xtReportLog.log(Status.INFO, text);
			logger.info(text);
		}
		Reporter.log(text, true);

	}

	public static String getRandomNumber() throws Exception {
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(100);
		return String.valueOf(randomInt);
	}

	/**
	 * 
	 */

	private static Sheet ExcelWSheet;
	private static Workbook ExcelWBook;
	private static Cell ExcelWCell;
	private static Row ExcelWRow;
	public static String CellData;

	static String FileName = "TestData";
	static String SheetName;
	static String FilePath;
	public static int totalNoOfRows;

	protected static void setExcelFileName(String fileName) {
		FileName = fileName;
		FilePath = usrdir + "//src//main//resources//testData//" + FileName + ".xlsx";
	}

	protected static void setExcelSheetName(String sheetName) {
		SheetName = sheetName;
	}

	protected static String getExcelFilePath() {
		return FilePath;
	}

	protected static String getExcelSheetName() {
		return SheetName;
	}

	/**
	 * 
	 */
	public static void setExcelFileObject() {
		try {
			FileInputStream inputStream = new FileInputStream(FilePath);
			String fileExtensionName = FilePath.substring(FilePath.indexOf("."));
			if (fileExtensionName.equals(".xlsx")) {
				ExcelWBook = new XSSFWorkbook(inputStream);
			} else if (fileExtensionName.equals(".xls")) {
				ExcelWBook = new HSSFWorkbook(inputStream);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String excelInput(String columnName) throws Exception {
		try {
			// Access the required test data sheet
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			int colNum = getColumnNo(columnName);
			String testDataValue = getCellData(colNum, rowForTestData);
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
		}
		return (null);
	}

	public String excelInput(String columnName, int rowNumber) throws Exception {
		String[][] tabArray = null;
		try {
			ExcelWSheet = ExcelWBook.getSheet("Sheet1");
			int colNum = getColumnNo(columnName);
			String testDataValue = getCellData(colNum, rowNumber);
			return testDataValue;
		} catch (FileNotFoundException e) {
			Reporter("Could not read the Excel sheet", "Fail");
			e.printStackTrace();
		} catch (IOException e) {
			Reporter("Could not read the Excel sheet", "Fail");
			e.printStackTrace();
		}
		return (CellData);
	}

	public static int getColumnNo(String columnName) throws Exception {
		try {
			int rowToCheck = 0;
			ExcelWRow = ExcelWSheet.getRow(rowToCheck);
			int totalColumns = ExcelWRow.getLastCellNum();
			int startCol = 0;
			int colNum = 0;

			for (int i = startCol; i <= totalColumns; i++) {
				ExcelWCell = ExcelWRow.getCell(i);
				String CellData = ExcelWCell.getStringCellValue().trim();
				if (CellData.equalsIgnoreCase(columnName)) {
					colNum = i;
					return colNum;
				}
			}
		} catch (Exception e) {
			Reporter("Could not read the Excel sheets column " + columnName, "Fail");
			throw (e);
		}
		return 0;
	}

	public static String getCellData(int ColNum, int rowNumber) throws Exception {
		try {
			ExcelWRow = ExcelWSheet.getRow(rowNumber);
			ExcelWCell = ExcelWRow.getCell(ColNum);
			int dataType = ExcelWCell.getCellType();
			if (dataType == 3) {
				return "";
			} else {
				String CellData = ExcelWCell.getStringCellValue();
				return CellData;
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
			// Row = ExcelWSheet.getRow(RowNum);
			ExcelWCell = ExcelWRow.getCell(ColNum);
			if (ExcelWCell == null) {
				ExcelWCell = ExcelWRow.createCell(ColNum);
				ExcelWCell.setCellValue(Result);
			} else {
				ExcelWCell.setCellValue(Result);
			}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(usrdir + "//src//main//resources//testData//TestData.xlsx");
			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);
		}
	}

	// Excel Code

	public static String[][] getExcelData(String filePath, String Sheet) {
		String[][] arrayExcelData = null;
		try {
			FileInputStream fs = new FileInputStream(filePath);
			ExcelWBook = new XSSFWorkbook(fs);
			ExcelWSheet = ExcelWBook.getSheet(Sheet);
			int totalNoOfCols = 2;
			totalNoOfRows = ExcelWSheet.getLastRowNum() + 1;
			arrayExcelData = new String[totalNoOfRows - 1][totalNoOfCols];
			for (int i = 1; i < totalNoOfRows; i++) {
				for (int j = 0; j < totalNoOfCols; j++) {
					ExcelWCell = ExcelWSheet.getRow(i).getCell(j);
					arrayExcelData[i - 1][j] = ExcelWCell.getStringCellValue();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arrayExcelData;
	}


	@DataProvider(name = "TestRuns")
	public static Object[][] DataProviderClass() throws Exception {
		String filePath = getExcelFilePath();
		String sheetName = getExcelSheetName();
		Object[][] testObjArray = getExcelData(filePath, sheetName);		
		return (testObjArray);
	}

	/**
	 * This method returns a driver instance
	 * @param os
	 * @param browser
	 * @return
	 */
	protected static WebDriver getDriverInstance(String os, String browser) {
		try {
			WebDriver driver;
			if (os.trim().equals("linux")) {
				if (browser.equals("chrome")) {
					System.setProperty("webdriver.chrome.driver",
							usrdir + "//src//main//resources//drivers//linux//chromedriver");
					driver = new ChromeDriver();
					return driver;
				} else if (browser.equals("firefox")) {
					System.setProperty("webdriver.firefox.marionette",
							usrdir + "//src//main//resources//drivers//linux//geckodriver");
					DesiredCapabilities capabilities = DesiredCapabilities.firefox();
					capabilities.setCapability("marionette", true);
					driver = new FirefoxDriver(capabilities);
					return driver;
				} else {
					System.setProperty("webdriver.chrome.driver",
							usrdir + "//src//main//resources//drivers//linux//chromedriver");
					driver = new ChromeDriver();
					return driver;
				}
			} else if (os.trim().equals("windows")) {
				if (browser.equals("chrome")) {
					System.setProperty("webdriver.chrome.driver",
							usrdir + "//src//main//resources//drivers//windows//chromedriver.exe");
					driver = new ChromeDriver();
					return driver;
				} else if (browser.equals("firefox")) {
					System.setProperty("webdriver.firefox.marionette",
							usrdir + "//src//main//resources//drivers//windows//geckodriver.exe");
					DesiredCapabilities capabilities = DesiredCapabilities.firefox();
					capabilities.setCapability("marionette", true);
					driver = new FirefoxDriver(capabilities);
					return driver;
				} else {
					System.setProperty("webdriver.chrome.driver",
							usrdir + "//src//main//resources//drivers//windows//chromedriver.exe");
					driver = new ChromeDriver();
					return driver;
				}
			} else if (os.trim().equals("mac")) {
				if (browser.equals("chrome")) {
					System.setProperty("webdriver.chrome.driver",
							usrdir + "//src//main//resources//drivers//mac//chromedriver.exe");
					driver = new ChromeDriver();
					return driver;
				} else if (browser.equals("firefox")) {
					System.setProperty("webdriver.firefox.marionette",
							usrdir + "//src//main//resources//drivers//windows//geckodriver.exe");
					DesiredCapabilities capabilities = DesiredCapabilities.firefox();
					capabilities.setCapability("marionette", true);
					driver = new FirefoxDriver(capabilities);
					return driver;
				} else {
					System.setProperty("webdriver.chrome.driver",
							usrdir + "//src//main//resources//drivers//mac//chromedriver.exe");
					driver = new ChromeDriver();
					return driver;
				}
			} else {
				throw new RuntimeException("Exception while instantiating driver");
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception while instantiating driver");
		}
	}
	
	
	//
	// public String getCellData(int RowNum, int ColNum) throws Exception {
	// try {
	// Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
	// int dataType = Cell.getCellType();
	// if (dataType == 3) {
	// return "";
	// } else {
	// String CellData = Cell.getRawValue();
	// CellData.toString();
	// return CellData;
	// }
	// } catch (Exception e) {
	// System.out.println(e.getMessage());
	// throw (e);
	// }
	// }
}
