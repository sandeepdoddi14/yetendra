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

import java.util.List;

public class AttendanceViewPage extends TestBase {

    WaitHelper objWait;
    GenericHelper objGenHelper;
    DropDownHelper objDropDownHelper;
    JavaScriptHelper objJavaScrHelper;
    AlertHelper objAlertHelper;
    WebDriver driver;

    public static final Logger log = Logger.getLogger(AttendanceViewPage.class);


    public AttendanceViewPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objWait = PageFactory.initElements(driver, WaitHelper.class);
        objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
        objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
        objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
    }

    @FindBy(xpath="//*[@id='attendance_log']/tbody/tr[1]/td[2]")
    WebElement inTime;

    @FindBy(xpath="//*[@id='attendance_log']/tbody/tr[1]/td[3]")
    WebElement outTime;

    @FindBy(xpath="//*[@id='attendance_log']/tbody/tr[1]/td[4]")
    WebElement workDuration;

    @FindBy(xpath="//*[@id='attendance_log']/tbody/tr[1]/td[7]")
    WebElement status;

    @FindBy(xpath = "//*[@id='attendance_log']/tbody/tr")
    WebElement calendarRowCount;

    @FindBy(xpath = "//*[@id='attendance_log']/thead/tr/th[1]")
    WebElement dateClickToChangeOrder;

    public String getInTime(int i)
    {
        String inTime = driver.findElement(By.xpath("//*[@id='attendance_log']/tbody/tr["+i+"]/td[2]")).getText().toString().trim();
        return inTime;
    }
    public String getOutTime(int i)
    {
        String outTime = driver.findElement(By.xpath("//*[@id='attendance_log']/tbody/tr["+i+"]/td[3]")).getText().toString().trim();
        return outTime;
    }
    public String getWorkDuration(int i)
    {
        String workDuration = driver.findElement(By.xpath("//*[@id='attendance_log']/tbody/tr["+i+"]/td[4]")).getText().toString().trim();
        return workDuration;
    }
    public String getStatus(int i)
    {
        String status = driver.findElement(By.xpath("//*[@id='attendance_log']/tbody/tr["+i+"]/td[7]")).getText().toString().trim();
        return status;
    }

    public int getCalendarRowCount()
    {
        List<WebElement> calendarRowsCount = driver.findElements(By.xpath("//*[@id='attendance_log']/tbody/tr"));
        return calendarRowsCount.size();
    }
    public void clickDateToChangeOrder()
    {
        dateClickToChangeOrder.click();
    }
}
