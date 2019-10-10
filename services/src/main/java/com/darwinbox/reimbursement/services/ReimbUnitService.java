package com.darwinbox.reimbursement.services;

import com.darwinbox.attendance.services.Services;
import com.darwinbox.reimbursement.objects.ReimbCreation.ReimbForm;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class ReimbUnitService extends Services {

public HashMap<String,String> getAllReimbUnits()
{
    HashMap<String, String> reimbUnitsData = new HashMap<>();
    String url = getData("@@url" + "settings/reimbursement/reimbunits");
    HashMap<String,String> headers = new HashMap<>();
    headers.put("x-requested-with" , "XMLHttpRequest");
    String response = doGet(url, headers);
    JSONObject objResponse = new JSONObject(response);
    JSONArray jsonArray= objResponse.getJSONArray("aaData");

    String reimbType="";
    String reimbId="";
    for(Object object : jsonArray)
    {
    JSONArray data = (JSONArray) object;
    reimbType = data.getString(0);
    reimbId = data.getString(1).split("\" class")[0].substring(7);
    reimbUnitsData.put(reimbType,reimbId);
    }
    return reimbUnitsData;
}

public void createReimbUnit(ReimbForm reimbForm)
{
    String url= getData("@@url" + "settings/reimbursement/reimbunits");
    HashMap<String,String> headers = new HashMap<>();
    headers.put("x-requested-with" , "XMLHttpRequest");
    String response = doPost(url, headers, reimbForm.toMap());
}

    public void updateReimbUnit(ReimbForm reimbForm) {
        String url = getData("@@url" + "settings/reimbursement/reimbunits");
        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-requested-with", "XMLHttpRequest");
    }
}
