package com.darwinbox.core.employee.objects;

import com.darwinbox.attendance.objects.Employee;

import java.util.HashMap;
import java.util.Map;

public class Band {
    private String id;

    private  String bandName;
    private  String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String,String> toMap(){

        Map<String,String> data = new HashMap<>();

        data.put("UserBand[band_name]",getBandName());

        if(getDescription()!=null)
        data.put("UserBand[descriptions]", getDescription());

        return data;
    }
}
