package com.darwinbox.mobile.pages.attendance;

import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class AttendanceRequestLocationPage extends TestBase {

    WaitHelper objWait;
    GenericHelper objGenHelper;
    DropDownHelper objDropDownHelper;
    JavaScriptHelper objJavaScrHelper;
    AlertHelper objAlertHelper;
    WebDriver driver;

    public static final Logger log = Logger.getLogger(AttendanceRequestLocationPage.class);


    public AttendanceRequestLocationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objWait = PageFactory.initElements(driver, WaitHelper.class);
        objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
        objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
        objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
    }

    @FindBy(id = "attendance_request")
    WebElement attendanceReqApplyBtn;

    @FindBy(id = "AttendanceRequestForm_request_type")
    WebElement attendanceReqType;

    @FindBy(id = "AttendanceRequestForm_location")
    WebElement attendanceReqLocation;

    @FindBy(id = "AttendanceRequestForm_edit_reason_id")
    WebElement attendanceReqReason;


    public void attendanceReqApplyBtnClick()
    {
         attendanceReqApplyBtn.click();
     }
    public List getAttendanceReqType()
    {
        List reqTypes = objDropDownHelper.getAllDropDownValues(attendanceReqType);
        return reqTypes;
    }
    public List getAttendanceReqLocation()
    {
        List reqLocations = objDropDownHelper.getAllDropDownValues(attendanceReqLocation);
        return reqLocations;
    }
    public List getAttendanceReqReason()
    {
        List reqReasons = objDropDownHelper.getAllDropDownValues(attendanceReqReason);
        return reqReasons;
    }
}
