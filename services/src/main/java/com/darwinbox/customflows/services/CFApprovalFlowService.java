package com.darwinbox.customflows.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.customflows.objects.approvalflows.CFApprovalFlow;
import com.darwinbox.customflows.objects.forms.CFForm;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
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
            cfApprovalflowData.put(cfAFName+"#"+cfAFVersion, cfAFID);

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
       /* waitForUpdate(3);
        if (!response.contains("Form created successfully.")) {
            throw new RuntimeException(" Error in creating Approval Flow for Custom Workflow. ");
        }*/

    }

    public void updateCFApprovalFlow(CFApprovalFlow cfApprovalFlow){

        String url = getData("@@url") + "/settings/EditCustomApprovalFlow";
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = cfApprovalFlow.toMap();
        obj.add(new BasicNameValuePair("reimb_id",cfApprovalFlow.getId()));
        obj.add(new BasicNameValuePair("mode","edit"));
        String response = doPost(url, headers, obj);
    }

    /**
     * method is used to delete a ApprovalFlow in Custom Flows
     *
     * @param cfApprovalFlow
     */
    public void deleteCFApprovalFlow(CFApprovalFlow cfApprovalFlow) {
        Map<String, String> body = new HashMap<>();
        String url = getData("@@url") + "/settings/editcustomapprovalflow";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        body.put("resource",cfApprovalFlow.getId());
        body.put("mode","delete");
        doPost(url, headers, mapToFormData(body));

    }
    public String getcfApprovalFlowByName(String expcfAFName){

        HashMap<String, String> cfAFDataMap = getAllCFApprovalflows();
        String cfAFID = null;

        for (Map.Entry<String, String> entry1 : cfAFDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("#")[0];
            if (expcfAFName.equalsIgnoreCase(actualKey)) {
                cfAFID = entry1.getValue();
                break;
            }
        }
        return cfAFID;
    }

    public String getcfApprovalFlowByName(String expcfAFName, String version){

        HashMap<String, String> cfAFDataMap = getAllCFApprovalflows();
        String cfAFID = null;

        for (Map.Entry<String, String> entry1 : cfAFDataMap.entrySet()) {
            String key = entry1.getKey();
            String actualKey = key.split("#")[0];
            String actVerstion = key.split("#")[1];
            if (expcfAFName.equalsIgnoreCase(actualKey) && version.equalsIgnoreCase(actVerstion)) {
                cfAFID = entry1.getValue();
                break;
            }
        }
        return cfAFID;
    }




}
