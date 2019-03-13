package com.darwinbox.mobile.pages.attendance;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

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

    @FindBy(id = "month_list_dropdown")
    WebElement monthDrpDown;

    @FindBy(xpath = "//tbody[1]/tr")
    WebElement rowsCount;

    public void checkInBtnClick() {
        checkInButton.click();
    }
    public int getRowsCount()
    {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//tbody[1]/tr"));
            return rows.size();
        }
        catch (NoSuchElementException e)
        {
            return 0;
        }
    }
    public String getTime(int rowNum)
    {
        try {
        objGenHelper.checkVisbilityOfElement(driver.findElement(By.xpath("//tbody[1]/tr["+rowNum+"]/td[2]")), "Check-IN time ");
            WebElement element = driver.findElement(By.xpath("//tbody[1]/tr["+rowNum+"]/td[2]"));
            String time = element.getText().toString();
            return time;
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }
    public String getComment(int rowNum)
    {
        WebElement element = driver.findElement(By.xpath("//tbody[1]/tr["+rowNum+"]/td[4]"));
        if(objGenHelper.checkVisbilityOfElement(element, "Comment  "))
        {
            String comment = element.getText().toString();
            return comment;
        }
        else
        {
            return "false";
        }
    }
    public String getLocation(int rowNum)
    {
        WebElement element = driver.findElement(By.xpath("//tbody[1]/tr["+rowNum+"]/td[5]"));
        if(objGenHelper.checkVisbilityOfElement(element, "location  "))
        {
            String location = element.getText().toString();
            return location;
        }
        else
        {
            return "false";
        }
    }
    public void selectMothFilter(int requiredMonth)
    {
        objDropDownHelper.selectUsingIndex(monthDrpDown,requiredMonth, "Month Filter");
    }
}