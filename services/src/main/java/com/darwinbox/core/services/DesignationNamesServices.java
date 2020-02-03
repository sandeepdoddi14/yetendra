package com.darwinbox.core.services;
import com.darwinbox.Services;
import com.darwinbox.core.employee.objects.DesignationNames;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DesignationNamesServices extends Services {

    public Map<String,String> getDefaultBody(){
        Map<String,String> body = new HashMap<>();

        body.put("UserDesignationNames[name]","");

        return  body;
    }


    public void createDesignationName(DesignationNames designationNames) {

        Map<String, String> body = getDefaultBody();

        body.putAll(designationNames.toMap());

        String url = getData("@@url") + "/settings/company/designationnames";

        doPost(url, null, mapToFormData(body));

    }

  /*  public void updateDesignationName(DesignationNames designationName) {
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
*/
    public JSONArray getAllDesignationNames() {

        String url = getData("@@url") + "/settings/getDesignationName";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        return objResponse.getJSONArray("aaData");


    }


    public DesignationNames getDesignationNamesID(String designationName) {

        DesignationNames designationNames = new DesignationNames();
        JSONArray arr = getAllDesignationNames();

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;

            designationNames.setDesignationName(objarr.getString(0));
            if (designationName.equalsIgnoreCase(designationNames.getDesignationName())) {
                String ab[]=objarr.getString(1).split("\"");
                designationNames.setId(ab[1]); //split
            }
        }
        return designationNames;
    }


  /*  public void deleteDesignationName(DesignationNames designationNames){

            String designationID=getDesignationNames().get(designationNames.getDesignationName());

            String url = getData("@@url") + "/settings/editDesignationName";

            Map headers = new HashMap();
            headers.put("X-Requested-With", "XMLHttpRequest");

            HashMap<String,String> body= new HashMap();

            body.put("resource",designationID);
            body.put("mode","delete");


            doPost(url,headers,mapToFormData(body));


    }

*/
}


