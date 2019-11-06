package com.darwinbox.core.location.objects;

import java.util.HashMap;

public class CityType {
    private String id="";
    private String cityType="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }

    public HashMap<String,String> toMap(){
    HashMap<String,String>  map=new HashMap<>();

    map.put("TenantCityTypes[city_type_name]",getCityType());
    return  map;
    }

}
