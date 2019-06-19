package com.darwinbox.attendance.objects;

import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.objects.policy.others.BufferTime;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import java.util.*;
import java.util.stream.Collectors;

import static com.darwinbox.attendance.objects.AttendancePunch.Status.*;

public class Attendance {

    boolean isEmcure = false;
    boolean isDirectionality = false;
    private DateTimeHelper helper = new DateTimeHelper();

    private Date shiftDate;
    private Date startBufferTime;
    private Date endBufferTime;

    private Date recorded_ClockIn = null;
    private Date recorded_ClockOut = null;
    private Date clockIn = null;
    private Date clockOut = null;

    private List<AttendancePunch> punches = new ArrayList<>();
    private Shift shift;
    private AttendancePolicy policy;

    private long totalWorkDuration;
    private long  breakDuration;
    private long finalWorkDuration;

    private boolean isEarlyMark = false;
    private boolean isLateMark = false;
    private String timeIn;
    private String timeOut;

    private boolean isOverNight;
    private boolean isWeeklyOff;
    private boolean isHoliday;
    private boolean isAbsent;
    private boolean isPresent;
    private boolean isOnLeave;
    private boolean hasPendingLeave;
    private boolean isHalfDay;
    private boolean isOptionalHoliday;
    private String status;

    public Date getRecorded_ClockIn() {
        return recorded_ClockIn;
    }

    public Date getRecorded_ClockOut() {
        return recorded_ClockOut;
    }

    public void validateBuffer() {

        Date shiftdate = getShiftDate();

        if (shift.isOverNightShift()) {
            shiftdate = helper.setTime(shiftdate, 720);
        }

        Date inTime = helper.addTime(shiftdate, 0);
        Date outTime = helper.addEndTime(shiftdate, 1440);

        BufferTime buffer = policy.getBufferTime();

        if (buffer != null) {

            int start = shift.getStartTime();
            int end = shift.getEndTime();

            inTime = helper.setTime(inTime, start);
            outTime = helper.setTime(outTime, end);
            inTime = helper.addTime(inTime, -buffer.getInTime());
            outTime = helper.addEndTime(outTime, buffer.getOutTime());
        }

        setShiftDate(shiftdate);
        setStartBufferTime(inTime);
        setEndBufferTime(outTime);
    }

    private void setRecordedClockIn() {
        recorded_ClockIn = getFirstPunch();
    }

    private void setRecorded_ClockOut() {
        recorded_ClockOut = getLastPunch();

        if (helper.getDifference(recorded_ClockIn, recorded_ClockOut) == 0) {
            recorded_ClockOut = null;
        }
        else if(isEmcure)
            removePostPunches();

    }

    private Date getFirstPunch() {
        punches.sort(Comparator.comparing(AttendancePunch::getCreatedDate));
        List<AttendancePunch> data = punches.stream().filter(punch -> punch.getStatus() == 0 || !isEmcure).collect(Collectors.toList());
        Date date = null;
        if (data.size() >= 1)
            date = data.get(0).getTimestamp();
        return date;
    }

    private Date getLastPunch() {

        List<AttendancePunch> data = punches.stream().filter(punch -> punch.getStatus() == 1 || !isEmcure).collect(Collectors.toList());
        Date date = null;
        int count = data.size();
        if (count >= 1)
            date = data.get(count - 1).getTimestamp();
        return date;
    }

    public void removeDuplicates() {

        punches.sort(Comparator.comparing(AttendancePunch::getTimestamp));

        for (int i = 0; i < punches.size() - 1; i++) {

            AttendancePunch first = punches.get(i);
            AttendancePunch second = punches.get(i + 1);
            if (first.compareTo(second)) {
                punches.remove(i--);
            }
        }

        if(isEmcure)
            removePrePunches();
    }

    private void validatePunches(Date startTime, Date endTime) {

        for (int i = 0; i < punches.size(); i++) {

            Date timestamp = punches.get(i).getTimestamp();

            long in = helper.getDifference(timestamp, startTime);

            if (in < 0) {
                punches.remove(i);
                continue;
            }

            long out = helper.getDifference(timestamp, endTime);

            if (out > 0) {
                punches.remove(i);
            }
        }
    }

    public void validatePunches() {

        try {

            validatePunches(startBufferTime, endBufferTime);
            setRecordedClockIn();
            removeDuplicates();

            setRecorded_ClockOut();

            clockIn = punches.get(0).getTimestamp();
            clockOut = punches.get(punches.size() - 1).getTimestamp();

            validatePunches(clockIn, clockOut);

            if (helper.getDifference(clockIn, clockOut) == 0) {
                clockOut = null;
            }

        } catch (Exception e) {

        }

    }

    private void removePrePunches() {

        for ( int i = 0;i < punches.size(); i++){

            Date punch = punches.get(i).getTimestamp();

            if ( punch.compareTo(recorded_ClockIn) == -1 ) {
                punches.remove(i);
                i--;
            }
        }
    }

    private void removePostPunches() {

        for ( int i = 0;i < punches.size(); i++){

            Date punch = punches.get(i).getTimestamp();

            if ( recorded_ClockOut.compareTo(punch) == -1 )
                punches.remove(i--);
        }
    }


    public Date getStartBufferTime() {
        return startBufferTime;
    }

    private void setStartBufferTime(Date startBufferTime) {
        this.startBufferTime = startBufferTime;
    }

    public Date getEndBufferTime() {
        return endBufferTime;
    }

    private void setEndBufferTime(Date endBufferTime) {
        this.endBufferTime = endBufferTime;
    }

    public List<AttendancePunch> getPunches() {
        return punches;
    }

    public void setPunches(List<AttendancePunch> punches) {
        this.punches = punches;
    }

    public Date getShiftDate() {
        return shiftDate;
    }

    public void setShiftDate(Date shiftDate) {
        this.shiftDate = shiftDate;
    }


    public Date getClockIn() {
        return clockIn;
    }

    public Date getClockOut() {
        return clockOut;
    }

    public void getTotalDuration() {


    }

    public void addPreBuffer(){

        int random = new Random().nextInt(60);

        Date prebuffer1 = helper.addEndTime(startBufferTime, -random);
        Date prebuffer2 = helper.addEndTime(prebuffer1, -random);

        AttendancePunch first = new AttendancePunch(prebuffer1,OUT);
        AttendancePunch second = new AttendancePunch(prebuffer2,IN);

        if ( punches == null) {
            punches = new ArrayList<>();
        }

        punches.add(first);
        punches.add(second);

    }


    public void addPostBuffer(){

        int random = new Random().nextInt(60);

        Date postbuffer1 = helper.addEndTime(endBufferTime, random);
        Date postbuffer2 = helper.addEndTime(postbuffer1, random);

        AttendancePunch first = new AttendancePunch(postbuffer1,IN);
        AttendancePunch second = new AttendancePunch(postbuffer2,OUT);

        if ( punches == null) {
            punches = new ArrayList<>();
        }

        punches.add(first);
        punches.add(second);

    }


    public boolean isOptionalHoliday() {
        return isOptionalHoliday;
    }

    public void setOptionalHoliday(boolean optionalHoliday) {
        isOptionalHoliday = optionalHoliday;
    }

    public boolean isEarlyMark() {
        return isEarlyMark;
    }

    public void setEarlyMark(boolean earlyMark) {
        isEarlyMark = earlyMark;
    }

    public boolean isLateMark() {
        return isLateMark;
    }

    public void setLateMark(boolean lateMark) {
        isLateMark = lateMark;
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

        String hrs = String.format("%02d", (totalWorkDuration  / 3600 ));
        long pending = totalWorkDuration % 3600;
        String mins = String.format("%02d", ( pending / 60 ));
        String secs = String.format("%02d", ( pending % 60 ));

        return  hrs + ":" + mins + ":" + secs;

    }

    public String getFinalWorkDuration() {

        String hrs = String.format("%02d", (finalWorkDuration  / 3600 ));
        long pending = finalWorkDuration % 3600;
        String mins = String.format("%02d", ( pending / 60 ));
        String secs = String.format("%02d", ( pending % 60 ));

        return  hrs + ":" + mins + ":" + secs;

    }

    public String getBreakDuration() {

        String hrs = String.format("%02d", (breakDuration / 3600 ));
        long pending = breakDuration % 3600;
        String mins = String.format("%02d", ( pending / 60 ));
        String secs = String.format("%02d", ( pending % 60 ));

        return  hrs + ":" + mins + ":" + secs;

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

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public AttendancePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(AttendancePolicy policy) {
        this.policy = policy;
    }

    public void calculateDurations() {

        calculateWorkDuration();

        if ( totalWorkDuration == 0 )
            return;

        calculateFinalAndBreakDuration();
    }

    private void calculateFinalAndBreakDuration() {

        int count = punches.size();
        boolean calculate = true;

        for (int i = 0 ; i<count-1;i++ ) {

            AttendancePunch first = punches.get(i);
            AttendancePunch second = punches.get(i + 1);

            long duration = helper.getDifference(second.getTimestamp(), first.getTimestamp());

            if (isEmcure || isDirectionality) {

                if ( first.getStatus()  == 0 && second.getStatus() == 1 ) {
                    finalWorkDuration += duration;
                }

                if ( first.getStatus()  == 1 && second.getStatus() == 0 && isEmcure ) {
                    breakDuration += duration;
                }

            } else {

                if (calculate) {
                    finalWorkDuration += duration;
                    calculate = false;
                }

                else
                    calculate = true;
            }
        }

        if (!isEmcure) {
            breakDuration = totalWorkDuration - finalWorkDuration;
        }

    }

    private void calculateWorkDuration() {
        try {
            totalWorkDuration = helper.getDifference(clockOut,clockIn);
        } catch (Exception e){

        }
    }

    public void setDirectionality(boolean isDirectionality) {
        this.isDirectionality= isDirectionality;
    }

    public void setEmcure(boolean isEmcure){
        this.isEmcure = isEmcure;
    }

    public void addPunch(AttendancePunch punch) {

        if ( punches == null)
            punches = new ArrayList<>();

        punches.add(punch);
    }

    public String getBiometricQuery(String employeeId) {

        String machine_id = "DBOX16";

        String query = "INSERT INTO `biometric`.`emp_att` (`machine_id`, `employee_id`, `timestamp`, `status`) VALUES ";

        String val_query = "";

        for ( AttendancePunch punch : punches) {

            val_query += ", (\""+machine_id+"\",\""+ employeeId+"\",\"" + punch.getTimestamp()+"\","+
                    ((true || false )? punch.getStatus() : 99 )+" )";
        }

        query = query + val_query.substring(1);

        return query;
    }
}
