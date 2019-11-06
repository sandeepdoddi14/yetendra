package com.darwinbox.core.services;
import com.darwinbox.Services;
import com.darwinbox.core.employee.objects.Designation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DesignationServices extends Services {



    public void createDesignation(Designation designation) {

        //Map<String, String> body = getDefaultBody();
        Map<String,String> body = new HashMap<>();

        body.putAll(designation.toMap());


        String url = getData("@@url") + "/settings/company/designations";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));

    }

    public void updateDesignation(Designation designation) {
       // Map<String, String> body =getDefaultBody();
        Map<String,String> body=new HashMap<>();
        body.putAll(designation.toMap());


        HashMap<String,String> designations=getDesignations();
        String  id=designations.get(designation.getDesignationName());

        if(id!=null){
            body.put("UserDesignationsForm[id]",id);
        }
        else
            throw new RuntimeException("There is no designation to update designation Name="+designation.getDesignationName());

        String url = getData("@@url") + "/settings/editDesignation";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
    }

    /*
   gets Designations
    */
    public HashMap<String, String> getDesignations() {
        String url = data.get("@@url") + "/settings/getdesignations";

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



    public void deleteDesignation(Designation designation){

            String designationID=getDesignations().get(designation.getDesignationName());

            String url = getData("@@url") + "/settings/editDesignationName";

            Map headers = new HashMap();
            headers.put("X-Requested-With", "XMLHttpRequest");

            HashMap<String,String> body= new HashMap();

            body.put("resource",designationID);
            body.put("mode","delete");


            doPost(url,headers,mapToFormData(body));


    }


}


