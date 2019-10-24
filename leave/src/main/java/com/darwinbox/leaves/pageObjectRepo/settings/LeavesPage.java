package com.darwinbox.leaves.pageObjectRepo.settings;


import com.darwinbox.core.Services;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Alert.AlertHelper;
import com.darwinbox.framework.uiautomation.helper.Dropdown.DropDownHelper;
import com.darwinbox.framework.uiautomation.helper.Javascript.JavaScriptHelper;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LeavesPage extends TestBase {
    WaitHelper objWait;
    GenericHelper objGenHelper;
    DropDownHelper objDropDownHelper;
    JavaScriptHelper objJavaScrHelper;
    AlertHelper objAlertHelper;
    WebDriver driver;

    public static final Logger log = Logger.getLogger(LeavesPage.class);
    public LocalDate[] workingDays = null;
    public String[] restrictDays = null;

    //intializes driver instance to helper objects
    public LeavesPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        objWait = PageFactory.initElements(driver, WaitHelper.class);
        objGenHelper = PageFactory.initElements(driver, GenericHelper.class);
        objDropDownHelper = PageFactory.initElements(driver, DropDownHelper.class);
        objAlertHelper = PageFactory.initElements(driver, AlertHelper.class);
    }

    /*
     * Leaves Page Object Repository
     */

    @FindBy(xpath = "//*[@id='message_leave']")
    private WebElement applyForLeave;

    @FindBy(xpath = "//*[@id='leave_change']")
    private WebElement selectLeaveType;

    @FindBy(xpath = "//*[@id='from_date']")
    private WebElement fromDate;

    @FindBy(xpath = "//*[@id='to_date']")
    private WebElement toDate;

    @FindBy(xpath = "//*[@id='message-box-text']")
    private WebElement messageBox;

    @FindBy(xpath = "//*[@id='total_days_app']")
    private WebElement totalLeavesApplied;

    @FindBy(xpath = "//*[@id='message_submit_btn_leaves']")
    private WebElement submitLeavesButton;

    @FindBy(xpath = "//*[@id='applyForOther']")
    private  WebElement applyForOthers;

    @FindBy(xpath = "//strong[@id='dashboard_module_error_data']")
    private WebElement errorMessage;

    @FindBy(xpath = "//*[@id='ui-datepicker-div']/table/tbody/tr")
    private List<WebElement> fromDateTable;

    @FindBy(xpath = "//*[@id='ui-datepicker-div']/div/div/select[1]")
    private WebElement fromMonth;

    @FindBy(xpath = "//*[@id='ui-datepicker-div']/div/div/select[2]")
    private WebElement fromYear;

    @FindBy(xpath = "//*[@id='ui-datepicker-div']/div/div/select[1]")
    private WebElement toMonth;

    @FindBy(xpath = "//*[@id='ui-datepicker-div']/div/div/select[2]")
    private WebElement toYear;

    @FindBy(xpath = "//*[@class='request-body comments-left-discription clickme']")
    private List<WebElement> requests;

    @FindBy(xpath = "//a[contains(text(),'Revoke Your Request')]")
    private WebElement revokeRequest;

    @FindBy(xpath = "//*[@id='total_days_app']")
    private WebElement totalDaysApplied;

    @FindBy(xpath = "//*[@id='employee-search-2']")
    private WebElement empSearch;

            // Naviagte to leaves Page
    public boolean navigateToLeavePage() {
        objGenHelper.navigateTo("/leaves");
        return true;

    }

    //clicks on apply leave button
    public void clickOnApplyForLeaveButton() {
        objGenHelper.elementClick(applyForLeave, "APPLY FOR LEAVE"
        );
    }

    //apply on behalf search
    public void searchEmployee(String empId) throws  Exception{
      objGenHelper.setElementText(empSearch,"search employee with id",empId);
      Thread.sleep(2000);
        Actions a= new Actions(driver);
        a.sendKeys(Keys.ARROW_DOWN);
        a.sendKeys(Keys.ENTER).build().perform();

    }

    //Apply Leave
    //Click On Apply Leave,Fi1ll Form,Submit
    public boolean applyLeave() {
        try {
            clickOnApplyForLeaveButton();
            Thread.sleep(2000);
            selectLeaveType(data.get("SelectLeaveType"));
            Thread.sleep(2000);
            selectFromDate(new DateTimeHelper().changeDateFormatForTable(workingDays[0]));
            Thread.sleep(2000);
            selectToDate(new DateTimeHelper().changeDateFormatForTable(workingDays[workingDays.length - 1]));
            Thread.sleep(2000);
            typeMessage(data.get("Message"));
            Thread.sleep(2000);
            verifyNoOfLeaveDays(workingDays.length);
            submitLeave();
            Thread.sleep(4000);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while applying leave" + e.getMessage());
        }

    }

    //apply leaves multiple times based on restrict week days
    public boolean applyLeaveForRestrictDays() {
        try {
            for (String day : restrictDays) {
                LocalDate tempDate;
                for (int i = 0; i < 7; i++) {
                    tempDate = LocalDate.now().plusDays(i);
                    if (tempDate.getDayOfWeek().toString().equalsIgnoreCase(day)) {
                        workingDays = new LocalDate[1];
                        workingDays[0] = tempDate;
                        applyLeave();
                        if (data.get("properties").trim().contains("cant apply leave"))
                            Assert.assertTrue(errorMessage().equalsIgnoreCase(data.get("ExpectedMessage").trim()), "Expected and Actual Error Message are Different");

                        else if (data.get("properties").trim().contains("can apply leave"))
                            Assert.assertFalse(isErrorMessageDisplayed(), "Error Message Model is Displayed");
                        break;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while applying leave" + e.getMessage());
        }

    }



    //Verifies the No of leaves applied in submit leave form
    public void verifyNoOfLeaveDays(int days) {
        Assert.assertTrue(objGenHelper.getTextFromElement(totalDaysApplied, "No of Working Days").equalsIgnoreCase(days + ""));
    }

    public void submitLeave() {
        objGenHelper.elementClick(submitLeavesButton, "Apply");
    }

    public void selectLeaveType(String leavetype) {
        objGenHelper.selectDropdown(selectLeaveType, leavetype, "Selecting Leave Type");
    }

    //selects the table with the given date
    public void selectTable(String day) {
        Boolean dayIndicagtor = false;

        String table = "//*[@id='ui-datepicker-div']/table/tbody/tr";

        List<WebElement> rows = driver.findElements(By.xpath(table));
        for (int i = 1; i <= rows.size(); i++) {
            List<WebElement> cells = driver.findElements(By.xpath(table + "[" + i + "]" + "/td"));
            for (int j = 1; j <= cells.size(); j++) {
                WebElement cell = driver.findElement(By.xpath(table + "[" + i + "]" + "/td" + "[" + j + "]"));
                if (cell.getText().contains(day)) {
                    cell.click();
                    dayIndicagtor = true;
                    break;
                }
            }
            if (dayIndicagtor) {
                break;
            }
        }
    }


    public void clickOnApplyForOthers(){
        objGenHelper.elementClick(applyForOthers,"Apply leave For others");
    }
    //checks if date is enabled
    public boolean checkIfDateisEnabled(String date) throws Exception{

        String day = date.split("-")[0];
        String month = date.split("-")[1];
        String year = date.split("-")[2];

        Thread.sleep(2000);
        objGenHelper.elementClick(fromDate, "From");
        objGenHelper.selectDropdown(fromMonth, month, "fromMonth DropDown");
        objGenHelper.selectDropdown(fromYear, year, "fromYear DropDown");

        Boolean dayIndicagtor = false;

        String table = "//*[@id='ui-datepicker-div']/table/tbody/tr";

        List days = new ArrayList();

        List<WebElement> rows = driver.findElements(By.xpath(table));
        for (int i = 1; i <= rows.size(); i++) {
            List<WebElement> cells = driver.findElements(By.xpath(table + "[" + i + "]" + "/td"));
            for (int j = 1; j <= cells.size(); j++) {
                WebElement cell = driver.findElement(By.xpath(table + "[" + i + "]" + "/td" + "[" + j + "]"));
                if (cell.getText().contains(day)) {
                 if(cell.getAttribute("class").contains("disabled"))
                     return false;
                 else
                     return true;
                }
            }
        }
       return false;
    }

    //selects from date in submit leave form
    public void selectFromDate(String date) {
        String day = date.split("-")[0];
        String month = date.split("-")[1];
        String year = date.split("-")[2];

        objGenHelper.elementClick(fromDate, "From");
        objGenHelper.selectDropdown(fromMonth, month, "fromMonth DropDown");
        objGenHelper.selectDropdown(fromYear, year, "fromYear DropDown");

        selectTable(day);

        objGenHelper.selectDropdown(fromMonth, month, "fromMonth DropDown");
        objGenHelper.selectDropdown(fromYear, year, "fromYear DropDown");
    }

    //selects to date in submit leave form
    public void selectToDate(String date) {
        String day = date.split("-")[0];
        String month = date.split("-")[1];
        String year = date.split("-")[2];

        objGenHelper.elementClick(toDate, "From");
        objGenHelper.selectDropdown(toMonth, month, "fromMonth DropDown");
        objGenHelper.selectDropdown(toYear, year, "fromYear DropDown");

        selectTable(day);

        objGenHelper.selectDropdown(fromMonth, month, "fromMonth DropDown");
        objGenHelper.selectDropdown(fromYear, year, "fromYear DropDown");
    }

    //initiates the workings days according to the properties in excel sheet
    public void createNumberOfDays(int numberOfWorkingDays) {
       if (data.get("properties").toLowerCase().contains("equal"))
            workingDays = new LocalDate[numberOfWorkingDays];
         else if (data.get("properties").toLowerCase().contains("more"))
            workingDays = new LocalDate[numberOfWorkingDays + 1];
        else if (data.get("properties").toLowerCase().contains("less"))
            workingDays = new LocalDate[numberOfWorkingDays - 1];
       else
            workingDays= new LocalDate[numberOfWorkingDays];
    }

    public void createNumberOfDaysWithoutProperty(int numberOfWorkingDays) {
            workingDays= new LocalDate[numberOfWorkingDays];
    }


    //checks if the given day is a holiday
    //inserts days into working days
    public void checkForHolidaysAndSetDays() {
        String holidays = getHolidaysForUser(UtilityHelper.getProperty("config", "Employee.userId"));
        LocalDate today = LocalDate.now();
        int skips = 0;
        for (int i = 0; i < workingDays.length; i++) {
            LocalDate tempDate = null;
            if (skips == 0) {
                for (int j = 0; j < 365; j++) {
                    tempDate = today.plusDays(j + i);
                    if (!holidays.toLowerCase().contains(new DateTimeHelper().changeDateFormatForHolidays(tempDate).toLowerCase())) {
                        break;
                    } else {
                        skips = skips + 1;
                    }
                }
                workingDays[i] = tempDate;
            } else {
                for (int j = 0; j < 365; j++) {
                    tempDate = today.plusDays(skips + j + i);
                    if (!holidays.toLowerCase().contains(new DateTimeHelper().changeDateFormatForHolidays(tempDate).toLowerCase())) {
                        break;
                    } else {
                        skips = skips + 1;
                    }
                }
                workingDays[i] = tempDate;
            }
        }
    }

    //start days from given date
    public void checkForHolidaysAndSetDays(LocalDate leaveStartToday) {
        String holidays = getHolidaysForUser(UtilityHelper.getProperty("config", "Employee.userId"));
        LocalDate today = leaveStartToday;
        int skips = 0;
        for (int i = 0; i < workingDays.length; i++) {
            LocalDate tempDate = null;
            if (skips == 0) {
                for (int j = 0; j < 365; j++) {
                    tempDate = today.plusDays(j + i);
                    if (!holidays.toLowerCase().contains(new DateTimeHelper().changeDateFormatForHolidays(tempDate).toLowerCase())) {
                        break;
                    } else {
                        skips = skips + 1;
                    }
                }
                workingDays[i] = tempDate;
            } else {
                for (int j = 0; j < 365; j++) {
                    tempDate = today.plusDays(skips + j + i);
                    if (!holidays.toLowerCase().contains(new DateTimeHelper().changeDateFormatForHolidays(tempDate).toLowerCase())) {
                        break;
                    } else {
                        skips = skips + 1;
                    }
                }
                workingDays[i] = tempDate;
            }
        }
    }


    //retives Holidays Bases on User Id
    public String getHolidaysForUser(String userId) {
        HashMap<String, String> h = new HashMap<>();
        h.put("x-requested-with", "XMLHttpRequest");

        Services s = new Services();
        String applicationUrl = data.get("@@url");
        return s.doGet(applicationUrl + "/settings/holidayslist?user_id=" + userId, h);
    }

    public boolean setFromAndToDates(int numberOfWorkingDays) {
        try {
            createNumberOfDays(numberOfWorkingDays);
            checkForHolidaysAndSetDays();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while setting From and To Dates" + e.getMessage());
        }
    }

    public boolean setFromAndToDatesWithoutProperty(int numberOfWorkingDays,LocalDate leaveStartDate){
        try {
            createNumberOfDaysWithoutProperty(numberOfWorkingDays);
            checkForHolidaysAndSetDays(leaveStartDate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while setting From and To Dates" + e.getMessage());
        }
    }


    public boolean setRestrictDays(String days) {
        restrictDays = days.split(",");
        return true;
    }

    public boolean setFromAndToDatesForRestrictDays(int numberOfWorkingDays) {
        try {
            createNumberOfDays(numberOfWorkingDays);
            checkForHolidaysAndSetDays();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while setting From and To Dates" + e.getMessage());
        }
    }


    public void typeMessage(String message) {
        objGenHelper.setElementText(messageBox, "Message Box", message);
    }

    public String errorMessage() {
        try {
            return objGenHelper.getTextFromElement(errorMessage, "Error Message Container");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while finding Error Message Container" + e.getMessage());
        }
    }

    //checks fot the error message container
    public boolean isErrorMessageDisplayed() {
        try {
            return objGenHelper.isDisplayed(errorMessage, "Error Message Container");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isApplyLeaveButtonDisplayed() {
        return objGenHelper.isDisplayed(applyForLeave, "Error Message Container");
    }

    //navigates to requests page
    public boolean navigateToReqestTask() {
        objGenHelper.navigateTo("/dashboard/requesttask");
        return true;
    }

    //revokes all the requests of user
    public boolean revokeRequests()  {
        try {
            for (WebElement request : requests) {
                objGenHelper.elementClick(request, "Revoke Request");
                Thread.sleep(2000);
                objGenHelper.elementClick(revokeRequest, "Revoke Request");
                Thread.sleep(2000);
            }
        } catch (NoSuchElementException e) {
            Reporter("No Pending Requests Found", "OK");
            return true;
        } catch (InterruptedException e){
            Reporter("Java Wait is Interrupted", "OK");
            return true;
        }

        return true;
    }
}
