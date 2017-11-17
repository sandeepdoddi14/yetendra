package com.dawinbox.common.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.darwin.global.utils.GlobalUtility;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class TestEngine extends GlobalUtility {

	WebDriver driver;

	@BeforeSuite
	public void startReport() {
		htmlReporter = new ExtentHtmlReporter(
				System.getProperty("user.dir") + "//target//Response_Report//STMExtentReport.html");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		htmlReporter.setAppendExisting(true);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter
				.loadXMLConfig(new File(System.getProperty("user.dir") + "//src//main//resources//extent-config.xml"));
		extent.setSystemInfo("User Name", "QA Automation");
		extent.setSystemInfo("OS", "Linux");
		extent.setSystemInfo("Host Name", "DawinBox_Automation_Team");
		extent.setSystemInfo("Environment", "QA Test");
		extent.setReportUsesManualConfiguration(true);
	}

	/**
	 * @description This method Initialize the driver instance based on browser
	 *              input
	 * @param browser
	 * @return WebDriver
	 * @author shikhar
	 * @since 25/10/2017
	 */
	public WebDriver getWebDriver() throws Exception {
		String Operating_System = config("login", "Operating_System");

		String browser = config("login", "browser");
		try {
			browser = browser.toLowerCase();
			Operating_System = Operating_System.toLowerCase();
			driver = getDriverInstance(Operating_System, browser);
			return driver;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while creating driver instance", "Fail");
			return null;
		} finally {
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		}
	}

	@BeforeMethod
	public void nameBefore(Method method) {
		try {
			rowForTestData++;
			if (excelInput("RunMode").equalsIgnoreCase("Yes")) {
				Reporter.log("*****" + method.getName(), true);
				if (totalNoOfRows > 2) {
					xtReportLog = extent
							.createTest((this.getClass().getSimpleName() + " :: " + excelInput("Test_Description")),
									method.getName())
							.assignCategory(this.getClass().getPackageName()
									.substring(this.getClass().getPackageName().lastIndexOf(".") + 1));
				} else {
					xtReportLog = extent.createTest((this.getClass().getSimpleName()), method.getName())
							.assignCategory(this.getClass().getPackageName()
									.substring(this.getClass().getPackageName().lastIndexOf(".") + 1));
				}
				logger.info("******************************************************************************");
				logger.info("*********** " + (this.getClass().getPackageName()
						.substring(this.getClass().getPackageName().lastIndexOf(".") + 1) + "::"
						+ this.getClass().getSimpleName() + " ******"));
			}
		} catch (Exception e) {
			throw new RuntimeException("Exception in Before Method" + e.getMessage());
		}
	}

	@AfterMethod
	public void takeScreenShot(ITestResult testResult) throws IOException {
		try {
			if (excelInput("RunMode").equalsIgnoreCase("Yes")) {
				File screenShotName;
				// System.setProperty("org.uncommons.reportng.escape-output", "false");
				if (testResult.getStatus() == ITestResult.FAILURE) {
					Reporter(" " + testResult.getThrowable(), "Fail");
					// Reporter(testResult.getName() + " Test Case Failed ", "Fail");
					File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					String scrPath = usrdir + config("configuration", "screenshot.file.path") + testResult.getName()
							+ "-" + Arrays.toString(testResult.getParameters()) + ".png";
					screenShotName = new File(scrPath);
					FileUtils.copyFile(scrFile, screenShotName);
					String filePath = screenShotName.toString();
					xtReportLog.fail("Failure Screenshot",
							MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
				} else if (testResult.getStatus() == ITestResult.SUCCESS) {
					File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					String scrPath = usrdir + config("configuration", "screenshot.file.path") + testResult.getName()
							+ "-" + Arrays.toString(testResult.getParameters()) + ".png";
					screenShotName = new File(scrPath);
					FileUtils.copyFile(scrFile, screenShotName);
					String filePath = screenShotName.toString();
					xtReportLog.pass("Pass Screenshot",
							MediaEntityBuilder.createScreenCaptureFromPath(filePath).build());
				} else {
					Reporter(testResult.getName() + " " + testResult.getStatus(), "info");
				}
				logger.info("****************** END ***********************");
				closeAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Reporter.log("Exception while taking Screenshot", true);
		}
	}

	@BeforeClass
	public void beforeClass() {
		rowForTestData = 0;
	}

	public void closeAll() {
		driver.close();
		driver.quit();
		extent.flush();
	}
}
