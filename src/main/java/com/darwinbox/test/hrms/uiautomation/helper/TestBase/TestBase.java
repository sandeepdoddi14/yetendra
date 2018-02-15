
package com.darwinbox.test.hrms.uiautomation.helper.TestBase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import com.darwinbox.test.hrms.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.ResourceHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.UtilityHelper;
import com.darwinbox.test.hrms.uiautomation.configreader.ObjectRepo;
import com.darwinbox.test.hrms.uiautomation.configreader.PropertyFileReader;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.BrowserType;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.ChromeBrowser;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.FirefoxBrowser;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.HtmlUnitBrowser;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.IExploreBrowser;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

/**
 * @author balaji
 * @Creation_Date: 20 Nov 2017
 * @ClassName: TestBase.java
 * @LastModified_Date: 20 Nov 2017
 */
public class TestBase {

	private static Logger log ;
	public static WebDriver driver;
	public static ExtentReports extent = new ExtentReports();;
	public static ExtentTest xtReportLog = null;
	public static ExtentHtmlReporter htmlReporter = null;
	public static List<Map<String,String>> dataItem = null;
	public static int dataCounter = 0;
	private static int currentData = 0;
	public static Map<String,String> data;
	ExtentTest parentLog = null;

	public static String resultsDIR = "Test_Execution_Results/Results"+ UtilityHelper.getCurrentDateTime();

	public static void setDataItem(List<Map<String,String>> dataItem) {
		TestBase.dataItem = dataItem;
		dataCounter = dataItem.size();
		currentData = 0;
	}

	@BeforeSuite
	public void startReport() {

		ObjectRepo.reader = new PropertyFileReader();
		String log4jConfPath = ResourceHelper.getResourcePath("/src/main/resources/configfile/log4j.properties");
		PropertyConfigurator.configure(log4jConfPath);
		log = Logger.getLogger(TestBase.class);
		UtilityHelper.CreateADirectory(resultsDIR);
		htmlReporter = new ExtentHtmlReporter(ResourceHelper.getBaseResourcePath()+File.separator +  resultsDIR +File.separator + "Response_Report" + UtilityHelper.getCurrentDateTime()+ ".html");
		htmlReporter.setAppendExisting(true);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.loadXMLConfig(new File(ResourceHelper.getBaseResourcePath()+"/src/main/resources/configfile/extent-config.xml"));

		init();

		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Environment", ObjectRepo.reader.getApplication());
		extent.setSystemInfo("Browser", ObjectRepo.reader.getBrowser().toString());
		extent.setSystemInfo("Host Name", "DawinBox_Automation_Team");
		extent.setReportUsesManualConfiguration(true);
	}

	@BeforeClass
	public void beforeClass() {
		try {
			parentLog = extent.createTest((this.getClass().getSimpleName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod()
	public void beforeMethod(Method method) {
		try {
			gotoHomePage();
			data = dataItem.get(currentData++);
			Reporter.log("*****" + method.getName() + ":" + data.get("Test_Description") +  "****", true);
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
	
		}catch(Exception e){
				e.printStackTrace();
				throw new RuntimeException("Exception in Before Method" + e.getMessage());
			}

	}

	@AfterMethod()
	public void afterMethod(ITestResult result) {
		try{

		getresult(result);
		extent.flush();

	}catch(Exception e){
		Reporter("Exception in @AfterMethod: "+ e, "Fail", log);
	}
	}
	
	@AfterClass(alwaysRun = true)
	public void endTest() {
		gotoHomePage();
	}

	public void gotoHomePage() {
		try {
			driver.manage().deleteAllCookies();
			driver.get(ObjectRepo.reader.getApplication());
			driver.manage().deleteAllCookies();
		}catch(Exception e){
			System.out.println(" Driver object exception. Creating new session and closing existing one");
			reinit();
			gotoHomePage();
		}
	}

	public void reinit(){

		try{
			driver.quit();
		} catch(Exception e){

		}
		init();

	}

	@AfterSuite
	public void closeBrowser() {
		driver.close();
		driver.quit();
		log.info("browser closed");
	}

	public void init() {
		try {
			ObjectRepo.reader = new PropertyFileReader();
			log.info(ObjectRepo.reader.getBrowser());
			setUpDriver(ObjectRepo.reader.getBrowser());
			log.info(ObjectRepo.reader.getBrowser());
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			Reporter("Exception while initlization :" + e.getMessage(), "Fail");
		}
	}

	public void setUpDriver(BrowserType bType) {

		try {
			driver = getBrowserObject(bType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// log.debug("InitializeWebDrive : " + driver.hashCode());

	}

	public WebDriver getBrowserObject(BrowserType bType) throws Exception {
		try {
			log.info(bType);

			switch (bType) {

			case Chrome:
				ChromeBrowser chrome = ChromeBrowser.class.newInstance();
				return chrome.getChromeDriver(chrome.getChromeCapabilities());

			case Firefox:
				FirefoxBrowser firefox = FirefoxBrowser.class.newInstance();
				return firefox.getFirefoxDriver(firefox.getFirefoxCapabilities());

			case HtmlUnitDriver:
				HtmlUnitBrowser htmlUnit = HtmlUnitBrowser.class.newInstance();
				return htmlUnit.getHtmlUnitDriver(htmlUnit.getHtmlUnitDriverCapabilities());

			case Iexplorer:
				IExploreBrowser iExplore = IExploreBrowser.class.newInstance();
				return iExplore.getIExplorerDriver(iExplore.getIExplorerCapabilities());
			default:
				throw new Exception(" Driver Not Found : " + new PropertyFileReader().getBrowser());
			}
		} catch (Exception e) {
			log.equals(e);
			throw e;
		}
	}

	/**
	 * 
	 * @param result
	 */
	public void getresult(ITestResult result) {
		try {
			File screenShotName;
			String tempPath = ResourceHelper.getBaseResourcePath()
					+File.separator + resultsDIR + File.separator + "Screenshots"+File.separator + dataItem.get(currentData-1).get("TestCaseName");
			if (result.getStatus() == ITestResult.SUCCESS) {
				xtReportLog.log(Status.PASS, result.getName() + " test is pass");
				/*	File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					String scrPath = tempPath + "-" + "PASS" + "-" + DateTimeHelper.getCurrentDateTime() + ".png";
					screenShotName = new File(scrPath);
					FileUtils.copyFile(scrFile, screenShotName);
					String filePath = screenShotName.toString();
					xtReportLog.pass("Pass Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
				*/
			}
			 else if (result.getStatus() == ITestResult.SKIP) {
					log.debug(result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
			} else if (result.getStatus() == ITestResult.FAILURE) {

				xtReportLog.log(Status.FAIL, result.getName() + " test is failed");
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				String scrPath = tempPath + "-" + "FAIL" + "-" + DateTimeHelper.getCurrentDateTime() + ".png";
				screenShotName = new File(scrPath);
				FileUtils.copyFile(scrFile, screenShotName);

				String filePath = screenShotName.toString();
				xtReportLog.fail("Failure Screenshot",
						MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
				log.error(result.getName() + " test is failed" + result.getThrowable());
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

		File destDir = new File(ResourceHelper.getResourcePath("screenshots/") + UtilityHelper.getCurrentDate());
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
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
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

	public void Reporter(String text, String status) {
		if (status.equalsIgnoreCase("Pass")) {
			xtReportLog.log(Status.PASS, text);
			log.info(text);
		} else if (status.equalsIgnoreCase("Fail")) {
			xtReportLog.log(Status.FAIL, text);
			log.error(text);
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
		}else if (status.equalsIgnoreCase("Skip")) {
			xtReportLog.log(Status.SKIP, text);
			log.debug(text);
		}  else {
			xtReportLog.log(Status.INFO, text);
			log.info(text);
		}
		Reporter.log(text);
	}

	public String getData(String key){
		return data.get(key);
	}

}
