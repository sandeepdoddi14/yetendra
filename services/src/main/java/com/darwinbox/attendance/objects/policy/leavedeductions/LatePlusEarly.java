package com.darwinbox.attendance.objects.policy.leavedeductions;

import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LatePlusEarly extends LateOrEarlyBase implements Serializable {

    public boolean isCount_as_2() {
        return count_as_2;
    }

    public void setCount_as_2(boolean count_as_2) {
        this.count_as_2 = count_as_2;
    }

    private boolean count_as_2;

    public static LatePlusEarly jsonToObject(Map<String, Object> data) {

        LatePlusEarly lateEarly = null;

        if (getFilterObject(data, "lateplusearlymark_policy", "1")) {

            lateEarly = new LatePlusEarly();

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

    public static Map<String,String> getMap(LatePlusEarly latePlusEarly) {

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

    public static boolean compareTo(LatePlusEarly lateEarly, LatePlusEarly lateEarly1) {

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

    public Map<String,String> getLateEarly(String employeeID, String policyName, Shift shift, Date date, boolean isWeekoff) {

        DateTimeHelper helper = new DateTimeHelper();

        String shiftDate = helper.formatDateTo(date,"dd-MM-yyyy" );

        Map<String,String> body = new HashMap<>();

        body.put("UserAttendanceImportBack[2][0]", employeeID);
        body.put("UserAttendanceImportBack[2][1]",shiftDate);
        body.put("UserAttendanceImportBack[2][2]","");
        body.put("UserAttendanceImportBack[2][3]","");
        body.put("UserAttendanceImportBack[2][4]","");
        body.put("UserAttendanceImportBack[2][5]","");
        body.put("UserAttendanceImportBack[2][6]",shift.getShiftName());
        body.put("UserAttendanceImportBack[2][7]",policyName);
        body.put("UserAttendanceImportBack[2][8]",isWeekoff ? "All" : "None");
        body.put("UserAttendanceImportBack[2][9]","");

        return body;

    }
}

