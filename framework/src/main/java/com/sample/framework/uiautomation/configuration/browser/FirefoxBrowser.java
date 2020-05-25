/**
 * 
 */
package com.sample.framework.uiautomation.configuration.browser;

import com.sample.framework.uiautomation.Utility.ResourceHelper;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;


/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: FirefoxBrowser.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class FirefoxBrowser {
	public Capabilities getFirefoxCapabilities() {
		DesiredCapabilities firefox = DesiredCapabilities.firefox();
		FirefoxProfile profile = new FirefoxProfile();
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(true);
		firefox.setCapability(FirefoxDriver.PROFILE, profile);
		firefox.setCapability("marionette", true);
		return firefox;
	}
	
	public WebDriver getFirefoxDriver(Capabilities cap) {
		
		if (System.getProperty("os.name").contains("Mac")){
			System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("/src/main/resources/drivers/geckodriver"));
			return new FirefoxDriver(cap);
		}
		else if(System.getProperty("os.name").contains("Window")){
			System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("/src/main/resources/drivers/windows/geckodriver.exe"));
			return new FirefoxDriver(cap);
		}
		else if(System.getProperty("os.name").contains("Linux")){
			System.setProperty("webdriver.gecko.driver", ResourceHelper.getResourcePath("/src/main/resources/drivers/linux/geckodriver"));
			return new FirefoxDriver(cap);
		}
		return null;
	}
}
