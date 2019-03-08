package com.darwinbox.mobile.pages.attendance;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AttendanceSummaryPage extends TestBase {

    WaitHelper objWait;
    GenericHelper objGenHelper;
    DropDownHelper objDropDownHelper;
    JavaScriptHelper objJavaScrHelper;
    AlertHelper objAlertHelper;
    WebDriver driver;

    public static final Logger log = Logger.getLogger(AttendanceSummaryPage.class);


    public AttendanceSummaryPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objWait = PageFactory.initElements(driver, WaitHelper.class);
        objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
        objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
        objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
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
    public String getShiftTimings()
    {
        String string_1 = shift.getText();
        String shift[] = string_1.split("\\r?\\n");
        String shiftTimings = shift[1];
        shiftTimings = shiftTimings.replaceAll("[\\[\\](){}]","");
        return shiftTimings;
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
}
