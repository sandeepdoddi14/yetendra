package com.darwinbox.attendance.objects.policy.others;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DisplayFlags implements Serializable {

    private boolean showTotalWorkDuration;
    private boolean showBreakDuration;
    private boolean showFinalDuration;
    private boolean showLateBy;
    private boolean showEarlyOut;
    private boolean showOverTime;
    private boolean showfirstClockin;
    private boolean showfirstClockOut;
    private boolean showClockin;
    private boolean showClockOut;

    public boolean isShowTotalWorkDuration() {
        return showTotalWorkDuration;
    }

    public void setShowTotalWorkDuration(boolean showTotalWorkDuration) {
        this.showTotalWorkDuration = showTotalWorkDuration;
    }

    public boolean isShowBreakDuration() {
        return showBreakDuration;
    }

    public void setShowBreakDuration(boolean showBreakDuration) {
        this.showBreakDuration = showBreakDuration;
    }

    public boolean isShowFinalDuration() {
        return showFinalDuration;
    }

    public void setShowFinalDuration(boolean showFinalDuration) {
        this.showFinalDuration = showFinalDuration;
    }

    public boolean isShowLateBy() {
        return showLateBy;
    }

    public void setShowLateBy(boolean showLateBy) {
        this.showLateBy = showLateBy;
    }

    public boolean isShowEarlyOut() {
        return showEarlyOut;
    }

    public void setShowEarlyOut(boolean showEarlyOut) {
        this.showEarlyOut = showEarlyOut;
    }

    public boolean isShowOverTime() {
        return showOverTime;
    }

    public void setShowOverTime(boolean showOverTime) {
        this.showOverTime = showOverTime;
    }

    public boolean isShowfirstClockin() {
        return showfirstClockin;
    }

    public void setShowfirstClockin(boolean showfirstClockin) {
        this.showfirstClockin = showfirstClockin;
    }

    public boolean isShowfirstClockOut() {
        return showfirstClockOut;
    }

    public void setShowfirstClockOut(boolean showfirstClockOut) {
        this.showfirstClockOut = showfirstClockOut;
    }

    public boolean isShowClockin() {
        return showClockin;
    }

    public void setShowClockin(boolean showClockin) {
        this.showClockin = showClockin;
    }

    public boolean isShowClockOut() {
        return showClockOut;
    }

    public void setShowClockOut(boolean showClockOut) {
        this.showClockOut = showClockOut;
    }

    public void toObject(Map<String, String> data) {

        setShowTotalWorkDuration(LeaveDeductionsBase.getFilter(data, "TotalWorkDuration", "yes"));
        setShowFinalDuration(LeaveDeductionsBase.getFilter(data, "FinalWorkDuration", "yes"));
        setShowLateBy(LeaveDeductionsBase.getFilter(data, "LateMark", "yes"));
        setShowEarlyOut(LeaveDeductionsBase.getFilter(data, "EarlyMark", "yes"));
        setShowBreakDuration(LeaveDeductionsBase.getFilter(data, "BreakDuration", "yes"));
        setShowOverTime(LeaveDeductionsBase.getFilter(data, "OverTime", "yes"));
        setShowfirstClockin(LeaveDeductionsBase.getFilter(data, "FirstClockIn", "yes"));
        setShowfirstClockOut(LeaveDeductionsBase.getFilter(data, "FirstClockOut", "yes"));
        setShowClockin(LeaveDeductionsBase.getFilter(data, "ClockIn", "yes"));
        setShowClockOut(LeaveDeductionsBase.getFilter(data, "ClockOut", "yes"));

    }

    public static DisplayFlags jsonToObject(Map<String, Object> data) {

        DisplayFlags dispFlags = new DisplayFlags();

        boolean totaldur = LeaveDeductionsBase.getFilterObject(data, "disable_work_duration", "1");
        boolean breakdur = LeaveDeductionsBase.getFilterObject(data, "disable_break_duration", "1");
        boolean finaldur = LeaveDeductionsBase.getFilterObject(data, "disable_final_work_duration", "1");
        boolean latemark = LeaveDeductionsBase.getFilterObject(data, "disable_late_mark", "1");
        boolean earlymark = LeaveDeductionsBase.getFilterObject(data, "disable_early_out", "1");
        boolean overtime = LeaveDeductionsBase.getFilterObject(data, "disable_overtime", "1");

        // # TODO

        dispFlags.setShowTotalWorkDuration(totaldur);
        dispFlags.setShowFinalDuration(finaldur);
        dispFlags.setShowBreakDuration(breakdur);
        dispFlags.setShowLateBy(latemark);
        dispFlags.setShowEarlyOut(earlymark);
        dispFlags.setShowOverTime(overtime);

        return dispFlags;

    }

    public Map<String,String> getMap() {

        Map<String,String> data = new HashMap<>();

        data.put("AttendancePolicyForm[disable_work_duration]", LeaveDeductionsBase.parseToPHP(isShowTotalWorkDuration()));
        data.put("AttendancePolicyForm[disable_break_duration]", LeaveDeductionsBase.parseToPHP(isShowBreakDuration()));
        data.put("AttendancePolicyForm[disable_final_work_duration]", LeaveDeductionsBase.parseToPHP(isShowFinalDuration()));
        data.put("AttendancePolicyForm[disable_late_mark]", LeaveDeductionsBase.parseToPHP(isShowLateBy()));
        data.put("AttendancePolicyForm[disable_early_out]", LeaveDeductionsBase.parseToPHP(isShowEarlyOut()));
        data.put("AttendancePolicyForm[disable_overtime]", LeaveDeductionsBase.parseToPHP(isShowOverTime()));

       return data;
    }

    public boolean compareTo(DisplayFlags displayFlags) {

        return displayFlags.isShowBreakDuration() == isShowBreakDuration() &&
               displayFlags.isShowEarlyOut() == isShowEarlyOut() &&
               displayFlags.isShowLateBy() == isShowLateBy() &&
               displayFlags.isShowFinalDuration() == isShowFinalDuration() &&
               displayFlags.isShowTotalWorkDuration() == isShowTotalWorkDuration() &&
               displayFlags.isShowOverTime() == isShowOverTime();

    }
}
