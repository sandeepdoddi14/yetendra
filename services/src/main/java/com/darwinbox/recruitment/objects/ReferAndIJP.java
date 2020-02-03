package com.darwinbox.recruitment.objects;

import java.util.HashMap;
import java.util.Map;

public class ReferAndIJP {


    /*Below method consists of payload to get all jobs in refer page*/
    public Map<String,String> toMap() {

        Map<String, String> body = new HashMap<>();


        body.put("draw","1");
        body.put("columns[0][data]","internal_job_code");
        body.put("columns[0][name]","");
        body.put("columns[0][searchable]","true");
        body.put("columns[0][orderable]","true");
        body.put("columns[0][search][value]","");
        body.put("columns[0][search][regex]","false");

        body.put("columns[1][data]","role");
        body.put("columns[1][name]","");
        body.put("columns[1][searchable]","true");
        body.put("columns[1][orderable]","true");
        body.put("columns[1][search][value]","");
        body.put("columns[1][search][regex]","false");

        body.put("columns[2][data]","location");
        body.put("columns[2][name]","");
        body.put("columns[2][searchable]","true");
        body.put("columns[2][orderable]","true");
        body.put("columns[2][search][value]","");
        body.put("columns[2][search][regex]","false");

        body.put("columns[3][data]","department");
        body.put("columns[3][name]","");
        body.put("columns[3][searchable]","true");
        body.put("columns[3][orderable]","true");
        body.put("columns[3][search][value]","");
        body.put("columns[3][search][regex]","false");

        body.put("columns[4][data]","employee_type");
        body.put("columns[4][name]","");
        body.put("columns[4][searchable]","true");
        body.put("columns[4][orderable]","true");
        body.put("columns[4][search][value]","");
        body.put("columns[4][search][regex]","false");

        body.put("columns[5][data]","created_on");
        body.put("columns[5][name]","");
        body.put("columns[5][searchable]","true");
        body.put("columns[5][orderable]","true");
        body.put("columns[5][search][value]","");
        body.put("columns[5][search][regex]","false");

        body.put("order[0][column]","internal_job_code");
        body.put("order[0][dir]","asc");
        body.put("start","0");
        body.put("length","10");
        body.put("search[value]","");
        body.put("search[regex]","false");

        return body;
    }


    public Map<String,String> toMapIJP() {

        Map<String, String> body = new HashMap<>();

        body.put("columns[1][orderable]","false");
        body.put("columns[2][orderable]","false");
        body.put("columns[3][orderable]","false");
        body.put("columns[4][orderable]","false");

        return body;
    }
    }
