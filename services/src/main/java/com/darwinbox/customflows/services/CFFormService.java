package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class CFFormService extends Services{



    // get url data
    public HashMap<String, String> getAllCFFomsdata(){

        HashMap<String, String> cfCFFormsdata = new HashMap<>();
        String url = getData("@@url") + "/settings/getcustomworkflowformevaluationdata";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);
        JSONArray jsonArray = objResponse.getJSONArray("aaData");

        String cfFormName = "";
        String cfFormVersion = "";
        String cfFormID = "";

        for (Object obj : jsonArray) {

            JSONArray data = (JSONArray) obj;
            cfFormName = data.getString(0);
            cfFormVersion = data.getString(1);
            cfFormID = data.getString(2).split("\" class")[0].substring(7);
            cfCFFormsdata.put(cfFormName+"_"+cfFormVersion, cfFormID);

        }

        return cfCFFormsdata;
    }
}
