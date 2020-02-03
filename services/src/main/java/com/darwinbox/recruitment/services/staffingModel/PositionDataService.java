package com.darwinbox.recruitment.services.staffingModel;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.staffingModel.PositionData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionDataService extends Services {


    /*Below methods fetches Position ID's mongoID,positionName,positionStatus, on designation's page*/

    public List<PositionData> getPositionID(String designationID, PositionData positionData){

        String url = getData("@@url") + "/position/positiondata";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.put("id",designationID);
        body.putAll(positionData.defaultToPositionData());

        String res = doPost(url, headers,mapToFormData(body));
        JSONObject obj = new JSONObject(res);
        JSONArray arr = obj.getJSONArray("data");

        List<PositionData> list = new ArrayList();

        for(int i=0;i<arr.length();i++) {

            PositionData positionData1 = new PositionData();
            String id = arr.getJSONObject(i).optString("effective_date");
            Document doc = Jsoup.parse(id);
            String des_position = doc.body().getElementsByTag("div").attr("id");
            String[] ab = des_position.split("_");

            positionData1.setPositionID(ab[1]);

            String status= arr.getJSONObject(i).optString("position_status");
            positionData1.setPositionStatus(status);

            String positionName = arr.getJSONObject(i).optString("position_id").replaceAll("\\<.*?>", "");
            positionData1.setPositionName(positionName);

            list.add(positionData1);
        }

        return  list;

    }


    /*Below method archives a position, in designations page*/


    public void archivePosition(String designationID,String positionID){


        String url = getData("@@url") + "/position/archievePosition";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.put("resource",designationID+"_"+positionID);

        String res = doPost(url, headers,mapToFormData(body));

        Reporter("Response while archiving position is ::"+res,"INFO");
    }
}
