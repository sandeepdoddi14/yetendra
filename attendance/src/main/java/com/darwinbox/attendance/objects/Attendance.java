package com.darwinbox.attendance.objects;

public class Attendance {
    private boolean isEarlyMark;
    private boolean isLarkMark;
    private String timeIn;
    private String timeOut;
    private String date;
    private String totalWorkDuration;
    private String breakDuration;
    private String finalWorkDuration;
    private boolean isOverNight;
    private boolean isWeeklyOff;
    private boolean isHoliday;
    private boolean isAbsent;
    private boolean isPresent;
    private boolean isOnLeave;
    private boolean hasPendingLeave;
    private boolean isHalfDay;
    private AttendancePolicy attendancePolicy;
    private Shift shift;
    private String status;
    private String assignment;

    public boolean isEarlyMark() {

        return isEarlyMark;

    }

    public void setEarlyMark(boolean earlyMark) {
        isEarlyMark = earlyMark;
    }

    public boolean isLarkMark() {
        return isLarkMark;
    }

    public void setLarkMark(boolean larkMark) {
        isLarkMark = larkMark;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isWeeklyOff() {
        return isWeeklyOff;
    }

    public void setWeeklyOff(boolean weeklyOff) {
        isWeeklyOff = weeklyOff;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public boolean isOnLeave() {
        return isOnLeave;
    }

    public void setOnLeave(boolean onLeave) {
        isOnLeave = onLeave;
    }

    public AttendancePolicy getAttendancePolicy() {
        return attendancePolicy;
    }

    public void setAttendancePolicy(AttendancePolicy attendancePolicy) {
        this.attendancePolicy = attendancePolicy;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public boolean isHalfDay() {
        return isHalfDay;
    }

    public void setHalfDay(boolean halfDay) {
        isHalfDay = halfDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalWorkDuration() {
        return totalWorkDuration;
    }

    public void setTotalWorkDuration(String totalWorkDuration) {
        this.totalWorkDuration = totalWorkDuration;
    }

    public String getFinalWorkDuration() {
        return finalWorkDuration;
    }

    public void setFinalWorkDuration(String finalWorkDuration) {
        this.finalWorkDuration = finalWorkDuration;
    }

    public String getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(String breakDuration) {
        this.breakDuration = breakDuration;
    }

    public boolean isHasPendingLeave() {
        return hasPendingLeave;
    }

    public void setHasPendingLeave(boolean hasPendingLeave) {
        this.hasPendingLeave = hasPendingLeave;
    }

    public boolean isOverNight() {
        return isOverNight;
    }

    public void setOverNight(boolean overNight) {
        isOverNight = overNight;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }
}
