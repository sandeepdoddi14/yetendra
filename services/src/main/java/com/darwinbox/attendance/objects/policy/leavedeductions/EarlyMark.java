package com.darwinbox.attendance.objects.policy.leavedeductions;

import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.others.PolicyInfo;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EarlyMark extends LateOrEarlyBase implements Serializable {


    public static EarlyMark jsonToObject(Map<String, Object> data) {

        EarlyMark earlyMark = null;

        if (data.get("earlymark_policy").toString().equals("1")) {

            earlyMark = new EarlyMark();

            boolean isApprovalRequired = getFilterObject(data, "earlymark_deduct_after_approval", "1");
            boolean isHoliday = getFilterObject(data, "earlymark_deduction_on_holiday", "1");
            boolean isWeeklyOff = getFilterObject(data, "earlymark_deduction_on_weeklyoff", "1");
            boolean isInDay = getFilterObject(data, "earlymark_deduction_on_leave", "1");
            boolean isHalfDay = getFilterObject(data, "earlymark_leave_deduction", "2");
            boolean isForEvery = getFilterObject(data, "every_next_earlymark", "1");
            int count = Integer.parseInt(data.get("number_of_earlymarks").toString());

            earlyMark.isApprovalRequired(isApprovalRequired);
            earlyMark.setHoliday(isHoliday);
            earlyMark.setWeekoff(isWeeklyOff);
            earlyMark.setInDay(isInDay);
            earlyMark.setCount(count);
            earlyMark.setLeaveId(data.get("earlymark_leave_type").toString());
            earlyMark.setHalfDay(isHalfDay);
            earlyMark.setForEvery(isForEvery);

        }

        return earlyMark;

    }

    public static Map<String, String> getMap(EarlyMark earlyMark) {

        Map<String, String> body = new HashMap<>();

        if (earlyMark != null) {
            body.put("AttendancePolicyForm[earlymark_policy]", "1");
            body.put("AttendancePolicyForm[earlymark_leave_type]", earlyMark.getLeaveId());
            body.put("AttendancePolicyForm[earlymark_deduct_after_approval]", parseToPHP(earlyMark.isApprovalRequired()));
            body.put("AttendancePolicyForm[earlymark_deduction_on_holiday]", parseToPHP(earlyMark.isHoliday()));
            body.put("AttendancePolicyForm[earlymark_deduction_on_weeklyoff]", parseToPHP(earlyMark.isWeekoff()));
            body.put("AttendancePolicyForm[earlymark_deduction_on_leave]", parseToPHP(earlyMark.isInDay()));
            body.put("AttendancePolicyForm[earlymark_leave_deduction]", (earlyMark.isHalfDay() ? "2" : "1"));
            body.put("AttendancePolicyForm[number_of_earlymarks]", (earlyMark.getCount() + ""));
            body.put("AttendancePolicyForm[every_next_earlymark]", parseToPHP(earlyMark.isForEvery()));

        }
        return body;
    }

    public static boolean compareTo(EarlyMark earlyMark, EarlyMark earlyMark1) {

        boolean status = false;
        if (earlyMark != null) {
            if (earlyMark1 != null)
                status = LateOrEarlyBase.compareToSuper(earlyMark, earlyMark1);
            return status;
        } else if (earlyMark1 != null) {
            return status;
        } else {
            return !status;
        }
    }

    public void toObject(Map<String, String> data) {

        boolean isApprovalRequired = getFilter(data, "RequiresApproval", "yes");
        boolean isHoliday = getFilter(data, "IsOnHoliday", "yes");
        boolean isWeeklyOff = getFilter(data, "IsOnWeeklyOff", "yes");
        boolean isInDay = getFilter(data, "IsInDay", "yes");
        boolean isHalfDay = getFilter(data, "LeaveType", "HalfDay");
        boolean isForEvery = getFilter(data, "ForEvery", "yes");

        int n = Integer.parseInt(data.get("No.of Instances"));

        isApprovalRequired(isApprovalRequired);
        setHoliday(isHoliday);
        setWeekoff(isWeeklyOff);
        setInDay(isInDay);
        setHalfDay(isHalfDay);
        setForEvery(isForEvery);
        setLeaveId(data.get("LeaveToDeductId"));
        setCount(n);
    }

    public Map<String, String> getEarlyMarks(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff) {

        Map<String, String> body = new HashMap<>();
        DateTimeHelper helper = new DateTimeHelper();

        for ( int i = 0; i < 2*getCount(); i++ ) {
            body.putAll(getEarly(empId, policyInfo, shift, date, isWeekOff, i + 2));
            date = helper.getNextDate(date);
        }

        return body;
    }

    public Map<String, String> getEarlyMark(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff) {

        return getEarly(empId, policyInfo, shift, date, isWeekOff, 2);
    }


    private Map<String,String> getEarly(String empId, PolicyInfo policyInfo, Shift shift, Date date, boolean isWeekOff, int i){
        DateTimeHelper helper = new DateTimeHelper();

        Map<String, String> body = new HashMap<>();

        int shiftStart = shift.getStartTime();
        int shiftEnd = shift.getEndTime() - policyInfo.getGraceTimeOut() - 1;

        String shiftDate = helper.formatDateTo(date, "dd-MM-yyyy");
        String inDate = shiftDate;
        String outDate = shift.isOverNightShift() ? helper.getNextDate(shiftDate) : shiftDate;

        String inTime = helper.parseTime(shiftStart % 1440);
        String outTime = helper.parseTime(shiftEnd % 1440);

        if ((shiftEnd > shiftStart) && shift.isOverNightShift())
            outDate = inDate;

        body.put("UserAttendanceImportBack["+i+"][0]", empId);
        body.put("UserAttendanceImportBack["+i+"][1]", shiftDate);
        body.put("UserAttendanceImportBack["+i+"][2]", inDate);
        body.put("UserAttendanceImportBack["+i+"][3]", inTime);
        body.put("UserAttendanceImportBack["+i+"][4]", outDate);
        body.put("UserAttendanceImportBack["+i+"][5]", outTime);
        body.put("UserAttendanceImportBack["+i+"][6]", shift.getShiftName());
        body.put("UserAttendanceImportBack["+i+"][7]", policyInfo.getPolicyName());
        body.put("UserAttendanceImportBack["+i+"][8]", isWeekOff ? "All" : "None");
        body.put("UserAttendanceImportBack["+i+"][9]", "00:00:00");

        return body;

    }


}
