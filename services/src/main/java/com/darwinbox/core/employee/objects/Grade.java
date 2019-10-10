package com.darwinbox.core.employee.objects;

import com.darwinbox.core.services.BandServices;

import java.util.HashMap;
import java.util.Map;

public class Grade {
    private String id;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    private String gradeName;
    private String description;
    private String bandName;



    public Map<String,String> toMap(){

        Map<String,String> data = new HashMap<>();

        data.put("UserGrade[grade_name]",getBandName());

        if(getDescription()!=null)
            data.put("UserGrade[descriptions]", getDescription());


        String bandId=new BandServices().getBands().get(getBandName());

        data.put("UserGrade[band_name]",bandId);

        return data;
    }

}
