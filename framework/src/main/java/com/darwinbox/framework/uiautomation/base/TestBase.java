package com.darwinbox.framework.uiautomation.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

import com.darwinbox.framework.beans.Configuration;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.MasterFileReader;
import com.darwinbox.framework.uiautomation.Utility.ResourceHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.utils.FileUtilsSerialized;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;


import com.darwinbox.framework.uiautomation.configuration.browser.BrowserType;
import com.darwinbox.framework.uiautomation.configuration.browser.ChromeBrowser;
import com.darwinbox.framework.uiautomation.configuration.browser.FirefoxBrowser;
import com.darwinbox.framework.uiautomation.configuration.browser.HtmlUnitBrowser;
import com.darwinbox.framework.uiautomation.configuration.browser.IExploreBrowser;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class TestBase implements IRetryAnalyzer {

	public Logger log = Logger.getLogger(TestBase.class) ;
	public static Configuration config;
	public static String datapath, respath;
	public static String resultsDir;

	public static WebDriver driver;
	public static MasterFileReader ms;
	public static boolean onLaunch = false;

	public static ExtentReports extent = new ExtentReports();
	public static ExtentTest xtReportLog = null;
	ExtentTest parentLog = null;

	public static Map<String, String> data = new HashMap<>();
	public static List<Map<String, String>> dataItem = new ArrayList<>();


	public static int dataCounter = 0;
	public static int currentData = 0;
	private static int retryCnt=0,maxRetryCnt;

	public static Markup strcode = MarkupHelper.createCodeBlock("text");
	public static String WriteResultToExcel = "No";

	public static int counter = 0;


	@BeforeSuite
	public void initSuite() {

		datapath = System.getProperty("testdata");
		respath = System.getProperty("resources");
		config = (FileUtilsSerialized.readFromFile(datapath + "config"));

		String sdft = new SimpleDateFormat("YYYYMMdd_HHmmss").format(new Date());
		resultsDir = datapath  +  "TestResults"+File.separator+"Results_" + sdft;
		UtilityHelper.createDir(resultsDir);

		PropertyConfigurator.configure((respath+ "log4j.properties"));

		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(resultsDir + File.separator + "TestExecutionReport.html");
		htmlReporter.setAppendExisting(true);
		htmlReporter.config().setChartVisibilityOnOpen(false);
		htmlReporter.loadXMLConfig(new File(respath+ "extent-config.xml"));

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Browser", config.getBrowser());
		extent.setSystemInfo("Instance", config.getInstance());
		extent.setReportUsesManualConfiguration(true);

		htmlReporter = new ExtentHtmlReporter( resultsDir + File.separator + "TestReport" + sdft + ".html");
		htmlReporter.setAppendExisting(true);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.loadXMLConfig(new File(respath + "extent-config.xml"));
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Environment", config.getInstance());
		extent.setSystemInfo("Browser", config.getBrowser());
		extent.setSystemInfo("Host Name", "DarwinBox_Automation_Team");
		extent.setReportUsesManualConfiguration(true);

		List statusHierarchy = Arrays.asList( Status.ERROR, Status.FAIL, Status.FATAL, Status.WARNING ,Status.PASS,
				Status.SKIP, Status.DEBUG, Status.INFO);

		extent.config().statusConfigurator().setStatusHierarchy(statusHierarchy);
		ms = new MasterFileReader(config.getTestDataFile(),"TestCaseMaster");
		ms.setExcelFileObject();

		readInstanceDetails();

		init();

		onLaunch = true;
		retryCnt++;

		log.info(" Test suite initialized ");
	}

	private void init() {
		if (onLaunch && retryCnt != 1) {
			try{
				driver.quit();
			}finally {
				System.exit(0);
			}
		}
		try {
			counter = 0;
			setUpDriver(BrowserType.valueOf(config.getBrowser().toUpperCase()));
			log.info(config.getBrowser());
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while initlization :" + e.getMessage(), "Error");
			log.info("Browser didn't initialise. No tests run occured");
		}
	}

	private void readInstanceDetails() {

		log.debug(" Reading instance details from config.ini ");
		HierarchicalINIConfiguration properties = null;

		String fileName = respath + "config.ini";

		try {
			properties = new HierarchicalINIConfiguration(fileName);
		} catch (Exception e) {
			log.error(" Exception while reading properties from config.ini ");
			log.error(" Exiting system as reading config failed ");
			log.error(e.getMessage());
			e.printStackTrace();
			System.exit(0);
		}


		SubnodeConfiguration c = properties.getSection(config.getInstance());

		String url = (c.getProperty("url").toString());


		data.put("@@url",url);
		data.put("@@admin", (c.getProperty("admin").toString()));
		data.put("@@password", (c.getProperty("password").toString()));

		data.put("@@parent", ( (c.getProperty("parent") == null) ? "" : c.getProperty("parent").toString() ));
		data.put("@@group", ( (c.getProperty("group") == null) ? " " : c.getProperty("group").toString() ));


		log.info(" Instance URL ----> " + url);

	}

	private void reinit() {
		try {
			driver.quit();
		} catch (Exception e) {

		}
		init();
	}

	private void setUpDriver(BrowserType bType) {
		try {
			driver = getBrowserObject(bType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private WebDriver getBrowserObject(BrowserType bType) throws Exception {
		try {
			log.info(bType);

			switch (bType) {

				case CHROME:
					ChromeBrowser chrome = ChromeBrowser.class.newInstance();
					return chrome.getChromeDriver(chrome.getChromeCapabilities());

				case FIREFOX:
					FirefoxBrowser firefox = FirefoxBrowser.class.newInstance();
					return firefox.getFirefoxDriver(firefox.getFirefoxCapabilities());

				case HTMLUNIT:
					HtmlUnitBrowser htmlUnit = HtmlUnitBrowser.class.newInstance();
					return htmlUnit.getHtmlUnitDriver(htmlUnit.getHtmlUnitDriverCapabilities());

				case IEXPLORE:
					IExploreBrowser iExplore = IExploreBrowser.class.newInstance();
					return iExplore.getIExplorerDriver(iExplore.getIExplorerCapabilities());
				default:
					throw new Exception(" Driver Not Found : " + bType);
			}
		} catch (Exception e) {
			log.fatal(e);
			throw e;
		}
	}

	@BeforeClass
	public void beforeClass() {
		try {
			parentLog = extent.createTest((this.getClass().getSimpleName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		currentData = 0;
	}

	@BeforeMethod()
	public void beforeMethod(Method method) {
		try {
			gotoHomePage();
			data.putAll(dataItem.get(currentData++));
			dataCounter = dataItem.size();
			Reporter.log("*****" + method.getName() + ":" + data.get("Test_Description") + "****", true);
			if (dataCounter >= 2)
				xtReportLog = parentLog.createNode(data.get("Test_Description"))
						.assignCategory(this.getClass().getPackage().toString()
								.substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1));
			else {
				xtReportLog = parentLog.assignCategory(this.getClass().getPackage().toString()
						.substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1));
			}

			log.info("******************************************************************************");
			log.info("*********** " + (this.getClass().getPackage().toString()
					.substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1) + "::"
					+ this.getClass().getSimpleName() + " ******"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception in Before Method" + e.getMessage());
		}

	}

	@AfterMethod()
	public void afterMethod(ITestResult result) {
		try {
			getresult(result);
			extent.flush();
		} catch (Exception e) {
			Reporter("Exception in @AfterMethod: " + e, "Error", log);
		}
	}

	@AfterClass(alwaysRun = true)
	public void endTest() {
		gotoHomePage();
	}

	public void gotoHomePage() {
		try{
			driver.manage().deleteAllCookies();
			driver.get(data.get("@@url"));
			driver.manage().deleteAllCookies();
			counter++;
			if ( counter == 500 ) {
				reinit();
				gotoHomePage();
			}
		} catch (Exception e) {
			log.info(" Driver object exception. Creating new session and closing existing one");
			reinit();
			gotoHomePage();
		}
	}

	@AfterSuite
	public void closeBrowser() {
		try {
			driver.close();
			driver.quit();
		} catch (NoSuchSessionException e) {
			log.info("browser closed");
		}
	}

	public void getresult(ITestResult result) {
		try {

			File screenShotName;
			String tempPath = resultsDir + File.separator + "Screenshots" + File.separator + dataItem.get(currentData - 1).get("TestCaseName");
			if (result.getStatus() == ITestResult.SUCCESS) {
				xtReportLog.log(Status.PASS, result.getName() + " test is pass");
			} else if (result.getStatus() == ITestResult.SKIP) {
				log.debug(result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
			} else if (result.getStatus() == ITestResult.FAILURE) {

				xtReportLog.log(Status.FAIL, result.getName() + " test is failed");
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				String scrPath = tempPath + "-" + "FAIL" + "-" + DateTimeHelper.getCurrentLocalDateAndTime().replace(":", "_") + ".png";
				screenShotName = new File(scrPath);
				FileUtils.copyFile(scrFile, screenShotName);
				String filePath = screenShotName.toString();
				xtReportLog.warning("Failure Screenshot",
						MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
				log.error(result.getName() + " test is failed" + result.getThrowable());
				// xtReportLog.fail(result.getThrowable());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String takeScreenShot(String name) throws IOException {

		if (driver instanceof HtmlUnitDriver) {
			log.fatal("HtmlUnitDriver Cannot take the ScreenShot");
			return "";
		}

		File destDir = new File(ResourceHelper.getResourcePath("screenshots/") + DateTimeHelper.getCurrentLocalDateAndTime());
		if (!destDir.exists())
			destDir.mkdir();

		File destPath = new File(destDir.getAbsolutePath() + System.getProperty("file.separator") + name + ".jpg");
		try {
			FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE), destPath);
		} catch (IOException e) {
			log.error(e);
			throw e;
		}
		log.info(destPath.getAbsolutePath());
		return destPath.getAbsolutePath();
	}

	/**
	 * This method used to take Screenshot
	 *
	 * @param driver
	 * @param result
	 * @param folderName
	 */
	public void getScreenShot(WebDriver driver, ITestResult result, String folderName) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		String methodName = result.getName();

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ "/src/main/java/com/test/automation/uiAutomation/";
			File destFile = new File((String) reportDirectory + "/" + folderName + "/" + methodName + "_"
					+ formater.format(calendar.getTime()) + ".png");

			FileUtils.copyFile(scrFile, destFile);

			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getScreenShotOnSucess(WebDriver driver, ITestResult result) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		String methodName = result.getName();

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ "/src/main/java/com/test/automation/uiAutomation/";
			File destFile = new File((String) reportDirectory + "/failure_screenshots/" + methodName + "_"
					+ formater.format(calendar.getTime()) + ".png");

			FileUtils.copyFile(scrFile, destFile);

			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String captureScreen(String fileName) {
		if (fileName == "") {
			fileName = "blank";
		}
		File destFile = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			String reportDirectory = new File(System.getProperty("")).getAbsolutePath()
					+ "/src/main/java/com/test/automation/uiAutomation/screenshot/";
			destFile = new File(
					(String) reportDirectory + fileName + "_" + formater.format(calendar.getTime()) + ".png");
			FileUtils.copyFile(scrFile, destFile);
			// This will help us to link the screen shot in testNG report
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destFile.toString();
	}

	/**
	 * This method is used for reporting in extent report
	 *
	 * @param text
	 * @param status
	 */
	public void Reporter(String text, String status) {
		if (status.equalsIgnoreCase("Pass")) {
			xtReportLog.log(Status.PASS, text);
			log.info(text);
		} else if (status.equalsIgnoreCase("Fail")) {
			xtReportLog.log(Status.FAIL, text);
			log.error(text);
		} else if (status.equalsIgnoreCase("Fatal")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else if (status.equalsIgnoreCase("Error")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else {
			xtReportLog.log(Status.INFO, text);
			log.info(text);
		}
		Reporter.log(text);
	}

	public void Reporter(String text, String status, Logger log) {
		if (status.equalsIgnoreCase("Pass")) {
			xtReportLog.log(Status.PASS, text);
			log.info(text);
		} else if (status.equalsIgnoreCase("Fail")) {
			xtReportLog.log(Status.FAIL, text);
			log.error(text);
		} else if (status.equalsIgnoreCase("Skip")) {
			xtReportLog.log(Status.SKIP, text);
			log.debug(text);
		} else if (status.equalsIgnoreCase("Fatal")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else if (status.equalsIgnoreCase("Error")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else {
			xtReportLog.log(Status.INFO, text);
			log.info(text);
		}
		Reporter.log(text);
	}

	public void ReporterForCodeBlock(String text, String status) {

		Markup code = MarkupHelper.createCodeBlock(text);
		if (status.equalsIgnoreCase("Pass")) {
			xtReportLog.log(Status.PASS, code);
			log.info(text);
		} else if (status.equalsIgnoreCase("Fail")) {
			xtReportLog.log(Status.FAIL, code);
			log.error(text);
		} else if (status.equalsIgnoreCase("Fatal")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else if (status.equalsIgnoreCase("Error")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else {
			xtReportLog.log(Status.INFO, code);
			log.info(text);
		}
		Reporter.log(text);
	}

	public void ReporterForLabel(String text, String status) {

		Markup code = MarkupHelper.createLabel(text, ExtentColor.BLUE);
		if (status.equalsIgnoreCase("Pass")) {
			xtReportLog.log(Status.PASS, code);
			log.info(code);
		} else if (status.equalsIgnoreCase("Fail")) {
			xtReportLog.log(Status.FAIL, code);
			log.error(code);
		} else if (status.equalsIgnoreCase("Fatal")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else if (status.equalsIgnoreCase("Error")) {
			xtReportLog.log(Status.ERROR, text);
			log.fatal(text);
		} else {
			xtReportLog.log(Status.INFO, code);
			log.info(code);
		}
		Reporter.log(text);
	}

    public boolean retry(ITestResult result) {
        if (retryCnt < maxRetryCnt) {
            Reporter("Retrying " + result.getName() + " again and the count is " + (retryCnt+1), "Info");
            retryCnt++;
            currentData--;
            return true;
        }
        return false;
    }

	/**
	 * This method gets data specified in excel
	 *
	 * @param key
	 * @return String
	 */
	public String getData(String key) {
		return data.get(key);
	}

}
