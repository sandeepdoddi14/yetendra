package com.darwinbox.attendance.objects.policy.others;

import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoShift implements Serializable {

    private List<Shift> shiftList = new ArrayList<Shift>();
    private boolean nearest;

    public static AutoShift jsonToObject(Map<String, Object> data) {

        AutoShift autoShift = null;

        if (data.get("allow_auto_shift_assignment").equals("1")) {

            autoShift = new AutoShift();

            boolean isNearest = LeaveDeductionsBase.getFilterObject(data, "nearest", "1");

            ArrayList<String> list = (ArrayList<String>) data.get("allowed_shift_auto_assignment");

            autoShift.setNearest(isNearest);

        }

        return autoShift;
    }

    public static Map<String, String> getMap(AutoShift autoshift) {

        Map<String, String> data = new HashMap<>();

        if (autoshift != null) {

            data.put("AttendancePolicyForm[allow_auto_shift_assignment]", "1");
            data.put("AttendancePolicyForm[nearest]", autoshift.isNearest() ? "1" : "2");
            data.put("AttendancePolicyForm[allowed_shift_auto_assignment]", autoshift.isNearest() ? "1" : "2");

        }

        return data;
    }

    public static boolean compareTo(AutoShift autoShift, AutoShift autoShift1) {

        if (autoShift != null) {

            if (autoShift1 != null) {

                return autoShift.isNearest() == autoShift1.isNearest();
            }

            return false;

        } else if (autoShift1 != null) {

            return false;

        } else {

            return true;

        }
    }

    public boolean isNearest() {
        return nearest;
    }

    public void setNearest(boolean nearest) {
        this.nearest = nearest;
    }

    public List<Shift> getShiftList() {
        return shiftList;
    }

    public void setShiftList(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }
}
