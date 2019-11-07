package com.darwinbox.customflows.services;

import com.darwinbox.Services;
import com.darwinbox.customflows.objects.customflow.CustomFlow;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomFlowService extends Services {

    //settings/getcustomflowdata

    public JSONArray getAllCustomFlows() {

        HashMap<String, String> customFlowData = new HashMap<>();
        String url = getData("@@url") + "/settings/getcustomflowdata";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);

        JSONArray jsonArry = new JSONObject(response).getJSONArray("aaData");

        return jsonArry;


    }

    /**
     * method is used to create a Form can be used in Custom Flows
     * @param customFlow
     */
    public void createCustomFlow(CustomFlow customFlow){

        String url = getData("@@url") + "/settings/customworkflow/createcustomflow";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doPost(url, headers, customFlow.toMap());
       /* waitForUpdate(3);
        if (!response.contains("Custom Flow created successfully.")) {
            throw new RuntimeException(" Error in creating Custom Flow for Custom Workflow. ");
        }*/

    }

    public void updateCustomFlow(CustomFlow customFlow){

        String url = getData("@@url") + "/settings/customworkflow/managecustomflow";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = customFlow.toMap();
        obj.add(new BasicNameValuePair("reimb_id",customFlow.getId()));
        obj.add(new BasicNameValuePair("mode","edit"));
        String response = doPost(url, headers, obj);

    }

    public void deleteCustomFlow(CustomFlow customFlow){

        String url = getData("@@url") + "/settings/settings/editcustomflow";
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = customFlow.toMap();
        obj.add(new BasicNameValuePair("resource",customFlow.getId()));
        obj.add(new BasicNameValuePair("mode","delete"));
        String response = doPost(url, headers, obj);

    }

    public String getCustomFlowDetails(String expCustomFlowName, String cfFor, String workflowName ) {

        JSONArray jsonArry = getAllCustomFlows();

        String cfName = "";
        String TriggerPoint = "";
        String cfWorkflow = "";
        String cfID = "";
        for (Object obj : jsonArry) {
            JSONArray data = (JSONArray) obj;

            cfName = data.getString(0);
            TriggerPoint = data.getString(1);
            Object cfWorkflowobj = data.get(2);
            boolean match = cfName.equalsIgnoreCase(expCustomFlowName) && TriggerPoint.contains(cfFor);


            if (workflowName.length()!= 0 && cfWorkflowobj != null) {
                match = match && (cfWorkflowobj.toString().equalsIgnoreCase(workflowName)) ;
            }

            if (match) {
                cfID = data.getString(3).split("\" class")[0].substring(7);
                return cfID;
            }
        }
        return null;
    }
}
