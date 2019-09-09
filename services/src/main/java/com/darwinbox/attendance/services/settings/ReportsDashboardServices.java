package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.objects.Reports;
import com.darwinbox.attendance.services.Services;
import org.json.JSONObject;

import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReportsDashboardServices extends Services {

       public void getReportLateMarkSummary(Reports reports){

           String url = getData("@@url") + "/reports/ShowRoster";
           Map headers = new HashMap();
           headers.put("X-Requested-With", "XMLHttpRequest");

           String response=  doPost(url, headers, reports.todoMap("month"));
           log.info("Response: " + response);

           try {
               JSONObject obj = new JSONObject(response);
               Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(6),"INFO");
               Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(6),"INFO");

               Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(7),"INFO");
               Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(7),"INFO");

               }catch (Exception e){
               Reporter(""+e,"ERROR");
           }

       }

    public void getReportEarlyOutSummary(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response=  doPost(url, headers, reports.todoMap("month"));
        log.info("Response: " + response);

        try {
            JSONObject obj = new JSONObject(response);
            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(5),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(5),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(1).toList().get(5),"INFO");

            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(6),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(6),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(1).toList().get(6),"INFO");

        }catch (Exception e){
            Reporter(""+e,"ERROR");
        }

    }

    public void getReportEarlyOutDateWise(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response=  doPost(url, headers, reports.todoMap("month"));
        log.info("Response: " + response);

        try {
            JSONObject obj = new JSONObject(response);
            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(1),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(1),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(1).toList().get(1),"INFO");

            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(8),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(8),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(1).toList().get(8),"INFO");

        }catch (Exception e){
            Reporter(""+e,"ERROR");
        }

    }



    public void getReportLateMarkDateWise(Reports reports) {

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doPost(url, headers, reports.todoMap("month"));
        log.info("Response: " + response);

        try {
            JSONObject obj = new JSONObject(response);
            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(0),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(0),"INFO");

            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(8),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(8),"INFO");

        }catch (Exception e){
            Reporter(""+e,"ERROR");
        }



    }
        public Object getReportAbsenteesDateWise(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
            Object a = new Object();
        String response=  doPost(url, headers, mapToFormData(reports.toMap("month")));
        waitForUpdate(3);
        log.info("Response: " + response);

        try {
            for(int i=1;i<=response.length();i++) {
                JSONObject obj = new JSONObject(response);
                Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(0), "INFO");
                Reporter("Report value : " + obj.getJSONArray("update").getJSONArray((i-1)).toList().get(0), "INFO");
               a=  obj.getJSONArray("update").getJSONArray((i-1)).toList().get(0);

            }
        }catch (Exception e){
        }
     return a;
    }

    public Object getReportAttendanceSummary(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        Object a = new Object();

        String response=  doPost(url, headers, mapToFormData(reports.toMap("month")));
        log.info("Response: " + response);

        try {
            JSONObject obj = new JSONObject(response);
            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(4),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(4),"INFO");
            a=obj.getJSONArray("update").getJSONArray(0).toList().get(4);
        }catch (Exception e){
            Reporter(""+e,"ERROR");
        }
        return a;
    }
    public Object getReportLocationSummary(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response=  doPost(url, headers, mapToFormData(reports.toMap("month")));
        log.info("Response: " + response);

        Object[] a = null;
        Object b=a;

        JSONObject obj = new JSONObject(response);
            for(int i=4;i<=6;i++) {
                Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(i), "INFO");
                Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(i), "INFO");

            }
            for(int i=6;i<=8;i++) {
                b = obj.getJSONArray("update").getJSONArray(0).toList().get(i);
                return b;
            }
            return a;
    }


    public long diffBetweenDates(Date firstDate, Date secondDate)
    {
        return ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());
    }

    public Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH,1);
        return cal.getTime();
    }

    public Object getReportAttendanceAssignments(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response=  doPost(url, headers, mapToFormData(reports.toMap("custom")));
        log.info("Response: " + response);
         Object[] a = null;
         Object b=a;


            JSONObject obj = new JSONObject(response);
            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(6),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(6),"INFO");

            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(7),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(7),"INFO");

            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(8),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(8),"INFO");

           for(int i=6;i<=8;i++) {
               b = obj.getJSONArray("update").getJSONArray(0).toList().get(i).toString();
               return b;
           }
           return a;
    }

    public void getReportCompleteAttendanceSummary(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response=  doPost(url, headers, mapToFormData(reports.toMap("custom")));
        log.info("Response: " + response);

        try {
            JSONObject obj = new JSONObject(response);
            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(9),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(9),"INFO");

            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(11),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(11),"INFO");

        }catch (Exception e){
            Reporter(""+e,"ERROR");
        }

    }



    public void getReportCompleteAttendanceSummaryList(Reports reports){

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response=  doPost(url, headers, reports.todoMap("custom"));
        log.info("Response: " + response);

        try {
            JSONObject obj = new JSONObject(response);
            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(9),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(9),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(1).toList().get(9),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(2).toList().get(9),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(3).toList().get(9),"INFO");

            Reporter("Coloumn is : "+obj.getJSONArray("cols").getString(11),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(0).toList().get(11),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(1).toList().get(11),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(2).toList().get(11),"INFO");
            Reporter("Report value : "+obj.getJSONArray("update").getJSONArray(3).toList().get(11),"INFO");

        }catch (Exception e){
            Reporter(""+e,"ERROR");
        }

    }

    public void getReportDailyAttendanceRoster(Reports reports) {

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doPost(url, headers,  mapToFormData(reports.toMap("month")));
        log.info("Response: " + response);
        JSONObject obj = new JSONObject(response);
        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(14), "INFO");

        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(14), "INFO");

         for(int i=0;i<=25;i++){
                Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(i).toList().get(14), "INFO");
        }

 for(int i=0;i<=25;i++){
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(i).toList().get(14), "INFO");
}
    }

    public void getReportDatewiseAttendanceStatus(Reports reports) {

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doPost(url, headers, mapToFormData(reports.toMap("month")));
        log.info("Response: " + response);

        JSONObject obj = new JSONObject(response.replaceAll("\\<.*?>", ""));

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(32), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(32), "INFO");

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(34), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(34), "INFO");

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(35), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(35), "INFO");

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(36), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(36), "INFO");

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(37), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(37), "INFO");

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(38), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(38), "INFO");
    }

    public void getReportWorkDurationSummary(Reports reports) {

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doPost(url, headers, mapToFormData(reports.toMap("month")));
        log.info("Response: " + response);
        JSONObject obj = new JSONObject(response);

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(4), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(4), "INFO");

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(5), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(5), "INFO");

    }

    public Object getReportShiftsRoster(Reports reports) {

        String url = getData("@@url") + "/reports/ShowRoster";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        Object a = new Object();

        String response = doPost(url, headers, mapToFormData(reports.toMap("month")));
        log.info("Response: " + response);
        JSONObject obj = new JSONObject(response);

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(5), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(5), "INFO");

        Reporter("Coloumn is : " + obj.getJSONArray("cols").getString(13), "INFO");
        Reporter("Report value : " + obj.getJSONArray("update").getJSONArray(0).toList().get(13), "INFO");
        a=obj.getJSONArray("update").getJSONArray(0).toList().get(13);
        return a;
    }
    }


