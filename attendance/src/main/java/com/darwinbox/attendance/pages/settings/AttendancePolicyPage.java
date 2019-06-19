package com.darwinbox.attendance.pages.settings;

import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.framework.uiautomation.helper.Wait.WaitHelper;
import com.darwinbox.framework.uiautomation.helper.genericHelper.GenericHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * Page for Attendance Policy to handle all Policy Actions
 * Should be on Attendance Policy Page for the same
 *
 * @author hkonakanchi ( Harsha Konakanchi - harsha.konakanchi@darwinbox.io )
 * @Date 28 Dec 2018
 */

public class AttendancePolicyPage extends TestBase {

    /**
     * Create Policy Btn
     */
    @FindBy(id = "create_policy_btn")
    public WebElement createPolicyBtn;
    /**
     * Search filter for Policy
     */
    @FindBy(css = "#policy_table_filter input")
    public WebElement policySearchFilter;
    /**
     * Attendance Policy name
     */
    @FindBy(id = "AttendancePolicyForm_policy_name")
    public WebElement policyName;
    /**
     * Attendance Policy Description
     */
    @FindBy(id = "AttendancePolicyForm_policy_description")
    public WebElement description;
    /**
     * Attendance Policy Applicable Group Company
     */
    @FindBy(id = "dept_grp_company")
    public WebElement grpCompany;
    /**
     * Attendance Policy Grace Time for Clock In
     */
    @FindBy(id = "AttendancePolicyForm_grace_time")
    public WebElement clockInGraceTime;
    /**
     * Attendance Policy Grace Time for Clock Out
     */
    @FindBy(id = "AttendancePolicyForm_grace_time_early")
    public WebElement clockOutGraceTime;
    /**
     * Attendance Policy Maximum Optional Holiday Count
     */
    @FindBy(id = "AttendancePolicyForm_max_optional_holiday_policy")
    public WebElement maxHolidayCount;
    /**
     * Attendance Policy - Auto Approve Optional Holidays
     */
    @FindBy(id = "AttendancePolicyForm_auto_approve_optional_holidays")
    public WebElement autoApproveOptionalHolidays;
    /**
     * Attendance Policy - MarkIn Policy
     */
    @FindBy(id = "AttendancePolicyForm_markin_policy")
    public WebElement markInPolicy;
    /**
     * Attendance Policy - Request from home
     */
    @FindBy(id = "AttendancePolicyForm_work_from_home")
    public WebElement allowRequestWorkFromHome;
    /**
     * Attendance Policy - Out Duty
     */
    @FindBy(id = "AttendancePolicyForm_out_duty")
    public WebElement allowRequestOutDuty;
    /**
     * Attendance Policy - Edit Back Days Attendance
     */
    @FindBy(id = "AttendancePolicyForm_edit_back_days")
    public WebElement editBackDays;
    /**
     * Attendance Policy - Apply Back Days Attendance
     */
    @FindBy(id = "AttendancePolicyForm_apply_back_days_employee")
    public WebElement applyBackDays;
    /**
     * Attendance Policy - Roster Back Days Attendance
     */
    @FindBy(id = "AttendancePolicyForm_roster_back_days")
    public WebElement editRosterBackDays;
    /**
     * Attendance Policy - Auto Approve Attendance
     */
    @FindBy(id = "AttendancePolicyForm_auto_approve_days")
    public WebElement autoApproveAttendance;
    /**
     * Attendance Policy - Trigger Absconding Flow
     */
    @FindBy(id = "AttendancePolicyForm_absconding_days")
    public WebElement abscondingTriggerDays;
    /**
     * Attendance Policy - Don't Show Work Duration
     */
    @FindBy(id = "AttendancePolicyForm_disable_work_duration")
    public WebElement showWorkDuration;
    /**
     * Attendance Policy - Don't Show Break Duration
     */
    @FindBy(id = "AttendancePolicyForm_disable_break_duration")
    public WebElement showBreakDuration;
    /**
     * Attendance Policy - Don't Show Final Work  Duration
     */
    @FindBy(id = "AttendancePolicyForm_disable_final_work_duration")
    public WebElement showFinalWorkDuration;
    /**
     * Attendance Policy - Don't Show Late Marks
     */
    @FindBy(id = "AttendancePolicyForm_disable_late_mark")
    public WebElement showLateMark;
    /**
     * Attendance Policy - Don't Show Early Marks
     */
    @FindBy(id = "AttendancePolicyForm_disable_early_out")
    public WebElement showEarlyMark;
    /**
     * Attendance Policy - Don't Show Over Time
     */
    @FindBy(id = "AttendancePolicyForm_disable_overtime")
    public WebElement showOverTime;
    /**
     * Attendance Policy - Allow Attendance Request on WeekOff
     */
    @FindBy(id = "AttendancePolicyForm_allow_attrequest_weekoff")
    public WebElement allowRequestOnWeekOff;
    /**
     * Attendance Policy - Allow Attendance Request on Holiday
     */
    @FindBy(id = "AttendancePolicyForm_allow_attrequest_holiday")
    public WebElement allowRequestOnHoliday;
    /**
     * Attendance Policy - Allow Clockin Request in attendance
     */
    @FindBy(id = "AttendancePolicyForm_clockin_allowed_attendance_regularise")
    public WebElement allowClockInRequest;
    /**
     * Attendance Policy - Allow Out Duty Requests
     */
    @FindBy(id = "AttendancePolicyForm_od_allowed_attendance_regularise")
    public WebElement allowOutDutyRequests;
    /**
     * Attendance Policy - Allow SHort Leaves
     */
    @FindBy(id = "AttendancePolicyForm_short_leave_allowed_attendance_regularise")
    public WebElement allowShortLeaves;
    /**
     * Attendance Policy - Maximum Number of Short Leaves Allowed
     */
    @FindBy(id = "AttendancePolicyForm_short_leave_allowed_days")
    public WebElement maxAllowedShortLeaves;
    /**
     * Attendance Policy - Minimum Duration for Short Leave in Minutes
     */
    @FindBy(id = "AttendancePolicyForm_short_leave_min_mins")
    public WebElement minDurationShortLeave;
    /**
     * Attendance Policy - Maximum Duration for Short Leave in Minutes
     */
    @FindBy(id = "AttendancePolicyForm_short_leave_max_mins")
    public WebElement maxDurationShortLeave;
    /**
     * Attendance Policy - Allow Attendance Request for future Days
     */
    @FindBy(id = "AttendancePolicyForm_allow_attendance_request_future")
    public WebElement allowRequestForFuture;
    /**
     * Attendance Policy - Allow Shift Change requests
     */
    @FindBy(id = "AttendancePolicyForm_allow_shift_change_request")
    public WebElement allowShiftChangeRequest;
    /**
     * Attendance Policy - Allow AutoShift Assignment
     */
    @FindBy(id = "AttendancePolicyForm_allow_auto_shift_assignment")
    public WebElement allowAutoShift;
    /**
     * Attendance Policy - Allowed Shifts for Auto Shift Assignment
     */
    @FindBy(id = "AttendancePolicyForm_allowed_shift_auto_assignment")
    public WebElement allowedAutoShifts;
    /**
     * Attendance Policy - Auto Shift to be ( nearest ) or ( nearest after )
     */
    @FindBy(id = "AttendancePolicyForm_nearest")
    public WebElement radioNearestAfter;
    /**
     * Attendance Policy - Allow Buffer Time for Shifts
     */
    @FindBy(id = "AttendancePolicyForm_pre_post")
    public WebElement allowBufferTime;
    /**
     * Attendance Policy - Buffer Time - Pre Hours
     */
    @FindBy(id = "AttendancePolicyForm_pre_time_hour")
    public WebElement preTimeHrs;
    /**
     * Attendance Policy - Buffer Time - Pre Minutes
     */
    @FindBy(id = "AttendancePolicyForm_pre_time_min")
    public WebElement preTimeMins;
    /**
     * Attendance Policy - Post Time Hrs
     */
    @FindBy(id = "AttendancePolicyForm_post_time_hour")
    public WebElement postTimehrs;
    /**
     * Attendance Policy - Post Time mins
     */
    @FindBy(id = "AttendancePolicyForm_post_time_min")
    public WebElement postTimeMins;

    /**
     * Attendance Policy Leave Deduction Absent - Dropdown Option for leave deduction
     */
    @FindBy(id = "AttendancePolicyForm_leave_deduction_policy")
    public WebElement absentDeduction;
    /**
     * Attendance Policy Leave Deduction Absent - Leave Type
     */
    @FindBy(id = "AttendancePolicyForm_leave_deduction")
    public WebElement absent_leaveType;
    /**
     * Attendance Policy Leave Deduction Absent - Deduct After Approval
     */
    @FindBy(id = "AttendancePolicyForm_leave_deduction_deduct_after_approval")
    public WebElement absent_afterApproval;
    /**
     * Attendance Policy Leave Deduction Absent - Applicable for Holiday
     */
    @FindBy(id = "AttendancePolicyForm_leave_deduction_on_holiday")
    public WebElement absent_Holiday;
    /**
     * Attendance Policy Leave Deduction Absent - Applicable for Weekly Off
     */
    @FindBy(id = "AttendancePolicyForm_leave_deduction_on_weeklyoff")
    public WebElement absent_Weekoff;
    WaitHelper waitHelper;
    GenericHelper genHelper;

    /**
     * Constructor to make use of Attendance Policy Page Object
     *
     * @param driver Webdriver object as param
     */

    public AttendancePolicyPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        waitHelper = new WaitHelper(driver);
        genHelper = new GenericHelper(driver);
    }

    /**
     * Attendance Policy Leave Deduction Work Duration - Dropdown Option for leave deduction
     */
    @FindBy(id = "AttendancePolicyForm_work_duration_policy")
    public WebElement workDuration_Deduction;
    /**
     * Attendance Policy Leave Deduction Work Duration - Leave Type
     */
    @FindBy(id = "AttendancePolicyForm_work_duration_leave_type")
    public WebElement workDuration_leaveType;
    /**
     * Attendance Policy Leave Deduction Work Duration - Deduct After Approval
     */
    @FindBy(id = "AttendancePolicyForm_work_duration_deduct_after_approval")
    public WebElement workDuration_afterApproval;
    /**
     * Attendance Policy Leave Deduction Work Duration - Applicable for Holiday
     */
    @FindBy(id = "AttendancePolicyForm_work_duration_deduction_on_holiday")
    public WebElement workDuration_Holiday;
    /**
     * Attendance Policy Leave Deduction Work Duration - Applicable for Weekly Off
     */
    @FindBy(id = "AttendancePolicyForm_work_duration_deduction_on_weeklyoff")
    public WebElement workDuration_Weekoff;

}