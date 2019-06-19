package com.darwinbox.attendance.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Shift implements Serializable {

    private String shiftName;
    private String shiftID;
    private String shiftDescription;
    private String groupCompany;
    private String policyId;

    private int startTime;
    private int endTime;
    private int workDuration;

    private boolean isOverNightShift;
    private boolean showShiftChangeRequest;

    public void toObject(Map<String, String> shift) {

        setShiftID(shift.get("Id"));
        setShiftName(shift.get("Shift Name"));
        setShiftDescription(shift.getOrDefault("Description","Shift Created via Automation"));
        setGroupCompany(shift.getOrDefault("Group Company",""));
        setStartTime(shift.get("Start Time"));
        setEndTime(shift.get("End Time"));
        setOverNightShift(shift.get("Is Overnight").toLowerCase().contains("yes"));
        setShowShiftChangeRequest(shift.get("Show in Change Request").toLowerCase().contains("yes"));
        setPolicyId(shift.getOrDefault("Policy ID",""));

    }

    public boolean compareTo(Shift obj) {

        return
                obj.getStartTime() == getStartTime() &&
                obj.getEndTime() == getEndTime() &&
                obj.isOverNightShift() == isOverNightShift() &&
                obj.isShowShiftChangeRequest() == isShowShiftChangeRequest() &&
                obj.getGroupCompany().equals(getGroupCompany());
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftDescription() {
        return shiftDescription;
    }

    public void setShiftDescription(String shiftDescription) {
        this.shiftDescription = shiftDescription;
    }

    public String getGroupCompany() {
        return groupCompany;
    }

    public void setGroupCompany(String groupCompany) {
        this.groupCompany = groupCompany;
    }

    public int getStartTime() {
        return (startTime);
    }

    public void setStartTime(String startTime) {
        String [] temp = startTime.split(":");
        int hrs = Integer.parseInt(temp[0]);
        int mins = Integer.parseInt(temp[1]);
        this.startTime = (hrs * 60 ) + mins;

    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        String [] temp = endTime.split(":");
        int hrs = Integer.parseInt(temp[0]);
        int mins = Integer.parseInt(temp[1]);
        this.endTime = (hrs * 60 ) + mins;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        if (policyId == null )
            policyId = "";
        this.policyId = policyId;
    }

    public boolean isOverNightShift() {
        return isOverNightShift;
    }

    public void setOverNightShift(boolean overNightShift) {
        isOverNightShift = overNightShift;
    }

    public boolean isShowShiftChangeRequest() {
        return showShiftChangeRequest;
    }

    public void setShowShiftChangeRequest(boolean showShiftChangeRequest) {
        this.showShiftChangeRequest = showShiftChangeRequest;
    }

    public String getShiftID() {
        if(shiftID == null)
            shiftID = "";
        return shiftID;
    }

    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }

    public int getWorkDuration() {
        workDuration = endTime - startTime;
        if ( isOverNightShift )
            workDuration = workDuration + 1440;
        return workDuration;
    }

    public String parseToPHP(boolean flag){
        return flag ? "1" : "0";
    }

    public Map<String,String> getShiftToMap(){

        Map<String,String> map = new HashMap<String,String> ();
        map.put("TenantShiftForm[shift_name]",getShiftName());
        map.put("TenantShiftForm[shift_description]",getShiftDescription());
        map.put("TenantShiftForm[parent_company_id]",getGroupCompany());
        map.put("TenantShiftForm[begin_time_hour]",(startTime / 60 )+"");
        map.put("TenantShiftForm[begin_time_min]",(startTime % 60 )+"");
        map.put("TenantShiftForm[end_time_hour]",(endTime / 60 )+"");
        map.put("TenantShiftForm[end_time_min]",(endTime % 60 )+"");
        map.put("TenantShiftForm[is_next_day]",parseToPHP(isOverNightShift));
        map.put("TenantShiftForm[show_in_request]",parseToPHP(showShiftChangeRequest));
        map.put("TenantShiftForm[policy_id]", getPolicyId());
        map.put("TenantShiftForm[id]",getShiftID());
        return map;
    }

}