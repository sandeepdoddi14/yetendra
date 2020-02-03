package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.core.employee.objects.Designation;
import com.darwinbox.recruitment.objects.Designations;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;

import java.util.*;

public class DesignationsService extends Services {


    public void createDesignation(Designations designations, String staffingModel) {

        String url = "";
        Map<String, String> body = new HashMap<>();
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        if (staffingModel.contains("PositionBased")){
            url = getData("@@url") + "/Position/createPosition";
            body.putAll(designations.getDefaultForDesignation());
            String response = doPost(url, headers,mapToFormData(body));

            JSONObject objResponse = new JSONObject(response);

            if (objResponse.get("status").toString().contains("success"))
                Reporter("Designation is created", "INFO");
            else
                Reporter("Designation is NOT created","ERROR");

        }
        else {
            url = getData("@@url") + "/settings/company/designations";
            body.put("yt0","SAVE");
            body.putAll(designations.getDefaultForDesignation());
            doPost(url, headers,mapToFormData(body));

        }


    }

    /*Below method is to get a designation ID by passing designation name*/

    public Designations getDesignationsID(String name) {

        Designations designations = new Designations();
        String url = data.get("@@url") + "/settings/getdesignations";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");

        JSONObject response = new JSONObject(doGet(url, headers));
        JSONArray arr = response.getJSONArray("aaData");

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;

            designations.setDesignation(objarr.getString(0));

            if (name.equalsIgnoreCase(designations.getDesignation())) {
                designations.setJobCode(objarr.getString(2));
                String ab[]=objarr.getString(4).split("\"");
                designations.setId(ab[1]);
                designations.setJobCode(objarr.get(2).toString());
                break;
            }
        }

        return designations;
    }

    /*Below method updates designation*/

    public void updateDesignation(Designations designations) {

        String url = getData("@@url") + "/settings/editDesignation";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();

         body.put("UserDesignationsForm[id]",designations.getId());
         body.putAll(designations.getDefaultForUpdateDesignation());

        String response =doPost(url, headers, mapToFormData(body));
        Reporter("Response while updating designation is :: "+response,"INFO");

    }

    public String getDesignationID(String designationName){

        String url = getData("@@url") + "/settings/getdesignations";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

       String response = doGet(url,headers);

        JSONObject objResponse = new JSONObject(response);

        JSONArray arr =  objResponse.getJSONArray("aaData");
        String designationID="";

        for (Object obj : arr) {

            JSONArray objarr = (JSONArray) obj;
            String name= objarr.getString(0);
           if(name.contains(designationName)){
               designationID = objarr.getString(0).substring(7,20);
           }
        }

        return designationID;
        }


    /*Below method updates Position staffing model based Designation page-1*/

    public void createPositionStageOne(Designations designations, String designationID){

        String url = getData("@@url") + "/Position/createPositionStageOne";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.put("UserDesignationsPositionForm[designation_id]",designationID);
        body.putAll(designations.toMapPositionStageOne());

      String response = doPost(url, headers,mapToFormData(body));
      Reporter("Response while creating designations page-1 ::"+response,"INFO");
    }

    /*Below method updates Position staffing model based Designation page-2*/

    public void createPositionStageTwo(Designations designations, String designationID){
        String url = getData("@@url") + "/Position/createPositionStageTwo";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("UserDesignationsPositionForm[designation_id]",designationID));

        list.addAll(designations.toMapPositionStageTwo());

        String response =   doPost(url, headers,list);
        Reporter("Response while creating designations page-2 ::"+response,"INFO");

    }
}
