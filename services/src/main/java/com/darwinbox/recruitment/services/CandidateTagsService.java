package com.darwinbox.recruitment.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.recruitment.objects.CandidateTags;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CandidateTagsService extends Services {

    public void createCandidateTags(CandidateTags candidateTags){

        String url = getData("@@url") + "/settings/recruitment/decisioning";

        Map<String, String> body = new HashMap<>();
        body.putAll(candidateTags.toMap());

        doPost(url, null,mapToFormData(body));
    }

    public JSONArray getAllCandidateTags(){

        Map<String, String> data = new HashMap<>();

        String url = getData("@@url") + "/settings/GetCandidateDesisioningList";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        return objResponse.getJSONArray("aaData");
    }

    public String getCandidateTagIDByName(String name){

        String url = getData("@@url") + "/settings/GetCandidateDesisioningList";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        JSONArray arr = objResponse.getJSONArray("aaData");
        String id = null;

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;
            String reason = objarr.getString(0);
            String type = objarr.getString(1);

            id = objarr.getString(2);

           if(name.equalsIgnoreCase(reason)){

               Pattern p = Pattern.compile("id=\"\\w+\"");
               Matcher m = p.matcher(id);

               if (m.find()){
                   id = StringUtils.substringsBetween(m.group(0), "\"", "\"")[0];
               }
               break;
           }
        }
        return id;

    }

    public void editCandidateTag(CandidateTags candidateTags){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditCandidateDecisioning";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("RecruitmentCandidateDescision[id]", getCandidateTagIDByName(candidateTags.getCandidateDecisionReason()));
        body.put("old_value",candidateTags.getCandidateDecisionReason());

        if(candidateTags.getCandidateDecisionType().contentEquals("5"))
             body.put("tag_decisioning","change");
        else
            body.put("tag_decisioning","none");

        body.putAll(candidateTags.toMap());
        doPost(url, headers, mapToFormData(body));

    }

    public void deleteCandidateTag(String ID){

        CandidateTags candidateTags = new CandidateTags();
        String url = getData("@@url") + "/settings/EditCandidateDecisioning";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();

        if(candidateTags.getCandidateDecisionType().contentEquals("5")){
            url = getData("@@url") + "/settings/editCandidateDecisioning";
            body.put("old_value","");
            body.put("resource",getCandidateTagIDByName(ID));
            body.put("action","delete");
            body.put("tag_decisioning",""); //none or delete
        }else{
            body.put("resource",getCandidateTagIDByName(ID));
            body.put("action","delete");
        }

        doPost(url, headers, mapToFormData(body));
    }


}
