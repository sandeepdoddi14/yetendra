package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.objects.overTime.OverTimePolicy;
import com.darwinbox.Services;

import java.util.HashMap;
import java.util.Map;

public class OverTimePolicyService extends Services {

    public void createSettings(OverTimePolicy overTimePolicy) {

        String url = getData("@@url") + "/compoff/compoff";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.putAll(overTimePolicy.toMap());

    String response = doPost(url, headers,mapToFormData(body));
    Reporter("Response while creating OT Policy is-"+response,"INFO");

    }


    public String getOTPolicyId(String OTpolicyname){

        String policy_id = null;
        String url = getData("@@url") + "/compoff/editcompoff";
        return  null;

    }

    }
