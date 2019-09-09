package com.darwinbox.attendance.objects.overTime;

import com.darwinbox.framework.uiautomation.base.TestBase;

import java.util.HashMap;
import java.util.Map;

public class OverTimePolicyOptions extends TestBase {

    private String minDurationForOT;
    private boolean requiresApproval;
    private boolean requiresL1manager;
    private boolean requiresL2manager;
    private boolean requiresAdmin;

    private String maxOTBeforeShift;
    private String maxOTAfterShift;
    private String maxOTPerDay;
    private String maxOTPerWeek;
    private String maxOTPerMonth;
    private String maxOTPerQuater;

    private boolean OTcompensatedVia;
    private boolean restrictionOnAccrual;

    private String minDurationForAccrue;
    private String minDurationToCreditForDay;
    private boolean creditForHalfDay;
    private String durationToCreditForHalfDay;
    private boolean allowCreateMoreThan1CompOff;

    private boolean enableMultiplierIfWeekOff;
    private String multipliedByIfWeekOff;

    public String getMinDurationForAccrue() {
        return minDurationForAccrue;
    }

    public void setMinDurationForAccrue(String minDurationForAccrue) {
        this.minDurationForAccrue = minDurationForAccrue;
    }

    public boolean isEnableMultiplierIfWeekOff() {
        return enableMultiplierIfWeekOff;
    }

    public void setEnableMultiplierIfWeekOff(boolean enableMultiplierIfWeekOff) {
        this.enableMultiplierIfWeekOff = enableMultiplierIfWeekOff;
    }

    public String getMultipliedByIfWeekOff() {
        return multipliedByIfWeekOff;
    }

    public void setMultipliedByIfWeekOff(String multipliedByIfWeekOff) {
        this.multipliedByIfWeekOff = multipliedByIfWeekOff;
    }

    public String getMinDurationForOT() {
        return minDurationForOT;
    }

    public void setMinDurationForOT(String minDurationForOT) {
        this.minDurationForOT = minDurationForOT;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public boolean isRequiresL1manager() {
        return requiresL1manager;
    }

    public void setRequiresL1manager(boolean requiresL1manager) {
        this.requiresL1manager = requiresL1manager;
    }

    public boolean isRequiresL2manager() {
        return requiresL2manager;
    }

    public void setRequiresL2manager(boolean requiresL2manager) {
        this.requiresL2manager = requiresL2manager;
    }

    public boolean isRequiresAdmin() {
        return requiresAdmin;
    }

    public void setRequiresAdmin(boolean requiresAdmin) {
        this.requiresAdmin = requiresAdmin;
    }

    public String getMaxOTBeforeShift() {
        return maxOTBeforeShift;
    }

    public void setMaxOTBeforeShift(String maxOTBeforeShift) {
        this.maxOTBeforeShift = maxOTBeforeShift;
    }

    public String getMaxOTAfterShift() {
        return maxOTAfterShift;
    }

    public void setMaxOTAfterShift(String maxOTAfterShift) {
        this.maxOTAfterShift = maxOTAfterShift;
    }

    public String getMaxOTPerDay() {
        return maxOTPerDay;
    }

    public void setMaxOTPerDay(String maxOTPerDay) {
        this.maxOTPerDay = maxOTPerDay;
    }

    public String getMaxOTPerWeek() {
        return maxOTPerWeek;
    }

    public void setMaxOTPerWeek(String maxOTPerWeek) {
        this.maxOTPerWeek = maxOTPerWeek;
    }

    public String getMaxOTPerMonth() {
        return maxOTPerMonth;
    }

    public void setMaxOTPerMonth(String maxOTPerMonth) {
        this.maxOTPerMonth = maxOTPerMonth;
    }

    public String getMaxOTPerQuater() {
        return maxOTPerQuater;
    }

    public void setMaxOTPerQuater(String maxOTPerQuater) {
        this.maxOTPerQuater = maxOTPerQuater;
    }

    public boolean isOTcompensatedVia() {
        return OTcompensatedVia;
    }

    public void setOTcompensatedVia(boolean OTcompensatedVia) {
        this.OTcompensatedVia = OTcompensatedVia;
    }

    public boolean isRestrictionOnAccrual() {
        return restrictionOnAccrual;
    }

    public void setRestrictionOnAccrual(boolean restrictionOnAccrual) {
        this.restrictionOnAccrual = restrictionOnAccrual;
    }

    public String getMinDurationToCreditForDay() {
        return minDurationToCreditForDay;
    }

    public void setMinDurationToCreditForDay(String minDurationToCreditForDay) {
        this.minDurationToCreditForDay = minDurationToCreditForDay;
    }

    public boolean isCreditForHalfDay() {
        return creditForHalfDay;
    }

    public void setCreditForHalfDay(boolean creditForHalfDay) {
        this.creditForHalfDay = creditForHalfDay;
    }

    public String getDurationToCreditForHalfDay() {
        return durationToCreditForHalfDay;
    }

    public void setDurationToCreditForHalfDay(String durationToCreditForHalfDay) {
        this.durationToCreditForHalfDay = durationToCreditForHalfDay;
    }

    public boolean isAllowCreateMoreThan1CompOff() {
        return allowCreateMoreThan1CompOff;
    }

    public void setAllowCreateMoreThan1CompOff(boolean allowCreateMoreThan1CompOff) {
        this.allowCreateMoreThan1CompOff = allowCreateMoreThan1CompOff;
    }


    public Map<String,String> selectDays(String WeekdaysOrWeekendsOrholidays) {

        Map<String, String> body = new HashMap<>();



        if(WeekdaysOrWeekendsOrholidays.equalsIgnoreCase("Weekdays")){

        }

        if(WeekdaysOrWeekendsOrholidays.equalsIgnoreCase("holidays")){

        }

    return body;
    }

    }
