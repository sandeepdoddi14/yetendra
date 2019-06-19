package com.darwinbox.attendance.objects.policy.others;

import com.darwinbox.attendance.objects.policy.leavedeductions.LeaveDeductionsBase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestDays implements Serializable {

    private int edit_backdated = -1;
    private int req_backdated = -1;
    private int roster_backdated = -1;
    private int autoApprove = -1;
    private int max_optional = -1;
    private int abscondCount = -1;

    public int getEdit_backdated() {
        return edit_backdated;
    }

    public void setEdit_backdated(int edit_backdated) {
        this.edit_backdated = edit_backdated;
    }

    public int getReq_backdated() {
        return req_backdated;
    }

    public void setReq_backdated(int req_backdated) {
        this.req_backdated = req_backdated;
    }

    public int getRoster_backdated() {
        return roster_backdated;
    }

    public void setRoster_backdated(int roster_backdated) {
        this.roster_backdated = roster_backdated;
    }

    public int getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(int autoApprove) {
        this.autoApprove = autoApprove;
    }

    public int getMax_optional() {
        return max_optional;
    }

    public void setMax_optional(int max_optional) {
        this.max_optional = max_optional;
    }

    public int getAbscondCount() {
        return abscondCount;
    }

    public void setAbscondCount(int abscondCount) {
        this.abscondCount = abscondCount;
    }


    public void toObject(Map<String, String> data) {

        boolean flagStatus = LeaveDeductionsBase.getFilter(data,"Edit Attendance","no");
        if ( !flagStatus)
            setEdit_backdated(Integer.parseInt(data.get("Edit Attendance")));

        flagStatus = LeaveDeductionsBase.getFilter(data,"Request Attendance","no");
        if ( !flagStatus)
            setReq_backdated(Integer.parseInt(data.get("Request Attendance")));

        flagStatus = LeaveDeductionsBase.getFilter(data,"Edit Roster","no");
        if ( !flagStatus)
            setRoster_backdated(Integer.parseInt(data.get("Edit Roster")));

        flagStatus = LeaveDeductionsBase.getFilter(data,"AutoApprove","no");
        if ( !flagStatus)
            setAutoApprove(Integer.parseInt(data.get("AutoApprove")));

        flagStatus = LeaveDeductionsBase.getFilter(data,"Abscond","no");
        if ( !flagStatus)
            setAbscondCount(Integer.parseInt(data.get("Abscond")));

        flagStatus = LeaveDeductionsBase.getFilter(data,"Optional Holidays","no");
        if ( !flagStatus)
            setMax_optional(Integer.parseInt(data.get("Optional Holidays")));

    }

    public static RequestDays jsonToObject(Map<String, Object> data) {

       RequestDays days = new RequestDays();

        boolean flagStatus = LeaveDeductionsBase.getFilterObject(data,"edit_back_days","");
        if ( !flagStatus)
            days.setEdit_backdated(Integer.parseInt(data.get("edit_back_days").toString()));

        flagStatus = LeaveDeductionsBase.getFilterObject(data,"apply_back_days_employee","");
        if ( !flagStatus)
            days.setReq_backdated(Integer.parseInt(data.get("apply_back_days_employee").toString()));

        flagStatus = LeaveDeductionsBase.getFilterObject(data,"roster_back_days","");
        if ( !flagStatus)
            days.setRoster_backdated(Integer.parseInt(data.get("roster_back_days").toString()));

        flagStatus = LeaveDeductionsBase.getFilterObject(data,"auto_approve_days","");
        if ( !flagStatus)
            days.setAutoApprove(Integer.parseInt(data.get("auto_approve_days").toString()));

        flagStatus = LeaveDeductionsBase.getFilterObject(data,"absconding_days","");
        if ( !flagStatus)
            days.setAbscondCount(Integer.parseInt(data.get("absconding_days").toString()));

        flagStatus = LeaveDeductionsBase.getFilterObject(data, "max_optional_holiday_policy","");
        if ( !flagStatus)
            days.setMax_optional(Integer.parseInt(data.get("max_optional_holiday_policy").toString()));

        return days;

    }

    public Map<String,String> getMap() {

        Map<String, String> data = new HashMap<>();

        String flag = getEdit_backdated() == -1 ? "" : getEdit_backdated()+"";
        data.put("AttendancePolicyForm[edit_back_days]",flag);

        flag = getReq_backdated() == -1 ? "" : getReq_backdated()+"";
        data.put("AttendancePolicyForm[apply_back_days_employee]",flag);

        flag = getRoster_backdated() == -1 ? "" : getRoster_backdated()+"";
        data.put("AttendancePolicyForm[roster_back_days]",flag);

        flag = getAutoApprove() == -1 ? "" : getAutoApprove()+"";
        data.put("AttendancePolicyForm[auto_approve_days]",flag);

        flag = getAbscondCount() == -1 ? "" : getAbscondCount()+"";
        data.put("AttendancePolicyForm[absconding_days]",flag);

        flag = getMax_optional() == -1 ? "" : getMax_optional()+"";
        data.put("AttendancePolicyForm[max_optional_holiday_policy]",flag);

        return data;
    }

    public boolean compareTo(RequestDays reqDays) {

        return  reqDays.getEdit_backdated() ==  getEdit_backdated() &&
                reqDays.getReq_backdated() ==  getReq_backdated() &&
                reqDays.getRoster_backdated() ==  getRoster_backdated() &&
                reqDays.getAutoApprove() ==  getAutoApprove() &&
                reqDays.getAbscondCount() ==  getAbscondCount() &&
                reqDays.getMax_optional() ==  getMax_optional();

    }
}
