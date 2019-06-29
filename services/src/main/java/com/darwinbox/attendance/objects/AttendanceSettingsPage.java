package com.darwinbox.attendance.objects;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;
import com.darwinbox.framework.uiautomation.base.TestBase;

import java.util.HashMap;
import java.util.Map;

public class AttendanceSettingsPage extends TestBase {

private boolean fixedDuration;
private String fromFixedDuration;
private String toFixedDuration;
private String numOfDays;

private boolean workDuration;
private boolean weeklyoffDays;
private boolean holidayDays;
private boolean leaveDays;

private boolean usePayrollCycle;
private boolean hideAttendancePolicy;
private boolean hideOTPolicy;
private boolean overTimeDisplay;

private String freezeDate;

    public boolean isFixedDuration() {
        return fixedDuration;
    }

    public void setFixedDuration(boolean fixedDuration) {
        this.fixedDuration = fixedDuration;
    }

    public String getFromFixedDuration() {
        return fromFixedDuration;
    }

    public void setFromFixedDuration(String fromFixedDuration) {
        this.fromFixedDuration = fromFixedDuration;
    }

    public String getToFixedDuration() {
        return toFixedDuration;
    }

    public void setToFixedDuration(String toFixedDuration) {
        this.toFixedDuration = toFixedDuration;
    }

    public String getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(String numOfDays) {
        this.numOfDays = numOfDays;
    }

    public boolean isWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(boolean workDuration) {
        this.workDuration = workDuration;
    }

    public boolean isWeeklyoffDays() {
        return weeklyoffDays;
    }

    public void setWeeklyoffDays(boolean weeklyoffDays) {
        this.weeklyoffDays = weeklyoffDays;
    }

    public boolean isHolidayDays() {
        return holidayDays;
    }

    public void setHolidayDays(boolean holidayDays) {
        this.holidayDays = holidayDays;
    }

    public boolean isLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(boolean leaveDays) {
        this.leaveDays = leaveDays;
    }

    public boolean isUsePayrollCycle() {
        return usePayrollCycle;
    }

    public void setUsePayrollCycle(boolean usePayrollCycle) {
        this.usePayrollCycle = usePayrollCycle;
    }

    public boolean isHideAttendancePolicy() {
        return hideAttendancePolicy;
    }

    public void setHideAttendancePolicy(boolean hideAttendancePolicy) {
        this.hideAttendancePolicy = hideAttendancePolicy;
    }

    public boolean isHideOTPolicy() {
        return hideOTPolicy;
    }

    public void setHideOTPolicy(boolean hideOTPolicy) {
        this.hideOTPolicy = hideOTPolicy;
    }

    public boolean isOverTimeDisplay() {
        return overTimeDisplay;
    }

    public void setOverTimeDisplay(boolean overTimeDisplay) {
        this.overTimeDisplay = overTimeDisplay;
    }

    public String getFreezeDate() {
        return freezeDate;
    }

    public void setFreezeDate(String freezeDate) {
        this.freezeDate = freezeDate;
    }
// 13 fields in total Fixed Duration is 1 if selected, 0 if not. There is no Defined Duration



    public Map<String,String> toMap(){

        Map<String, String> body = new HashMap<>();

        body.put("yt0"," SAVE");
        body.put("TenantAttendanceSettings[type]", LeaveDeductionsBase.parseToPHP(isFixedDuration()));
        body.put("TenantAttendanceSettings[from_date]",getFromFixedDuration());
        body.put("TenantAttendanceSettings[to_date]",getToFixedDuration());
        body.put("TenantAttendanceSettings[no_of_days]",getNumOfDays());
        body.put("TenantAttendanceSettings[based_on_working_days]",LeaveDeductionsBase.parseToPHP(isWorkDuration()));
        body.put("TenantAttendanceSettings[count_weeklyoff]",LeaveDeductionsBase.parseToPHP(isWeeklyoffDays()));
        body.put("TenantAttendanceSettings[count_holiday]",LeaveDeductionsBase.parseToPHP(isHolidayDays()));
        body.put("TenantAttendanceSettings[count_leaves]",LeaveDeductionsBase.parseToPHP(isLeaveDays()));
        body.put("TenantAttendanceSettings[payroll_cycle]",LeaveDeductionsBase.parseToPHP(isUsePayrollCycle()));
        body.put("TenantAttendanceSettings[hide_attendance_policy]",LeaveDeductionsBase.parseToPHP(isHideAttendancePolicy()));
        body.put("TenantAttendanceSettings[hide_ot_policy]",LeaveDeductionsBase.parseToPHP(isHideOTPolicy()));
        body.put("TenantAttendanceSettings[show_old_overtime]",LeaveDeductionsBase.parseToPHP(isOverTimeDisplay()));
        body.put("TenantAttendanceSettings[freeze_date]",getFreezeDate());

        return body;

    }

    public void toAddData(){

        setFixedDuration(false);
        setHideAttendancePolicy(true);
        setHideOTPolicy(true);

}

}
