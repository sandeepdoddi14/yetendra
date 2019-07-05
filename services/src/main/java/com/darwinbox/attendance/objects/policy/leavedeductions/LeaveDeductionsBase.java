package com.darwinbox.attendance.objects.policy.leavedeductions;

import java.io.Serializable;
import java.util.Map;

public abstract class LeaveDeductionsBase implements Serializable {

    private boolean isApprovalRequired;
    private boolean isWeekoff;
    private boolean isHoliday;
    private boolean inDay;
    private String leaveId;

    public void setApprovalRequired(boolean approvalRequired) {
        isApprovalRequired = approvalRequired;
    }

    public static boolean getFilterObject(Map<String, Object> data, String key, String value) {
        if (data.get(key).toString().equals(value))
            return true;
        return false;
    }

    public static boolean getFilter(Map<String, String> data, String key, String value) {
        if (data.get(key).equals(value))
            return true;
        return false;
    }

    public static String parseToPHP(boolean status) {
        return (status ? "1" : "0");
    }

    public static boolean compareToSuper(LeaveDeductionsBase ld, LeaveDeductionsBase ld1) {

        return ld.isApprovalRequired() == ld1.isApprovalRequired() &&
                ld.isHoliday() == ld1.isHoliday() &&
                ld.isWeekoff() == ld1.isWeekoff() &&
                ld.isInDay() == ld1.isInDay() &&
                ld.getLeaveId().equals(ld1.getLeaveId());
    }

    public boolean isApprovalRequired() {
        return isApprovalRequired;
    }

    public void isApprovalRequired(boolean isApprovalRequired) {
        this.isApprovalRequired = isApprovalRequired;
    }

    public boolean isWeekoff() {
        return isWeekoff;
    }

    public void setWeekoff(boolean weekoff) {
        isWeekoff = weekoff;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public boolean isInDay() {
        return false;
    }

    public void setInDay(boolean inDay) {
        this.inDay = inDay;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public boolean getProceed(LeaveDeductionsBase ldbase, DAYSTATUS day){
        return ldbase.isInDay() && ( getProceedOnNoLeave(ldbase, day) );
    }

    public boolean getProceedOnNoLeave(LeaveDeductionsBase ldbase, DAYSTATUS day){

        boolean isboth = day.equals(DAYSTATUS.WH);
        boolean isholiday = day.equals(DAYSTATUS.HOLIDAY) || isboth;
        boolean isWeekoff = day.equals(DAYSTATUS.WEEKOFF) || isboth;
        boolean empty = day.equals(DAYSTATUS.EMPTY);

        boolean proceed = ( empty ||  ( isholiday && ldbase.isHoliday() )  ||
                          ( isWeekoff && ldbase.isWeekoff() && (!isholiday)) );

        return proceed;
    }

    public enum DAYSTATUS {WEEKOFF, HOLIDAY, WH, EMPTY}
}
