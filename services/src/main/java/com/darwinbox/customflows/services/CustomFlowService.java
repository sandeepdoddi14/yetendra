package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CustomFlowService extends Services {

    //settings/getcustomflowdata

    public HashMap<String, String> getAllCustomFlows() {

        HashMap<String, String> customFlowData = new HashMap<>();
        String url = getData("@@url") + "/settings/getcustomflowdata";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);

        JSONArray jsonArry = new JSONObject(response).getJSONArray("aaData");
        String cfName = "";
        String cfVersion = "";
        String cfID = "";
        for (Object obj : jsonArry) {
            JSONArray data = (JSONArray) obj;
            cfName = data.getString(0);
            cfVersion =  data.getString(1);
            cfID = data.getString(2).split("\" class")[0].substring(7);
            customFlowData.put(cfName + "_" + cfVersion, cfID);
        }
        return customFlowData;
    }


}
