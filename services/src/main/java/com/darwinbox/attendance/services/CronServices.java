package com.darwinbox.attendance.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;

import java.net.URLEncoder;
import java.util.Date;

public class CronServices extends Services {

    public void changeDate(Date date){
        DateTimeHelper helper = new DateTimeHelper();
        String params = helper.formatDateTo(date,"yyyy-MM-dd HH:mm:ss" );
        String url = data.get("@@url")+ "/emailtemplate/Setserverdate?set_date=" + encodeUrl("\""+params+"\"");
        doGet(url, null);
    }

    private void runCron(String params){
        String url = data.get("@@url")+ "/emailtemplate/Runcron?details="+ encodeUrl("\""+params+"\"");
        doGet(url, null);
    }

    public void runClockInReminder(){
        String cron_name = "ClockinWarning";
        runCron(cron_name);
    }

    public void runUpdatePolicyContinuous() {
        String cron_name = "UpdatePolicyContinious";
        runCron(cron_name);
    }

    public void runBiometricCron() {
        String cron_name = "AttendanceDesktopToSqlToMongoNew";
        runCron(cron_name);
    }
    public void runCompoffPolicyCron() {
        String cron_name = "compoffdaily";
        runCron(cron_name);
    }
}