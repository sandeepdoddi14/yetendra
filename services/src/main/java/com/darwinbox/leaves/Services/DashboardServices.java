package com.darwinbox.leaves.Services;

import com.darwinbox.core.Services;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardServices  extends Services {

    public String doLogin(String userName, String password) {

        String url = data.get("@@url") + "/user/login";
        String phpSessionID = "";

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("UserLogin[username]", userName));
        body.add(new BasicNameValuePair("UserLogin[password]", password));

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(url);
            request.setEntity(new UrlEncodedFormEntity(body));
            log.debug("POST Call: " + url);
            log.debug("Request body: " + body);
            HttpResponse result = httpClient.execute(request);
            //phpSessionID = "";
            phpSessionID =result.getHeaders("Set-Cookie")[1].getValue();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("EXCEP: login call failed");
        }
        return phpSessionID;
    }

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

    /**
     * @param userId : which user requests it should return, all or null for all employee requests
     * @param reqStatus : Pending/Processed/archived. Use RequestType enum
     * @param reqType : Attendance/leave.. etc., requests. Use RequestType enum
     * @param action : Approved/rejected/revoked. Use RequestType enum ********** NOT REQUIRED for PENDING REQUESTS
     * @return ids of returned requests
     */
    public List<String> getRequestIds(String userId, RequestType reqStatus, RequestType reqType, RequestType action) {

        String url = data.get("@@url") + "/dashboard/filter";
        Map<String, String> headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = "";
        List<String> requestIds = new ArrayList<>();

        if(userId == null || userId.equalsIgnoreCase("all")) {
            userId = "";
        }

        List<NameValuePair> body = new ArrayList<>();
        body.add(new BasicNameValuePair("whose", userId));
        body.add(new BasicNameValuePair("which", reqType.value));
        body.add(new BasicNameValuePair("type", reqStatus.value));
        if(!reqStatus.value.equals(RequestType.PENDING.value)) {
            if (action.value == null || action.value.isEmpty()) {
                body.add(new BasicNameValuePair("action", RequestType.ALL_ACTIONS.value));
            }
            body.add(new BasicNameValuePair("action", action.value));
        }
        try {
            response = doPost(url, headers, body);
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

    //Supported action inputs: accept, reject, revoke
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

