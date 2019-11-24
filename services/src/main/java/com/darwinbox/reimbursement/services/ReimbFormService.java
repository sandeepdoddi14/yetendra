package com.darwinbox.reimbursement.services;

import com.darwinbox.Services;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbFormService extends Services {

    public Map<String,String> getAllReimbForms() {
        Map reimbFormsData = new HashMap<>();
        String url = getData("@@url") + "/settings/getReimData";

        Map headers = new HashMap<>();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);
        JSONArray jsonArray = objResponse.getJSONArray("aaData");

        for (Object object : jsonArray) {
            JSONArray data = (JSONArray) object;
            String rfType = data.getString(0);
            String rfId = data.getString(2).split("\" class")[0].substring(7);
            reimbFormsData.put(rfType,rfId);
        }
        return reimbFormsData;
    }

    public ReimbForm getReimbFormIdByName(String grpCompName, String reimbname) {
        String url = getData("@@url") + "settings/getReimData";

        Map headers = new HashMap<>();
        headers.put("x-requested-with", "XMLHttpRequest");
        ReimbForm reimbForm = new ReimbForm();
        String reimbFormId = null;
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);
        JSONArray objArray = objResponse.getJSONArray("aaData");

        for (Object object : objArray) {
            JSONArray data = (JSONArray) object;
            String reimbFormName = data.getString(0);
            String gpCompName = data.getString(1);
            String reFormId = data.getString(2).split("\"class")[0].substring(7, 20);

            boolean isGpPresent = grpCompName.equalsIgnoreCase(gpCompName);
            boolean isReimFormPresent = reimbname.equalsIgnoreCase(reimbFormName);

            reimbFormId = isGpPresent && isReimFormPresent ? reFormId : null;
            if(reimbFormId == reFormId) {
                reimbForm.setId(reimbFormId);
                reimbForm.setGrpCompany(gpCompName);
                reimbForm.setName(reimbFormName);
                break;
            }
        }
        return reimbForm;
    }

    public String createReimbform(ReimbForm reimbForm) {
        String url = getData("@@url") + "/settings/reimbursement/settings";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = reimbForm.toMap("create");
        String response = doPost(url, headers, obj);
        return response;
    }

    public String updateReimbForm(ReimbForm reimbForm) {
        String url = getData("@@url") + "/settings/editreimbursement";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = reimbForm.toMap("edit");
        String response = doPost(url, headers, obj);
        return response;
    }

    public String deleteReimbForm(ReimbForm reimbForm) {
        String url = getData("@@url" + "settings/EditReimbursement");
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        Map<String, String> body = new HashMap<>();
        body.put("mode", "delete");
        body.put("resource", reimbForm.getId());

        String response = doPost(url, headers, mapToFormData(body));
        return response;
    }
}

