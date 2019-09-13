package com.darwinbox.recruitment.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.recruitment.objects.CandidateTags;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
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

    public CandidateTags getCandidateTagByName(String name){

        CandidateTags candidateTags = new CandidateTags();
        String url = getData("@@url") + "/settings/GetCandidateDesisioningList";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        JSONArray arr = objResponse.getJSONArray("aaData");

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;
            candidateTags.setCandidateDecisionReason(objarr.getString(0));
            //candidateTags.setCandidateDecisionType(CandidateTags.candidateDecisionType.valueOf(objarr.getString(1)));
            candidateTags.setID(objarr.getString(2));

           if(name.equalsIgnoreCase(candidateTags.getCandidateDecisionReason())){

               Pattern p = Pattern.compile("id=\"\\w+\"");
               Matcher m = p.matcher(candidateTags.getID());

               if (m.find()){
                   candidateTags.setID(StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
               }
               break;
           }
        }
        return candidateTags;

    }

    public void editCandidateTag(CandidateTags candidateTags){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditCandidateDecisioning";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("RecruitmentCandidateDescision[id]", candidateTags.getID());
        body.put("old_value",candidateTags.getCandidateDecisionReason());
        body.put("tag_decisioning","none");

        if(candidateTags.getCandidateDecisionType().equals(CandidateTags.candidateDecisionType.CUSTOMTAGS))
             body.put("tag_decisioning","change");


        body.putAll(candidateTags.toMap());
        doPost(url, headers, mapToFormData(body));

    }

    public void deleteCandidateTag(CandidateTags candidateTag){

        String url = getData("@@url") + "/settings/EditCandidateDecisioning";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();

        body.put("resource",candidateTag.getID());
        body.put("action","delete");

        if(candidateTag.getCandidateDecisionType().equals(CandidateTags.candidateDecisionType.CUSTOMTAGS)){
            url = getData("@@url") + "/settings/editCandidateDecisioning";
            body.put("old_value","");
            body.put("tag_decisioning","none"); //or delete
        }
        doPost(url, headers, mapToFormData(body));
    }

    }


