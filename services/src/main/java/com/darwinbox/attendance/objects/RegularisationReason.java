package com.darwinbox.attendance.objects;

import java.util.HashMap;
import java.util.Map;

public class RegularisationReason {

    public String name;
    public String id;
    public String count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    //below method is for Create reason
    public Map<String, String> toMap(){

        Map<String, String> body = new HashMap<>();

        body.put("id","");
        body.put("AttendanceEditReason[value]",getName());
        body.put("AttendanceEditReason[limit]",getCount());

        return body;
    }

    //Below method is for edit reasons
    public Map<String, String> getMap(){

        Map<String, String> body = new HashMap<>();

        body.put("id",getId());
        body.put("AttendanceEditReason[value]",getName());
        body.put("AttendanceEditReason[limit]",getCount());

        return body;
    }

}
