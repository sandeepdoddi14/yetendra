package com.darwinbox.recruitment.objects;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobPortals {

    private String portalName;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public void toObject(Map<String, String> body) {

             setPortalName(body.get("portalName"));
    }

    public Map<String, String> toMap() {

        Map<String, String> body = new HashMap<>();

        body.put("yt0","SAVE");
        body.put("RecruitmentPortal[portal_name]",getPortalName());
        return body;
    }
    }
