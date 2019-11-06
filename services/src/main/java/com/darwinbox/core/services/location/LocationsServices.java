package com.darwinbox.core.services.location;

import com.darwinbox.Services;
import com.darwinbox.core.location.objects.CityType;
import com.darwinbox.core.location.objects.LocationType;
import com.darwinbox.core.location.objects.Locations;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocationsServices extends Services {

    public String createLocationType(Locations locations) {

        Map<String, String> body = locations.toMap();
        //body.putAll(band.toMap());
        String url = getData("@@url") + "/settings/company/offices";

        Map headers = new HashMap();
        headers.put("x-requested-with", "XMLHttpRequest");

        return doPost(url, headers, mapToFormData(body));
        //Reporter("Band Created Successfully "+band.getBandName(),"Info");
    }

    public void updateLocation(Locations locations) {
        String url = getData("@@url") + "/settings/offices";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");


        String locationID=getLocations().get(locations);

        Map<String,String> open= new HashMap<>();

        open.put("resource",locationID);
        open.put("action","open");


        //doPost(url, headers, mapToFormData(open));


        Map<String, String> body = locations.toMap();

        if(locations.getId()!="")
            body.put("TenantOffices[id]",locations.getId());




        doPost(url, headers, mapToFormData(body));
        //Reporter("Band updated successfuuly"+band.getBandName(),"info");
    }


    public HashMap<String, String> getLocations() {
        String url = data.get("@@url") + "/settings/getOffices";

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

    public void deleteLocation(Locations locations){

        String id=getLocations().get(locations.getCityType());

        String url = getData("@@url") + "/settings/offices";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        HashMap<String,String> body= new HashMap();

        body.put("resource",id);
        body.put("mode","delete");


        doPost(url,headers,mapToFormData(body));


    }

}
