package com.darwinbox.attendance.services;

import com.darwinbox.attendance.objects.WeeklyOff;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WeeklyOffService extends Services {

    public WeeklyOff getWeeklyOff(String name) {

            String url = getData("@@url") + "/settings/getWeeklyOffList";
            Map headers = new HashMap();
            headers.put("X-Requested-With", "XMLHttpRequest");
            String response = doGet(url, headers);
            JSONObject objResponse = new JSONObject(response);

            if (objResponse != null && objResponse.getString("status").equals("success")) {
                return new WeeklyOff();//objResponse.getJSONArray("update");
            } else {
                log.error("Status in Response: " + objResponse);
                throw new RuntimeException("ERROR: Unable to get shifts");
            }

    }


    public WeeklyOff createWeekOff(WeeklyOff weekOffData) {

        String url = getData("@@url") + "/weeklyOff/create";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        doPost(url, headers, mapToFormData(weekOffData.getWeekOffMapObject()));
        return getWeeklyOff(weekOffData.getWeeklyOffName());

    }
}
