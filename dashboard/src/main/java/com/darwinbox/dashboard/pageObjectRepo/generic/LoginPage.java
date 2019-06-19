/**
 * 
 */
package com.darwinbox.dashboard.pageObjectRepo.generic;

import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * @author balaji
 * @Creation_Date:  29 Nov 2017 
 * @ClassName: LoginPage.java
 * @LastModified_Date:  29 Nov 2017 
 */
public class LoginPage extends TestBase {
		
	private static final Logger log = Logger.getLogger(LoginPage.class);
	WebDriver driver;
	GenericHelper objGenHelper;
	WaitHelper objWaitHelper;
	
	public LoginPage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
	}
	
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
	
	
	public boolean EnterUsername(String susername) {
		return objGenHelper.setElementText (username, "User ID", susername);
	}
	
	public boolean EnterPassword(String spassword) {
		return objGenHelper.setElementText(password, "Password", spassword);
	}
	
	public boolean clickSignIn() throws NoSuchElementException {
		try {

			return objGenHelper.elementClick(signIn, "SignIn button");
						
		} catch (NoSuchElementException e) {
			if (signInMsg.isDisplayed()) {
				Reporter("Sign In failed with error message: " + signInMsg.getText(), "Fail");
				throw new RuntimeException("SignIn Failed");
			} else {
				return false;
			}
		}
	}

	
	/**
	 * This method for login to Application
	 * 
	 * @author shikhar
	 * @param sUserName
	 * @return boolean
	 */
	public boolean loginToApplication(String sUserName, String sPassword) throws Exception {
		try {
			EnterUsername(sUserName);
			EnterPassword(sPassword);
			clickSignIn();
			objWaitHelper.waitForPageToLoad();
			return true;
		} catch (Exception e) {
			Reporter("Exception while logging to application :" + e.getMessage(), "Error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method for login to Application
	 * 
	 * @author shikhar

	 * @return boolean
	 */
	public boolean loginToApplication() {
		try {
			EnterUsername(data.get("@@admin"));
			EnterPassword(data.get("@@password"));
			clickSignIn();
			objWaitHelper.waitForPageToLoad();
			return true;
		} catch (Exception e) {
			Reporter("Exception while signing to application :" + e.getMessage(), "Error");
			e.printStackTrace();
			return false;
		}
	}

	public boolean loginToApplicationAsAdmin() {
		try {
			EnterUsername(data.get("@@admin"));
			EnterPassword(data.get("@@password"));
			clickSignIn();
			objWaitHelper.waitForPageToLoad();
			return true;
		} catch (Exception e) {
			Reporter("Exception while signing to application :" + e.getMessage(), "Error");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method for login to Application for a Employee
	 *
	 * @author sandeep

	 * @return boolean
	 */
	public boolean empLoginToApplication() {
		try {
			EnterUsername(UtilityHelper.getProperty("config", "Employee.email"));
			EnterPassword(UtilityHelper.getProperty("config", "Employee.pass"));
			clickSignIn();
			objWaitHelper.waitForPageToLoad();
			return true;
		} catch (Exception e) {
			Reporter("Exception while signing to application :" + e.getMessage(), "Error");
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * This method for login to Application
	 *
	 * @author Abhilash

	 * @return boolean
	 */
	public boolean loginAsEmployee(String username, String password) {
		try {
			EnterUsername(username);
			EnterPassword(password);
			clickSignIn();
			objWaitHelper.waitForPageToLoad();
			return true;
		} catch (Exception e) {
			Reporter("Exception while signing to application :" + e.getMessage(), "Error");
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * This method used to verify invalid Sign in
	 * 
	 * @author shikhar
	 * @param errorMsg
	 * @return boolean
	 */
	public boolean checkInvalidSignIn(String errorMsg) throws NoSuchElementException {
		try {
			objGenHelper.elementClick(signIn, "SignIn button");
			if (signInMsg.isDisplayed()) {
				if (signInMsg.getText().trim().contentEquals(errorMsg.trim())) {
					Reporter("Actual error message: '" + signInMsg.getText() + "' is equal to expected error message",
							"Pass");
					return true;
				} else {
					Reporter("Actual error message: '" + signInMsg.getText()
							+ "' is not equal to expected error message: '" + errorMsg + "'", "Fail");
					return false;
				}
			} else {
				Reporter("Error Message not displayed", "Fail");
				return false;
			}

		} catch (NoSuchElementException e) {
			if (userProfileIcon.isDisplayed()) {
				Reporter("User successfully logged in to the application", "Error");
				return false;
			} else {
				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean switchToAdmin() {

		int retry = 2;
		boolean switched = false;
		while(retry >= 0 ) {
			objGenHelper.navigateTo("/dashboard/changeAccess");
			objGenHelper.navigateTo("/dashboard");
			objGenHelper.sleep(2);
			switched = driver.getPageSource().contains("Switch to Employee");
			if (switched)
				return switched;
			retry--;
		}
		return switched;

	}


}
