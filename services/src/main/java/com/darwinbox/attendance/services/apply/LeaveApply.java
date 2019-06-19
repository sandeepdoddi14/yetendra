package com.darwinbox.attendance.services.apply;

import com.darwinbox.attendance.services.Services;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LeaveApply extends Services {

    public void applyLeave(Map<String,String> headers, Map<String,String> body ) {

        String url = getData("@@url") + "/leaves/leaves/apply";
        body.putAll(getDefaultforLeaveApply());
        doPost(url, headers, mapToFormData(body));

    }

    public Map<String,String> getDefaultforLeaveApply(){

        Map<String,String> body = new HashMap<>();

        body.put("message", "Created via Automation");
        body.put("name","");
        body.put("Messages_type","leave");
        body.put("source","dashboard");
        body.put("selected_user_search","");
        body.put("iOS","1");
        body.put("form_id","");

        return body;
    }


}
