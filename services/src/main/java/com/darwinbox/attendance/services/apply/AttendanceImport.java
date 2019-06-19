package com.darwinbox.attendance.services.apply;

import com.darwinbox.attendance.services.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.*;

public class AttendanceImport extends Services {

    public void importBackdated(Map<String,String> body ) {

        String url = data.get("@@url") + "/attendance/attendance/processback";

        List<NameValuePair> list = new ArrayList<>();

        body.put("Import", "Done mapping, Next");
        body.put("date_format", "dd-MM-yyyy");
        body.put("time_format", "H:m:s");
        body.put("step", "1");

        list.add(new BasicNameValuePair("AttendanceHeader[]", "email"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "shift_date"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "intime_date"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "intime"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "outtime_date"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "outtime"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "shift_name"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "policy_name"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "weekly_off_name"));
        list.add(new BasicNameValuePair("AttendanceHeader[]", "break_duration"));

        list.addAll(mapToFormData(body));

        doPost(url, null,list);

    }

    public boolean assignPolicyAndShift(String userID, String shiftID, String policyID, String weeklyOffID,String date) {
        
        String url = getData("@@url") + "/employee/openShiftModalnew";

        Map<String,String> data = new HashMap<>();
        
        data.put("action", "assign_shift");
        data.put("UserShiftsBulk[user_id][]", userID);
        data.put("UserShiftForm[shift_id]", shiftID);
        data.put("UserShiftForm[policy_id]", policyID);
        data.put("UserWeeklyOffForm[weekly_off_id]", weeklyOffID);
        data.put("UserShiftsBulk[effective_date]", date);

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doPost(url, headers, mapToFormData(data));
        log.info("Response: " + response);
        JSONObject objResponse = new JSONObject(response);

        if (objResponse != null && objResponse.getString("status").equals("success")) {
            return true;
        } else
            throw  new RuntimeException("Assign Shift and Policy Failed ");

    }

    


}
