package com.darwinbox.core.services;
import com.darwinbox.Services;
import com.darwinbox.core.employee.objects.DesignationNames;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DesignationNamesServices extends Services {

    public Map<String,String> getDefaultBody(){
        Map<String,String> body = new HashMap<>();

        body.put("UserDesignationNames[name]","");

        return  body;
    }


    public void createDesignationName(DesignationNames designationNames) {

        Map<String, String> body = getDefaultBody();

        body.putAll(designationNames.toMap());


        String url = getData("@@url") + "/company/designationnames";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));

    }

    public void updateDesignationName(DesignationNames designationName) {
        Map<String, String> body =getDefaultBody();
        body.putAll(designationName.toMap());


        HashMap<String,String> designationNames=getDesignationNames();
        String  id=designationNames.get(designationName.getDesignationName());

        if(id!=null){
            body.put("UserDesignationNames[id]",id);
        }
        else
            throw new RuntimeException("There is no designation to update designation Name="+designationName.getDesignationName());

        String url = getData("@@url") + "/settings/editdesignationname";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }

    /*
   gets Designation Names
    */
    public HashMap<String, String> getDesignationNames() {
        String url = data.get("@@url") + "/settings/getDesignationName";

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



    public void deleteDesignationName(DesignationNames designationNames){

            String designationID=getDesignationNames().get(designationNames.getDesignationName());

            String url = getData("@@url") + "/settings/editDesignationName";

            Map headers = new HashMap();
            headers.put("X-Requested-With", "XMLHttpRequest");

            HashMap<String,String> body= new HashMap();

            body.put("resource",designationID);
            body.put("mode","delete");


            doPost(url,headers,mapToFormData(body));


    }


}


