package com.darwinbox.attendance.pages.settings;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

/**
 * Page for Shifts to handle all Shift Actions
 * Should be on Shifts Page for the same
 * @author hkonakanchi ( Harsha Konakanchi - harsha.konakanchi@darwinbox.io )
 * @Date 28 Dec 2018
 */

public class ShiftsPage extends TestBase {

    GenericHelper genHelper;

    /**
     * Create Shift Button
     */
    @FindBy(id = "create_shift_btn")
    private WebElement createShift;

    /**
     * Shift Name text box
     */
    @FindBy(id = "TenantShiftForm_shift_name")
    private WebElement shiftName;

    /**
     * Shift Description text box
     */
    @FindBy(id = "TenantShiftForm_shift_description")
    private WebElement shiftDescription;

    /**
     * Group company dropdown
     */
    @FindBy(id = "dept_grp_company")
    private WebElement groupCompany;

    /**
     * Shift Start Time Hours Dropdown
     */
    @FindBy(id = "TenantShiftForm_begin_time_hour")
    private WebElement beginTimeHours;

    /**
     * Shift Start Time Minutes Dropdown
     */
    @FindBy(id = "TenantShiftForm_begin_time_min")
    private WebElement beginTimeMins;

    /**
     * Shift End Time Hours Dropdown
     */
    @FindBy(id = "TenantShiftForm_end_time_hour")
    private WebElement endTimeHours;

    /**
     * Shift End Time Minutes Dropdown
     */
    @FindBy(id = "TenantShiftForm_end_time_min")
    private WebElement endTimeMins;

    /**
     * Save Button in Edit Mode
     */
    @FindBy(id = "shift_btn")
    private WebElement saveShift;

    /**
     * Cancel Button in Edit Mode
     */
    @FindBy(id = "cancel_shift")
    private WebElement cancelShift;

    /**
     * Next Day - Checkbox
     */
    @FindBy(id = "TenantShiftForm_is_next_day")
    private WebElement nextDay;

    /**
     * Show in Request - Checkbox
     */
    @FindBy(id = "TenantShiftForm_show_in_request")
    private WebElement showInRequest;

    /**
     * Attendance Policy dropdown - for tagging
     */
    @FindBy(id = "att_policy_id")
    private WebElement attendancePolicy;

    /**
     * Error Messages
     */
    @FindBy(id = "input-error")
    private WebElement shiftErrorMsg;

    /**
     * Search Filter in Shifts Page
     */
    @FindBy(css = "#shift_table_filter input")
    private WebElement shiftSearchFilter;

    /**
     * Constructor to make use of Shift Page Object
     * @param driver Webdriver object as param
     */
    public ShiftsPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        genHelper = new GenericHelper(driver);
    }

    /**
     * Set Shift Description
     * @param description Description of shift to be set
     * @return Returns true on success
     */
    public boolean setShiftDescription(String description) {
        return (genHelper.setElementText(shiftDescription, "Shift Description", description));
    }

    /**
     * Set Shift Name
     * @param shiftname Name of Shift
     * @return Returns true on success
     */
    public boolean setShiftname(String shiftname) {
        return (genHelper.setElementText(shiftName, "Shift Name", shiftname));
    }

    /**
     * Select Group company of shift
     * @param companyName Name of company
     * @return Returns true on success
     */
    public boolean selectGroupCompany(String companyName) {
        return (genHelper.selectDropdown(groupCompany, companyName, "Company Name"));
    }

    /**
     * Set Shift start time hours
     * @param beginTimeHrs Shift Begin time in Hours
     * @return Returns true on success
     */
    public boolean selectBeginTimeHrs(String beginTimeHrs) {
        return (genHelper.selectDropdown(beginTimeHours, beginTimeHrs, "Shift Begin Time Hrs"));
    }

    /**
     * Set shift start time Minutes
     * @param startTimeMins Shift Begin time in Mins
     * @return Returns true on success
     */
    public boolean selectBeginTimeMins(String startTimeMins) {
        return (genHelper.selectDropdown(beginTimeMins, startTimeMins, "Shift Begin Time Mins"));
    }

    /**
     * Set Shift end time in Hours
     * @param endTimeHrs Shift End time in Hours
     * @return Returns true on success
     */
    public boolean selectEndTimeHrs(String endTimeHrs) {
        return (genHelper.selectDropdown(endTimeHours, endTimeHrs, "Shift End Time Hrs"));
    }

    /**
     * Set Shift end time in Minutes
     * @param endTimeMinutes Shift end time in minutes
     * @return Returns true on success
     */
    public boolean selectEndTimeMins(String endTimeMinutes) {
        return (genHelper.selectDropdown(endTimeMins, endTimeMinutes, "Shift End Time Mins"));
    }

    /**
     * Set whether Shift is a next day shift or not
     * @param isNextDay true if Shift is a next day shift else false
     * @return Returns true on success
     */
    public boolean setNextDay(boolean isNextDay) {
        return genHelper.toggleElement(nextDay,isNextDay,"Next Day Checkbox");
    }

    /**
     * Set Whether the shift must be shown as part of shift change requests or not
     * @param showInShiftChangeRequest true if Shift needs to be shown in Shift change request
     * @return Return true on success
     */
    public boolean setShowinShiftChange(boolean showInShiftChangeRequest) {
        return genHelper.toggleElement(showInRequest,showInShiftChangeRequest,"Shift Change request Checkbox");
    }

    /**
     * Set the policy to be tagged for this shift
     * @param policy Name of the policy
     * @return Returns true on Success
     */

    public boolean selectPolicy(String policy) {
        return (genHelper.selectDropdown(attendancePolicy, policy, "Policy to be tagged"));
    }

    /**
     *
     * @return Returns true on Success
     */
    public boolean clickCreateShift(){
        return genHelper.elementClick(createShift,"Create Shift");
    }


    public boolean clickSaveShift(){
        return genHelper.elementClick(saveShift,"Save Shift");
    }


    public boolean clickCancelShift(){
        return genHelper.elementClick(cancelShift,"Cancel Shift");
    }

    /**
     * Fill Shift form with details present in HashMap
     * @param data Dictionary having Shift name, Description, Begin Time , End Time and other data
     * @return Return true on Success
     */
    public boolean fillShiftData(Map<String,String> data) {

        try {

            String name = data.get("Name");
            String beginTime = data.get("BeginTime");
            String endTime = data.get("EndTime");

            setShiftname(name);
            selectBeginTimeHrs(beginTime.split(":")[0]);
            selectBeginTimeMins(beginTime.split(":")[1]);
            selectEndTimeHrs(endTime.split(":")[0]);
            selectEndTimeMins(endTime.split(":")[1]);

            if (data.containsKey("Company")) {
                String groupCompany = data.get("Company");
                selectGroupCompany(groupCompany);
            }

            if ( data.containsKey("Description")) {
                String desc = data.get("Description");
                setShiftDescription(desc);
            }

            if (data.containsKey("NextDay")) {
                boolean isNextDay = Boolean.parseBoolean(data.get("NextDay"));
                setNextDay(isNextDay);
            }

            if (data.containsKey("ShiftChange")) {
                boolean isShiftChangeReq = Boolean.parseBoolean(data.get("ShiftChange"));
                setNextDay(isShiftChangeReq);
            }

            if (data.containsKey("Policy")) {
                String policy = data.get("Policy");
                selectPolicy(policy);
            }

        } catch(Exception e){
            e.printStackTrace();
            Reporter("Error while filling Shift Data ", "Error");
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * Create a shift
     * @param data
     * @return
     */
    public boolean createShift(Map<String,String> data) {

        try {
            clickCreateShift();
            fillShiftData(data);
            clickSaveShift();
        } catch(Exception e){
            e.printStackTrace();
            Reporter("Error while filling Shift Data ", "Error");
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * Set Shift Name for search in Shift Search Text Box
     * @param shiftName
     * @return Returns True on Success
     */
    public boolean searchShift(String shiftName) {
        return (genHelper.setElementText(shiftSearchFilter, "Shift Search Filter", shiftName));
    }

}