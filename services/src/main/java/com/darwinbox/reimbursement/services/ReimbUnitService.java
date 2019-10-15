package com.darwinbox.reimbursement.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbUnits;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReimbUnitService extends Services {

    public Map<String, String> getAllReimbUnits() {
        Map reimbUnitsData = new HashMap<>();
        String url = getData("@@url") + "/settings/getreimbunits";

        Map headers = new HashMap<>();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);
        JSONArray jsonArray = objResponse.getJSONArray("aaData");

        for (Object object : jsonArray) {
            JSONArray data = (JSONArray) object;
            String reimbType = data.getString(0);
            String reimbId = data.getString(1).split("\" class")[0].substring(7);
            reimbUnitsData.put(reimbType, reimbId);
        }
        return reimbUnitsData;
    }

    public void createReimbUnit(ReimbUnits reimbUnits) {
        String url = getData("@@url")+ "/settings/reimbursement/reimbunits";

        Map headers = new HashMap<>();
        headers.put("x-requested-with", "XMLHttpRequest");

        String response = doPost(url, headers, reimbUnits.toMap());
        waitForUpdate(3);

    }

    public ReimbUnits getReimbUnitByName(String reUnitName)
    {
        Map<String,String> allReimbData= getAllReimbUnits();
        ReimbUnits reimbUnits = new ReimbUnits();

      for(Map.Entry<String, String> reEntry : allReimbData.entrySet())
        {
            String reimbType = reEntry.getKey();
            if(reUnitName.equalsIgnoreCase(reimbType))
            {
                reimbUnits.setId(reEntry.getValue());
                reimbUnits.setUnitType(reEntry.getKey());
                break;
            }
        }

        return  reimbUnits;
    }

    public void updateReimbUnit(ReimbUnits reimbUnits) {
        String url = getData("@@url") + "/settings/editreimbursementunit";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> obj = reimbUnits.toMap();
        obj.add(new BasicNameValuePair("reimb_id",reimbUnits.getId()));
        obj.add(new BasicNameValuePair("mode","edit"));

        String response = doPost(url, headers, obj);
        waitForUpdate(3);

        if (!response.contains("Reimbursement Unit has been updated!"))
        {
            throw new RuntimeException(" Error in updating Reimbursement unit. ");
        }
    }

    public void deleteReimbUnit(ReimbUnits reimbUnits) {
        String url = getData("@@url") + "/settings/editreimbursementunit";

        Map<String, String> body = new HashMap<>();
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> obj = reimbUnits.toMap();
        body.put("resource",reimbUnits.getId());
        body.put("mode","delete");

        String response =doPost(url, headers, mapToFormData(body));
        waitForUpdate(3);
        if (!response.contains("Reimbursement Unit has been deleted successfully."))
        {
            throw new RuntimeException(" Error in deleting Reimbursement unit. ");
        }
    }
}
