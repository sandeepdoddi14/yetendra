package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.CFSLASettings;
import com.darwinbox.recruitment.objects.ArchivePosition;
import com.darwinbox.recruitment.objects.CandidateTags;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CFSLASettingsService extends Services {


    public HashMap<String, String> getAllCFSLASettings() {

        HashMap<String, String> cfSLASettingsData = new HashMap<>();
        String url = getData("@@url") + "/settings/getSlaSettings";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        JSONArray jsonArry = objResponse.getJSONArray("aaData");

        String cf_SLAName = "";
        String cf_SLAVersion = "";
        String cf_SLAID = "";
        for (Object obj : jsonArry) {
            JSONArray data = (JSONArray) obj;
            cf_SLAName = data.getString(0);
            cf_SLAVersion = data.getString(1);
            cf_SLAID = data.getString(2).split("\" class")[0].substring(7);
            cfSLASettingsData.put(cf_SLAName + "_" + cf_SLAVersion, cf_SLAID);
        }

        return cfSLASettingsData;
    }

    /**
     * method used to create SLA Setting for CustomFlow
     * @param cfSLASettings
     */
    public void createCFSLASettings(CFSLASettings cfSLASettings){

        String url = getData("@@url") + "/settings/editsla";
        doPost(url, null,cfSLASettings.toMap());
    }

    /**
     * method used to update SLA Setting using Service call
     * @param cfSLASetting
     */
    public void updateSLASettings(CFSLASettings cfSLASetting){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/editsla";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("SlaSetting[id]",cfSLASetting.getId());
        body.put("SlaSetting[descriptions]", cfSLASetting.getDescription());
        body.put("SlaSetting[sla_duration]", cfSLASetting.getSlaDuration());
        body.put("SlaSetting[sla_breach_output]", cfSLASetting.getSlaBreachOutput());

        //body.putAll(cfSLASetting.toMap());
        doPost(url, headers, mapToFormData(body));

    }

    /**
     * methos used to delete the SLA Setting using service call
     * @param cfSLASetting
     */
    public void deleteSLASettings(CFSLASettings cfSLASetting){

        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/editsla";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        body.put("resource",cfSLASetting.getId());
        body.put("mode","delete");

        doPost(url, headers, mapToFormData(body));

    }

    public String getSlaSettingByName(String expSlaName){

        HashMap<String, String> slaSettingsDatMap = getAllCFSLASettings();
        String slaSettingID = "";
        for (Map.Entry<String, String> entry1 : slaSettingsDatMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("_")[0];
            if (expSlaName.equalsIgnoreCase(actualKey)) {
                slaSettingID = entry1.getValue();
                break;
            }
        }
        return slaSettingID;
    }

}
