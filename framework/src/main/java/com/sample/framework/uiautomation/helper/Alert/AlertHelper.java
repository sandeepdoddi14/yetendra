/**
 * 
 */
package com.sample.framework.uiautomation.helper.Alert;

import com.sample.framework.uiautomation.base.TestBase;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;


/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: AlertHelper.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class AlertHelper extends TestBase {

	private WebDriver driver;
	private static final Logger oLog = Logger.getLogger(AlertHelper.class);
	
	public AlertHelper(WebDriver driver) {
		this.driver = driver;
		oLog.debug("AlertHelper : " + this.driver.hashCode());
	}
	
	
	public void AcceptAlert() {
		oLog.info("");
		getAlert().accept();
	}
	
	public void DismissAlert() {
		oLog.info("");
		getAlert().dismiss();
	}

	public String getAlertText() {
		String text = getAlert().getText();
		oLog.info(text);
		return text;
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			oLog.info("true");
			return true;
		} catch (NoAlertPresentException e) {
			// Ignore
			oLog.info("false");
			return false;
		}
	}

	public void AcceptAlertIfPresent() {
		if (!isAlertPresent())
			return;
		AcceptAlert();
		oLog.info("");
	}

	public void DismissAlertIfPresent() {

		if (!isAlertPresent())
			return;
		DismissAlert();
		oLog.info("");
	}
	
	public void AcceptPrompt(String text) {
		
		if (!isAlertPresent())
			return;
		
		Alert alert = getAlert();
		alert.sendKeys(text);
		alert.accept();
		oLog.info(text);
	}
	
	/**
	 * This method accepts Alert
	 * 
	 * @return
	 * @throws UnhandledAlertException
	 */
	public boolean acceptAlert() throws UnhandledAlertException {
		try {
			Alert alert = getAlert();
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

	public Alert getAlert() {
		return driver.switchTo().alert();


	}

}
