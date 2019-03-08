package com.darwinbox.attendance.objects;

public class Shift {

    private String shiftName;
    private String shiftID;
    private String shiftDescription;
    private String groupCompany;
    private String startTime;
    private String endTime;
    private String workDuration;
    private String policy;
    private boolean isOverNightShift;
    private boolean showShiftChangeRequest;

    public Shift() {

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
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
        return shiftID;
    }

    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }


    public String getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(String workDuration) {
        this.workDuration = workDuration;
    }

}
