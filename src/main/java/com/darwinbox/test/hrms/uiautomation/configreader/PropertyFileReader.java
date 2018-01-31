/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.configreader;

import java.util.Properties;

import com.darwinbox.test.hrms.uiautomation.Utility.ResourceHelper;
import com.darwinbox.test.hrms.uiautomation.configuration.browser.BrowserType;

/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: PropertyFileReader.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class PropertyFileReader implements ConfigReader{

	private Properties prop = null;

	public PropertyFileReader() {
		prop = new Properties();
		try {
			prop.load(ResourceHelper.getResourcePathInputStream("/src/main/resources/configfile/config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAdminUserName() {
		return prop.getProperty("admin.username");
	}

	public String getAdminPassword() {
		return prop.getProperty("admin.password");
	}

	public String getApplication() {
		return prop.getProperty("URL");
	}

	public int getPageLoadTimeOut() {
		return Integer.parseInt(prop.getProperty("PageLoadTimeOut"));
	}

	public int getImplicitWait() {
		return Integer.parseInt(prop.getProperty("ImplcitWait"));
	}

	public int getExplicitWait() {
		return Integer.parseInt(prop.getProperty("ExplicitWait"));
	}

	public BrowserType getBrowser() {
		return BrowserType.valueOf(prop.getProperty("Browser"));
	}
	public String getLogLevel() {
		return prop.getProperty("Logger.Level");
	}

	
}
