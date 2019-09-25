package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlow;
import com.darwinbox.customflows.objects.workflows.CFWorkFLow;
import com.darwinbox.framework.uiautomation.Utility.UtilityHelper;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFWorkFLowService extends Services{

    // get url data
    public HashMap<String, String> getAllCFWorkflows(){

        HashMap<String, String> cfWorkflowData = new HashMap<>();
        String url = getData("@@url") + "/settings/getcustomWorkFlowevaluationData";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        JSONArray jsonArry = objResponse.getJSONArray("aaData");

        String cf_wfName = "";
        String cf_wfVersion = "";
        String cf_wfID = "";
        for (Object obj : jsonArry) {
            JSONArray data = (JSONArray) obj;
            cf_wfName = data.getString(0);
            cf_wfVersion =  data.getString(1);
            cf_wfID = data.getString(2).split("\" class")[0].substring(7);
            cfWorkflowData.put(cf_wfName + "_" + cf_wfVersion, cf_wfID);
        }


         return cfWorkflowData;
    }


    /**
     * method is used to create a Form can be used in Custom Flows
     * @param cfWF
     */
    public void createCFWorkflow(CFWorkFLow cfWF){

        String url = getData("@@url") + "/settings/customworkflow/createworkflow";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doPost(url, headers, cfWF.toMap());
       /* waitForUpdate(3);
        if (!response.contains("Form created successfully.")) {
            throw new RuntimeException(" Error in creating Approval Flow for Custom Workflow. ");
        }*/

    }


}
