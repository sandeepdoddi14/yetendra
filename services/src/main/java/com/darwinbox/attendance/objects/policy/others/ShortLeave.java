package com.darwinbox.attendance.objects.policy.others;

import com.darwinbox.attendance.objects.policy.AttendancePolicy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ShortLeave implements Serializable {

    private int minTime;
    private int maxTime;
    private int count;

    public static ShortLeave jsonToObject(Map<String, Object> data) {

        ShortLeave shortleave = null;

        if (data.get("short_leave_allowed_attendance_regularise").equals("1")) {

            int minMins = Integer.parseInt(data.get("short_leave_min_mins").toString());
            int maxMins = Integer.parseInt(data.get("short_leave_max_mins").toString());
            int count = Integer.parseInt(data.get("short_leave_allowed_days").toString());

            shortleave.setMaxTime(maxMins);
            shortleave.setMinTime(minMins);
            shortleave.setCount(count);

        }

        return shortleave;
    }

    public static Map<String, String> getMap(ShortLeave shortLeave) {

        Map<String, String> data = new HashMap<>();

        if (shortLeave != null) {

            data.put("AttendancePolicyForm[short_leave_allowed_attendance_regularise]", "1");
            data.put("AttendancePolicyForm[short_leave_min_mins]", shortLeave.getMinTime() + "");
            data.put("AttendancePolicyForm[short_leave_max_mins]", shortLeave.getMaxTime() + "");
            data.put("AttendancePolicyForm[short_leave_allowed_days]", shortLeave.getCount() + "");
        }

        return data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public static boolean compareTo(ShortLeave shortLeave, ShortLeave shortLeave1) {

        if ( shortLeave != null ) {

            if ( shortLeave1 != null) {

                return shortLeave.getMaxTime() == shortLeave1.getMaxTime() &&
                shortLeave.getMinTime() == shortLeave1.getMinTime() &&
                shortLeave.getCount() == shortLeave1.getCount();

            }

            return false;

        } else if ( shortLeave1 != null) {

            return false;

        } else {

            return true;

        }
    }
}

