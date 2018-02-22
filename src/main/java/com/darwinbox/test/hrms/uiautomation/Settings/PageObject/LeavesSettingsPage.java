package com.darwinbox.test.hrms.uiautomation.Settings.PageObject;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.test.hrms.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

public class LeavesSettingsPage extends TestBase {

	WaitHelper objWait;
	GenericHelper objGenHelper;
	DropDownHelper objDropDownHelper;
	JavaScriptHelper objJavaScrHelper;
	AlertHelper objAlertHelper;
	WebDriver driver;

	public static final Logger log = Logger.getLogger(LeavesSettingsPage.class);
	

	public LeavesSettingsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objWait = PageFactory.initElements(driver, WaitHelper.class);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
		objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);	
	}

	/*
	 * Leaves Main Menu Object Repository
	 */

	@FindBy(xpath = "//*[@id='edit']/a")
	private WebElement leavesManageLeavePolicies;

	@FindBy(xpath = "//*[@id='create']/a")
	private WebElement leavesMenuCreateLeavePolicies;

	@FindBy(xpath = "//*[@id='unpaid']/a")
	private WebElement leavesMenuUnpaidLeaves;

	@FindBy(xpath = "//*[@id='holidays']/a")
	private WebElement leavesMenuHolidays;

	@FindBy(xpath = "//*[@id='settings']/a")
	private WebElement leavesMenuSettings;

	@FindBy(xpath = "//*[@id='compoff']/a")
	private WebElement leavesMenuCompoff;

	/**
	 * This method clicks on Manage Leave Policies on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickManageLeavePolicies() {
		return objGenHelper.elementClick(leavesManageLeavePolicies, "Manage Leave Policies link");
	}

	/**
	 * This method clicks on Create Leave Policies on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickCreateLeavePolicies() {
		try {
			objWait.waitForPageToLoad();
			objWait.waitAndClickElement(leavesMenuCreateLeavePolicies, "Create Leave Policies link");
		return objGenHelper.elementClick(leavesMenuCreateLeavePolicies, "Create Leave Policies link");
	}catch(Exception e) {
		e.printStackTrace();
		return false;
	}
	}

	/**
	 * This method clicks on Unpaid Leaves on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickUnpaidLeaves() {
		return objGenHelper.elementClick(leavesMenuUnpaidLeaves, "Unpaid Leaves link");
	}

	/**
	 * This method clicks on Holidays on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickHolidays() {
		return objGenHelper.elementClick(leavesMenuHolidays, "Holidays link");
	}

	/**
	 * This method clicks on Settings on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickSettings() {
		return objGenHelper.elementClick(leavesMenuSettings, "Settings link");
	}

	/**
	 * This method clicks on Compensatory Off on Leaves Menu
	 * 
	 * @return
	 */
	public boolean clickCompensatoryOff() {
		return objGenHelper.elementClick(leavesMenuCompoff, "Compensatory Off link");
	}

}
