/**
 * 
 */
package com.darwinbox.test.hrms.uiautomation.Pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.darwinbox.test.hrms.uiautomation.configreader.ObjectRepo;
import com.darwinbox.test.hrms.uiautomation.helper.TestBase.TestBase;
import com.darwinbox.test.hrms.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.test.hrms.uiautomation.helper.genericHelper.GenericHelper;

/**
 * @author balaji
 * @Creation_Date:  29 Nov 2017 
 * @ClassName: LoginPage.java
 * @LastModified_Date:  29 Nov 2017 
 */
public class LoginPage extends TestBase{
		
	private static final Logger log = Logger.getLogger(LoginPage.class);
	WebDriver driver;
	GenericHelper objGenHelper;
	WaitHelper objWaitHelper;
	
	public LoginPage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);
		objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
		
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
//	public void loginToApplication(String emailAddress,String loginPassword){
//		
//		log("cliked on sign in and object is: "+signIn.toString());
//		username.sendKeys(emailAddress);
//		log("entered email address: "+emailAddress+" and object is "+username.toString());
//		password.sendKeys(loginPassword);
//		log("entered password:"+password+" and object is "+loginPassword.toString());
//		signIn.click();
//		log("clicked on sublit butto and object is: "+signIn.toString());
//	}
	
	/**
	 * This method for login to Application
	 * 
	 * @author shikhar
	 * @param susername
	 * @return boolean
	 */
	public boolean loginToApplication(String sUserName, String sPassword) throws Exception {
		try {
			EnterUsername(sUserName);
			EnterPassword(sPassword);
			clickSignIn();
			return true;
		} catch (Exception e) {
			Reporter("Exception while ing to application :" + e.getMessage(), "Fail");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * This method for login to Application
	 * 
	 * @author shikhar
	 * @param susername
	 * @return boolean
	 */
	public boolean loginToApplication() throws Exception {
		try {
			EnterUsername(ObjectRepo.reader.getAdminUserName());
			EnterPassword(ObjectRepo.reader.getAdminPassword());
			clickSignIn();
			return true;
		} catch (Exception e) {
			Reporter("Exception while ing to application :" + e.getMessage(), "Fail");
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * This method used to verify invalid Sign in
	 * 
	 * @author shikhar
	 * @param susername
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
				Reporter("User successfully ed in to the application", "Fail");
				return false;
			} else {
				e.printStackTrace();
				return false;
			}
		}
	}

}
