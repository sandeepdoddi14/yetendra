package com.darwinbox.recruitment.services.staffingModel;

import com.darwinbox.Services;
import com.darwinbox.recruitment.objects.staffingModel.PositionData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class PositionDataService extends Services {


    /*Below methods fetches Position ID's mongoID, on designation's page*/

    public PositionData getPositionID(String designationID, PositionData positionData){

        String url = getData("@@url") + "/position/positiondata";

        Map headers = new HashMap();
        headers.put("X-Requested-With", "XMLHttpRequest");

        Map<String, String> body = new HashMap<>();
        body.put("id",designationID);
        body.putAll(positionData.defaultToPositionData());

        String res = doPost(url, headers,mapToFormData(body));
        JSONObject obj = new JSONObject(res);
        JSONArray arr = obj.getJSONArray("data");

        String  id=arr.getJSONObject(0).optString("effective_date");
        Document doc = Jsoup.parse(id);
        String des_position=doc.body().getElementsByTag("div").attr("id");
        String[] ab= des_position.split("_");

        positionData.setPositionID(ab[1]);

        String status= arr.getJSONObject(0).optString("position_status");
        positionData.setPositionStatus(status);

        return  positionData;

    }

}
