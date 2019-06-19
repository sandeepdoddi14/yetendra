package com.darwinbox.attendance.objects;

import java.util.Date;

public class AttendancePunch {

    private Date timestamp;
    private int status = 99;
    private Date createdDate;

    public AttendancePunch(Date timestamp, int status) {
        setTimestamp(timestamp);
        setStatus(status);
        setCreatedDate(new Date());
    }

    public AttendancePunch(Date timestamp, Status status) {
        setTimestamp(timestamp);
        setStatus(status);
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.ordinal();
        if (this.status == 2)
            setStatus(99);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean compareTo(AttendancePunch punch) {
               return (this.getTimestamp().compareTo(punch.getTimestamp()) == 0);
    }

    protected enum Status {IN, OUT, NONE}

}
