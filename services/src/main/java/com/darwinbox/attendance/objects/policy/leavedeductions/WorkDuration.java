package com.darwinbox.attendance.objects.policy.leavedeductions;

import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkDuration extends LeaveDeductionsBase implements Serializable {

        private int minTime;
        private int maxTime;
        private boolean isFinal;

        public boolean isFinal() {
            return isFinal;
        }

        public void setFinal(boolean aFinal) {
            isFinal = aFinal;
        }

        public void setWdhrs_halfday(String wdhrs_halfday) {
            minTime = minTime + Integer.parseInt(wdhrs_halfday) * 60;
        }

        public void setWdhrs_fullday(String wdhrs_fullday) {
            maxTime = maxTime + Integer.parseInt(wdhrs_fullday) * 60;
        }

        public void setWdmins_halfday(String wdmins_halfday) {
            minTime = minTime + Integer.parseInt(wdmins_halfday);
        }

        public void setWdmins_fullday(String wdmins_fullday) {
            maxTime = maxTime + Integer.parseInt(wdmins_fullday);
        }

        public int getMinTime() {
            return minTime;
        }

        public int getMaxTime() {
            return maxTime;
        }

    public static WorkDuration jsonToObject(Map<String, Object> data) {

        WorkDuration workDuration = null;

        if (data.get("work_duration_policy").toString().equals("1")) {

            workDuration = new WorkDuration();

            boolean isApprovalRequired = getFilterObject(data, "work_duration_deduct_after_approval", "1");
            boolean isHoliday = getFilterObject(data, "work_duration_deduction_on_holiday", "1");
            boolean isWeeklyOff = getFilterObject(data, "work_duration_deduction_on_weeklyoff", "1");
            boolean isInDay = getFilterObject(data, "work_duration_deduction_on_leave", "1");
            boolean isFinal = getFilterObject(data, "calculate_on_net_work_duration", "1");

            String wdHrs_half = data.get("clock_in_hrs_half_day").toString();
            String wdmins_half = data.get("clock_in_min_half_day").toString();
            String wdHrs_full = data.get("clock_in_hrs_full_day").toString();
            String wdmins_full = data.get("clock_in_min_full_day").toString();

            workDuration.isApprovalRequired(isApprovalRequired);
            workDuration.setHoliday(isHoliday);
            workDuration.setWeekoff(isWeeklyOff);
            workDuration.setInDay(isInDay);
            workDuration.setFinal(isFinal);
            workDuration.setLeaveId(data.get("work_duration_leave_type").toString());
            workDuration.setWdhrs_fullday(wdHrs_full);
            workDuration.setWdmins_fullday(wdmins_full);
            workDuration.setWdhrs_halfday(wdHrs_half);
            workDuration.setWdmins_halfday(wdmins_half);

        }

        return workDuration;
    }

    public void toObject(Map<String, String> data) {

        boolean isApprovalRequired = getFilter(data, "RequiresApproval", "yes");
        boolean isHoliday = getFilter(data, "IsOnHoliday", "yes");
        boolean isWeeklyOff = getFilter(data, "IsOnWeeklyOff", "yes");
        boolean isInDay = getFilter(data, "IsInDay", "yes");
        boolean isFinal = getFilter(data, "IsFinal", "yes");

        String halfday [] = data.get("MinHrs").split(":");
        String fullday [] = data.get("MaxHrs").split(":");

        isApprovalRequired(isApprovalRequired);
        setHoliday(isHoliday);
        setWeekoff(isWeeklyOff);
        setInDay(isInDay);
        setFinal(isFinal);
        setLeaveId(data.get("LeaveToDeductId"));
        setWdhrs_fullday(fullday[0]);
        setWdmins_fullday(fullday[1]);
        setWdhrs_halfday(halfday[0]);
        setWdmins_halfday(halfday[1]);

    }

    public static Map<String,String> getMap(WorkDuration workDuration) {

        Map<String, String> data = new HashMap<>();

        if (workDuration != null) {

            data.put("AttendancePolicyForm[work_duration_policy]", parseToPHP(true));
            data.put("AttendancePolicyForm[work_duration_deduct_after_approval]", parseToPHP(workDuration.isApprovalRequired()));
            data.put("AttendancePolicyForm[work_duration_deduction_on_holiday]", parseToPHP(workDuration.isHoliday()));
            data.put("AttendancePolicyForm[work_duration_deduction_on_weeklyoff]", parseToPHP(workDuration.isWeekoff()));
            data.put("AttendancePolicyForm[work_duration_deduction_on_leave]", parseToPHP(workDuration.isInDay()));
            data.put("AttendancePolicyForm[calculate_on_net_work_duration]", parseToPHP(workDuration.isFinal()));
            data.put("AttendancePolicyForm[work_duration_leave_type]", workDuration.getLeaveId());

            data.put("AttendancePolicyForm[clock_in_hrs_half_day]", (workDuration.getMinTime() / 60) + "");
            data.put("AttendancePolicyForm[clock_in_min_half_day]", (workDuration.getMinTime() % 60) + "");
            data.put("AttendancePolicyForm[clock_in_hrs_full_day]", (workDuration.getMaxTime() / 60) + "");
            data.put("AttendancePolicyForm[clock_in_min_full_day]", (workDuration.getMaxTime() % 60) + "");

        }

        return data;
    }

    public static boolean compareTo(WorkDuration workDuration, WorkDuration workDuration1) {
        if (workDuration != null) {

            if (workDuration1 != null) {

                boolean status = LeaveDeductionsBase.compareToSuper(workDuration, workDuration1);

                return status && workDuration.isFinal() == workDuration1.isFinal() &&
                       workDuration.getMaxTime() == workDuration1.getMaxTime() &&
                       workDuration.getMinTime() == workDuration1.getMinTime();

            }

            return false;

        } else if (workDuration1 != null) {

            return false;

        } else {

            return true;

        }
    }

    public Map<String,String> getWorkDuration(String empId, String policyName, Shift shift, Date date, boolean isHalf, boolean isFinal, boolean isWeekoff) {

        DateTimeHelper helper = new DateTimeHelper();

        String shiftDate = helper.formatDateTo(date,"dd-MM-yyyy" );
        String inDate = shiftDate;
        String outDate =   shift.isOverNightShift() ? helper.getNextDate(shiftDate) : shiftDate;

        int minDur = getMinTime();
        int maxDur = getMaxTime();

        int shiftStart = shift.getStartTime();
        int shiftEnd = shiftStart + maxDur -2;
        int breakDur = 0;

        if(isHalf) {
            shiftEnd = shiftStart + minDur - 2;
        }

        if ( isFinal) {
            breakDur = 1;
        }

        if (shiftEnd > 1440 && !shift.isOverNightShift()) {
            outDate = helper.getNextDate(inDate);
        }

        String inTime = helper.parseTime(shiftStart%1440);
        String outTime = helper.parseTime(shiftEnd%1440);
        String breakTime = helper.parseTime(breakDur);

        Map<String,String> body = new HashMap<>();

        body.put("UserAttendanceImportBack[2][0]", empId);
        body.put("UserAttendanceImportBack[2][1]",shiftDate);
        body.put("UserAttendanceImportBack[2][2]",inDate);
        body.put("UserAttendanceImportBack[2][3]",inTime);
        body.put("UserAttendanceImportBack[2][4]",outDate);
        body.put("UserAttendanceImportBack[2][5]",outTime);
        body.put("UserAttendanceImportBack[2][6]",shift.getShiftName());
        body.put("UserAttendanceImportBack[2][7]",policyName);
        body.put("UserAttendanceImportBack[2][8]",isWeekoff ? "All" : "None");
        body.put("UserAttendanceImportBack[2][9]",breakTime);

        return body;

    }
}
