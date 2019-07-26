/**
 * 
 */
package com.darwinbox.framework.uiautomation.configuration.browser;

import com.darwinbox.framework.uiautomation.base.TestBase;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.darwinbox.framework.uiautomation.Utility.ResourceHelper;


/**
 * @author balaji
 * @Creation_Date:  20 Nov 2017 
 * @ClassName: ChromeBrowser.java
 * @LastModified_Date:  20 Nov 2017 
 */
public class ChromeBrowser {

	public Capabilities getChromeCapabilities() {
		ChromeOptions option = new ChromeOptions();
		option.addArguments("start-maximized");
		DesiredCapabilities chrome = DesiredCapabilities.chrome();
		chrome.setJavascriptEnabled(true);
		chrome.setCapability(ChromeOptions.CAPABILITY, option);
		return chrome;
	}

	public WebDriver getChromeDriver(Capabilities cap) {
		if (System.getProperty("os.name").contains("Mac")){
			System.setProperty("webdriver.chrome.driver", TestBase.respath + "drivers/mac/chromedriver");
			return new ChromeDriver(cap);
		}
		else if(System.getProperty("os.name").contains("Window")){
			System.setProperty("webdriver.chrome.driver", TestBase.respath +"drivers/windows/chromedriver.exe");
			return new ChromeDriver(cap);
		}
		else if(System.getProperty("os.name").contains("Linux")){
			System.setProperty("webdriver.chrome.driver", TestBase.respath + "drivers/linux/chromedriver");
			return new ChromeDriver(cap);
		}
		return null;
	}
}