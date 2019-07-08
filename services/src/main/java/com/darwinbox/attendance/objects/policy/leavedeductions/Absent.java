package com.darwinbox.attendance.objects.policy.leavedeductions;

import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Absent extends LeaveDeductionsBase implements Serializable {

    private boolean isHalfDay;

    public boolean isHalfDay() {
        return isHalfDay;
    }

    public void setHalfDay(boolean halfDay) {
        isHalfDay = halfDay;
    }

    public void toObject(Map<String, String> data) {

        boolean isApprovalRequired = getFilter(data, "RequiresApproval", "yes");
        boolean isHoliday = getFilter(data, "IsOnHoliday", "yes");
        boolean isWeeklyOff = getFilter(data, "IsOnWeeklyOff", "yes");
        boolean isInDay = getFilter(data, "IsInDay", "yes");
        boolean isHalfDay = getFilter(data, "LeaveType", "HalfDay");

        isApprovalRequired(isApprovalRequired);
        setHoliday(isHoliday);
        setWeekoff(isWeeklyOff);
        setInDay(isInDay);
        setHalfDay(isHalfDay);
        setLeaveId(data.get("LeaveToDeductId"));
    }

    public static Absent jsonToObject(Map<String, Object> data) {

        Absent absent = null;

        if (data.get("leave_deduction_policy").toString().equals("1")) {

            absent = new Absent();

            boolean isApprovalRequired = getFilterObject(data, "leave_deduction_deduct_after_approval", "1");
            boolean isHoliday = getFilterObject(data, "leave_deduction_on_holiday", "1");
            boolean isWeeklyOff = getFilterObject(data, "leave_deduction_on_weeklyoff", "1");
            boolean isInDay = getFilterObject(data, "leave_deduction_on_leave", "1");
            boolean isHalfDay = getFilterObject(data, "absent_leave_deduction", "2");


            absent.isApprovalRequired(isApprovalRequired);
            absent.setHoliday(isHoliday);
            absent.setWeekoff(isWeeklyOff);
            absent.setInDay(isInDay);
            absent.setHalfDay(isHalfDay);
            absent.setLeaveId(data.get("leave_deduction").toString());
        }

        return absent;
    }

    public static Map<String,String> getMap(Absent absent) {

        Map<String,String> body = new HashMap<>();

        if (absent != null ) {
            body.put("AttendancePolicyForm[leave_deduction_policy]", "1");
            body.put("AttendancePolicyForm[leave_deduction]", absent.getLeaveId());
            body.put("AttendancePolicyForm[leave_deduction_deduct_after_approval]", parseToPHP(absent.isApprovalRequired()));
            body.put("AttendancePolicyForm[leave_deduction_on_holiday]", parseToPHP(absent.isHoliday()));
            body.put("AttendancePolicyForm[leave_deduction_on_weeklyoff]", parseToPHP(absent.isWeekoff()));
            body.put("AttendancePolicyForm[leave_deduction_on_leave]", parseToPHP(absent.isInDay()));
            body.put("AttendancePolicyForm[absent_leave_deduction]", (absent.isHalfDay() ? "2" : "1"));
        }
        return body;
    }

    public static boolean compareTo(Absent absent, Absent absent1) {

        if (absent != null) {

            if (absent1 != null) {

                boolean status = ( absent.isHalfDay() == absent1.isHalfDay() )
                             && LeaveDeductionsBase.compareToSuper(absent, absent1);
                return status;
            }

            return false;

        } else if (absent1 != null) {

            return false;

        } else {

            return true;

        }

    }

    public Map<String,String> getAbsent(String employeeID, String policyName, String shiftName, Date date, boolean isWeekoff) {

        DateTimeHelper helper = new DateTimeHelper();

        String shiftDate = helper.formatDateTo(date,"dd-MM-yyyy" );

        Map<String,String> body = new HashMap<>();

        body.put("UserAttendanceImportBack[2][0]", employeeID);
        body.put("UserAttendanceImportBack[2][1]",shiftDate);
        body.put("UserAttendanceImportBack[2][2]","");
        body.put("UserAttendanceImportBack[2][3]","");
        body.put("UserAttendanceImportBack[2][4]","");
        body.put("UserAttendanceImportBack[2][5]","");
        body.put("UserAttendanceImportBack[2][6]",shiftName);
        body.put("UserAttendanceImportBack[2][7]",policyName);
        body.put("UserAttendanceImportBack[2][8]",isWeekoff ? "All" : "None");
        body.put("UserAttendanceImportBack[2][9]","");

        return body;

    }
}
