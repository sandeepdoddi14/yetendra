package com.dbox.commonPage;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.darwin.global.utils.CommonUtils;

public class CommonPageObject extends CommonUtils {

	public WebDriver driver;

	public CommonPageObject(WebDriver driver) {
		super(driver);
		this.driver = driver;
		// This initElements method will create all WebElements
		PageFactory.initElements(driver, this);
	}

	private static String fileSeperator = System.getProperty("file.separator");

	@FindBy(id = "UserLogin_username")
	protected WebElement username;

	@FindBy(id = "UserLogin_password")
	protected WebElement password;

	@FindBy(id = "login-submit")
	protected WebElement signIn;

	@FindBy(xpath = "//*[@class = 'errorMessage']")
	protected WebElement signInMsg;

	@FindBy(xpath = "//*[@class = 'directory-avatar']")
	protected WebElement userProfileIcon;
	
	@FindBy(xpath = "//*[@id='toggle-sidebar']/span")
	protected WebElement userProfileIconAdmin;

	@FindBy(xpath = "//a[contains(@class,'dashboard nav-dashboard')]")
	protected WebElement navMenuDashboard;
	
	@FindBy(xpath = "//a[contains(@class,'employees nav-employee')]")
	protected WebElement navMenuEmployee;
	
	@FindBy(xpath = "//a[contains(@class,'payroll nav-payroll')]")
	protected WebElement navMenuPayroll; 

	@FindBy(xpath = "//a[contains(@class,'reports nav-reports')]")
	protected WebElement navMenuReports;
	
	@FindBy(xpath = "//a[contains(@class,'pms nav-pms')]")
	protected WebElement navMenuPerformance;
	
	@FindBy(xpath = "//a[contains(@class,'hr-documents nav-hrdocs')]")
	protected WebElement navMenuHRDocs;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'My Profile']")
	protected WebElement sidebarMyProfile;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Switch To Employee']")
	protected WebElement sidebarSwitchToEmployee;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Change Password']")
	protected WebElement sidebarChangePassword;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Email Settings']")
	protected WebElement sidebarEmailSettings;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Settings']")
	protected WebElement sidebarSettings;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Account']")
	protected WebElement sidebarAccount;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Recruitment']")
	protected WebElement sidebarRecruitment;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Calendar']")
	protected WebElement sidebarCalendar;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Logout']")
	protected WebElement sidebarLogout;
	
	@FindBy (xpath = "//*[@id='main-sidebar']/ul//a[text() = 'Switch To Admin']")
	protected WebElement sidebarSwitchToAdmin;
	
		
	/**
	 * This function used for Launching the Application
	 * @return boolean
	 * @author shikhar
	 * @return
	 * @since 25/10/2017
	 */

	public boolean launchApp() throws Exception {
		try {
			driver.get(config("login", "URL"));
			Reporter("Launched Application successfully","Pass");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			Reporter("Exception while launching Application ","Fail");
			return false;
		}

	}

	/**
	 * Input User Name while login
	 * @author shikhar
	 * @param susername
	 * @return
	 */
	public boolean inputUsername(String susername) {
		return insertText(username, "User Name", susername);
	}
	
	/**
	 * Input Password while login
	 * @author shikhar
	 * @param susername
	 * @return boolean
	 */
	public boolean inputPassword(String spassword) {
		return insertText(password, "Password", spassword);
	}

	/**
	 * This method used to verify Sign in
	 * @author shikhar
	 * @param susername
	 * @return boolean
	 */
	public boolean clickSignIn() throws NoSuchElementException {
		try {
			
			click(signIn, "SignIn button");
			if (userProfileIcon.isDisplayed()) {
				Reporter("User successfully Login to the application", "Pass");
				return true;			
			}
		} catch (NoSuchElementException e) {
			if (signInMsg.isDisplayed()) {
				Reporter("Sign In failed with error message: "+ signInMsg.getText(), "Fail");
				throw new RuntimeException("SignIn Failed");
			} else {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * This method for login to Application
	 * @author shikhar
	 * @param susername
	 * @return boolean
	 */
	public boolean loginToApplication(String sUserName, String sPassword) throws Exception {
		try {
			inputUsername(sUserName);
			inputPassword(sPassword);
			clickSignIn();
			return true;
		} catch (Exception e) {
			Reporter("Exception while logging to application :"+ e.getMessage(),"Fail");
			e.printStackTrace();
			return false;
		}
	}
	

	
	/**
	 * This method used to verify invalid Sign in
	 * @author shikhar
	 * @param susername
	 * @return boolean
	 */
	public boolean checkInvalidSignIn(String errorMsg) throws NoSuchElementException {
		try {
			click(signIn, "SignIn button");
			if (signInMsg.isDisplayed()) {
				if(signInMsg.getText().trim().contentEquals(errorMsg.trim())) {
					Reporter("Actual error message: '"+ signInMsg.getText()+ "' is equal to expected error message", "Pass");
					return true;
				}
				else {
					Reporter("Actual error message: '"+ signInMsg.getText()+ "' is not equal to expected error message: '"
							+errorMsg+"'","Fail");
					return false;
				}
			}
				else {
				Reporter("Error Message not displayed","Fail");
				return false;
			}
			
		} catch (NoSuchElementException e) {
			if (userProfileIcon.isDisplayed()) {
				Reporter("User successfully logged in to the application", "Fail");
				return false;
			} else {
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * This method used to click UserProfileIcon
	 */
	public boolean clickUserProfileIcon() {
		
		waitForPageToLoad();
		return waitAndClickElement(userProfileIcon,"User Profile icon");

	}
	/**
	 * This method used to click UserProfileIcon
	 */	
	public boolean clickUserProfileIconAdmin() {
		waitElementToBeClickable(userProfileIconAdmin);
		return actionClick(userProfileIconAdmin,"User Profile icon");
//		waitElementToBeClickable(userProfileIcon);
//		return click(userProfileIcon, "User Profile icon");
	}

	/**
	 * This method used to click Dashboard Icon
	 */
	public boolean clickNavMenuDashboard() {
		return click(navMenuDashboard,"Dashboard icon");
	}
	
	/**
	 * This method used to click Employee Icon
	 */
	public boolean clickNavMenuEmployee() {
		return click(navMenuEmployee,"Employee icon");
	}
	
	/**
	 * This method used to click Payroll Icon
	 */
	public boolean clickNavMenuPayroll() {
		return click(navMenuPayroll,"Payroll icon");
	}
	
	/**
	 * This method used to click Reports Icon
	 */
	public boolean clickNavMenuReports() {
		return click(navMenuReports,"Reports icon");
	}
	
	/**
	 * This method used to click HR Documents Icon
	 */
	public boolean clickNavMenuHRDocs() {
		return click(navMenuHRDocs,"HR Documents icon");
	}
	
	/**
	 * This method used to click Performance Icon
	 */
	public boolean clickNavMenuPerformance() {
		return click(navMenuPerformance,"Performance icon");
	}
	
	/**
	 * This method used to click My Profile in side bar
	 */
	public boolean clickSidebarMyProfile() {
		return click(sidebarMyProfile,"My Profile");
	}
	
	/**
	 * This method used to click Switch To Employee in side bar
	 */
	public boolean clickSidebarSwitchToEmployee() {
		return click(sidebarSwitchToEmployee,"Switch To Employee");
	}
	
	/**
	 * This method used to click 'Change Password' in side bar
	 */
	public boolean clickSidebarChangePassword() {
		return click(sidebarChangePassword,"Change Password");
	}
	
	/**
	 * This method used to click 'Email Settings' in side bar
	 */
	public boolean clickSidebarEmailSettings() {
		return click(sidebarEmailSettings,"Email Settings");
	}
	
	/**
	 * This method used to click 'Settings' in side bar
	 */
	public boolean clickSidebarSettings() {
		waitElementToBeClickable(sidebarSettings);
		return click(sidebarSettings,"Settings");
	}
	
	/**
	 * This method used to click 'Accounts' in side bar
	 */
	public boolean clickSidebarAccounts() {
		return click(sidebarAccount,"Accounts");
	}
	
	/**
	 * This method used to click 'Recruitment' in side bar
	 */
	public boolean clickSidebarRecruitment() {
		return click(sidebarRecruitment,"Recruitment");
	}
	
	/**
	 * This method used to click 'Calendar' in side bar
	 */
	public boolean clickSidebarCalendar() {
		return click(sidebarCalendar,"Calendar");
	}
	
	/**
	 * This method used to click 'Logout' in side bar
	 */
	public boolean clickSidebarLogout() {
		return click(sidebarLogout,"Logout");
	}
	
	/**
	 * This method used to click 'Switch To Admin' in side bar
	 */
	public boolean clickSidebarSwitchToAdmin() {
		waitElementToBeClickable(sidebarSwitchToAdmin);
		return click(sidebarSwitchToAdmin,"Switch To Admin");
	}
	
	

	/**
	 * This method used to take ScreenShots
	 * @author shikhar
	 * @param susername
	 * @return boolean
	 */
	
	public static String takeScreenShot(WebDriver driver, String screenShotName, String testName) {
		try {
			File file = new File("Screenshots" + fileSeperator + "Results");
			if (!file.exists()) {
				System.out.println("File created " + file);
				file.mkdir();
			}

			File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File targetFile = new File("Screenshots" + fileSeperator + "Results" + fileSeperator + testName,
					screenShotName);
			FileUtils.copyFile(screenshotFile, targetFile);

			return screenShotName;
		} catch (Exception e) {
			System.out.println("An exception occured while taking screenshot " + e.getCause());
			return null;
		}
	}

	public String getTestClassName(String testName) {
		String[] reqTestClassname = testName.split("\\.");
		int i = reqTestClassname.length - 1;
		System.out.println("Required Test Name : " + reqTestClassname[i]);
		return reqTestClassname[i];
	}

	
	
	
	
	
}