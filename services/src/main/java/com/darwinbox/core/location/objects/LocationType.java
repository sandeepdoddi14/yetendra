package com.darwinbox.core.location.objects;

import java.util.HashMap;
import java.util.Map;

public class LocationType {

    private String id = "";
    private String locationType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void toObject(String locationType){
        setLocationType(locationType);
    }

    public Map<String,String> toMap() {

        HashMap<String,String> body = new HashMap<>();
        body.put("TenantLocationTypes[location_type_name]",getLocationType());
        return body;

    }
}
