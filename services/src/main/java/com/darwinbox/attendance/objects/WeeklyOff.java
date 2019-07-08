package com.darwinbox.attendance.objects;

import java.util.HashMap;
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

    public Map<String,String> getDefaultBody() {

        Map<String, String> body = new HashMap<>();

        body.put("WeeklyOffForm[weekly_off_name]", "");
        body.put("WeeklyOffForm[description]", "Weekly Off Generated through Automation");
        body.put("WeeklyOffForm[non_working_days][day][6]", "0");
        body.put("WeeklyOffForm[non_working_days][frequency][6]", "0");
        body.put("WeeklyOffForm[non_working_days][day][0]", "0");
        body.put("WeeklyOffForm[non_working_days][frequency][0]", "0");
        body.put("WeeklyOffForm[non_working_days][day][1]", "0");
        body.put("WeeklyOffForm[non_working_days][frequency][1]", "0");
        body.put("WeeklyOffForm[non_working_days][day][2]", "0");
        body.put("WeeklyOffForm[non_working_days][frequency][2]", "0");
        body.put("WeeklyOffForm[non_working_days][day][3]", "0");
        body.put("WeeklyOffForm[non_working_days][frequency][3]", "0");
        body.put("WeeklyOffForm[non_working_days][day][4]", "0");
        body.put("WeeklyOffForm[non_working_days][frequency][4]", "0");
        body.put("WeeklyOffForm[non_working_days][day][5]", "0");
        body.put("WeeklyOffForm[non_working_days][frequency][5]", "0");
        body.put("AttendancePolicyForm[change_date]", "0");

        return body;
    }

}
