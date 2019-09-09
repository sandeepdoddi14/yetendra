package com.darwinbox.attendance.objects.overTime;

import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.framework.uiautomation.base.TestBase;

import java.util.HashMap;
import java.util.Map;

public class OverTimePolicy extends TestBase {

  private String companyName;
  private String policyName;
  private String description;
  private boolean restriction;
  private String restrictionTo;

  private String maxAllowed;
  private String maxConsecutiveAllowed;
  private boolean allowHalfDay;
  private boolean allowPastDated;
  private String applyNumberOfBackDatedLeaves;

  private boolean pushRequestsToAdmin;
  private String autoApproveLeaveRequest;
  private String compOffLapse;
  private boolean interveningHolidaysOrWeekoffs;
  private boolean interveningHoliday;
  private boolean interveningWeekoff;

    private boolean suffixedToHolidayAndWeekoff;
    private String weekoffPrefix0_1_2;
    private String weekoffSuffix0_1_2;
    private String holidayPrefix0_1_2;
    private String holidaySuffix0_1_2;
    private String selectFormID;
    private String approvalFlowID;
    private boolean resetCompOff;
    private String resetCompOffBalanceAtEndOf;

    private  WeekDayOption weekDay;
    private  WeekendOption weekoff;
    private  HolidayOption holiday;

    public WeekDayOption getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDayOption weekDay) {
        this.weekDay = weekDay;
    }

    public WeekendOption getWeekoff() {
        return weekoff;
    }

    public void setWeekoff(WeekendOption weekoff) {
        this.weekoff = weekoff;
    }

    public HolidayOption getHoliday() {
        return holiday;
    }

    public void setHoliday(HolidayOption holiday) {
        this.holiday = holiday;
    }

    public boolean isResetCompOff() {
        return resetCompOff;
    }

    public void setResetCompOff(boolean resetCompOff) {
        this.resetCompOff = resetCompOff;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRestriction() {
        return restriction;
    }

    public void setRestriction(boolean restriction) {
        this.restriction = restriction;
    }

    public String getRestrictionTo() {
        return restrictionTo;
    }

    public void setRestrictionTo(String restrictionTo) {
        this.restrictionTo = restrictionTo;
    }

    public String getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(String maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public String getMaxConsecutiveAllowed() {
        return maxConsecutiveAllowed;
    }

    public void setMaxConsecutiveAllowed(String maxConsecutiveAllowed) {
        this.maxConsecutiveAllowed = maxConsecutiveAllowed;
    }

    public boolean isAllowHalfDay() {
        return allowHalfDay;
    }

    public void setAllowHalfDay(boolean allowHalfDay) {
        this.allowHalfDay = allowHalfDay;
    }

    public boolean isAllowPastDated() {
        return allowPastDated;
    }

    public void setAllowPastDated(boolean allowPastDated) {
        this.allowPastDated = allowPastDated;
    }

    public String getApplyNumberOfBackDatedLeaves() {
        return applyNumberOfBackDatedLeaves;
    }

    public void setApplyNumberOfBackDatedLeaves(String applyNumberOfBackDatedLeaves) {
        this.applyNumberOfBackDatedLeaves = applyNumberOfBackDatedLeaves;
    }

    public boolean isPushRequestsToAdmin() {
        return pushRequestsToAdmin;
    }

    public void setPushRequestsToAdmin(boolean pushRequestsToAdmin) {
        this.pushRequestsToAdmin = pushRequestsToAdmin;
    }

    public String getAutoApproveLeaveRequest() {
        return autoApproveLeaveRequest;
    }

    public void setAutoApproveLeaveRequest(String autoApproveLeaveRequest) {
        this.autoApproveLeaveRequest = autoApproveLeaveRequest;
    }

    public String getCompOffLapse() {
        return compOffLapse;
    }

    public void setCompOffLapse(String compOffLapse) {
        this.compOffLapse = compOffLapse;
    }

    public boolean isInterveningHolidaysOrWeekoffs() {
        return interveningHolidaysOrWeekoffs;
    }

    public void setInterveningHolidaysOrWeekoffs(boolean interveningHolidaysOrWeekoffs) {
        this.interveningHolidaysOrWeekoffs = interveningHolidaysOrWeekoffs;
    }

    public boolean isInterveningHoliday() {
        return interveningHoliday;
    }

    public void setInterveningHoliday(boolean interveningHoliday) {
        this.interveningHoliday = interveningHoliday;
    }

    public boolean isInterveningWeekoff() {
        return interveningWeekoff;
    }

    public void setInterveningWeekoff(boolean interveningWeekoff) {
        this.interveningWeekoff = interveningWeekoff;
    }

    public boolean isSuffixedToHolidayAndWeekoff() {
        return suffixedToHolidayAndWeekoff;
    }

    public void setSuffixedToHolidayAndWeekoff(boolean suffixedToHolidayAndWeekoff) {
        this.suffixedToHolidayAndWeekoff = suffixedToHolidayAndWeekoff;
    }

    public String getWeekoffPrefix0_1_2() {
        return weekoffPrefix0_1_2;
    }

    public void setWeekoffPrefix0_1_2(String weekoffPrefix0_1_2) {
        this.weekoffPrefix0_1_2 = weekoffPrefix0_1_2;
    }

    public String getWeekoffSuffix0_1_2() {
        return weekoffSuffix0_1_2;
    }

    public void setWeekoffSuffix0_1_2(String weekoffSuffix0_1_2) {
        this.weekoffSuffix0_1_2 = weekoffSuffix0_1_2;
    }

    public String getHolidayPrefix0_1_2() {
        return holidayPrefix0_1_2;
    }

    public void setHolidayPrefix0_1_2(String holidayPrefix0_1_2) {
        this.holidayPrefix0_1_2 = holidayPrefix0_1_2;
    }

    public String getHolidaySuffix0_1_2() {
        return holidaySuffix0_1_2;
    }

    public void setHolidaySuffix0_1_2(String holidaySuffix0_1_2) {
        this.holidaySuffix0_1_2 = holidaySuffix0_1_2;
    }

    public String getSelectFormID() {
        return selectFormID;
    }

    public void setSelectFormID(String selectFormID) {
        this.selectFormID = selectFormID;
    }

    public String getApprovalFlowID() {
        return approvalFlowID;
    }

    public void setApprovalFlowID(String approvalFlowID) {
        this.approvalFlowID = approvalFlowID;
    }

    public String getResetCompOffBalanceAtEndOf() {
        return resetCompOffBalanceAtEndOf;
    }

    public void setResetCompOffBalanceAtEndOf(String resetCompOffBalanceAtEndOf) {
        this.resetCompOffBalanceAtEndOf = resetCompOffBalanceAtEndOf;
    }

    public Map<String,String> toMap() {

        Map<String, String> body = new HashMap<>();
        body.put("grp_compoff_id","");
        body.put("leave_id","5d5d386728646");  //by HTML parser // get by ID or get by name, return ID or create randomn name OT everytime on run
        body.put("grp_compoff_cmp","");

       //  body.put("TenantLeavesCompoff[compoff]",getCompanyName());
        body.put("TenantLeavesCompoff[compoff]","1");
        body.put("TenantLeavesCompoff[name]",getPolicyName());
        body.put("TenantLeavesCompoff[description]",getDescription());
        body.put("TenantLeavesCompoff[or_and]", LeaveDeductionsBase.parseToPHP(isRestriction()));
        body.put("TenantLeavesCompoff[valid_for][]","0");


        //or instead below get, use getweekday() form below toObject() or make only one getter for weekday in toObject() and in below putAll
       // body.putAll(weekDayOption.selectWeekDays());
        body.putAll(weekDay.selectWeekDays());
        body.putAll(weekoff.selectWeekend());
        body.putAll(holiday.selectHolidays());

        body.put("TenantLeavesCompoff[maximum_leaves_allowed_in_a_month]","0");
        body.put("TenantLeavesCompoff[maximum_consecutive_leaves_allowed_in_a_month]","0");
        body.put("TenantLeavesCompoff[allow_half_day_leaves]","0");
        body.put("TenantLeavesCompoff[allow_previous_no_of_days]","0");
        body.put("TenantLeavesCompoff[previous_no_of_days]","0");
        body.put("TenantLeavesCompoff[Leaves_push_leaves_to_admin]","0");
        body.put("TenantLeavesCompoff[auto_approve_days]","0");
        body.put("TenantLeavesCompoff[Compoff_earned_will_lapse_after_how_many_days]","0");
        body.put("LeavePolicy_InterveningHolidays[status]","0");
        body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_public_holiday]","0");
        body.put("LeavePolicy_InterveningHolidays[count_intervening_holidays][count_weekly_off]","0");
        body.put("LeavePolicyPrefixSuffix[status]","0");
        body.put("LeavePolicyPrefixSuffix[prefix_weekly_off]","0");
        body.put("LeavePolicyPrefixSuffix[suffix_weekly_off]","0");
        body.put("LeavePolicyPrefixSuffix[prefix_holiday]","0");
        body.put("LeavePolicyPrefixSuffix[suffix_holiday]","0");
        //body.put("TenantLeavesCompoff[form]",getSelectFormID());
       // body.put("TenantLeavesCompoff[approval_flow]",getApprovalFlowID());
        body.put("TenantLeavesCompoff[form]","");
        body.put("TenantLeavesCompoff[approval_flow]","");

        body.put("TenantLeavesCompoff[lapse_policy]","0");
        body.put("TenantLeavesCompoff[lapse_policy_financial_cycle]","1");

        return body;
    }

    public void toObject(Map<String, String> body) {

          // setCompanyName(body.get("CompanyName"));
          //  setCompanyName("5d5250b3da889");
           setPolicyName(body.get("OTPolicyName"));
           setDescription("Done->");
           setRestriction(false);
           /*weekDayOption.toObject(body);
           weekendOption.toObject(body);
           holidayOption.toObject(body);
*/
    }


    }
