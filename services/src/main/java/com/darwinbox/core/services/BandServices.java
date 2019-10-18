package com.darwinbox.core.services;
import com.darwinbox.core.Services;
import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.employee.objects.BusinessUnit;
import com.darwinbox.framework.uiautomation.base.TestBase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BandServices extends Services {

    public Map<String,String> getDefaultforBand(){
        Map<String,String> body = new HashMap<>();

        body.put("UserBand[band_name]","");
        body.put("UserBand[descriptions]","Automation Created Band");

        return  body;
    }


    public void createBand(Band band) {

        Map<String, String> body = getDefaultforBand();

        body.putAll(band.toMap());


        String url = getData("@@url") + "/settings/company/bands";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");


            doPost(url, headers, mapToFormData(body));
            Reporter("Band Created Successfully "+band.getBandName(),"Info");



    }

    public void updateBand(Band band) {
        Map<String, String> body = getDefaultforBand();
        body.putAll(band.toMap());


       // HashMap<String,String> bands=getBands();
        //String  id=bands.get(band.getBandName());

        //if(id!=null){
            body.put("UserBand[id]",band.getId());
        //}
        //else
          //  throw new RuntimeException("There is no band to update Band Name="+band.getBandName());

        //*/

        String url = getData("@@url") + "/settings/editBands";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
        Reporter("Band updated successfuuly"+band.getBandName(),"info");
    }

    /*
   gets bands
    */
    public HashMap<String, String> getBands() {
        String url = data.get("@@url") + "/settings/GetBands";

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


    /***YTD****/

    public Band getBand(Band band){

        String url = data.get("@@url") + "/settings/editBands";

        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Requested-With", "XMLHttpRequest");


        HashMap<String,String>  body = new HashMap<>();

        body.put("resource",getBands().get(band.getBandName()));
        body.put("mode","open");

        String response=doPost(url,headers,mapToFormData(body));


        JSONObject obj= new JSONObject(response);
            obj.getJSONArray("update");
        return null;


    }

    public void deleteBand(Band band){

        String bandID=getBands().get(band.getBandName());

        String url = getData("@@url") + "/settings/editBands";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",bandID);
        body.put("mode","delete");


       doPost(url,headers,mapToFormData(body));

       Reporter("Band deleted Successfully"+band.getBandName(),"Info");
    }


}


