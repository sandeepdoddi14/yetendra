package com.darwinbox.customflows.services;

import com.darwinbox.Services;
import com.darwinbox.customflows.objects.workflows.CFWorkFLow;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            cfWorkflowData.put(cf_wfName + "#" + cf_wfVersion, cf_wfID);
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

    public void updateCFWorkFlow(CFWorkFLow cfWorkFlow){

        String url = getData("@@url") + "/settings/EditCustomWorkFLowevaluation";
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = cfWorkFlow.toMap();
        obj.add(new BasicNameValuePair("reimb_id",cfWorkFlow.getId()));
        obj.add(new BasicNameValuePair("mode","edit"));
        String response = doPost(url, headers, obj);
    }

    /**
     * method is used to delete a WorkFlow in Custom Flows
     *
     * @param cfWorkFlow
     */
    public void deleteCFWorkFlow(CFWorkFLow cfWorkFlow) {
        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/EditCustomWorkFlowevaluation";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        body.put("resource",cfWorkFlow.getId());
        body.put("mode","delete");
        doPost(url, headers, mapToFormData(body));

    }

    public String getcfWorkFlowByName(String expcfWFName){

        HashMap<String, String> cfWFDataMap = getAllCFWorkflows();
        String cfWFID = null;

        for (Map.Entry<String, String> entry1 : cfWFDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("#")[0];
            if (expcfWFName.equalsIgnoreCase(actualKey)) {
                cfWFID = entry1.getValue();
                break;
            }
        }
        return cfWFID;
    }

    public String getcfWorkFlowByName(String expcfWFName, String version){

        HashMap<String, String> cfWFDataMap = getAllCFWorkflows();
        String cfWFID = null;

        for (Map.Entry<String, String> entry1 : cfWFDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("#")[0];
            String actVerstion = key.split("#")[1];
            if (expcfWFName.equalsIgnoreCase(actualKey) && version.equalsIgnoreCase(actVerstion)) {
                cfWFID = entry1.getValue();
                break;
            }
        }
        return cfWFID;
    }

}
