package com.darwinbox.dashboard.actionClasses;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.helper.Action.ActionHelper;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import com.darwinbox.framework.uiautomation.Utility.HTTPSClientHelper;
import com.darwinbox.dashboard.pageObjectRepo.generic.HomePage;
import com.darwinbox.dashboard.pageObjectRepo.generic.RightMenuOptionsPage;

import java.util.HashMap;

public class CommonAction extends TestBase {

	WaitHelper objWait;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	WebDriver driver;
	DateTimeHelper objDateTimeHelper;
	UtilityHelper objUtil;
	ActionHelper objActionHelper;
	BrowserHelper objBrowserHelper;
	HTTPSClientHelper objHttpClient;

	RightMenuOptionsPage rightMenuOption;
	HomePage homepage;

	public static final Logger log = Logger.getLogger(CommonAction.class);

	public CommonAction(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
		objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
		objDateTimeHelper = PageFactory.initElements(driver, DateTimeHelper.class);
		objUtil = PageFactory.initElements(driver, UtilityHelper.class);
		objJavaScrHelper = PageFactory.initElements(driver, JavaScriptHelper.class);
		objActionHelper = PageFactory.initElements(driver, ActionHelper.class);
		objBrowserHelper = PageFactory.initElements(driver, BrowserHelper.class);
		//excelWriter = PageFactory.initElements(driver, ExcelWriter.class);
		rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
		objHttpClient = PageFactory.initElements(driver, HTTPSClientHelper.class);
	}

	/**
	 * This method will switch the mode to Admin
	 * @return
	 */
	public boolean switchToAdminMode() {
		try {
			homepage.clickUserProfileIcon();
			if (rightMenuOption.getWebElementSidebarSwitchToAdmin().getText().contains("Switch To Admin")) {
				rightMenuOption.clickSidebarSwitchToAdmin();
				homepage.clickUserProfileIconAdmin();
				return true;
			} else {
				return true;
			}
		} catch (NoSuchElementException e) {
			return true;
		} catch (Exception e) {
			Reporter("Exception  while switching to Admin Mode","Error");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * This method change the application access from Employee to Admin
	 * @param text
	 * @return
	 */
	public boolean changeApplicationAccessMode(String text) {
		try {
			String applicationURL = data.get("@@url");
			String URL = "/dashboard/changeAccess";
			objGenHelper.navigateTo(URL);
			objWait.waitForPageToLoad();
			driver.navigate().to(applicationURL);
			objWait.waitForPageToLoad();

			if (homepage.checkIntroToolTipIsDisplayed() == true){
				homepage.clickIntroToolTipSkipButton();
			}

			objJavaScrHelper.scrollDownVertically(driver);
			if (text.equalsIgnoreCase("Admin")){
				if (homepage.checkAdminAccessHeadIsDisplayed() == true){
					Reporter("Access is changed to '"+ text+ "' mode", "Pass");
				}else {
					changeApplicationAccessMode(text);
				}
			}else if(text.equalsIgnoreCase("Employee")) {
				if (homepage.checkAdminAccessHeadIsDisplayed( )==true) {
					changeApplicationAccessMode(text);
				} else {
					Reporter("Access is changed to '" + text + "' mode", "Pass");
				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			Reporter("Exception while changing access to '"+ text + "' mode", "Error");
			return false;
		}
	}

	/**
	 * This method change the application access from Employee to Admin
	 * @param text
	 * @return
	 */
	public boolean changeApplicationAccessModeUsingAPI(String text) {
		try {
			String applicationURL = data.get("@@url");
			String URL = applicationURL + "/dashboard/changeAccess";
	        HashMap<String, String> headers = new HashMap<>();
	        headers.put("X-Requested-With", "XMLHttpRequest");

			objHttpClient.doGet(URL, headers);
			objWait.waitForPageToLoad();
			driver.navigate().to(applicationURL);
			objWait.waitForPageToLoad();
			Reporter("Access is changed to '"+ text+ "' mode", "Pass");
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			Reporter("Exception while changing access to '"+ text + "' mode", "Error");
			return false;
		}
	}
}