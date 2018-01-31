/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.helper.Javascript;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;



/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: JavaScriptHelper.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class JavaScriptHelper extends TestBase{
	private WebDriver driver;
	private static final Logger Log = Logger.getLogger(JavaScriptHelper.class);

	
	public JavaScriptHelper(WebDriver driver) {
		this.driver = driver;
		Log.debug("JavaScriptHelper : " + this.driver.hashCode());
	}

	
	public Object executeScript(String script) {
		try {
			JavascriptExecutor exe = (JavascriptExecutor) driver;
			return exe.executeScript(script);
		} catch (Exception e) {
			throw new RuntimeException("Exception while invoking java script" + e.getMessage());
		}
	}

	public void executeScript(String script, Object... args) {
		try {
			JavascriptExecutor exe = (JavascriptExecutor) driver;
			exe.executeScript(script, args);
		} catch (Exception e) {
			throw new RuntimeException("Exception while invoking java script" + e.getMessage());
		}
	}

	/**
	 * This method used to Scroll Window up
	 * 
	 * @param text
	 * @return
	 */
	public boolean scrollUpVertically(String text) {
		try {
			executeScript("window.scrollTo(0, -document.body.scrollHeight);");
			return true;
		} catch (Exception e) {
			Reporter("Exception while scrooling to the element " + text, "Fail");
			throw new RuntimeException("Exception while clicking on " + text + ":" + e.getMessage());
		}
	}

	/**
	 * This method used to Scroll Window down
	 * 
	 * @param text
	 * @return
	 */
	public boolean scrollDownVertically(WebDriver driver, String text) {
		try {
			executeScript("window.VerticallyscrollTo(0, document.body.scrollHeight);");
			return true;
		} catch (Exception e) {
			Reporter("Exception while scrooling to the element " + text, "Fail");
			throw new RuntimeException("Exception while scrooling to the element " + text + e.getMessage());
		}
	}

	public boolean scrollToElemet(WebDriver driver, WebElement element, String text) {
		try {
			executeScript("window.scrollTo(arguments[0],arguments[1])", element.getLocation().x,
					element.getLocation().y);
			return true;
		} catch (Exception e) {
			Reporter("Exception while scrolling to the element " + text, "Fail");
			throw new RuntimeException("Exception while scrolling to the element " + text + e.getMessage());
		}
	}

	public void scrollToElemetAndClick(WebElement element, String text) {
		scrollToElemet(driver, element, text);
		element.click();
	}
	

//	public Object executeScript(String script) {
//		JavascriptExecutor exe = (JavascriptExecutor) driver;
//		Log.info(script);
//		return exe.executeScript(script);
//	}
//
//	public Object executeScript(String script, Object... args) {
//		JavascriptExecutor exe = (JavascriptExecutor) driver;
//		Log.info(script);
//		return exe.executeScript(script, args);
//	}
//
//	public void scrollToElemet(WebElement element) {
//		executeScript("window.scrollTo(arguments[0],arguments[1])", element.getLocation().x, element.getLocation().y);
//		Log.info(element);
//	}
//
//	public void scrollToElemetAndClick(WebElement element) {
//		scrollToElemet(element);
//		element.click();
//		Log.info(element);
//	}
//
//	public void scrollIntoView(WebElement element) {
//		executeScript("arguments[0].scrollIntoView()", element);
//		Log.info(element);
//	}
//
//	public void scrollIntoViewAndClick(WebElement element) {
//		scrollIntoView(element);
//		element.click();
//		Log.info(element);
//	}
//
//	public void scrollDownVertically() {
//		executeScript("window.scrollTo(0, document.body.scrollHeight)");
//	}
//
//	public void scrollUpVertically() {
//		executeScript("window.scrollTo(0, -document.body.scrollHeight)");
//	}
//
//	public void scrollDownByPixel() {
//		executeScript("window.scrollBy(0,1500)");
//	}
//
//	public void scrollUpByPixel() {
//		executeScript("window.scrollBy(0,-1500)");
//	}
//
//	public void ZoomInBypercentage() {
//		executeScript("document.body.style.zoom='40%'");
//	}
//
//	public void ZoomBy100percentage() {
//		executeScript("document.body.style.zoom='100%'");
//	}
}
