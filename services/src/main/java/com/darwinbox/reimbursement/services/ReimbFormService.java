package com.darwinbox.reimbursement.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbFormService extends Services {

    public ReimbForm getReimbFormIdByName(String grpCompName, String reimbname) {

        String url = getData("@@url") + "/settings/getReimData";

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
            reimbForm.setId(reimbFormId);
            reimbForm.setGrpCompany(gpCompName);
            reimbForm.setName(reimbFormName);
        }

        return reimbForm;
    }


    public String createReimbform(ReimbForm reimbForm) {
        String url = getData("@@url" + "/settings/reimbursement/settings");
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        String response = doPost(url, headers, reimbForm.toMap());
        return response;
    }

    //return response: success> returns null
    /*public ReimbForm getReimbFormIdByName(String parentCompanyName,String reimbName ) {
        ReimbForm reimbForm = null;
        if (allReimbTypesData.containsKey(reFormName)) {
            reimbForm = new ReimbForm();
            reimbForm.setName(reFormName);
            reimbForm.setId(reimbForm.getId());
        }
        return reimbForm;
    }*/

    public String updateReimbForm(ReimbForm reimbForm) {
        String url = getData("@@url") + "/settings/editreimbursement";
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        List<NameValuePair> obj = reimbForm.toMap();
        obj.add(new BasicNameValuePair("reimb_id", reimbForm.getId()));
        String response = doPost(url, headers, obj);
        waitForUpdate(3);
        return response;
    }

    public String deleteReimbForm(ReimbForm reimbForm) {
        String url = getData("@@url" + "/settings/editreimbursement");
        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");
        Map<String, String> body = new HashMap<>();
        body.put("resource", reimbForm.getId());
        body.put("mode", "delete");

        String response = doPost(url, headers, mapToFormData(body));
        waitForUpdate(3);
        return response;
    }
}

