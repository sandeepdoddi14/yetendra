package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFApprovalFlowService extends Services {


    // get url data
    public HashMap<String, String> getAllCFApprovalflows(){

        HashMap<String, String> cfApprovalflowData = new HashMap<>();
        String url = getData("@@url") + "/settings/getapprovalFlowData";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);
        JSONArray jsonArray = objResponse.getJSONArray("aaData");

        String cfAFName = "";
        String cfAFVersion = "";
        String cfAFID = "";

        for (Object obj : jsonArray) {

            JSONArray data = (JSONArray) obj;
            cfAFName = data.getString(0);
            cfAFVersion = data.getString(1);
            cfAFID = data.getString(2).split("\" class")[0].substring(7);
            cfApprovalflowData.put(cfAFName+"_"+cfAFVersion, cfAFID);

        }

        return cfApprovalflowData;
    }
}
