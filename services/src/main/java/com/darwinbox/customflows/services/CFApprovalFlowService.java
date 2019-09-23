package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlow;
import com.darwinbox.customflows.objects.forms.CFForm;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
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
    /**
     * method is used to create a Form can be used in Custom Flows
     * @param cfAF
     */
    public void createCFApprovalFlow(CFApprovalFlow cfAF){

        String url = getData("@@url") + "/settings/customworkflow/createapprovalflow";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doPost(url, headers, cfAF.toMap());
        waitForUpdate(3);
        if (!response.contains("Form created successfully.")) {
            throw new RuntimeException(" Error in creating Approval Flow for Custom Workflow. ");
        }

    }

}
