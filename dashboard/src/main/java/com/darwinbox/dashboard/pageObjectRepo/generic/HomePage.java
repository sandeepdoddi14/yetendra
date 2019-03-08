/**
 *
 */
package com.darwinbox.dashboard.pageObjectRepo.generic;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Action.ActionHelper;
import com.darwinbox.framework.uiautomation.helper.Browser.BrowserHelper;
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
 * @ClassName: HomePage.java
 * @LastModified_Date: 29 Nov 2017
 */
public class HomePage extends TestBase {

    public static final Logger log = Logger.getLogger(HomePage.class);

    @FindBy(xpath = "//*[@id = 'main-sidebar']")
    protected WebElement mainSideBar;

    @FindBy(xpath = "//div[@class='main-navigation']/div[4]/div[1]/ul/li[2]]")
    protected WebElement userIcon;

    @FindBy(xpath = "//*[@class = 'directory-avatar']")
    protected WebElement userProfileIcon;

    @FindBy(xpath = "//*[@id='main-sidebar']//li[@class = 'close-sb']/a[contains(text() , 'Logout')]")
    protected WebElement userProfileLogout;

    @FindBy(xpath = "//div[@class='right-menu']/ul/li[2]")
    protected WebElement userProfileIconAdmin;

    @FindBy(xpath = "//*[@id='dasboard-bigheader']//a[contains(@class, 'changeaccess')]")
    protected WebElement userProfileDropdownAccessMode;

    @FindBy(xpath = "//div[@class='introjs-tooltip db-intro-step-first']")
    protected WebElement introToolTip;

    @FindBy(xpath = "//div[@class='introjs-tooltip db-intro-step-first']//a[@class='introjs-button introjs-skipbutton']")
    protected WebElement introToolTipSkipButton;

    @FindBy(xpath = "//div[contains(@class,'accessAdminHead')]")
    protected WebElement adminAccessHead;


    WaitHelper objWait;
    GenericHelper objGenHelper;
    WebDriver driver;
    ActionHelper actionHelper;
    BrowserHelper objBrowserHelper;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objWait = PageFactory.initElements(driver, WaitHelper.class);
        objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
        actionHelper = PageFactory.initElements(driver, ActionHelper.class);
        objBrowserHelper = PageFactory.initElements(driver, BrowserHelper.class);
    }

    /**
     * @return
     * @author balaji
     * 30 Nov 201713:03:11
     */
    public boolean verifyProfileIcontDisplay() {

        objWait.waitForElement(userProfileIcon, 5);
        if (objGenHelper.isDisplayed(userProfileIcon, "ProfileIcon"))
            return true;
        return false;
    }

    public boolean clickLogout() {
        return objWait.waitAndClickElement(userProfileLogout, "Logout link");
        //return objGenHelper.elementClick(userProfileLogout, "Logout butotn");
    }

    /**
     * This method used to click UserProfileIcon
     */
    public boolean clickUserProfileIcon() {
        //objWait.waitForPageToLoad();
        actionHelper.moveToElement(driver,userProfileIcon,"User Profile icon");
        return objWait.waitAndClickElement(userProfileIcon, "User Profile icon");
    }

    /**
     * This method will logout from Application
     * @return
     */
    public boolean logout() {
        try {
            objWait.waitAndClickElement(userProfileIcon, "User Profile icon");
            objGenHelper.elementClick(userProfileIcon, "User Profile icon");
            objWait.waitForElement(mainSideBar, 20);
            clickLogout( );
            return true;
        } catch (Exception e) {
            Reporter("Exception while clicking Logout :" + e.getMessage( ), "Fail");
            return false;
        }
    }

    /**
     * s This method used to click UserProfileIcon
     */
    public boolean clickUserProfileIconAdmin() {
//		objWait.waitForElementDisapear(mainSideBar, 10);
        WebElement ele = objWait.waitForElement(2000, userProfileIconAdmin);
        return objGenHelper.elementClick(ele, "User Profile icon");
    }

    public String getUserProfileAccessModeText(){
        return objGenHelper.getTextFromElement(userProfileDropdownAccessMode, "Access Mode");
    }

    public boolean checkIntroToolTipIsDisplayed(){
     return objGenHelper.isDisplayed(introToolTip, "Intro Tool Tip");
    }

    public boolean clickIntroToolTipSkipButton(){
        return objGenHelper.elementClick(introToolTipSkipButton, "Intro Tool Tip Skip button");
    }

    public boolean checkAdminAccessHeadIsDisplayed(){
        return objGenHelper.isDisplayed(adminAccessHead, "Admin Access Head");
    }
}
