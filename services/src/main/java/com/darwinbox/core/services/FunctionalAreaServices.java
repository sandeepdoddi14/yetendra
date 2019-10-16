package com.darwinbox.core.services;
import com.darwinbox.core.Services;
import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.employee.objects.FunctionalArea;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FunctionalAreaServices extends Services {

    public Map<String,String> getDefault(){
        Map<String,String> body = new HashMap<>();

        body.put("FunctionalArea[name]","");
        body.put("FunctionalArea[description]","Automation Created Functional Area");
        body.put("FunctionalArea[code]","");
        body.put("FunctionalArea[cost_center]","");

        return  body;
    }


    public void createFunctionalArea(FunctionalArea functionalArea) {

        Map<String, String> body = getDefault();

        body.putAll(functionalArea.toMap());


        String url = getData("@@url") + "/settings/company/functionalarea";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));

    }

    public void updateFunctionalArea(FunctionalArea functionalArea) {
        Map<String, String> body = getDefault();
        body.putAll(functionalArea.toMap());


        HashMap<String,String> functionalAreas=getFunctionalAreas();
        String  id=functionalAreas.get(functionalArea.getFunctionalAreaName());

        if(id!=null){
            body.put("FunctionalArea[id]",id);
            body.put("FunctionalArea[effective_from]",functionalArea.getEffectiveDate());

        }

        else
            throw new RuntimeException("There is no Functional Area to update Functional Name="+functionalArea.getFunctionalAreaName());

        String url = getData("@@url") + "/settings/editFunctionalArea";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }

    /*
    gets Functional Area
    */
    public HashMap<String, String> getFunctionalAreas() {
        String url = data.get("@@url") + "/settings/GetFunctionalAreas";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");
        int i = 0;
        HashMap<String, String> ids = new HashMap();
        while (i < arr.length()) {
            //Pattern p = Pattern.compile("id=\"\\w+\"");
            String grade_name = arr.getJSONArray(i).getString(0);
            String value = arr.getJSONArray(i).getString(1).substring(7, 20);

            //  if (m.find()) {
            ids.put(grade_name, value);
            //  } else {
            //    ids.put(arr.getJSONArray(i).getString(0), "");
            // }
            i++;
        }
        return ids;
    }


}


