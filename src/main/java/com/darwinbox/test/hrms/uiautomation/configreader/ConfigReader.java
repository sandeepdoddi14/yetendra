/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.configreader;

import com.darwinbox.test.hrms.uiautomation.configuration.browser.BrowserType;


/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: ConfigReader.java
 * @LastModified_Date:  20 Nov 2017 
 */
public interface ConfigReader {
	public String getAdminUserName();
	public String getAdminPassword();
	public String getApplication();
	public int getPageLoadTimeOut();
	public int getImplicitWait();
	public int getExplicitWait();
	public String getLogLevel();
	public BrowserType getBrowser();
}