package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.JobPortals;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobPortalService extends Services {


    public void createJobPortal(JobPortals jobPortals){

        String url = getData("@@url") + "/settings/recruitment/portal";

        Map<String, String> body = new HashMap<>();
        body.putAll(jobPortals.toMap());

        doPost(url, null,mapToFormData(body));
    }

    public JSONArray getAllJobPortals() {

        String url = getData("@@url") + "/settings/getRecruitmentPortal";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        return objResponse.getJSONArray("aaData");
    }

    public JobPortals getJobPortalByName(String name){

        JobPortals jobPortals = new JobPortals();
        JSONArray arr = getAllJobPortals();

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;

            jobPortals.setPortalName(objarr.getString(0));
            jobPortals.setId(objarr.getString(1));

            if(name.equalsIgnoreCase(jobPortals.getPortalName())){

                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(jobPortals.getId());
                if (m.find()){
                    jobPortals.setId(StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
                }
                break;
            }
        }
        return jobPortals;
    }

    public void editJobPortal(JobPortals jobPortals){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditRecruitmentPortal";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("RecruitmentPortal[id]",jobPortals.getId());
        body.putAll(jobPortals.toMap());
        doPost(url, headers, mapToFormData(body));
    }
    public void deleteJobPortal(JobPortals jobPortals){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditRecruitmentPortal";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("resource",jobPortals.getId());
        body.put("mode","delete");

        doPost(url, headers, mapToFormData(body));
    }
}
