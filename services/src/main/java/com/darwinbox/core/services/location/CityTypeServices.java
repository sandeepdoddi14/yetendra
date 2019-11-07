package com.darwinbox.core.services.location;

import com.darwinbox.Services;
import com.darwinbox.core.employee.objects.Band;
import com.darwinbox.core.location.objects.CityType;
import com.darwinbox.core.location.objects.LocationType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CityTypeServices extends Services {

    public void createCityType(CityType cityType) {

        Map<String, String> body = cityType.toMap();
        //body.putAll(band.toMap());
        String url = getData("@@url") + "/settings/company/citytype";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(body));
        //Reporter("Band Created Successfully "+band.getBandName(),"Info");
    }

    public void updateCityType(CityType cityType) {
        Map<String, String> body = cityType.toMap();

        if(cityType.getId()!="")
        body.put("TenantCityTypes[id]",cityType.getId());


        String url = getData("@@url") + "/settings/editCityType";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(body));
        //Reporter("Band updated successfuuly"+band.getBandName(),"info");
    }


    public HashMap<String, String> getCityTypes() {
        String url = data.get("@@url") + "/settings/getCityType";

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

    public void deleteCityType(CityType cityType){

        String id=getBands().get(cityType.getCityType());

        String url = getData("@@url") + "/settings/editCityType";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",id);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));


    }



}
