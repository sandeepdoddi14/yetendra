package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.objects.RegularisationReason;
import com.darwinbox.attendance.objects.Shift;
import com.darwinbox.attendance.services.Services;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttendanceRegularisationServices extends Services {

    private JSONArray getRegularisationReasons() {
        String url = getData("@@url") + "/attendance/AttendanceRegulariseReasons/GetReasons";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");
        String response = doGet(url, headers);
        JSONObject objResponse = new JSONObject(response);

        if (objResponse == null || objResponse.get("aaData")!= null) {
            return new JSONArray();
        } else {
            log.error("Status in Response: " + objResponse);
        }

        return new JSONArray(objResponse.get("aaData"));
    }

    public List<RegularisationReason> getAllReasons() {

        JSONArray reasons = getRegularisationReasons();

        List<RegularisationReason> reasonList = new ArrayList<>();

        for (Object reason : reasons) {

            JSONArray arrObj = new JSONArray(reason);

            String name = arrObj.getString(1);
            String limit = arrObj.getString(2);
            String id = arrObj.getString(3).split("reference_id=\"")[1].split("\"")[0].trim();

            RegularisationReason reasonObj = new RegularisationReason();
            //reasonObj.setCount();


        }
        return null;
    }

}
