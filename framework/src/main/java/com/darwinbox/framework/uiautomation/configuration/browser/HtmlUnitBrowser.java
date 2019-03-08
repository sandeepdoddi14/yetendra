/**
 * 
 */
package com.darwinbox.framework.uiautomation.configuration.browser;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: HtmlUnitBrowser.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class HtmlUnitBrowser {
	public Capabilities getHtmlUnitDriverCapabilities() {
		DesiredCapabilities unit = DesiredCapabilities.htmlUnit();
		return unit;
	}
	
	public WebDriver getHtmlUnitDriver(Capabilities cap) {
		return new HtmlUnitDriver(cap);
	}
}
