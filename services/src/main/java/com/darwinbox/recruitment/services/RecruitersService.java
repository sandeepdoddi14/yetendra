package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.Recruiters;
import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecruitersService extends Services {

    public void createCandidateTags(Recruiters recruiters){

        String url = getData("@@url") + "/settings/recruitment/recruiter";

        List<NameValuePair> list = new ArrayList<>();
        list.addAll(recruiters.toMap());

        doPost(url, null,list);
    }

    public Recruiters getRecruiterByName(String name){

        Recruiters recruiters = new Recruiters();
        String url = getData("@@url") + "/settings/getRecruitmentRecruiter";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        JSONArray arr = objResponse.getJSONArray("aaData");

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;

            recruiters.setRecruiterName(objarr.getString(0));
            recruiters.setId(objarr.getString(2));

            if(name.equalsIgnoreCase(recruiters.getRecruiterName())){

                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(recruiters.getId());

                if (m.find()){
                    recruiters.setId(StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
                }
                break;
            }
        }
        return recruiters;
    }

    public void editRecruiters(Recruiters recruiters){

        List<NameValuePair> list = new ArrayList<>();
        String url = getData("@@url") + "/settings/EditRecruitmentRecruiter";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        list.add(new BasicNameValuePair("RecruitmentRecruiters[id]",recruiters.getId()));
        list.addAll(recruiters.toMap());
        doPost(url, headers,list);
    }

    public void editRecruiterPassword(Recruiters recruiters, String newPassword){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditPasswordRecruiter";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("RecruitmentRecruiters[id]",recruiters.getId());
        body.put("RecruitmentRecruiters[recruiter_password]",newPassword);

        doPost(url, headers,mapToFormData(body));
    }

    public void deleteRecruiters(Recruiters recruiters){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditRecruitmentRecruiter";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("resource",recruiters.getId());
        body.put("mode","delete");

        doPost(url, headers, mapToFormData(body));
    }


}
