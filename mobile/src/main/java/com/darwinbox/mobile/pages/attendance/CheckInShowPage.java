package com.darwinbox.mobile.pages.attendance;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CheckInShowPage extends TestBase {

    WaitHelper objWait;
    GenericHelper objGenHelper;
    DropDownHelper objDropDownHelper;
    JavaScriptHelper objJavaScrHelper;
    AlertHelper objAlertHelper;
    WebDriver driver;

    public static final Logger log = Logger.getLogger(CheckInShowPage.class);


    public CheckInShowPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objWait = PageFactory.initElements(driver, WaitHelper.class);
        objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
        objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
        objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
    }

    @FindBy(xpath = "//div[2]/a[3]")
    WebElement checkInButton;

    @FindBy(xpath = "//tbody[1]/tr[1]/td[2]")
    WebElement presentDays;

    public void checkInBtnClick() {
        checkInButton.click();
    }

    public String getTime(int rowNum)
    {
        String time = driver.findElement(By.xpath("//tbody[1]/tr["+rowNum+"]/td[2]")).getText().toString();
        return time;
    }
    public String getComment(int rowNum)
    {
        String comment = driver.findElement(By.xpath("//tbody[1]/tr["+rowNum+"]/td[4]")).getText().toString();
        return comment;
    }
    public String getLocation(int rowNum)
    {
        String location = driver.findElement(By.xpath("//tbody[1]/tr["+rowNum+"]/td[5]")).getText().toString();
        return location;
    }
}