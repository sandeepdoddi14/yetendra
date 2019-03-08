/**
 * 
 */
package com.darwinbox.dashboard.pageObjectRepo.generic;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author balaji
 * @Creation_Date: 29 Nov 2017
 * @ClassName: RightMenuOptionsPage.java
 * @LastModified_Date: 29 Nov 2017
 */
public class RightMenuOptionsPage extends TestBase {

	WebDriver driver;
	private static final Logger log = Logger.getLogger(RightMenuOptionsPage.class);
	GenericHelper objGenHelper;
	WaitHelper objWaitHelper;

	public RightMenuOptionsPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
	}

	@FindBy(id = "main-sidebar")
	protected WebElement mainSideBar;

	@FindBy(xpath = "//*[@class = 'directory-avatar']")
	protected WebElement userProfileIcon;

	@FindBy(xpath = "//*[@id='main-sidebar']//li[@class = 'close-sb']/a[contains(text() , 'Logout')]")
	protected WebElement userProfileLogout;

	@FindBy(xpath = "//*[@id='toggle-sidebar']/span")
	protected WebElement userProfileIconAdmin;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'My Profile']")
	protected WebElement sidebarMyProfile;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Switch To Employee']")
	protected WebElement sidebarSwitchToEmployee;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Change Password']")
	protected WebElement sidebarChangePassword;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Email Settings']")
	protected WebElement sidebarEmailSettings;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Settings']")
	protected WebElement sidebarSettings;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Account']")
	protected WebElement sidebarAccount;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Recruitment']")
	protected WebElement sidebarRecruitment;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Calendar']")
	protected WebElement sidebarCalendar;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Logout']")
	protected WebElement sidebarLogout;

	@FindBy(xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Switch To Admin']")
	protected WebElement sidebarSwitchToAdmin;

	/**
	 * This method used to objGenHelper.elementClick My Profile in side bar
	 */
	public boolean clickSidebarMyProfile() {
		return objGenHelper.elementClick(sidebarMyProfile, "My Profile");
	}

	/**
	 * This method used to objGenHelper.elementClick Switch To Employee in side bar
	 */
	public boolean clickSidebarSwitchToEmployee() {
		return objGenHelper.elementClick(sidebarSwitchToEmployee, "Switch To Employee");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Change Password' in side bar
	 */
	public boolean clickSidebarChangePassword() {
		return objGenHelper.elementClick(sidebarChangePassword, "Change Password");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Email Settings' in side bar
	 */
	public boolean clickSidebarEmailSettings() {
		return objGenHelper.elementClick(sidebarEmailSettings, "Email Settings");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Settings' in side bar
	 */
	public boolean clickSidebarSettings() {
		objWaitHelper.waitElementToBeClickable(sidebarSettings);
		return objGenHelper.elementClick(sidebarSettings, "Settings");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Accounts' in side bar
	 */
	public boolean clickSidebarAccounts() {
		return objGenHelper.elementClick(sidebarAccount, "Accounts");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Recruitment' in side bar
	 */
	public boolean clickSidebarRecruitment() {
		return objGenHelper.elementClick(sidebarRecruitment, "Recruitment");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Calendar' in side bar
	 */
	public boolean clickSidebarCalendar() {
		return objGenHelper.elementClick(sidebarCalendar, "Calendar");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Logout' in side bar
	 */
	public boolean clickSidebarLogout() {
		objWaitHelper.waitElementToBeClickable(sidebarLogout);
		return objGenHelper.elementClick(sidebarLogout, "Logout");
	}

	/**
	 * This method used to objGenHelper.elementClick 'Switch To Admin' in side bar
	 */
	public boolean clickSidebarSwitchToAdmin() {
		objWaitHelper.waitElementToBeClickable(sidebarSwitchToAdmin);
		return objGenHelper.elementClick(sidebarSwitchToAdmin, "Switch To Admin");
	}

	public WebElement getWebElementSidebarSwitchToAdmin() {
		objWaitHelper.waitElementToBeClickable(sidebarSwitchToAdmin);
		return sidebarSwitchToAdmin;
	}
}
