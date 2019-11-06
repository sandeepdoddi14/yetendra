package com.darwinbox.core.location.services;

import com.darwinbox.Services;
import com.darwinbox.core.location.objects.LocationType;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationTypeServices extends Services {

    public List<LocationType> getAllLocationTypes () {

        String url = getData("@@url") + "/settings/getLocationType" ;
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        List<LocationType> locTypes = new ArrayList<>();

        if (objResponse != null && objResponse.has("aaData")) {

            JSONArray objarr = objResponse.getJSONArray("aaData");

            for (Object loctype : objarr) {

                JSONArray locarr = (JSONArray) loctype;

                String name = locarr.getString(0);
                String id = locarr.getString(1);

                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(id);
                if (m.find()) {
                    id = StringUtils.substringsBetween(m.group(0), "\"", "\"")[0];
                }

                LocationType loc = new LocationType();
                loc.setId(id);
                loc.setLocationType(name);
                locTypes.add(loc);
            }

        }

        return  locTypes;
    }

    public String getLocationTypeIdByName (String loc) {

        String url = getData("@@url") + "/settings/company/locationtype" ;
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        List<LocationType> locTypes = new ArrayList<>();
        String id = null;

        if (objResponse != null && objResponse.has("aaData")) {

            JSONArray objarr = objResponse.getJSONArray("aaData");

            for (Object loctype : objarr) {

                JSONArray locarr = (JSONArray) loctype;

                String name = locarr.getString(0);
                 if (name.equalsIgnoreCase(loc)) {
                     id = locarr.getString(1);
                    Pattern p = Pattern.compile("id=\"\\w+\"");
                    Matcher m = p.matcher(id);
                    if (m.find()) {
                        id = StringUtils.substringsBetween(m.group(0), "\"", "\"")[0];
                        break;
                    }

                }
            }

        }

        return  id;
    }

    public void createLocationType(LocationType loctype) {

        String url = getData("@@url") + "/settings/company/locationtype" ;

        Map<String,String> body = new HashMap<>();
        body.put("yt0","SAVE");
        body.putAll(loctype.toMap());

        String response = doPost(url, null, mapToFormData(body) );

        int i = 1/0;



    }

    public void deleteLocationType(LocationType loctype) {

        String url = getData("@@url") + "/settings/editLocationType";

        Map<String,String> body = new HashMap<>();
        body.put("mode","delete");
        body.put("resource",loctype.getId());

        doPost(url, null, mapToFormData(body) );

    }

    public void updateLocationType(LocationType loctype) {

        String url = getData("@@url") + "/settings/editLocationType";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        String response = doPost(url, null, mapToFormData(loctype.toMap()) );
    }


}
