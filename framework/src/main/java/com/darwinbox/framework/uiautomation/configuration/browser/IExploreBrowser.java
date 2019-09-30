/**
 * 
 */
package com.darwinbox.framework.uiautomation.configuration.browser;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.ElementScrollBehavior;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.darwinbox.framework.uiautomation.Utility.ResourceHelper;



/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: IExploreBrowser.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class IExploreBrowser {

	public InternetExplorerOptions getIExplorerOptions() {

		InternetExplorerOptions cap = new InternetExplorerOptions();

		cap.setCapability(InternetExplorerDriver.ELEMENT_SCROLL_BEHAVIOR, ElementScrollBehavior.BOTTOM);
		cap.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,true);
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		cap.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);

		return cap;
	}

	public WebDriver getIExplorerDriver(InternetExplorerOptions options) {
		System.setProperty("webdriver.ie.driver", ResourceHelper.getResourcePath("driver/IEDriverServer.exe"));
		return new InternetExplorerDriver();
	}

	public WebDriver getIExplorerDriver() {
		System.setProperty("webdriver.ie.driver", ResourceHelper.getResourcePath("driver/IEDriverServer.exe"));
		return new InternetExplorerDriver(getIExplorerOptions());
	}
}
