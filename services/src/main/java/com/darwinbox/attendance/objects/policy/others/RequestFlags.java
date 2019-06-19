package com.darwinbox.attendance.objects.policy.others;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestFlags implements Serializable {

    private boolean autoApproveOptional;

    public enum Location { NONE, OFFICE, FIELDDUTY, HOME }

    private boolean allowWFH;
    private boolean allowOutDuty;

    private boolean req_weekoff;
    private boolean req_holiday;
    private boolean req_clockIn;
    private boolean req_outDuty;
    private boolean req_futureDates;
    private boolean req_shiftChange;

    public boolean isAutoApproveOptional() {
        return autoApproveOptional;
    }

    public void setAutoApproveOptional(boolean autoApproveOptional) {
        this.autoApproveOptional = autoApproveOptional;
    }

    public boolean isAllowWFH() {
        return allowWFH;
    }

    public void setAllowWFH(boolean allowWFH) {
        this.allowWFH = allowWFH;
    }

    public boolean isAllowOutDuty() {
        return allowOutDuty;
    }

    public void setAllowOutDuty(boolean allowOutDuty) {
        this.allowOutDuty = allowOutDuty;
    }

    public boolean isReq_weekoff() {
        return req_weekoff;
    }

    public void setReq_weekoff(boolean req_weekoff) {
        this.req_weekoff = req_weekoff;
    }

    public boolean isReq_holiday() {
        return req_holiday;
    }

    public void setReq_holiday(boolean req_holiday) {
        this.req_holiday = req_holiday;
    }

    public boolean isReq_clockIn() {
        return req_clockIn;
    }

    public void setReq_clockIn(boolean req_clockIn) {
        this.req_clockIn = req_clockIn;
    }

    public boolean isReq_outDuty() {
        return req_outDuty;
    }

    public void setReq_outDuty(boolean req_outDuty) {
        this.req_outDuty = req_outDuty;
    }

    public boolean isReq_futureDates() {
        return req_futureDates;
    }

    public void setReq_futureDates(boolean req_futureDates) {
        this.req_futureDates = req_futureDates;
    }

    public boolean isReq_shiftChange() {
        return req_shiftChange;
    }

    public void setReq_shiftChange(boolean req_shiftChange) {
        this.req_shiftChange = req_shiftChange;
    }

    public void toObject(Map<String, String> data) {

        setReq_futureDates(LeaveDeductionsBase.getFilter(data, "FutureDates", "yes"));
        setReq_shiftChange(LeaveDeductionsBase.getFilter(data, "ShiftChange", "yes"));
        setReq_outDuty(LeaveDeductionsBase.getFilter(data, "OutDuty", "yes"));
        setReq_weekoff(LeaveDeductionsBase.getFilter(data, "WeeklyOff", "yes"));
        setReq_holiday(LeaveDeductionsBase.getFilter(data, "Holiday", "yes"));
        setReq_clockIn(LeaveDeductionsBase.getFilter(data, "Clockin", "yes"));

        setAllowWFH(LeaveDeductionsBase.getFilter(data, "Home", "yes"));
        setAllowOutDuty(LeaveDeductionsBase.getFilter(data, "FieldOffice", "yes"));
        setAutoApproveOptional(LeaveDeductionsBase.getFilter(data, "AutoApproveOptional", "yes"));

    }

    public static RequestFlags jsonToObject(Map<String, Object> data) {
        RequestFlags reqFlags = new RequestFlags();

        boolean req_holiday = LeaveDeductionsBase.getFilterObject(data, "allow_attrequest_holiday", "1");
        boolean req_weekoff = LeaveDeductionsBase.getFilterObject(data, "allow_attrequest_weekoff", "1");
        boolean req_Future = LeaveDeductionsBase.getFilterObject(data, "allow_attendance_request_future", "1");
        boolean req_Shift = LeaveDeductionsBase.getFilterObject(data, "allow_shift_change_request", "1");
        boolean req_ClockIn =  LeaveDeductionsBase.getFilterObject(data, "clockin_allowed_attendance_regularise", "1");
        boolean req_OD =  LeaveDeductionsBase.getFilterObject(data, "od_allowed_attendance_regularise", "1");

        boolean autoApprove =  LeaveDeductionsBase.getFilterObject(data, "auto_approve_optional_holidays", "1");

        boolean allowWFH =  LeaveDeductionsBase.getFilterObject(data, "work_from_home", "1");
        boolean allowField =  LeaveDeductionsBase.getFilterObject(data, "out_duty", "1");

        reqFlags.setReq_clockIn(req_ClockIn);
        reqFlags.setReq_outDuty(req_OD);
        reqFlags.setReq_weekoff(req_weekoff);
        reqFlags.setReq_holiday(req_holiday);
        reqFlags.setReq_shiftChange(req_Shift);
        reqFlags.setReq_futureDates(req_Future);

        reqFlags.setAllowWFH(allowWFH);
        reqFlags.setAllowOutDuty(allowField);
        reqFlags.setAutoApproveOptional(autoApprove);

        return reqFlags;
    }

    public Map<String,String> getMap() {

        Map<String, String> data = new HashMap<>();

        data.put("AttendancePolicyForm[allow_attrequest_holiday]", LeaveDeductionsBase.parseToPHP(isReq_holiday()));
        data.put("AttendancePolicyForm[allow_attrequest_weekoff]", LeaveDeductionsBase.parseToPHP(isReq_weekoff()));
        data.put("AttendancePolicyForm[allow_attendance_request_future]", LeaveDeductionsBase.parseToPHP(isReq_futureDates()));
        data.put("AttendancePolicyForm[allow_shift_change_request]", LeaveDeductionsBase.parseToPHP(isReq_shiftChange()));
        data.put("AttendancePolicyForm[clockin_allowed_attendance_regularise]", LeaveDeductionsBase.parseToPHP(isReq_clockIn()));
        data.put("AttendancePolicyForm[od_allowed_attendance_regularise]", LeaveDeductionsBase.parseToPHP(isReq_outDuty()));

        data.put("AttendancePolicyForm[auto_approve_optional_holidays]", LeaveDeductionsBase.parseToPHP(isAutoApproveOptional()));
        data.put("AttendancePolicyForm[work_from_home]", LeaveDeductionsBase.parseToPHP(isAllowWFH()));
        data.put("AttendancePolicyForm[out_duty]", LeaveDeductionsBase.parseToPHP(isAllowOutDuty()));

        return data;
    }

    public boolean compareTo(RequestFlags reqFlags) {
        
        return reqFlags.isReq_clockIn() == isReq_clockIn() && 
               reqFlags.isReq_outDuty() == isReq_outDuty() &&
               reqFlags.isReq_weekoff() == isReq_weekoff() &&
               reqFlags.isReq_holiday() == isReq_holiday() &&
               reqFlags.isReq_shiftChange() == isReq_shiftChange() &&
               reqFlags.isReq_futureDates() == isReq_futureDates() &&
               reqFlags.isAllowWFH() == isAllowWFH() &&
               reqFlags.isAllowOutDuty() == isAllowOutDuty() &&
               reqFlags.isAutoApproveOptional() == isAutoApproveOptional();

    }
}
