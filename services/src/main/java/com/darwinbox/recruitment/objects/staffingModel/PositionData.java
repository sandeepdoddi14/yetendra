package com.darwinbox.recruitment.objects.staffingModel;

import com.darwinbox.Services;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public class PositionData extends Services {

    private String positionID;
    private String positionStatus;
    private String positionName;

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionID() {
        return positionID;
    }

    public void setPositionID(String positionID) {
        this.positionID = positionID;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public Map<String,String> defaultToPositionData() {

        Map<String, String> body = new HashMap<>();

        body.put("draw","1");
        body.put("columns[0][data]","position_id");
        body.put("columns[0][name]","");
        body.put("columns[0][searchable]","true");
        body.put("columns[0][orderable]","true");
        body.put("columns[0][search][value]","");
        body.put("columns[0][search][regex]","false");

        body.put("columns[1][data]","manager_position");
        body.put("columns[1][name]","");
        body.put("columns[1][searchable]","true");
        body.put("columns[1][orderable]","false");
        body.put("columns[1][search][value]","");
        body.put("columns[1][search][regex]","false");

        body.put("columns[2][data]","location");
        body.put("columns[2][name]","");
        body.put("columns[2][searchable]","true");
        body.put("columns[2][orderable]","true");
        body.put("columns[2][search][value]","");
        body.put("columns[2][search][regex]","false");

        body.put("columns[3][data]","employee_type");
        body.put("columns[3][name]","");
        body.put("columns[3][searchable]","true");
        body.put("columns[3][orderable]","true");
        body.put("columns[3][search][value]","");
        body.put("columns[3][search][regex]","false");

        body.put("columns[4][data]","hrbp");
        body.put("columns[4][name]","");
        body.put("columns[4][searchable]","true");
        body.put("columns[4][orderable]","false");
        body.put("columns[4][search][value]","");
        body.put("columns[4][search][regex]","false");

        body.put("columns[5][data]","probation");
        body.put("columns[5][name]","");
        body.put("columns[5][searchable]","true");
        body.put("columns[5][orderable]","false");
        body.put("columns[5][search][value]","");
        body.put("columns[5][search][regex]","false");

        body.put("columns[6][data]","dotted_line_manager");
        body.put("columns[6][name]","");
        body.put("columns[6][searchable]","true");
        body.put("columns[6][orderable]","false");
        body.put("columns[6][search][value]","");
        body.put("columns[6][search][regex]","false");

        body.put("columns[7][data]","job_level");
        body.put("columns[7][name]","");
        body.put("columns[7][searchable]","true");
        body.put("columns[7][orderable]","false");
        body.put("columns[7][search][value]","");
        body.put("columns[7][search][regex]","false");

        body.put("columns[8][data]","neev_level");
        body.put("columns[8][name]","");
        body.put("columns[8][searchable]","true");
        body.put("columns[8][orderable]","false");
        body.put("columns[8][search][value]","");
        body.put("columns[8][search][regex]","false");

        body.put("columns[9][data]","functional_area");
        body.put("columns[9][name]","");
        body.put("columns[9][searchable]","true");
        body.put("columns[9][orderable]","false");
        body.put("columns[9][search][value]","");
        body.put("columns[9][search][regex]","false");

        body.put("columns[10][data]","need_to_hire");
        body.put("columns[10][name]","");
        body.put("columns[10][searchable]","true");
        body.put("columns[10][orderable]","false");
        body.put("columns[10][search][value]","");
        body.put("columns[10][search][regex]","false");

        body.put("columns[11][data]","reason");
        body.put("columns[11][name]","");
        body.put("columns[11][searchable]","true");
        body.put("columns[11][orderable]","false");
        body.put("columns[11][search][value]","");
        body.put("columns[11][search][regex]","false");

        body.put("columns[12][data]","bugeted");
        body.put("columns[12][name]","");
        body.put("columns[12][searchable]","true");
        body.put("columns[12][orderable]","false");
        body.put("columns[12][search][value]","");
        body.put("columns[12][search][regex]","false");

        body.put("columns[13][data]","position_status");
        body.put("columns[13][name]","");
        body.put("columns[13][searchable]","true");
        body.put("columns[13][orderable]","false");
        body.put("columns[13][search][value]","");
        body.put("columns[13][search][regex]","false");

        body.put("columns[14][data]","occupancy_status");
        body.put("columns[14][name]","");
        body.put("columns[14][searchable]","true");
        body.put("columns[14][orderable]","false");
        body.put("columns[14][search][value]","");
        body.put("columns[14][search][regex]","false");

        body.put("columns[15][data]","incumbent_name");
        body.put("columns[15][name]","");
        body.put("columns[15][searchable]","true");
        body.put("columns[15][orderable]","false");
        body.put("columns[15][search][value]","");
        body.put("columns[15][search][regex]","false");

        body.put("columns[16][data]","created");
        body.put("columns[16][name]","");
        body.put("columns[16][searchable]","true");
        body.put("columns[16][orderable]","false");
        body.put("columns[16][search][value]","");
        body.put("columns[16][search][regex]","false");

        body.put("columns[17][data]","effective_date");
        body.put("columns[17][name]","");
        body.put("columns[17][searchable]","true");
        body.put("columns[17][orderable]","false");
        body.put("columns[17][search][value]","");
        body.put("columns[17][search][regex]","false");

        body.put("order[0][column]","location");
        body.put("order[0][dir]","asc");
        body.put("start","0");
        body.put("length","10");
        body.put("search[value]","");
        body.put("search[regex]","false");

        return body;
}
}
