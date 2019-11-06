package com.darwinbox.attendance.services.settings;

import com.darwinbox.attendance.objects.RegularisationReason;
import com.darwinbox.Services;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

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
        Map<String, String> data = new HashMap<>();


        List<RegularisationReason> reasonList = new ArrayList<>();

        for (Object reason : reasons) {

            JSONArray arrObj = new JSONArray(reason);
            log.info("done");

            RegularisationReason regularisationReason=new RegularisationReason();

            regularisationReason.setName(arrObj.getString(1));
            regularisationReason.setCount(arrObj.getString(2));
            regularisationReason.setId(arrObj.getString(3).split("reference_id=\"")[1].split("\"")[0].trim());

            reasonList.add(regularisationReason);

        }

        return reasonList;
    }

    public void createReasons(RegularisationReason reasonObj){

        String url = getData("@@url") + "/attendance/AttendanceRegulariseReasons/editreason";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(reasonObj.toMap()));

    }

    public void deleteReasons(RegularisationReason reasonObj){

        String url = getData("@@url") + "/attendance/AttendanceRegulariseReasons/editreason";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.put("id",reasonObj.getId());
        body.put("mode","delete");

        doGet(url, headers);
    }


    public void editReasons(RegularisationReason reasonObj){

        String url = getData("@@url") + "/attendance/AttendanceRegulariseReasons/editreason";
        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        doPost(url, headers, mapToFormData(reasonObj.getMap()));


    }
}
