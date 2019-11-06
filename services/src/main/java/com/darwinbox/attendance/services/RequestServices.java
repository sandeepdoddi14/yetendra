package com.darwinbox.attendance.services;

import com.darwinbox.Services;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestServices extends Services {


    public enum RequestType {

        PENDING("1"),
        PROCESSED("2"),
        ARCHIVED("3"),

        ALL_REQUESTS("all"),
        ATTENDANCE_REQUESTS("attendance"),
        LEAVE_REQUESTS("leave"),

        ALL_ACTIONS("all"),
        APPROVED("approve"),
        REJECTED("reject"),
        REVOKED("revoke");

        public final String value;

        RequestType(String value) {
            this.value = value;
        }
    }

    public enum Act {

        APPROVE("accept"),
        REJECT("reject"),
        REVOKE("revoke");

        public final String value;

        Act(String value) {
            this.value = value;
        }
    }

    public List<String> getRequestIds(String userId, RequestType status, RequestType type, RequestType action) {

        String url = data.get("@@url") + "/dashboard/filter";

        Map<String, String> headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<String> requestIds = new ArrayList<>();

        if(userId == null || userId.equalsIgnoreCase("all")) {
            userId = "";
        }

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("whose", userId));
        body.add(new BasicNameValuePair("which", type.value));
        body.add(new BasicNameValuePair("type", status.value));
        if(!status.value.equals(RequestType.PENDING.value)) {
            if (action.value == null || action.value.isEmpty()) {
                body.add(new BasicNameValuePair("action", RequestType.ALL_ACTIONS.value));
            }
            body.add(new BasicNameValuePair("action", action.value));
        }
        try {
           String response = doPost(url, headers, body);
            JSONObject objResponse = new JSONObject(response);

            if (!objResponse.getString("status").equalsIgnoreCase("success")) {
                log.error("Status in Response: " + objResponse);
                throw new RuntimeException("ERROR: Error occured while getting requests from dashboard");
            }
            Pattern p = Pattern.compile("request_thread_[a-z0-9]+");
            Matcher m = p.matcher(objResponse.getString("update"));

            while (m.find()) {
                requestIds.add(m.group(0).replace("request_thread_", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestIds;
    }

    public void processRequest(String reqId, Act action) {

        String url = data.get("@@url") + "/request/process";

        Map<String, String> headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("action", action.value));
        body.add(new BasicNameValuePair("id", reqId));

        try {
            String response = doPost(url, headers, body);
            JSONObject objResponse = new JSONObject(response);

            if (objResponse.getString("status").equals("success")) {
                log.info("Success: " + objResponse.getString("update"));
            } else {
                log.error("Status in Response: " + objResponse.toString());
                throw new RuntimeException("ERROR: Error occured while process the requests");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR: Error occured while process the requests");
        }
    }

}
