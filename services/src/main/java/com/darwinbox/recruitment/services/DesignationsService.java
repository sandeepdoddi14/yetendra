package com.darwinbox.recruitment.services;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.Designations;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DesignationsService extends Services {


    public void createDesignation(Designations designations, String staffingModel){

        String url="";
        if(staffingModel.contains("PositionBased"))
             url = getData("@@url") + "/Position/createPosition";
        else
             url = getData("@@url") + "/settings/company/designations";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.putAll(designations.getDefaultForDesignation());
       // driver.navigate().refresh();
        String response = doPost(url, headers,mapToFormData(body));

        JSONObject objResponse = new JSONObject(response);

           if (objResponse.get("status").toString().contains("success"))
               Reporter("Designation is created", "INFO");
           else
               Reporter("Designation is NOT created","ERROR");

    }

    /*Below method is to get a designation ID by passing designation name*/

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

        doPost(url, null,mapToFormData(body));
    }

    /*Below method updates Position staffing model based Designation page-2*/

    public void createPositionStageTwo(Designations designations, String designationID){
        String url = getData("@@url") + "/Position/createPositionStageTwo";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String,String> body = new HashMap<>();
        body.put("UserDesignationsPositionForm[designation_id]",designationID);

        body.putAll(designations.toMapPositionStageTwo());

        doPost(url, null,mapToFormData(body));


        /*TODO : another parameter as number of positions to be created??
        *  (linewise) for same des  */
    }
}
