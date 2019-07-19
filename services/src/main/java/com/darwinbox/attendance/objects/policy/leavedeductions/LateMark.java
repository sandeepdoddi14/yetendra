package com.darwinbox.attendance.objects.policy.leavedeductions;

import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.others.PolicyInfo;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LateMark extends LateOrEarlyBase implements Serializable {

    public static LateMark jsonToObject(Map<String, Object> data) {

        LateMark lateMark = null;

        if (data.get("latemark_policy").toString().equals("1")) {

            lateMark = new LateMark();

            boolean isApprovalRequired = getFilterObject(data, "latemark_deduct_after_approval", "1");
            boolean isHoliday = getFilterObject(data, "latemark_deduction_on_holiday", "1");
            boolean isWeeklyOff = getFilterObject(data, "latemark_deduction_on_weeklyoff", "1");
            boolean isInDay = getFilterObject(data, "latemark_deduction_on_leave", "1");
            boolean isForAll = getFilterObject(data, "every_next_latemark", "1");
            boolean isHalfDay = getFilterObject(data, "latemark_leave_deduction", "2");
            int count = Integer.parseInt(data.get("number_of_latemarks").toString());

            lateMark.isApprovalRequired(isApprovalRequired);
            lateMark.setHoliday(isHoliday);
            lateMark.setWeekoff(isWeeklyOff);
            lateMark.setInDay(isInDay);
            lateMark.setForEvery(isForAll);
            lateMark.setCount(count);
            lateMark.setLeaveId(data.get("latemark_leave_type").toString());
            lateMark.setHalfDay(isHalfDay);

        }

        return lateMark;

    }

    public static Map<String, String> getMap(LateMark lateMark) {

        Map<String, String> body = new HashMap<>();

        if (lateMark != null) {
            body.put("AttendancePolicyForm[latemark_policy]", "1");
            body.put("AttendancePolicyForm[latemark_leave_type]", lateMark.getLeaveId());
            body.put("AttendancePolicyForm[latemark_deduct_after_approval]", parseToPHP(lateMark.isApprovalRequired()));
            body.put("AttendancePolicyForm[latemark_deduction_on_holiday]", parseToPHP(lateMark.isHoliday()));
            body.put("AttendancePolicyForm[latemark_deduction_on_weeklyoff]", parseToPHP(lateMark.isWeekoff()));
            body.put("AttendancePolicyForm[latemark_deduction_on_leave]", parseToPHP(lateMark.isInDay()));
            body.put("AttendancePolicyForm[every_next_latemark]", parseToPHP(lateMark.isForEvery()));
            body.put("AttendancePolicyForm[latemark_leave_deduction]", (lateMark.isHalfDay() ? "2" : "1"));
            body.put("AttendancePolicyForm[number_of_latemarks]", (lateMark.getCount() + ""));
        }
        return body;
    }

    public static boolean compareTo(LateMark lateMark, LateMark lateMark1) {

        if (lateMark != null) {

            if (lateMark1 != null) {

                boolean status = LateOrEarlyBase.compareToSuper(lateMark, lateMark1);
                return status;

            }

            return false;

        } else if (lateMark1 != null) {

            return false;

        } else {

            return true;

        }
    }

    public void toObject(Map<String, String> data) {

        boolean isApprovalRequired = getFilter(data, "RequiresApproval", "yes");
        boolean isHoliday = getFilter(data, "IsOnHoliday", "yes");
        boolean isWeeklyOff = getFilter(data, "IsOnWeeklyOff", "yes");
        boolean isInDay = getFilter(data, "IsInDay", "yes");
        boolean isHalfDay = getFilter(data, "LeaveType", "HalfDay");
        boolean isEvery = getFilter(data, "ForEveryInstance", "yes");
        int n = Integer.parseInt(data.get("No.of Instances"));

        isApprovalRequired(isApprovalRequired);
        setHoliday(isHoliday);
        setWeekoff(isWeeklyOff);
        setInDay(isInDay);
        setHalfDay(isHalfDay);
        setLeaveId(data.get("LeaveToDeductId"));
        setForEvery(isEvery);
        setCount(n);
    }

    public Map<String, String> getLatemarks(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff) {

        DateTimeHelper helper = new DateTimeHelper();
        Map<String, String> body = new HashMap<>();

        for (int i = 0; i < 2 * getCount(); i++) {
            body.putAll(getLatemark(empId, policyInfo, shift, date, isWeekOff, i + 2));
            date = helper.getNextDate(date);
        }

        return body;

    }

    public Map<String, String> getLatemark(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff) {
        return (getLatemark(empId, policyInfo, shift, date, isWeekOff, 2));
    }

    private Map<String, String> getLatemark(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff, int import_line) {

        DateTimeHelper helper = new DateTimeHelper();

        String shiftDate = helper.formatDateTo(date, "dd-MM-yyyy");
        String inDate = shiftDate;
        String outDate = shift.isOverNightShift() ? helper.getNextDate(shiftDate) : shiftDate;

        int shiftStart = shift.getStartTime() + policyInfo.getGraceTimeIn();
        int shiftEnd = shift.getEndTime();

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
