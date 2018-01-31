/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.helper.TestBase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.darwinbox.test.hrms.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelReader;
import com.darwinbox.test.hrms.uiautomation.Utility.ResourceHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.UtilityHelper;
import com.darwinbox.test.hrms.uiautomation.configreader.ObjectRepo;
import com.darwinbox.test.hrms.uiautomation.configreader.PropertyFileReader;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.BrowserType;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.ChromeBrowser;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.FirefoxBrowser;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.HtmlUnitBrowser;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.IExploreBrowser;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;
//import com.relevantcodes.extentreports.ExtentReports;
//import com.relevantcodes.extentreports.ExtentTest;
//import com.relevantcodes.extentreports.LogStatus;
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
	GenericHelper objHelper;
	public WebDriver driver;
	public static ExtentReports extent = null;
	public static ExtentTest xtReportLog = null;
	public static ExtentHtmlReporter htmlReporter = null;
	ExtentTest parentLog = null;
	public static int testDataRowNo = 0;
	public ITestResult result;

	public static ExcelReader excel;
	

	@BeforeSuite
	public void startReport() {
		excel = new ExcelReader();
		ObjectRepo.reader = new PropertyFileReader();
		String log4jConfPath = ResourceHelper.getResourcePath("/src/main/resources/configfile/log4j.properties");
		PropertyConfigurator.configure(log4jConfPath);
		log = Logger.getLogger(TestBase.class);
		UtilityHelper.CreateADirectory("//target//TestExecution_Report");
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")
				+ "//target//TestExecution_Report//STMExtentReport" + UtilityHelper.getCurrentDateTime() + ".html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		htmlReporter.setAppendExisting(true);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.loadXMLConfig(
				new File(System.getProperty("user.dir") + "//src//main//resources//configfile//extent-config.xml"));
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
			testDataRowNo = 0;
			parentLog = extent.createTest((this.getClass().getSimpleName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeMethod()
	public void beforeMethod(Method method) throws Exception {
		try {		
			testDataRowNo++;
			Reporter.log("*****" + method.getName() + excel.getTestInput("Test_Description") + "****", true);
			if (excel.getTestInput("RunMode").equalsIgnoreCase("Yes")) {
				init();
				Reporter.log("*****" + method.getName() + "****", true);

				if (excel.getTotalNoOfRows() > 2) {
					xtReportLog = parentLog.createNode(excel.getTestInput("Test_Description"))
							.assignCategory(this.getClass().getPackage().toString()
									.substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1));
				} else {
					xtReportLog = parentLog.assignCategory(this.getClass().getPackage().toString()
							.substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1));
				}
				log.info("******************************************************************************");
				log.info("*********** " + (this.getClass().getPackage().toString()
						.substring(this.getClass().getPackage().toString().lastIndexOf(".") + 1) + "::"
						+ this.getClass().getSimpleName() + " ******"));

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception in Before Method" + e.getMessage());
		}
	}

	@AfterMethod()
	public void afterMethod(ITestResult result) {
		try{
		if (excel.getTestInput("RunMode").equalsIgnoreCase("Yes")) {
		getresult(result);
		extent.flush();
		closeBrowser();
		}
	}catch(Exception e){
		Reporter("Exception in @AfterMethod: "+ e, "Fail", log);
	}
	}
	
	@AfterClass(alwaysRun = true)
	public void endTest() {
//	closeBrowser();
	}

	@AfterSuite
	public void closeAll() {
		try {
			driver.close();
			driver.quit();
			Reporter("All browser sessions are closed", "Pass");
		} catch (NoSuchSessionException e) {
			Reporter("All browser sessions are closed", "Pass");
		}
	}

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

	public boolean launchApplication() {
		log.info("navigating to :" + ObjectRepo.reader.getApplication());
		try {
			driver.get(ObjectRepo.reader.getApplication());
			driver.manage().timeouts().pageLoadTimeout(ObjectRepo.reader.getPageLoadTimeOut(), TimeUnit.SECONDS);
			Reporter("Launched Application successfully", "Pass");
			return true;
		} catch (Exception e) {
			Reporter("Exception while launching Application ", "Fail");
			return false;
		}
	}

	/**
	 * 
	 * @param result
	 */
	public void getresult(ITestResult result) {
		try {
			File screenShotName;
			if (result.getStatus() == ITestResult.SUCCESS) {
				xtReportLog.log(Status.PASS, result.getName() + " test is pass");
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				String scrPath = ResourceHelper.getBaseResourcePath()
						+ "//src//main//java//com.darwinbox.test.hrms.uiautomation.Screenshots//" + result.getName()
						+ "-" + "PASS" + "-"
						+ /* Arrays.toString(testResult.getParameters()) + */ DateTimeHelper.getCurrentDateTime()
						+ ".png";
				screenShotName = new File(scrPath);
				FileUtils.copyFile(scrFile, screenShotName);
				String filePath = screenShotName.toString();
				xtReportLog.pass("Pass Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
			} else if (result.getStatus() == ITestResult.SKIP) {
				log.debug(result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
			} else if (result.getStatus() == ITestResult.FAILURE) {
				//Reporter(" " + result.getThrowable(), "Fail");
				xtReportLog.log(Status.FAIL, result.getName() + " test is failed");
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				String scrPath = ResourceHelper.getBaseResourcePath()
						+ "//src//main//java//com.darwinbox.test.hrms.uiautomation.Screenshots//" + result.getName()
						+ "-" + "FAIL" + "-"
						+ /* Arrays.toString(testResult.getParameters()) + */ DateTimeHelper.getCurrentDateTime()
						+ ".png";
				screenShotName = new File(scrPath);
				FileUtils.copyFile(scrFile, screenShotName);
				String filePath = screenShotName.toString();
				xtReportLog.fail("Failure Screenshot",
						MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
				log.error(result.getName() + " test is failed" + result.getThrowable());
//			} else if (result.getStatus() == ITestResult.STARTED) {
//				xtReportLog.log(Status.INFO, result.getName() + " test is started");
//				log.info(result.getName() + " test is started");
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
	
	/*
	 * @SuppressWarnings("unused") private Function<WebDriver, Boolean>
	 * elementLocated(final WebElement element) { return new Function<WebDriver,
	 * Boolean>() {
	 * 
	 * public Boolean apply(WebDriver driver) { log.debug("Waiting for Element : " +
	 * element); return element.isDisplayed(); } }; }
	 *//**
		 * Check for element is present based on locator If the element is present
		 * return the web element otherwise null
		 * 
		 * @param locator
		 * @return WebElement or null
		 */
	/*
	 * 
	 * public WebElement getElementWithNull(By locator) { log.info(locator); try {
	 * return driver.findElement(locator); } catch (NoSuchElementException e) { //
	 * Ignore } return null; }
	 */

	/**
	 * this methos is usefull if we use Selenium Grid
	 * 
	 * @param browser
	 * @throws IOException
	 * @author balaji 20 Nov 201713:05:45
	 */
	// @Parameters("browser")
	// @BeforeTest
	public void launchapp(String browser) throws IOException {

		if (System.getProperty("os.name").contains("Mac")) {
			if (browser.equals("chrome")) {
				// System.setProperty("webdriver.chrome.driver",
				// System.getProperty("user.dir") + "/drivers/chromedriver");
				System.out.println(" Executing on CHROME");
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				String Node = "http://localhost:5001/wd/hub";
				driver = new RemoteWebDriver(new URL(Node), cap);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				// Launch website
				// loadData();
				// getUrl(OR.getProperty("url"));
			} else if (browser.equals("firefox")) {
				// System.setProperty("webdriver.gecko.driver",
				// System.getProperty("user.dir") + "/drivers/geckodriver.exe");
				System.out.println(" Executing on FireFox");
				String Node = "http://111.11.11.11:5000/wd/hub";
				DesiredCapabilities cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				driver = new RemoteWebDriver(new URL(Node), cap);
				// loadData();
				// getUrl(OR.getProperty("url"));
			} else if (browser.equalsIgnoreCase("ie")) {
				System.out.println(" Executing on IE");
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setBrowserName("ie");
				String Node = "http://110.100.100.10:5555/wd/hub";
				driver = new RemoteWebDriver(new URL(Node), cap);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				// Launch website
				// loadData();
				// getUrl(OR.getProperty("url"));
			} else {
				throw new IllegalArgumentException("The Browser Type is Undefined");
			}
		}
		if (System.getProperty("os.name").contains("Window")) {
			if (browser.equals("chrome")) {
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "/drivers/chromedriver.exe");
				System.out.println(" Executing on CHROME");
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setBrowserName("chrome");
				String Node = "http://localhost:5555/wd/hub";
				driver = new RemoteWebDriver(new URL(Node), cap);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				// Launch website
				// loadData();
				// getUrl(OR.getProperty("url"));
			} else if (browser.equals("firefox")) {
				System.out.println(System.getProperty("user.dir"));
				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + "/drivers/geckodriver.exe");
				System.out.println(" Executing on FireFox");
				String Node = "http://localhost:5554/wd/hub";
				DesiredCapabilities cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				driver = new RemoteWebDriver(new URL(Node), cap);
				// loadData();
				// getUrl(OR.getProperty("url"));
			} else if (browser.equalsIgnoreCase("ie")) {
				System.out.println(" Executing on IE");
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				cap.setBrowserName("ie");
				String Node = "http://localhost:5555/wd/hub";
				driver = new RemoteWebDriver(new URL(Node), cap);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				// Launch website
				// getUrl(OR.getProperty("url"));
			} else {
				throw new IllegalArgumentException("The Browser Type is Undefined");
			}
		}
	}

}
