package com.darwinbox.attendance.objects;

import java.util.Map;

public class WeeklyOff {

    private String weeklyOffName;
    private String weeklyOffDescription;
    private Map<String, String> nonWorkingDays;

    public String getWeeklyOffName() {
        return weeklyOffName;
    }

    public void setWeeklyOffName(String weeklyOffName) {
        this.weeklyOffName = weeklyOffName;
    }

    public String getWeeklyOffDescription() {
        return weeklyOffDescription;
    }

    public void setWeeklyOffDescription(String weeklyOffDescription) {
        this.weeklyOffDescription = weeklyOffDescription;
    }

    public Map<String, String> getWeekOffMapObject() {
        return nonWorkingDays;
    }

    public Map<String, String> getNonWorkingDays() {
        return nonWorkingDays;
    }


    public void setNonWorkingDays(Map<String, String> nonWorkingDays) {
        this.nonWorkingDays = nonWorkingDays;
    }
}
