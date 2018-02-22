package com.darwinbox.test.hrms.uiautomation.Common.Action;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import com.darwinbox.test.hrms.uiautomation.Pages.HomePage;
import com.darwinbox.test.hrms.uiautomation.Pages.RightMenuOptionsPage;
import com.darwinbox.test.hrms.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.test.hrms.uiautomation.Utility.ExcelWriter;
import com.darwinbox.test.hrms.uiautomation.Utility.UtilityHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Action.ActionHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Browser.BrowserHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class CommonActionClass extends TestBase {

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
	ExcelWriter excelWriter;
	RightMenuOptionsPage rightMenuOption;
	HomePage homepage;

	public static final Logger log = Logger.getLogger(CommonActionClass.class);

	public CommonActionClass(WebDriver driver) {
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
		excelWriter = PageFactory.initElements(driver, ExcelWriter.class);
		rightMenuOption = PageFactory.initElements(driver, RightMenuOptionsPage.class);
		homepage = PageFactory.initElements(driver, HomePage.class);
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
			Reporter("Exception  while switching to Admin Mode","Fail");
			e.printStackTrace();
			return false;
		}
	}
}
