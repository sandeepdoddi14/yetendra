package com.darwinbox.attendance.objects.policy.leavedeductions;

import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.others.PolicyInfo;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LateEarly extends LateOrEarlyBase implements Serializable {

    public boolean isCount_as_2() {
        return count_as_2;
    }

    public void setCount_as_2(boolean count_as_2) {
        this.count_as_2 = count_as_2;
    }

    private boolean count_as_2;

    public static LateEarly jsonToObject(Map<String, Object> data) {

        LateEarly lateEarly = null;

        if (getFilterObject(data, "lateplusearlymark_policy", "1")) {

            lateEarly = new LateEarly();

            boolean isApprovalRequired = getFilterObject(data, "lateplusearlymark_deduct_after_approval", "1");
            boolean isHoliday = getFilterObject(data, "lateplusearlymark_deduction_on_holiday", "1");
            boolean isWeeklyOff = getFilterObject(data, "lateplusearlymark_deduction_on_weeklyoff", "1");
            boolean isInDay = getFilterObject(data, "lateplusearlymark_deduction_on_leave", "1");
            boolean isForAll = getFilterObject(data, "every_next_lateplusearlymark", "1");
            boolean isHalfDay = getFilterObject(data, "lateplusearlymark_leave_deduction", "2");
            boolean count_2 = getFilterObject(data, "lateplusearlymark_both_on_sameday_count", "2");
            int count = Integer.parseInt(data.get("number_of_lateplusearlymarks").toString());

            lateEarly.isApprovalRequired(isApprovalRequired);
            lateEarly.setHoliday(isHoliday);
            lateEarly.setWeekoff(isWeeklyOff);
            lateEarly.setInDay(isInDay);
            lateEarly.setForEvery(isForAll);
            lateEarly.setCount(count);
            lateEarly.setCount_as_2(count_2);
            lateEarly.setLeaveId(data.get("lateplusearlymark_leave_type").toString());
            lateEarly.setHalfDay(isHalfDay);

        }

        return lateEarly;

    }

    public void toObject(Map<String, String> data) {

        boolean isApprovalRequired = getFilter(data, "RequiresApproval", "yes");
        boolean isHoliday = getFilter(data, "IsOnHoliday", "yes");
        boolean isWeeklyOff = getFilter(data, "IsOnWeeklyOff", "yes");
        boolean isInDay = getFilter(data, "IsInDay", "yes");
        boolean isHalfDay = getFilter(data, "LeaveType", "HalfDay");
        boolean isEvery = getFilter(data, "ForEveryInstance", "yes");
        boolean count_as_2 = getFilter(data, "Count as 1 or 2", "2");

        int n = Integer.parseInt(data.get("No.of Instances"));

        isApprovalRequired(isApprovalRequired);
        setHoliday(isHoliday);
        setWeekoff(isWeeklyOff);
        setInDay(isInDay);
        setHalfDay(isHalfDay);
        setLeaveId(data.get("LeaveToDeductId"));
        setForEvery(isEvery);
        setCount(n);
        setCount_as_2(count_as_2);
    }

    public static Map<String,String> getMap(LateEarly latePlusEarly) {

        Map<String,String> body = new HashMap<>();

        if (latePlusEarly != null ) {
            body.put("AttendancePolicyForm[lateplusearlymark_policy]", "1");
            body.put("AttendancePolicyForm[lateplusearlymark_leave_type]", latePlusEarly.getLeaveId());
            body.put("AttendancePolicyForm[lateplusearlymark_deduct_after_approval]", parseToPHP(latePlusEarly.isApprovalRequired()));
            body.put("AttendancePolicyForm[lateplusearlymark_deduction_on_holiday]", parseToPHP(latePlusEarly.isHoliday()));
            body.put("AttendancePolicyForm[lateplusearlymark_deduction_on_weeklyoff]", parseToPHP(latePlusEarly.isWeekoff()));
            body.put("AttendancePolicyForm[lateplusearlymark_deduction_on_leave]", parseToPHP(latePlusEarly.isInDay()));
            body.put("AttendancePolicyForm[every_next_lateplusearlymark]", parseToPHP(latePlusEarly.isForEvery()));
            body.put("AttendancePolicyForm[lateplusearlymark_leave_deduction]", (latePlusEarly.isHalfDay() ? "2" : "1"));
            body.put("AttendancePolicyForm[number_of_lateplusearlymarks]", (latePlusEarly.getCount()+""));
            body.put("AttendancePolicyForm[lateplusearlymark_both_on_sameday_count]", (latePlusEarly.isCount_as_2() ? "2" : "1"));
        }
        return body;
    }

    public static boolean compareTo(LateEarly lateEarly, LateEarly lateEarly1) {

        if ( lateEarly != null ) {

            if ( lateEarly1 != null) {

                boolean status = LateOrEarlyBase.compareToSuper(lateEarly,lateEarly1);
                return status && lateEarly.isCount_as_2() == lateEarly1.isCount_as_2();
            }

            return false;

        } else if ( lateEarly1 != null) {

            return false;

        } else {

            return true;

        }

    }
    
    public Map<String, String> getLateEarlys(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff) {

        DateTimeHelper helper = new DateTimeHelper();
        Map<String, String> body = new HashMap<>();

        for (int i = 0; i < 2 * getCount(); i++) {
            body.putAll(getLateEarly(empId, policyInfo, shift, date, isWeekOff, i + 2));
            date = helper.getNextDate(date);
        }

        return body;

    }

    public Map<String, String> getLateEarly(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff) {
        return (getLateEarly(empId, policyInfo, shift, date, isWeekOff, 2));
    }

    private Map<String, String> getLateEarly(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff, int import_line) {

        DateTimeHelper helper = new DateTimeHelper();

        String shiftDate = helper.formatDateTo(date, "dd-MM-yyyy");
        String inDate = shiftDate;
        String outDate = shift.isOverNightShift() ? helper.getNextDate(shiftDate) : shiftDate;

        int shiftStart = shift.getStartTime() + policyInfo.getGraceTimeIn();
        int shiftEnd = shift.getEndTime() - policyInfo.getGraceTimeOut() -1 ; 

        String inTime = helper.parseTime(shiftStart % 1440);
        String outTime = helper.parseTime(shiftEnd % 1440);

        Map<String, String> body = new HashMap<>();

        body.put("UserAttendanceImportBack[" + import_line + "][0]", empId);
        body.put("UserAttendanceImportBack[" + import_line + "][1]", shiftDate);
        body.put("UserAttendanceImportBack[" + import_line + "][2]", inDate);
        body.put("UserAttendanceImportBack[" + import_line + "][3]", inTime);
        body.put("UserAttendanceImportBack[" + import_line + "][4]", outDate);
        body.put("UserAttendanceImportBack[" + import_line + "][5]", outTime);
        body.put("UserAttendanceImportBack[" + import_line + "][6]", shift.getShiftName());
        body.put("UserAttendanceImportBack[" + import_line + "][7]", policyInfo.getPolicyName());
        body.put("UserAttendanceImportBack[" + import_line + "][8]", isWeekOff ? "All" : "None");
        body.put("UserAttendanceImportBack[" + import_line + "+][9]", "00:00:00");

        return body;

    }
}

