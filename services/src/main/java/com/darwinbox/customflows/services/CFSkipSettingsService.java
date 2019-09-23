package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.CFSLASettings;
import com.darwinbox.customflows.objects.CFSkipSettings;
import com.darwinbox.recruitment.objects.CandidateTags;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFSkipSettingsService extends Services {


    public HashMap<String, String> getAllCFSkipSettings() {

        HashMap<String, String> cfskipSettingsData = new HashMap<>();
        String url = getData("@@url") + "/settings/getSkipSettings";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        JSONArray jsonArry = objResponse.getJSONArray("aaData");

        String cf_skipName = "";
        String cf_skipVersion = "";
        String cf_skipID = "";
        for (Object obj : jsonArry) {
            JSONArray data = (JSONArray) obj;
            cf_skipName = data.getString(0);
            cf_skipVersion = data.getString(1);
            cf_skipID = data.getString(2).split("\" class")[0].substring(7);
            cfskipSettingsData.put(cf_skipName + "_" + cf_skipVersion, cf_skipID);
        }

        return cfskipSettingsData;
    }

    /**
     * method used to create Skip Settings for CustomFlow
     * @param cfSkipSettings
     */
    public void createCFSkipSettings(CFSkipSettings cfSkipSettings){

        String url = getData("@@url") + "/settings/editskip";
        doPost(url, null,cfSkipSettings.toMap());

    }

    /**
     * method used to update Skip Setting using Service call
     * @param cfSkipSettings
     */
    public void updateSkipSettings(CFSkipSettings cfSkipSettings){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/editskip";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("SkipSetting[name]","");
        body.put("SkipSetting[descriptions]","");
        body.put("SkipSetting[skip_conditions][]", "");
        body.put("SkipSetting[skip_conditions][]", "");
        body.put("SkipSetting[skip_roles][]", "");
        body.put("SkipSetting[initiator_skip_roles][]","");
        body.put("SkipSetting[initiator_skip_roles][]", "");
        body.put("SkipSetting[skip_output]", "");

        //body.putAll(cfSLASetting.toMap());
        doPost(url, headers, mapToFormData(body));

    }

    /**
     * methos used to delete the Skip Setting using service call
     * @param cfSkipSettings
     */
    public void deleteSkipSettings(CFSkipSettings cfSkipSettings){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/editskip";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("resource",cfSkipSettings.getId());
        body.put("mode","delete");

        doPost(url, headers, mapToFormData(body));

    }

    public String getSkipSettingByName(String expSkipName){

        HashMap<String, String> skipSettingsDatMap = getAllCFSkipSettings();
        String skipSettingID = "";

        for (Map.Entry<String, String> entry1 : skipSettingsDatMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("_")[0];
            if (expSkipName.equalsIgnoreCase(actualKey)) {
                skipSettingID = entry1.getValue();
                break;
            }
        }
        return skipSettingID;
    }


}