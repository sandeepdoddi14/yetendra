package com.darwinbox.attendance.pages.settings;

import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class DisplaySettingsPage extends TestBase {

    private WebDriver driver;
    private GenericHelper objGenericHelper;
    private WaitHelper objWaitHelper;
    private DateTimeHelper objDateTimeHelper;
    private UtilityHelper objUtilityHelper;


    public static String dateAndMonth;
    public static String coloumnName;
    public static String header;


    public DisplaySettingsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objGenericHelper = PageFactory.initElements(driver, GenericHelper.class);
        objWaitHelper = PageFactory.initElements(driver, WaitHelper.class);
        objDateTimeHelper = PageFactory.initElements(driver, DateTimeHelper.class);
        objUtilityHelper = PageFactory.initElements(driver, UtilityHelper.class);
    }

    public boolean navigateToEmployeeAttendancePage() {
        try {
            objGenericHelper.navigateTo("/attendance/index/index/view/list");
            objWaitHelper.waitForPageToLoad();
            log.info("Attendance Page is loaded");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean navigateToAttendancePage(String Id) {
        try {
            objGenericHelper.navigateTo("/attendance/index/index/view/list/id/"+Id);
            objWaitHelper.waitForPageToLoad();
            log.info("Attendance Page is loaded for employee "+Id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean headersDisplay(String header) throws InterruptedException{

           try {

               WebElement headersCheck = driver.findElement(By.xpath("//table[@id='attendance_log']//th[contains(text(),'"+header+"')]"));
               return objGenericHelper.isDisplayed(headersCheck,header);

            } catch (Exception e) {
                Reporter(header+" is NOT displayed", "INFO");
                return false;
            }

    }


    @FindBy(xpath = "//select[@id='month']")
    WebElement selectMonth;

    @FindBy(xpath = "//label[contains(text(),'Search:')]//input")
    WebElement search;

    public boolean selectMonth(String month){
     return  objGenericHelper.selectDropdown(selectMonth,month,"Selected month ");
    }

    public boolean searchByDate(String date){
        objWaitHelper.waitElementToBeClickable(search);
        return  objGenericHelper.setElementText(search,"date selected ",date);
    }

public String verifyColoumnValue(Date date, String coloumnName) throws InterruptedException {

    String a = "";

    List<WebElement> headersCheck = driver.findElements(By.xpath("//table[@id='attendance_log']//th"));

    for (int i = 1; i <= headersCheck.size() - 1; i++) {
        objWaitHelper.waitForPageToLoad();
        sleep(3000);
        WebElement particularColoumn = driver.findElement(By.xpath("//table[@id='attendance_log']//th[@class='sorting'][" + i + "]"));
        String text = particularColoumn.getText().toLowerCase();
        try {
            if (text.contains(coloumnName.toLowerCase())) {
sleep(2000);
                Reporter(coloumnName + " is  displayed", "INFO");

                WebElement workTime = driver.findElement(By.xpath("//table[@id='attendance_log']//tr[1]/td[" + (i+1) + "]"));

                a=workTime.getText();

                Reporter("The duration captured is "+a,"INFO");

                if (workTime.getText().isEmpty()) {
                    Reporter("No data recorded.For given date " + dateAndMonth, "FAIL");

                }
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            Reporter(coloumnName + " is NOT displayed", "INFO");
        }
    }
    return a;

}
    /*Logout of application*/

    @FindBy(xpath = "//div[@class='profile-img']//span[@class='directory-avatar']")
    protected WebElement userProfileIcon;

    @FindBy(xpath = "//a[@class='menu-link logout']")
    protected WebElement logout;

    public boolean  logOut(){

        objGenericHelper.elementClick(userProfileIcon,"click on profile icon");
        objGenericHelper.elementClick(logout,"Logged out of application");

        return true;

    }

    /*Policies display on user attendance page*/


    @FindBy(xpath = "//div[@class='modal-content']//h4[contains(text(),'Attendance Policy')]")
    WebElement attendancePolicy;

    public static String policyName;

    public boolean checkPolicyDisplay() throws InterruptedException{

        policyName=getData("PolicyName");
        WebElement policy= driver.findElement(By.xpath("//a[@class='site-color text-underline'][contains(text(),'"+policyName+"')]"));

        policy.click();
        log.info("Policy is clicked");

sleep(2000);

        try {
            if (attendancePolicy.isDisplayed()==true)
                Reporter("Policy is displayed", "INFO");
                return true;

        } catch (org.openqa.selenium.NoSuchElementException e) {
            Reporter("Policy is not displayed", "INFO");
            return true;
        }
    }


    public boolean checkPolicies() throws InterruptedException{

        List<WebElement> elements = driver.findElements(By.xpath("//a[@class='site-color text-underline']"));
        for (int i = 1; i <= elements.size() ; i++) {
sleep(2000);
            WebElement policy= driver.findElement(By.xpath("//a[@class='site-color text-underline']["+i+"]"));

            policy.click();
            log.info("Policy is clicked");

            sleep(2000);

            try {
                if (attendancePolicy.isDisplayed()==true)
                    Reporter("Policy is displayed", "INFO");
                return false;

            } catch (org.openqa.selenium.NoSuchElementException e) {
                Reporter("Policy is not displayed", "INFO");
            }
        }
        return true;
        }






}
