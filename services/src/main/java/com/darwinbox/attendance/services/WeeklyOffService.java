package com.darwinbox.attendance.services;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeeklyOffService extends Services {

    public JSONArray getWeeklyOffList() {

        String url = getData("@@url") + "/settings/getWeeklyOffList";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        if (objResponse != null)
            return objResponse.getJSONArray("aaData");

        return null;
    }


    public String getWeeklyOffId(String name) {

        JSONArray weekoffs = getWeeklyOffList();
        String id = null;

        for (Object weekOff : weekoffs) {

            JSONArray obj = (JSONArray) weekOff;

            String weekoffName = obj.getString(0).replaceAll("\\<.*?>", "");

            if (weekoffName.equalsIgnoreCase(name)) {
                Pattern p = Pattern.compile("id=\"\\w+\"");
                Matcher m = p.matcher(obj.getString(2));
                if (m.find()) {
                    id = StringUtils.substringsBetween(m.group(0), "\"", "\"")[0];
                    break;
                }
            }

        }

        return id;

    }
}
