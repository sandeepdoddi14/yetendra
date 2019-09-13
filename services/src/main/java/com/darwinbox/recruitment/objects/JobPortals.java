package com.darwinbox.recruitment.objects;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobPortals {

    private String portalName;

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public void toObject(Map<String, String> body) {

             setPortalName("");
    }

    public Map<String, String> toMap() {

        Map<String, String> body = new HashMap<>();

        body.put("yt0","SAVE");
        body.put("RecruitmentPortal[portal_name]",getPortalName());
        return body;
    }
    }
