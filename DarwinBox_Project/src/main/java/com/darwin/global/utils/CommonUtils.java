package com.darwin.global.utils;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import com.aventstack.extentreports.ExtentTest;

public class CommonUtils extends GlobalUtility {

	public static String usrdir = System.getProperty("user.dir");

	static WebDriver driver;

	protected CommonUtils(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * This method clicks on button or link
	 * 
	 * @param element,
	 *            Name of the element
	 * @return true/false
	 * @author shikhar
	 * @return
	 * @since 25/10/2017
	 */
	public static boolean click(WebElement element, String text) {
		try {
			element.click();
			Reporter(text + " is clicked successfully", "Pass");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while clicking " + text, "Fail");
			throw new RuntimeException("Exception while clicking " + text + ": " + e.getMessage());
		}
	}

	/**
	 * This method sends characters in text box
	 * 
	 * @param element,
	 *            Name of the element
	 * @return true/false
	 * @author shikhar
	 * @return
	 * @since 25/10/2017
	 */
	public static boolean insertText(WebElement element, String text, String value) {

		try {
			element.clear();
			element.sendKeys(value);
			Reporter("In " + text + " textbox parameter inserted is: '" + value + "'", "Pass");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while inserting text in " + text + " textbox", "Fail");
			throw new RuntimeException(e.getLocalizedMessage());
		}
	}

	/**
	 * This method selects visible text from Dropdown
	 * 
	 * @param element,
	 *            Visible text, Name of the element
	 * @return true/false
	 * @author shikhar
	 * @return
	 * @since 25/10/2017
	 */
	public static boolean selectVisibleTextFromDropDown(WebElement element, String drptextToSelect, String text) {

		try {
			Select drpElement = new Select(element);
			drpElement.selectByVisibleText(drptextToSelect);
			Reporter("From '" + text + "' drop down '" + drptextToSelect + "' is selected", "Pass");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while selecting text from " + text + " dropdown", "Fail");
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * This method enables or disables text based on parameter paased
	 * 
	 * @param element
	 * @param enableOrDisable
	 * @param text
	 * @return
	 */
	public static boolean enableOrDisableCheckBox(WebElement element, String enableOrDisable, String text) {
		try {
			if (element.isDisplayed()) {
				if (enableOrDisable.equalsIgnoreCase("Enable")) {
					if (element.isEnabled() == false) {
						element.click();
						return true;
					}
				} else if (enableOrDisable.equalsIgnoreCase("Disable")) {
					if (element.isEnabled() == true) {
						element.click();
						return true;
					}
				}
			} else {
				Reporter(text + " checkbox not displayed", "Fail");
				return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while " + enableOrDisable + "ing " + text + " checkbox", "Fail");
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * This method selects text from Dropdown using index
	 * 
	 * @param element,Index,
	 *            Name of the element
	 * @return true/false
	 * @author shikhar
	 * @return
	 * @since 25/10/2017
	 */
	public static boolean selectTextFromDropDownByIndex(WebElement element, int drptIndexToSelect, String text) {

		try {
			Select drpElement = new Select(element);
			drpElement.selectByIndex(drptIndexToSelect);
			Reporter("From '" + text + "' drop down test data is selected", "Pass");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while selecting text from " + text + " dropdown", "Pass");
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public static boolean acceptAlert() throws UnhandledAlertException {
		try {
			Alert alert = driver.switchTo().alert();
			Reporter(alert.getText() + " alert accepted", "Pass");
			alert.accept();
			return true;
		} catch (UnhandledAlertException e) {
			Alert alert = driver.switchTo().alert();
			alert.accept();
			return true;
		} catch (Exception e) {
			Reporter("Exception while handling alert. ", "Fail");
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * This method allows page to Load for 10 seconds
	 * 
	 * @author shikhar
	 */
	public static void waitForPageToLoad() {
		try {
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			Reporter("Exception while applying wait to load page:" + e.getMessage(), "Fail");
		}
	}

	/**
	 * This method wait for an element to be clickable using Explicit wait
	 */
	public static void waitElementToBeClickable(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			Reporter("Exception while applying wait to the element to become clickable:" + e.getMessage(), "Fail");
		}
	}

	public static boolean waitAndClickElement(WebElement element, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			Boolean elementPresent = wait.until(ExpectedConditions.elementToBeClickable(element)).isEnabled();
			if (elementPresent == true && element.isDisplayed()) {
				element.click();
				Reporter("Clicked on the element: " + text, "Pass");
				return true;
			}

		} catch (StaleElementReferenceException elementUpdated) {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			Boolean elementPresent = wait.until(ExpectedConditions.stalenessOf(element));
			if (elementPresent == true) {
				WebElement staleElement = element;
				staleElement.click();
				Reporter("Clicked on the 'Stale' element: " + text, "Pass");
				return true;
			}
		} catch (NoSuchElementException e) {
			System.out.println("Exception! - Could not click on the element: " + text + ", Exception: " + e.toString());
			throw (e);
		} catch (TimeoutException e) {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			Boolean elementPresent = wait.until(ExpectedConditions.elementToBeClickable(element)).isEnabled();
			if (elementPresent == true && element.isDisplayed()) {
				element.click();
				Reporter("Clicked on the element: " + text, "Pass");
				return true;
			}
		} catch (Exception e) {
			Reporter("Exception while waiting for element to be clickable:" + e.getMessage(), "Fail");
			throw new RuntimeException(e);
		}

		finally {
			waitForPageToLoad();
		}
		return false;
	}

	/**
	 * This method wait for an element to be visible using Explicit wait
	 */
	public static void waitElementToBeVisible(WebElement element) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (NoSuchElementException e) {
			Reporter("Element is not visible on page", "Info");
		} catch (Exception e) {
			Reporter.log("Not able to apply wait to the element to be visible", true);
			e.printStackTrace();
		}
	}

	/**
	 * This method wait for a text to be available in an element using Explicit wait
	 */
	public static void waitForTextToBeAvailable(WebElement element, String text) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			wait.until(ExpectedConditions.textToBePresentInElement(element, text));
		} catch (Exception e) {
			Reporter("Exception while applying wait to the element to become clickable:" + e.getMessage(), "Fail");
		}
	}

	/**
	 * Set Logger for Report
	 * 
	 * @param log
	 */
	public static void setLogger(ExtentTest log) {
		xtReportLog = log;
	}

	/**
	 * This method checks visibility of element on page
	 * 
	 * @author shikhar
	 * @param WebElement,
	 *            info text for WebElement
	 * 
	 */
	public static boolean checkVisbilityOfElement(WebElement element, String text) {
		try {
			waitElementToBeVisible(element);
			if (element.isDisplayed() == true) {
				Reporter(text + " element is visible", "Pass");
				return true;
			} else {
				Reporter(text + " is not visible on page", "Info");
				return false;
			}
		} catch (NoSuchElementException e) {
			Reporter(text + " is not visible on page", "Info");
			return false;
		}
	}

	/**
	 * This method checks visibility of element on page
	 * 
	 * @author shikhar
	 * @param WebElement,
	 *            info text for WebElement
	 */
	public static boolean checkInvisbilityOfElement(WebElement element, String text) {
		try {
			if (element.isDisplayed() == false) {
				Reporter("Correctly" + text + " is not visible on page", "Pass");
				return true;
			} else {
				Reporter(text + " element is visible", "fail");
				throw new RuntimeException(text + " is visible");
			}
		} catch (NoSuchElementException e) {
			Reporter("Correctly" + text + " is not visible on page", "Pass");
			return true;
		}
	}

	/**
	 * This method clicks using action class
	 * 
	 * @param element
	 * @param text
	 * @return
	 */
	public static boolean actionClick(WebElement element, String text) {
		try {
			Actions action = new Actions(driver);
			action.click(element).build().perform();
			Reporter("Clicked on " + text + " successfully", "Pass");
			return true;
		} catch (Exception e) {
			Reporter("Exception while clicking on " + text, "Fail");
			throw new RuntimeException("Exception while clicking on " + text + ":" + e.getMessage());
		}
	}

	/**
	 * This method used to Scroll Window up
	 * @param text
	 * @return
	 */
	public static boolean scrollUp(String text) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			 jse.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
			return true;
		} catch (Exception e) {
			Reporter("Exception while scrooling to the element " + text, "Fail");
			throw new RuntimeException("Exception while clicking on " + text + ":" + e.getMessage());
		}
	}

	/**
	 * This method used to Scroll Window down
	 * @param text
	 * @return
	 */
	public static boolean scrollDown(String text) {
		try {
			JavascriptExecutor jse = (JavascriptExecutor) driver;
			 jse.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			return true;
		} catch (Exception e) {
			Reporter("Exception while scrooling to the element " + text, "Fail");
			throw new RuntimeException("Exception while clicking on " + text + ":" + e.getMessage());
		}
	}

}
