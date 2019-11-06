package com.darwinbox.core.services;

import com.darwinbox.Services;
import com.darwinbox.core.company.objects.Division;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DivisionServices extends Services {

    public void createDivision(Division division) {

        //Map<String, String> body = getDefaultBody();
        List<NameValuePair> body = new ArrayList<>();

        body= division.toMap();


        String url = getData("@@url") + "/settings/company/division";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, body);

    }


    public HashMap<String, String> getAllDivisions(){
        String url = data.get("@@url") + "/settings/getDivision";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(1);
            String value = arr.getJSONArray(i).getString(2).substring(7, 20);

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }


    public void updateDivision(Division division) {
        List<NameValuePair> body = new ArrayList<>();
        body=division.toMap();


       // HashMap<String,String> divisions=getAllDivisions();
        String  id=division.getId();

        if(id!=null){
            body.add(new BasicNameValuePair("TenantDivision[id]",id));
        }
        else
            throw new RuntimeException("There is no Division to update division name="+division.getDivisionName());

        String url = getData("@@url") + "/settings/editDivision";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, body);
    }


    public void deleteDivision(Division division){

        String divisionID=division.getId();

        String url = getData("@@url") + "/settings/editDivision";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",divisionID);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));


    }





}
