package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.ArchivePosition;
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

        String url = getData("@@url") + "/settings/GetArchieveReason";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        return objResponse.getJSONArray("aaData");
    }

    public ArchivePosition getArchivePositionIDByName(String reason){

        ArchivePosition archivePosition = new ArchivePosition();
        JSONArray arr = getAllArchivePositions();

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;

            archivePosition.setArchiveName(objarr.getString(0));
            archivePosition.setArchiveDescription(objarr.getString(1));

            if(reason.equalsIgnoreCase(archivePosition.getArchiveName())){

                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(archivePosition.getID());
                if (m.find()){
                    archivePosition.setID(StringUtils.substringsBetween(m.group(0), "\"", "\"")[0]);
                }
                break;
            }
        }
        return archivePosition;
    }

    public void editArchivePosition(ArchivePosition archivePosition){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditArchieverec";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("RecruitmentArchieveReason[id]",archivePosition.getID());
        body.putAll(archivePosition.toMap());

        doPost(url, headers, mapToFormData(body));

    }

    public void deleteArchivePosition(ArchivePosition archivePosition){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditArchieverec";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("resource",archivePosition.getID());
        body.put("mode","delete");

        doPost(url, headers, mapToFormData(body));

    }
    }
