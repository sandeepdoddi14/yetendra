package com.darwinbox.recruitment.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.framework.uiautomation.base.TestBase;
import com.darwinbox.recruitment.objects.ArchivePosition;
import com.darwinbox.recruitment.objects.CandidateTags;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArchivePositionService extends Services {


    public void createArchivePosition(ArchivePosition archivePosition){

        String url = getData("@@url") + "/settings/recruitment/archdelete";

        Map<String, String> body = new HashMap<>();
        body.putAll(archivePosition.toMap());

        doPost(url, null,mapToFormData(body));
    }

    public JSONArray getAllArchivePositions() {

        Map<String, String> data = new HashMap<>();

        String url = getData("@@url") + "/settings/GetArchieveReason";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        return objResponse.getJSONArray("aaData");
    }

    public String getArchivePositionIDByName(String reason){

        JSONArray arr = getAllArchivePositions();
        String id = null;

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;
            String name = objarr.getString(0);
            String desc = objarr.getString(1);

            if(reason.equalsIgnoreCase(name)){

                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(desc);
                if (m.find()){
                    id = StringUtils.substringsBetween(m.group(0), "\"", "\"")[0];
                }
                break;
            }
        }
        return id;
    }

    public void editArchivePosition(ArchivePosition archivePosition){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditArchieverec";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("RecruitmentArchieveReason[id]",getArchivePositionIDByName(archivePosition.getArchiveName()));
        body.putAll(archivePosition.toMap());

        doPost(url, headers, mapToFormData(body));

    }

    public void deleteArchivePosition(String ID){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditArchieverec";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("resource",getArchivePositionIDByName(ID));
        body.put("mode","delete");

        doPost(url, headers, mapToFormData(body));

    }
    }
