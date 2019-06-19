package com.darwinbox.attendance.services.apply;

import com.darwinbox.attendance.objects.Attendance;
import com.darwinbox.attendance.objects.Employee;
import com.darwinbox.attendance.objects.policy.AttendancePolicy;
import com.darwinbox.attendance.services.Services;
import com.darwinbox.framework.uiautomation.Utility.DateTimeHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttendanceRequestServices extends Services {

    private DateTimeHelper dateTimeHelper = new DateTimeHelper();

    public String applyClockIn( Map<String, String> headers, int time, int location,String date) {

        String url = getData("@@url") + "/attendance";

        Map<String,String> body = new HashMap<>();

        body.put("AttendanceRequestForm[request_type]","1");
        body.put("AttendanceRequestForm[location]",location+"");
        body.put("AttendanceRequestForm[clock_in_hrs]",(time/60+""));
        body.put("AttendanceRequestForm[clock_in_min]",(time%60+""));
        body.put("AttendanceRequestForm[message]","Created by Automation");
        body.put("AttendanceRequestForm[clock_in_date]",date);

        String response = doPost(url, headers, mapToFormData(body));

        return response;
    }


    public String applyAttendanceUpdate(String fromDate, int inTime,String toDate, int outTime) {

        String url = getData("@@url") + "/attendance";
        Map<String, String> headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String,String> body = new HashMap<>();

//        body.put("AttendanceRequestForm[request_type]","1");
//        body.put("AttendanceRequestForm[clock_in_date]",timestamp);
//        body.put("AttendanceRequestForm[location]","1");
//        body.put("AttendanceRequestForm[clock_in_hrs]",(time/60+""));
//        body.put("AttendanceRequestForm[clock_in_min]",(time%60+""));
//        body.put("AttendanceRequestForm[message]","Created by Automation");

        String response = doPost(url, headers, mapToFormData(body));

        return response;
    }

    public void deleteAttendance(String attendanceID) {
        String url = getData("@@url") + "/attendance/attendance/delete/id/" + attendanceID;
        String response = "";
        try {
            response = doGet(url, null);
        } catch (Exception e) {
            log.debug("Response : " + response);
            e.printStackTrace();
        }
    }

    public JSONArray getAttendanceRegulariseReasons() {
        String url = getData("@@url") + "/attendance/AttendanceRegulariseReasons/GetReasons";
        Map<String, String> headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = "";
        JSONArray arr = new JSONArray();
        try {
            response = doGet(url, headers);
            JSONObject objResponse = new JSONObject(response);
            for (Object t : objResponse.getJSONArray("aaData")) {
                Map<String, String> map = new HashMap();
                JSONArray temp = (JSONArray) t;
                map.put("name", temp.getString(0));
                map.put("limit", temp.getString(1));
                Pattern p = Pattern.compile("id=\"([^\\\"]*)\"");
                Matcher m = p.matcher(temp.getString(2));
                if (m.find()) {
                    map.put("id", m.group(1));
                } else {
                    map.put("id", "");
                }
                arr.put(map);
            }
        } catch (Exception e) {
            log.debug("Response : " + response);
            e.printStackTrace();
        }
        return arr;
    }

    public void raiseAttendanceAdjustment(Employee employee, AttendancePolicy policy, Attendance attendanceData) {
        String url = getData("@@url") + "/request/attendance";
        Map<String, String> headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Cookie", employee.getPhpSessid());

        String editReasonID = "";
        JSONArray reasons = getAttendanceRegulariseReasons();
        for (int i = 0; i < reasons.length(); i++) {
            JSONObject obj = reasons.getJSONObject(i);
            if (obj.getString("limit").equalsIgnoreCase("no limit")) {
                editReasonID = obj.getString("id");
                break;
            }
        }
        if (editReasonID.isEmpty()) {
            throw new RuntimeException("ERROR: No AttendanceRegulariseReasons found with No Limit");
        }

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("AttendanceRequestForm[user_id]", employee.getUserID()));
        //body.add(new BasicNameValuePair("AttendanceRequestForm[request_type]", AttendanceRequestType.UPDATE_ATTENDANCE.value));
        body.add(new BasicNameValuePair("AttendanceRequestForm[edit_reason_id]", editReasonID));
        body.add(new BasicNameValuePair("AttendanceRequestForm[clock_in_date]", attendanceData.getShiftDate().toString()));
        body.add(new BasicNameValuePair("AttendanceRequestForm[clock_in_date_to]", attendanceData.isOverNight() ? "" : attendanceData.getShiftDate().toString()));
        //body.add(new BasicNameValuePair("AttendanceRequestForm[location]", AttendanceRequestLocation.OFFICE.value));
        body.add(new BasicNameValuePair("AttendanceRequestForm[clock_in_hrs]", attendanceData.getTimeIn().split(":")[0]));
        body.add(new BasicNameValuePair("AttendanceRequestForm[clock_in_min]", attendanceData.getTimeIn().split(":")[1]));
        body.add(new BasicNameValuePair("AttendanceRequestForm[clock_out_hrs]", attendanceData.getTimeOut().split(":")[0]));
        body.add(new BasicNameValuePair("AttendanceRequestForm[clock_out_min]", attendanceData.getTimeOut().split(":")[1]));
        if (attendanceData.getBreakDuration() != null) {
            body.add(new BasicNameValuePair("AttendanceRequestForm[break_hrs]", attendanceData.getBreakDuration().split(":")[0]));
            body.add(new BasicNameValuePair("AttendanceRequestForm[break_min]", attendanceData.getBreakDuration().split(":")[1]));
        }
        body.add(new BasicNameValuePair("AttendanceRequestForm[is_next_day]", attendanceData.isOverNight() ? "1" : "0"));
        body.add(new BasicNameValuePair("AttendanceRequestForm[message]", RandomStringUtils.randomAlphabetic(10) + String.valueOf(new Date().getTime())));

        String response = "";
        try {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60 * 1000).setSocketTimeout(3 * 60 * 1000).build();
            response = customPost(requestConfig, url, headers, body);
            JSONObject responseObj = new JSONObject(response);
            if (responseObj.getString("status").equals("success")) {
                log.info("Success: " + responseObj.getString("update"));
            } else {
                log.error("Status in Response: " + responseObj);
                throw new RuntimeException("ERROR: Unable to raise Attendance request");
            }
        } catch (Exception e) {
            log.error("Error: Unable to parse response: " + e.getMessage());
            log.debug("Response : " + response);
            e.printStackTrace();
        }
    }

    //@wip
    public boolean changeAttendanceFromOneView(String formID, String userMangoID, String date, boolean isNightShift, Integer... timings) {

        String url = getData("@@url") + "/employee/changeAttendanceLogs";
        Map<String, String> headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("UserMongoView[mongo_id]", formID));
        body.add(new BasicNameValuePair("UserMongoView[tenant_id]", userMangoID));
        body.add(new BasicNameValuePair("dailyAttendance[shift_date]", date));
        body.add(new BasicNameValuePair("ynRadio", "attButton"));
        body.add(new BasicNameValuePair("UserAttendanceLogForm[punchin_min]", String.valueOf(timings[1])));

        body.add(new BasicNameValuePair("UserAttendanceLogForm[punchout_date]", date));
        body.add(new BasicNameValuePair("UserAttendanceLogForm[punchout_hrs]", String.valueOf(timings[2])));
        body.add(new BasicNameValuePair("UserAttendanceLogForm[punchout_min]", String.valueOf(timings[3])));

        String response = doPost(url, headers, body);
        try {
            JSONObject newAttendenceLog = new JSONObject(response);
            if (newAttendenceLog.getString("status").equals("success")) {
                return true;
            } else {
                log.error("Status in Response: " + newAttendenceLog);
                return false;
            }
        } catch (Exception e) {
            log.error("Error: Unable to parse response: " + e.getMessage());
            log.debug("Response : " + response);
            e.printStackTrace();
            return false;
        }
    }



    public JSONArray getCompleteAttendanceLog(String userMongoId, String attendanceForMonth) {

        JSONArray objResponse = new JSONArray();
        boolean isLogGenerated = false;
        int i = 0;
        while (!isLogGenerated && i < 3) {
            try {
                objResponse = null;//getAttendanceLog(userMongoId, attendanceForMonth);
                isLogGenerated = true;
            } catch (Exception e) {
                log.warn("Trying to generate attendance log again.....");
            }
            i++;
        }
        if (!isLogGenerated) {
            throw new RuntimeException("ERR: Failed to get attendance log after multiple retries");
        }
        return objResponse;
    }



    public String getFormIDForDate(String date, JSONArray attendanceLog) {
        String dt = date.split("-")[0];
        JSONArray dayInfo = attendanceLog.getJSONArray(Integer.parseInt(dt) - 1);
        String dataWithFormID = dayInfo.getString(11);
        String formID = "";
        if (dataWithFormID != null && !dataWithFormID.isEmpty()) {
            Pattern p = Pattern.compile("id=\"([^\\\"]*)\"");
            Matcher m = p.matcher(dataWithFormID);
            while (m.find()) {
                formID = m.group(1);
            }
            log.info("INFO: Form ID for timestamp: " + date + "  -> " + formID);
            if (formID.isEmpty()) {
                throw new RuntimeException("ERROR: Failed to fetch Form ID for timestamp : " + date);
            }
        } else {
            throw new RuntimeException("ERROR: Unable to fetch Form ID. May be Attendance log is not generated for the day: " + date);
        }
        return formID;
    }


    public Map<String,String> getDefaultImportBody() {

        Map<String,String> body = new HashMap<>();
        body.put("AttendanceHeader[]","email");
        body.put("AttendanceHeader[]","shift_date");
        body.put("AttendanceHeader[]"," ");
        body.put("AttendanceHeader[]"," ");
        body.put("AttendanceHeader[]"," ");
        body.put("AttendanceHeader[]"," ");
        body.put("AttendanceHeader[]"," ");
        body.put("AttendanceHeader[]","shift_name");
        body.put("AttendanceHeader[]","policy_name");
        body.put("AttendanceHeader[]","weekly_off_name");
        body.put("AttendanceHeader[]"," ");

        return body;
    }
}

