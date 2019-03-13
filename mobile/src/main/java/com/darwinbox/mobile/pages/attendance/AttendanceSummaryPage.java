package com.darwinbox.mobile.pages.attendance;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Action.ActionHelper;
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

public class AttendanceSummaryPage extends TestBase {

    WaitHelper objWait;
    GenericHelper objGenHelper;
    DropDownHelper objDropDownHelper;
    JavaScriptHelper objJavaScrHelper;
    AlertHelper objAlertHelper;
    ActionHelper objActionHelper;
    WebDriver driver;

    public static final Logger log = Logger.getLogger(AttendanceSummaryPage.class);


    public AttendanceSummaryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objWait = PageFactory.initElements(driver, WaitHelper.class);
        objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
        objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
        objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
        objActionHelper = PageFactory.initElements(driver, ActionHelper.class);
    }

    @FindBy(id = "total_leave")
    WebElement leaveDays;

    @FindBy(id = "total_present")
    WebElement presentDays;

    @FindBy(id = "total_absent")
    WebElement absentDays;

    @FindBy(xpath = "//div[1]/small[1]")
    WebElement clockInPriority;

    @FindBy(xpath = "//div[2]/small")
    WebElement shift;

    @FindBy(xpath = "//div[3]/small/a")
    WebElement policyName;

    @FindBy(xpath = "//div[4]/small")
    WebElement weeklyOff;

    @FindBy(xpath = "//div[1]/small[2]")
    WebElement checkIN;


    public String getLeaveDays()
    {
        return leaveDays.getText();
    }
    public String getPresentDays()
    {
        return presentDays.getText();
    }
    public String getAbsentDays()
    {
        return absentDays.getText();
    }
    public String getClockInPriority()
    {
        return clockInPriority.getText();
    }
    public String getShiftName()
    {
        String string_1 = shift.getText();
        String shift[] = string_1.split("\\r?\\n");
        String shiftName = shift[0];
        return shiftName;
    }
    public String getStartShiftTimings()
    {
        String string_1 = shift.getText();
        String shift[] = string_1.split("\\r?\\n");
        String shiftTimings = shift[1];
        shiftTimings = shiftTimings.replaceAll("[\\[\\](){}-]","");
        String shiftTimings1[] = shiftTimings.split(" ");
        return shiftTimings1[1];
    }
    public String getEndShiftTimings()
    {
        String string_1 = shift.getText();
        String shift[] = string_1.split("\\r?\\n");
        String shiftTimings = shift[1];
        shiftTimings = shiftTimings.replaceAll("[\\[\\](){}-]","");
        String shiftTimings1[] = shiftTimings.split(" ");
        return shiftTimings1[3];
    }
    public String getPolicyName()
    {
        return policyName.getText();
    }
    public String getWeeklyOffName()
    {
        String string_1 = weeklyOff.getText();
        String weeklyOff[] = string_1.split("\\r?\\n");
        String weeklyOffName = weeklyOff[0];
        return weeklyOffName;
    }
    public String getWeeklyOffDesc()
    {
        String string_1 = weeklyOff.getText();
        String weeklyOff[] = string_1.split("\\r?\\n");
        String weeklyOffDesc = weeklyOff[1];
        weeklyOffDesc = weeklyOffDesc.replaceAll("[\\[\\](){}]","");
        return weeklyOffDesc;
    }
    public void clickAttendancePolicy()
    {
        objActionHelper.actionClick(driver, policyName, "'click on policy");
        objGenHelper.sleep(2);
    }
    public int getRowsCount()
    {
        try {
            List<WebElement> rows = driver.findElements(By.xpath("//*[@id='myPolicy']/div/div/div[2]/table/tbody/tr"));
            return rows.size();
        }
        catch (NoSuchElementException e)
        {
            return 0;
        }
    }
    public String getPolicyAttribute(int rowNum)
    {
        try {
                WebElement element = driver.findElement(By.xpath("//*[@id='myPolicy']/div/div/div[2]/table/tbody/tr[" + rowNum + "]/td[" + 1 + "]"));
                String policyAttribute = element.getText().toString();
                return policyAttribute;
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }
    public String getPolicyStatus(int rowNum)
    {
        try {
            WebElement element = driver.findElement(By.xpath("//*[@id='myPolicy']/div/div/div[2]/table/tbody/tr[" + rowNum + "]/td[" + 2 + "]"));
            String policyStatus = element.getText().toString();
            return policyStatus;
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }
    public String getPolicyDescription(int rowNum)
    {
        try {
            WebElement element = driver.findElement(By.xpath("//*[@id='myPolicy']/div/div/div[2]/table/tbody/tr[" + rowNum + "]/td[" + 3 + "]"));
            String policyDescription = element.getText().toString();
            return policyDescription;
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }
    public String getCheckINStatus()
    {
        try {
            if (objGenHelper.checkVisbilityOfElement(checkIN, "CheckIN Active/InActive")) {
                return "1";
            } else {
                return "0";
            }
        }catch (NoSuchElementException e)
        {
            return "0";
        }
    }
}
